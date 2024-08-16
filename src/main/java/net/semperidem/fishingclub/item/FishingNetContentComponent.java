package net.semperidem.fishingclub.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.registry.FCComponents;
import org.apache.commons.lang3.math.Fraction;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record FishingNetContentComponent(
  List<ItemStack> stacks,
  Fraction occupancy,
  Integer stackCount
) implements TooltipData {

    public static final FishingNetContentComponent DEFAULT = new FishingNetContentComponent(List.of(), Fraction.ZERO, 1);
    public static final Codec<FishingNetContentComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(FishingNetContentComponent::stacks),
      Codec.INT.fieldOf("occupancy").forGetter(o -> o.occupancy.getNumerator()),
      Codec.INT.fieldOf("occupancy").forGetter(o -> o.occupancy.getDenominator()),
        Codec.INT.fieldOf("stackCount").forGetter(FishingNetContentComponent::stackCount)
      ).apply(instance, FishingNetContentComponent::new)
    );

    public static FishingNetContentComponent of(ItemStack fishingNet) {

        int stackCount = 1;

        if (fishingNet.getItem() instanceof FishingNetItem fishingNetItem) {
            stackCount = fishingNetItem.stackCount;
        }

        FishingNetContentComponent component = fishingNet.getOrDefault(FCComponents.FISHING_NET_CONTENT, DEFAULT);
        return new FishingNetContentComponent(component.stacks, stackCount);
    }

    public FishingNetContentComponent(List<ItemStack> stacks, int numerator, int denominator, Integer size) {

        this(stacks, Fraction.getFraction(numerator, denominator), size);
    }

    public FishingNetContentComponent(List<ItemStack> stacks, Integer size) {

        this(stacks, calculateOccupancy(stacks, size), size);
    }

    public static final PacketCodec<RegistryByteBuf, FishingNetContentComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);


    private static Fraction calculateOccupancy(List<ItemStack> stacks, int stackCount) {

        Fraction fraction = Fraction.ZERO;

        for (ItemStack itemStack : stacks) {
            fraction = fraction.add(getOccupancy(itemStack, stackCount).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
        }

        return fraction;
    }

    static Fraction getOccupancy(ItemStack stack, int stackCount) {

        if (!FishUtil.isFish(stack)) {
            return Fraction.ONE;
        }

        return Fraction.getFraction(1, stack.getMaxCount() * stackCount);
    }

    public ItemStack get(int index) {

        return this.stacks.get(index);
    }

    public Stream<ItemStack> stream() {

        return this.stacks.stream().map(ItemStack::copy);
    }

    public int size() {

        return this.stacks.size();
    }

    public Fraction getOccupancy() {

        return this.occupancy;
    }

    public boolean isEmpty() {

        return this.stacks.isEmpty();
    }

    public boolean equals(Object o) {

        if (this == o) {
            return true;
        }

        if (!(o instanceof FishingNetContentComponent component)) {
            return false;
        }

        if (!this.occupancy.equals(component.occupancy)) {
            return false;
        }

        return ItemStack.stacksEqual(this.stacks, component.stacks);
    }

    public int hashCode() {

        return ItemStack.listHashCode(this.stacks);
    }

    public String toString() {

        return "FishingNetContent" + this.stacks;
    }

    public List<ItemStack> stacks() {

        return stacks;
    }

    public static class Builder {

        private final List<ItemStack> stacks;
        private Fraction occupancy;
        private final int stackCount;

        public Builder(FishingNetContentComponent base) {

            this.stacks = new ArrayList<>(base.stacks);
            this.occupancy = base.occupancy;
            this.stackCount = base.stackCount;
        }


        private int addInternal(ItemStack stack) {

            if (!stack.isStackable()) {
                return -1;
            }

            for (int i = 0; i < this.stacks.size(); i++) {
                boolean sizeLimit = this.stacks.get(i).getCount() + stack.getCount() > this.stacks.get(i).getMaxCount();
                if (ItemStack.areItemsAndComponentsEqual(this.stacks.get(i), stack) && !sizeLimit) {
                    return i;
                }
            }

            return -1;
        }

        public final int getMaxAllowed(ItemStack stack) {

            return Math.max(Fraction.ONE.subtract(this.occupancy).divideBy(FishingNetContentComponent.getOccupancy(stack, stackCount)).intValue(), 0);
        }

        public int add(ItemStack stack) {

            if (stack.isEmpty() || !stack.getItem().canBeNested()) {
                return 0;
            }
            int i = Math.min(stack.getCount(), this.getMaxAllowed(stack));
            if (i == 0) {
                return 0;
            }
            this.occupancy = this.occupancy.add(FishingNetContentComponent.getOccupancy(stack, stackCount).multiplyBy(Fraction.getFraction(i, 1)));
            int j = this.addInternal(stack);
            if (j == -1) {
                this.stacks.addFirst(stack.split(i));
                return i;
            }
            ItemStack itemStack = this.stacks.remove(j);
            ItemStack itemStack2 = itemStack.copyWithCount(itemStack.getCount() + i);
            stack.decrement(i);
            this.stacks.addFirst(itemStack2);
            return i;
        }

        public int add(Slot slot, PlayerEntity player) {

            ItemStack itemStack = slot.getStack();
            int i = this.getMaxAllowed(itemStack);
            return this.add(slot.takeStackRange(itemStack.getCount(), i, player));
        }

        @Nullable
        public ItemStack removeFirst() {

            if (this.stacks.isEmpty()) {
                return null;
            }

            ItemStack itemStack = this.stacks.removeFirst().copy();
            this.occupancy = this.occupancy.subtract(FishingNetContentComponent.getOccupancy(itemStack, stackCount).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
            return itemStack;
        }

        public FishingNetContentComponent build(int stackCount) {

            return new FishingNetContentComponent(List.copyOf(this.stacks), this.occupancy, stackCount);
        }

    }

}

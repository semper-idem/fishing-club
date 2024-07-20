package net.semperidem.fishing_club.item;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.entity.BeehiveBlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipData;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishing_club.fish.FishUtil;
import net.semperidem.fishing_club.registry.FCComponents;
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
    public static final FishingNetContentComponent DOUBLE = new FishingNetContentComponent(List.of(), Fraction.ZERO, 2);
    public static final Codec<FishingNetContentComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ItemStack.CODEC.listOf().fieldOf("stacks").forGetter(FishingNetContentComponent::stacks),
      Codec.INT.fieldOf("occupancy").forGetter(o -> o.occupancy.getDenominator()),
      Codec.INT.fieldOf("stackCount").forGetter(FishingNetContentComponent::stackCount)
      ).apply(instance, FishingNetContentComponent::new)
    );

    public FishingNetContentComponent(List<ItemStack> stacks, int denominator, Integer size) {
        this(stacks, Fraction.getFraction(1, denominator), size);
    }

    public static final PacketCodec<RegistryByteBuf, FishingNetContentComponent> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);
    private static final Fraction NESTED_BUNDLE_OCCUPANCY = Fraction.getFraction(1, 16);
    private static final int ADD_TO_NEW_SLOT = -1;


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

    public Iterable<ItemStack> iterate() {
        return this.stacks;
    }

    public Iterable<ItemStack> iterateCopy() {
        return Lists.<ItemStack, ItemStack>transform(this.stacks, ItemStack::copy);
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
        } else {
            return !(o instanceof FishingNetContentComponent FishingNetContentComponent)
                   ? false
                   : this.occupancy.equals(FishingNetContentComponent.occupancy) && ItemStack.stacksEqual(this.stacks, FishingNetContentComponent.stacks);
        }
    }

    public int hashCode() {
        return ItemStack.listHashCode(this.stacks);
    }

    public String toString() {
        return "BundleContents" + this.stacks;
    }

    public List<ItemStack> stacks() {
        return stacks;
    }

    public static class Builder {
        private final List<ItemStack> stacks;
        private Fraction occupancy;
        private int stackCount;

        public Builder(FishingNetContentComponent base) {
            this.stacks = new ArrayList(base.stacks);
            this.occupancy = base.occupancy;
            this.stackCount = base.stackCount;
        }

        public FishingNetContentComponent.Builder clear() {
            this.stacks.clear();
            this.occupancy = Fraction.ZERO;
            return this;
        }

        private int addInternal(ItemStack stack) {
            if (!stack.isStackable()) {
                return -1;
            } else {
                for (int i = 0; i < this.stacks.size(); i++) {
                    boolean sizeLimit = this.stacks.get(i).getCount() + stack.getCount() > this.stacks.get(i).getMaxCount();
                    if (ItemStack.areItemsAndComponentsEqual((ItemStack)this.stacks.get(i), stack) && !sizeLimit) {
                        return i;
                    }
                }

                return -1;
            }
        }

        public final int getMaxAllowed(ItemStack stack) {
            Fraction fraction = Fraction.ONE.subtract(this.occupancy);
            return Math.max(fraction.divideBy(FishingNetContentComponent.getOccupancy(stack, stackCount)).intValue(), 0);
        }

        public int add(ItemStack stack) {
            if (!stack.isEmpty() && stack.getItem().canBeNested()) {
                int i = Math.min(stack.getCount(), this.getMaxAllowed(stack));
                if (i == 0) {
                    return 0;
                } else {
                    this.occupancy = this.occupancy.add(FishingNetContentComponent.getOccupancy(stack, stackCount).multiplyBy(Fraction.getFraction(i, 1)));
                    int j = this.addInternal(stack);
                    if (j != -1) {
                        ItemStack itemStack = (ItemStack)this.stacks.remove(j);
                        ItemStack itemStack2 = itemStack.copyWithCount(itemStack.getCount() + i);
                        stack.decrement(i);
                        this.stacks.add(0, itemStack2);
                    } else {
                        this.stacks.add(0, stack.split(i));
                    }

                    return i;
                }
            } else {
                return 0;
            }
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
            } else {
                ItemStack itemStack = ((ItemStack)this.stacks.remove(0)).copy();
                this.occupancy = this.occupancy.subtract(FishingNetContentComponent.getOccupancy(itemStack, stackCount).multiplyBy(Fraction.getFraction(itemStack.getCount(), 1)));
                return itemStack;
            }
        }

        public Fraction getOccupancy() {
            return this.occupancy;
        }

        public FishingNetContentComponent build(int stackCount) {
            return new FishingNetContentComponent(List.copyOf(this.stacks), this.occupancy, stackCount);
        }
    }
}

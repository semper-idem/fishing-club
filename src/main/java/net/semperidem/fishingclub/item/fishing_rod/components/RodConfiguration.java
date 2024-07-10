package net.semperidem.fishingclub.item.fishing_rod.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.registry.FCItems;

import java.util.Optional;

public record RodConfiguration(
        Optional<ItemStack> core,
        Optional<ItemStack> line,
        int weightCapacity,
        int weightMagnitude,
        int maxLineLength,
        float castPower,
        boolean canCast
) {

    private static RodConfiguration DEFAULT;
    public static final RodConfiguration EMPTY = new RodConfiguration(
            Optional.empty(),
            Optional.empty(),
            0,
            1,
            0,
            1,
            false
    );

    public static RodConfiguration getDefault() {
        if (DEFAULT == null) {
            DEFAULT = EMPTY.equip(FCItems.CORE_WOODEN_OAK.getDefaultStack());
        }
        return DEFAULT;
    }

    public RodConfiguration(ItemStack core, ItemStack line) {
        Controller result = Controller.process(core, line);
        this(
                Optional.ofNullable(core),
                Optional.ofNullable(line),
                result.weightCapacity,
                result.weightMagnitude,
                result.maxLineLength,
                result.castPower,
                result.canCast);
    }

    public static Codec<RodConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.optionalFieldOf("core").forGetter(RodConfiguration::core),
            ItemStack.CODEC.optionalFieldOf("line").forGetter(RodConfiguration::line)
    ).apply(instance, (core, line)-> new RodConfiguration(
            core.orElse(ItemStack.EMPTY),
            line.orElse(ItemStack.EMPTY)
    )));

    public static PacketCodec<RegistryByteBuf, RodConfiguration>  PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public static RodConfiguration of(ItemStack fishingRod) {
        return fishingRod.getOrDefault(FCComponents.ROD_CONFIGURATION, getDefault());
    }

    public RodConfiguration equip(ItemStack part) {
        PartType partType = PartType.of(part);
        return new RodConfiguration(
                partType == PartType.CORE ? part : this.core.orElse(ItemStack.EMPTY),
                partType == PartType.LINE ? part : this.line.orElse(ItemStack.EMPTY)
        );
    }


    public SimpleInventory getParts() {
        SimpleInventory parts = new SimpleInventory(5) {
            @Override
            public void markDirty() {
                super.markDirty();
            }
        };
        parts.setStack(0, core.orElse(ItemStack.EMPTY));
        parts.setStack(1, line.orElse(ItemStack.EMPTY));
        parts.setStack(2, ItemStack.EMPTY);
        parts.setStack(3, ItemStack.EMPTY);
        parts.setStack(4, ItemStack.EMPTY);
        return parts;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RodConfiguration other)) {
            return false;
        }
        boolean areEqual = true;
        areEqual &= this.core.equals(other.core);
        areEqual &= this.line.equals(other.line);
        return areEqual;
    }

    public void damage(int i, PartItem.DamageSource damageSource, PlayerEntity playerOwner) {
        //todo
    }

    static class Controller {
        int weightCapacity = 0;
        int weightMagnitude = 2;
        int maxLineLength = 0;
        float castPower = 1;
        boolean canCast = false;


        public static Controller process(ItemStack core, ItemStack line) {
            Controller processor = new Controller();
            processor.canCast = processor.validateAndApply(core);
            processor.canCast &= processor.validateAndApply(line);
            return processor;
        }

        boolean validateAndApply(ItemStack part) {
            if (!part.getOrDefault(FCComponents.BROKEN, false)) {
                return false;
            }
            if (!(part.getItem() instanceof PartItem partItem)) {
                return false;
            }
            partItem.applyComponent(this);
            return true;
        }
    }
    enum PartType {
        CORE,
        LINE,
        INVALID;

        static PartType of(ItemStack part) {
            Item partItem = part.getItem();
            if (partItem instanceof CorePartItem) {
                return CORE;
            }
            if (partItem instanceof LinePartItem) {
                return LINE;
            }
            return INVALID;
        }
    }
}


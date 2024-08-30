package net.semperidem.fishingclub.item.fishing_rod.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.screen.configuration.RodInventory;

import java.util.HashSet;

public record RodConfiguration(
        ItemStack line,
        ItemStack bobber,
        ItemStack reel,
        ItemStack bait,
        ItemStack hook,
        AttributeComposite attributes
) {

    public static final RodConfiguration EMPTY = new RodConfiguration(
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            ItemStack.EMPTY,
            new AttributeComposite()
    );

    public static RodConfiguration getDefault() {
        return EMPTY;
    }

    public static RodConfiguration of(
            ItemStack line,
            ItemStack bobber,
            ItemStack reel,
            ItemStack bait,
            ItemStack hook
    ) {
        return new RodConfiguration(
                line,
                bobber,
                reel,
                bait,
                hook,
                new AttributeComposite(
                        line,
                        bobber,
                        reel,
                        bait,
                        hook
                )
        );
    }

    public static RodConfiguration valid(ItemStack core) {
        RodConfiguration configuration = of(core);
        return of(
                configuration.line,
                configuration.bobber,
                configuration.reel,
                configuration.bait,
                configuration.hook
        );
    }

    public static Codec<RodConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.fieldOf("line").forGetter(RodConfiguration::line),
            ItemStack.CODEC.fieldOf("bobber").forGetter(RodConfiguration::bobber),
            ItemStack.CODEC.fieldOf("reel").forGetter(RodConfiguration::reel),
            ItemStack.CODEC.fieldOf("bait").forGetter(RodConfiguration::bait),
            ItemStack.CODEC.fieldOf("hook").forGetter(RodConfiguration::hook)
    ).apply(instance, RodConfiguration::of));

    public static PacketCodec<RegistryByteBuf, RodConfiguration> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public static RodConfiguration of(ItemStack fishingRod) {
        return fishingRod.getOrDefault(FCComponents.ROD_CONFIGURATION, EMPTY);
    }

    public RodConfiguration equip(ItemStack part) {
        if (!(part.getItem() instanceof PartItem partItem)) {
            return this;
        }
        return switch (partItem.type) {
            case PartType.LINE -> equipLine(part);
            case PartType.BOBBER -> equipBobber(part);
            case PartType.REEL -> equipReel(part);
            case PartType.BAIT -> equipBait(part);
            case PartType.HOOK -> equipHook(part);
            default -> this;
        };
    }

    private RodConfiguration equipLine(ItemStack line) {
        return of(line, this.bobber, this.reel, this.bait, this.hook);
    }

    private RodConfiguration equipBobber(ItemStack bobber) {
        return of(this.line, bobber, this.reel, this.bait, this.hook);
    }

    private RodConfiguration equipReel(ItemStack reel) {
        return of(this.line, this.bobber, reel, this.bait, this.hook);
    }

    private RodConfiguration equipBait(ItemStack bait) {
        return of(this.line, this.bobber, this.reel, bait, this.hook);
    }

    private RodConfiguration equipHook(ItemStack hook) {
        return of(this.line, this.bobber, this.reel, this.bait, hook);
    }

    public static void dropContent(PlayerEntity holder, ItemStack core) {
        RodConfiguration rodConfiguration = core.get(FCComponents.ROD_CONFIGURATION);
        if (rodConfiguration == null) {
            return;
        }
        rodConfiguration.inventory(holder).dropContent();
        core.set(FCComponents.ROD_CONFIGURATION, EMPTY);
    }

    public RodInventory inventory(PlayerEntity playerEntity) {
        RodInventory parts = new RodInventory(playerEntity);
        parts.setStack(0, reel);
        parts.setStack(1, line);
        parts.setStack(2, bobber);
        parts.setStack(3, hook);
        parts.setStack(4, bait);
        return parts;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RodConfiguration other)) {
            return false;
        }

        //fixme
        return this.line.equals(other.line);
    }


    public void damage(int amount, PartItem.DamageSource damageSource, PlayerEntity player, ItemStack fishingRod) {
        for (ItemStack part : this.attributes().parts) {
            damagePart(amount, damageSource, part, player, fishingRod);
        }
    }


    private void damagePart(int amount, PartItem.DamageSource damageSource, ItemStack partStack, PlayerEntity player, ItemStack fishingRod) {
        if (!(partStack.getItem() instanceof PartItem partItem)) {
            return;
        }
        if (partStack.getOrDefault(FCComponents.BROKEN, false)) {
            return;
        }
        partItem.damage(partStack, amount, damageSource, player, fishingRod);
        partStack.getDamage();
        partStack.getMaxDamage();
    }

    public static class AttributeComposite {

        int weightClass = 0;
        int weightMagnitude = 2;
        int maxLineLength = 0;
        float bobberControl = 0;
        float bobberControlMultiplier = 1;
        float bobberWidth = 0;
        boolean canCast = false;
        int minOperatingTemperature = -1;
        int maxOperatingTemperature = 1;
        float fishQuality = 0;
        float fishRarity = 0;
        float fishRarityMultiplier = 1;
        float fishControl = 0;
        float fishControlMultiplier = 0;
        float treasureBonus = 0;
        float treasureRarityBonus = 0;
        float timeHookedMultiplier = 1;
        float waitTimeReductionMultiplier = 1;
        float baitFailChance = 0;

        HashSet<ItemStack> parts = new HashSet<>();

        AttributeComposite() {
        }

        AttributeComposite(
                ItemStack line,
                ItemStack bobber,
                ItemStack reel,
                ItemStack bait,
                ItemStack hook
        ) {
            this.canCast = this.validateAndApply(line);
            this.validateAndApply(bobber);
            if (!this.validateAndApply(bait)) {
                this.applyNoBaitPenalty();
            }
            if (this.validateAndApply(reel)) {
                applyNoReelPenalty();
            }
            if (!this.validateAndApply(hook)) {
                this.applyNoHookPenalty();
            }
        }

        void applyNoReelPenalty() {
            this.fishControl -= ItemStat.BASE_T5.value;
        }

        void applyNoHookPenalty() {
            this.baitFailChance = 0.3f;
        }

        void applyNoBaitPenalty() {
            this.waitTimeReductionMultiplier *= ItemStat.MULTIPLIER_T025.value;
            this.timeHookedMultiplier *= ItemStat.MULTIPLIER_T05.value;
            this.fishRarity -= ItemStat.BASE_T5.value;
            this.fishRarityMultiplier *= ItemStat.MULTIPLIER_T05.value;
        }

        boolean validateAndApply(ItemStack part) {
            if (!(part.getItem() instanceof PartItem partItem)) {
                return false;
            }

            if (part.getOrDefault(FCComponents.BROKEN, false)) {
                return false;
            }

            partItem.apply(this);
            parts.add(part);
            return true;
        }


        public int weightClass() {
            return this.weightClass;
        }

        public int weightMagnitude() {
            return weightMagnitude;
        }

        public int maxLineLength() {
            return maxLineLength;
        }

        public float bobberWidth() {
            return bobberWidth;
        }

        public boolean canCast() {
            return canCast;
        }

        public int minOperatingTemperature() {
            return minOperatingTemperature;
        }

        public int maxOperatingTemperature() {
            return maxOperatingTemperature;
        }

        public float baitFailChance() {
            return this.baitFailChance;
        }

        public float fishQuality() {
            return fishQuality;
        }

        public float fishControl(ItemStack coreStack) {
            if (!(coreStack.getItem() instanceof FishingRodCoreItem coreItem)) {
               return 0;
            }
            return MathHelper.clamp(fishControl * fishControlMultiplier, 0, coreItem.fishControlCeiling.value);
        }

        public float bobberControl(ItemStack coreStack) {
            if (!(coreStack.getItem() instanceof FishingRodCoreItem coreItem)) {
                return 0;
            }
            return MathHelper.clamp(bobberControl * bobberControlMultiplier, 0, coreItem.bobberControlCeiling.value);
        }

        public float treasureBonus() {
            return this.treasureBonus;
        }

        public float treasureRarityBonus() {
            return this.treasureRarityBonus;
        }

        @Override
        public String toString() {
            return ":" +
                    ", weightCapacity=" + weightClass +
                    ", minOperatingTemperature=" + minOperatingTemperature +
                    ", maxOperatingTemperature=" + maxOperatingTemperature +
                    ", fishQuality=" + fishQuality +
                    ", weightMagnitude=" + weightMagnitude +
                    ", maxLineLength=" + maxLineLength +
                    ", bobberWidth=" + bobberWidth +
                    ", canCast=" + canCast +
                    ", fishControl=" + fishControl +
                    ", fishControlMultiplier=" + fishControlMultiplier +
                    ", treasureBonus=" + treasureBonus +
                    ", treasureRarityBonus=" + treasureRarityBonus +
                    ",";
        }

    }

    public enum PartType {
        LINE,
        BOBBER,
        REEL,
        BAIT,
        HOOK,
        INVALID;
    }

}


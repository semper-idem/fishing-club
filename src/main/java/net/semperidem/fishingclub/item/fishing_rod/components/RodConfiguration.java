package net.semperidem.fishingclub.item.fishing_rod.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.screen.configuration.RodInventory;

import java.util.Optional;

public record RodConfiguration(
        Optional<ItemStack> line,
        Optional<ItemStack> bobber,
        Optional<ItemStack> reel,
        Optional<ItemStack> bait,
        Optional<ItemStack> hook,
        AttributeComposite attributes
) {

    public static final RodConfiguration EMPTY = new RodConfiguration(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
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
                line.isEmpty() ? Optional.empty() : Optional.of(line),
                bobber.isEmpty() ? Optional.empty() : Optional.of(bobber),
                reel.isEmpty() ? Optional.empty() : Optional.of(reel),
                bait.isEmpty() ? Optional.empty() : Optional.of(bait),
                hook.isEmpty() ? Optional.empty() : Optional.of(hook),
                new AttributeComposite(
                        line,
                        bobber,
                        reel,
                        bait,
                        hook
                )
        );
    }

    public static Codec<RodConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.optionalFieldOf("line").forGetter(RodConfiguration::line),
            ItemStack.CODEC.optionalFieldOf("bobber").forGetter(RodConfiguration::bobber),
            ItemStack.CODEC.optionalFieldOf("reel").forGetter(RodConfiguration::reel),
            ItemStack.CODEC.optionalFieldOf("bait").forGetter(RodConfiguration::bait),
            ItemStack.CODEC.optionalFieldOf("hook").forGetter(RodConfiguration::hook)
    ).apply(instance, (line, bobber, reel, bait, hook) -> of(
            line.orElse(ItemStack.EMPTY),
            bobber.orElse(ItemStack.EMPTY),
            reel.orElse(ItemStack.EMPTY),
            bait.orElse(ItemStack.EMPTY),
            hook.orElse(ItemStack.EMPTY)
    )));

    public static PacketCodec<RegistryByteBuf, RodConfiguration> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public static RodConfiguration of(ItemStack fishingRod) {
        return fishingRod.getOrDefault(FCComponents.ROD_CONFIGURATION, EMPTY);
    }

    public RodConfiguration unEquip(PartType partType) {
        return switch (partType) {
            case PartType.LINE -> equipLine(ItemStack.EMPTY);
            case PartType.BOBBER -> equipBobber(ItemStack.EMPTY);
            case PartType.REEL -> equipReel(ItemStack.EMPTY);
            case PartType.BAIT -> equipBait(ItemStack.EMPTY);
            case PartType.HOOK -> equipHook(ItemStack.EMPTY);
            default -> this;
        };
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
        return of(line, this.bobber.orElse(ItemStack.EMPTY), this.reel.orElse(ItemStack.EMPTY), this.bait.orElse(ItemStack.EMPTY), this.hook.orElse(ItemStack.EMPTY));
    }

    private RodConfiguration equipBobber(ItemStack bobber) {
        return of(this.line.orElse(ItemStack.EMPTY), bobber, this.reel.orElse(ItemStack.EMPTY), this.bait.orElse(ItemStack.EMPTY), this.hook.orElse(ItemStack.EMPTY));
    }

    private RodConfiguration equipReel(ItemStack reel) {
        return of(this.line.orElse(ItemStack.EMPTY), this.bobber.orElse(ItemStack.EMPTY), reel, this.bait.orElse(ItemStack.EMPTY), this.hook.orElse(ItemStack.EMPTY));
    }

    private RodConfiguration equipBait(ItemStack bait) {
        return of(this.line.orElse(ItemStack.EMPTY), this.bobber.orElse(ItemStack.EMPTY), this.reel.orElse(ItemStack.EMPTY), bait, this.hook.orElse(ItemStack.EMPTY));
    }

    private RodConfiguration equipHook(ItemStack hook) {
        return of(this.line.orElse(ItemStack.EMPTY), this.bobber.orElse(ItemStack.EMPTY), this.reel.orElse(ItemStack.EMPTY), this.bait.orElse(ItemStack.EMPTY), hook);
    }

    public static void dropContent(PlayerEntity holder, ItemStack core) {
        RodConfiguration configuration = core.get(FCComponents.ROD_CONFIGURATION);
        if (configuration == null) {
            return;
        }
        configuration.inventory(holder).dropContent();
        core.set(FCComponents.ROD_CONFIGURATION, EMPTY);
    }

    public static void destroyPart(ItemStack core, PartType partType) {
        RodConfiguration configuration = core.getOrDefault(FCComponents.ROD_CONFIGURATION, EMPTY);
        core.set(FCComponents.ROD_CONFIGURATION, configuration.unEquip(partType));
    }

    public RodInventory inventory(PlayerEntity playerEntity) {
        RodInventory parts = new RodInventory(playerEntity);
        parts.setStack(0, reel.orElse(ItemStack.EMPTY));
        parts.setStack(1, line.orElse(ItemStack.EMPTY));
        parts.setStack(2, bobber.orElse(ItemStack.EMPTY));
        parts.setStack(3, hook.orElse(ItemStack.EMPTY));
        parts.setStack(4, bait.orElse(ItemStack.EMPTY));
        return parts;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RodConfiguration other)) {
            return false;
        }
        return this.line.equals(other.line)
                & this.hook.equals(other.hook)
                & this.bobber.equals(other.bobber)
                & this.bait.equals(other.bait)
                & this.reel.equals(other.reel);
    }


    public void damage(int amount, PlayerEntity player, ItemStack fishingRod) {
        if (this.bait.isEmpty()) {
            return;
        }
        ItemStack baitStack = this.bait.get();
        baitStack.damage(amount, player, EquipmentSlot.MAINHAND);
        fishingRod.set(FCComponents.ROD_CONFIGURATION, this.equipBait(baitStack));
    }

    private ItemStack rodPart(PartType partType) {
       return switch (partType) {
            case PartType.LINE -> this.line().orElse(ItemStack.EMPTY);
            case BOBBER -> this.bobber().orElse(ItemStack.EMPTY);
            case REEL -> this.reel().orElse(ItemStack.EMPTY);
            case BAIT -> this.bait().orElse(ItemStack.EMPTY);
            case HOOK -> this.hook().orElse(ItemStack.EMPTY);
            case INVALID -> ItemStack.EMPTY;
        };
    }

    public int maxTemperatureOfPart(PartType partType) {
        if (this.rodPart(partType).getItem() instanceof PartItem partItem) {
            return  partItem.maxOperatingTemperature;
        }
        return this.attributes.maxOperatingTemperature;
    }

    public int minTemperatureOfPart(PartType partType) {
        if (this.rodPart(partType).getItem() instanceof PartItem partItem) {
            return partItem.minOperatingTemperature;
        }
        return this.attributes.minOperatingTemperature;
    }

    public static class AttributeComposite {

        /** DONE
         * Map to limit in kgs
         * If fish min weight > kg limit remove from pool
         * Caught fish weight must be lower the kg limit
         * */
        int weightClass = 0;

        /** DONE
         * Akin to weight class
         * Dictates relation for entities pulled by hookEntity
         * */
        int weightMagnitude = 2;

        /** DONE
         * Max range between fisher and hookEntity
         * */
        int maxLineLength = 0;

        /** DONE
         * Bobber width
         * Distance at which you take damage
         * Mouse move dead-zone
         * Fish influence on Bobber
         * */
        float bobberControl = 0;

        /** DONE
         * If rod has broken part that's "required"
         * blocks casting of rod
         * */
        boolean canCast = false;

        /** DONE
         *  If hookEntity temperature is lower than min
         *  apply freezing to player
         *  chance to break parts on reel
         * */
        int minOperatingTemperature = -2;

        /** DONE
         *  If hookEntity temperature is higher than max
         *  apply fire to player
         *  after x seconds line,bobber,hook and bait breaks
         * */
        int maxOperatingTemperature = 2;

        /** DONE
         * Increases average fish quality
         * */
        float fishQuality = 0;


        /** DONE
         * Increase average fish rarity
         * */
        float fishRarity = 0;

        /** DONE
         * Decrease damage fish deals to player
         * Slows fish base speed
         * Increase fish stamina drain
         * */
        float fishControl = 0;

        /** DONE
         * Increase chance of treasure appearing
         * */
        float treasureBonus = 0;

        /** DONE
         * Increase average grade of treasure
         * */
        float treasureRarityBonus = 0;

        /** DONE
         * Impacts period of time when fish can be pull when they bite
         * */
        float timeHookedMultiplier = 1;

        /** DONE
         * Impacts period of time when waiting for fish to bait*/
        float waitTimeReductionMultiplier = 1;


        /** DONE
         * Chance for bait to lose all of its durability
         * */
        float baitFailChance = 0;

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
            if (!this.validateAndApply(bobber)) {
                this.applyNoBobberPenalty();
            }
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

        void applyNoBobberPenalty() {
            this.bobberControl = 0;
        }

        void applyNoReelPenalty() {
            this.fishControl -= ItemStat.BASE_T5.value;
        }

        void applyNoHookPenalty() {
            this.baitFailChance = 0.5f;
        }

        void applyNoBaitPenalty() {
            this.waitTimeReductionMultiplier *= ItemStat.MULTIPLIER_T025.value;
            this.timeHookedMultiplier *= ItemStat.MULTIPLIER_T05.value;
            this.fishRarity -= ItemStat.BASE_T5.value;
        }

        boolean validateAndApply(ItemStack part) {
            if (!(part.getItem() instanceof PartItem partItem)) {
                return false;
            }

            partItem.apply(this);
            return true;
        }

        public float bobberWidth() {
            return 0.75f + this.bobberControl / (this.bobberControl + 50);
        }

        public float maxFishWeight() {
            return switch(this.weightClass) {
                case 6 -> 2500f;
                case 5 -> 500f;
                case 4 -> 100f;
                case 3 -> 25f;
                case 2 -> 12.5f;
                case 1 -> 5f;
                default -> 1;
            };
        }

        public float fishRarity() {
            return this.mapToPercent(this.fishRarity);
        }

        public float fishControl() {
            return this.mapToPercent(this.fishControl);
        }

        public int weightMagnitude() {
            return weightMagnitude;
        }

        public int maxLineLength() {
            return maxLineLength;
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

        public float treasureBonus() {
            return this.mapToPercent(this.treasureBonus);
        }

        public float treasureRarityBonus() {
            return this.mapToPercent(this.treasureRarityBonus);
        }

        public float timeHookedMultiplier() {
            return this.timeHookedMultiplier;
        }

        public float waitTimeReductionMultiplier() {
            return this.waitTimeReductionMultiplier;
        }

        private float mapToPercent(float value) {
            return value / (value + 100);
        }
        @Override
        public String toString() {
            return ":" +
                    ", weightCapacity=" + weightClass +
                    ", minOperatingTemperature=" + minOperatingTemperature +
                    ", maxOperatingTemperature=" + maxOperatingTemperature +
                    ", bobberControl=" + bobberControl +
                    ", fishControl=" + fishControl +
                    ", fishQuality=" + fishQuality +
                    ", weightMagnitude=" + weightMagnitude +
                    ", maxLineLength=" + maxLineLength +
                    ", canCast=" + canCast +
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


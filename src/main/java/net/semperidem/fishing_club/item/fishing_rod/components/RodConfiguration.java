package net.semperidem.fishing_club.item.fishing_rod.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCItems;
import net.semperidem.fishing_club.screen.configuration.RodInventory;

import java.util.HashSet;
import java.util.Optional;

public record RodConfiguration(
  Optional<ItemStack> core,
  Optional<ItemStack> line,
  Optional<ItemStack> bobber,
  Optional<ItemStack> reel,
  Optional<ItemStack> bait,
  Optional<ItemStack> hook,
  AttributeProcessor attributes
) {

    private static RodConfiguration DEFAULT;
    public static final RodConfiguration EMPTY = new RodConfiguration(
      Optional.empty(),
      Optional.empty(),
      Optional.empty(),
      Optional.empty(),
      Optional.empty(),
      Optional.empty(),
      new AttributeProcessor()
    );

    public static RodConfiguration getDefault() {
        if (DEFAULT == null) {
            DEFAULT = EMPTY.equip(FCItems.CORE_OAK_WOOD.getDefaultStack(), PartType.CORE);
        }
        return DEFAULT;
    }


    public static RodConfiguration of(ItemStack core, ItemStack line, ItemStack bobber, ItemStack reel, ItemStack bait, ItemStack hook) {
        AttributeProcessor stats = AttributeProcessor.process(core, line, bobber, reel, bait, hook);
        return new RodConfiguration(
          core.isEmpty() ? Optional.empty() : Optional.of(core),
          line.isEmpty() ? Optional.empty() : Optional.of(line),
          bobber.isEmpty() ? Optional.empty() : Optional.of(bobber),
          reel.isEmpty() ? Optional.empty() : Optional.of(reel),
          bait.isEmpty() ? Optional.empty() : Optional.of(bait),
          hook.isEmpty() ? Optional.empty() : Optional.of(hook),
          stats
        );
    }

    public static RodConfiguration valid(ItemStack fishingRod) {
        RodConfiguration configuration = of(fishingRod);
        return of(
          configuration.core.orElse(ItemStack.EMPTY),
          configuration.line.orElse(ItemStack.EMPTY),
          configuration.bobber.orElse(ItemStack.EMPTY),
          configuration.reel.orElse(ItemStack.EMPTY),
          configuration.bait.orElse(ItemStack.EMPTY),
          configuration.hook.orElse(ItemStack.EMPTY)
        );
    }

    public static Codec<RodConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
      ItemStack.CODEC.optionalFieldOf("core").forGetter(RodConfiguration::core),
      ItemStack.CODEC.optionalFieldOf("line").forGetter(RodConfiguration::line),
      ItemStack.CODEC.optionalFieldOf("bobber").forGetter(RodConfiguration::bobber),
      ItemStack.CODEC.optionalFieldOf("reel").forGetter(RodConfiguration::reel),
      ItemStack.CODEC.optionalFieldOf("bait").forGetter(RodConfiguration::bait),
      ItemStack.CODEC.optionalFieldOf("hook").forGetter(RodConfiguration::hook)
    ).apply(instance, (core, line, bobber, reel, bait, hook) -> of(
      core.orElse(ItemStack.EMPTY),
      line.orElse(ItemStack.EMPTY),
      bobber.orElse(ItemStack.EMPTY),
      reel.orElse(ItemStack.EMPTY),
      bait.orElse(ItemStack.EMPTY),
      hook.orElse(ItemStack.EMPTY)
    )));

    public static PacketCodec<RegistryByteBuf, RodConfiguration> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

    public static RodConfiguration of(ItemStack fishingRod) {
        return fishingRod.getOrDefault(FCComponents.ROD_CONFIGURATION, getDefault());
    }

    public RodConfiguration equip(ItemStack part, PartType partType) {
        return of(
          partType == PartType.CORE ? part : this.core.orElse(ItemStack.EMPTY),
          partType == PartType.LINE ? part : this.line.orElse(ItemStack.EMPTY),
          partType == PartType.BOBBER ? part : this.bobber.orElse(ItemStack.EMPTY),
          partType == PartType.REEL ? part : this.reel.orElse(ItemStack.EMPTY),
          partType == PartType.BAIT ? part : this.bait.orElse(ItemStack.EMPTY),
          partType == PartType.HOOK ? part : this.hook.orElse(ItemStack.EMPTY)
        );
    }


    public RodInventory getParts(PlayerEntity playerEntity) {
        RodInventory parts = new RodInventory(playerEntity);
        parts.setStack(0, core.orElse(ItemStack.EMPTY));
        parts.setStack(1, reel.orElse(ItemStack.EMPTY));
        parts.setStack(2, line.orElse(ItemStack.EMPTY));
        parts.setStack(3, bobber.orElse(ItemStack.EMPTY));
        parts.setStack(4, hook.orElse(ItemStack.EMPTY));
        parts.setStack(5, bait.orElse(ItemStack.EMPTY));
        return parts;
    }

    @Override
    public boolean equals(Object obj) {

        if (!(obj instanceof RodConfiguration other)) {

            return false;
        }

        //fixme
        boolean areEqual = true;
        areEqual &= this.core.equals(other.core);
        areEqual &= this.line.equals(other.line);
        return areEqual;
    }

    public void calculateRodDurability(ItemStack fishingRod) {

        float partDamagePercentage = 0;
        float maxDamage = 0;

        for(ItemStack part : this.attributes().parts) {

            partDamagePercentage += PartItem.getPartDamagePercentage(part);
            maxDamage++;
        }

        calculateRodDurability(fishingRod, partDamagePercentage / maxDamage);
    }

    public void calculateRodDurability(ItemStack fishingRod, float partDamagePercentage) {

        if (!fishingRod.isDamageable()) {
            return;
        }

        fishingRod.setDamage((int) (fishingRod.getMaxDamage() * partDamagePercentage));
    }

    public void damage(int amount, PartItem.DamageSource damageSource, PlayerEntity player, ItemStack fishingRod) {

        float partDamagePercentage = 0;
        float maxDamage = 0;

        for(ItemStack part : this.attributes().parts) {
            
            partDamagePercentage += damagePart(amount, damageSource, part, player, fishingRod);
            maxDamage++;
        }

        calculateRodDurability(fishingRod, partDamagePercentage / maxDamage);
    }


    private float damagePart(int amount, PartItem.DamageSource damageSource, ItemStack partStack, PlayerEntity player, ItemStack fishingRod) {

        if (!(partStack.getItem() instanceof PartItem partItem)) {

            return 1;
        }
        if (partStack.getOrDefault(FCComponents.BROKEN, false)) {

            return 1;
        }

        partItem.damage(partStack, amount, damageSource, player, fishingRod);
        return partStack.getDamage() * 1f / partStack.getMaxDamage();
    }

    public static class AttributeProcessor {

        int weightCapacity = 0;
        int weightMagnitude = 2;
        int maxLineLength = 0;
        float castPower = 1;
        float bobberControl = 0;
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

        AttributeProcessor() {
        }

        AttributeProcessor(ItemStack core, ItemStack line, ItemStack bobber, ItemStack reel, ItemStack bait, ItemStack hook) {
            this.canCast = this.validateAndApply(core);
            this.canCast &= this.validateAndApply(line);
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

        public static AttributeProcessor process(ItemStack core, ItemStack line, ItemStack bobber, ItemStack reel, ItemStack bait, ItemStack hook) {
            return new AttributeProcessor(core, line, bobber, reel, bait, hook);
        }

        void applyNoReelPenalty() {
            this.fishControl -= ItemStat.BASE_T5.value;
        }

        void applyNoHookPenalty() {
            this.baitFailChance = 0.3f;
        }

        void applyNoBaitPenalty() {
            this.waitTimeReductionMultiplier *= ItemStat.MULTIPLIER_T025.value;
            this.timeHookedMultiplier *=  ItemStat.MULTIPLIER_T05.value;
            this.fishRarity -= ItemStat.BASE_T5.value;
            this.fishRarityMultiplier *=  ItemStat.MULTIPLIER_T05.value;
        }

        boolean validateAndApply(ItemStack part) {

            if (part.getOrDefault(FCComponents.BROKEN, false)) {
                return false;
            }

            if (!(part.getItem() instanceof PartItem partItem)) {
                return false;
            }

            partItem.applyComponent(this);
            parts.add(part);
            return true;
        }


        public int weightCapacity() {
            return weightCapacity;
        }

        public int weightMagnitude() {
            return weightMagnitude;
        }

        public int maxLineLength() {
            return maxLineLength;
        }

        public float castPower() {
            return castPower;
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

        public float fishControl() {
            return fishControl;
        }

        public float fishControlMultiplier() {
            return fishControlMultiplier;
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
              ", weightCapacity=" + weightCapacity +
              ", minOperatingTemperature=" + minOperatingTemperature +
              ", maxOperatingTemperature=" + maxOperatingTemperature +
              ", fishQuality=" + fishQuality +
              ", weightMagnitude=" + weightMagnitude +
              ", maxLineLength=" + maxLineLength +
              ", castPower=" + castPower +
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
        CORE,
        LINE,
        BOBBER,
        REEL,
        BAIT,
        HOOK,
        INVALID;

        public static PartType of(ItemStack part) {
            Item partItem = part.getItem();

            if (partItem instanceof CorePartItem) {
                return CORE;
            }

            if (partItem instanceof LinePartItem) {
                return LINE;
            }

            if (partItem instanceof BobberPartItem) {
                return BOBBER;
            }

            if (partItem instanceof ReelPartItem) {
                return REEL;
            }

            if (partItem instanceof BaitPartItem) {
                return BAIT;
            }

            if (partItem instanceof HookPartItem) {
                return HOOK;
            }
            return INVALID;
        }
    }

}


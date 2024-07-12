package net.semperidem.fishing_club.item.fishing_rod.components;

import com.mojang.datafixers.types.templates.Hook;
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

import java.util.Optional;

public record RodConfiguration(
  Optional<ItemStack> core,
  Optional<ItemStack> line,
  Optional<ItemStack> bobber,
  Optional<ItemStack> reel,
  Optional<ItemStack> bait,
  Optional<ItemStack> hook,
  int weightCapacity,
  int weightMagnitude,
  int maxLineLength,
  float castPower,
  float bobberWidth,
  boolean canCast
) {

    private static RodConfiguration DEFAULT;
    public static final RodConfiguration EMPTY = new RodConfiguration(
      Optional.empty(),
      Optional.empty(),
      Optional.empty(),
      Optional.empty(),
      Optional.empty(),
      Optional.empty(),
      0,
      1,
      0,
      1,
      1,
      false
    );

    public static RodConfiguration getDefault() {
        if (DEFAULT == null) {
            DEFAULT = EMPTY.equip(FCItems.CORE_WOODEN_OAK.getDefaultStack(), PartType.CORE);
        }
        return DEFAULT;
    }



    public static RodConfiguration of(ItemStack core, ItemStack line, ItemStack bobber, ItemStack reel, ItemStack bait, ItemStack hook) {
        Controller result = Controller.process(core, line, bobber, reel, bait, hook);
        return new RodConfiguration(
          core.isEmpty() ? Optional.empty() : Optional.of(core),
          line.isEmpty() ? Optional.empty() : Optional.of(line),
          bobber.isEmpty() ? Optional.empty() : Optional.of(bobber),
          reel.isEmpty() ? Optional.empty() : Optional.of(reel),
          bait.isEmpty() ? Optional.empty() : Optional.of(bait),
          hook.isEmpty() ? Optional.empty() : Optional.of(hook),
          result.weightCapacity,
          result.weightMagnitude,
          result.maxLineLength,
          result.castPower,
          result.bobberWidth,
          result.canCast
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

        if (core.isPresent()) {
            partDamagePercentage += PartItem.getPartDamagePercentage(core.get());
            maxDamage++;
        }

        if (line.isPresent()) {
            partDamagePercentage += PartItem.getPartDamagePercentage(line.get());
            maxDamage++;
        }

        if (bobber.isPresent()) {
            partDamagePercentage += PartItem.getPartDamagePercentage(bobber.get());
            maxDamage++;
        }


        if (reel.isPresent()) {
            partDamagePercentage += PartItem.getPartDamagePercentage(reel.get());
            maxDamage++;
        }


        if (bait.isPresent()) {
            partDamagePercentage += PartItem.getPartDamagePercentage(bait.get());
            maxDamage++;
        }


        if (hook.isPresent()) {
            partDamagePercentage += PartItem.getPartDamagePercentage(hook.get());
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

        if (core.isPresent()) {
            partDamagePercentage += damagePart(amount, damageSource, core.get(), player, fishingRod);
            maxDamage++;
        }

        if (line.isPresent()) {
            partDamagePercentage += damagePart(amount, damageSource, line.get(), player, fishingRod);
            maxDamage++;
        }

        if (bobber.isPresent()) {
            partDamagePercentage += damagePart(amount, damageSource, bobber.get(), player, fishingRod);
            maxDamage++;
        }

        if (reel.isPresent()) {
            partDamagePercentage += damagePart(amount, damageSource, reel.get(), player, fishingRod);
            maxDamage++;
        }

        if (bait.isPresent()) {
            partDamagePercentage += damagePart(amount, damageSource, bait.get(), player, fishingRod);
            maxDamage++;
        }

        if (hook.isPresent()) {
            partDamagePercentage += damagePart(amount, damageSource, hook.get(), player, fishingRod);
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

    static class Controller {

        int weightCapacity = 0;
        int weightMagnitude = 2;
        int maxLineLength = 0;
        float castPower = 1;
        float bobberWidth = 1;
        boolean canCast = false;


        public static Controller process(ItemStack core, ItemStack line, ItemStack bobber, ItemStack reel, ItemStack bait, ItemStack hook) {

            Controller processor = new Controller();
            processor.canCast = processor.validateAndApply(core);
            processor.canCast &= processor.validateAndApply(line);
            processor.validateAndApply(bobber);
            processor.validateAndApply(reel);
            processor.validateAndApply(bait);
            processor.validateAndApply(hook);
            return processor;
        }

        boolean validateAndApply(ItemStack part) {

            if (part.getOrDefault(FCComponents.BROKEN, false)) {
                return false;
            }

            if (!(part.getItem() instanceof PartItem partItem)) {
                return false;
            }

            partItem.applyComponent(this);
            return true;
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


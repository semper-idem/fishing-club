package net.semperidem.fishing_club.item.fishing_rod.components;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishing_club.registry.FCComponents;

import java.util.HashSet;

public class PartItem extends Item {

    int weightCapacity;
    int minOperatingTemperature = 0;
    int maxOperatingTemperature = 0;
    float fishQuality = 0;
    boolean destroyOnBreak = false;


    public enum DamageSource {
        CAST(0), BITE(1), REEL_FISH(2), REEL_ENTITY(3), REEL_WATER(4), REEL_GROUND(5);

        public final int value;

        DamageSource(int value) {
            this.value = value;
        }

    }

    private static HashSet<DamageSource> TEMPERATURE_INFLUENCED_DAMAGE_SOURCES = new HashSet<>();

    static {
        TEMPERATURE_INFLUENCED_DAMAGE_SOURCES.add(DamageSource.BITE);
        TEMPERATURE_INFLUENCED_DAMAGE_SOURCES.add(DamageSource.REEL_FISH);
        TEMPERATURE_INFLUENCED_DAMAGE_SOURCES.add(DamageSource.REEL_WATER);
    }

    ;

    float[] durabilityMultiplier = {
      0.5f,
      0.5f,
      0.5f,
      0.5f,
      0.5f,
      0.5f
    };

    protected void setDamageMultiplier(DamageSource source, float value) {
        durabilityMultiplier[source.value] = value;
    }

    public float getDamageMultiplier(DamageSource source) {
        return durabilityMultiplier[source.value];
    }

    public PartItem(Settings settings, int weightCapacity, int minOperatingTemperature, int maxOperatingTemperature, float fishQuality) {

        this(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature);
        this.fishQuality = fishQuality;
    }

    public PartItem(Settings settings, int weightCapacity, int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public PartItem(Settings settings, int weightCapacity) {

        super(settings);
        this.weightCapacity = weightCapacity;
    }

    public PartItem(Settings settings) {

        super(settings);
    }

    public static float getPartDamagePercentage(ItemStack partStack) {

        return (float) partStack.getDamage() / partStack.getMaxDamage();
    }

    private boolean shouldDamage(int quality) {

        return Math.random() > MathHelper.clamp((quality - 1) * 0.25f, 0, 1);
    }

    public <T extends LivingEntity> void damage(ItemStack componentStack, int amount, DamageSource damageSource, PlayerEntity player, ItemStack fishingRod) {

        damage(componentStack,
          getTemperatureAdjustedDamage(
            damageSource,
            player,
            Math.ceil(
              getDamageMultiplier(damageSource) * amount)
          ),
          player,
          fishingRod);
    }

    int getTemperatureAdjustedDamage(DamageSource damageSource, PlayerEntity player, double amount) {

        if (!TEMPERATURE_INFLUENCED_DAMAGE_SOURCES.contains(damageSource)) {
            return (int) amount;
        }

        boolean isNegativeTemperatureArea = player.getWorld().getBiome(player.getBlockPos()).value().getTemperature() <= 0f;
        boolean isUltraWarm = player.getWorld().getDimension().ultrawarm();
        int positionTemperature = isNegativeTemperatureArea ? -1 : isUltraWarm ? 1 : 0;

        if (positionTemperature > maxOperatingTemperature) {
            int maxDiff = positionTemperature - maxOperatingTemperature;
            amount *= maxDiff * 20;
        }

        if (positionTemperature < minOperatingTemperature) {
            int minDiff = positionTemperature - minOperatingTemperature;
            amount *= minDiff * 10;
        }
        return (int) amount;
    }

    <T extends LivingEntity> void damage(ItemStack componentStack, int amount, PlayerEntity player, ItemStack fishingRod) {

        int quality = componentStack.getOrDefault(FCComponents.PART_QUALITY, 1);
        if (!shouldDamage(quality)) {
            return;
        }

        int currentDamage = componentStack.getDamage();
        if (currentDamage + amount <= componentStack.getMaxDamage()) {
            componentStack.setDamage(currentDamage + amount);
            return;
        }

        player.playSound(SoundEvents.ENTITY_ITEM_BREAK);
        if (!destroyOnBreak) {
            componentStack.set(FCComponents.BROKEN, true);
            return;
        }

        componentStack.setCount(0);
        fishingRod.set(FCComponents.ROD_CONFIGURATION, RodConfiguration.valid(fishingRod));
    }

    void applyComponent(RodConfiguration.Controller configuration) {
        configuration.fishQuality += this.fishQuality;
        validateWeightCapacity(configuration);
        validateTemperature(configuration);
    }

    public int getWeightMagnitude() {

        if (weightCapacity < 25) {
            return 1;
        }

        if (weightCapacity < 100) {
            return 0;
        }

        if (weightCapacity < 250) {
            return -1;
        }

        return -4;
    }

    void validateWeightCapacity(RodConfiguration.Controller configuration) {

        if (configuration.weightCapacity > this.weightCapacity || configuration.weightCapacity == 0) {

            configuration.weightCapacity = this.weightCapacity;
            configuration.weightMagnitude = this.getWeightMagnitude();
        }
    }

    void validateTemperature(RodConfiguration.Controller configuration) {

        if (configuration.minOperatingTemperature() < this.minOperatingTemperature) {
            configuration.minOperatingTemperature = this.minOperatingTemperature;
        }

        if (configuration.maxOperatingTemperature() > this.maxOperatingTemperature) {
            configuration.maxOperatingTemperature = this.maxOperatingTemperature;
        }

    }

}

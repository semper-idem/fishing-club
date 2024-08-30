package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.registry.FCComponents;

import java.util.HashSet;
import java.util.List;

public abstract class PartItem extends Item {
    int weightClass = 6;
    int minOperatingTemperature = 0;
    int maxOperatingTemperature = 0;
    int fishQuality = 0;

    RodConfiguration.PartType type;
    boolean destructible = false;
    float[] durabilityMultiplier = {0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f};

    public PartItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.of("Durability " + this.getRatingText(this.getDurabilityRating(stack.getMaxDamage()))));
        if (this.fishQuality != 0) {
            tooltip.add(Text.of("Quality " + (this.fishQuality > 0 ? "§a+" : "§c") + this.fishQuality));
        }
    }

    protected void setDamageMultiplier(DamageSource source, float value) {
        this.durabilityMultiplier[source.value] = value;
    }

    public float damageMultiplierForSource(DamageSource source) {
        return this.durabilityMultiplier[source.value];
    }

    private boolean shouldDamage(int quality) {
        return Math.random() > MathHelper.clamp((quality - 1) * 0.25f, 0, 1);
    }

    public void damage(ItemStack componentStack, int amount, DamageSource damageSource, PlayerEntity player, ItemStack fishingRod) {
        damage(componentStack,
                getTemperatureAdjustedDamage(
                        damageSource,
                        player,
                        Math.ceil(damageMultiplierForSource(damageSource) * amount)
                ),
                player,
                fishingRod);
    }

    int getTemperatureAdjustedDamage(DamageSource damageSource, PlayerEntity player, double amount) {

        if (!TEMPERATURE_INFLUENCED_DAMAGE_SOURCES.contains(damageSource)) {
            return (int) amount;
        }
        int positionTemperature = FishUtil.getTemperature(player.getWorld(), player.getBlockPos());

        if (positionTemperature > this.maxOperatingTemperature) {
            return (int) (amount * positionTemperature - this.maxOperatingTemperature * 20);
        }

        if (positionTemperature < this.minOperatingTemperature) {
            return (int) (amount * positionTemperature - this.minOperatingTemperature * 20);
        }
        return (int) amount;
    }

    void damage(ItemStack stack, int amount, PlayerEntity player, ItemStack fishingRod) {

        int quality = stack.getOrDefault(FCComponents.PART_QUALITY, 1);
        if (!shouldDamage(quality)) {
            return;
        }

        int currentDamage = stack.getDamage();
        if (currentDamage + amount <= stack.getMaxDamage()) {
            stack.setDamage(currentDamage + amount);
            return;
        }

        player.playSound(SoundEvents.ENTITY_ITEM_BREAK);
        if (!this.destructible) {
            stack.set(FCComponents.BROKEN, true);
            return;
        }

        stack.setCount(0);
        fishingRod.set(FCComponents.ROD_CONFIGURATION, RodConfiguration.valid(fishingRod));
    }

    void apply(RodConfiguration.AttributeComposite attributes) {
        attributes.fishQuality += this.fishQuality;
        validateWeightCapacity(attributes);
        validateTemperature(attributes);
    }

    public int getWeightMagnitude() {
        if (this.weightClass < 3) {//3
            return 1;
        }
        if (this.weightClass < 5) {
            return 0;
        }
        if (this.weightClass < 6) {
            return -1;
        }
        return -4;
    }

    void validateWeightCapacity(RodConfiguration.AttributeComposite configuration) {
        if (configuration.weightClass > this.weightClass || configuration.weightClass == 0) {
            configuration.weightClass = this.weightClass;
            configuration.weightMagnitude = this.getWeightMagnitude();
        }
    }

    void validateTemperature(RodConfiguration.AttributeComposite configuration) {
        if (configuration.minOperatingTemperature() < this.minOperatingTemperature) {
            configuration.minOperatingTemperature = this.minOperatingTemperature;
        }
        if (configuration.maxOperatingTemperature() > this.maxOperatingTemperature) {
            configuration.maxOperatingTemperature = this.maxOperatingTemperature;
        }
    }

    public int getDurabilityRating(int maxDamage) {
        int rating = 0;
        float maxDamageTrimmed = maxDamage / 10f;
        while ((maxDamageTrimmed *= 0.5f) > 1) {
            rating++;
        }
        return rating;
    }

    public String getRatingText(int rating) {
        String ratingString = "[§6";
        String value = "✦✦✦✦✦✦✦✦✦✦";
        ratingString += value.substring(0, rating) + "§r" + value.substring(rating);
        return ratingString + "]";
    }


    public enum DamageSource {
        CAST(0), BITE(1), REEL_FISH(2), REEL_ENTITY(3), REEL_WATER(4), REEL_GROUND(5);
        public final int value;

        DamageSource(int value) {
            this.value = value;
        }
    }

    private static final HashSet<DamageSource> TEMPERATURE_INFLUENCED_DAMAGE_SOURCES = new HashSet<>();

    static {
        TEMPERATURE_INFLUENCED_DAMAGE_SOURCES.add(DamageSource.BITE);
        TEMPERATURE_INFLUENCED_DAMAGE_SOURCES.add(DamageSource.REEL_FISH);
        TEMPERATURE_INFLUENCED_DAMAGE_SOURCES.add(DamageSource.REEL_WATER);
    }

    ;

}

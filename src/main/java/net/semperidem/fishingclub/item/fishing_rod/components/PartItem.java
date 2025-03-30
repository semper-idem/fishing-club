package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;

import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;

public abstract class PartItem extends Item {
    int weightClass = 6;
    int minOperatingTemperature = 0;
    int maxOperatingTemperature = 0;
    int fishQuality = 0;

    RodConfiguration.PartType type;

    public PartItem(Settings settings) {
        super(settings);
    }


    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.of("Durability " + this.getRatingText(this.getDurabilityRating(stack.getMaxDamage()))));
        if (this.fishQuality != 0) {
            tooltip.add(Text.of("Quality " + (this.fishQuality > 0 ? "§a+" : "§c") + this.fishQuality));
        }
    }

    public RodConfiguration.PartType type() {
        return this.type;
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

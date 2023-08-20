package net.semperidem.fishingclub.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.status_effects.FishFrequencyBuffStatusEffect;
import net.semperidem.fishingclub.status_effects.FishQualityBuffStatusEffect;

import java.awt.*;

public class FStatusEffectRegistry {

    public static final StatusEffect QUALITY_BUFF = new FishQualityBuffStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect FREQUENCY_BUFF = new FishFrequencyBuffStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());

    public static void register(){
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("fish_quality_buff_status_effect"), QUALITY_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("fish_frequency_buff_status_effect"), FREQUENCY_BUFF);
    }
}

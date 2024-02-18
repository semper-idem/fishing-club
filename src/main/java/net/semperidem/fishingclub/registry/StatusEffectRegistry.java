package net.semperidem.fishingclub.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.status_effects.*;

import java.awt.*;

public class StatusEffectRegistry {
    //TODO TREASURE CHANCE BUFF
    public static final StatusEffect QUALITY_BUFF = new IncreaseFishQualityStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect FREQUENCY_BUFF = new IncreaseCatchFrequencyStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect EXP_BUFF = new IncreaseFishingExpStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect SLOW_FISH_BUFF = new DecreaseFishSpeedStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect BOBBER_BUFF = new IncreaseBobberSizeStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect ONE_TIME_QUALITY_BUFF = new OneTimeIncreaseFishQualityStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());

    public static void register(){
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("quality_buff"), QUALITY_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("frequency_buff"), FREQUENCY_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("exp_buff"), EXP_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("slow_fish_buff"), SLOW_FISH_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("bobber_buff"), BOBBER_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("one_time_quality_buff"), ONE_TIME_QUALITY_BUFF);
    }
}

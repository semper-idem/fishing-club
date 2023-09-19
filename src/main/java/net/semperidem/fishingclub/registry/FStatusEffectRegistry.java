package net.semperidem.fishingclub.registry;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.status_effects.*;

import java.awt.*;

public class FStatusEffectRegistry {

    public static final StatusEffect QUALITY_BUFF = new FishQualityBuffStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect FREQUENCY_BUFF = new FishFrequencyBuffStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect EXP_BUFF = new FishExpBuffStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect SLOW_FISH_BUFF = new FishSlowBuffStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect BOBBER_BUFF = new FishBobberBuffStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect ONE_TIME_QUALITY_BUFF = new FishOneTimeQualityStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());
    public static final StatusEffect SHARED_BAIT_BUFF = new FishSharedBaitStatusEffect(StatusEffectCategory.BENEFICIAL, Color.CYAN.getRGB());

    public static void register(){
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("quality_buff"), QUALITY_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("frequency_buff"), FREQUENCY_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("exp_buff"), EXP_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("slow_fish_buff"), SLOW_FISH_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("bobber_buff"), BOBBER_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("one_time_quality_buff"), ONE_TIME_QUALITY_BUFF);
        Registry.register(Registry.STATUS_EFFECT, FishingClub.getIdentifier("shared_bait_buff"), SHARED_BAIT_BUFF);
    }
}

package net.semperidem.fishingclub.registry;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.status_effects.*;
import static net.semperidem.fishingclub.FishingClub.identifier;

public class FCStatusEffects {
    //TODO TREASURE CHANCE BUFF
    public static RegistryEntry<StatusEffect> QUALITY_BUFF;
    public static RegistryEntry<StatusEffect> FREQUENCY_BUFF ;
    public static RegistryEntry<StatusEffect> EXP_BUFF;
    public static RegistryEntry<StatusEffect> SLOW_FISH_BUFF;
    public static RegistryEntry<StatusEffect> BOBBER_BUFF;
    public static RegistryEntry<StatusEffect> ONE_TIME_QUALITY_BUFF;
    public static RegistryEntry<StatusEffect> MOISTURIZED;

    public static void register(){
        QUALITY_BUFF = registerStatusEffect(identifier("quality_buff"), new IncreaseFishQualityStatusEffect());
        FREQUENCY_BUFF = registerStatusEffect(identifier("frequency_buff"), new IncreaseCatchFrequencyStatusEffect());
        EXP_BUFF = registerStatusEffect(identifier("exp_buff"), new IncreaseFishingExpStatusEffect());
        SLOW_FISH_BUFF = registerStatusEffect(identifier("slow_fish_buff"), new DecreaseFishSpeedStatusEffect());
        BOBBER_BUFF = registerStatusEffect(identifier("bobber_buff"), new IncreaseBobberSizeStatusEffect());
        ONE_TIME_QUALITY_BUFF = registerStatusEffect(identifier("one_time_quality_buff"), new OneTimeIncreaseFishQualityStatusEffect());
        MOISTURIZED = registerStatusEffect(identifier("moisturized"), new MoisturizedStatusEffect()
            .addAttributeModifier(EntityAttributes.GENERIC_LUCK, Identifier.ofVanilla("effect.luck"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE)
        );
    }

    private static RegistryEntry<StatusEffect> registerStatusEffect(Identifier identifier, StatusEffect statusEffect ) {
        return Registry.registerReference(Registries.STATUS_EFFECT, identifier, statusEffect);
    }
}

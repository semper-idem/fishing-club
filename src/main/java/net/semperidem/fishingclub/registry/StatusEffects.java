package net.semperidem.fishingclub.registry;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.status_effects.*;
import static net.semperidem.fishingclub.FishingClub.identifier;

public class StatusEffects {
    //TODO TREASURE CHANCE BUFF
    public static RegistryEntry<StatusEffect> QUALITY_BUFF;
    public static RegistryEntry<StatusEffect> FREQUENCY_BUFF ;
    public static RegistryEntry<StatusEffect> EXP_BUFF;
    public static RegistryEntry<StatusEffect> SLOW_FISH_BUFF;
    public static RegistryEntry<StatusEffect> BOBBER_BUFF;
    public static RegistryEntry<StatusEffect> MOISTURIZED;


    public static RegistryEntry<Potion> FISHING_JUICE;
    public static RegistryEntry<Potion> LONG_FISHING_JUICE;
    public static RegistryEntry<Potion> STRONG_FISHING_JUICE;

    public static void register(){
        QUALITY_BUFF = registerStatusEffect(identifier("quality_buff"), new IncreaseFishQualityStatusEffect());
        FREQUENCY_BUFF = registerStatusEffect(identifier("frequency_buff"), new IncreaseCatchFrequencyStatusEffect());
        EXP_BUFF = registerStatusEffect(identifier("exp_buff"), new IncreaseFishingExpStatusEffect());
        SLOW_FISH_BUFF = registerStatusEffect(identifier("slow_fish_buff"), new DecreaseFishSpeedStatusEffect());
        BOBBER_BUFF = registerStatusEffect(identifier("bobber_buff"), new IncreaseBobberSizeStatusEffect());
        MOISTURIZED = registerStatusEffect(identifier("moisturized"), new MoisturizedStatusEffect()
            .addAttributeModifier(EntityAttributes.LUCK, Identifier.ofVanilla("effect.luck"), 1.0, EntityAttributeModifier.Operation.ADD_VALUE)
        );

        FISHING_JUICE = Registry.registerReference(Registries.POTION, FishingClub.identifier("fishing_juice"), new Potion("fishing_juice", new StatusEffectInstance(FREQUENCY_BUFF, 14400, 1)));
        LONG_FISHING_JUICE = Registry.registerReference(Registries.POTION, FishingClub.identifier("long_fishing_juice"), new Potion("long_fishing_juice", new StatusEffectInstance(FREQUENCY_BUFF, 28800, 1)));
        STRONG_FISHING_JUICE = Registry.registerReference(Registries.POTION, FishingClub.identifier("strong_fishing_juice"), new Potion("string_fishing_juice", new StatusEffectInstance(FREQUENCY_BUFF, 14400, 3)));

    }

    private static RegistryEntry<StatusEffect> registerStatusEffect(Identifier identifier, StatusEffect statusEffect ) {
        return Registry.registerReference(Registries.STATUS_EFFECT, identifier, statusEffect);
    }
}

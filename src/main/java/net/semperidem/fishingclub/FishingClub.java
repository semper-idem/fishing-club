package net.semperidem.fishingclub;

import net.fabricmc.api.ModInitializer;
import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;

public class FishingClub implements ModInitializer {


    private static final String LEVEL_ID = "generic.fishing_level";
    private static final String EXP_ID = "generic.fishing_exp";
    private static final float MAX_LEVEL = 100;
    private static final float MIN_LEVEL = 1;


    public static EntityAttribute FISHING_LEVEL = Registry.register(
            Registry.ATTRIBUTE, LEVEL_ID,
            (new ClampedEntityAttribute(
                    "attribute.name." + LEVEL_ID,
                    MIN_LEVEL,
                    MIN_LEVEL,
                    MAX_LEVEL
            )).setTracked(true));


    public static EntityAttribute FISHING_EXP = Registry.register(
            Registry.ATTRIBUTE, EXP_ID,
            (new ClampedEntityAttribute(
                    "attribute.name." + EXP_ID,
                    0,
                    0,
                    1000000
            )).setTracked(true));


    @Override
    public void onInitialize() {
    }
}

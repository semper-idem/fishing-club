package net.semperidem.fishing_club.fish;


import net.semperidem.fishing_club.fish.species.butterfish.ButterfishEntity;
import net.semperidem.fishing_club.fish.species.butterfish.ButterfishEntityModel;
import net.semperidem.fishing_club.fish.species.butterfish.ButterfishEntityRenderer;

import java.util.HashMap;
import java.util.Iterator;

import static net.semperidem.fishing_club.fish.MovementPatterns.MID5;

public class SpeciesLibrary {

    static HashMap<String, Species<? extends AbstractFishEntity>> ALL_FISH_TYPES = new HashMap<>();

//    public static Species COD;
    public static Species<ButterfishEntity> BUTTERFISH;
    public static Species DEFAULT;

    public static Iterator<Species<? extends AbstractFishEntity>> iterator() {
        return ALL_FISH_TYPES.values().iterator();
    }

    static {
//        COD = new Species<AbstractFishEntity>("Cod")
//            .level(0)
//            .rarity(100)
//            .length(23, 100)
//            .weight(45, 140)
//            .movement(EASY5)
//            .staminaLevel(4);

        BUTTERFISH = new Species<ButterfishEntity>("Butterfish")
            .level(10)
            .rarity(100)
            .length(20, 40)
            .weight(30, 90)
            .movement(MID5)
            .staminaLevel(1)
            .withEntity(ButterfishEntity::new)
            .withTexturedModel(ButterfishEntityModel::getTexturedModelData)
            .withRenderer(ButterfishEntityRenderer::new)
            .build();

        DEFAULT = BUTTERFISH;
    }
}

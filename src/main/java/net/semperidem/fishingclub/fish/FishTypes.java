package net.semperidem.fishingclub.fish;

import java.util.ArrayList;

public class FishTypes {
    /*
    *
            String name,
            FishPattern fishPattern,
            int fishMinLevel,
            int fishRandomLevel,
            int fishMinEnergyLevel,
            int fishMinEnergy,
            int fishRandomEnergy,
            float fishMinLength,
            float fishRandomLength,
            float fishMinWeight,
            float fishRandomWeight,
            float fishRarity)
    * */

    /*
     Fresh
    * Largemouth bass 5kg
    * Lake trout 18kg
    *
    * Ocean
    * Rainbow trout 12kg
    * */

    public static ArrayList<FishType> EASY_FISHES = new ArrayList<>();

    public static final FishType COD;
    public static final FishType L_BASS;
    public static final FishType S_BASS;
    public static final FishType SALMON;
    public static final FishType SARDINE;
    public static final FishType SHRIMP;


    static {
        COD = new FishType(
                "Cod",
            FishPatterns.EASY5,
                0,
                10,
                4,
                45,
                140,
                2,
                23,
                100
        );

        L_BASS = new FishType(
                "Largemouth Bass",
                FishPatterns.EASY1,
                10,
                15,
                4,
                30,
                45,
                0.5f,
                10,
                45
        );

        S_BASS = new FishType(
                "Smallmouth Bass",
                FishPatterns.EASY1,
                5,
                15,
                2,
                20,
                40,
                0.5f,
                4.5f,
                60
        );

        SALMON = new FishType(
                "Salmon",
                FishPatterns.EASY2,
                0,
                20,
                4,
                50,
                100,
                2,
                48,
                30
        );

        SARDINE = new FishType(
                "Sardine",
                FishPatterns.EASY4,
                0,
                15,
                1,
                13,
                17,
                0.05f,
                0.85f,
                50
        );

        SHRIMP = new FishType(
                "Shrimp",
                FishPatterns.EASY3,
                5,
                15,
                1,
                3,
                27,
                0.01f,
                0.89f,
                75
        );

        EASY_FISHES.add(COD);
        EASY_FISHES.add(SALMON);
        EASY_FISHES.add(SHRIMP);
        EASY_FISHES.add(SARDINE);
        EASY_FISHES.add(S_BASS);
        EASY_FISHES.add(L_BASS);
    }
}

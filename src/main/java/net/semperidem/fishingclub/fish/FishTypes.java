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
    public static final FishType EEL;
    public static final FishType PIKE;
    public static final FishType PUFFERFISH;
    public static final FishType RED_SNAPPER;
    public static final FishType BREAM;
    public static final FishType CARP;
    public static final FishType CATFISH;
    public static final FishType RAINBOW_TROUT;
    public static final FishType WALLEYE;

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
                55
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

        EEL = new FishType(
                "Eel",
                FishPatterns.DEFAULT,
                40,
                60,
                4,
                30,
                170,
                0.5f,
                10.5f,
                25
        );

        PIKE = new FishType(
                "Pike",
                FishPatterns.DEFAULT,
                35,
                65,
                4,
                41,
                109,
                0.5f,
                24.5f,
                25
        );

        PUFFERFISH = new FishType(
                "Pufferfish",
                FishPatterns.DEFAULT,
                30,
                70,
                3,
                2.5f,
                58.5f,
                0.05f,
                13.95f,
                35
        );

        RED_SNAPPER = new FishType(
                "Red Snapper",
                FishPatterns.DEFAULT,
                25,
                50,
                3,
                30,
                70,
                1,
                22,
                45
        );

        BREAM = new FishType(
                "Bream",
                FishPatterns.DEFAULT,
                20,
                30,
                2,
                20,
                41,
                0.5f,
                2.2f,
                45
        );

        CARP = new FishType(
                "Carp",
                FishPatterns.DEFAULT,
                15,
                35,
                3,
                30,
                122,
                1,
                26,
                55
        );

        CATFISH = new FishType(
                "Catfish",
                FishPatterns.DEFAULT,
                15,
                25,
                4,
                30,
                122,
                0.5f,
                44.5f,
                65
        );

        RAINBOW_TROUT = new FishType(
                "Rainbow Trout",
                FishPatterns.DEFAULT,
                10,
                30,
                3,
                30,
                84,
                0.5f,
                22.5f,
                65
        );

        WALLEYE = new FishType(
                "Walleye",
                FishPatterns.DEFAULT,
                10,
                30,
                2,
                30,
                61,
                0.5f,
                10.5f,
                65
        );

    }
}

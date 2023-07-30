package net.semperidem.fishingclub.client.game.fish;


public class FishTypes {
    public static FishType COD;
    public static FishType L_BASS;
    public static FishType S_BASS;
    public static FishType SALMON;
    public static FishType SARDINE;
    public static FishType SHRIMP;
    public static FishType EEL;
    public static FishType PIKE;
    public static FishType PUFFERFISH;
    public static FishType RED_SNAPPER;
    public static FishType BREAM;
    public static FishType CARP;
    public static FishType CATFISH;
    public static FishType RAINBOW_TROUT;
    public static FishType WALLEYE;

    static {
        COD = new FishType(
                "Cod",
            FishPatterns.EASY5,
                0,
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
                1,
                3,
                27,
                0.01f,
                0.89f,
                75
        );

        EEL = new FishType(
                "Eel",
                FishPatterns.HARD3,
                40,
                4,
                30,
                170,
                0.5f,
                10.5f,
                25
        );

        PIKE = new FishType(
                "Pike",
                FishPatterns.HARD2,
                35,
                4,
                41,
                109,
                0.5f,
                24.5f,
                25
        );

        PUFFERFISH = new FishType(
                "Pufferfish",
                FishPatterns.HARD1,
                30,
                3,
                2.5f,
                58.5f,
                0.05f,
                13.95f,
                35
        );

        RED_SNAPPER = new FishType(
                "Red Snapper",
                FishPatterns.MID6,
                25,
                3,
                30,
                70,
                1,
                22,
                45
        );

        BREAM = new FishType(
                "Bream",
                FishPatterns.MID5,
                20,
                2,
                20,
                41,
                0.5f,
                2.2f,
                45
        );

        CARP = new FishType(
                "Carp",
                FishPatterns.MID4,
                15,
                3,
                30,
                122,
                1,
                26,
                55
        );

        CATFISH = new FishType(
                "Catfish",
                FishPatterns.MID3,
                15,
                4,
                30,
                122,
                0.5f,
                44.5f,
                65
        );

        RAINBOW_TROUT = new FishType(
                "Rainbow Trout",
                FishPatterns.MID2,
                10,
                3,
                30,
                84,
                0.5f,
                22.5f,
                65
        );

        WALLEYE = new FishType(
                "Walleye",
                FishPatterns.MID1,
                10,
                2,
                30,
                61,
                0.5f,
                10.5f,
                65
        );

    }
}

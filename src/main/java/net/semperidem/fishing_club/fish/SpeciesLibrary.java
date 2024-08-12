package net.semperidem.fishing_club.fish;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SpeciesLibrary {

    static HashMap<String, Species> ALL_FISH_TYPES = new HashMap<>();


    public static Species COD;
    public static Species L_BASS;
    public static Species S_BASS;
    public static Species SALMON;
    public static Species SARDINE;
    public static Species SHRIMP;
    public static Species EEL;
    public static Species PIKE;
    public static Species PUFFER_FISH;
    public static Species RED_SNAPPER;
    public static Species BREAM;
    public static Species CARP;
    public static Species CATFISH;
    public static Species RAINBOW_TROUT;
    public static Species WALLEYE;
    public static Species BUTTERFISH;
    public static Species DEFAULT;

    public static ArrayList<Species> getSpeciesForLevel(int level){
        return  new ArrayList<>(
                ALL_FISH_TYPES.values().stream().filter(
                        species -> level > species.minLevel).toList()
        );
    }

    public static Iterator<Species> iterator() {
        return ALL_FISH_TYPES.values().iterator();
    }

    static {
        COD = new Species(
                "Cod",
            MovementPatterns.EASY5,
                0,
                4,
                45,
                140,
                2,
                23,
                100
        );

        BUTTERFISH = new Species(
            "Butterfish",
            MovementPatterns.MID1,
            10,
            2,
            30,
            61,
            0.5f,
            10.5f,
            65
        );

        L_BASS = new Species(
                "Largemouth Bass",
                MovementPatterns.EASY1,
                10,
                4,
                30,
                45,
                0.5f,
                10,
                45
        );

        S_BASS = new Species(
                "Smallmouth Bass",
                MovementPatterns.EASY1,
                5,
                2,
                20,
                40,
                0.5f,
                4.5f,
                60
        );

        SALMON = new Species(
                "Salmon",
                MovementPatterns.EASY2,
                0,
                4,
                50,
                100,
                2,
                48,
                55
        );

        SARDINE = new Species(
                "Sardine",
                MovementPatterns.EASY4,
                0,
                1,
                13,
                17,
                0.05f,
                0.85f,
                50
        );

        SHRIMP = new Species(
                "Shrimp",
                MovementPatterns.EASY3,
                5,
                1,
                3,
                27,
                0.01f,
                0.89f,
                75
        );

        EEL = new Species(
                "Eel",
                MovementPatterns.HARD3,
                40,
                4,
                30,
                170,
                0.5f,
                10.5f,
                25
        );

        PIKE = new Species(
                "Pike",
                MovementPatterns.HARD2,
                35,
                4,
                41,
                109,
                0.5f,
                24.5f,
                25
        );

        PUFFER_FISH = new Species(
                "Pufferfish",
                MovementPatterns.HARD1,
                30,
                3,
                2.5f,
                58.5f,
                0.05f,
                13.95f,
                35
        ).withSpawnBiome(Species.HOT);

        RED_SNAPPER = new Species(
                "Red Snapper",
                MovementPatterns.MID6,
                25,
                3,
                30,
                70,
                1,
                22,
                45
        );

        BREAM = new Species(
                "Bream",
                MovementPatterns.MID5,
                20,
                2,
                20,
                41,
                0.5f,
                2.2f,
                45
        );

        CARP = new Species(
                "Carp",
                MovementPatterns.MID4,
                15,
                3,
                30,
                122,
                1,
                26,
                55
        );

        CATFISH = new Species(
                "Catfish",
                MovementPatterns.MID3,
                15,
                4,
                30,
                122,
                0.5f,
                44.5f,
                65
        );

        RAINBOW_TROUT = new Species(
                "Rainbow Trout",
                MovementPatterns.MID2,
                10,
                3,
                30,
                84,
                0.5f,
                22.5f,
                65
        );

        WALLEYE = new Species(
          "Walleye",
          MovementPatterns.MID1,
          10,
          2,
          30,
          61,
          0.5f,
          10.5f,
          65
        );


        DEFAULT = COD;
    }
}

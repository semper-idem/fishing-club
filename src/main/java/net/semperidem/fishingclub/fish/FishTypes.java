package net.semperidem.fishingclub.fish;

public class FishTypes {
    /*
    *
            FishPattern fishPattern,
            int fishMinLevel,
            int fishMinEnergyLevel,
            int fishMinEnergy,
            float fishMinLength,
            float fishMinWeight
    * */

    /*
     Fresh
    * Largemouth bass 5kg
    * Lake trout 18kg
    *
    * Ocean
    * Rainbow trout 12kg
    * */
    public static final FishType CARP;
    static {
        CARP = new FishType(
                "Common Carp",
            FishPatterns.DEFAULT,
                0,
                1000,
                2000,
                60,
                7
        );
    }
}

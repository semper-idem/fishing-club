package net.semperidem.fishingclub.fish;

import java.util.ArrayList;

public class FishType {
    public static ArrayList<FishType> allFishTypes = new ArrayList<>();
    String name;
    FishPattern fishPattern;
    int fishMinLevel;
    int fishRandomLevel;

    int fishEnergyLevel;

    float fishMinLength;
    float fishRandomLength;

    float fishMinWeight;
    float fishRandomWeight;

    float fishRarity;

    //TODO refactor into factory
    public FishType(
            String name,
            FishPattern fishPattern,
            int fishMinLevel,
            int fishRandomLevel,
            int fishEnergyLevel,
            float fishMinLength,
            float fishRandomLength,
            float fishMinWeight,
            float fishRandomWeight,
            float fishRarity)
    {
        this.name = name;
        this.fishPattern = fishPattern;
        this.fishMinLevel = fishMinLevel;
        this.fishRandomLevel = fishRandomLevel;
        this.fishEnergyLevel = fishEnergyLevel;
        this.fishMinLength = fishMinLength;
        this.fishRandomLength = fishRandomLength;
        this.fishMinWeight = fishMinWeight;
        this.fishRandomWeight = fishRandomWeight;
        this.fishRarity = fishRarity;
        allFishTypes.add(this);
    }
}

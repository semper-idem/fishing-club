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

    //TODO sanitize input, add ranges etc etc avoid constructor param hell
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
    public FishType(
            String name,
            FishPattern fishPattern,
            int fishMinLevel,
            int fishEnergyLevel,
            float fishMinLength,
            float fishMinWeight,
            float fishRarity
            )
    {
        this.name = name;
        this.fishPattern = fishPattern;
        this.fishMinLevel = fishMinLevel;
        this.fishRandomLevel = fishMinLevel * 2 + 10;
        this.fishEnergyLevel = fishEnergyLevel;
        this.fishMinLength = fishMinLength;
        this.fishRandomLength = fishMinLength * 0.5f;
        this.fishMinWeight = fishMinWeight;
        this.fishRandomWeight = fishMinWeight * 0.5f;
        this.fishRarity = fishRarity;
    }
}

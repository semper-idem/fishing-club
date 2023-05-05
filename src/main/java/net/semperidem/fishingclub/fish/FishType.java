package net.semperidem.fishingclub.fish;

public class FishType {
    String name;
    FishPattern fishPattern;
    int fishMinLevel;
    int fishRandomLevel;

    int fishMinEnergyLevel;
    int fishMinEnergy;
    int fishRandomEnergy;

    float fishMinLength;
    float fishRandomLength;

    float fishMinWeight;
    float fishRandomWeight;

    //TODO sanitize input, add ranges etc etc avoid constructor param hell
    public FishType(
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
            float fishRandomWeight)
    {
        this.name = name;
        this.fishPattern = fishPattern;
        this.fishMinLevel = fishMinLevel;
        this.fishRandomLevel = fishRandomLevel;
        this.fishMinEnergyLevel = fishMinEnergyLevel;
        this.fishMinEnergy = fishMinEnergy;
        this.fishRandomEnergy = fishRandomEnergy;
        this.fishMinLength = fishMinLength;
        this.fishRandomLength = fishRandomLength;
        this.fishMinWeight = fishMinWeight;
        this.fishRandomWeight = fishRandomWeight;
    }
    public FishType(
            String name,
            FishPattern fishPattern,
            int fishMinLevel,
            int fishMinEnergyLevel,
            int fishMinEnergy,
            float fishMinLength,
            float fishMinWeight
            )
    {
        this.name = name;
        this.fishPattern = fishPattern;
        this.fishMinLevel = fishMinLevel;
        this.fishRandomLevel = fishMinLevel * 2 + 10;
        this.fishMinEnergyLevel = fishMinEnergyLevel;
        this.fishMinEnergy = fishMinEnergy;
        this.fishRandomEnergy = (int) (fishMinEnergy * 0.5);
        this.fishMinLength = fishMinLength;
        this.fishRandomLength = fishMinLength * 0.5f;
        this.fishMinWeight = fishMinWeight;
        this.fishRandomWeight = fishMinWeight * 0.5f;
    }
}

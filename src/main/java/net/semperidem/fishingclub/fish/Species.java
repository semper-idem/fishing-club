package net.semperidem.fishingclub.fish;

public class Species {

    public String name;

    final FishPattern fishPattern;
    final int fishMinLevel;
    final int staminaLevel;
    final float fishMinLength;
    final float fishRandomLength;
    final float fishMinWeight;
    final float fishRandomWeight;
    final float fishRarity;

    //TODO refactor into factory
    public Species(
            String name,
            FishPattern fishPattern,
            int fishMinLevel,
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
        this.staminaLevel = fishEnergyLevel;
        this.fishMinLength = fishMinLength;
        this.fishRandomLength = fishRandomLength;
        this.fishMinWeight = fishMinWeight;
        this.fishRandomWeight = fishRandomWeight;
        this.fishRarity = fishRarity;
        FishTypes.ALL_FISH_TYPES.put(name, this);
    }

    public FishPattern getFishPattern() {
        return fishPattern;
    }

    public int getStaminaLevel() {
        return staminaLevel;
    }
}

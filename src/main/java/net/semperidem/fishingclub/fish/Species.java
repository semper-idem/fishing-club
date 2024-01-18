package net.semperidem.fishingclub.fish;

public class Species {

    public String name;

    final MovementPattern fishPattern;
    final int minLevel;
    final int staminaLevel;
    final float fishMinLength;
    final float fishRandomLength;
    final float fishMinWeight;
    final float fishRandomWeight;
    final float fishRarity;

    //TODO refactor into factory
    public Species(
            String name,
            MovementPattern fishPattern,
            int minLevel,
            int fishEnergyLevel,
            float fishMinLength,
            float fishRandomLength,
            float fishMinWeight,
            float fishRandomWeight,
            float fishRarity)
    {
        this.name = name;
        this.fishPattern = fishPattern;
        this.minLevel = minLevel;
        this.staminaLevel = fishEnergyLevel;
        this.fishMinLength = fishMinLength;
        this.fishRandomLength = fishRandomLength;
        this.fishMinWeight = fishMinWeight;
        this.fishRandomWeight = fishRandomWeight;
        this.fishRarity = fishRarity;
        SpeciesLibrary.ALL_FISH_TYPES.put(name, this);
    }

    public MovementPattern getFishPattern() {
        return fishPattern;
    }

    public int getStaminaLevel() {
        return staminaLevel;
    }
}

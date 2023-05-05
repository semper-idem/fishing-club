package net.semperidem.fishingclub.fish;

import net.semperidem.fishingclub.util.Point;

public class Fish {
    FishType fishType;
    int fishLevel;
    float weight;
    float length;
    int fishEnergy;
    int fishMinEnergyLevel;
    int fishMaxEnergyLevel;
    Point[] curvePoints;
    Point[] curveControlPoints;

    public Fish(){
        this( FishTypes.CARP);
    }

    public Fish(FishType fishType){
        this.fishType = fishType;
        this.fishLevel = Math.max(1, Math.min(99 ,getRandomValue(fishType.fishMinLevel, fishType.fishRandomLevel)));
        this.fishEnergy = (int) getSemiRandomValue(fishType.fishMinEnergy, fishType.fishRandomEnergy);
        this.weight = getSemiRandomValue(fishType.fishMinWeight, fishType.fishRandomWeight);
        this.length = getSemiRandomValue(fishType.fishMinLength, fishType.fishRandomLength);
        this.fishMinEnergyLevel = fishType.fishMinEnergyLevel;
        this.fishMaxEnergyLevel = this.fishEnergy;
        this.curvePoints = FishPatterns.getRandomizedPoints(fishType.fishPattern, fishLevel);
        this.curveControlPoints = FishPatterns.getRandomizedControlPoints(fishType.fishPattern, fishLevel);

    }

    private float getSemiRandomValue(float base, float randomAdjustment){
        float levelValue = fishLevel / 100f;
        return (float) (base + randomAdjustment / 2 * levelValue + randomAdjustment / 2 * Math.random());
    }
    private int getRandomValue(int base, int randomAdjustment){
        return (int) ((base + randomAdjustment * Math.random()));
    }
}

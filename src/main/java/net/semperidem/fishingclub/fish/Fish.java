package net.semperidem.fishingclub.fish;

import net.semperidem.fishingclub.util.Point;

import java.util.HashMap;

public class Fish {
    FishType fishType;
    int fishLevel;
    float weight;
    float length;
    int fishEnergy;
    int fishMinEnergyLevel;
    int fishMaxEnergyLevel;
    int experience;
    Point[] curvePoints;
    Point[] curveControlPoints;

    public Fish(){
        this( FishTypes.COD);
    }

    public Fish(FishType fishType){
        this.fishType = fishType;
        this.fishLevel = Math.max(1, Math.min(99 ,getRandomValue(fishType.fishMinLevel, fishType.fishRandomLevel)));
        this.fishEnergy = fishType.fishEnergyLevel * 2000;
        this.weight = getSemiRandomValue(fishType.fishMinWeight, fishType.fishRandomWeight);
        this.length = getSemiRandomValue(fishType.fishMinLength, fishType.fishRandomLength);
        this.fishMinEnergyLevel = this.fishEnergy / 2;
        this.fishMaxEnergyLevel = this.fishEnergy;
        this.curvePoints = FishPatterns.getRandomizedPoints(fishType.fishPattern, fishLevel);
        this.curveControlPoints = FishPatterns.getRandomizedControlPoints(fishType.fishPattern, fishLevel);
        this.experience = (int) Math.min(500, (5 + Math.pow(fishLevel, 1.1)));

    }

    private float getSemiRandomValue(float base, float randomAdjustment){
        float levelValue = fishLevel / 100f;
        return (float) (base + randomAdjustment / 2 * levelValue + randomAdjustment / 2 * Math.random());
    }
    private int getRandomValue(int base, int randomAdjustment){
        return (int) ((base + randomAdjustment * Math.random()));
    }

    public static Fish getFishOnHook(int fisherLevel){
        int totalRarity = 0;
        HashMap<FishType, Integer> fishTypeToThreshold = new HashMap<>();
        for (FishType fishType : FishTypes.EASY_FISHES) {
            totalRarity += fishType.fishRarity;
            fishTypeToThreshold.put(fishType, totalRarity);
        }
        int randomFish = (int) (Math.random() * totalRarity);
        for (FishType fishType : FishTypes.EASY_FISHES) {
            if (randomFish < fishTypeToThreshold.get(fishType)) {
                return new Fish(fishType);
            }
        }
        return new Fish();
    }
}

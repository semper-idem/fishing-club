package net.semperidem.fishingclub.fish;

import net.semperidem.fishingclub.util.Point;

public class Fish {
    public FishType fishType;
    int fishLevel;
    public float weight;
    public float length;
    int fishEnergy;
    int fishMinEnergyLevel;
    int fishMaxEnergyLevel;
    public int experience;
    Point[] curvePoints;
    Point[] curveControlPoints;
    int grade;

    public Fish(){
        this(FishTypes.COD, 100);
    }

    public Fish(FishType fishType, int fisherLevel){
        this.fishType = fishType;
        this.fishLevel = (int)Math.max(1, Math.min(99 ,
                getSemiRandomValue(fishType.fishMinLevel, fishType.fishRandomLevel, (float)Math.sqrt(fisherLevel / 100f))
                )
        );
        this.fishEnergy = fishType.fishEnergyLevel * 2000;
        this.weight = getSemiRandomValue(fishType.fishMinWeight, fishType.fishRandomWeight, fishLevel / 100f);
        this.length = getSemiRandomValue(fishType.fishMinLength, fishType.fishRandomLength, fishLevel / 100f);
        this.fishMinEnergyLevel = this.fishEnergy / 2;
        this.fishMaxEnergyLevel = this.fishEnergy;
        this.curvePoints = FishPatterns.getRandomizedPoints(fishType.fishPattern, fishLevel);
        this.curveControlPoints = FishPatterns.getRandomizedControlPoints(fishType.fishPattern, fishLevel);
        this.experience = (int) Math.min(500, (200 - fishType.fishRarity) / 100 * (5 + Math.pow(fishLevel, 1.1)));
        this.grade = calculateGrade();
        if (this.grade > 3) {
            this.experience *= Math.pow(2, this.grade - 3);
        }
    }

    private int calculateGrade(){
        int weightGrade = FishUtil.getGrade(weight, fishType.fishMinWeight, fishType.fishMinWeight + fishType.fishRandomWeight);
        int lengthGrade = FishUtil.getGrade(length, fishType.fishMinLength, fishType.fishMinLength + fishType.fishRandomLength);
        return Math.max(weightGrade, lengthGrade);
    }

    private float getSemiRandomValue(float base, float randomAdjustment, float basedOf){
        return (float) (base + randomAdjustment / 2 * basedOf + randomAdjustment / 2 * Math.random());
    }
    private int getRandomValue(int base, int randomAdjustment){
        return (int) ((base + randomAdjustment * Math.random()));
    }

    public static Fish getFishOnHook(int fisherLevel){
        return new Fish();
        /*
        int totalRarity = 0;
        HashMap<FishType, Integer> fishTypeToThreshold = new HashMap<>();
        ArrayList<FishType> availableFish = new ArrayList<>();
        for (FishType fishType : FishType.allFishTypes.values()) {
            if (fisherLevel > fishType.fishMinLevel) {
                availableFish.add(fishType);
            }
        }
        for (FishType fishType : availableFish) {
            totalRarity += fishType.fishRarity;
            fishTypeToThreshold.put(fishType, totalRarity);
        }
        int randomFish = (int) (Math.random() * totalRarity);
        for (FishType fishType : availableFish) {
            if (randomFish < fishTypeToThreshold.get(fishType)) {
                return new Fish(fishType);
            }
        }
        return new Fish();

         */
    }
}

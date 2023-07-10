package net.semperidem.fishingclub.client.game.fish;

import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.util.Point;

public class Fish {
    FisherInfo fisherInfo;

    private FishType fishType;

    public String name;
    public int fishLevel;
    public int grade;
    public int experience;
    public int value;

    public float weight;
    public float length;

    public int fishEnergy;
    public int fishMinEnergyLevel;
    public int fishMaxEnergyLevel = 1;

    public Point[] curvePoints;
    public Point[] curveControlPoints;

    public Fish(){
    }

    public Fish(FishType fishType, FisherInfo fisherInfo){
        this.fishType = fishType;
        this.name = fishType.name;
        this.fisherInfo = fisherInfo;
        this.fishLevel = calculateFishLevel();
        this.weight = calculateFishWeight();
        this.length = calculateFishLength();
        this.grade = calculateGrade();
        this.experience = calculateFishExp();
        initEnergyLevels();
        initCurvePoints();
        this.value = FishUtil.getFishValue(this);
    }

    public FishType getFishType(){
        return this.fishType;
    }

    public void setFishType(FishType fishType){
        this.fishType = fishType;
        initEnergyLevels();
    }

    private void initCurvePoints(){
        this.curvePoints = FishPatterns.getRandomizedPoints(fishType.fishPattern, fishLevel);
        this.curveControlPoints = FishPatterns.getRandomizedControlPoints(fishType.fishPattern, fishLevel);
    }

    private void initEnergyLevels(){
        this.fishEnergy = 1000 + fishType.fishEnergyLevel * 2000;
        this.fishMinEnergyLevel = this.fishEnergy / 2;
        this.fishMaxEnergyLevel = this.fishEnergy;
    }

    private int calculateFishLevel(){
        int adjustedFishLevel = FishUtil.getPseudoRandomValue(
                fishType.fishMinLevel,
                fishType.fishRandomLevel,
                (int)Math.sqrt(fisherInfo.getLevel() / 100f)
        );
        return FishUtil.clampValue(1, 99, adjustedFishLevel);
    }


    private float calculateFishWeight(){
        return FishUtil.getPseudoRandomValue(fishType.fishMinWeight, fishType.fishRandomWeight, fishLevel / 100f);
    }

    private float calculateFishLength(){
        return FishUtil.getPseudoRandomValue(fishType.fishMinLength, fishType.fishRandomLength, fishLevel / 100f);
    }

    private int calculateFishExp(){
        float fishRarityMultiplier = (200 - fishType.fishRarity) / 100;
        float fishExpValue = (float) Math.pow(fishLevel, 1.3);
        float fishGradeMultiplier = this.grade > 3 ? (float) Math.pow(2, this.grade - 3) : 1;
        float fishExp = fishGradeMultiplier * fishRarityMultiplier * (5 + fishExpValue);
        return FishUtil.clampValue(5, 99999, (int)fishExp);
    }

    private int calculateGrade(){
        int weightGrade = FishUtil.getWeightGrade(this);
        int lengthGrade = FishUtil.getLengthGrade(this);
        return Math.max(weightGrade, lengthGrade);
    }


}

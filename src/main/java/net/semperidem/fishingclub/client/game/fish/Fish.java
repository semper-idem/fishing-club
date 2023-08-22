package net.semperidem.fishingclub.client.game.fish;

import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.client.game.FishGameLogic;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.util.FishingRodUtil;
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

    public ItemStack caughtUsing;

    public FisherInfo.Chunk caughtIn;
    public Fish(){
    }

    public Fish(FishType fishType, FisherInfo fisherInfo) {
        this(fishType, fisherInfo, FItemRegistry.CUSTOM_FISHING_ROD.getDefaultStack(), new FisherInfo.Chunk(0,0));
    }
    public Fish(FishType fishType, FisherInfo fisherInfo, ItemStack fishingRod, FisherInfo.Chunk chunk) {
        this.caughtIn = chunk;
        this.caughtUsing = fishingRod;
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
        this.fishEnergy = 200 + fishType.fishEnergyLevel * 250;
        this.fishMinEnergyLevel = this.fishEnergy / 2;
        this.fishMaxEnergyLevel = this.fishEnergy;
    }

    private int calculateFishLevel(){
        int adjustedFishLevel = FishUtil.getPseudoRandomValue(
                fishType.fishMinLevel,
                Math.min(99 - fishType.fishMinLevel, fisherInfo.getLevel()),
                Math.min(1, (float) (Math.min(0.5, (fisherInfo.getLevel() / 200f)) +
                                (Math.sqrt(fisherInfo.getLevel()) / 50f)))
        );
        return FishUtil.clampValue(1, 99, adjustedFishLevel);
    }


    private float calculateFishWeight(){
        float weightMultiplier = Math.max(1, FishingRodUtil.getStat(caughtUsing, FishGameLogic.Stat.FISH_MAX_WEIGHT_MULTIPLIER));
        float minGradeBuff = fisherInfo.getMinGrade() / 5f;
        float minWeight = fishType.fishMinWeight + fishType.fishRandomWeight * minGradeBuff;
        float weightRange = fishType.fishRandomWeight * weightMultiplier * (1 - minGradeBuff);
        return FishUtil.getPseudoRandomValue(minWeight, weightRange, fishLevel / 100f);
    }

    private float calculateFishLength(){
        float lengthMultiplier = Math.max(1, FishingRodUtil.getStat(caughtUsing, FishGameLogic.Stat.FISH_MAX_LENGTH_MULTIPLIER));
        float minGradeBuff = fisherInfo.getMinGrade() / 5f;
        float minLength = fishType.fishMinLength + fishType.fishRandomLength * minGradeBuff;
        float lengthRange = fishType.fishRandomLength * lengthMultiplier * (1 - minGradeBuff);
        return FishUtil.getPseudoRandomValue(minLength, lengthRange, fishLevel / 100f);
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
        float oneUpChance = Math.max(0, FishingRodUtil.getStat(caughtUsing, FishGameLogic.Stat.FISH_RARITY_BONUS));
        if (fisherInfo.hasPerk(FishingPerks.CHUNK_QUALITY_INCREASE)) {
            if (fisherInfo.caughtInChunk(caughtIn)) {
                oneUpChance = 1;
            }
        }
        return Math.min(5, Math.max(weightGrade, lengthGrade) + (Math.random() < oneUpChance ? 1 : 0));
    }

}

package net.semperidem.fishingclub.client.game.fish;

import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.client.game.FishGameLogic;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;
import net.semperidem.fishingclub.util.FishingRodUtil;
import net.semperidem.fishingclub.util.Point;

public class Fish {
    FishingCard fishingCard;

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

    public FishingCard.Chunk caughtIn;

    public boolean oneTimeBuffed;
    public Fish(){
    }

    public Fish(FishType fishType, FishingCard fishingCard) {
        this(fishType, fishingCard, FItemRegistry.CUSTOM_FISHING_ROD.getDefaultStack(), new FishingCard.Chunk(0,0));
    }
    public Fish(FishType fishType, FishingCard fishingCard, ItemStack fishingRod, FishingCard.Chunk chunk) {
        this.caughtIn = chunk;
        this.caughtUsing = fishingRod;
        this.fishType = fishType;
        this.name = fishType.name;
        this.fishingCard = fishingCard;
        this.fishLevel = calculateFishLevel();
        this.weight = calculateFishWeight();
        this.length = calculateFishLength();
        this.grade = calculateGrade();
        this.experience = calculateFishExp();
        initEnergyLevels();
        initCurvePoints();
        this.value = FishUtil.getFishValue(this);
        oneTimeBuffed = fishingCard.getOwner().hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
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
                Math.min(99 - fishType.fishMinLevel, fishingCard.getLevel()),
                Math.min(1, (float) (Math.min(0.5, (fishingCard.getLevel() / 200f)) +
                                (Math.sqrt(fishingCard.getLevel()) / 50f)))
        );
        return FishUtil.clampValue(1, 99, adjustedFishLevel);
    }


    private float calculateFishWeight(){
        float weightMultiplier = Math.max(1, FishingRodUtil.getStat(caughtUsing, FishGameLogic.Stat.FISH_MAX_WEIGHT_MULTIPLIER));
        float minGradeBuff = fishingCard.getMinGrade() / 5f;
        float minWeight = fishType.fishMinWeight + fishType.fishRandomWeight * minGradeBuff;
        float weightRange = fishType.fishRandomWeight * weightMultiplier * (1 - minGradeBuff);
        return FishUtil.getPseudoRandomValue(minWeight, weightRange, fishLevel / 100f);
    }

    private float calculateFishLength(){
        float lengthMultiplier = Math.max(1, FishingRodUtil.getStat(caughtUsing, FishGameLogic.Stat.FISH_MAX_LENGTH_MULTIPLIER));
        float minGradeBuff = fishingCard.getMinGrade() / 5f;
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
        if (fishingCard.hasPerk(FishingPerks.CHUNK_QUALITY_INCREASE)) {
            if (fishingCard.caughtInChunk(caughtIn)) {
                oneUpChance = 1;
            }
        }
        return Math.min(5, Math.max(weightGrade, lengthGrade) + (Math.random() < oneUpChance ? 1 : 0));
    }

    public Fish getHarpoonFish(float multiplier){
        Fish hFish = FishUtil.fromNbt(FishUtil.toNbt(this));
        hFish.experience = (int) (this.experience * multiplier);
        hFish.weight = Math.max(this.fishType.fishMinWeight, (this.weight * multiplier));
        hFish.length = Math.max(this.fishType.fishMinLength, (this.length * multiplier));
        hFish.grade = Math.max(1, (int) (this.grade * multiplier));
        hFish.value = Math.max(1, (int) (this.value * multiplier));
        return hFish;
    }
}

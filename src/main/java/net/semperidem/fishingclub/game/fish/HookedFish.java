package net.semperidem.fishingclub.game.fish;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;
import net.semperidem.fishingclub.util.Point;

public class HookedFish {
    private static final float FISHER_VEST_SLOW_BONUS = -0.15f;
    private static final float FISHER_VEST_EXP_BONUS = 0.1f;

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

    public float damage;

    public Point[] curvePoints;
    public Point[] curveControlPoints;

    public ItemStack caughtUsing;
    public FishingCard.Chunk caughtIn;
    public BlockPos caughtAt;
    public PlayerEntity caughtBy;

    public boolean oneTimeBuffed;
    public HookedFish(){
    }

    public HookedFish(FishType fishType, FishingCard fishingCard) {
        this(fishType, fishingCard, FItemRegistry.CUSTOM_FISHING_ROD.getDefaultStack(), new FishingCard.Chunk(0,0), null);
    }
    public HookedFish(FishType fishType, FishingCard fishingCard, ItemStack fishingRod, FishingCard.Chunk chunk, BlockPos caughtAt) {
        this.caughtIn = chunk;
        this.caughtUsing = fishingRod;
        this.caughtAt = caughtAt;
        this.caughtBy = fishingCard.getOwner();
        this.fishType = fishType;
        this.name = fishType.name;
        this.fishingCard = fishingCard;
        this.fishLevel = calculateFishLevel();
        this.weight = calculateFishWeight();
        this.length = calculateFishLength();
        this.grade = calculateGrade();
        this.experience = calculateFishExp();
        this.damage = calculateDamage();
        initEnergyLevels();
        initCurvePoints();
        this.value = FishUtil.getFishValue(this);
        oneTimeBuffed = fishingCard.getOwner().hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        applyFisherVestEffect();
    }


    private float calculateDamage(){
        float damageReduction = FishingRodPartController.getStat(caughtUsing, FishingRodStatType.DAMAGE_REDUCTION);
        boolean boatBoosted = fishingCard.isFishingFromBoat() && fishingCard.hasPerk(FishingPerks.LINE_HEALTH_BOAT);
        damageReduction += boatBoosted ? 0.2f : 0;
        float percentDamageReduction = (1 - MathHelper.clamp(damageReduction, 0f ,1f));
        return getFishRawDamage() * percentDamageReduction;
    }

    private float getFishRawDamage() {
        return Math.max(0, (fishLevel - 5 - (fishingCard.getLevel() * 0.25f)) * 0.05f);
    }

    private void applyFisherVestEffect(){
        PlayerEntity player = this.fishingCard.getOwner();
        if (!FishUtil.hasFishingVest(player)) return;
        float slowRatio = 1 + FISHER_VEST_SLOW_BONUS;
        float expRatio = 1 + FISHER_VEST_EXP_BONUS;
        if (!FishUtil.hasNonFishingEquipment(player)) {
            slowRatio += FISHER_VEST_SLOW_BONUS;
            expRatio += FISHER_VEST_EXP_BONUS;
        }
        this.fishEnergy = (int) (this.fishEnergy * slowRatio);
        this.experience = (int) (this.experience * expRatio);
    }

    public FishType getFishType(){
        return this.fishType;
    }

    public void setFishType(FishType fishType){
        this.fishType = fishType;
        initEnergyLevels();
    }

    private void initCurvePoints(){
      //  this.curvePoints = FishPatterns.getRandomizedPoints(fishType.fishPattern, fishLevel);
     //   this.curveControlPoints = FishPatterns.getRandomizedControlPoints(fishType.fishPattern, fishLevel);
    }

    private void initEnergyLevels(){
        this.fishEnergy = 200 + fishType.staminaLevel * 250;
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
        float weightMultiplier = Math.max(1, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.FISH_MAX_WEIGHT_MULTIPLIER));
        float minGradeBuff = fishingCard.getMinGrade() / 5f;
        float minWeight = fishType.fishMinWeight + fishType.fishRandomWeight * minGradeBuff;
        float weightRange = fishType.fishRandomWeight * weightMultiplier * (1 - minGradeBuff);
        return FishUtil.getPseudoRandomValue(minWeight, weightRange, fishLevel / 100f);
    }

    private float calculateFishLength(){
        float lengthMultiplier = Math.max(1, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.FISH_MAX_LENGTH_MULTIPLIER));
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
        float oneUpChance = Math.max(0, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.FISH_RARITY_BONUS));
        if (fishingCard.hasPerk(FishingPerks.CHUNK_QUALITY_INCREASE)) {
            if (fishingCard.caughtInChunk(caughtIn)) {
                oneUpChance = 1;
            }
        }
        return Math.min(5, Math.max(weightGrade, lengthGrade) + (Math.random() < oneUpChance ? 1 : 0));
    }

    public HookedFish applyHarpoonMultiplier(float multiplier){
        this.experience = (int) (this.experience * multiplier);
        this.weight = Math.max(this.fishType.fishMinWeight, (this.weight * multiplier));
        this.length = Math.max(this.fishType.fishMinLength, (this.length * multiplier));
        this.grade = Math.max(1, (int) (this.grade * multiplier));
        this.value = Math.max(1, (int) (this.value * multiplier));
        return this;
    }
}

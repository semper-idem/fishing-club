package net.semperidem.fishingclub.fish;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

import java.util.UUID;

public class Fish {
    private static final float FISHER_VEST_EXP_BONUS = 0.3f;

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 99;

    private static final int MIN_EXP = 5;
    private static final int MAX_EXP = 99999;

    private final Species species;

    public String name;

    public int level;
    public int experience;
    public float damage;

    public int grade;
    public int value;

    public float weight;
    public float length;

    public ItemStack caughtUsing;
    public UUID caughtByUUID;

    public boolean consumeGradeBuff;

    public Fish(Species species, FishingCard fishingCard, IHookEntity hookedWith) {
        this.species = species;
        this.name = species.name;

        this.caughtUsing = hookedWith.getCaughtUsing();

        int fisherLevel = fishingCard.getLevel();
        boolean hasBoatBoostedHealth = fishingCard.isFishingFromBoat() && fishingCard.hasPerk(FishingPerks.LINE_HEALTH_BOAT);
        int minGrade = fishingCard.getMinGrade();
        this.level = calculateLevel(fisherLevel);
        this.weight = calculateWeight(minGrade);
        this.length = calculateLength(minGrade);
        this.grade = calculateGrade();
        this.damage = calculateDamage(fisherLevel, hasBoatBoostedHealth);
        this.value = calculateValue();

        PlayerEntity caughtBy = fishingCard.getHolder();
        this.experience = calculateExperience(caughtBy);
        this.caughtByUUID = caughtBy.getUuid();
        this.consumeGradeBuff = caughtBy.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
    }

    public Fish(NbtCompound nbt){
        this.name = nbt.getString("name");
        this.species = SpeciesLibrary.ALL_FISH_TYPES.get(name);
        this.level = nbt.getInt("level");
        this.experience = nbt.getInt("experience");
        this.grade = nbt.getInt("grade");
        this.value = nbt.getInt("value");
        this.weight = nbt.getFloat("weight");
        this.length = nbt.getFloat("length");
        this.caughtUsing = ItemStack.fromNbt(nbt.getCompound("caughtUsing"));
    }

     public NbtCompound getNbt(){
         NbtCompound nbt = new NbtCompound();
         nbt.putString("name", name);
         nbt.putInt("level", level);
         nbt.putInt("experience", experience);
         nbt.putInt("grade", grade);
         nbt.putInt("value", value);
         nbt.putFloat("weight", weight);
         nbt.putFloat("length", length);
         nbt.put("caughtUsing", caughtUsing.writeNbt(new NbtCompound()));
         return nbt;
    }

    private float calculateDamage(int fisherLevel, boolean hasBoatBoostedHealth){
        float damageReduction = FishingRodPartController.getStat(caughtUsing, FishingRodStatType.DAMAGE_REDUCTION);
        damageReduction += hasBoatBoostedHealth ? 0.2f : 0;
        float percentDamageReduction = (1 - MathHelper.clamp(damageReduction, 0f ,1f));
        return getFishRawDamage(fisherLevel) * percentDamageReduction;
    }

    private float getFishRawDamage( int fisherLevel) {
        return Math.max(0, (level - 5 - (fisherLevel * 0.25f)) * 0.05f);
    }


    private float getVestExpMultiplier(PlayerEntity caughtBy) {
        if (!FishUtil.hasFishingVest(caughtBy)) {
            return 1;
        }

        float result = 1 + FISHER_VEST_EXP_BONUS;

        if (FishUtil.hasProperFishingEquipment(caughtBy)) {
            result += FISHER_VEST_EXP_BONUS;
        }
        return result;
    }

    public Species getSpecies(){
        return this.species;
    }

    private int calculateLevel(int fisherLevel){
        int adjustedFishLevel = FishUtil.getPseudoRandomValue(
                species.minLevel,
                Math.min(99 - species.minLevel, fisherLevel),
                Math.min(1, (float) (Math.min(0.5, (fisherLevel / 200f)) +
                                (Math.sqrt(fisherLevel) / 50f)))
        );
        return MathHelper.clamp(adjustedFishLevel, MIN_LEVEL, MAX_LEVEL);
    }


    private float calculateWeight(int minGrade){
        float weightMultiplier = Math.max(1, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.FISH_MAX_WEIGHT_MULTIPLIER));
        float minGradeBuff = minGrade / 5f;
        float minWeight = species.fishMinWeight + species.fishRandomWeight * minGradeBuff;
        float weightRange = species.fishRandomWeight * weightMultiplier * (1 - minGradeBuff);
        return FishUtil.getPseudoRandomValue(minWeight, weightRange, level / 100f);
    }

    private float calculateLength(int minGrade){
        float lengthMultiplier = Math.max(1, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.FISH_MAX_LENGTH_MULTIPLIER));
        float minGradeBuff = minGrade / 5f;
        float minLength = species.fishMinLength + species.fishRandomLength * minGradeBuff;
        float lengthRange = species.fishRandomLength * lengthMultiplier * (1 - minGradeBuff);
        return FishUtil.getPseudoRandomValue(minLength, lengthRange, level / 100f);
    }

    private int calculateExperience(PlayerEntity caughtBy){
        float fishRarityMultiplier = (200 - species.fishRarity) / 100;
        float fishExpValue = (float) Math.pow(level, 1.3);
        float fishGradeMultiplier = this.grade > 3 ? (float) Math.pow(2, this.grade - 3) : 1;
        int fishExp = (int) (fishGradeMultiplier * fishRarityMultiplier * (5 + fishExpValue));
        fishExp = (int) (fishExp * getVestExpMultiplier(caughtBy));
        return MathHelper.clamp(fishExp, MIN_EXP, MAX_EXP);
    }

    private int calculateGrade(){
        int weightGrade = FishUtil.getWeightGrade(this);
        int lengthGrade = FishUtil.getLengthGrade(this);
        float oneUpChance = Math.max(0, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.FISH_RARITY_BONUS));
        return Math.min(5, Math.max(weightGrade, lengthGrade) + (Math.random() < oneUpChance ? 1 : 0));
    }

    public void applyMultiplier(float multiplier){
        this.experience = (int) (this.experience * multiplier);
        this.weight = Math.max(this.species.fishMinWeight, (this.weight * multiplier));
        this.length = Math.max(this.species.fishMinLength, (this.length * multiplier));
        this.grade = Math.max(1, (int) (this.grade * multiplier));
        this.value = Math.max(1, (int) (this.value * multiplier));
    }

    private int calculateValue() {
        float gradeMultiplier = (float) (grade <= 3 ? (0.5 + (grade / 3f)) : Math.pow(2, (grade - 2)));
        float levelMultiplier = 1 + (float) Math.pow(2, level / 50f);
        float weightMultiplier = 1 + weight / 100;
        float fValue = 1 + ((125 - species.fishRarity) / 100) * 0.5f;
        fValue *= gradeMultiplier;
        fValue *= levelMultiplier;
        fValue *= weightMultiplier;
        return (int) fValue;
    }
}

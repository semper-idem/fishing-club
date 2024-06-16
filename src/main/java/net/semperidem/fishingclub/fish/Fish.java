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
import net.semperidem.fishingclub.registry.StatusEffectRegistry;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static net.semperidem.fishingclub.fish.FishUtil.getPseudoRandomValue;

public class Fish {
    private static final float FISHER_VEST_EXP_BONUS = 0.3f;

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 99;

    private static final int MIN_EXP = 3;
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
    private IHookEntity hookedWith;
    private FishingCard hookedBy;
    public UUID caughtByUUID;

    public boolean consumeGradeBuff;


    public String id;

    public Fish(Species species, FishingCard fishingCard, IHookEntity hookedWith) {
        this.species = species;
        this.name = species.name;

        this.hookedBy = fishingCard;
        this.hookedWith = hookedWith;
        this.caughtUsing = hookedWith.getCaughtUsing();


        int fisherLevel = fishingCard.getLevel();
        boolean hasBoatBoostedHealth = fishingCard.isFishingFromBoat() && fishingCard.hasPerk(FishingPerks.LINE_HEALTH_BOAT);
        this.level = calculateLevel(fisherLevel);
        this.weight = getPseudoRandomValue(species.fishMinWeight, species.fishRandomWeight, level * 0.01f);
        this.length = getPseudoRandomValue(species.fishMinLength, species.fishRandomLength, level * 0.01f);
        this.grade = calculateGrade();
        this.damage = calculateDamage(fisherLevel, hasBoatBoostedHealth);
        this.value = calculateValue();

        PlayerEntity caughtBy = fishingCard.getHolder();
        this.experience = calculateExperience(caughtBy);
        this.caughtByUUID = caughtBy.getUuid();
        this.consumeGradeBuff = caughtBy.hasStatusEffect(StatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        this.id =  (System.currentTimeMillis()) + "" + Math.abs(caughtByUUID.hashCode());
        applyMethodDebuff(hookedWith.getFishMethodDebuff());
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
        this.caughtByUUID = nbt.getUuid("caughtBy");
        this.id = nbt.getString("id");
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
         nbt.putUuid("caughtBy", caughtByUUID);
         nbt.putString("id", id);
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
        int adjustedFishLevel = getPseudoRandomValue(
                species.minLevel,
                Math.min(99 - species.minLevel, fisherLevel),
                Math.min(1, (float) (Math.min(0.5, (fisherLevel / 200f)) +
                                (Math.sqrt(fisherLevel) / 50f)))
        );
        return MathHelper.clamp(adjustedFishLevel, MIN_LEVEL, MAX_LEVEL);
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
        float qualityIncrease = Math.max(0, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.FISH_RARITY_BONUS));
        float levelSkew = level * 0.0025f;
        float rodSkew = 0.25f * qualityIncrease;
        float chunkSkew = 0.25f;//0.25 for best chunk  TODO CHUNK QUALITY
        float waitSkew = 0.25f * (hookedWith.getWaitTime() / 1200); //make waiting over 60s STILL improve quality (intended)
        float skew = levelSkew + rodSkew + chunkSkew + waitSkew;
        return MathHelper.clamp(getPseudoRandomValue(1, 4, skew),hookedBy.getMinGrade(), 5);
    }

    private void applyMethodDebuff(float multiplier){
        this.experience = (int) (this.experience * Math.pow(multiplier, 2));
        this.grade = Math.max(1, (int) (this.grade * Math.pow(multiplier, 2)));
        this.value = calculateValue();
    }

    private int calculateValue() {
        float gradeMultiplier = (float) (grade <= 3 ? (0.5 + (grade / 3f)) : Math.pow(2, (grade - 2)));
        float weightMultiplier = 1 +(weight < 100 ? (float) Math.sqrt(weight / 0.01f) : weight * 0.01f);
        float fValue = 1 + ((125 - species.fishRarity) / 100) * 0.5f;
        fValue *= gradeMultiplier;
        fValue *= weightMultiplier;
        return (int) fValue;
    }

    public boolean isEqual(Fish other) {
        boolean isEqual = (Objects.equals(this.name, other.name));
        isEqual &= (Objects.equals(this.species.name, other.species.name));
        isEqual &= (this.level == other.level);
        isEqual &= (this.experience == other.experience);
        isEqual &= (this.grade == other.grade);
        isEqual &= (this.value == other.value);
        isEqual &= (this.weight == other.weight);
        isEqual &= (this.length == other.length);
        isEqual &= (this.caughtByUUID.compareTo(other.caughtByUUID) == 0);
        isEqual &= (Objects.equals(this.id, other.id));
        return isEqual;
    }

    @Override
    public String toString() {
        return name;
    }
}

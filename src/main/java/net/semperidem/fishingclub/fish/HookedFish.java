package net.semperidem.fishingclub.fish;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

import java.util.UUID;

public class HookedFish {
    private static final float FISHER_VEST_EXP_BONUS = 0.3f;

    private static final int MIN_LEVEL = 1;
    private static final int MAX_LEVEL = 99;

    private static final int MIN_EXP = 5;
    private static final int MAX_EXP = 99999;

    private FishingCard fishingCard;

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
    public FishingCard.Chunk caughtIn;
    public BlockPos caughtAt;
    public PlayerEntity caughtBy;
    public UUID caughtByUUID;

    public boolean consumeGradeBuff;

    public HookedFish(NbtCompound nbt){
        this.name = nbt.getString("name");
        this.species = SpeciesLibrary.ALL_FISH_TYPES.get(name);

        //TODO FISHING CARD HERE

        this.caughtIn = new FishingCard.Chunk(nbt.getString("caughtIn"));
        this.caughtUsing = ItemStack.fromNbt(nbt.getCompound("caughtUsing"));

        this.level = nbt.getInt("fishLevel");
        this.experience = nbt.getInt("experience");
        this.grade = nbt.getInt("grade");
        this.value = nbt.getInt("value");
        this.weight = nbt.getFloat("weight");
        this.length = nbt.getFloat("length");
    }

    public HookedFish(Species species, FishingCard fishingCard, ItemStack caughtUsing, FishingCard.Chunk caughtIn, BlockPos caughtAt) {
        this.species = species;
        this.name = species.name;

        this.fishingCard = fishingCard;

        this.caughtIn = caughtIn;
        this.caughtUsing = caughtUsing;
        this.caughtAt = caughtAt;
        this.caughtBy = fishingCard.getOwner();
        this.caughtByUUID = caughtBy.getUuid();

        this.consumeGradeBuff = caughtBy.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);

        this.level = calculateLevel();
        this.weight = calculateWeight();
        this.length = calculateLength();
        this.grade = calculateGrade();
        this.experience = calculateExperience();
        this.damage = calculateDamage();
        this.value = calculateValue();

        applyFisherVestEffect();
    }

     public NbtCompound getNbt(){
        NbtCompound fishNbt = new NbtCompound();
        fishNbt.putString("name", name);
        fishNbt.putInt("grade", grade);
        fishNbt.putInt("fishLevel", level);
        fishNbt.putInt("experience", experience);
        fishNbt.putInt("value", value);
        fishNbt.putFloat("weight", weight);
        fishNbt.putFloat("length", length);
        fishNbt.put("caughtUsing", caughtUsing.writeNbt(new NbtCompound()));
        fishNbt.putString("caughtIn", caughtIn.toString());
        return fishNbt;
    }

    private float calculateDamage(){
        float damageReduction = FishingRodPartController.getStat(caughtUsing, FishingRodStatType.DAMAGE_REDUCTION);
        boolean boatBoosted = fishingCard.isFishingFromBoat() && fishingCard.hasPerk(FishingPerks.LINE_HEALTH_BOAT);
        damageReduction += boatBoosted ? 0.2f : 0;
        float percentDamageReduction = (1 - MathHelper.clamp(damageReduction, 0f ,1f));
        return getFishRawDamage() * percentDamageReduction;
    }

    private float getFishRawDamage() {
        return Math.max(0, (level - 5 - (fishingCard.getLevel() * 0.25f)) * 0.05f);
    }

    private void applyFisherVestEffect(){
        PlayerEntity player = this.fishingCard.getOwner();
        if (!FishUtil.hasFishingVest(player)) return;
        float expRatio = 1 + FISHER_VEST_EXP_BONUS;
        if (FishUtil.hasProperFishingEquipment(player)) {
            expRatio += FISHER_VEST_EXP_BONUS;
        }
        this.experience = (int) (this.experience * expRatio);
    }

    public Species getSpecies(){
        return this.species;
    }

    private int calculateLevel(){
        int adjustedFishLevel = FishUtil.getPseudoRandomValue(
                species.fishMinLevel,
                Math.min(99 - species.fishMinLevel, fishingCard.getLevel()),
                Math.min(1, (float) (Math.min(0.5, (fishingCard.getLevel() / 200f)) +
                                (Math.sqrt(fishingCard.getLevel()) / 50f)))
        );
        return MathHelper.clamp(adjustedFishLevel, MIN_LEVEL, MAX_LEVEL);
    }


    private float calculateWeight(){
        float weightMultiplier = Math.max(1, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.FISH_MAX_WEIGHT_MULTIPLIER));
        float minGradeBuff = fishingCard.getMinGrade() / 5f;
        float minWeight = species.fishMinWeight + species.fishRandomWeight * minGradeBuff;
        float weightRange = species.fishRandomWeight * weightMultiplier * (1 - minGradeBuff);
        return FishUtil.getPseudoRandomValue(minWeight, weightRange, level / 100f);
    }

    private float calculateLength(){
        float lengthMultiplier = Math.max(1, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.FISH_MAX_LENGTH_MULTIPLIER));
        float minGradeBuff = fishingCard.getMinGrade() / 5f;
        float minLength = species.fishMinLength + species.fishRandomLength * minGradeBuff;
        float lengthRange = species.fishRandomLength * lengthMultiplier * (1 - minGradeBuff);
        return FishUtil.getPseudoRandomValue(minLength, lengthRange, level / 100f);
    }

    private int calculateExperience(){
        float fishRarityMultiplier = (200 - species.fishRarity) / 100;
        float fishExpValue = (float) Math.pow(level, 1.3);
        float fishGradeMultiplier = this.grade > 3 ? (float) Math.pow(2, this.grade - 3) : 1;
        int fishExp = (int) (fishGradeMultiplier * fishRarityMultiplier * (5 + fishExpValue));
        return MathHelper.clamp(fishExp, MIN_EXP, MAX_EXP);
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
        this.weight = Math.max(this.species.fishMinWeight, (this.weight * multiplier));
        this.length = Math.max(this.species.fishMinLength, (this.length * multiplier));
        this.grade = Math.max(1, (int) (this.grade * multiplier));
        this.value = Math.max(1, (int) (this.value * multiplier));
        return this;
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

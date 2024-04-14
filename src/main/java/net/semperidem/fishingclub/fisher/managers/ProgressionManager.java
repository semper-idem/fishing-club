package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.fisher.perks.spells.Spells;

import java.util.HashMap;

public class ProgressionManager extends DataManager{
    private static final int BASE_EXP = 50;
    private static final float EXP_EXPONENT = 1.25f;
    private static final float PLAYER_EXP_RATIO = 0.1f;
    private static final int MIN_PLAYER_EXP = 1;

    private int level = 1;
    private int exp = 0;
    private int perkPoints = 0;

    private static final int RESET_COST_PER_PERK = 1000;
    private static final int RESET_COST_PER_RESET = 10000;
    private int resetCount = 0;

    private final HashMap<String, FishingPerk> perks = new HashMap<>();
    private final HashMap<String, SpellInstance> spells = new HashMap<>();

    public ProgressionManager(FishingCard trackedFor) {
        super(trackedFor);
    }

    public void resetCooldown(){
        spells.forEach((key, spell) -> spell.resetCooldown());
    }


    public int getResetCost() {
        return RESET_COST_PER_RESET * resetCount + RESET_COST_PER_PERK * perks.size();
    }

    public void resetPerks() {
        resetCount++;
        int refundPerkPoints = perks.size();
        perks.clear();
        spells.clear();
        addPerkPoints(refundPerkPoints);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        int currentLevel = this.level;
        this.level = level;
        this.perkPoints += (level - currentLevel);
    }
    public int getExp() {
        return exp;
    }

    public void addPerk(String perkName){
        FishingPerk perk = FishingPerks.getPerkFromName(perkName);
        if (!hasRequiredPerk(perk) || !hasPerkPoints()) {
            return;
        }
        perk.onEarn(trackedFor.getHolder());
        this.perks.put(perk.getName(), perk);
        if (Spells.perkHasSpell(perk)) {
            this.spells.put(perk.getName(), SpellInstance.getSpellInstance(perk, 0));
        }
        perkPoints--;
    }

    public boolean hasRequiredPerk(FishingPerk perk){
        return perk.getParent() == null || this.perks.containsKey(perk.getParent().getName());
    }

    public boolean canUnlockPerk(FishingPerk perk) {
        boolean isNotUnlocked = !this.perks.containsKey(perk.getName());
        return hasRequiredPerk(perk) && hasPerkPoints() && isNotUnlocked;
    }

    public void addPerkPoints(int amount){
        this.perkPoints += amount;
    }

    public int getPerkPoints(){
        return this.perkPoints;
    }

    public boolean hasPerkPoints(){
        return this.perkPoints > 0;
    }

    public void useSpell(String perkName, Entity target){
        if (!perks.containsKey(perkName)){
            return;
        }
        SpellInstance spellInstance = spells.get(perkName);
        spellInstance.use((ServerPlayerEntity) trackedFor.getHolder(), target);
        spells.put(perkName, spellInstance);
    }

    public int nextLevelXP(){
        return (int) Math.floor(BASE_EXP * Math.pow(level, EXP_EXPONENT));
    }

    public void grantExperience(double gainedXP){
        if (gainedXP < 0) {
            return;
        }

        trackedFor.getHolder().addExperience((int) Math.max(MIN_PLAYER_EXP, gainedXP * PLAYER_EXP_RATIO));

        this.exp += gainedXP;
        float nextLevelXP = nextLevelXP();
        while (this.exp >= nextLevelXP) {
            this.exp -= nextLevelXP;
            this.level++;
            LevelRewardRule.getRewardForLevel(this.level).forEach(levelReward -> levelReward.grant(trackedFor));
            nextLevelXP = nextLevelXP();
        }
    }

    public HashMap<String, FishingPerk> getPerks(){
        return perks;
    }

    public boolean hasPerk(FishingPerk perk) {
        return perks.containsKey(perk.getName());
    }

    @Override
    public void readNbt(NbtCompound fishingCardNbt) {
        NbtCompound progressionNbt = fishingCardNbt.getCompound(TAG);
        level = progressionNbt.getInt(LEVEL_TAG);
        exp = progressionNbt.getInt(EXP_TAG);
        perkPoints = progressionNbt.getInt(PERK_POINTS);
        resetCount = progressionNbt.getInt(RESET_COUNT);
        readPerks(progressionNbt);
        readSpells(progressionNbt);
    }

    private void readPerks(NbtCompound fishingCardNbt){
        perks.clear();
        NbtList perkListTag = fishingCardNbt.getList(PERKS_TAG, NbtElement.STRING_TYPE);
        perkListTag.forEach(nbtElement -> {
            FishingPerk perk = FishingPerks.getPerkFromName(nbtElement.asString());
            perks.put(perk.getName(), perk);
        });
    }

    private void readSpells(NbtCompound progressionNbt){
        spells.clear();
        NbtList spellListTag = progressionNbt.getList(SPELLS_TAG, NbtElement.COMPOUND_TYPE);
        for(int i = 0; i < spellListTag.size(); i++) {
            SpellInstance spellInstance = SpellInstance.fromNbt(spellListTag.getCompound(i));
            spells.put(spellInstance.getKey(), spellInstance);
        }
    }

    @Override
    public void writeNbt(NbtCompound fishingCardNbt) {
        NbtCompound progressionNbt = new NbtCompound();
        progressionNbt.putInt(LEVEL_TAG, level);
        progressionNbt.putInt(EXP_TAG, exp);
        progressionNbt.putInt(PERK_POINTS, perkPoints);
        progressionNbt.putInt(RESET_COUNT, resetCount);
        writePerks(progressionNbt);
        writeSpells(progressionNbt);
        fishingCardNbt.put(TAG, progressionNbt);
    }

    public void writePerks(NbtCompound progressionNbt){
        NbtList perksTag = new NbtList();
        perks.forEach((fishingPerkName, fishingPerk) -> perksTag.add(NbtString.of(fishingPerkName)));
        progressionNbt.put(PERKS_TAG, perksTag);
    }

    public void writeSpells(NbtCompound progressionNbt){
        NbtList spellsTag = new NbtList();
        spells.forEach((fishingPerkNbt, spellInstance) -> spellsTag.add(SpellInstance.toNbt(spellInstance)));
        progressionNbt.put(SPELLS_TAG, spellsTag);
    }


    private static final String TAG = "progression";
    private static final String LEVEL_TAG = "level";
    private static final String EXP_TAG = "exp";
    private static final String PERK_POINTS = "perk_points";
    private static final String RESET_COUNT = "reset_count";
    private static final String PERKS_TAG ="perks";
    private static final String SPELLS_TAG ="spells";
}

package net.semperidem.fishing_club.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishing_club.fisher.perks.FishingPerk;
import net.semperidem.fishing_club.fisher.perks.FishingPerks;
import net.semperidem.fishing_club.fisher.perks.spells.SpellInstance;
import net.semperidem.fishing_club.fisher.perks.spells.Spells;

import java.util.HashMap;

public class ProgressionManager extends DataManager {
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
        sync();
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
        sync();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        int currentLevel = this.level;
        this.level = level;
        this.perkPoints += (level - currentLevel);
        sync();
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
        sync();
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
        sync();
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
        sync();
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
        sync();
    }

    public HashMap<String, FishingPerk> getPerks(){
        return perks;
    }

    public boolean hasPerk(FishingPerk perk) {
        return perks.containsKey(perk.getName());
    }

    @Override
    public void readNbt(NbtCompound fishingCardNbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (!fishingCardNbt.contains(TAG)) {
            return;
        }
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
        this.perks.putAll(FishingPerks.fromByteArray(fishingCardNbt.getByteArray(PERKS_TAG)));
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
    public void writeNbt(NbtCompound fishingCardNbt, RegistryWrapper.WrapperLookup wrapperLookup) {
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
        progressionNbt.putByteArray(PERKS_TAG, FishingPerks.toByteArray(this.perks));//todo with spells
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

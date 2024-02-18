package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.level_reward.LevelReward;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.fisher.perks.spells.Spells;

import java.util.HashMap;

public class ProgressionManager extends DataManager{
    private static final String TAG = "progression";
    private static final String LEVEL_TAG = "lvl";
    private static final String EXP_TAG = "xp";
    private static final String SKILL_POINTS_TAG = "sp";
    public static final String PERKS_TAG ="perks";
    public static final String SPELLS_TAG ="spells";

    private static final int BASE_EXP = 50;
    private static final float EXP_EXPONENT = 1.25f;

    int level = 1;
    int exp = 0;
    int perkPoints = 0;
    final HashMap<String, FishingPerk> perks = new HashMap<>();
    final HashMap<String, SpellInstance> spells = new HashMap<>();

    public ProgressionManager(FishingCard trackedFor) {
        super(trackedFor);
    }


    public void resetCooldown(){
        for(SpellInstance spellInstance : spells.values()) {
            spellInstance.resetCooldown();
        }
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    void addPerk(FishingPerk perk){
        if (hasRequiredPerk(perk) && hasPerkPoints()) {
            perk.onEarn(trackedFor.getHolder());
            this.perks.put(perk.getName(), perk);
            if (Spells.perkHasSpell(perk)) {
                this.spells.put(perk.getName(), SpellInstance.getSpellInstance(perk, 0));
            }
            perkPoints--;
        }
    }

    public void addPerk(String perkName){
        addPerk(FishingPerks.getPerkFromName(perkName));
    }

    public boolean hasRequiredPerk(FishingPerk perk){
        return perk.getParent() == null || this.perks.containsKey(perk.getParent().getName());
    }

    public boolean canUnlockPerk(FishingPerk perk) {
        boolean isNotUnlocked = !this.perks.containsKey(perk.getName());
        return hasRequiredPerk(perk) && hasPerkPoints() && isNotUnlocked;
    }

    public void addSkillPoints(int amount){
        this.perkPoints += amount;
    }


    public int getPerkPoints(){
        return this.perkPoints;
    }

    public boolean hasPerkPoints(){
        return this.perkPoints > 0;
    }

    public void setPerkPoints(int perkPoints){
        this.perkPoints = perkPoints;
    }

    public void useSpell(String perkName, Entity target){
        if (!perks.containsKey(perkName)) return;
        SpellInstance spellInstance = spells.get(perkName);
        spellInstance.use((ServerPlayerEntity) trackedFor.getHolder(), target);
        spells.put(perkName, spellInstance);
    }


    public void removePerk(String perkName){
        if (!this.perks.containsKey(perkName)) return;
        this.perks.remove(perkName);
        this.perkPoints++;
    }

    public int nextLevelXP(){
        return (int) Math.floor(BASE_EXP * Math.pow(level, EXP_EXPONENT));
    }


    public void grantExperience(double gainedXP){
        if (gainedXP == 0) {
            return;
        }

        trackedFor.getHolder().addExperience((int) Math.max(1, gainedXP / 10));

        this.exp += gainedXP;
        float nextLevelXP = nextLevelXP();
        while (this.exp >= nextLevelXP) {
            this.exp -= nextLevelXP;
            this.level++;
            onLevelUpBehaviour();
            nextLevelXP = nextLevelXP();
        }
    }


    private void onLevelUpBehaviour() {
        for (LevelReward reward : LevelRewardRule.getRewardForLevel(this.level)) {
            reward.grant(trackedFor);
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
        perkPoints = progressionNbt.getInt(SKILL_POINTS_TAG);
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
        progressionNbt.putInt(SKILL_POINTS_TAG, perkPoints);
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

}

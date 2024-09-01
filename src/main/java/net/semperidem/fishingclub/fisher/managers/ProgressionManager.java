package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.perks.TradeSecret;
import net.semperidem.fishingclub.fisher.perks.TradeSecrets;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.fisher.perks.spells.Spells;

import java.util.*;

public class ProgressionManager extends DataManager {
    private static final int BASE_EXP = 50;
    private static final float EXP_EXPONENT = 1.25f;
    private static final float PLAYER_EXP_RATIO = 0.1f;
    private static final int MIN_PLAYER_EXP = 1;

    private int level = 1;
    private int exp = 0;
    private int admirationPoints = 0;

    private static final int RESET_COST_PER_PERK = 1000;
    private static final int RESET_COST_PER_RESET = 10000;
    private int resetCount = 0;

    private final HashMap<String, Integer> knownTradeSecrets = new HashMap<>();
    private final HashMap<String, SpellInstance> spells = new HashMap<>();

    public ProgressionManager(FishingCard trackedFor) {
        super(trackedFor);
    }

    public void resetCooldown(){
        spells.forEach((key, spell) -> spell.resetCooldown());
        sync();
    }


    public int getResetCost() {
        return RESET_COST_PER_RESET * resetCount + RESET_COST_PER_PERK * knownTradeSecrets.size();
    }

    public void resetPerks() {
        resetCount++;
        int refundPerkPoints = knownTradeSecrets.size();
        knownTradeSecrets.clear();
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
        this.admirationPoints += (level - currentLevel);
        sync();
    }
    public int getExp() {
        return exp;
    }

    public void learnTradeSecret(String tradeSecretName){
        if (!hasAdmirationPoints()) {
            return;
        }
        if (this.knownTradeSecrets.containsKey(tradeSecretName)) {
            return;
        }
        Optional<TradeSecret> maybeTradeSecret = TradeSecrets.fromName(tradeSecretName);
        if (maybeTradeSecret.isEmpty()){
            return;
        }
        TradeSecret tradeSecret = maybeTradeSecret.get();
        if (!hasRequiredPerk(tradeSecret)) {
            return;
        }
        int currentLevel = this.knownTradeSecrets.getOrDefault(tradeSecretName, 0);
        if (tradeSecret.maxLevel() <= currentLevel) {
            return;
        }
        int nextLevelPoints = tradeSecret.cost(currentLevel);
        if (!this.hasAdmirationPoints(nextLevelPoints)){
            return;
        }
        this.admirationPoints -= currentLevel;
        this.knownTradeSecrets.put(tradeSecretName, currentLevel + 1);
        if (Spells.perkHasSpell(tradeSecret)) {
            this.spells.put(tradeSecretName, SpellInstance.getSpellInstance(tradeSecret, 0));
        }

        this.sync();
    }



    public boolean hasRequiredPerk(TradeSecret tradeSecret){
        return tradeSecret.getParent() == null;
    }

    public boolean canUnlockPerk(TradeSecret tradeSecret) {
        if (!hasAdmirationPoints()) {
            return false;
        }
        if (!this.hasRequiredPerk(tradeSecret)) {
            return false;
        }
        return !this.knownTradeSecrets.containsKey(tradeSecret.name());
    }

    public void addPerkPoints(int amount){
        this.admirationPoints += amount;
        sync();
    }

    public int getAdmirationPoints(){
        return this.admirationPoints;
    }

    public boolean hasAdmirationPoints(){
        return this.admirationPoints > 0;
    }

    public boolean hasAdmirationPoints(int count) {
        return this.admirationPoints >= count;
    }

    public void useSpell(String perkName, Entity target){
        if (!this.spells.containsKey(perkName)){
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
        //It was intentionally to not cast it to decimal but maybe we should
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

    public Set<String> getKnownTradeSecrets(){
        return this.knownTradeSecrets.keySet();
    }

    public boolean knowsTradeSecret(TradeSecret tradeSecret) {
        return this.knowsTradeSecret(tradeSecret.name());
    }

    public boolean knowsTradeSecret(String tradeSecretName) {
        return this.knownTradeSecrets.containsKey(tradeSecretName);
    }

    @Override
    public void readNbt(NbtCompound fishingCardNbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (!fishingCardNbt.contains(TAG)) {
            return;
        }
        NbtCompound progressionNbt = fishingCardNbt.getCompound(TAG);
        this.level = progressionNbt.getInt(LEVEL_TAG);
        this.exp = progressionNbt.getInt(EXP_TAG);
        this.admirationPoints = progressionNbt.getInt(ADMIRATION_POINTS_TAG);
        this.resetCount = progressionNbt.getInt(RESET_COUNT);
        this.readKnownTradeSecrets(progressionNbt);
        this.readSpells(progressionNbt);
    }

    private void readKnownTradeSecrets(NbtCompound fishingCardNbt){
        this.knownTradeSecrets.clear();
        //this.knownTradeSecrets.addAll(Arrays.stream((fishingCardNbt.getIntArray(TRADE_SECRETS_TAG))).boxed().toList());
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
        progressionNbt.putInt(LEVEL_TAG, this.level);
        progressionNbt.putInt(EXP_TAG, this.exp);
        progressionNbt.putInt(ADMIRATION_POINTS_TAG, this.admirationPoints);
        progressionNbt.putInt(RESET_COUNT, this.resetCount);
       // progressionNbt.putIntArray(TRADE_SECRETS_TAG, this.knownTradeSecrets);//todo with spells
        this.writeSpells(progressionNbt);
        fishingCardNbt.put(TAG, progressionNbt);
    }

    public void writeSpells(NbtCompound progressionNbt){
        NbtList spellsTag = new NbtList();
        spells.forEach((fishingPerkNbt, spellInstance) -> spellsTag.add(SpellInstance.toNbt(spellInstance)));
        progressionNbt.put(SPELLS_TAG, spellsTag);
    }


    private static final String TAG = "progression";
    private static final String LEVEL_TAG = "level";
    private static final String EXP_TAG = "exp";
    private static final String ADMIRATION_POINTS_TAG = "perk_points";
    private static final String RESET_COUNT = "reset_count";
    private static final String TRADE_SECRETS_TAG ="perks";
    private static final String SPELLS_TAG ="spells";
}

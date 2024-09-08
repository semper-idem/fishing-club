package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import org.jetbrains.annotations.Nullable;

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

    private final HashMap<String, TradeSecret.Instance> knownTradeSecrets = new HashMap<>();

    public ProgressionManager(FishingCard trackedFor) {
        super(trackedFor);
    }


    public int getResetCost() {
        return RESET_COST_PER_RESET * resetCount + RESET_COST_PER_PERK * knownTradeSecrets.size();
    }

    public void resetPerks() {
        this.resetCount++;
        int refundPerkPoints = knownTradeSecrets.size();
        //todo fix refund points calculation
        this.knownTradeSecrets.clear();
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
        TradeSecret.Instance instance = this.knownTradeSecrets.computeIfAbsent(tradeSecretName, tsn -> tradeSecret.instance());
        int nextLevelPoints = instance.upgradeCost();
        if (!this.hasAdmirationPoints(nextLevelPoints)){
            return;
        }
        this.admirationPoints -= nextLevelPoints;
        instance.upgrade();
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

    public void useTradeSecret(String tradeSecretName, @Nullable Entity target){
        if (!(this.trackedFor.holder() instanceof ServerPlayerEntity serverHolder)){
            return;
        }
        this.knownTradeSecrets.computeIfPresent(tradeSecretName, (key, instance) -> {
            boolean wasUsed = instance.use(serverHolder, target);
            if (wasUsed) {
                this.sync();
            }
            return instance;
        });
    }

    public int nextLevelXP(){
        return (int) Math.floor(BASE_EXP * Math.pow(level, EXP_EXPONENT));
    }

    public void grantExperience(double gainedXP){
        if (gainedXP < 0) {
            return;
        }
        //It was intentionally to not cast it to decimal but maybe we should
        trackedFor.holder().addExperience((int) Math.max(MIN_PLAYER_EXP, gainedXP * PLAYER_EXP_RATIO));

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

    public Collection<TradeSecret.Instance> tradeSecrets() {
        return this.knownTradeSecrets.values();
    }

    public void unlockAllSecrets() {
        for(TradeSecret tradeSecret : TradeSecrets.all()) {
            this.knownTradeSecrets.put(tradeSecret.name(), tradeSecret.instanceOfLevel(tradeSecret.maxLevel()));
        }
        sync();
    }
    public Set<String> getKnownTradeSecrets(){
        return this.knownTradeSecrets.keySet();
    }

    public int tradeSecretValue(TradeSecret tradeSecret) {
        if (this.knowsTradeSecret(tradeSecret)) {
            return tradeSecret.power(this.knownTradeSecrets.get(tradeSecret.name()).level());
        }
        return 0;
    }

    public boolean knowsTradeSecret(TradeSecret tradeSecret) {
        return this.knowsTradeSecret(tradeSecret.name(), 1);
    }

    public boolean knowsTradeSecret(TradeSecret tradeSecret, int level) {
        return this.knowsTradeSecret(tradeSecret.name(), level);
    }

    public boolean knowsTradeSecret(String tradeSecretName, int level) {
        if (!this.knownTradeSecrets.containsKey(tradeSecretName)) {
            return false;
        }
        return this.knownTradeSecrets.get(tradeSecretName).level() >= level;
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
    }

    private void readKnownTradeSecrets(NbtCompound progressionNbt){
        this.knownTradeSecrets.clear();
        progressionNbt.getList(TRADE_SECRETS_TAG, NbtElement.COMPOUND_TYPE).forEach(
                element -> {
                    TradeSecret.Instance instance = TradeSecret.Instance.fromNbt((NbtCompound) element);
                    this.knownTradeSecrets.put(instance.name(), instance);
                }
        );
    }

    @Override
    public void writeNbt(NbtCompound fishingCardNbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        NbtCompound progressionNbt = new NbtCompound();
        progressionNbt.putInt(LEVEL_TAG, this.level);
        progressionNbt.putInt(EXP_TAG, this.exp);
        progressionNbt.putInt(ADMIRATION_POINTS_TAG, this.admirationPoints);
        progressionNbt.putInt(RESET_COUNT, this.resetCount);
        this.writeKnownTradeSecrets(progressionNbt);
        fishingCardNbt.put(TAG, progressionNbt);
    }

    private void writeKnownTradeSecrets(NbtCompound progressionNbt) {
        NbtList tradeSecretsTag = new NbtList();
        this.knownTradeSecrets.forEach((name, instance) -> tradeSecretsTag.add(TradeSecret.Instance.toNbt(instance)));
        progressionNbt.put(TRADE_SECRETS_TAG, tradeSecretsTag);
    }



    private static final String TAG = "progression";
    private static final String LEVEL_TAG = "level";
    private static final String EXP_TAG = "exp";
    private static final String ADMIRATION_POINTS_TAG = "perk_points";
    private static final String RESET_COUNT = "reset_count";
    private static final String TRADE_SECRETS_TAG ="perks";
    private static final String SPELLS_TAG ="spells";
}

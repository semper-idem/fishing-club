package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.Random;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.managers.*;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.leaderboard.LeaderboardTracker;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import java.util.*;


public final class Card extends CardInventory implements EntityComponentInitializer, AutoSyncedComponent {
    public static final ComponentKey<Card> CARD = ComponentRegistry.getOrCreate(FishingClub.identifier("card"), Card.class);
    public static final Card DEFAULT = new Card();
    private PlayerEntity holder;
    private final ProgressionManager progressionManager;
    private final SummonRequestManager summonRequestManager;
    private final HistoryManager historyManager;
    private final LinkingManager linkingManager;
    private CheckedRandom random;

    private final StatusEffectHelper statusEffectHelper;//todo its prob util class, verify

    public static Card of(PlayerEntity playerEntity) {
        return CARD.get(playerEntity);
    }


    public Card() {
        this.progressionManager = new ProgressionManager(this);
        this.summonRequestManager = new SummonRequestManager(this);
        this.historyManager = new HistoryManager(this);
        this.linkingManager = new LinkingManager(this);
        this.statusEffectHelper = new StatusEffectHelper(this);
    }

    public Card(PlayerEntity playerEntity) {
        this();
        this.holder = playerEntity;
        this.random = new CheckedRandom(holder.getUuid().getLeastSignificantBits());
    }

    public Random getRandom() {
        return this.random;
    }

    public boolean isClient() {
        return holder.getWorld().isClient();
    }

    private void syncHolder() {
        CARD.sync(this.holder,
                (buf, recipient) -> {
                    NbtCompound tag = new NbtCompound();
                    writeNbt(tag, buf.getRegistryManager());
                    buf.writeNbt(tag);
                },
                player -> holder == player
        );
    }

    public void sell() {
        super.sell();
        syncHolder();
    }
    @Override
    public void setSharedBait(ItemStack baitToShare) {
        super.setSharedBait(baitToShare);
    }

    @Override
    public void readFromNbt(NbtCompound fishingCardNbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        progressionManager.readNbt(fishingCardNbt, wrapperLookup);
        historyManager.readNbt(fishingCardNbt, wrapperLookup);
        summonRequestManager.readNbt(fishingCardNbt, wrapperLookup);
        linkingManager.readNbt(fishingCardNbt, wrapperLookup);
        readNbt(fishingCardNbt, wrapperLookup);
    }

    @Override
    public void writeToNbt(NbtCompound fishingCardNbt, RegistryWrapper.WrapperLookup registryLookup) {
        progressionManager.writeNbt(fishingCardNbt, registryLookup);
        historyManager.writeNbt(fishingCardNbt, registryLookup);
        summonRequestManager.writeNbt(fishingCardNbt, registryLookup);
        linkingManager.writeNbt(fishingCardNbt, registryLookup);
        writeNbt(fishingCardNbt, registryLookup);
    }

    @Override
    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
        NbtCompound tag = new NbtCompound();
        this.writeToNbt(tag, buf.getRegistryManager());
        buf.writeNbt(tag);
    }

    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient, DataManager source) {
        NbtCompound tag = new NbtCompound();
        if (source == null) {
            this.writeNbt(tag, buf.getRegistryManager());
        } else {
            source.writeNbt(tag, buf.getRegistryManager());
        }
        buf.writeNbt(tag);
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        AutoSyncedComponent.super.applySyncPacket(buf);
    }

    public String getIssuedDate() {
        return this.historyManager.getIssuedDate();
    }

    public void addUnclaimedReward(ItemStack rewardStack) {
        historyManager.addUnclaimedReward(rewardStack);
    }

    public void claimReward(ItemStack rewardStack) {
        historyManager.claimReward(rewardStack);
    }

    public ArrayList<ItemStack> getUnclaimedRewards() {
        return historyManager.getUnclaimedRewards();
    }

    public void resetCooldown(){
       // progressionManager.resetCooldown();
    }

    public Collection<TradeSecret.Instance> tradeSecrets() {
        return this.progressionManager.tradeSecrets();
    }

    public boolean hasRequiredPerk(TradeSecret perk){
        return progressionManager.hasRequiredSecrets(perk);
    }

    public void setLevel(int level) {
        progressionManager.setLevel(level);
    }

    public void addSkillPoints(int amount){
        progressionManager.addPerkPoints(amount);
    }

    public void resetPerks(){
        if (canResetPerks()) {
            progressionManager.resetPerks();
        }
    }

    public boolean canResetPerks() {
        return getResetCost() <= getCredit() && !progressionManager.getKnownTradeSecrets().isEmpty();
    }

    public int getResetCost() {
        return progressionManager.getResetCost();
    }
    public int getPerkPoints(){
        return this.progressionManager.getAdmirationPoints();
    }

    public boolean hasPerkPoints(){
        return this.progressionManager.hasAdmirationPoints();
    }

    public void useTradeSecret(TradeSecret tradeSecret, @Nullable Entity target) {
        progressionManager.useTradeSecret(tradeSecret.name(), target);
    }

    public void useTradeSecret(String tradeSecret, @Nullable Entity target){
        progressionManager.useTradeSecret(tradeSecret, target);
    }

    public int nextLevelXP(){
        return progressionManager.nextLevelXP();
    }


    public void grantExperience(double gainedXP){
        progressionManager.grantExperience(gainedXP);
    }

    public int getLevel() {
        return progressionManager.getLevel();
    }

    public int getExp() {
        return progressionManager.getExp();
    }

    public float getExpProgress() {
        return this.progressionManager.getExpProgress();
    }

    public PlayerEntity unsafeHolder() {
        return this.holder;
    }

    public PlayerEntity holder(){
        return Objects.requireNonNull(this.holder, "Card holder did not load correctly");
    }

    public void shareStatusEffect(StatusEffectInstance sei, LivingEntity source, HashSet<UUID> sharedWith){
        linkingManager.shareStatusEffect(sei, source, sharedWith);
    }

    public void setSummonRequest(ServerPlayerEntity target){
        summonRequestManager.set(target);
    }

    public void acceptSummonRequest(){
        summonRequestManager.execute();//todo put cooldown on summon
    }

    public boolean isFishingFromBoat(){
        return holder.getVehicle() != null && holder.getVehicle() instanceof BoatEntity;
    }

    public int minQuality(IHookEntity caughtWith){
        int minQuality = 1;
        minQuality += historyManager.minQualityIncrement(caughtWith, progressionManager);
        minQuality += statusEffectHelper.minQualityIncrement();
        return Math.min(5, minQuality);
    }

    public void fishCaught(SpecimenData fish){

        this.progressionManager.grantExperience(fish.experience(this.statusEffectHelper.getExpMultiplier()));
        this.historyManager.fishCaught(fish);
        this.statusEffectHelper.fishCaught(fish);

        LeaderboardTracker tracker = LeaderboardTracker.of(holder.getScoreboard());
        tracker.record(holder, fish);
        tracker.record(holder, this, tracker.highestLevel);
    }

    public HashMap<String, SpeciesStatistics> getFishAtlas() {
        return historyManager.getFishAtlas();
    }

    public void learnTradeSecret(String tradeSecretName) {
        this.progressionManager.learnTradeSecret(tradeSecretName);
    }

    public int tradeSecretLevel(TradeSecret tradeSecret) {
        return this.progressionManager.tradeSecretLevel(tradeSecret);
    }

    public float tradeSecretValue(TradeSecret tradeSecret) {
        return this.progressionManager.tradeSecretValue(tradeSecret);
    }

    public boolean knowsTradeSecret(TradeSecret tradeSecret){
        return progressionManager.knowsTradeSecret(tradeSecret);
    }

    public boolean knowsTradeSecret(TradeSecret tradeSecret, int level) {
        return progressionManager.knowsTradeSecret(tradeSecret, level);
    }

    public boolean canUnlockPerk(TradeSecret perk){
        return progressionManager.canLearnSecret(perk);
    }

    public void linkTarget(Entity target){
        linkingManager.linkTarget(target);
    }

    public boolean addCredit(int credit) {
        if (this.credit + credit < 0) {
            return false;
        }
        this.credit += credit;
        LeaderboardTracker tracker = LeaderboardTracker.of(this.holder().getWorld().getScoreboard());
        tracker.record(holder, this, tracker.highestCredit);
        CARD.sync(this.holder, ((buf, recipient) -> writeSyncPacket(buf, recipient, null)));
        return true;
    }

    public void unlockAllSecret() {
        this.progressionManager.unlockAllSecrets();
    }

    public void requestSummon(){
        linkingManager.requestSummon();
    }

    public boolean isMember() {
        return this.historyManager.isMember();
    }
    @Override
    public String toString() {
        return "level: " + this.getLevel() +"   exp: " + this.getExp() + "/" + this.nextLevelXP();
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(CARD, Card::new, RespawnCopyStrategy.ALWAYS_COPY);

    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.holder;
    }

    //Message in bottle
    public void hearMessage() {
        this.historyManager.hearMessage();
    }

}

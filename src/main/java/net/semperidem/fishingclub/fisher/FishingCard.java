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
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.managers.*;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.leaderboard.LeaderboardTracker;
import net.semperidem.fishingclub.screen.dialog.DialogNode;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import java.util.*;


public final class FishingCard extends FishingCardInventory implements EntityComponentInitializer, AutoSyncedComponent {
    public static final ComponentKey<FishingCard> FISHING_CARD = ComponentRegistry.getOrCreate(FishingClub.identifier("fishing_card"), FishingCard.class);
    public static final FishingCard DEFAULT = new FishingCard();
    private PlayerEntity holder;
    private final ProgressionManager progressionManager;
    private final SummonRequestManager summonRequestManager;
    private final HistoryManager historyManager;
    private final LinkingManager linkingManager;

    private final StatusEffectHelper statusEffectHelper;//todo its prob util class, verify

    public static FishingCard of(PlayerEntity playerEntity) {
        return FISHING_CARD.get(playerEntity);
    }


    public FishingCard() {
        this.progressionManager = new ProgressionManager(this);
        this.summonRequestManager = new SummonRequestManager(this);
        this.historyManager = new HistoryManager(this);
        this.linkingManager = new LinkingManager(this);
        this.statusEffectHelper = new StatusEffectHelper(this);
    }

    public FishingCard(PlayerEntity playerEntity) {
        this();
        this.holder = playerEntity;
    }

    public boolean isClient() {
        return holder.getWorld().isClient();
    }

    @Override
    public void setSharedBait(ItemStack baitToShare) {
        super.setSharedBait(baitToShare);
        FISHING_CARD.sync(this.holder, ((buf, recipient) -> writeSyncPacket(buf, recipient, null)));
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

    public void giveDerekFish() {
        this.addCredit(1000);
        historyManager.giveDerekFish();
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

    public void meetDerek(FishermanEntity.SummonType summonType) {
        historyManager.meetDerek(summonType);
    }


    public void resetCooldown(){
       // progressionManager.resetCooldown();
    }

    public Collection<TradeSecret.Instance> tradeSecrets() {
        return this.progressionManager.tradeSecrets();
    }

    public boolean hasRequiredPerk(TradeSecret perk){
        return progressionManager.hasRequiredPerk(perk);
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

    public HashSet<DialogNode.DialogKey> getKeys() {
        HashSet<DialogNode.DialogKey> keys = new HashSet<>();
        if (this.historyManager.gaveDerekFish()) {
            keys.add(DialogNode.DialogKey.CARD);
        }
        return keys;
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

    public int tradeSecretValue(TradeSecret tradeSecret) {
        return this.progressionManager.tradeSecretValue(tradeSecret);
    }

    public boolean knowsTradeSecret(TradeSecret tradeSecret){
        return progressionManager.knowsTradeSecret(tradeSecret);
    }

    public boolean knowsTradeSecret(TradeSecret tradeSecret, int level) {
        return progressionManager.knowsTradeSecret(tradeSecret, level);
    }

    public boolean canUnlockPerk(TradeSecret perk){
        return progressionManager.canUnlockPerk(perk);
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
        FISHING_CARD.sync(this.holder, ((buf, recipient) -> writeSyncPacket(buf, recipient, null)));
        return true;
    }

    public void unlockAllSecret() {
        this.progressionManager.unlockAllSecrets();
    }

    @Override
    public ItemStack removeStack(int slot) {
        ItemStack result = super.removeStack(slot);
        FISHING_CARD.sync(this.holder, ((buf, recipient) -> writeSyncPacket(buf, recipient, null)));
        return result;
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack result = super.removeStack(slot, amount);
        FISHING_CARD.sync(this.holder, ((buf, recipient) -> writeSyncPacket(buf, recipient, null)));
        return result;
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        super.setStack(slot, stack);
        FISHING_CARD.sync(this.holder, ((buf, recipient) -> writeSyncPacket(buf, recipient, null)));
    }

    public void requestSummon(){
        linkingManager.requestSummon();
    }

    public boolean isMember() {
        return this.historyManager.gaveDerekFish();
    }
    @Override
    public String toString() {
        return "";
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(FISHING_CARD, FishingCard::new, RespawnCopyStrategy.ALWAYS_COPY);

    }
}

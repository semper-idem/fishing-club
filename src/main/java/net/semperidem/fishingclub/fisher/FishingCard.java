package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.LeaderboardTrackingServer;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.FishComponent;
import net.semperidem.fishingclub.fisher.managers.*;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.leaderboard.LeaderboardTracker;
import net.semperidem.fishingclub.screen.dialog.DialogKey;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public final class FishingCard extends FishingCardInventory implements EntityComponentInitializer, AutoSyncedComponent {
    public static final ComponentKey<FishingCard> FISHING_CARD = ComponentRegistry.getOrCreate(FishingClub.getIdentifier("fishing_card"), FishingCard.class);
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

    public void giveDerekFish() {
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
        progressionManager.resetCooldown();
    }

    public boolean hasRequiredPerk(FishingPerk perk){
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
        return getResetCost() <= getCredit() && !progressionManager.getPerks().isEmpty();
    }

    public int getResetCost() {
        return progressionManager.getResetCost();
    }
    public int getPerkPoints(){
        return this.progressionManager.getPerkPoints();
    }

    public boolean hasPerkPoints(){
        return this.progressionManager.hasPerkPoints();
    }

    public void useSpell(String perkName, Entity target){
        progressionManager.useSpell(perkName, target);
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

    public PlayerEntity getHolder(){
        return holder;
    }

    public void shareStatusEffect(StatusEffectInstance sei){
        linkingManager.shareStatusEffect(sei);
    }

    public void setSummonRequest(ServerPlayerEntity target){
        summonRequestManager.set(target);
    }

    public void acceptSummonRequest(){
        summonRequestManager.execute();//todo put cooldown on summon
    }

    public HashSet<DialogKey> getKeys(FishermanEntity.SummonType summonType) {
        HashSet<DialogKey> keys = new HashSet<>();
        keys.add(historyManager.metDerek(summonType) ? DialogKey.NOT_UNIQUE : DialogKey.UNIQUE);
        keys.add(historyManager.gaveDerekFish() ? DialogKey.WELCOME: DialogKey.NOT_WELCOME);
        return keys;
    }

    public boolean isFishingFromBoat(){
        return holder.getVehicle() != null && holder.getVehicle() instanceof BoatEntity;
    }
    public int getMinGrade(){
        int minGrade = 1;
        minGrade += historyManager.getMinGrade(progressionManager);
        minGrade += statusEffectHelper.getMinGrade();
        return Math.min(5, minGrade);
    }

    public void fishHooked(IHookEntity hookEntity){;
        historyManager.fishHooked(hookEntity);
    }

    public void fishCaught(FishComponent fish){
        int expGained = fish.experience();
        expGained += (int) statusEffectHelper.getExpMultiplier();
        float passiveExpMultiplier = 1 + 0.1f * statusEffectHelper.spreadStatusEffect(progressionManager, fish);
        expGained = (int)(expGained * passiveExpMultiplier);

        progressionManager.grantExperience(expGained);
        historyManager.fishCaught(fish);
        statusEffectHelper.fishCaught(progressionManager);

        if (holder.getServer() instanceof LeaderboardTrackingServer leaderboardTrackingServer) {
            LeaderboardTracker tracker = leaderboardTrackingServer.getLeaderboardTracker();
            tracker.record(holder, fish);
            tracker.record(holder, this, tracker.highestLevel);
        }
    }

    public HashMap<String, SpeciesStatistics> getFishAtlas() {
        return historyManager.getFishAtlas();
    }

    public void addPerk(String perkName){
        progressionManager.addPerk(perkName);
    }

    public boolean hasPerk(FishingPerk perk){
        return progressionManager.hasPerk(perk);
    }

    public boolean canUnlockPerk(FishingPerk perk){
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
        if (holder.getServer() instanceof LeaderboardTrackingServer leaderboardTrackingServer) {
            LeaderboardTracker tracker = leaderboardTrackingServer.getLeaderboardTracker();
            tracker.record(holder, this, tracker.highestCredit);
        }
        return true;
    }

    public void requestSummon(){
        linkingManager.requestSummon();
    }

    public void shareBait() {
        linkingManager.shareBait(historyManager.getLastUsedBait().copy());
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

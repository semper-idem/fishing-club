package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.managers.*;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.leaderboard.LeaderboardManager;
import net.semperidem.fishingclub.screen.dialog.DialogKey;

import java.util.ArrayList;
import java.util.HashSet;


public class FishingCard extends FishingCardInventory {
    private final PlayerEntity holder;

    private final ProgressionManager progressionManager;
    private final SummonRequestManager summonRequestManager;
    private final HistoryManager historyManager;
    private final LinkingManager linkingManager;

    private final StatusEffectHelper statusEffectHelper;


    public FishingCard(PlayerEntity playerEntity) {
        this.holder = playerEntity;
        this.progressionManager = new ProgressionManager(this);
        this.summonRequestManager = new SummonRequestManager(this);
        this.historyManager = new HistoryManager(this);
        this.linkingManager = new LinkingManager(this);
        this.statusEffectHelper = new StatusEffectHelper(this);
    }

    public FishingCard(PlayerEntity playerEntity, NbtCompound fishingCardNbt) {
        this(playerEntity);
        progressionManager.readNbt(fishingCardNbt);
        historyManager.readNbt(fishingCardNbt);
        summonRequestManager.readNbt(fishingCardNbt);
        linkingManager.readNbt(fishingCardNbt);
        readNbt(fishingCardNbt);
    }

    public NbtCompound toNbt() {
        NbtCompound fishingCardNbt = new NbtCompound();
        progressionManager.writeNbt(fishingCardNbt);
        historyManager.writeNbt(fishingCardNbt);
        summonRequestManager.writeNbt(fishingCardNbt);
        linkingManager.writeNbt(fishingCardNbt);
        writeNbt(fishingCardNbt);
        return fishingCardNbt;
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

    public static FishingCard getPlayerCard(PlayerEntity user){
        return ((FishingPlayerEntity)user).getCard();
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
        summonRequestManager.execute();
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
        int minGrade = 0;
        minGrade += historyManager.getMinGrade(progressionManager);
        minGrade += statusEffectHelper.getMinGrade();
        return Math.min(4, minGrade);
    }

    public void fishHooked(IHookEntity hookEntity){;
        historyManager.fishHooked(hookEntity);
    }

    public void fishCaught(Fish fish){
        int expGained = fish.experience;
        expGained += (int) statusEffectHelper.getExpMultiplier();
        float passiveExpMultiplier = 1 + 0.1f * statusEffectHelper.spreadStatusEffect(progressionManager, fish);
        expGained = (int)(expGained * passiveExpMultiplier);

        progressionManager.grantExperience(expGained);
        historyManager.fishCaught();
        statusEffectHelper.fishCaught(progressionManager, fish);

        LeaderboardManager.record(holder, fish);
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

    public void requestSummon(){
        linkingManager.requestSummon();
    }

    public void shareBait() {
        linkingManager.shareBait(historyManager.getLastUsedBait().copy());
    }
}

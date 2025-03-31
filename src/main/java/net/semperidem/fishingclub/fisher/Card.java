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
    private final CardProgression progression;
    private final CardHistory history;
    private final CardLinking linking;
    private CheckedRandom random;

    private final StatusEffectHelper statusEffectHelper;//todo its prob util class, verify

    public static Card of(PlayerEntity playerEntity) {
        return CARD.get(playerEntity);
    }


    public Card() {
        this.progression = new CardProgression(this);
        this.history = new CardHistory(this);
        this.linking = new CardLinking(this);
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

    public void sell() {
        super.sell();
        sync();
    }
    @Override
    public void setSharedBait(ItemStack baitToShare) {
        super.setSharedBait(baitToShare);
    }

    @Override
    public void readFromNbt(NbtCompound fishingCardNbt, RegistryWrapper.WrapperLookup wrapperLookup) {
        progression.readNbt(fishingCardNbt, wrapperLookup);
        history.readNbt(fishingCardNbt, wrapperLookup);
        linking.readNbt(fishingCardNbt, wrapperLookup);
        readNbt(fishingCardNbt, wrapperLookup);
    }

    @Override
    public void writeToNbt(NbtCompound fishingCardNbt, RegistryWrapper.WrapperLookup registryLookup) {
        progression.writeNbt(fishingCardNbt, registryLookup);
        history.writeNbt(fishingCardNbt, registryLookup);
        linking.writeNbt(fishingCardNbt, registryLookup);
        writeNbt(fishingCardNbt, registryLookup);
    }

    @Override
    public void writeSyncPacket(RegistryByteBuf buf, ServerPlayerEntity recipient) {
        NbtCompound tag = new NbtCompound();
        this.writeToNbt(tag, buf.getRegistryManager());
        buf.writeNbt(tag);
    }


    public void writeSyncPacket(RegistryByteBuf buf) {
        NbtCompound tag = new NbtCompound();
        this.writeNbt(tag, buf.getRegistryManager());
        buf.writeNbt(tag);
    }

    @Override
    public void applySyncPacket(RegistryByteBuf buf) {
        AutoSyncedComponent.super.applySyncPacket(buf);
    }

    public String getIssuedDate() {
        return this.history.getIssuedDate();
    }

    public void addUnclaimedReward(ItemStack rewardStack) {
        history.addUnclaimedReward(rewardStack);
    }

    public void claimReward(ItemStack rewardStack) {
        history.claimReward(rewardStack);
    }

    public ArrayList<ItemStack> getUnclaimedRewards() {
        return history.getUnclaimedRewards();
    }

    public void resetCooldown(){
       // progressionManager.resetCooldown();
    }

    public Collection<TradeSecret.Instance> tradeSecrets() {
        return this.progression.tradeSecrets();
    }

    public boolean hasRequiredPerk(TradeSecret perk){
        return progression.hasRequiredSecrets(perk);
    }

    public void setLevel(int level) {
        progression.setLevel(level);
    }

    public void addSkillPoints(int amount){
        progression.addPerkPoints(amount);
    }

    public void resetPerks(){
        if (canResetPerks()) {
            progression.resetPerks();
        }
    }

    public boolean canResetPerks() {
        return getResetCost() <= getCredit() && !progression.getKnownTradeSecrets().isEmpty();
    }

    public int getResetCost() {
        return progression.getResetCost();
    }
    public int getPerkPoints(){
        return this.progression.getAdmirationPoints();
    }

    public boolean hasPerkPoints(){
        return this.progression.hasAdmirationPoints();
    }

    public void useTradeSecret(TradeSecret tradeSecret, @Nullable Entity target) {
        progression.useTradeSecret(tradeSecret.name(), target);
    }

    public void useTradeSecret(String tradeSecret, @Nullable Entity target){
        progression.useTradeSecret(tradeSecret, target);
    }

    public int nextLevelXP(){
        return progression.nextLevelXP();
    }


    public void grantExperience(double gainedXP){
        progression.grantExperience(gainedXP);
    }

    public int getLevel() {
        return progression.getLevel();
    }

    public int getExp() {
        return progression.getExp();
    }

    public float getExpProgress() {
        return this.progression.getExpProgress();
    }

    public PlayerEntity unsafeHolder() {
        return this.holder;
    }

    public PlayerEntity owner(){
        return Objects.requireNonNull(this.holder, "Card holder did not load correctly");
    }

    public void shareStatusEffect(StatusEffectInstance sei, LivingEntity source, HashSet<UUID> sharedWith){
        linking.shareStatusEffect(sei, source, sharedWith);
    }

    public void setSummonRequest(ServerPlayerEntity target){
        linking.set(target);
    }

    public void acceptSummonRequest(){
        linking.execute();//todo put cooldown on summon
    }

    public boolean isFishingFromBoat(){
        return holder.getVehicle() != null && holder.getVehicle() instanceof BoatEntity;
    }

    public int minQuality(IHookEntity caughtWith){
        int minQuality = 1;
        minQuality += history.minQualityIncrement(caughtWith, progression);
        minQuality += statusEffectHelper.minQualityIncrement();
        return Math.min(5, minQuality);
    }

    public void fishCaught(SpecimenData fish){

        this.progression.grantExperience(fish.experience(this.statusEffectHelper.getExpMultiplier()));
        this.history.fishCaught(fish);
        this.statusEffectHelper.fishCaught(fish);

        LeaderboardTracker tracker = LeaderboardTracker.of(holder.getScoreboard());
        tracker.record(holder, fish);
        tracker.record(holder, this, tracker.highestLevel);
    }

    public HashMap<String, AtlasEntry> getFishAtlas() {
        return history.getFishAtlas();
    }

    public void learnTradeSecret(String tradeSecretName) {
        this.progression.learnTradeSecret(tradeSecretName);
    }

    public int tradeSecretLevel(TradeSecret tradeSecret) {
        return this.progression.tradeSecretLevel(tradeSecret);
    }

    public float tradeSecretValue(TradeSecret tradeSecret) {
        return this.progression.tradeSecretValue(tradeSecret);
    }

    public boolean knowsTradeSecret(TradeSecret tradeSecret){
        return progression.knowsTradeSecret(tradeSecret);
    }

    public boolean knowsTradeSecret(TradeSecret tradeSecret, int level) {
        return progression.knowsTradeSecret(tradeSecret, level);
    }

    public boolean canUnlockPerk(TradeSecret perk){
        return progression.canLearnSecret(perk);
    }

    public void linkTarget(Entity target){
        linking.linkTarget(target);
    }

    public boolean addCredit(int credit) {
        if (this.credit + credit < 0) {
            return false;
        }
        this.credit += credit;
        LeaderboardTracker tracker = LeaderboardTracker.of(this.owner().getWorld().getScoreboard());
        tracker.record(holder, this, tracker.highestCredit);
        return true;
    }

    private void sync() {
        CARD.sync(this.holder, (buf, recipient) -> writeSyncPacket(buf));
    }

    public void unlockAllSecret() {
        this.progression.unlockAllSecrets();
    }

    public void requestSummon(){
        linking.requestSummon();
    }

    public boolean isMember() {
        return this.history.isMember();
    }
    @Override
    public String toString() {
        return "level: " + this.getLevel() +"   exp: " + this.getExp() + "/" + this.nextLevelXP();
    }

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(CARD, Card::new, RespawnCopyStrategy.ALWAYS_COPY);

    }
    private Card get() {
        return this;
    }

    @Override
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.holder;
    }

    //Message in bottle
    public void hearMessage() {
        this.history.hearMessage();
    }
}

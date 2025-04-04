package net.semperidem.fishingclub.fisher;

import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.Random;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.managers.*;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.leaderboard.LeaderboardManager;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.Tags;
import org.jetbrains.annotations.Nullable;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

import java.util.*;


public final class Card extends CardInventory implements AutoSyncedComponent{
    private final PlayerEntity owner;
    private final CardProgression progression;
    private final CardHistory history;
    private final CardLinking linking;
    private final CheckedRandom random;

    public static Card of(PlayerEntity playerEntity) {
        return Components.CARD.get(playerEntity);
    }

    public Card(PlayerEntity playerEntity) {
        this.progression = new CardProgression(this);
        this.history = new CardHistory(this);
        this.linking = new CardLinking(this);
        this.owner = playerEntity;
        this.random = new CheckedRandom(owner.getUuid().getLeastSignificantBits());
    }

    public Random getRandom() {
        return this.random;
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
        return progression.hasRequiredSecret(perk);
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
        return getResetCost() <= getGS() && !progression.getKnownTradeSecrets().isEmpty();
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

    public PlayerEntity owner(){
        return Objects.requireNonNull(this.owner, "Card owner did not load correctly");
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

    public int minQuality(ChunkPos chunkPos){
        int minQuality = 1;
        minQuality += history.minQualityIncrement(chunkPos, progression);
        minQuality += StatusEffectHelper.minQualityIncrement(owner);
        return Math.min(5, minQuality);
    }

    public void fishCaught(SpecimenData fish){
        this.progression.grantExperience(fish.experience(StatusEffectHelper.getExpMultiplier(owner)));

        this.history.fishCaught(fish);

        StatusEffectHelper.triggerQualityCelebration(this, fish);
        StatusEffectHelper.triggerPassiveXP(this);

        LeaderboardManager tracker = LeaderboardManager.of(owner.getScoreboard());
        tracker.record(owner, fish);
        tracker.record(owner, this, tracker.highestLevel);
    }

    public boolean isKing() {
        UUID kingUUID = FishingKing.of(owner.getScoreboard()).getUuid();
        if (kingUUID == null) {
            return true;
        }
        return kingUUID.equals(owner.getUuid());
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

    public void sell() {
        for(int i = 0; i < SLOT_COUNT; i++) {
            ItemStack inventoryStack = inventory.get(i);
            if (!inventoryStack.isIn(Tags.FISH_ITEM)) {
                continue;
            }
            goldenScales += (inventoryStack.getOrDefault(Components.SPECIMEN_DATA, SpecimenData.DEFAULT).value() * inventoryStack.getCount());
            inventoryStack.setCount(0);
            break;
        }
        sync();
    }

    public void addGS(int amount) {
        if (this.goldenScales + amount < 0) {
            return;
        }
        this.goldenScales += amount;
        sync();

        LeaderboardManager tracker = LeaderboardManager.of(this.owner().getWorld().getScoreboard());
        tracker.record(owner, this, tracker.highestCredit);
    }

    private void sync() {
        Components.CARD.sync(this.owner, (buf, recipient) -> writeSyncPacket(buf));
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
    public boolean shouldSyncWith(ServerPlayerEntity player) {
        return player == this.owner;
    }
    //TODO LANG
    private static final Map<Integer, Text> MIN_LEVEL_TO_TITLE = new ImmutableMap.Builder<Integer, Text>()
            .put(0, Text.of("Casual"))
            .put(10, Text.of("Novice"))
            .put(20, Text.of("Beginner"))
            .put(30, Text.of("Competent"))
            .put(40, Text.of("Respected"))
            .put(50, Text.of("Skillful"))
            .put(100, Text.of("Expert"))
            .put(250, Text.of("Master"))
            .put(500, Text.of("Grandmaster"))
            .put(1000, Text.of("Ol'stinker"))
            .build();

    public Text getTitle() {
        return MIN_LEVEL_TO_TITLE
                .keySet()
                .stream()
                .sorted((a, b) -> b - a)
                .filter(titleLevel -> getLevel() >= titleLevel)
                .findFirst()
                .map(MIN_LEVEL_TO_TITLE::get)
                .orElse(Text.empty());
    }


    //Message in bottle
    public void hearMessage() {
        this.history.hearMessage();
    }
}

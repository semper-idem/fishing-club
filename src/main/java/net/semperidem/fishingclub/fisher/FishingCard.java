package net.semperidem.fishingclub.fisher;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.FishingDatabase;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreenHandler;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.level_reward.LevelReward;
import net.semperidem.fishingclub.fisher.level_reward.LevelRewardRule;
import net.semperidem.fishingclub.fisher.managers.LinkingManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.fisher.perks.spells.Spells;
import net.semperidem.fishingclub.fisher.managers.ChunkManager;
import net.semperidem.fishingclub.fisher.managers.SummonRequestManager;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;
import net.semperidem.fishingclub.network.ServerPacketSender;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;

import java.util.HashMap;


public class FishingCard {

    //Progression manager?
    private static final int BASE_EXP = 50;
    private static final float EXP_EXPONENT = 1.25f;
    int level = 1;
    int exp = 0;
    int skillPoints = 0;
    final HashMap<String, FishingPerk> perks = new HashMap<>();
    final HashMap<FishingPerk, SpellInstance> spells = new HashMap<>();

    //Inventory manager
    //Fishing vest etc?
    ItemStack sharedBait = ItemStack.EMPTY;
    SimpleInventory fisherInventory = new SimpleInventory(FishingCardScreenHandler.SLOT_COUNT);
    int credit = 0;

    //HistoryManager?
    long lastFishCaughtTime = 0;
    long firstFishOfTheDayCaughtTime = 0;
    ItemStack lastUsedBait = ItemStack.EMPTY;

    SummonRequestManager summonRequestManager;
    ChunkManager chunkManager;
    LinkingManager linkingManager;

    private final PlayerEntity holder;


    public FishingCard(PlayerEntity playerEntity) {
        this.holder = playerEntity;
        this.summonRequestManager = new SummonRequestManager(this);
        this.chunkManager = new ChunkManager(this);
        this.linkingManager = new LinkingManager(this);
    }

    public PlayerEntity getHolder(){
        return holder;
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

    public int getCredit(){
        return credit;
    }


    public void setSharedBait(ItemStack baitToShare){
        this.sharedBait = baitToShare;
    }

    public void shareStatusEffect(StatusEffectInstance sei){
        linkingManager.shareStatusEffect(sei);
    }

    public ItemStack getSharedBait(){
        return sharedBait;
    }

    public void setSummonRequest(ServerPlayerEntity target){
        summonRequestManager.set(target);
    }

    public void acceptSummonRequest(){
        summonRequestManager.execute();
    }

    void addPerk(FishingPerk perk){
        if (availablePerk(perk) && hasSkillPoints()) {
            perk.onEarn(holder);
            this.perks.put(perk.getName(), perk);
            if (Spells.perkHasSpell(perk)) {
                this.spells.put(perk, SpellInstance.getSpellInstance(perk, 0));
                ServerPacketSender.sendSpellsUpdate(this);
            }
            skillPoints--;
        }
        ServerPacketSender.sendPerksUpdate(this);
    }

    public void addPerk(String perkName){
        FishingPerks.getPerkFromName(perkName).ifPresent(this::addPerk);
    }

    public boolean availablePerk(FishingPerk perk){
        return perk.getParent() == null || this.perks.containsKey(perk.getParent().getName());
    }

    public void addSkillPoints(int amount){
        this.skillPoints+= amount;
    }

    public boolean isFishingFromBoat(){
        return holder.getVehicle() != null && holder.getVehicle() instanceof BoatEntity;
    }
    public int getMinGrade(){
        int minGrade = 0;
        long worldTime = this.holder.world.getTime();
        if (this.firstFishOfTheDayCaughtTime + 24000 < worldTime) {
            if (hasPerk(FishingPerks.FIRST_CATCH)) {
                minGrade++;
            }
            minGrade++;
        }
        if (this.holder.hasStatusEffect(FStatusEffectRegistry.QUALITY_BUFF) && Math.random() > 0.25f) {
            minGrade++;
        } else if (this.holder.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)){
            minGrade++;
        }

        if (hasPerk(FishingPerks.QUALITY_TIME_INCREMENT) && lastFishCaughtTime - worldTime > 24000) {
            int daysSinceLastFish = (int) ((lastFishCaughtTime - worldTime) / 24000f);
            while (daysSinceLastFish > 4) {
                minGrade++;
                daysSinceLastFish -= 4;
            }
            if (Math.random() < 0.25f * daysSinceLastFish) {
                minGrade++;
            }
        }
        return Math.min(4, minGrade);
    }

    public int getSkillPoints(){
        return this.skillPoints;
    }

    public boolean hasSkillPoints(){
        return this.skillPoints > 0;
    }

    void setSkillPoints(int skillPoints){
        this.skillPoints = skillPoints;
    }

    public void useSpell(FishingPerk fishingPerk, Entity target){
        if (!perks.containsKey(fishingPerk.getName())) return;
        SpellInstance spellInstance = spells.get(fishingPerk);
        spellInstance.use((ServerPlayerEntity) holder, target);
        spells.put(fishingPerk, spellInstance);
    }


    void removePerk(String perkName){
        if (!this.perks.containsKey(perkName)) return;
        this.perks.remove(perkName);
        this.skillPoints++;
    }

    public int nextLevelXP(){
        return (int) Math.floor(BASE_EXP * Math.pow(level, EXP_EXPONENT));
    }

    public void setFirstFishOfTheDayCaughtTime(long time) {
        this.firstFishOfTheDayCaughtTime = time;
    }

    public void setLastFishCaughtTime(long time){
        this.lastFishCaughtTime = time;
    }

    public void fishHooked(IHookEntity hookEntity){
        lastUsedBait = FishingRodPartController.getPart(hookEntity.getCaughtUsing(), FishingRodPartType.BAIT);
        chunkManager.fishedInChunk(hookEntity.getFishedInChunk());
    }

    public void fishCaught(Fish fish){
        int expGained = fish.experience;
        if (holder.hasStatusEffect(FStatusEffectRegistry.EXP_BUFF)) {
            float multiplier = (float) (1 + 0.1 * (holder.getStatusEffect(FStatusEffectRegistry.EXP_BUFF).getAmplifier() + 1));
            expGained = (int) (expGained * multiplier);
        }

        Box box = new Box(holder.getBlockPos());
        box.expand(3);
        boolean qualitySharing = fish.grade >= 4 && hasPerk(FishingPerks.QUALITY_SHARING) && !holder.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF) && !fish.consumeGradeBuff;
        float passivExpMultiplier = 1;
        for(Entity entity : holder.getEntityWorld().getOtherEntities(null, box)) {
            if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                if (FishingDatabase.getCard(entity.getUuid()).hasPerk(FishingPerks.PASSIVE_FISHING_XP)) {
                    passivExpMultiplier += 0.1f;
                }
                if (qualitySharing && !serverPlayerEntity.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)) {
                    serverPlayerEntity.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF, 2400));
                }
            }
        }
        expGained = (int)(expGained * passivExpMultiplier);

        long worldTime = holder.world.getTime();
        grantExperience(expGained);
        setLastFishCaughtTime(worldTime);
        firstFishOfTheDayCaught(worldTime);
        processOneTimeBuff(fish);
        prolongStatusEffects();
    }


    private void prolongStatusEffects(){
        if (!hasPerk(FishingPerks.SHARED_BUFFS)) {
            return;
        }

        if (!this.holder.hasVehicle()) {
            return;
        }

        if (!(this.holder.getVehicle() instanceof BoatEntity boatEntity)) {
            return;
        }

        ImmutableList<Entity> passengers = (ImmutableList<Entity>) boatEntity.getPassengerList();

        for(Entity passenger : passengers) {
            if(!(passenger instanceof PlayerEntity playerPassenger)) continue;
            playerPassenger.getStatusEffects().forEach(
                    sei -> sei.upgrade(
                            new StatusEffectInstance(
                                    sei.getEffectType(),
                                    sei.getDuration() + 200,
                                    sei.getAmplifier()
                            )
                    )
            );
        }

    }

    private void firstFishOfTheDayCaught(long worldTime){
        if (worldTime < firstFishOfTheDayCaughtTime + 24000) {
            return;
        }
        setFirstFishOfTheDayCaughtTime(worldTime);
        if (hasPerk(FishingPerks.FREQUENT_CATCH_FIRST_CATCH)) {
            holder.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.FREQUENCY_BUFF,120));
        }
        if (hasPerk(FishingPerks.QUALITY_INCREASE_FIRST_CATCH)) {
            holder.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.QUALITY_BUFF,120));
        }
    }

    public boolean hasFreshChunkBuff(ChunkPos chunkPos) {
        return chunkManager.fishedInChunk(chunkPos);
    }

    private void processOneTimeBuff(Fish fish){
        if (!fish.consumeGradeBuff) {
            return;
        }
        if (!holder.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)) {
            return;
        }
        consumeOneTimeBuff();
    }

    private void consumeOneTimeBuff(){
        StatusEffectInstance sei = holder.getStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        int effectPower = sei.getAmplifier();
        holder.removeStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        if (effectPower > 0) {
            StatusEffectInstance lowerSei = new StatusEffectInstance(sei.getEffectType(), sei.getDuration(), effectPower - 1);
            holder.addStatusEffect(lowerSei);
        }
    }

    public void grantExperience(double gainedXP){
        if (gainedXP == 0) {
            return;
        }

        holder.addExperience((int) Math.max(1, gainedXP / 10));

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
            reward.grant(this);
        }
    }

    public boolean addCredit(int credit) {
        if (this.credit + credit < 0) {
            return false;
        }

        this.credit += credit;
        return true;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public HashMap<String, FishingPerk> getPerks(){
        return perks;
    }

    public boolean hasPerk(FishingPerk perk) {
        return perks.containsKey(perk.getName());
    }

    public SimpleInventory getFisherInventory(){
        return this.fisherInventory;
    }

    public void linkTarget(Entity target){
        linkingManager.linkTarget(target);
    }

    public void requestSummon(){
        linkingManager.requestSummon();
    }

    public void shareBait() {
        linkingManager.shareBait(lastUsedBait.copy());
    }
    @Override
    public String toString(){
        return "\n============[Fisher Info]============" +
                "\nLevel: " + level +
                "\nExperience: " + exp +
                "\nPerk Count: " + perks.size() +
                "\nCredit: " + credit +
                "\n============[Fisher Info]============";
    }

}

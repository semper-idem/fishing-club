package net.semperidem.fishingclub.fisher;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.managers.HistoryManager;
import net.semperidem.fishingclub.fisher.managers.LinkingManager;
import net.semperidem.fishingclub.fisher.managers.ProgressionManager;
import net.semperidem.fishingclub.fisher.managers.SummonRequestManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;


public class FishingCard extends FishingCardInventory {
    ProgressionManager progressionManager;
    SummonRequestManager summonRequestManager;
    HistoryManager historyManager;
    LinkingManager linkingManager;

    private final PlayerEntity holder;


    public FishingCard(PlayerEntity playerEntity) {
        this.holder = playerEntity;
        this.progressionManager = new ProgressionManager(this);
        this.summonRequestManager = new SummonRequestManager(this);
        this.historyManager = new HistoryManager(this);
        this.linkingManager = new LinkingManager(this);
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

    public static FishingCard getPlayerCard(PlayerEntity user){
        return ((FishingPlayerEntity)user).getCard();
    }

    public void resetCooldown(){
        progressionManager.resetCooldown();
    }

    public boolean hasRequiredPerk(FishingPerk perk){
        return progressionManager.hasRequiredPerk(perk);
    }

    public void addSkillPoints(int amount){
        progressionManager.addSkillPoints(amount);
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

    public boolean isFishingFromBoat(){
        return holder.getVehicle() != null && holder.getVehicle() instanceof BoatEntity;
    }
    public int getMinGrade(){
        int minGrade = 0;
        if (historyManager.isFirstCatchInChunk()) {
            minGrade++;
        }
        if (historyManager.isFirstCatchOfTheDay()) {
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
        int daysSinceLastFish = historyManager.getDaysSinceLastCatch();
        if (hasPerk(FishingPerks.QUALITY_TIME_INCREMENT) &&  daysSinceLastFish > 0) {
            minGrade = (int) (minGrade + Math.floor(daysSinceLastFish / 4f));
            if (Math.random() < 0.25f * (daysSinceLastFish % 4)) {
                minGrade++;
            }
        }
        return Math.min(4, minGrade);
    }


    public void fishHooked(IHookEntity hookEntity){;
        historyManager.fishHooked(hookEntity);
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
                if (getPlayerCard(serverPlayerEntity).hasPerk(FishingPerks.PASSIVE_FISHING_XP)) {
                    passivExpMultiplier += 0.1f;
                }
                if (qualitySharing && !serverPlayerEntity.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)) {
                    serverPlayerEntity.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF, 2400));
                }
            }
        }
        expGained = (int)(expGained * passivExpMultiplier);

        progressionManager.grantExperience(expGained);
        historyManager.fishCaught();
        processOneTimeBuff(fish);
        prolongStatusEffects();
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

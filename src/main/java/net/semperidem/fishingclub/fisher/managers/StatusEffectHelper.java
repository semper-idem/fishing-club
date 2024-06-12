package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.StatusEffectRegistry;

import java.util.List;

public class StatusEffectHelper {
    private static final float QUALITY_BUFF_SUCCESS_CHANCE = 0.25f;
    private static final float EXP_MULTIPLIER_PER_AMPLIFIER = 0.1f;
    private static final int FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER = 4;
    private static final int SPREAD_EFFECT_RANGE = 3;
    private static final int PROLONG_EFFECT_LENGTH = 200;
    private static final int ONE_TIME_BUFF_LENGTH = 2400;

    FishingCard trackedFor;
    PlayerEntity holder;

    public StatusEffectHelper(FishingCard trackedFor) {
        this.trackedFor = trackedFor;
        this.holder = trackedFor.getHolder();
    }

    public int getMinGrade() {
        int minGrade = 0;
        if (this.holder.hasStatusEffect(StatusEffectRegistry.QUALITY_BUFF) && Math.random() > QUALITY_BUFF_SUCCESS_CHANCE) {
            minGrade++;
        } else if (this.holder.hasStatusEffect(StatusEffectRegistry.ONE_TIME_QUALITY_BUFF)){
            minGrade++;
        }
        return minGrade;
    }

    public void fishCaught(ProgressionManager progressionManager, Fish fish){
        processOneTimeBuff(fish);
        prolongStatusEffects(progressionManager);
    }

    public float getExpMultiplier() {
        StatusEffectInstance xpBuffInstance;
        float multiplier = 1;
        if ((xpBuffInstance = holder.getStatusEffect(StatusEffectRegistry.EXP_BUFF)) != null) {
             multiplier += EXP_MULTIPLIER_PER_AMPLIFIER * (xpBuffInstance.getAmplifier() + 1);
        }
        return multiplier;
    }

    private boolean shouldSpreadQualityBuff(ProgressionManager progressionManager, Fish fish) {
        return
                fish != null &&
                fish.grade >= FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER &&
                progressionManager.hasPerk(FishingPerks.QUALITY_SHARING) &&
                !holder.hasStatusEffect(StatusEffectRegistry.ONE_TIME_QUALITY_BUFF) &&
                !fish.consumeGradeBuff;
    }

    public int spreadStatusEffect(ProgressionManager progressionManager, Fish fish) {
        int xpBuffAffectedCount = 0;
        Box box = new Box(holder.getBlockPos());
        box.expand(SPREAD_EFFECT_RANGE);

        List<ServerPlayerEntity> nearPlayers = holder.getEntityWorld()
                .getOtherEntities(holder, box)
                .stream()
                .filter(ServerPlayerEntity.class::isInstance)
                .map(ServerPlayerEntity.class::cast)
                .toList();

        boolean spreadQualityBuff = shouldSpreadQualityBuff(progressionManager, fish);
        for(ServerPlayerEntity serverPlayerEntity : nearPlayers) {
            if (FishingCard.getPlayerCard(serverPlayerEntity).hasPerk(FishingPerks.PASSIVE_FISHING_XP)) {
                xpBuffAffectedCount++;
            }
            if (spreadQualityBuff && !serverPlayerEntity.hasStatusEffect(StatusEffectRegistry.ONE_TIME_QUALITY_BUFF)) {
                serverPlayerEntity.addStatusEffect(new StatusEffectInstance(StatusEffectRegistry.ONE_TIME_QUALITY_BUFF, ONE_TIME_BUFF_LENGTH));
            }
        }
        return xpBuffAffectedCount;
    }

    private void prolongStatusEffects(ProgressionManager progressionManager){
        if (!progressionManager.hasPerk(FishingPerks.SHARED_BUFFS)) {
            return;
        }

        if (!this.holder.hasVehicle()) {
            return;
        }

        if (!(this.holder.getVehicle() instanceof BoatEntity boatEntity)) {
            return;
        }

        boatEntity.getPassengerList()
                .stream()
                .filter(PlayerEntity.class::isInstance)
                .map(PlayerEntity.class::cast)
                .forEach(playerEntity -> playerEntity.getStatusEffects().forEach(
                        sei -> sei.upgrade(
                                new StatusEffectInstance(
                                        sei.getEffectType(),
                                        sei.getDuration() + PROLONG_EFFECT_LENGTH,
                                        sei.getAmplifier()
                                )
                        )
                ));
    }

    private void processOneTimeBuff(Fish fish){
        if (!fish.consumeGradeBuff) {
            return;
        }
        if (!trackedFor.getHolder().hasStatusEffect(StatusEffectRegistry.ONE_TIME_QUALITY_BUFF)) {
            return;
        }
        consumeOneTimeBuff();
    }

    private void consumeOneTimeBuff(){
        StatusEffectInstance sei = holder.getStatusEffect(StatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        int effectPower = sei.getAmplifier();
        holder.removeStatusEffect(StatusEffectRegistry.ONE_TIME_QUALITY_BUFF);
        if (effectPower > 0) {
            StatusEffectInstance lowerSei = new StatusEffectInstance(sei.getEffectType(), sei.getDuration(), effectPower - 1);
            holder.addStatusEffect(lowerSei);
        }
    }
}

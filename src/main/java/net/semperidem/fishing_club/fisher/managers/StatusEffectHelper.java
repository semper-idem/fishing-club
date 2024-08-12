package net.semperidem.fishing_club.fisher.managers;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.fisher.perks.FishingPerks;
import net.semperidem.fishing_club.registry.FCStatusEffects;

import java.util.List;

public class StatusEffectHelper {
    private static final float QUALITY_BUFF_SUCCESS_CHANCE = 0.25f;
    private static final float EXP_MULTIPLIER_PER_AMPLIFIER = 0.1f;
    private static final int FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER = 4;
    private static final int SPREAD_EFFECT_RANGE = 3;
    private static final int PROLONG_EFFECT_LENGTH = 200;
    private static final int ONE_TIME_BUFF_LENGTH = 2400;

    FishingCard trackedFor;

    public StatusEffectHelper(FishingCard trackedFor) {
        this.trackedFor = trackedFor;
    }

    public int getMinGrade() {
        int minGrade = 0;
        if (this.trackedFor.getHolder() == null) {
            return minGrade;
        }
        if (this.trackedFor.getHolder().hasStatusEffect(FCStatusEffects.QUALITY_BUFF) && Math.random() > QUALITY_BUFF_SUCCESS_CHANCE) {
            minGrade++;
        } else if (this.trackedFor.getHolder().hasStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF)){
            minGrade++;
        }
        return minGrade;
    }

    public void fishCaught(ProgressionManager progressionManager){
        processOneTimeBuff();
        prolongStatusEffects(progressionManager);
    }

    public float getExpMultiplier() {
        StatusEffectInstance xpBuffInstance;
        float multiplier = 1;
        if ((xpBuffInstance = this.trackedFor.getHolder().getStatusEffect(FCStatusEffects.EXP_BUFF)) != null) {
             multiplier += EXP_MULTIPLIER_PER_AMPLIFIER * (xpBuffInstance.getAmplifier() + 1);
        }
        return multiplier;
    }

    private boolean shouldSpreadQualityBuff(ProgressionManager progressionManager, FishRecord fish) {
        return
                fish.quality() >= FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER &&
                progressionManager.hasPerk(FishingPerks.QUALITY_SHARING) &&
                !this.trackedFor.getHolder().hasStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF);
    }

    public int spreadStatusEffect(ProgressionManager progressionManager, FishRecord fish) {
        int xpBuffAffectedCount = 0;
        Box box = new Box(this.trackedFor.getHolder().getBlockPos());
        box.expand(SPREAD_EFFECT_RANGE);

        List<ServerPlayerEntity> nearPlayers = this.trackedFor.getHolder().getEntityWorld()
                .getOtherEntities(this.trackedFor.getHolder(), box)
                .stream()
                .filter(ServerPlayerEntity.class::isInstance)
                .map(ServerPlayerEntity.class::cast)
                .toList();

        boolean spreadQualityBuff = shouldSpreadQualityBuff(progressionManager, fish);
        for(ServerPlayerEntity serverPlayerEntity : nearPlayers) {
            if (FishingCard.of(serverPlayerEntity).hasPerk(FishingPerks.PASSIVE_FISHING_XP)) {
                xpBuffAffectedCount++;
            }
            if (spreadQualityBuff && !serverPlayerEntity.hasStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF)) {
                serverPlayerEntity.addStatusEffect(new StatusEffectInstance(FCStatusEffects.ONE_TIME_QUALITY_BUFF, ONE_TIME_BUFF_LENGTH));
            }
        }
        return xpBuffAffectedCount;
    }

    private void prolongStatusEffects(ProgressionManager progressionManager){
        if (!progressionManager.hasPerk(FishingPerks.SHARED_BUFFS)) {
            return;
        }

        if (!this.trackedFor.getHolder().hasVehicle()) {
            return;
        }

        if (!(this.trackedFor.getHolder().getVehicle() instanceof BoatEntity boatEntity)) {
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

    private void processOneTimeBuff(){
        if (!trackedFor.getHolder().hasStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF)) {
            return;
        }
        consumeOneTimeBuff();
    }

    private void consumeOneTimeBuff(){
        StatusEffectInstance sei = this.trackedFor.getHolder().getStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF);
        int effectPower = sei.getAmplifier();
        this.trackedFor.getHolder().removeStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF);
        if (effectPower > 0) {
            StatusEffectInstance lowerSei = new StatusEffectInstance(sei.getEffectType(), sei.getDuration(), effectPower - 1);
            this.trackedFor.getHolder().addStatusEffect(lowerSei);
        }
    }
}

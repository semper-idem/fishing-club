package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.registry.FCStatusEffects;
import net.semperidem.fishingclub.status_effects.IncreaseFishingExpStatusEffect;

import java.util.List;

public class StatusEffectHelper {
    private static final float QUALITY_BUFF_SUCCESS_CHANCE = 0.25f;
    private static final int FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER = 4;
    private static final int SPREAD_EFFECT_RANGE = 4;
    private static final int PROLONG_EFFECT_LENGTH = 200;
    private static final int ONE_TIME_BUFF_LENGTH = 2400;

    FishingCard trackedFor;

    public StatusEffectHelper(FishingCard trackedFor) {
        this.trackedFor = trackedFor;
    }

    public int minQualityIncrement() {
        int minGrade = 0;
        if (this.trackedFor.holder() == null) {
            return minGrade;
        }
        if (this.trackedFor.holder().hasStatusEffect(FCStatusEffects.QUALITY_BUFF) && Math.random() > QUALITY_BUFF_SUCCESS_CHANCE) {
            minGrade++;
        }
        StatusEffectInstance sei = this.trackedFor.holder().getStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF);
        if (sei == null) {
            return minGrade;
        }
        int oneTimeQualityLevel = sei.getAmplifier() + 1;
        int oneTimeQualityDuration = sei.getDuration();
        this.trackedFor.holder().removeStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF);
        if (oneTimeQualityLevel > 0) {
            this.trackedFor.holder().addStatusEffect(new StatusEffectInstance(FCStatusEffects.ONE_TIME_QUALITY_BUFF, oneTimeQualityDuration + 600, oneTimeQualityLevel - 1));
        }
        minGrade++;
        return minGrade;
    }

    public void fishCaught(ProgressionManager progressionManager) {
        processOneTimeBuff();
        prolongStatusEffects(progressionManager);
    }

    public float getExpMultiplier() {
        StatusEffectInstance xpBuffInstance;
        float multiplier = 1;
        if ((xpBuffInstance = this.trackedFor.holder().getStatusEffect(FCStatusEffects.EXP_BUFF)) != null) {
            multiplier += IncreaseFishingExpStatusEffect.BONUS_PER_AMPLIFIER * (xpBuffInstance.getAmplifier() + 1);
        }
        return multiplier;
    }

    private boolean shouldSpreadQualityBuff(ProgressionManager progressionManager, SpecimenData fish) {
        if (fish.quality() < FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER) {
            return false;
        }
        if (!progressionManager.knowsTradeSecret(TradeSecrets.QUALITY_CELEBRATION)) {
            return false;
        }
        return !this.trackedFor.holder().hasStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF);
    }

    public int spreadStatusEffect(ProgressionManager progressionManager, SpecimenData fish) {
        int xpBuffAffectedCount = 0;
        Box box = new Box(this.trackedFor.holder().getBlockPos());
        box.expand(SPREAD_EFFECT_RANGE);

        List<ServerPlayerEntity> nearPlayers = this.trackedFor.holder().getEntityWorld()
                .getOtherEntities(this.trackedFor.holder(), box)
                .stream()
                .filter(ServerPlayerEntity.class::isInstance)
                .map(ServerPlayerEntity.class::cast)
                .toList();

        boolean spreadQualityBuff = shouldSpreadQualityBuff(progressionManager, fish);
        for (ServerPlayerEntity serverPlayerEntity : nearPlayers) {
            if (FishingCard.of(serverPlayerEntity).knowsTradeSecret(TradeSecrets.PASSIVE_FISHING_XP_BUFF)) {
                xpBuffAffectedCount++;
            }
            if (spreadQualityBuff && !serverPlayerEntity.hasStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF)) {
                serverPlayerEntity.addStatusEffect(new StatusEffectInstance(FCStatusEffects.ONE_TIME_QUALITY_BUFF, ONE_TIME_BUFF_LENGTH));
            }
        }
        return xpBuffAffectedCount;
    }

    private void prolongStatusEffects(ProgressionManager progressionManager) {
        if (!progressionManager.knowsTradeSecret(TradeSecrets.SHARED_BUFFS)) {
            return;
        }

        if (!this.trackedFor.holder().hasVehicle()) {
            return;
        }

        if (!(this.trackedFor.holder().getVehicle() instanceof BoatEntity boatEntity)) {
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

    private void processOneTimeBuff() {
        if (!trackedFor.holder().hasStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF)) {
            return;
        }
        consumeOneTimeBuff();
    }

    private void consumeOneTimeBuff() {
        StatusEffectInstance sei = this.trackedFor.holder().getStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF);
        int effectPower = sei.getAmplifier();
        this.trackedFor.holder().removeStatusEffect(FCStatusEffects.ONE_TIME_QUALITY_BUFF);
        if (effectPower > 0) {
            StatusEffectInstance lowerSei = new StatusEffectInstance(sei.getEffectType(), sei.getDuration(), effectPower - 1);
            this.trackedFor.holder().addStatusEffect(lowerSei);
        }
    }
}

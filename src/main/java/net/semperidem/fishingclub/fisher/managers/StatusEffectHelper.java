package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.registry.StatusEffects;
import net.semperidem.fishingclub.status_effects.IncreaseFishingExpStatusEffect;

public class StatusEffectHelper {
    private static final float QUALITY_BUFF_SUCCESS_CHANCE = 0.25f;
    private static final int FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER = 4;
    private static final int SPREAD_EFFECT_RANGE = 4;
    private static final int PROLONG_EFFECT_LENGTH = 200;

    Card trackedFor;

    public StatusEffectHelper(Card trackedFor) {
        this.trackedFor = trackedFor;
    }

    public int minQualityIncrement() {
        int minGrade = 0;
        if (this.trackedFor.holder() == null) {
            return minGrade;
        }
        if (this.trackedFor.holder().hasStatusEffect(StatusEffects.QUALITY_BUFF) && Math.random() > QUALITY_BUFF_SUCCESS_CHANCE) {
            minGrade++;
        }
        return minGrade;
    }

    public void fishCaught(SpecimenData fish) {
        this.celebrateFishQuality(fish);
        this.stackPassiveExp();
    }

    public float getExpMultiplier() {
        return  getExpMultiplier(this.trackedFor.holder());
    }

    public static float getExpMultiplier(PlayerEntity player) {
        StatusEffectInstance xpBuffInstance;
        float multiplier = 1;
        if ((xpBuffInstance = player.getStatusEffect(StatusEffects.EXP_BUFF)) != null) {
            multiplier += IncreaseFishingExpStatusEffect.BONUS_PER_AMPLIFIER * (xpBuffInstance.getAmplifier() + 1);
        }
        return multiplier;
    }

    public void celebrateFishQuality(SpecimenData fish) {
        if (!this.trackedFor.knowsTradeSecret(TradeSecrets.QUALITY_CELEBRATION)) {
            return;
        }

        if (fish.quality() < FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER) {
            return;
        }

        this.trackedFor.useTradeSecret(TradeSecrets.QUALITY_CELEBRATION, null);
    }

    public void stackPassiveExp() {
        if (!this.trackedFor.knowsTradeSecret(TradeSecrets.PASSIVE_FISHING_XP_BUFF)) {
            return;
        }
        final int maxAmplifier = (int) this.trackedFor.tradeSecretValue(TradeSecrets.PASSIVE_FISHING_XP_BUFF);
        this.trackedFor.holder().getEntityWorld()
                .getOtherEntities(this.trackedFor.holder(), new Box(this.trackedFor.holder().getBlockPos()).expand(4))
                .stream()
                .filter(ServerPlayerEntity.class::isInstance)
                .map(entity -> Card.of((PlayerEntity)entity))
                .forEach(card -> {
                    int nextAmplifier = 0;
                    int nextDuration = 600;

                    StatusEffectInstance currentExpBuff = card.holder().getStatusEffect(StatusEffects.EXP_BUFF);
                    if (currentExpBuff != null && currentExpBuff.getAmplifier() >= maxAmplifier) {
                        nextAmplifier = Math.min(maxAmplifier, currentExpBuff.getAmplifier() + 1);
                        nextDuration += currentExpBuff.getDuration();
                    }

                    card.holder().addStatusEffect(new StatusEffectInstance(StatusEffects.EXP_BUFF, nextAmplifier, Math.min(6000, nextDuration)));
                });
    }
}

package net.semperidem.fishingclub.fisher.managers;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.registry.StatusEffects;
import net.semperidem.fishingclub.status_effects.IncreaseFishingExpStatusEffect;

import java.util.Optional;

public abstract class StatusEffectHelper {
    private static final float QUALITY_BUFF_SUCCESS_CHANCE = 0.25f;
    private static final int FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER = 4;
    private static final int SPREAD_EFFECT_RANGE = 4;
    private static final int PASSIVE_XP_EFFECT_LENGTH = 600;
    private static final int PASSIVE_XP_EFFECT_MAX_LENGTH = 6000;

    public static int minQualityIncrement(PlayerEntity player) {
        return Optional.ofNullable(player.getStatusEffect(StatusEffects.QUALITY_BUFF))
                .map(StatusEffectInstance::getAmplifier)
                .filter(amplifier -> QUALITY_BUFF_SUCCESS_CHANCE * amplifier > Math.random())
                .orElse(0);
    }

    public static float getExpMultiplier(PlayerEntity player) {
        StatusEffectInstance xpBuffInstance;
        float multiplier = 1;
        if ((xpBuffInstance = player.getStatusEffect(StatusEffects.EXP)) != null) {
            multiplier += IncreaseFishingExpStatusEffect.BONUS_PER_AMPLIFIER * (xpBuffInstance.getAmplifier() + 1);
        }
        return multiplier;
    }

    public static void triggerQualityCelebration(Card card, SpecimenData fish) {
        if (!card.knowsTradeSecret(TradeSecrets.SHARE_QUALITY)) {
            return;
        }

        if (fish.quality() < FISH_GRADE_FOR_QUALITY_BUFF_TRIGGER) {
            return;
        }

        card.useTradeSecret(TradeSecrets.SHARE_QUALITY, null);
    }

    public static void triggerPassiveXP(Card card) {
        if (!card.knowsTradeSecret(TradeSecrets.PASSIVE_EXP)) {
            return;
        }
        final int maxAmplifier = (int) card.tradeSecretValue(TradeSecrets.PASSIVE_EXP);
        PlayerEntity owner = card.owner();
        owner.getEntityWorld()
                .getOtherEntities(owner, new Box(owner.getBlockPos()).expand(SPREAD_EFFECT_RANGE))
                .stream()
                .filter(ServerPlayerEntity.class::isInstance)
                .map(entity -> (ServerPlayerEntity)entity)
                .forEach(player -> {
                    int nextAmplifier = 0;
                    int nextDuration = PASSIVE_XP_EFFECT_LENGTH;

                    StatusEffectInstance currentExpBuff = player.getStatusEffect(StatusEffects.EXP);
                    if (currentExpBuff != null && currentExpBuff.getAmplifier() <= maxAmplifier) {
                        nextAmplifier = Math.min(maxAmplifier, currentExpBuff.getAmplifier() + 1);
                        nextDuration += currentExpBuff.getDuration();
                    }

                    player.addStatusEffect(
                            new StatusEffectInstance(
                                    StatusEffects.EXP,
                                    nextAmplifier,
                                    MathHelper.clamp(
                                            nextDuration,
                                            PASSIVE_XP_EFFECT_LENGTH,
                                            PASSIVE_XP_EFFECT_MAX_LENGTH
                                    )
                            )
                    );
                });
    }
}

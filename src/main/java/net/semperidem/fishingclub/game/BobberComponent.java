package net.semperidem.fishingclub.game;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.network.payload.FishingGameTickS2CPayload;
import net.semperidem.fishingclub.status_effects.IncreaseBobberSizeStatusEffect;

import static net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets.BOBBER_SIZE_BOAT;
import static net.semperidem.fishingclub.registry.FCStatusEffects.BOBBER_BUFF;

public class BobberComponent {
    public static final float BASE_LENGTH = 0.0682f;

    private final float length;
    private final float baseResistance;

    private final float minPositionX;
    private final float maxPositionX;

    private float positionX;

    private final FishingGameController parent;

    public BobberComponent(FishingGameController parent) {
        this.parent = parent;
        this.length = calculateLength();
        this.positionX = 0.5f;
        this.baseResistance = getBaseResistance();
        this.minPositionX = 0 + length / 2;
        this.maxPositionX = 1 - length / 2;
    }


    public void updateClient(FishingGameTickS2CPayload payload) {
        this.positionX = payload.bobberPositionX();
    }


    private float calculateLength(){
        float lengthMultiplier = 1;

        lengthMultiplier +=  this.parent.fishingCard.tradeSecretValue(BOBBER_SIZE_BOAT);

        StatusEffectInstance sei = this.parent.player.getStatusEffect(BOBBER_BUFF);
        if (sei != null){
            lengthMultiplier += IncreaseBobberSizeStatusEffect.SIZE_INCREMENT * (sei.getAmplifier() + 1);
        }

        int levelDifference = this.parent.fishingCard.getLevel() - this.parent.hookedFish.level();
        float levelDifferenceMultiplier = (float) MathHelper.clamp(levelDifference > 0 ? 1 + levelDifference * 0.01 : 1 - levelDifference * 0.05, 0.5, 2);
        return BASE_LENGTH * levelDifferenceMultiplier * lengthMultiplier * this.parent.rodConfiguration.attributes().bobberWidth();
    }

    public void tick() {
        this.positionX = getNextPositionX();
    }

    public float getNextPositionX(){
        return MathHelper.clamp(positionX + getSpeed(parent.reelForce), minPositionX, maxPositionX);
    }

    private float getSpeed(float reelForce){
        return MathHelper.clamp(getCurrentResistance() + reelForce, -1f, 1f);
    }

    private float getBaseResistance() {
        float levelDifference = MathHelper.clamp(parent.fishingCard.getLevel() - parent.hookedFish.level(), -50, 50);
       return (parent.hookedFish.level() + 50) * 0.04f * (0.0375f + levelDifference / 50f * 0.0125f);
    }

    private float getCurrentResistance() {
        float fishDistanceFromCenterPercent = parent.fishController.getPositionX() - 0.5f;
        if (Math.abs(fishDistanceFromCenterPercent) < 0.01f) {
            return 0;
        }
        return (fishDistanceFromCenterPercent) * baseResistance * parent.fishController.getStaminaPercentage();
    }

    public float getPositionX() {
        return positionX;
    }

    public float getLength() {
        return length;
    }

    public boolean hasPoint(float pointPosition){
        return Math.abs(positionX - pointPosition) <= length * 0.5f;
    }

    public boolean hasFish(FishController fishController) {
        if (fishController.isFishJumping()) {
            return false;
        }
        return hasPoint(fishController.getPositionX());
    }
}

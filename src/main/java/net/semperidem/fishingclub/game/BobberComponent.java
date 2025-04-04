package net.semperidem.fishingclub.game;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.network.payload.FishingUpdatePayload;
import net.semperidem.fishingclub.status_effects.IncreaseBobberSizeStatusEffect;

import static net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets.BOAT_BOBBER_SIZE;
import static net.semperidem.fishingclub.registry.StatusEffects.BOBBER;

public class BobberComponent {
    public static final float BASE_LENGTH = 0.0682f;

    private final float length;
    private final float baseResistance;
    private float speed;

    private final float minPositionX;
    private final float maxPositionX;

    private float positionX;

    private final FishingController parent;

    public BobberComponent(FishingController parent) {
        this.parent = parent;
        this.length = calculateLength();
        this.positionX = 0.5f;
        this.baseResistance = getBaseResistance();
        this.minPositionX = 0 + length / 2;
        this.maxPositionX = 1 - length / 2;
    }


    public void updateClient(FishingUpdatePayload payload) {
        this.positionX = payload.bobberPositionX();
    }


    private float calculateLength(){
        float lengthMultiplier = 1;

        lengthMultiplier +=  this.parent.card.tradeSecretValue(BOAT_BOBBER_SIZE);

        StatusEffectInstance sei = this.parent.player.getStatusEffect(BOBBER);
        if (sei != null){
            lengthMultiplier += IncreaseBobberSizeStatusEffect.SIZE_INCREMENT * (sei.getAmplifier() + 1);
        }

        int levelDifference = this.parent.card.getLevel() - this.parent.hookedFish.level();
        float levelDifferenceMultiplier = (float) MathHelper.clamp(levelDifference > 0 ? 1 + levelDifference * 0.01 : 1 - levelDifference * 0.05, 0.5, 2);
        return BASE_LENGTH * levelDifferenceMultiplier * lengthMultiplier * this.parent.rodConfiguration.attributes().bobberWidth();
    }

    public void tick() {
        this.positionX = getNextPositionX();
    }

    public float getNextPositionX(){
        this.speed = getSpeed(parent.reelForce);
        return MathHelper.clamp(positionX + this.speed, minPositionX, maxPositionX);
    }

    private float getSpeed(float reelForce){
        return MathHelper.clamp(getCurrentResistance() + reelForce + this.speed * 0.5f, -1f, 1f);
    }

    private float getBaseResistance() {
        float levelDifference = MathHelper.clamp(parent.card.getLevel() - parent.hookedFish.level(), -50, 50);
       return (parent.hookedFish.level() + 50) * 0.04f * (0.0375f + levelDifference / 50f * 0.0125f);
    }

    private float getCurrentResistance() {
        float fishDistanceFromCenterPercent = parent.fishController.getPositionX() - 0.5f;
        if (Math.abs(fishDistanceFromCenterPercent) < 0.01f) {
            return 0;
        }
        return (fishDistanceFromCenterPercent) * baseResistance * parent.fishController.getStaminaPercentage() * (2 / this.parent.rodConfiguration.attributes().bobberWidth() - 1);
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

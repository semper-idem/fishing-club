package net.semperidem.fishingclub.game;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.network.payload.FishingGameTickPayload;

import static net.semperidem.fishingclub.fisher.perks.FishingPerks.BOBBER_SIZE_BOAT;
import static net.semperidem.fishingclub.registry.FCStatusEffects.BOBBER_BUFF;

public class BobberComponent {
    private static final float BASE_LENGTH = 0.25f;

    private static final float BOAT_BASE_BONUS = 0.1f;
    private static final float BOAT_PERK_BONUS = 0.1f;
    private static final float BUFF_BONUS = 0.1f;

    private float length;
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


    public void consumeData(FishingGameTickPayload payload) {
        this.positionX = payload.bobberPositionX();
    }

    public void writeData(PacketByteBuf buf) {
        buf.writeFloat(positionX);
    }


    private float calculateLength(){

        float lengthMultiplier = 1;

        boolean isFromBoat = parent.fishingCard.isFishingFromBoat();
        boolean hasPerk = parent.fishingCard.hasPerk(BOBBER_SIZE_BOAT);
        boolean hasBuff = parent.player.hasStatusEffect(BOBBER_BUFF);

        if (isFromBoat) {
            lengthMultiplier += BOAT_BASE_BONUS;
        }

        if (isFromBoat && hasPerk) {
            lengthMultiplier += BOAT_PERK_BONUS;
        }

        if (hasBuff) {
            lengthMultiplier += BUFF_BONUS;

        }

        return BASE_LENGTH * lengthMultiplier;
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

    public boolean hasFish(FishController fishController) {
        if (fishController.isFishJumping()) {
            return false;
        }
        float fishPositionX = fishController.getPositionX();
        return Math.abs(positionX - fishPositionX) <= (length - FishController.FISH_LENGTH + 0.001) * 0.5f;
    }
}

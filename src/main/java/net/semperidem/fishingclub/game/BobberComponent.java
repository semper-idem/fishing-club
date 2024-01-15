package net.semperidem.fishingclub.game;

import net.minecraft.util.math.MathHelper;

import static net.semperidem.fishingclub.fisher.perks.FishingPerks.BOAT_BOBBER_SIZE;
import static net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController.getStat;
import static net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType.BOBBER_WIDTH;
import static net.semperidem.fishingclub.registry.FStatusEffectRegistry.BOBBER_BUFF;

public class BobberComponent {
    private static final float BASE_LENGTH = 0.25f;

    private static final float BOAT_BASE_BONUS = 0.1f;
    private static final float BOAT_PERK_BONUS = 0.1f;
    private static final float BUFF_BONUS = 0.1f;

    private final float length;
    private final float resistance;

    private final float bottomBound;
    private final float topBound;

    private float positionX;

    private final FishingGameController parent;

    public BobberComponent(FishingGameController parent) {
        this.parent = parent;
        this.length = calculateLength();
        this.positionX = 0.5f - length * 0.5f;
        this.resistance = (parent.fish.fishLevel + 50) * 0.05f;
        this.topBound = 1 - length;
        this.bottomBound = 0;
    }

    private float calculateLength(){

        float lengthMultiplier = 0.9f + getStat(parent.fish.caughtUsing, BOBBER_WIDTH);

        boolean isFromBoat = parent.fishingCard.isFishingFromBoat();
        boolean hasPerk = parent.fishingCard.hasPerk(BOAT_BOBBER_SIZE);
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
        return MathHelper.clamp(positionX + getSpeed(parent.reelForce), bottomBound, topBound);
    }

    private float getSpeed(float reelForce){
        return MathHelper.clamp(getResistance() + reelForce, -1f, 1f);
    }

    private float getResistance() {
        return  -0.05f * (parent.fishComponent.getPositionX() - 0.5f) * resistance;
    }

    public float getPositionX() {
        return positionX;
    }

    public float getLength() {
        return length;
    }

    public boolean hasFish(FishComponent fishComponent) {
        if (fishComponent.isFishJumping()) {
            return false;
        }
        float positionX = fishComponent.getPositionX();
        return this.positionX <= positionX && this.positionX + length >= positionX;
    }
}

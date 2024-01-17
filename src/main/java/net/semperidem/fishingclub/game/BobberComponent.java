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
        return MathHelper.clamp(positionX + getSpeed(parent.reelForce), minPositionX, maxPositionX);
    }

    private float getSpeed(float reelForce){
        return MathHelper.clamp(getCurrentResistance() + reelForce, -1f, 1f);
    }

    private float getBaseResistance() {
        float levelDifference = MathHelper.clamp(parent.fishingCard.getLevel() - parent.fish.fishLevel, -50, 50);
       return (parent.fish.fishLevel + 50) * 0.04f * (0.0375f + 50f / levelDifference * 0.0125f);
    }

    private float getCurrentResistance() {
        return (parent.fishComponent.getPositionX() - 0.5f) * baseResistance * parent.fishComponent.getStaminaPercentage();
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
        float fishPositionX = fishComponent.getPositionX();
        return Math.abs(positionX - fishPositionX) <= (length - FishComponent.FISH_LENGTH + 0.001) * 0.5f;
    }
}

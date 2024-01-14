package net.semperidem.fishingclub.game.components;

import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.game.FishGameController;
import net.semperidem.fishingclub.game.fish.FishPatternInstance;
import net.semperidem.fishingclub.game.fish.FishType;

public class FishComponent {
    private static final float SINE_PERIOD = (float) (Math.PI * 2);
    private static final float WAVE_SPEED = 5;
    private static final float WAVE_STRENGTH = 0.05f;
    private static final int STAMINA_PER_LEVEL = 250;
    private static final int STAMINA_BASE = 200;

    public static final float FISH_LENGTH = 0.0625f; // 1/16

    private float stamina;
    private final float minStamina;
    private final float maxStamina;
    private final FishPatternInstance pattern;
    private float positionX;
    private float nextPositionX;
    private float positionY;

    private float levelBasedSpeed;
    private int tick;
    private int lastSegmentIndex;
    private FishPatternInstance.Segment lastSegment;
    private float fishSpeed;
    private int jumpTicks;

    public FishComponent(FishGameController parent){
        FishType fishType = parent.fish.getFishType();
        stamina = STAMINA_BASE + fishType.getStaminaLevel() * STAMINA_PER_LEVEL;
        minStamina = stamina * 0.5f;
        maxStamina = stamina;
        pattern = new FishPatternInstance(fishType.getFishPattern(), parent.fish.fishLevel);
        nextPositionX = 0.5f - FISH_LENGTH * 0.5f;
        positionX = nextPositionX;
        positionY = 0;
        levelBasedSpeed = parent.fish.fishLevel / 200f;
        lastSegmentIndex = 0;
        lastSegment = pattern.getSegmentList().get(0);
    }

    public void updateSpeed(boolean hasFishSlowingEffectApplied) {
        float staminaPercent = stamina / maxStamina;
        fishSpeed = 0.75f * staminaPercent + levelBasedSpeed;
        if (hasFishSlowingEffectApplied) {
            fishSpeed = fishSpeed * 0.75f;
        }
    }


    public void tick(boolean hasFishSlowingEffectApplied) {
        tick++;
        tickMovement();
        tickStamina(hasFishSlowingEffectApplied);
    }

    private void tickMovement() {
        positionX = nextPositionX;
        nextPositionX = getFishPos();
        tickJump();
    }

    private void tickJump() {
        if (pattern.isJumpTick(tick)) {
            jumpTicks = 20;
        }

        if (jumpTicks == 0) {
            return;
        }

        jumpTicks--;
        if (jumpTicks >= 15) {
            positionY = (float) Math.sqrt((20 - jumpTicks) / 5f);
        } else {
            positionY = (float) Math.sqrt((jumpTicks) / 15f);
        }

    }

    private float getDistanceTraveled(int tick) {
        return (fishSpeed * tick) % pattern.getLength();
    }

    private float getFishPos() {
        float distanceTraveled = getDistanceTraveled(tick);
        return MathHelper.clamp(getFishPosOnCurve(distanceTraveled, getCurrentSegment(distanceTraveled)) + getFishPosOnWave(distanceTraveled), 0 , 1 - FISH_LENGTH) ;
    }

    private float getFishPosOnWave(float distanceTraveled) {
        return  (float) (Math.sin(SINE_PERIOD * WAVE_SPEED * (distanceTraveled / pattern.getLength())) * WAVE_STRENGTH);
    }

    private float getFishPosOnCurve(float distanceTraveled, FishPatternInstance.Segment segment) {
        float segmentDistance = segment.nextPoint.x - segment.currentPoint.x;
        float segmentElapsedDistance = distanceTraveled - segment.currentPoint.x;
        return quadraticBezier(
                segmentElapsedDistance / segmentDistance,
                segment.currentPoint.y,
                segment.controlPoint.y,
                segment.nextPoint.y
        ) / 1000;
    }

    private FishPatternInstance.Segment getCurrentSegment(float distanceTraveled) {
        if (distanceTraveled > lastSegment.currentPoint.x) {
            lastSegmentIndex++;
            if (lastSegmentIndex >= pattern.getSegmentList().size()) {
                lastSegmentIndex = 0;
            }
            lastSegment = pattern.getSegmentList().get(lastSegmentIndex);
        }
        return lastSegment;
    }

    public static float quadraticBezier(float t, float p0, float p1, float p2) {
        float oneMinusT = 1 - t;
        return oneMinusT * oneMinusT * p0 + 2 * oneMinusT * t * p1 + t * t * p2;
    }


    private void tickStamina(boolean hasFishSlowingEffectApplied) {
        if (stamina > minStamina) {
            stamina--;
        }
        updateSpeed(hasFishSlowingEffectApplied);
    }

    public float getPositionX() {
        return positionX;
    }

    public float getNextPositionX(){
        return nextPositionX;
    }

    public float getPositionY() {
        return positionY;
    }

    public boolean isFishJumping() {
        return positionY > 0;
    }
    public int getJumpTicks() {
        return jumpTicks;
    }
}

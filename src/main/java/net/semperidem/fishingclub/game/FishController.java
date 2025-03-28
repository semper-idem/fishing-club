package net.semperidem.fishingclub.game;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fish.MovementPatternInstance;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.network.payload.FishingGameTickS2CPayload;
import net.semperidem.fishingclub.registry.StatusEffects;
import net.semperidem.fishingclub.status_effects.DecreaseFishSpeedStatusEffect;

import static net.semperidem.fishingclub.util.MathUtil.quadraticBezier;

public class FishController {
    private static final float SINE_PERIOD = (float) (Math.PI * 2);
    private static final float WAVE_SPEED = 5;
    private static final float WAVE_STRENGTH = 0.05f;
    private static final int STAMINA_PER_LEVEL = 250;
    private static final int STAMINA_BASE = 200;

    private final FishingGameController parent;
    private final float minStamina;
    private final float maxStamina;
    private final float staminaDrain;
    private final float baseSpeed;

    private float stamina;
    private final MovementPatternInstance patternInstance;

    private float positionX;
    private float nextPositionX;
    private float positionY;

    private float speed;
    private int jumpTicks;
    private float totalDistanceTraveled;
    private int lastSegmentIndex;
    private MovementPatternInstance.Segment currentSegment;

    public FishController(FishingGameController parent){
        this.parent = parent;

        Species<?> species = parent.hookedFish.species();
        stamina = STAMINA_BASE + species.staminaLevel() * STAMINA_PER_LEVEL;
        minStamina = stamina * 0.5f;
        maxStamina = stamina;
        this.staminaDrain = (1 + 3 * this.parent.rodConfiguration.attributes().fishControl());

        nextPositionX = 0.5f;
        positionX = nextPositionX;
        positionY = 0;
        baseSpeed = parent.hookedFish.level() * 0.05f * (1 - this.parent.rodConfiguration.attributes().fishControl());

        patternInstance = new MovementPatternInstance(species.movement(), parent.hookedFish.level());
        lastSegmentIndex = 0;
        currentSegment = patternInstance.getSegmentList().get(lastSegmentIndex);
    }


    public void updateClient(FishingGameTickS2CPayload payload) {
        this.positionX = payload.fishPositionX();
        this.positionY = payload.fishPositionY();
    }

    public void tick() {
        totalDistanceTraveled +=speed;
        tickMovement();
        tickStamina();
    }

    private void tickMovement() {
        this.positionX = this.nextPositionX;
        calculateNextPositionX();
        tickJump();
    }

    private void calculateNextPositionX() {
        float distanceTraveled = totalDistanceTraveled % patternInstance.getLength();
        calculateCurrentSegment(distanceTraveled);
        nextPositionX = MathHelper.clamp(
                getCurve(distanceTraveled, currentSegment) + getWave(distanceTraveled),
                0,
                1
        );
    }

    private void calculateCurrentSegment(float distanceTraveled) {
        if (currentSegment.currentPoint.x <= distanceTraveled && distanceTraveled <= currentSegment.nextPoint.x) {
            return;
        }
        lastSegmentIndex++;
        if (lastSegmentIndex >= patternInstance.getSegmentList().size()) {
            lastSegmentIndex = 0;
        }
        currentSegment = patternInstance.getSegmentList().get(lastSegmentIndex);
    }

    private float getWave(float distanceTraveled) {
        float percentageDistanceTraveled = distanceTraveled / patternInstance.getLength();
        return (float) (Math.sin(SINE_PERIOD * WAVE_SPEED * percentageDistanceTraveled) * WAVE_STRENGTH);
    }

    private float getCurve(float distanceTraveled, MovementPatternInstance.Segment segment) {
        float segmentDistance = segment.nextPoint.x - segment.currentPoint.x;
        float segmentElapsedDistance = distanceTraveled - segment.currentPoint.x;
        return quadraticBezier(
                segmentElapsedDistance / segmentDistance,
                segment.currentPoint.y,
                segment.controlPoint.y,
                segment.nextPoint.y
        ) * 0.001f;
    }

    private void tickJump() {
        float distanceTraveled = totalDistanceTraveled % patternInstance.getLength();
        if (patternInstance.isJumpTick((int) distanceTraveled)) {
            jumpTicks = 20;
        }

        if (jumpTicks == 0) {
            return;
        }

        jumpTicks--;
        if (jumpTicks >= 15) {
            positionY = (float) Math.sqrt((20 - jumpTicks) * 0.2f);
        } else {
            positionY = (float) Math.sqrt((jumpTicks) * 0.06667f);
        }
    }

    private void tickStamina() {
        if (stamina > minStamina) {
            this.stamina -= this.staminaDrain;
        }
        calculateSpeed();
    }

    public void calculateSpeed() {
        float staminaPercent = getStaminaPercentage();
        speed = baseSpeed + 0.5f * staminaPercent;
        StatusEffectInstance sei = this.parent.player.getStatusEffect(StatusEffects.SLOW_FISH_BUFF);
        if (sei != null) {
            speed *= 1 - ((sei.getAmplifier() + 1) * DecreaseFishSpeedStatusEffect.SLOW_AMOUNT);
        }
    }

    public void setPosition(float fishPositionX, float fishPositionY){
        this.positionX = fishPositionX;
        this.positionY = fishPositionY;
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

    public float getStaminaPercentage() {
        return stamina / maxStamina;
    }
}

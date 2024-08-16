package net.semperidem.fishingclub.game;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.fish.MovementPatternInstance;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.network.payload.FishingGameTickPayload;
import net.semperidem.fishingclub.registry.FCStatusEffects;

import static net.semperidem.fishingclub.util.MathUtil.quadraticBezier;

public class FishController {
    private static final float SINE_PERIOD = (float) (Math.PI * 2);
    private static final float WAVE_SPEED = 5;
    private static final float WAVE_STRENGTH = 0.05f;
    private static final int STAMINA_PER_LEVEL = 250;
    private static final int STAMINA_BASE = 200;

    public static final float FISH_LENGTH = 0.0625f; // 1/16

    private final FishingGameController parent;
    private final float minStamina;
    private final float maxStamina;
    private final float baseSpeed;

    private float stamina;
    private final MovementPatternInstance patternInstance;

    private final float minPositionX;
    private final float maxPositionX;

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

        Species species = Species.of(parent.hookedFish.speciesName());
        stamina = STAMINA_BASE + species.staminaLevel() * STAMINA_PER_LEVEL;
        minStamina = stamina * 0.5f;
        maxStamina = stamina;

        nextPositionX = 0.5f;
        positionX = nextPositionX;
        positionY = 0;
        baseSpeed = parent.hookedFish.level() * 0.005f;
        minPositionX = 0 + FISH_LENGTH / 2;
        maxPositionX = 1 - FISH_LENGTH / 2;

        patternInstance = new MovementPatternInstance(species.getMovement(), parent.hookedFish.level());
        lastSegmentIndex = 0;
        currentSegment = patternInstance.getSegmentList().get(lastSegmentIndex);
    }


    public void consumeData(FishingGameTickPayload payload) {
        this.positionX = payload.fishPositionX();
        this.positionY = payload.fishPositionY();
    }

    public void writeData(PacketByteBuf buf) {
        buf.writeFloat(positionX);
        buf.writeFloat(positionY);
    }

    public void tick() {
        totalDistanceTraveled +=speed;
        tickMovement();
        tickStamina();
    }

    private void tickMovement() {
        positionX = nextPositionX;
        calculateNextPositionX();
        tickJump();
    }

    private void calculateNextPositionX() {
        float distanceTraveled = totalDistanceTraveled % patternInstance.getLength();
        calculateCurrentSegment(distanceTraveled);
        nextPositionX = MathHelper.clamp(
                getCurve(distanceTraveled, currentSegment) + getWave(distanceTraveled),
                minPositionX,
                maxPositionX
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
            stamina--;
        }
        calculateSpeed();
    }

    public void calculateSpeed() {
        float staminaPercent = getStaminaPercentage();
        speed = baseSpeed + 0.75f * staminaPercent;
        if (parent.player.hasStatusEffect(FCStatusEffects.SLOW_FISH_BUFF)) {
            speed = speed * 0.75f;
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

package net.semperidem.fishingclub.fish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.semperidem.fishingclub.util.Point;
import org.lwjgl.glfw.GLFW;

public class FishGameLogic {
    float health = 100;
    float progress = 0;
    float bobberPos = 0;
    float bobberWidth = 0.1f;
    float fishPos = 0;
    public int ticks = 0;
    float bobberSpeed = 0f;
    float playerAcceleration = 0.005f;
    float gravityAcceleration = 0.0035f;
    Fish fish;
    boolean isFinished = false;
    boolean isWon = false;


    public FishGameLogic(){
        this.fish = new Fish();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isWon() {
        return isWon;
    }

    public float getFishPos() {
        return fishPos;
    }
    public float getProgress() {
        return progress;
    }

    public float getBobberPos() {
        return bobberPos;
    }

    public float getHealth() {
        return health;
    }

    public float getBobberWidth() {
        return bobberWidth;
    }


    public void tick() {
        fishPos = calculateFishPos();
        float bobberForce = -gravityAcceleration;
        if (InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_SPACE)) {
            bobberForce = playerAcceleration;
        }
        bobberSpeed += bobberForce;
        float nextPos = bobberPos + bobberSpeed;
        bobberPos = Math.max(Math.min(nextPos, 1 - bobberWidth), 0 + bobberWidth);
        if (bobberPos <= 0 + bobberWidth) {
            if (bobberSpeed < -gravityAcceleration) {
                bobberSpeed = bobberSpeed * -0.5f;
            } else {
                bobberSpeed = 0;
            }
        } else if (bobberPos >= 1 - bobberWidth) {
            bobberSpeed = 0;
        }


        if (fishPos < bobberPos + bobberWidth && fishPos > bobberPos - bobberWidth) {
            if (progress < 1) {
                progress += 0.01f;
                fish.fishEnergy--;
            } else {
                this.isFinished = true;
                this.isWon = true;
            }
        } else {
            if (progress > 0) {
                float nextProgress = progress - 0.005f;
                progress = Math.max(0, nextProgress);
            }
            if (health > 0) {
                health -= Math.max(0, (fish.fishLevel - 5) / 20f);
            } else {
                this.isFinished = true;
            }
        }
        ticks++;
    }

    float calculateFishPos() {
        float totalDuration = calculateTotalDuration(fish.curvePoints);

// In the game loop:
        float power = Math.max(fish.fishMinEnergyLevel, fish.fishEnergy) / (fish.fishMaxEnergyLevel * 1f);
        float elapsedTime = (power * ticks) % totalDuration; // Increment this value based on the time passed since the last frame


        float sinePeriod = (float) (Math.PI * 2);
        float waveSpeed = 5;
        float waveStrength = 0.05f;
        float percentElapsedTime = elapsedTime / totalDuration;
        float waveFunction = (float) (Math.sin(sinePeriod * waveSpeed * percentElapsedTime) * waveStrength);
        return (moveFish(fish.curvePoints, fish.curveControlPoints, elapsedTime, totalDuration) / 1000) + waveFunction ;//+ ((float)Math.sin(ticks / 3f)/ 40);
    }
    public static float calculateTotalDuration(Point[] points) {
        return points[points.length - 1].x;
    }


    public static float moveFish(Point[] points, Point[] controlPoints, float elapsedTime, float totalDuration) {
        elapsedTime %= totalDuration; // Wrap the elapsed time

        int pointCount = points.length;

        int currentIndex = 0;
        while (currentIndex < pointCount - 1 && elapsedTime >= points[currentIndex + 1].x) {
            currentIndex++;
        }

        if (currentIndex < pointCount - 1) {
            Point currentPoint = points[currentIndex];
            Point nextPoint = points[currentIndex + 1];
            Point controlPoint = controlPoints[currentIndex];

            float segmentStartTime = currentPoint.x;
            float segmentEndTime = nextPoint.x;
            float segmentDuration = segmentEndTime - segmentStartTime;
            float segmentElapsedTime = elapsedTime - segmentStartTime;

            float t = segmentElapsedTime / segmentDuration;
            return quadraticBezier(t, currentPoint.y, controlPoint.y, nextPoint.y);

        } else {
            return points[0].y;
        }
    }

    public static float quadraticBezier(float t, float p0, float p1, float p2) {
        float oneMinusT = 1 - t;
        return oneMinusT * oneMinusT * p0 + 2 * oneMinusT * t * p1 + t * t * p2;
    }

    public int getLevel() {
        return fish.fishLevel;
    }
}

package net.semperidem.fishingclub.fish;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.FishingClubClient;
import net.semperidem.fishingclub.fish.fishingskill.FishingPerks;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkill;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.util.Point;
import org.lwjgl.glfw.GLFW;

public class FishGameLogic {
    private static final float sinePeriod = (float) (Math.PI * 2);

    PlayerEntity player;
    float health = 100;
    float progress = 0;
    float fishPos = 0;
    int ticks = 0;
    float bobberPos = 0;
    float bobberLength;
    float bobberSpeed = 0f;
    float reelingAcceleration = 0.005f;
    float gravityAcceleration = 0.0035f;
    Fish fish;
    boolean isFinished = false;
    boolean isWon = false;
    FishingSkill fishingSkill;
    float bottomBound;
    float topBound;
    float trackLength;
    float totalDuration;
    float waveSpeed = 5;
    float waveStrength = 0.05f;


    public FishGameLogic(PlayerEntity player){
        this.player = player;
        this.fishingSkill = FishingClubClient.clientFishingSkill;
        setBobberLength();
        this.fish = FishUtil.getFishOnHook(fishingSkill);
        this.totalDuration = fish.curvePoints[fish.curvePoints.length - 1].x;
        this.trackLength = (float) (1 + Math.floor((this.fish.fishLevel / 10f)) / 10f);
        this.bottomBound = bobberLength;
        this.topBound = trackLength - bobberLength;
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

    public void setBobberLength(){
        bobberLength = 0.15f;
        if(fishingSkill.hasPerk(FishingPerks.FISHING_SCHOOL)) {
            bobberLength =  0.15f;
        }
    }

    public float getBobberLength() {
        return bobberLength;
    }

    private boolean isReeling(){
       return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_KEY_SPACE);
    }

    private float getBobberAcceleration(){
        return isReeling() ? reelingAcceleration : -gravityAcceleration;
    }

    private float nextBobberPos(){
        float nextBobberPosUnbound = bobberPos + bobberSpeed;
        return Math.max(Math.min(nextBobberPosUnbound, topBound),bottomBound);
    }

    private void bobberCollideWithBound(){
        if (bobberPos <= bottomBound) {
            bobberBounce();
        } else if (bobberPos >= topBound) {
            bobberSpeed = 0;
        }
    }

    private void bobberBounce(){
        bobberSpeed = bobberSpeed < 0 ? bobberSpeed * -0.5f : 0;
    }

    private void processHealth(){
        if (!fishDealsDamage()) {
            return;
        }
        if (health > 0) {
            health -= getFishDamage();
        } else {
            this.isFinished = true;
        }
    }

    private float getFishDamage(){
        return Math.max(0, (fish.fishLevel - 5) / 20f);
    }

    private boolean fishDealsDamage(){
        return fish.fishLevel > 10;
    }

    private void processProgress(){
        boolean bobberHasFish = fishPos <= bobberPos + bobberLength && fishPos >= bobberPos - bobberLength;
        if (bobberHasFish) {
            grantProgress();
        } else {
            revokeProgress();
        }
    }

    private void grantProgress(){
        if (progress < 1) {
            progress += 0.01f;
            fish.fishEnergy--;
        } else {
            processVictory();
        }
    }

    private void revokeProgress(){
        processHealth();
        if (progress > 0) {
            progress = Math.max(0, progress - 0.005f);
        }
    }

    private void processVictory(){
        this.isFinished = true;
        this.isWon = true;
        grantExperience();
    }
    public void tick() {
        fishPos = nextFishPosition();
        bobberSpeed += getBobberAcceleration();
        bobberPos = nextBobberPos();
        bobberCollideWithBound();
        processProgress();
        ticks++;
    }

    float nextFishPosition() {
        float fishSpeed = Math.max(fish.fishMinEnergyLevel, fish.fishEnergy) / (fish.fishMaxEnergyLevel * 1f);
        float fishSpeedScaledToLevel = fishSpeed * 0.75f + (fish.fishLevel / 200f);
        float elapsedTime = (fishSpeedScaledToLevel * ticks) % totalDuration;
        float nextFishPosUnbound = nextFishPosOnCurve(elapsedTime) + nextFishPosOnWave(elapsedTime);
        return Math.min(Math.max(nextFishPosUnbound * trackLength, 0), trackLength);
    }

    private float nextFishPosOnWave(float elapsedTime){
        float percentElapsedTime = elapsedTime / totalDuration;
        return  (float) (Math.sin(sinePeriod * percentElapsedTime * waveSpeed) * waveStrength);
    }

    private float nextFishPosOnCurve(float elapsedTime) {
        elapsedTime %= this.totalDuration;

        int pointCount = fish.curvePoints.length;

        int currentIndex = 0;
        while (currentIndex < pointCount - 1 && elapsedTime >= fish.curvePoints[currentIndex + 1].x) {
            currentIndex++;
        }

        if (currentIndex < pointCount - 1) {
            Point currentPoint = fish.curvePoints[currentIndex];
            Point nextPoint = fish.curvePoints[currentIndex + 1];
            Point controlPoint = fish.curveControlPoints[currentIndex];

            float segmentStartTime = currentPoint.x;
            float segmentEndTime = nextPoint.x;
            float segmentDuration = segmentEndTime - segmentStartTime;
            float segmentElapsedTime = elapsedTime - segmentStartTime;

            float t = segmentElapsedTime / segmentDuration;
            return quadraticBezier(t, currentPoint.y, controlPoint.y, nextPoint.y) / 1000;

        } else {
            return fish.curvePoints[0].y / 1000;
        }
    }

    public static float quadraticBezier(float t, float p0, float p1, float p2) {
        float oneMinusT = 1 - t;
        return oneMinusT * oneMinusT * p0 + 2 * oneMinusT * t * p1 + t * t * p2;
    }

    public int getLevel() {
        return fish.fishLevel;
    }

    public String getName() {
        return fish.fishType.name;
    }
    public int getExperience() {
        return fish.experience;
    }

    private void grantExperience(){
        ClientPacketSender.sendFishingSkillGrantExp(fish);
    }
}

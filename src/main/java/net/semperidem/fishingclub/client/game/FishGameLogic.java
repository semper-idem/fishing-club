package net.semperidem.fishingclub.client.game;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfoManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.FishingRodPartItem;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.util.FishingRodUtil;
import net.semperidem.fishingclub.util.Point;
import org.lwjgl.glfw.GLFW;

public class FishGameLogic {
    //TODO IMPLEMENT TREASURE MECHANIC - "time event"

    private static final float sinePeriod = (float) (Math.PI * 2);
    private static final float STARTING_BOBBER_LENGTH = 0.1f;

    PlayerEntity player;
    float lineHealth = 0;
    float progress = 0;
    float fishPos = 0;
    int ticks = 0;
    float bobberPos = 0;
    float bobberLength;
    float bobberSpeed = 0f;
    float fishLength = 0.0625f; //possible future upgrade
    float reelingAcceleration = 0.005f; //possible future upgrade
    float gravityAcceleration = 0.0035f;//possible future upgrade
    Fish fish;
    boolean isFinished = false;
    boolean isWon = false;
    FisherInfo fisherInfo;
    float totalDuration;
    float waveSpeed = 5; //TODO make bobber in world dependant eg. ocean = bigger waves
    float waveStrength = 0.05f;//TODO above

    float fishDamage;

    ItemStack caughtUsing;
    boolean boatFishing;


    public FishGameLogic(PlayerEntity player, ItemStack caughtUsing, Fish fish, boolean boatFishing){
        this.player = player;
        this.fisherInfo = FisherInfoManager.getFisher(player);
        this.fish = fish;
        this.caughtUsing = caughtUsing;
        this.boatFishing = boatFishing;
        calculateBobberLength();
        calculateHealth();
        calculateFishDamage();
        this.totalDuration = fish.curvePoints[fish.curvePoints.length - 1].x;
        this.bobberPos = nextBobberPos();
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

    public float getLineHealth() {
        return lineHealth;
    }

    private void calculateBobberLength(){
        float perksBonusLengthMultiplier = 1;
        if(fisherInfo.hasPerk(FishingPerks.FISHING_SCHOOL)) {
            perksBonusLengthMultiplier += 0.1f;
        }
        if ((boatFishing && fisherInfo.hasPerk(FishingPerks.BOAT_BOBBER_SIZE))) {
            perksBonusLengthMultiplier += 0.1f;
        }


        //PARTS
        float partsBonusLengthMultiplier = 1;
        for(ItemStack partStack : FishingRodUtil.getRodParts(caughtUsing)) {
            FishingRodPartItem part = ((FishingRodPartItem) partStack.getItem());
            if (part.getStatBonuses().containsKey(Stat.BOBBER_WIDTH)) {
                partsBonusLengthMultiplier += part.getStatBonuses().get(Stat.BOBBER_WIDTH);
            }
        }

        this.bobberLength = STARTING_BOBBER_LENGTH * partsBonusLengthMultiplier * perksBonusLengthMultiplier;
    }


    private void calculateFishDamage(){
        float damageReduction = 0;
        for(ItemStack partStack : FishingRodUtil.getRodParts(caughtUsing)) {
            FishingRodPartItem part = ((FishingRodPartItem) partStack.getItem());
            if (part.getStatBonuses().containsKey(Stat.DAMAGE_REDUCTION)) {
                damageReduction += part.getStatBonuses().get(Stat.DAMAGE_REDUCTION);
            }
        }
        if (boatFishing && fisherInfo.hasPerk(FishingPerks.LINE_HEALTH_BOAT)) {
            damageReduction += 0.2f;
        }
        float fishRawDamage = Math.max(0, (fish.fishLevel - 5 - (fisherInfo.getLevel() / 4f)) / 20f);
        this.fishDamage =  fishRawDamage * (1 - Math.max(0, Math.min(1, damageReduction)));
    }

    private void calculateHealth(){
        float calculatedHealth = 0;
        for(ItemStack partStack : FishingRodUtil.getRodParts(caughtUsing)) {
            FishingRodPartItem part = ((FishingRodPartItem) partStack.getItem());
            if (part.getStatBonuses().containsKey(Stat.LINE_HEALTH)) {
                calculatedHealth += part.getStatBonuses().get(Stat.LINE_HEALTH);
            }
        }
        this.lineHealth = calculatedHealth;
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
        return Math.max(Math.min(nextBobberPosUnbound, 1), bobberLength);
    }

    private void bobberCollideWithBound(){
        if (bobberPos <= bobberLength) {
            bobberBounce();
        } else if (bobberPos >= 1) {
            bobberSpeed = 0;
        }
    }

    private void bobberBounce(){
        bobberSpeed = bobberSpeed < 0 ? bobberSpeed * -0.5f : 0;
    }

    private void tickHealth(){
        if (fishDamage != 0) {
            lineHealth -= (fishDamage);
            if (lineHealth <= 0){
                isFinished = true;
            }
        }
    }

    private void processProgress(){
        float fishPosCenter = fishPos - fishLength / 2;
        boolean bobberHasFish = bobberPos >= fishPosCenter && bobberPos - bobberLength <= fishPosCenter;
        if (bobberHasFish) {
            grantProgress();
        } else {
            revokeProgress();
        }
    }

    private void grantProgress(){
        if (progress < 1) {
            progress += (0.0075f * Math.max(1, FishingRodUtil.getStat(caughtUsing, Stat.PROGRESS_MULTIPLIER)));
            fish.fishEnergy--;
        } else {
            processVictory();
        }
    }

    private void revokeProgress(){
        tickHealth();
        if (progress > 0) {
            progress = Math.max(0, progress - 0.005f - fishDamage / 100);
        }
    }

    private void processVictory(){
        this.isFinished = true;
        this.isWon = true;
        grantReward();
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
        return Math.max(Math.min(nextFishPosUnbound, 1), fishLength);
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
        return fish.getFishType().name;
    }
    public int getExperience() {
        return fish.experience;
    }

    private void grantReward(){
        ClientPacketSender.sendFishGameGrantReward(fish, boatFishing);
    }

    public enum Stat{
        BOBBER_WIDTH, //Percentage increase of bobber width DONE
        DAMAGE_REDUCTION, // Percentage damage reduction uncapped atm DONE
        LINE_HEALTH, // Additional health point DONE
        CATCH_RATE,//Percentage reduction of time until bite DONE
        PROGRESS_MULTIPLIER,//Self-explanatory DONE
        FISH_MAX_WEIGHT_MULTIPLIER,//Moves upper barrier of max fish weight (on average heavier fish) DONE
        FISH_MAX_LENGTH_MULTIPLIER,//Moves upper barrier of max fish length (on average longer fish) DONE
        FISH_RARITY_BONUS,//Percentage chance to gain fish on higher grade DONE
        BITE_WINDOW_MULTIPLIER//Extends ticks in which fish can be hooked DONE
    }
}

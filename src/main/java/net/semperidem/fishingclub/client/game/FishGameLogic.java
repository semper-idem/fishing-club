package net.semperidem.fishingclub.client.game;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.client.game.treasure.Reward;
import net.semperidem.fishingclub.client.game.treasure.Rewards;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.FishingRodPartItem;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.util.FishingRodUtil;
import net.semperidem.fishingclub.util.Point;
import org.lwjgl.glfw.GLFW;

public class FishGameLogic {

    private static final float SINE_PERIOD = (float) (Math.PI * 2);
    private static final float STARTING_BOBBER_LENGTH = 0.25f;
    private static final float FISH_LENGTH = 0.0625f;
    private static final float REELING_ACCELERATION = 0.005f;
    private static final float REELING_DECELERATION = 0.0035f;
    private static final float WAVE_SPEED = 5;
    private static final float WAVE_STRENGTH = 0.05f;
    private static final float PROGRESS_GAIN = 0.0075f;

    private static final float TREASURE_MIN_CHANCE = 0.05f;
    private static final float TREASURE_MIN_TRIGGER_TIME = 0.5f;
    private static final float TREASURE_MAX_TRIGGER_TIME = 0.25f;
    private static final float TREASURE_MAX_SPOT_SIZE = 0.225f;
    private static final float TREASURE_GRADE_TO_SPOT_SIZE_RATIO = 0.025f;

    private static final float FISHER_VEST_SLOW_BONUS = -0.15f;
    private static final float FISHER_VEST_EXP_BONUS = 0.1f;


    private final PlayerEntity player;
    private final Fish fish;
    private final FisherInfo fisherInfo;
    private final float totalDuration;
    private final ItemStack caughtUsing;
    private final boolean boatFishing;

    private boolean isFinished;
    private boolean isFishWon;
    private boolean isTreasureWon;
    private boolean reelingTreasure;

    private int ticks;

    private float lineHealth;
    private float progress;

    private float fishDamage;
    private float fishPos;
    private float fishPosCenter;

    private float bobberPos;
    private float bobberLength;
    private float bobberSpeed;

    private boolean treasureAvailable;
    private int treasureAvailableTicks;
    private float treasureTriggerTime;
    private int treasureHookedTicks;
    private float treasureSpotSize;
    private float arrowSpeed;
    private float arrowPos;
    private Reward treasureReward;

    public FishGameLogic(PlayerEntity player, ItemStack caughtUsing, Fish fish, boolean boatFishing){
        this.player = player;
        this.fisherInfo = FishingClubClient.CLIENT_INFO;
        this.fish = fish;
        this.caughtUsing = caughtUsing;
        this.boatFishing = boatFishing;
        calculateBobberLength();
        calculateHealth();
        calculateFishDamage();
        this.totalDuration = fish.curvePoints[fish.curvePoints.length - 1].x;
        this.bobberPos = bobberLength;
        this.treasureAvailable = rollForTreasure();
        treasureRoll(player, caughtUsing, boatFishing);
        applyFisherVestEffect();
    }

    private void applyFisherVestEffect(){
        if (!FishUtil.hasFishingVest(player)) return;
        float slowRatio = 1 + FISHER_VEST_SLOW_BONUS;
        float expRatio = 1 + FISHER_VEST_EXP_BONUS;
        if (!FishUtil.hasNonFishingEquipment(player)) {
            slowRatio += FISHER_VEST_SLOW_BONUS;
            expRatio += FISHER_VEST_EXP_BONUS;
        }
        this.fish.fishEnergy = (int) (this.fish.fishEnergy * slowRatio);
        this.fish.experience = (int) (this.fish.experience * expRatio);
    }

    private boolean rollForTreasure(){
        float treasureChance = TREASURE_MIN_CHANCE;
        if (fisherInfo.hasPerk(FishingPerks.DOUBLE_TREASURE_BOAT)) {
            treasureChance *= 2;
        }
        return Math.random() < treasureChance;
    }

    private void treasureRoll(PlayerEntity player, ItemStack caughtUsing, boolean boatFishing){
        if (this.treasureAvailable) {
            this.treasureTriggerTime = (float) (Math.random() * TREASURE_MAX_TRIGGER_TIME + TREASURE_MIN_TRIGGER_TIME);
            this.treasureReward = Rewards.roll(fisherInfo);
            this.arrowSpeed = 1 + treasureReward.getGrade();
            this.treasureSpotSize = TREASURE_MAX_SPOT_SIZE - (treasureReward.getGrade() * TREASURE_GRADE_TO_SPOT_SIZE_RATIO);
        }
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


    public void tick() {
        if(!keyPressed(GLFW.GLFW_KEY_C)) return;
        if (reelingTreasure) {
            tickHookedTreasure();
        } else {
            tickFishGame();
        }
        ticks++;
    }

    private void tickFishGame(){
        if (!isReeling() && isYanking() && treasureAvailableTicks == 0) this.isFinished = true;
        tickMovement();
        tickProgress();
        tickUnhookedTreasure();
    }

    private void tickMovement(){
        tickBobberMovement();
        tickFishMovement();
    }

    private void tickFishMovement(){
        fishPos = nextFishPosition();
        fishPosCenter = fishPos - (FISH_LENGTH / 2);
    }

    private void tickBobberMovement(){
        bobberSpeed += getBobberAcceleration();
        bobberPos = nextBobberPos();
        tickBobberMovementCollision();
    }

    private void tickBobberMovementCollision(){
        if (bobberPos <= bobberLength) {
            bobberSpeed = bobberSpeed < 0 ? bobberSpeed * -0.5f : 0;
        } else if (bobberPos >= 1) {
            bobberSpeed = 0;
        }
    }

    private void tickHealth(){
        if (fishDamage != 0) {
            lineHealth -= (fishDamage);
            if (lineHealth <= 0){
                isFinished = true;
            }
        }
    }

    private void tickProgress(){
        boolean bobberHasFish = bobberPos >= fishPosCenter && bobberPos - bobberLength <= fishPosCenter;
        if (treasureAvailableTicks > 0) return;
        if (bobberHasFish) {
            grantProgress();
        } else {
            revokeProgress();
        }
    }

    private void grantProgress(){
        if (progress < 1) {
            progress += (PROGRESS_GAIN * Math.max(1, FishingRodUtil.getStat(caughtUsing, Stat.PROGRESS_MULTIPLIER)));
            fish.fishEnergy--;
        } else {
            this.isFinished = true;
            this.isFishWon = true;
            grantReward();
        }
        if (!treasureAvailable) return;
        if (progress > treasureTriggerTime) {
            treasureAvailable = false;
            treasureAvailableTicks = 50;
        }
    }

    private void revokeProgress(){
        tickHealth();
        if (progress > 0) {
            progress = Math.max(0, progress - 0.005f - fishDamage / 100);
        }
    }

    private void tickHookedTreasure(){
        if (!reelingTreasure) return;
        arrowPos = getNextArrowPos();
        treasureHookedTicks--;
        if (treasureHookedTicks == 0) this.isFinished = true;
        if (isReeling() && !isYanking()){
            this.isTreasureWon = isTreasureReeled();
            this.isFinished = true;
        }
    }

    private void tickUnhookedTreasure(){
        if (treasureAvailableTicks == 0 && !reelingTreasure) return;
        treasureAvailableTicks--;
        if (!isReeling() && isYanking()) {
            reelingTreasure = true;
            treasureHookedTicks = (8 - treasureReward.getGrade()) * 60;
        };
    }

    public float nextFishPosition() {
        float fishSpeed = Math.max(fish.fishMinEnergyLevel, fish.fishEnergy) / (fish.fishMaxEnergyLevel * 1f);
        float fishSpeedScaledToLevel = fishSpeed * 0.75f + (fish.fishLevel / 200f);
        float elapsedTime = (fishSpeedScaledToLevel * ticks) % totalDuration;
        float nextFishPosUnbound = nextFishPosOnCurve(elapsedTime) + nextFishPosOnWave(elapsedTime);
        return Math.max(Math.min(nextFishPosUnbound, 1), FISH_LENGTH);
    }

    private float nextFishPosOnWave(float elapsedTime){
        float percentElapsedTime = elapsedTime / totalDuration;
        return  (float) (Math.sin(SINE_PERIOD * percentElapsedTime * WAVE_SPEED) * WAVE_STRENGTH);
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


    private void grantReward(){
        ClientPacketSender.sendFishGameGrantReward(fish, boatFishing);
    }



    private float getBobberAcceleration(){
        return isReeling() ? REELING_ACCELERATION : -REELING_DECELERATION;
    }

    public float nextBobberPos(){
        float nextBobberPosUnbound = bobberPos + bobberSpeed;
        return Math.max(Math.min(nextBobberPosUnbound, 1), bobberLength);
    }


    public float getNextArrowPos(){
        return ((float) Math.sin(ticks / (12f - arrowSpeed)) + 1) / 2;
    }


    private boolean isTreasureReeled(){
        return (arrowPos >= (0.5f - treasureSpotSize / 2)) && (arrowPos <= (0.5f + treasureSpotSize));
    }

    private boolean keyPressed(int keyCode){
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),keyCode);
    }

    private boolean isReeling(){
        return keyPressed(GLFW.GLFW_KEY_SPACE);
    }

    private boolean isYanking(){
        return keyPressed(GLFW.GLFW_KEY_ENTER);
    }

    public float getBobberLength() {
        return bobberLength;
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

    public float getTreasureSpotSize(){
        return this.treasureSpotSize;
    }

    public int getTimeLeft(){
        return treasureHookedTicks / 20;
    }

    public boolean isTreasureOnHook(){
        return treasureAvailableTicks > 0;
    }

    public boolean isReelingTreasure(){
        return reelingTreasure;
    }


    public boolean isFinished() {
        return isFinished;
    }

    public boolean isFishWon() {
        return isFishWon;
    }

    public boolean isTreasureWon(){
        return isTreasureWon;
    }

    public float getArrowPos(){
        return arrowPos;
    }

    public float getFishPos() {
        return fishPos;
    }

    public float getBobberPos() {
        return bobberPos;
    }

    public float getProgress() {
        return progress;
    }


    public float getLineHealth() {
        return lineHealth;
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

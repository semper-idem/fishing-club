package net.semperidem.fishingclub.game;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.game.fish.FishUtil;
import net.semperidem.fishingclub.game.fish.HookedFish;
import net.semperidem.fishingclub.game.treasure.Reward;
import net.semperidem.fishingclub.game.treasure.Rewards;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;
import net.semperidem.fishingclub.util.Point;
import org.lwjgl.glfw.GLFW;

public class FishGameLogic {

    private static final float SINE_PERIOD = (float) (Math.PI * 2);
    private static final float STARTING_BOBBER_LENGTH = 0.25f;
    private static final float FISH_LENGTH = 0.0625f;

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
    private final HookedFish fish;
    private final FishingCard fishingCard;
    private final float fishPatternDuration;
    private final ItemStack caughtUsing;
    private final boolean boatFishing;
    private boolean slowedFish;
    private float fishDamage;
    private float bobberLength;

    private boolean isFinished;
    private boolean isFishCaptured;
    private boolean isTreasureCaptured;
    private boolean reelingTreasure;
    private float arrowSpeed;
    private float treasureSpotSize;
    private float treasureTriggerTime;
    private Reward treasureReward;

    private int ticks;

    private float lineHealth;
    private float progress;

    private float fishPos;

    private float bobberPos;
    private float bobberSpeed;

    private int treasureAvailableTicks;
    private int treasureHookedTicks;
    private float arrowPos;

    float reelForce = 0;

    //TODO MOVE TO FISH PATTERN CLASS
    public int jumpTicks = 0;
    public int jumpCount = 0;
    public int [] jumpTimestamps;

    private BlockPos fishingSpotBlockPos;

    public FishGameLogic(PlayerEntity player, ItemStack caughtUsing, HookedFish fish, boolean boatFishing, BlockPos fishingSpotBlockPos){
        this.player = player;
        this.fishingCard = FishingClubClient.CLIENT_INFO;
        this.fish = fish;
        this.caughtUsing = caughtUsing;
        this.boatFishing = boatFishing;
        calculateBobberLength();
        calculateHealth();
        calculateFishDamage();
        this.fishPatternDuration = fish.curvePoints[fish.curvePoints.length - 1].x;
        this.bobberPos = 0.5f - bobberLength / 2;
        this.fishPos = 0.5f - FISH_LENGTH / 2;
        setTreasure();
        applyFisherVestEffect();

        jumpCount = fish.fishLevel < 10 ? 0 : (int) Math.max(1, fish.fishLevel / 25f * Math.random());
        jumpTimestamps = new int[jumpCount];
        int segmentTime = (int) (fishPatternDuration / jumpCount);
        for(int i = 0; i < jumpCount; i++){
            jumpTimestamps[i] = (int) (segmentTime * Math.random() + segmentTime * i);
        }

        this.slowedFish = player.hasStatusEffect(FStatusEffectRegistry.SLOW_FISH_BUFF);
        this.fishingSpotBlockPos = fishingSpotBlockPos;
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

    private boolean isTreasureAvailable(){
        float treasureChance = TREASURE_MIN_CHANCE;

        boolean boatBoosted = boatFishing && fishingCard.hasPerk(FishingPerks.DOUBLE_TREASURE_BOAT);
        treasureChance *= boatBoosted ? 2 : 1;

        return Math.random() < treasureChance;
    }

    //TODO MOVE TO FISHED TREASURE CLASS
    private void setTreasure(){//TODO IMPLEMENT GOLDEN ROD
        if (!isTreasureAvailable()) return;

        this.treasureTriggerTime = (float) (Math.random() * TREASURE_MAX_TRIGGER_TIME + TREASURE_MIN_TRIGGER_TIME);
        this.treasureReward = Rewards.roll(fishingCard);
        this.arrowSpeed = 1 + treasureReward.getGrade();
        this.treasureSpotSize = TREASURE_MAX_SPOT_SIZE - (treasureReward.getGrade() * TREASURE_GRADE_TO_SPOT_SIZE_RATIO);
    }

    private void calculateBobberLength(){
        float bobberLengthMultiplier = 0.9f;

        bobberLengthMultiplier += boatFishing ? fishingCard.hasPerk(FishingPerks.BOAT_BOBBER_SIZE) ? 0.2f : 0.1f : 0;
        bobberLengthMultiplier += FishingRodPartController.getStat(caughtUsing, FishingRodStatType.BOBBER_WIDTH);
        bobberLengthMultiplier += player.hasStatusEffect(FStatusEffectRegistry.BOBBER_BUFF) ? 0.1f : 0;

        this.bobberLength = STARTING_BOBBER_LENGTH * bobberLengthMultiplier;
    }


    private void calculateFishDamage(){
        float damageReduction = 0;

        damageReduction += FishingRodPartController.getStat(caughtUsing, FishingRodStatType.DAMAGE_REDUCTION);

        boolean boatBoosted = boatFishing && fishingCard.hasPerk(FishingPerks.LINE_HEALTH_BOAT);
        damageReduction += boatBoosted ? 0.2f : 0;
        this.fishDamage =  getFishRawDamage() * (1 - MathHelper.clamp(damageReduction, 0f ,1f));
    }

    private float getFishRawDamage() {
        return Math.max(0, (fish.fishLevel - 5 - (fishingCard.getLevel() * 0.25f)) * 0.05f);
    }

    private void calculateHealth(){
        this.lineHealth = FishingRodPartController.getStat(caughtUsing, FishingRodStatType.LINE_HEALTH);
    }


    public void tick() {
        if(!keyPressed(GLFW.GLFW_KEY_C)) return; //TODO DEBUG FEATURE
        if (reelingTreasure) {
            tickHookedTreasure();
        } else {
            tickFishGame();
        }
        ticks++;
    }

    public void tickJump(){
        if (jumpCount == 0) return;
        if ((int)(ticks % fishPatternDuration) == jumpTimestamps[jumpTimestamps.length - jumpCount]) {
            jumpTicks = 20;
        }
        if (jumpTicks == 0) return;
        jumpTicks--;
    }

    private void tickFishGame(){
        if (!isReeling() && isYanking() && treasureAvailableTicks == 0) this.isFinished = true;
        tickMovement();
        tickProgress();
        tickUnhookedTreasure();
        tickJump();
    }

    private void tickMovement(){
        tickBobberMovement();
        tickFishMovement();
    }

    private void tickFishMovement(){
        fishPos = nextFishPosition();
    }

    private void tickBobberMovement(){
        bobberSpeed = getBobberSpeed();
        bobberSpeed = Math.min(1, Math.max( -1, bobberSpeed));
        bobberPos = nextBobberPos();
    }


    private void tickHealth(){
        if (fishDamage != 0) {
            lineHealth -= (fishDamage);
        }
        if (lineHealth <= 0){
            isFinished = true;
            ClientPacketSender.sendFishGameLost();
        }
    }

    private void tickProgress(){
        boolean bobberHasFish = bobberPos <= fishPos && bobberPos + bobberLength >= fishPos + FISH_LENGTH;
        if (treasureAvailableTicks > 0) return;
        if (isReeling()) {
            if (bobberHasFish && jumpTicks == 0) {
                grantProgress();
            } else {
                revokeProgress();
            }
        }
        if (!bobberHasFish){
            revokeProgress();
        }
    }

    private void grantProgress(){
        if (progress < 1) {
            progress += (PROGRESS_GAIN * Math.max(1, FishingRodPartController.getStat(caughtUsing, FishingRodStatType.PROGRESS_MULTIPLIER)));
            fish.fishEnergy--;
        } else {
            this.isFinished = true;
            this.isFishCaptured = true;
            grantReward();
        }
        tickTreasure();
    }

    private void tickTreasure(){
        if (progress <= treasureTriggerTime) return;
        treasureTriggered();

    }

    private void treasureTriggered(){
        treasureTriggerTime = -1;
        treasureAvailableTicks = 50;

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
            this.isTreasureCaptured = isTreasureReeled();
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
        if (slowedFish) {
            fishSpeed *= 0.75;
        }
        float fishSpeedScaledToLevel = fishSpeed * 0.75f + (fish.fishLevel / 200f);
        float elapsedTime = (fishSpeedScaledToLevel * ticks) % fishPatternDuration;
        float nextFishPosUnbound = nextFishPosOnCurve(elapsedTime) + nextFishPosOnWave(elapsedTime);
        return Math.max(Math.min(nextFishPosUnbound, 1 - FISH_LENGTH), 0);
    }

    private float nextFishPosOnWave(float elapsedTime){
        float percentElapsedTime = elapsedTime / fishPatternDuration;
        return  (float) (Math.sin(SINE_PERIOD * percentElapsedTime * WAVE_SPEED) * WAVE_STRENGTH);
    }

    private float nextFishPosOnCurve(float elapsedTime) {
        elapsedTime %= this.fishPatternDuration;

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
        ClientPacketSender.sendFishGameGrantReward(fish, boatFishing, fishingSpotBlockPos);
    }



    private float getBobberSpeed(){
        return getFishForce() + getReelForce();
    }
    private float getReelForce(){
        return reelForce;
    }

    public void setReelForce(float reelForce){
        this.reelForce = reelForce;
    }

    private float getFishForce() {
        return -((fishPos - 0.5f) / 50f) * ((fish.fishLevel + 50f) / 50);
    }

    public float nextBobberPos(){
        float nextBobberPosUnbound = bobberPos + bobberSpeed;
        return Math.max(Math.min(nextBobberPosUnbound, 1 - bobberLength), 0);
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

    public boolean isFishCaptured() {
        return isFishCaptured;
    }

    public boolean isTreasureCaptured(){
        return isTreasureCaptured;
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

}

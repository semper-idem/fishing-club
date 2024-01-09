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
import net.semperidem.fishingclub.game.components.BobberComponent;
import net.semperidem.fishingclub.game.components.FishComponent;
import net.semperidem.fishingclub.game.components.ProgressComponent;
import net.semperidem.fishingclub.game.fish.FishUtil;
import net.semperidem.fishingclub.game.fish.HookedFish;
import net.semperidem.fishingclub.game.treasure.Reward;
import net.semperidem.fishingclub.game.treasure.Rewards;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;
import org.lwjgl.glfw.GLFW;

public class FishGameController {


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
    private final ItemStack caughtUsing;
    private final boolean boatFishing;
    private float fishDamage;

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

    private int treasureAvailableTicks;
    private int treasureHookedTicks;
    private float arrowPos;

    float reelForce = 0;

    private BlockPos fishingSpotBlockPos;
    private final FishComponent fishComponent;
    private final BobberComponent bobberComponent;
    private final ProgressComponent progressComponent;

    public FishGameController(PlayerEntity player, ItemStack caughtUsing, HookedFish fish, boolean boatFishing, BlockPos fishingSpotBlockPos){
        this.player = player;
        this.fishingCard = FishingClubClient.CLIENT_INFO;
        this.fish = fish;
        this.caughtUsing = caughtUsing;
        this.boatFishing = boatFishing;
        calculateHealth();
        calculateFishDamage();
        setTreasure();
        applyFisherVestEffect();
        progressComponent = new ProgressComponent();
        fishComponent = new FishComponent(fish.getFishType(), fish.fishLevel);
        bobberComponent = new BobberComponent(fish.fishLevel, player, fishingCard, caughtUsing, boatFishing);
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

    private void tickFishGame(){
        if (!isReeling() && isYanking() && treasureAvailableTicks == 0) this.isFinished = true;
        fishComponent.tick(player.hasStatusEffect(FStatusEffectRegistry.SLOW_FISH_BUFF));
        bobberComponent.tick(reelForce, fishComponent.getPositionX());
        progressComponent.tick(
                isReeling(),
                fishComponent.bobberHasFish(bobberComponent.getBobberPos(), bobberComponent.getBobberSize()),
                fishComponent.getPositionY() != 0,
                treasureAvailableTicks > 0
        );
        tickUnhookedTreasure();
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


    public float getReelForce() {
        return reelForce;
    }

    public void setReelForce(float reelForce) {
        this.reelForce = reelForce;
    }

    private void tickTreasure(){
        if (progress <= treasureTriggerTime) return;
        treasureTriggered();

    }

    private void treasureTriggered(){
        treasureTriggerTime = -1;
        treasureAvailableTicks = 50;

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


    private void grantReward(){
        ClientPacketSender.sendFishGameGrantReward(fish, boatFishing, fishingSpotBlockPos);
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

    public float getBobberSize() {
        return bobberComponent.getBobberSize();
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
        return fishComponent.getPositionX();
    }

    public float getBobberPos() {
        return bobberComponent.getBobberPos();
    }

    public float getProgress() {
        return progress;
    }


    public float getLineHealth() {
        return lineHealth;
    }

}

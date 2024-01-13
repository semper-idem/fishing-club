package net.semperidem.fishingclub.game;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.game.components.*;
import net.semperidem.fishingclub.game.fish.HookedFish;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartController;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;

public class FishGameController {
    private static final boolean IS_DEBUG = false;




    private final PlayerEntity player;
    private final HookedFish fish;
    private final FishingCard fishingCard;
    private final ItemStack caughtUsing;
    private final boolean boatFishing;
    private final BlockPos fishingSpotBlockPos;
    private ArrayList<ItemStack> treasureReward = new ArrayList<>();

    private float reelForce = 0;

    private final FishComponent fishComponent;
    private final BobberComponent bobberComponent;
    private final ProgressComponent progressComponent;
    private final HealthComponent healthComponent;
    private final TreasureComponent treasureComponent;

    private TreasureGameController treasureGameController;

    public FishGameController(PlayerEntity player, ItemStack caughtUsing, HookedFish fish, boolean boatFishing, BlockPos fishingSpotBlockPos){
        this.player = player;
        this.fishingCard = FishingClubClient.CLIENT_INFO;
        this.fish = fish;
        this.caughtUsing = caughtUsing;
        this.boatFishing = boatFishing;
        this.fishingSpotBlockPos = fishingSpotBlockPos;
        progressComponent = new ProgressComponent(FishingRodPartController.getStat(caughtUsing, FishingRodStatType.PROGRESS_MULTIPLIER_BONUS), fish.damage);
        fishComponent = new FishComponent(fish.getFishType(), fish.fishLevel);
        bobberComponent = new BobberComponent(fish.fishLevel, player, fishingCard, caughtUsing, boatFishing);
        healthComponent = new HealthComponent(FishingRodPartController.getStat(caughtUsing, FishingRodStatType.LINE_HEALTH), fish.damage);
        treasureComponent = new TreasureComponent(this, boatFishing && fishingCard.hasPerk(FishingPerks.DOUBLE_TREASURE_BOAT));
    }

    public void tick() {
        if(!keyPressed(GLFW.GLFW_KEY_C) && IS_DEBUG) return;
        if (isReelingTreasure()) {
            treasureGameController.tick(isReeling());
        }
        tickInner();
    }

    private void tickInner(){
        boolean isReeling = isReeling();
        boolean isPulling = isPulling();

        boolean hasSlowFishBuff = player.hasStatusEffect(FStatusEffectRegistry.SLOW_FISH_BUFF);
        fishComponent.tick(hasSlowFishBuff);

        bobberComponent.tick(reelForce, fishComponent.getPositionX());

        boolean bobberHasFish = fishComponent.bobberHasFish(bobberComponent.getBobberPos(), bobberComponent.getBobberSize());
        boolean isFishJumping = fishComponent.getPositionY() != 0;
        progressComponent.tick(isReeling, isPulling, bobberHasFish, isFishJumping);
        healthComponent.tick(progressComponent.isWinning());
        treasureComponent.tick(progressComponent.getProgress(), isPulling);
    }



    public float getReelForce() {
        return reelForce;
    }

    public void setReelForce(float reelForce) {
        this.reelForce = reelForce;
    }

    public void startTreasureGame(){
        this.treasureGameController = new TreasureGameController(this, fishingCard);
    }

    public void endTreasureGame(boolean successful) {
        if (successful) {
            treasureReward = this.treasureGameController.getRewards();
        }
        this.treasureGameController = null;
    }

    private boolean keyPressed(int keyCode){
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),keyCode);
    }

    private boolean isReeling(){
        return keyPressed(GLFW.GLFW_KEY_SPACE);
    }

    private boolean isPulling(){
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

    public float getTreasureSpotSize(){
        return isReelingTreasure() ? 0 : treasureGameController.getTreasureSpotSize();
    }

    public int getTimeLeft(){
        return isReelingTreasure() ? 0 : treasureGameController.getTimeLeft();
    }

    public boolean isTreasureAvailable(){
        return treasureComponent.canPullTreasure();
    }

    public boolean isReelingTreasure(){
        return treasureGameController != null;
    }


    public float getArrowPos(){
        return isReelingTreasure() ? 0 : treasureGameController.getArrowPos();
    }
    public float getNextArrowPos(){
        return isReelingTreasure() ? 0 : treasureGameController.getNextArrowPos();
    }

    public float getFishPosX() {
        return fishComponent.getPositionX();
    }
    public float getNextFishPosX() {
        return fishComponent.getNextPositionX();
    }
    public float getFishPosY() {
        return fishComponent.getPositionY();
    }

    public float getBobberPos() {
        return bobberComponent.getBobberPos();
    }
    public float getNextBobberPos() {
        return bobberComponent.getNextBobberPos();
    }

    public float getLineHealth() {
        return healthComponent.getLineHealth();
    }
    public float getProgress() {
        return progressComponent.getProgress();
    }

}

package net.semperidem.fishingclub.game;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.fish.HookedFish;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.network.ClientPacketSender;
import org.lwjgl.glfw.GLFW;



public class FishingGameController {
    private static final boolean IS_DEBUG = false;

    public final PlayerEntity player;
    public final HookedFish fish;
    public final FishingCard fishingCard;

    public float reelForce = 0;

    public FishingGameController(HookedFish hookedFish){
        this.fish = hookedFish;
        this.fishingCard = FishingClubClient.CLIENT_INFO;
        this.player = hookedFish.caughtBy;

        progressComponent = new ProgressComponent(this);
        fishComponent = new FishComponent(this);
        bobberComponent = new BobberComponent(this);
        healthComponent = new HealthComponent(this);
        treasureComponent = new TreasureComponent(this);
        treasureGameController = new TreasureGameController(this);
    }

    public void tick() {
        if(!keyPressed(GLFW.GLFW_KEY_C) && IS_DEBUG) return;
        if (isTreasureHuntActive()) {
            treasureGameController.tick();
        } else {
            tickInner();
        }
    }

    private void tickInner(){
        fishComponent.tick();
        bobberComponent.tick();
        progressComponent.tick();
        healthComponent.tick();
        treasureComponent.tick();
    }

    private boolean keyPressed(int keyCode){
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),keyCode);
    }

    boolean isReeling(){
        return keyPressed(GLFW.GLFW_KEY_SPACE);
    }

    boolean isPulling(){
        return keyPressed(GLFW.GLFW_KEY_ENTER);
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

    public float getBobberSize() {
        return bobberComponent.getLength();
    }

    public float getBobberPos() {
        return bobberComponent.getPositionX();
    }

    public float getNextBobberPos() {
        return bobberComponent.getNextPositionX();
    }

    public float getProgress() {
        return progressComponent.getProgress();
    }

    public float getLineHealth() {
        return healthComponent.getHealth();
    }

    public boolean canPullTreasure(){
        return treasureComponent.canPullTreasure();
    }

    public float getTreasureSpotSize(){
        return treasureGameController.getTreasureSpotSize();
    }

    public int getTimeLeft(){
        return treasureGameController.getTimeLeft();
    }

    public float getArrowPos(){
        return treasureGameController.getArrowPos();
    }

    public float getNextArrowPos(){
        return treasureGameController.getNextArrowPos();
    }

    public void startTreasureHunt() {
        treasureGameController.start(fishingCard);
    }
    public boolean isTreasureHuntActive(){
        return treasureGameController.isActive();
    }
    public void winGame() {
        ClientPacketSender.sendFishGameWon(fish, treasureGameController.getRewards());
    }

    public void loseGame() {
        ClientPacketSender.sendFishGameLost();
    }

    final FishComponent fishComponent;
    final BobberComponent bobberComponent;
    final ProgressComponent progressComponent;
    final HealthComponent healthComponent;
    final TreasureComponent treasureComponent;

    private final TreasureGameController treasureGameController;
}

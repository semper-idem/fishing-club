package net.semperidem.fishingclub.game;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.network.ServerPacketSender;


public class FishingGameController {

    public final PlayerEntity player;
    public final Fish hookedFish;
    public final FishingCard fishingCard;

    public float reelForce = 0;
    private boolean isReeling;
    private boolean isPulling;

    public FishingGameController(FishingCard fishingCard, Fish hookedFish){
        this.hookedFish = hookedFish;
        this.fishingCard = fishingCard;
        this.player = fishingCard.getHolder();

        progressComponent = new ProgressComponent(this);
        fishComponent = new FishComponent(this);
        bobberComponent = new BobberComponent(this);
        healthComponent = new HealthComponent(this);
        treasureComponent = new TreasureComponent(this);
        treasureGameController = new TreasureGameController(this);
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPacketSender.sendInitialFishingGameData(serverPlayerEntity,this);
        }
    }

    public void writeInitialPacket(PacketByteBuf buf) {
        bobberComponent.writeInitialData(buf);
        treasureComponent.writeInitialData(buf);
        treasureGameController.writeInitialData(buf);
    }

    public void readInitialPacket(PacketByteBuf buf) {
        bobberComponent.readInitialData(buf);
        treasureComponent.readInitialData(buf);
        treasureGameController.readInitialData(buf);
    }

    public void writeUpdatePacket(PacketByteBuf buf) {
        bobberComponent.writeData(buf);
        fishComponent.writeData(buf);
        healthComponent.writeData(buf);
        progressComponent.writeData(buf);
        treasureComponent.writeData(buf);
        treasureGameController.writeData(buf);
    }


    public void readUpdatePacket(PacketByteBuf buf) {
        bobberComponent.readData(buf);
        fishComponent.readData(buf);
        healthComponent.readData(buf);
        progressComponent.readData(buf);
        treasureComponent.readData(buf);
        treasureGameController.readData(buf);
    }

    public void tick() {
        if (isTreasureHuntActive()) {
            treasureGameController.tick();
        } else {
            tickInner();
        }
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPacketSender.sendFishingGameData(serverPlayerEntity,this);
        }
        winGame();
    }

    private void tickInner(){
        fishComponent.tick();
        bobberComponent.tick();
        progressComponent.tick();
        healthComponent.tick();
        treasureComponent.tick();
    }

    public boolean isReeling(){
        return  isReeling;
    }
    public boolean isPulling(){
        return  isPulling;
    }

    public void consumeBobberMovementPacket(float reelForce, boolean isReeling, boolean isPulling) {
        this.reelForce = reelForce;
        this.isReeling = isReeling;
        this.isPulling = isPulling;

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

    public boolean bobberHasFish() {
        return bobberComponent.hasFish(fishComponent);
    }

    public void startTreasureHunt() {
        treasureGameController.start();
    }

    public boolean isTreasureHuntActive(){
        return treasureGameController.isActive();
    }

    public void winGame() {
        ClientPacketSender.sendFishGameWon(hookedFish, treasureGameController.getRewards());
    }

    public void loseGame() {
        ClientPacketSender.sendFishGameLost();
    }


    FishComponent fishComponent;
    BobberComponent bobberComponent;
    ProgressComponent progressComponent;
    HealthComponent healthComponent;
    TreasureComponent treasureComponent;

    private final TreasureGameController treasureGameController;
}

package net.semperidem.fishingclub.game;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.fishing_rod.components.ComponentItem;
import net.semperidem.fishingclub.network.ServerPacketSender;

import static net.semperidem.fishingclub.registry.ItemRegistry.MEMBER_FISHING_ROD;


public class FishingGameController {

    public final PlayerEntity player;
    public final FishingCard fishingCard;
    public final Fish hookedFish;

    public float reelForce = 0;
    private boolean isReeling;
    private boolean isPulling;

    public FishingGameController(PlayerEntity playerEntity, Fish hookedFish){
        this.hookedFish = hookedFish;
        this.player = playerEntity;
        this.fishingCard = FishingCard.of(playerEntity);

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
        if (!(this.player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }
        FishUtil.fishCaught(serverPlayer, this.hookedFish);
        FishUtil.giveReward(serverPlayer, this.treasureGameController.getRewards());
        serverPlayer.closeHandledScreen();
    }

    public void loseGame() {
        MEMBER_FISHING_ROD.damageComponents(this.hookedFish.caughtUsing, 4, ComponentItem.DamageSource.BITE, this.player);
        if (!(this.player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }
        serverPlayer.closeHandledScreen();
    }


    FishComponent fishComponent;
    BobberComponent bobberComponent;
    ProgressComponent progressComponent;
    HealthComponent healthComponent;
    TreasureComponent treasureComponent;

    private final TreasureGameController treasureGameController;
}

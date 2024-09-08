package net.semperidem.fishingclub.game;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.network.payload.FishingGameTickPayload;

public class FishingGameController {

    public final PlayerEntity player;
    public final FishingCard fishingCard;
    public final SpecimenData hookedFish;
    public final RodConfiguration rodConfiguration;

    public float reelForce = 0;
    private boolean isReeling;
    private boolean isPulling;

    public FishingGameController(PlayerEntity playerEntity, SpecimenData hookedFish, RodConfiguration rodConfiguration){
        this.hookedFish = hookedFish;
        this.player = playerEntity;
        this.fishingCard = FishingCard.of(playerEntity);
        this.rodConfiguration = rodConfiguration;

        progressComponent = new ProgressComponent(this);
        fishController = new FishController(this);
        bobberComponent = new BobberComponent(this);
        healthComponent = new HealthComponent(this);
        treasureComponent = new TreasureComponent(this);
        treasureGameController = new TreasureGameController(this);
    }

    public void consumeTick(FishingGameTickPayload payload) {
        bobberComponent.consumeData(payload);
        fishController.consumeData(payload);
        healthComponent.consumeData(payload);
        progressComponent.consumeData(payload);
        treasureComponent.consumeData(payload);
        treasureGameController.consumeData(payload);
    }

    public void tick() {
        if (isTreasureHuntActive()) {
            treasureGameController.tick();
        } else {
            tickInner();
        }
        if (player instanceof ServerPlayerEntity serverPlayerEntity) {
            ServerPlayNetworking.send(serverPlayerEntity, new FishingGameTickPayload(
                    bobberComponent.getPositionX(),
                    fishController.getPositionX(),
                    fishController.getPositionY(),
                    healthComponent.getHealth(),
                    progressComponent.getProgress(),
                    treasureComponent.canPullTreasure(),
                    treasureGameController.getArrowPos(),
                    treasureGameController.getTreasureHookedTicks(),
                    treasureGameController.isWon()
            ));
        }
    }

    private void tickInner(){
        fishController.tick();
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
        return fishController.getPositionX();
    }

    public float getNextFishPosX() {
        return fishController.getNextPositionX();
    }

    public float getFishPosY() {
        return fishController.getPositionY();
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
        return bobberComponent.hasFish(fishController);
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
        //this.hookedUsing.damage(4, PartItem.DamageSource.BITE, this.player);
        if (!(this.player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }
        serverPlayer.closeHandledScreen();
    }


    FishController fishController;
    BobberComponent bobberComponent;
    ProgressComponent progressComponent;
    HealthComponent healthComponent;
    TreasureComponent treasureComponent;

    private final TreasureGameController treasureGameController;
}

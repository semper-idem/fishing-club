package net.semperidem.fishingclub.game;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.managers.StatusEffectHelper;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.network.payload.FishingUpdatePayload;
import net.semperidem.fishingclub.screen.fishing_post.FishingPostScreenHandlerFactory;

public class FishingController {

    final PlayerEntity player;
    final Card card;
    final SpecimenData hookedFish;
    final RodConfiguration rodConfiguration;

    public float reelForce = 0;
    private int fishCaughtSlot = -1;
    private boolean isReeling = false;

    private int ticksBeforeClose = 30;
    private boolean isWon = false;

    public FishingController(PlayerEntity playerEntity, SpecimenData hookedFish, RodConfiguration rodConfiguration) {
        this.hookedFish = hookedFish;
        this.player = playerEntity;
        this.card = Card.of(playerEntity);
        this.rodConfiguration = rodConfiguration;

        progressComponent = new ProgressComponent(this);
        fishController = new FishController(this);
        bobberComponent = new BobberComponent(this);
        healthComponent = new HealthComponent(this);
        treasureComponent = new TreasureComponent(this);
        treasureGameController = new TreasureGameController(this);
    }

    public void updateClient(FishingUpdatePayload payload) {
        bobberComponent.updateClient(payload);
        fishController.updateClient(payload);
        healthComponent.updateClient(payload);
        progressComponent.updateClient(payload);
        treasureComponent.updateClient(payload);
        this.isReeling = payload.isReeling();
        treasureGameController.updateClient(payload);

    }

    public void tick() {
        this.tickLogic();
        this.sendUpdate();
    }

    private void sendUpdate() {
        ServerPlayNetworking.send((ServerPlayerEntity)this.player, new FishingUpdatePayload(
                bobberComponent.getPositionX(),
                fishController.getPositionX(),
                fishController.getPositionY(),
                healthComponent.getHealth(),
                progressComponent.getProgress(),
                treasureComponent.canPullTreasure(),
                treasureComponent.getProgress(),
                treasureComponent.getPosition(),
                treasureGameController.getProgress(),
                treasureGameController.getTicksLeft(),
                isReeling(),
                treasureGameController.isDone()
        ));
    }

    private void tickLogic() {
        this.tickWon();
//        if (this.isWon) {
//           this.tickWon();
//           return;
//        }
//        if (this.treasureGameController.isActive()) {
//            this.treasureGameController.tick();
//            return;
//        }
//       this.fishController.tick();
//       this.bobberComponent.tick();
//       this.progressComponent.tick();
//       this.treasureComponent.tick();
//       this.healthComponent.tick();
    }


    public boolean isReeling() {
        return isReeling;
    }

    public void consumeBobberMove(float reelForce) {
        this.reelForce = reelForce;
    }

    public void consumeReel(boolean isReeling) {
        this.isReeling = isReeling;
        if (!isReeling) {
            this.treasureGameController.letReel();
        }
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

    public boolean canPullTreasure() {
        return treasureComponent.canPullTreasure();
    }

    public boolean bobberHasFish() {
        return bobberComponent.hasFish(fishController);
    }

    public void startTreasureHunt() {
        treasureGameController.start();
    }

    public boolean isTreasureHuntActive() {
        return treasureGameController.isActive();
    }

    public void tickWon() {
        if (this.ticksBeforeClose > 0) {
            this.ticksBeforeClose--;
            return;
        }
        this.player.openHandledScreen(new FishingPostScreenHandlerFactory(
                this.hookedFish,
                this.treasureGameController.getRewards(),
                this.hookedFish.experience(StatusEffectHelper.getExpMultiplier(player)
                )
        ));
    }

    public void winGame() {
        this.isWon = true;
    }

    public void loseGame() {
        if (!(this.player instanceof ServerPlayerEntity serverPlayer)) {
            return;
        }
        serverPlayer.closeHandledScreen();
    }

    public float getTreasureStartProgress() {
        return this.treasureComponent.getProgress();
    }

    public float getTreasureStartPosition() {
        return this.treasureComponent.getPosition();
    }

    public float getTreasureProgress() {
        return this.treasureGameController.getProgress();
    }

    public String getTreasureTimeLeft() {
        return String.valueOf(this.treasureGameController.getTimeLeft());
    }

    public boolean isTreasureDone() {
        return this.treasureGameController.isDone();
    }

    public int getLevel() {
        return this.hookedFish.level();
    }

    FishController fishController;
    BobberComponent bobberComponent;
    ProgressComponent progressComponent;
    HealthComponent healthComponent;
    TreasureComponent treasureComponent;

    private final TreasureGameController treasureGameController;
}

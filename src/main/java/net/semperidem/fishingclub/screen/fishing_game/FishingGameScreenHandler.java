package net.semperidem.fishingclub.screen.fishing_game;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.game.FishingGameController;
import net.semperidem.fishingclub.network.payload.FishingGamePayload;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

public class FishingGameScreenHandler extends ScreenHandler {
    public final FishingGameController fishGameLogic;

    public FishingGameScreenHandler(int syncId, PlayerInventory playerInventory, FishingGamePayload payload) {
        super(ScreenHandlerRegistry.FISHING_GAME_SCREEN, syncId);
        this.fishGameLogic = new FishingGameController(playerInventory.player, payload.fishComponent(), payload.configurationComponent());
    }

    public void consumeBobberMovement(float reelForce, boolean isReeling, boolean isPulling) {
        fishGameLogic.consumeBobberMovementPacket(reelForce, isReeling, isPulling);
    }


    /*
    * Called every tick inside ServerPlayerEntity might as well be "tickHandledScreen"
    * 20 packets per second could be unnecessary
    * */
    @Override
    public void sendContentUpdates() {
        fishGameLogic.tick();
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}

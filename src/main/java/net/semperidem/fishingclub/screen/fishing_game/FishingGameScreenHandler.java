package net.semperidem.fishingclub.screen.fishing_game;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.game.FishingGameController;
import net.semperidem.fishingclub.network.payload.FishingGameStartS2CPayload;
import net.semperidem.fishingclub.registry.FCScreenHandlers;
import net.semperidem.fishingclub.screen.fishing_game_post.FishingGamePostScreenHandlerFactory;

public class FishingGameScreenHandler extends ScreenHandler {
    public final FishingGameController controller;


    public FishingGameScreenHandler(int syncId, PlayerInventory playerInventory, FishingGameStartS2CPayload payload) {
        super(FCScreenHandlers.FISHING_GAME_SCREEN, syncId);
        this.controller = new FishingGameController(playerInventory.player, payload.fishComponent(), payload.configurationComponent());
    }

    public void consumeBobberMove(float reelForce) {
        this.controller.consumeBobberMove(reelForce);
    }

    public void consumeReel(boolean isReeling) {
        this.controller.consumeReel(isReeling);
    }


    /*
    * Called every tick inside ServerPlayerEntity might as well be "tickHandledScreen"
    * 20 packets per second could be unnecessary
    * */
    @Override
    public void sendContentUpdates() {
        this.controller.tick();
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

package net.semperidem.fishingclub.screen.fishing_game;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.game.FishingGameController;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

public class FishingGameScreenHandler extends ScreenHandler {
    public final FishingCard fishingCard;
    public final Fish hookedFish;
    public final FishingGameController fishGameLogic;

    public FishingGameScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(ScreenHandlerRegistry.FISH_GAME_SCREEN, syncId);
        NbtCompound fishingCardNbt = buf.readNbt();
        NbtCompound fishNbt = buf.readNbt();

        fishingCard = new FishingCard(playerInventory.player, fishingCardNbt);
        hookedFish = new Fish(fishNbt);
        this.fishGameLogic = new FishingGameController(fishingCard, hookedFish);
    }

    public FishingGameScreenHandler(int syncId, PlayerInventory playerInventory, FishingCard fishingCard,  Fish fish) {
        super(ScreenHandlerRegistry.FISH_GAME_SCREEN, syncId);
        this.fishingCard = fishingCard;
        this.hookedFish = fish;
        this.fishGameLogic = new FishingGameController(fishingCard, hookedFish);
    }

    public void consumeBobberMovement(float reelForce, boolean isReeling, boolean isPulling) {
        fishGameLogic.consumeBobberMovementPacket(reelForce, isReeling, isPulling);
    }

    @Override
    public void sendContentUpdates() {
        fishGameLogic.tick();
        super.sendContentUpdates();
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}

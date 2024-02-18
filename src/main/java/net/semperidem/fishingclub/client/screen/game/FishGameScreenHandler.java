package net.semperidem.fishingclub.client.screen.game;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

public class FishGameScreenHandler extends ScreenHandler {
    FishingCard fishingCard;
    Fish hookedFish;
    public FishGameScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(ScreenHandlerRegistry.FISH_GAME_SCREEN, syncId);
        NbtCompound fishingCardNbt = buf.readNbt();
        NbtCompound fishNbt = buf.readNbt();

        fishingCard = new FishingCard(playerInventory.player, fishingCardNbt);
        hookedFish = new Fish(fishNbt);
    }

    public FishGameScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ScreenHandlerRegistry.FISH_GAME_SCREEN, syncId);
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

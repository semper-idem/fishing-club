package net.semperidem.fishingclub.client.screen.game;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardSerializer;
import net.semperidem.fishingclub.registry.FScreenHandlerRegistry;
public class FishGameScreenHandler extends ScreenHandler {
    FishingCard fishingCard;
    Fish hookedFish;
    public FishGameScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        super(FScreenHandlerRegistry.FISH_GAME_SCREEN, syncId);
        fishingCard = FishingCardSerializer.fromNbt(playerInventory.player, buf.readNbt());
        hookedFish = new Fish(buf.readNbt());
    }

    public FishGameScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(FScreenHandlerRegistry.FISH_GAME_SCREEN, syncId);
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

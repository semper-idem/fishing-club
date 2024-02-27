package net.semperidem.fishingclub.client.screen.dialog;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;
import org.jetbrains.annotations.Nullable;

public class DialogScreenHandler extends ScreenHandler {
    public DialogScreenHandler(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new FishingCard(playerInventory.player, buf.readNbt()));
    }

    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, FishingCard fishingCard) {
        super(ScreenHandlerRegistry.DIALOG_SCREEN, syncId);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}

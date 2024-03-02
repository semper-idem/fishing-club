package net.semperidem.fishingclub.client.screen.dialog;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

import java.util.HashSet;

public class DialogScreenHandler extends ScreenHandler {
    HashSet<DialogKey> openingKeys;
    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory,  buf.readString());
    }


    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, String openingKeyString) {
        super(ScreenHandlerRegistry.DIALOG_SCREEN, syncId);
        openingKeys = DialogUtil.getKeysFromString(openingKeyString);
    }

    public HashSet<DialogKey> getOpeningKeys(){
        return openingKeys;
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

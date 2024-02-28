package net.semperidem.fishingclub.client.screen.dialog;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

import java.util.HashSet;
import java.util.List;

public class DialogScreenHandler extends ScreenHandler {
    HashSet<String> openingKeys = new HashSet<>();
    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory,  buf.readString());
    }


    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, String openingKeyString) {
        super(ScreenHandlerRegistry.DIALOG_SCREEN, syncId);
        openingKeys.addAll(List.of(openingKeyString.split(";")));
    }

    public HashSet<String> getOpeningKeys(){
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

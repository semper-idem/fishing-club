package net.semperidem.fishingclub.screen.dialog;

import static net.semperidem.fishingclub.screen.dialog.DialogKey.*;

import java.util.HashSet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.network.payload.DialogPayload;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

public class DialogScreenHandler extends ScreenHandler {
    HashSet<DialogKey> openingKeys;

    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, DialogPayload dialogPayload) {
        this(syncId, playerInventory, dialogPayload.openingKeys());
    }

    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, String openingKeyString) {
        super(ScreenHandlerRegistry.DIALOG_SCREEN, syncId);
        this.openingKeys = DialogUtil.getKeysFromString(openingKeyString);//new HashSet<>(Set.of(GOLDEN, NOT_WELCOME, NOT_REPEATED, UNIQUE, SUMMONER));
    }

    public HashSet<DialogKey> getOpeningKeys() {
        return openingKeys;
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int index) {
        return null;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }

    @Override
    public void onClosed(PlayerEntity player) {
        super.onClosed(player);
    }
}

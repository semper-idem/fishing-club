package net.semperidem.fishingclub.screen.dialog;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

import java.util.HashSet;
import java.util.Set;

import static net.semperidem.fishingclub.screen.dialog.DialogKey.*;

public class DialogScreenHandler extends ScreenHandler {
    HashSet<DialogKey> openingKeys;
    FishermanEntity fishermanEntity;
    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory,  buf.readString(), null);
    }

    public DialogScreenHandler(int syncId, PlayerInventory playerInventory, String openingKeyString, FishermanEntity fishermanEntity) {
        super(ScreenHandlerRegistry.DIALOG_SCREEN, syncId);
        this.openingKeys = new HashSet<>(Set.of(GOLDEN, NOT_WELCOME, NOT_REPEATED, UNIQUE, SUMMONER));
        this.fishermanEntity = fishermanEntity;
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

    @Override
    public void close(PlayerEntity player) {
        super.close(player);
    }
}

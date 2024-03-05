package net.semperidem.fishingclub.screen.dialog;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.registry.ScreenHandlerRegistry;

import java.util.HashSet;

public class MemberScreenHandler extends ScreenHandler {
    HashSet<DialogKey> openingKeys;
    FishermanEntity fishermanEntity;
    public MemberScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory,  buf.readString(), null);
    }

    public MemberScreenHandler(int syncId, PlayerInventory playerInventory, String openingKeyString, FishermanEntity fishermanEntity) {
        super(ScreenHandlerRegistry.DIALOG_SCREEN, syncId);
        this.openingKeys = DialogUtil.getKeysFromString(openingKeyString);
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
        if (fishermanEntity != null) {
            fishermanEntity.setCustomer(null);
        }
        super.close(player);
    }
}

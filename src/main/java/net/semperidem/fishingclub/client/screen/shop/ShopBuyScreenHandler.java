package net.semperidem.fishingclub.client.screen.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class ShopBuyScreenHandler  extends ScreenHandler {

    public ShopBuyScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ShopScreenUtil.SHOP_BUY_SCREEN, syncId);

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);
    }

    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addSlot(new Slot(playerInventory, x + y * SLOTS_PER_ROW + 9, 8 + x * SLOT_SIZE, 139 + y * SLOT_SIZE));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addSlot(new Slot(playerInventory, x, 8 + x * SLOT_SIZE, 197));
        }
    }


    @Override
    public ItemStack transferSlot(PlayerEntity playerEntity, int i) {
        return ItemStack.EMPTY;
    }

    public void close(PlayerEntity player) {
        super.close(player);
    }

    @Override
    public boolean canUse(PlayerEntity playerEntity) {
        return true;
    }
}
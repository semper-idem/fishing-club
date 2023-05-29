package net.semperidem.fishingclub.client.screen.shop;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.fish.FishUtil;

public class FishSlot extends Slot {
    public FishSlot(Inventory inventory, int i, int j, int k) {
        super(inventory, i, j, k);
    }

    @Override
    public boolean canInsert(ItemStack itemStack) {
        return itemStack.isOf(FishUtil.FISH_ITEM);
    }
}

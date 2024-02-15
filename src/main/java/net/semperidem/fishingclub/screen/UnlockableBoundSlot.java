package net.semperidem.fishingclub.screen;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class UnlockableBoundSlot extends UnlockableSlot{
    private final Item boundItem;

    public UnlockableBoundSlot(
            Inventory inventory, int index, int x, int y,
            FishingCardScreenHandler parent, FishingCardTab tab,
            boolean isUnlocked,
            Item boundItem
    ){
        super(inventory, index, x, y, parent, tab, isUnlocked);
        this.boundItem = boundItem;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.getItem().getClass() == boundItem.getClass();
    }
}

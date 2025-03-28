package net.semperidem.fishingclub.screen.fishing_card;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class UnlockableBoundSlot extends UnlockableSlot{
    private final Item boundItem;

    public UnlockableBoundSlot(
            Inventory inventory, int index, int x, int y,
            OldFishingCardScreenHandler parent,
            boolean isUnlocked,
            Item boundItem
    ){
        super(inventory, index, x, y, parent,isUnlocked);
        this.boundItem = boundItem;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return boundItem.getClass().isAssignableFrom(stack.getItem().getClass());
    }
}

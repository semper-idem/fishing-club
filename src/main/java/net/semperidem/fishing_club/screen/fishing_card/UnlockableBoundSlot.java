package net.semperidem.fishing_club.screen.fishing_card;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.semperidem.fishing_club.fisher.perks.Path;

public class UnlockableBoundSlot extends UnlockableSlot{
    private final Item boundItem;

    public UnlockableBoundSlot(
            Inventory inventory, int index, int x, int y,
            FishingCardScreenHandler parent, Path tab,
            boolean isUnlocked,
            Item boundItem
    ){
        super(inventory, index, x, y, parent, tab, isUnlocked);
        this.boundItem = boundItem;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return boundItem.getClass().isAssignableFrom(stack.getItem().getClass());
    }
}

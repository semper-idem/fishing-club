package net.semperidem.fishingclub.screen;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.fish.FishUtil;

public class InstantSellSlot extends UnlockableBoundSlot {
    public InstantSellSlot(Inventory inventory, int index, int x, int y, FishingCardScreenHandler parent, FishingCardTab tab, boolean isUnlocked, Item boundItem) {
        super(inventory, index, x, y, parent, tab, isUnlocked, boundItem);
    }

    @Override
    public int getMaxItemCount () {
        return 1;
    }

    @Override
    public void setStack (ItemStack stack){
        ItemStack currentStack = getStack();
        if (!currentStack.isEmpty()) {
            parent.fishingCard.addCredit(FishUtil.getFishValue(currentStack));
            currentStack.setCount(0);
        }
        super.setStack(stack);
    }
}

package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.FishingRodPartItem;

public class RodPartSlot extends Slot {
    FishingRodPartItem.PartType partType;
    public RodPartSlot(Inventory inventory, int index, int x, int y, FishingRodPartItem.PartType partType) {
        super(inventory, index, x, y);
        this.partType = partType;
    }


    @Override
    public boolean canInsert(ItemStack itemStack) {
        return itemStack.getItem() instanceof FishingRodPartItem
                && ((FishingRodPartItem) itemStack.getItem()).getPartType() == partType;
    }
}

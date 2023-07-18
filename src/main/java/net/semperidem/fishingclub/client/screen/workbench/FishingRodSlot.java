package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.FishingRodPartItem;


import static net.semperidem.fishingclub.FishingClub.CUSTOM_FISHING_ROD;

public class FishingRodSlot extends Slot {
    public FishingRodSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(CUSTOM_FISHING_ROD);//TODO Add vanilla fishing rod transformmation
    }

    @Override
    public void setStack(ItemStack stack) {
        handleFishingRodUnpacking(stack);
        super.setStack(stack);
    }

    private void handleFishingRodUnpacking(ItemStack stack){
        if (stack.isOf(CUSTOM_FISHING_ROD)) {
            for(FishingRodPartItem.PartType partType : FishingRodPartItem.PartType.values()) {
                this.inventory.setStack(partType.slotIndex, CUSTOM_FISHING_ROD.getPart(stack, partType));
            }
        }
    }
}

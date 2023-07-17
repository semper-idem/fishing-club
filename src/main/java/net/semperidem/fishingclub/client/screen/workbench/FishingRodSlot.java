package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.item.CustomFishingRod;
import net.semperidem.fishingclub.item.FishingRodPartItem;

public class FishingRodSlot extends Slot {
    int index;
    public FishingRodSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
        this.index = index;
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return stack.isOf(FishingClub.CUSTOM_FISHING_ROD);//TODO Add vanilla fishing rod transformmation
    }

    @Override
    public void setStack(ItemStack stack) {
        if (stack.isOf(FishingClub.CUSTOM_FISHING_ROD)) {
            for(FishingRodPartItem.PartType partType : FishingRodPartItem.PartType.values()) {
                CustomFishingRod rodItem = ((CustomFishingRod)FishingClub.CUSTOM_FISHING_ROD);
                if (rodItem.hasPart(stack, partType)) {
                    ItemStack partStack = ((CustomFishingRod)FishingClub.CUSTOM_FISHING_ROD).getPart(stack, partType);
                    this.inventory.setStack(partType.slotIndex, partStack);
                }
            }
        } else {
            ItemStack rodStack = this.inventory.getStack(0);
            for(FishingRodPartItem.PartType partType : FishingRodPartItem.PartType.values()) {
                CustomFishingRod rodItem = (CustomFishingRod) FishingClub.CUSTOM_FISHING_ROD;
                ItemStack partStack = this.inventory.getStack(partType.slotIndex);
                if (!partStack.isEmpty()) {
                    rodItem.addPart(rodStack,this.inventory.getStack(partType.slotIndex), partType);
                }
                this.inventory.setStack(partType.slotIndex, ItemStack.EMPTY);
            }
        }
        this.inventory.setStack(this.index, stack);
        this.markDirty();
    }
}

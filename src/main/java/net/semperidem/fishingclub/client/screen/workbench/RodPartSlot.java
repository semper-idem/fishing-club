package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItem;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;

public class RodPartSlot extends Slot {
    FishingRodPartType partType;
    FisherWorkbenchScreenHandler parent;

    public RodPartSlot(FisherWorkbenchScreenHandler parent, FishingRodPartType partType, int x, int y){
        super(parent.getInventory(), parent.getNextIndex(), x, y);
        this.partType = partType;
        this.parent = parent;
    }


    @Override
    public boolean isEnabled() {
        return parent.isSlotEnabled(this);
    }


    @Override
    public boolean canTakeItems(PlayerEntity playerEntity) {
        if (inventory.getStack(getIndex()).getItem() instanceof FishingRodPartItem rodPart) {
            boolean isRodPartCore = rodPart.getPartType() == FishingRodPartType.CORE;
            if (isRodPartCore) {
                return !inventory.getStack(0).isDamaged();
            }
        }
        return true;
    }


    @Override
    public boolean canInsert(ItemStack itemStack) {
        return !this.inventory.getStack(0).isEmpty()
                && itemStack.getItem() instanceof FishingRodPartItem
                && ((FishingRodPartItem) itemStack.getItem()).getPartType() == partType;
    }
}

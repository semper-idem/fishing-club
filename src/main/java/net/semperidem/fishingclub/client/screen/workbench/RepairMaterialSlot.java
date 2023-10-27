package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class RepairMaterialSlot extends Slot {
    FisherWorkbenchScreenHandler fisherWorkbenchScreenHandler;

    public RepairMaterialSlot(Inventory inventory, int index, int x, int y, FisherWorkbenchScreenHandler fisherWorkbenchScreenHandler) {
        super(inventory, index, x, y);
        this.fisherWorkbenchScreenHandler = fisherWorkbenchScreenHandler;
    }

    @Override
    public boolean isEnabled() {
        return fisherWorkbenchScreenHandler.enabledSlots.contains(this);
    }

    @Override
    public boolean canInsert(ItemStack stack) {//TODO CHANGE REPAIR ITEM
        return stack.getItem() == Items.STRING && super.canInsert(stack);
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        FisherWorkbenchScreen screen = fisherWorkbenchScreenHandler.getScreen();
        if (screen != null ) {
            screen.repairButton.active = true;
        }
        return super.insertStack(stack, count);
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        FisherWorkbenchScreen screen = fisherWorkbenchScreenHandler.getScreen();
        if (screen != null ) {
            screen.repairButton.active = false;
        }
        super.onTakeItem(player, stack);
    }

}


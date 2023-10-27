package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.Slot;

public class RepairMaterialSlot extends Slot {

    public RepairMaterialSlot(Inventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }


    @Override
    public boolean canInsert(ItemStack stack) {//TODO CHANGE REPAIR ITEM
        return stack.getItem() == Items.STRING && super.canInsert(stack);
    }

    @Override
    public ItemStack insertStack(ItemStack stack, int count) {
        return super.insertStack(stack, count);
    }

    @Override
    public void onTakeItem(PlayerEntity player, ItemStack stack) {
        //TODO activate repair button
        super.onTakeItem(player, stack);
    }

}


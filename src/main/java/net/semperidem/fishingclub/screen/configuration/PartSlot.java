package net.semperidem.fishingclub.screen.configuration;

import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.item.fishing_rod.components.CorePartItem;
import net.semperidem.fishingclub.item.fishing_rod.components.RodPartComponent;

import java.util.Optional;
import java.util.function.Consumer;

public class PartSlot extends Slot {
    private final Item boundItem;
    private final Consumer<ItemStack> equipPartAction;
    public PartSlot(Inventory inventory, int index, int x, int y, Item boundItem, Consumer<ItemStack> equipPartAction) {
        super(inventory, index, x, y);
        this.boundItem = boundItem;
        this.equipPartAction = equipPartAction;
    }

    @Override
    public void setStack(ItemStack stack, ItemStack previousStack) {
        equipPartAction.accept(stack);
        super.setStack(stack, previousStack);
    }

    @Override
    public boolean canInsert(ItemStack stack) {
        return boundItem.getClass().isAssignableFrom(stack.getItem().getClass());
    }
}

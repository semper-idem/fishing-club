package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;

public class FishingCardInventory implements Inventory{
    public static final int SLOT_COUNT = 5;
    DefaultedList<ItemStack> inventory = DefaultedList.ofSize (SLOT_COUNT, ItemStack.EMPTY);

    @Override
    public int size() {
        return SLOT_COUNT;
    }

    @Override
    public boolean isEmpty() {
        return inventory.stream().allMatch(ItemStack::isEmpty);
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.get(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        ItemStack itemStack = Inventories.splitStack(inventory, slot, amount);
        if (!itemStack.isEmpty()) {
            markDirty();
        }

        return itemStack;
    }

    @Override
    public ItemStack removeStack(int slot) {
        return Inventories.removeStack(inventory, slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.set(slot, stack);
        if (stack.getCount() > getMaxCountPerStack()) {
            stack.setCount(getMaxCountPerStack());
        }

        markDirty();
    }

    @Override
    public void markDirty() {

    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return false;
    }

    @Override
    public void clear() {

    }
}

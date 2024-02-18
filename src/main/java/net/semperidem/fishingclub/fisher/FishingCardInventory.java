package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.collection.DefaultedList;

public class FishingCardInventory implements Inventory{
    public static final int SLOT_COUNT = 5;
    DefaultedList<ItemStack> inventory = DefaultedList.ofSize (SLOT_COUNT, ItemStack.EMPTY);
    ItemStack sharedBait = ItemStack.EMPTY;
    int credit = 0;

    private static final String CREDIT_TAG = "credit";

    public int getCredit(){
        return credit;
    }

    public boolean addCredit(int credit) {
        if (this.credit + credit < 0) {
            return false;
        }

        this.credit += credit;
        return true;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public ItemStack getSharedBait(){
        return sharedBait;
    }

    public void setSharedBait(ItemStack baitToShare){
        this.sharedBait = baitToShare;
    }

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

    public void writeNbt(NbtCompound tag) {
        tag.putInt(CREDIT_TAG, credit);
        Inventories.writeNbt(tag, inventory);
    }

    public void readNbt(NbtCompound tag) {
        credit = tag.getInt(CREDIT_TAG);
        Inventories.readNbt(tag, inventory);
    }
}

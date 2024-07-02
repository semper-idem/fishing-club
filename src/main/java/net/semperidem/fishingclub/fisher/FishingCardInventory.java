package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;

public class FishingCardInventory implements Inventory{
    public static final int SLOT_COUNT = 5;
    DefaultedList<ItemStack> inventory = DefaultedList.ofSize (SLOT_COUNT, ItemStack.EMPTY);
    ItemStack sharedBait = ItemStack.EMPTY;
    int credit = 0;
    boolean isDirty = true;

    private static final String CREDIT_TAG = "credit";
    private static final String BAIT_TAG = "bait";

    public int getCredit(){
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public ItemStack getSharedBait(){
        return sharedBait;
    }

    public void setSharedBait(ItemStack baitToShare){
        this.sharedBait = baitToShare;
        this.isDirty = true;
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

    public void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (!isDirty) {
            return;
        }
        this.isDirty = false;
        Inventories.writeNbt(tag, inventory, wrapperLookup);
        tag.putInt(CREDIT_TAG, credit);
        if (sharedBait.isEmpty()) {
            return;
        }
        tag.put(BAIT_TAG, sharedBait.encode(wrapperLookup));
    }

    public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup wrapperLookup) {
        if (tag.contains(CREDIT_TAG)) {
            credit = tag.getInt(CREDIT_TAG);
        }
        Inventories.readNbt(tag, inventory, wrapperLookup);
        sharedBait = ItemStack.fromNbt(wrapperLookup, tag.get(BAIT_TAG)).orElse(ItemStack.EMPTY);
    }
}

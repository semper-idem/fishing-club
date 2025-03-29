package net.semperidem.fishingclub.fisher;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.collection.DefaultedList;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.FishingNetItem;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.Tags;

public class CardInventory implements Inventory{
    public static final int SLOT_COUNT = 4;
    DefaultedList<ItemStack> inventory = DefaultedList.ofSize (SLOT_COUNT, ItemStack.EMPTY);
    ItemStack sharedBait = ItemStack.EMPTY;
    int credit = 0;

    private static final String CREDIT_TAG = "credit";
    private static final String BAIT_TAG = "bait";

    public int getCredit(){
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    void sell() {
        for(int i = 0; i < SLOT_COUNT; i++) {
            ItemStack inventoryStack = inventory.get(i);
            if (!inventoryStack.isIn(Tags.FISH_ITEM)) {
                continue;
            }
            credit += (inventoryStack.getOrDefault(Components.SPECIMEN, SpecimenData.DEFAULT).value() * inventoryStack.getCount());
            inventoryStack.setCount(0);
            return;
        }
    }

    public ItemStack getSharedBait(){
        return sharedBait;
    }

    public void setSharedBait(ItemStack baitToShare){
        this.sharedBait = baitToShare;
    }

    public ItemStack getFishingNet(ItemStack fishStack) {
        for (ItemStack stack : inventory) {
            if (!(stack.getItem() instanceof FishingNetItem fishingNetItem)) {
                continue;
            }
            if (fishingNetItem.canInsert(stack, fishStack)) {
                return stack;
            }
        }
        return ItemStack.EMPTY;
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
        if (tag.contains("Items")) {
            Inventories.readNbt(tag, inventory, wrapperLookup);
        }
        if (tag.contains(BAIT_TAG)) {
            sharedBait = ItemStack.fromNbt(wrapperLookup, tag.get(BAIT_TAG)).orElse(ItemStack.EMPTY);

        }
    }
}

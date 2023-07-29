package net.semperidem.fishingclub.util;

import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.semperidem.fishingclub.FishingClub;

public class InventoryUtil {
    //Main difference from vanilla inventory to nbt methods is it remembers slot number
    public static SimpleInventory readInventory(NbtCompound inventoryTag){
        int size = inventoryTag.getInt("size");
        SimpleInventory inventory = new SimpleInventory(size);
        NbtList inventoryListTag = inventoryTag.getList("content", NbtElement.COMPOUND_TYPE);
        inventoryListTag.forEach(nbtElement -> {
            NbtCompound stackTag = (NbtCompound) nbtElement;
            int slot = stackTag.getInt("slot");
            ItemStack stack = ItemStack.fromNbt(stackTag.getCompound("stack"));
            inventory.setStack(slot, stack);
        });
        return inventory;
    }

    public static SimpleInventory readFishingNetInventory(ItemStack fishingNetStack){
        NbtCompound fishingNetInventoryTag;
        if (!fishingNetStack.getOrCreateNbt().contains("inventory")) {
            fishingNetInventoryTag = new NbtCompound();
            fishingNetInventoryTag.putInt("size", fishingNetStack.isOf(FishingClub.FISHING_NET) ? 27 : 54);
            fishingNetInventoryTag.put("content", new NbtList());
            fishingNetStack.getNbt().put("inventory", fishingNetInventoryTag);
        } else {
            fishingNetInventoryTag = fishingNetStack.getNbt().getCompound("inventory");
        }

        int size = fishingNetInventoryTag.getInt("size");
        SimpleInventory inventory = new SimpleInventory(size){
            @Override
            public void markDirty() {
                fishingNetStack.getOrCreateNbt().put("inventory", InventoryUtil.writeInventory(this));
                super.markDirty();
            }
        };
        NbtList inventoryListTag = fishingNetInventoryTag.getList("content", NbtElement.COMPOUND_TYPE);
        inventoryListTag.forEach(nbtElement -> {
            NbtCompound stackTag = (NbtCompound) nbtElement;
            int slot = stackTag.getInt("slot");
            ItemStack stack = ItemStack.fromNbt(stackTag.getCompound("stack"));
            inventory.setStack(slot, stack);
        });
        return inventory;
    }

    public static NbtCompound writeInventory(SimpleInventory inventory){
        NbtCompound inventoryTag = new NbtCompound();
        inventoryTag.putInt("size", inventory.size());
        NbtList inventoryListTag = new NbtList();
        for(int slot = 0; slot < inventory.size(); slot++) {
            ItemStack stack = inventory.getStack(slot);
            if (!stack.isEmpty()) {
                NbtCompound stackTag = new NbtCompound();
                stackTag.putInt("slot", slot);
                stackTag.put("stack", stack.writeNbt(new NbtCompound()));
                inventoryListTag.add(stackTag);
            }
        }
        inventoryTag.put("content", inventoryListTag);

        return inventoryTag;
    }
}

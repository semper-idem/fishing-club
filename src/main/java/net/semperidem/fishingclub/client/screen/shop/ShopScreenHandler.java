package net.semperidem.fishingclub.client.screen.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.ScreenHandlerSlotUpdateS2CPacket;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.FishingClub;

public class ShopScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final int rows;
    private final PlayerEntity player;


    public ShopScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(FishingClub.SHOP_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(27);
        this.rows = 3;
        this.player = playerInventory.player;
        checkSize(inventory, this.rows * 9);
        inventory.onOpen(playerInventory.player);
        int k = (this.rows - 4) * 18;

        int l;
        int m;
        for(l = 0; l < this.rows; ++l) {
            for(m = 0; m < 9; ++m) {
                this.addSlot(new Slot(inventory, m + l * 9, 8 + m * 18, 18 + l * 18));
            }
        }

        for(l = 0; l < 3; ++l) {
            for(m = 0; m < 9; ++m) {
                this.addSlot(new Slot(playerInventory, m + l * 9 + 9, 8 + m * 18, 103 + l * 18 + k));
            }
        }

        for(l = 0; l < 9; ++l) {
            this.addSlot(new Slot(playerInventory, l, 8 + l * 18, 161 + k));
        }

    }

    public boolean canUse(PlayerEntity playerEntity) {
        return this.inventory.canPlayerUse(playerEntity);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);

        if (slot != null && slot.hasStack()) {
            ItemStack itemStackInSlot = slot.getStack();
            itemStackCopy = itemStackInSlot.copy();

            // If the slot is in our custom inventory
            if (index < 27) {
                // Try to move the item stack to the player's inventory
                if (!this.insertItem(itemStackInSlot, 27, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // The slot is in the player's inventory, try to move the item stack to our custom inventory
                if (!this.insertItem(itemStackInSlot, 0, 27, false)) {
                    return ItemStack.EMPTY;
                }
            }

            // If the item stack is empty, clear the slot
            if (itemStackInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }

            // If nothing changed, return an empty item stack
            if (itemStackInSlot.getCount() == itemStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTakeItem(player, itemStackInSlot);
        }

        return itemStackCopy;
    }

    @Override
    public void onContentChanged(Inventory inventory) {
        super.onContentChanged(inventory);
        if (this.player instanceof ServerPlayerEntity) {
            for (int slotId = 0; slotId < inventory.size(); slotId++) {
                Slot slot = this.slots.get(slotId);
                ((ServerPlayerEntity) this.player).networkHandler.sendPacket(new ScreenHandlerSlotUpdateS2CPacket(this.syncId, this.nextRevision(), slotId, slot.getStack()));
            }
        }
    }


    public void close(PlayerEntity player) {
        super.close(player);
        if (!player.world.isClient) {
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack itemStack = inventory.removeStack(i);
                if (!itemStack.isEmpty()) {
                    player.dropItem(itemStack, false);
                }
            }
            this.dropInventory(player, this.inventory);
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getRows() {
        return this.rows;
    }
}

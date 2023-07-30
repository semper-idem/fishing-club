package net.semperidem.fishingclub.client.screen.fishing_net;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.registry.FScreenHandlerRegistry;
import net.semperidem.fishingclub.util.InventoryUtil;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;

public class FishingNetScreenHandler extends ScreenHandler {
    int slotCount;
    int rows;

    SimpleInventory fishingNetInventory;
    PlayerInventory playerInventory;
    ItemStack fishingNetStack;

    public FishingNetScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, buf.readItemStack());
    }

    public FishingNetScreenHandler(int syncId,PlayerInventory playerInventory, ItemStack itemStack) {
        super(FScreenHandlerRegistry.FISHING_NET_SCREEN_HANDLER, syncId);
        this.rows = itemStack.isOf(FItemRegistry.FISHING_NET) ? 3 : 6;
        this.slotCount = SLOTS_PER_ROW * rows;
        this.playerInventory = playerInventory;
        this.fishingNetStack = itemStack;
        this.fishingNetInventory = InventoryUtil.readFishingNetInventory(itemStack);

        addFishingNetInventory();
        addPlayerInventory();
        addPlayerHotbar();
    }


    public int getRows(){
        return this.rows;
    }

    private void addFishingNetInventory(){
        for (int j = 0; j < this.rows; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(fishingNetInventory, k + j * 9, 8 + k * 18, 18 + j * 18){
                    @Override
                    public boolean canInsert(ItemStack stack) {
                        return stack.isOf(Items.TROPICAL_FISH);
                    }
                });
            }
        }
    }

    private void addPlayerInventory(){
        int i = (this.rows - 4) * 18;
        for (int j = 0; j < 3; ++j) {
            for (int k = 0; k < 9; ++k) {
                this.addSlot(new Slot(playerInventory, k + j * 9 + 9, 8 + k * 18, 103 + j * 18 + i));
            }
        }
    }

    private void addPlayerHotbar(){
        int i = (rows - 4) * 18;
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * 18, 161 + i));
        }
    }


    @Override
    protected boolean insertItem(ItemStack stack, int startIndex, int endIndex, boolean fromLast) {
        return super.insertItem(stack, startIndex, endIndex, fromLast);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStackInSlot = slot.getStack();
            itemStackCopy = itemStackInSlot.copy();
            if (index < slotCount) {
                if (!this.insertItem(itemStackInSlot, slotCount, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStackInSlot, 0, slotCount, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (itemStackInSlot.isEmpty()) {
                slot.setStack(ItemStack.EMPTY);
            } else {
                slot.markDirty();
            }
            if (itemStackInSlot.getCount() == itemStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTakeItem(player, itemStackInSlot);
        }
        return itemStackCopy;
    }


    @Override
    public boolean canUse(PlayerEntity player) {
        return true;
    }
}

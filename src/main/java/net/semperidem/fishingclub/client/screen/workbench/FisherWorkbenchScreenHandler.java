package net.semperidem.fishingclub.client.screen.workbench;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.shop.FishSlot;
import net.semperidem.fishingclub.item.FishingRodPartItem;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class FisherWorkbenchScreenHandler extends ScreenHandler {
    private final PlayerEntity player;
    private final Inventory fishingRodSlots;
    public FisherWorkbenchScreenHandler(int syncId, PlayerInventory inventory) {
        super(FishingClub.FISHER_WORKBENCH_SCREEN_HANDLER, syncId);
        this.player = inventory.player;
        fishingRodSlots = new SimpleInventory(6);
        addRodPartSlots();
        addPlayerInventory(player.getInventory());
        addPlayerHotbar(player.getInventory());
    }

    private void addRodPartSlots(){
            addSlot(new Slot(fishingRodSlots, 0, 16, 17){
            @Override
            public boolean canInsert(ItemStack stack) {
                return stack.isOf(FishingClub.CUSTOM_FISHING_ROD);//TODO Add vanilla fishing rod transformmation
            }
        });

        addSlot(new RodPartSlot(fishingRodSlots, 1, 145, 17,  FishingRodPartItem.PartType.CORE));
        addSlot(new RodPartSlot(fishingRodSlots, 2, 145, 17 + 26,  FishingRodPartItem.PartType.BOBBER));
        addSlot(new RodPartSlot(fishingRodSlots, 3, 145, 17 + 26 * 2,  FishingRodPartItem.PartType.LINE));
        addSlot(new RodPartSlot(fishingRodSlots, 4, 145, 17 + 26 * 3,  FishingRodPartItem.PartType.HOOK));
        addSlot(new RodPartSlot(fishingRodSlots, 5, 54, 17 + 26 * 3,  FishingRodPartItem.PartType.BAIT));
    }

    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addSlot(new Slot(playerInventory, x + y * SLOTS_PER_ROW + 9, 8 + x * SLOT_SIZE, 140 + y * SLOT_SIZE));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addSlot(new Slot(playerInventory, x, 8 + x * SLOT_SIZE, 198));
        }
    }

    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStackInSlot = slot.getStack();
            itemStackCopy = itemStackInSlot.copy();
            if (index < 6) {
                if (!this.insertItem(itemStackInSlot, 6, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStackInSlot, 0, 6, false)) {
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

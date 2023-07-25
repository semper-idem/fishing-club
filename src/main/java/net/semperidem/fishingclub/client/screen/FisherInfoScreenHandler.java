package net.semperidem.fishingclub.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.fisher.FisherInfo;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class FisherInfoScreenHandler extends ScreenHandler {

    final static int SLOT_COUNT = 4;

    private final SimpleInventory fisherInventory;
    private final PlayerInventory playerInventory;
    FisherInfo clientInfo;

    public FisherInfoScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ShopScreenUtil.FISHER_INFO_SCREEN, syncId);
        this.playerInventory = playerInventory;
        this.fisherInventory = new SimpleInventory(SLOT_COUNT);
        addSlots();
    }


    public void addSlots(){
        addFisherInventory();
        addPlayerInventory();
        addPlayerHotbar();
    }

    public void removeSlots(){
        slots.forEach(slots::remove);
    }

    private void addFisherInventory(){
        addSlot(new Slot(fisherInventory, 0, 0, 0));
    }


    public Inventory getFisherInventory() {
        return fisherInventory;
    }

    private void addPlayerInventory(){
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addSlot(new Slot(playerInventory, x + y * SLOTS_PER_ROW + 9, 8 + x * SLOT_SIZE, 139 + y * SLOT_SIZE));
            }
        }
    }

    private void addPlayerHotbar(){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addSlot(new Slot(playerInventory, x, 8 + x * SLOT_SIZE, 197));
        }
    }

    public boolean canUse(PlayerEntity playerEntity) {
        return this.fisherInventory.canPlayerUse(playerEntity);
    }


    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasStack()) {
            ItemStack itemStackInSlot = slot.getStack();
            itemStackCopy = itemStackInSlot.copy();
            if (index < SLOT_COUNT) {
                if (!this.insertItem(itemStackInSlot, SLOT_COUNT, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!this.insertItem(itemStackInSlot, 0, SLOT_COUNT, false)) {
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


}

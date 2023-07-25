package net.semperidem.fishingclub.client.screen;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.client.screen.workbench.FishingRodSlot;
import net.semperidem.fishingclub.fisher.FisherInfos;

import java.util.ArrayList;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class FisherInfoScreenHandler extends ScreenHandler {

    final static int SLOT_COUNT = 4;

    private final SimpleInventory fisherInventory;
    private final PlayerInventory playerInventory;

    private final ArrayList<Slot> playerInventorySlots = new ArrayList<>();


    public FisherInfoScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ShopScreenUtil.FISHER_INFO_SCREEN, syncId);
        this.playerInventory = playerInventory;
        this.fisherInventory = FisherInfos.getClientInfo().getFisherInventory();
        addFisherInventory();
        addPlayerInventorySlots();

    }


    public void addPlayerInventorySlots(){
        addPlayerInventory();
        addPlayerHotbar();
    }

    public void removePlayerInventorySlots(){
        playerInventorySlots.forEach(slots::remove);
        playerInventorySlots.clear();
    }

    private void addFisherInventory(){
        addSlot(new FishingRodSlot(fisherInventory, 0, 105, 235));
        addSlot(new BoatSlot(fisherInventory, 1, 135, 235));
        addSlot(new FishingNetSlot(fisherInventory, 2, 105, 265));
        addSlot(new FishingNetSlot(fisherInventory, 3, 135, 265)); //TODO MOVE AFTER RESCALE
    }

    public Inventory getFisherInventory() {
        return fisherInventory;
    }

    private void addPlayerInventory(){
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addSlot(new Slot(playerInventory, x + y * SLOTS_PER_ROW + 9, 237 + x * SLOT_SIZE, 202 + y * SLOT_SIZE));
            }
        }
    }

    private void addPlayerHotbar(){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addSlot(new Slot(playerInventory, x, 237 + x * SLOT_SIZE, 262));
        }
    }

    public boolean canUse(PlayerEntity playerEntity) {
        return this.fisherInventory.canPlayerUse(playerEntity);
    }


    @Override
    protected Slot addSlot(Slot slot) {
        if (slot instanceof FishingRodSlot) return super.addSlot(slot);
        if (slot instanceof FishingNetSlot) return super.addSlot(slot);
        if (slot instanceof BoatSlot) return super.addSlot(slot);
        playerInventorySlots.add(slot);
        return super.addSlot(slot);
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


    class FishingNetSlot extends Slot {
        public FishingNetSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.isOf(FishingClub.FISHING_NET);
        }
    }

    class BoatSlot extends Slot {

        public BoatSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.isOf(Items.ACACIA_BOAT);
        }
    }
}

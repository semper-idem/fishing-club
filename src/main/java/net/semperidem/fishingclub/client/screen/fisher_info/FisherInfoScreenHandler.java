package net.semperidem.fishingclub.client.screen.fisher_info;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.util.InventoryUtil;

import java.util.ArrayList;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class FisherInfoScreenHandler extends ScreenHandler {
    final static int SLOT_COUNT = 4;

    private final SimpleInventory fisherInventory;
    private final PlayerInventory playerInventory;
    private final ArrayList<Slot> playerInventorySlots = new ArrayList<>();
    private NbtCompound lastSavedNbt;

    FisherInfo fisherInfo;

    public FisherInfoScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new FisherInfo(playerInventory.player, buf.readNbt()));
    }

    public FisherInfoScreenHandler(int syncId, PlayerInventory playerInventory, FisherInfo fisherInfo) {
        super(FishingClub.FISHER_INFO_SCREEN, syncId);
        enableSyncing();
        this.fisherInfo = fisherInfo;
        this.playerInventory = playerInventory;
        this.fisherInventory = fisherInfo.getFisherInventory();
        this.lastSavedNbt = playerInventory.player.writeNbt(new NbtCompound());
        addFisherInventory();
        addPlayerInventorySlots();
        this.fisherInventory.addListener(onInventoryChange());
    }

    private InventoryChangedListener onInventoryChange(){
        return sender -> {
            NbtCompound playerNbt = new NbtCompound();
            playerInventory.player.writeNbt(playerNbt);
            NbtCompound fisherInventoryTag = InventoryUtil.writeInventory((SimpleInventory) sender);
            if (!fisherInventoryTag.equals(lastSavedNbt)) {
                playerNbt.getCompound("fisher_info").put("inventory", fisherInventoryTag);
                playerInventory.player.readNbt(playerNbt);
                lastSavedNbt = fisherInventoryTag;
            }
        };
    }

    private void addFisherInventory(){
        addSlot(new FishingRodSlot(fisherInventory, 0, 25, 199));
        addSlot(new BoatSlot(fisherInventory, 1, 55, 199));
        addSlot(new FishingNetSlot(fisherInventory, 2, 25, 229));
        addSlot(new FishingNetSlot(fisherInventory, 3, 55, 229));
    }

    public void addPlayerInventorySlots(){
        addPlayerInventory();
        addPlayerHotbar();
    }

    public void removePlayerInventorySlots(){
        playerInventorySlots.forEach(slots::remove);
        playerInventorySlots.clear();
    }
    
    private void addPlayerInventorySlot(int index, int x, int y){
        Slot slot = new Slot(playerInventory, index, x, y);
        playerInventorySlots.add(slot);
        addSlot(slot);
    }

    private void addPlayerInventory(){
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addPlayerInventorySlot(x + y * SLOTS_PER_ROW + 9, 157 + x * SLOT_SIZE, 166 + y * SLOT_SIZE);
            }
        }
    }

    private void addPlayerHotbar(){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addPlayerInventorySlot(x, 157 + x * SLOT_SIZE, 226);
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


    private static class FishingNetSlot extends Slot {
        public FishingNetSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.isOf(FishingClub.FISHING_NET);
        }
    }
    private static class FishingRodSlot extends Slot {
        public FishingRodSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return stack.isOf(FishingClub.CUSTOM_FISHING_ROD) || stack.isOf(Items.FISHING_ROD);
        }
    }

    private static class BoatSlot extends Slot {
        public BoatSlot(Inventory inventory, int index, int x, int y) {
            super(inventory, index, x, y);
        }

        @Override
        public boolean canInsert(ItemStack stack) {
            return FishingClub.BOATS.contains(stack.getItem());
        }
    }
}

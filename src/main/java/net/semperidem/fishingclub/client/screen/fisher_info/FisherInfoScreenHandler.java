package net.semperidem.fishingclub.client.screen.fisher_info;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.InventoryChangedListener;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.BoatItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.FishingNetItem;
import net.semperidem.fishingclub.registry.FScreenHandlerRegistry;
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
    FishingPerk rootPerk;

    public FisherInfoScreenHandler(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory);
    }

    public FisherInfoScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(FScreenHandlerRegistry.FISHER_INFO_SCREEN, syncId);
        enableSyncing();
        this.fisherInfo = FishingClubClient.CLIENT_INFO;
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
                playerNbt.getCompound(FisherInfo.TAG).put("inventory", fisherInventoryTag);
                playerInventory.player.readNbt(playerNbt);
                lastSavedNbt = fisherInventoryTag;
            }
        };
    }

    private void addFisherInventory(){
        addSlot(new FisherSlot(fisherInventory, 0, 25, 199, FishingPerks.FISHING_ROD_SLOT){
            @Override
            public boolean canInsert(ItemStack stack) {
                return fisherInfo.hasPerk(FishingPerks.FISHING_ROD_SLOT) && stack.getItem() instanceof FishingRodItem;
            }

        });
        addSlot(new FisherSlot(fisherInventory, 1, 55, 199, FishingPerks.BOAT_SLOT){
            @Override
            public boolean canInsert(ItemStack stack) {
                return fisherInfo.hasPerk(FishingPerks.BOAT_SLOT) && stack.getItem() instanceof BoatItem;
            }

        });
        addSlot(new FisherSlot(fisherInventory, 2, 25, 229, FishingPerks.NET_SLOT_UNLOCK){
            @Override
            public boolean canInsert(ItemStack stack) {
                return fisherInfo.hasPerk(FishingPerks.NET_SLOT_UNLOCK) && stack.getItem() instanceof FishingNetItem;
            }
        });
        addSlot(new FisherSlot(fisherInventory, 3, 55, 229, FishingPerks.NET_SLOT_UNLOCK){
            @Override
            public boolean canInsert(ItemStack stack) {
                return fisherInfo.hasPerk(FishingPerks.NET_SLOT_UNLOCK) && stack.getItem() instanceof FishingNetItem;
            }
        });
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

    class FisherSlot extends Slot{
        FishingPerk requiredPerk;
        public FisherSlot(Inventory inventory, int index, int x, int y, FishingPerk requiredPerk) {
            super(inventory, index, x, y);
            this.requiredPerk = requiredPerk;
        }

        @Override
        public boolean isEnabled() {
            return rootPerk == null && fisherInfo.hasPerk(requiredPerk);
        }

    }
}

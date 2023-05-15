package net.semperidem.fishingclub.client.screen.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.fisher.FisherInfo;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;
import net.semperidem.fishingclub.network.ClientPacketSender;

public class ShopSellScreenHandler extends ScreenHandler {
    private final Inventory inventory;
    private final int rows = 6;
    private final int slotsPerRow = 9;
    private final int slotCount =  54;
    private final PlayerEntity player;
    private final int slotSize = 18;
    private FisherInfo clientInfo;
    int lastSellValue = 0;


    public ShopSellScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(FishingClub.SHOP_SELL_SCREEN_HANDLER, syncId);
        this.inventory = new SimpleInventory(slotCount);
        this.player = playerInventory.player;
        this.clientInfo = FisherInfos.getClientInfo();


        addSellInventory();
        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);


    }

    private void addSellInventory(){
        for(int x = 0; x < this.rows; ++x) {
            for(int y = 0; y < slotsPerRow; ++y) {
                this.addSlot(new Slot(inventory, y + x * 9, 8 + y * slotSize, slotSize + x * slotSize));
            }
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * slotSize, 139 + y * slotSize));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory){
        for(int x = 0; x < 9; ++x) {
            this.addSlot(new Slot(playerInventory, x, 8 + x * slotSize, 197));
        }
    }

    public boolean canUse(PlayerEntity playerEntity) {
        return this.inventory.canPlayerUse(playerEntity);
    }

    public int getPlayerBalance(){
        return FisherInfos.getClientInfo().getFisherCredit();
    }

    public int getSellContainerValue(){
        int result = 0;
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack fishStack = inventory.getStack(i);
            if (!fishStack.isOf(Items.AIR)) {
                result += FishUtil.getFishValue(inventory.getStack(i));
            }
        }

        return result;
    }

    public void containerSold(){
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack itemStack = inventory.removeStack(i);
            if (!itemStack.isEmpty()) {
                //inventory.removeStack(i);
            }
        }
    }

    boolean sellContainer(){
        lastSellValue =  getSellContainerValue();
        if (lastSellValue == 0) {
            return false;
        }
        ClientPacketSender.sellShopContainer(lastSellValue);
        return true;
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


    public void close(PlayerEntity player) {
        super.close(player);
        if (!player.world.isClient) {
            for (int i = 0; i < inventory.size(); i++) {
                ItemStack itemStack = inventory.removeStack(i);
                if (!itemStack.isEmpty()) {
                    player.dropItem(itemStack, false);
                }
            }
        }
    }

    public Inventory getInventory() {
        return this.inventory;
    }

    public int getRows() {
        return this.rows;
    }
}

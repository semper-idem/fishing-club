package net.semperidem.fishingclub.client.screen.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.network.ServerPacketSender;

import java.util.ArrayList;

import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOTS_PER_ROW;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class ShopScreenHandler extends ScreenHandler {
    final static int ROW_COUNT = 6;
    final static int SLOT_COUNT =  ROW_COUNT *  SLOTS_PER_ROW;

    private PlayerEntity player;
    private final Inventory sellContainer;
    int lastBalanceChange = 0;


    public ShopScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(ShopScreenUtil.SHOP_SCREEN, syncId);
        this.player = playerInventory.player;
        this.sellContainer = new SimpleInventory(SLOT_COUNT);
        addSellInventory();
        addPlayerInventory(player.getInventory());
        addPlayerHotbar(player.getInventory());
    }

    private void addSellInventory(){
        for(int x = 0; x < ROW_COUNT; ++x) {
            for(int y = 0; y < SLOTS_PER_ROW; ++y) {
                addSlot(new FishSlot(sellContainer, y + x * SLOTS_PER_ROW, 8 + y * SLOT_SIZE, SLOT_SIZE + x * SLOT_SIZE));
            }
        }
    }

    private void addPlayerInventory(PlayerInventory playerInventory){
        for(int y = 0; y < 3; ++y) {
            for(int x = 0; x < 9; ++x) {
                addSlot(new Slot(playerInventory, x + y * SLOTS_PER_ROW + 9, 8 + x * SLOT_SIZE, 139 + y * SLOT_SIZE));
            }
        }
    }

    private void addPlayerHotbar(PlayerInventory playerInventory){
        for(int x = 0; x < SLOTS_PER_ROW; ++x) {
            addSlot(new Slot(playerInventory, x, 8 + x * SLOT_SIZE, 197));
        }
    }

    public boolean canUse(PlayerEntity playerEntity) {
        return this.sellContainer.canPlayerUse(playerEntity);
    }

    public int getSellContainerValue(){
        int result = 0;
        for (int i = 0; i < sellContainer.size(); i++) {
            ItemStack fishStack = sellContainer.getStack(i);
            if (fishStack.isOf(FishUtil.FISH_ITEM)) {
                result += FishUtil.getFishValue(sellContainer.getStack(i));
            }
        }
        return result;
    }


    //Client
    public boolean sellContainer(){
        lastBalanceChange =  getSellContainerValue();
        if (lastBalanceChange != 0) {
            ClientPacketSender.sellShopContainer(lastBalanceChange);
            return true;
        }
        return false;
    }

    //Server
    public void soldContainer(){
        for (int i = 0; i < sellContainer.size(); i++) {
            ItemStack removedStack = sellContainer.removeStack(i);
            if (!removedStack.isOf(FishUtil.FISH_ITEM)) {
                returnItemStack(removedStack);
            }
        }
    }
//    //Client
//    public boolean buyContaier(OrderListWidget orderListWidget){//change to itemstack basket
//        ArrayList<OrderEntryData> basket = orderListWidget.getBasektData();
//        int cost = orderListWidget.getBasketTotal();
//        int currentCredit =  FisherInfos.getClientInfo().getFisherCredit();
//        lastBalanceChange = cost;
//        if (cost <= currentCredit) {
//            ClientPacketSender.buyShopContainer(cost, basket);
//            return true;
//        }
//        return false;
//    }

    //Server
    public void boughtContainer(ServerPlayerEntity player, ArrayList<ItemStack> basket, int cost){
        if (!FisherInfos.removeCredit(player.getUuid(), cost)) return;
        for(ItemStack itemStack : basket) {
            player.getInventory().insertStack(itemStack.copy());
        }
        ServerPacketSender.sendShopScreenInventorySyncPacket(player);
        ServerPacketSender.sendFisherInfoSyncPacket(player);
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


    public void close(PlayerEntity player) {
        super.close(player);
        if (!player.world.isClient) {
            for (int i = 0; i < sellContainer.size(); i++) {
                returnItemStack(sellContainer.removeStack(i));
            }
        }
    }

    private void returnItemStack(ItemStack itemStack){
        if (itemStack.isEmpty()) {
            return;
        }
        if(player.getInventory().getEmptySlot() != -1) {
            player.dropItem(itemStack, false);
        } else {
            player.giveItemStack(itemStack);
        }
    }
}

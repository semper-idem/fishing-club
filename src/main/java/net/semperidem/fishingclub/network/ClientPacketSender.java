package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishingclub.client.screen.shop.widget.OrderEntryData;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.FishUtil;

import java.util.ArrayList;

public class ClientPacketSender {
    public static void sendFisherInfoGrantExp(Fish fish) {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_GRANT_REWARD, FishUtil.fishToPacketBuf(fish));
    }
    public static void sendFishingInfoDataRequest() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_REQUEST_DATA_SYNC_ID, PacketByteBufs.empty());
    }

    public static void sendOpenSellShopRequest() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_OPEN_SELL_SHOP, PacketByteBufs.empty());
    }
    public static void sellShopContainer(int containerValue) {
        if (containerValue <= 0 ) return;
        PacketByteBuf  buf = PacketByteBufs.create();
        buf.writeInt(containerValue);
        ClientPlayNetworking.send(PacketIdentifiers.C2S_SELL_FISH, buf);
    }


    public static void buyShopContainer(int containerValue, ArrayList<OrderEntryData> basket) {
        if (containerValue <= 0 ) return;
        PacketByteBuf  buf = PacketByteBufs.create();
        buf.writeInt(containerValue);
        buf.writeInt(basket.size());
        for (OrderEntryData itemInBasket : basket) {
            int itemCount = itemInBasket.getCount();
            ItemStack itemStack = new ItemStack(itemInBasket.getItem());
            itemStack.setCount(itemCount);
            buf.writeItemStack(itemStack);
        }
        ClientPlayNetworking.send(PacketIdentifiers.C2S_BUY_BASKET, buf);
    }



}

package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.FishUtil;

public class ClientPacketSender {
    public static void sendFisherInfoGrantExp(Fish fish) {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_GRANT_REWARD, FishUtil.getFishPacketBuf(fish));
    }
    public static void sendFishingInfoDataRequest() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_REQUEST_DATA_SYNC_ID, PacketByteBufs.empty());
    }

    public static void sendOpenShopRequest() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_OPEN_SHOP, PacketByteBufs.empty());
    }

}

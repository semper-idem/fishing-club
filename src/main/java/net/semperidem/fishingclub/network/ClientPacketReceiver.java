package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.semperidem.fishingclub.network.payload.CoinFlipResultPayload;
import net.semperidem.fishingclub.network.payload.HookPayload;

public class ClientPacketReceiver {
    public static void registerClientPacketHandlers() {
        ClientPlayNetworking.registerGlobalReceiver(HookPayload.ID, HookPayload::consumePayload);
        ClientPlayNetworking.registerGlobalReceiver(CoinFlipResultPayload.ID, CoinFlipResultPayload::consumePayload);
    }
}

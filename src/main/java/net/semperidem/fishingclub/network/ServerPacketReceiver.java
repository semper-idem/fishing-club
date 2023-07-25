package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import static net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerReceiver;
import static net.semperidem.fishingclub.network.PacketIdentifiers.*;

public class ServerPacketReceiver {
    public static void registerServerPacketHandlers() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            registerReceiver(handler, C2S_F_GAME_WON, ServerPacketHandlers::handleFishingGameFished);
            registerReceiver(handler, C2S_F_SHOP_OPEN, ServerPacketHandlers::handleFishingShopOpenRequest);
            registerReceiver(handler, C2S_F_INFO_OPEN, ServerPacketHandlers::handleFishingInfoOpenRequest);
            registerReceiver(handler, C2S_F_SHOP_SELL, ServerPacketHandlers::handleFishingShopSellContainer);
            registerReceiver(handler, C2S_F_SHOP_BUY, ServerPacketHandlers::handleFishingShopBuyBasket);
            registerReceiver(handler, C2S_F_DATA_PERK_ADD, ServerPacketHandlers::handlePerkAdd);
        });
    }
}

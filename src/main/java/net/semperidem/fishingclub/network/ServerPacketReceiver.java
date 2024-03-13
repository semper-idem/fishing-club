package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import static net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerReceiver;
import static net.semperidem.fishingclub.network.PacketIdentifiers.*;

public class ServerPacketReceiver {
    public static void registerServerPacketHandlers() {
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            registerReceiver(handler, C2S_F_GAME_WON, ServerPacketHandlers::handleFishingGameFished);
            registerReceiver(handler, C2S_F_GAME_LOST, ServerPacketHandlers::handleFishingGameLost);
            registerReceiver(handler, C2S_OPEN_MEMBER_SCREEN, ServerPacketHandlers::handleMemberScreenOpenRequest);
            registerReceiver(handler, C2S_F_INFO_OPEN, ServerPacketHandlers::handleFishingInfoOpenRequest);
            registerReceiver(handler, C2S_F_SHOP_SELL, ServerPacketHandlers::handleFishingShopSellContainer);
            registerReceiver(handler, C2S_F_SHOP_BUY, ServerPacketHandlers::handleFishingShopBuyBasket);
            registerReceiver(handler, C2S_F_DATA_PERK_ADD, ServerPacketHandlers::handlePerkAdd);
            registerReceiver(handler, C2S_CAST_SPELL, ServerPacketHandlers::handleSpellCast);
            registerReceiver(handler, C2S_F_SLOT_SELL, ServerPacketHandlers::handleFishingCardInstantSell);
            registerReceiver(handler, C2S_SUMMON_ACCEPT, ServerPacketHandlers::handleSummonAccept);
            registerReceiver(handler, C2S_REPAIR_ROD, ServerPacketHandlers::handleRepairRod);
            registerReceiver(handler, C2S_BOBBER_MOVEMENT, ServerPacketHandlers::handleBobberMovement);
            registerReceiver(handler, C2S_ACCEPT_DEREK, ServerPacketHandlers::handleAcceptDerek);
            registerReceiver(handler, C2S_REFUSE_DEREK, ServerPacketHandlers::handleRefuseDerek);
            registerReceiver(handler, C2S_TOSS_COIN, ServerPacketHandlers::handleCoinToss);
            registerReceiver(handler, C2S_RESET_PERKS, ServerPacketHandlers::handleResetPerk);
        });
    }
}

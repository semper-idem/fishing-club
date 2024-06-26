package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.semperidem.fishingclub.network.payload.CoinFlipPayload;
import net.semperidem.fishingclub.network.payload.HookClientPayload;
import net.semperidem.fishingclub.network.payload.HookPayload;
import net.semperidem.fishingclub.network.payload.TitleClaimPayload;

import static net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.registerReceiver;
import static net.semperidem.fishingclub.network.PacketIdentifiers.*;

public class ServerPacketReceiver {

    private static void registerPayload() {
        PayloadTypeRegistry.playS2C().register(HookPayload.ID, HookPayload.CODEC);

        PayloadTypeRegistry.playC2S().register(HookClientPayload.ID, HookClientPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(CoinFlipPayload.ID, CoinFlipPayload.CODEC);
        PayloadTypeRegistry.playC2S().register(TitleClaimPayload.ID, TitleClaimPayload.CODEC);
    }

    public static void registerServerPacketHandlers() {
        registerPayload();
        ServerPlayNetworking.registerGlobalReceiver(HookClientPayload.ID, HookClientPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(CoinFlipPayload.ID, CoinFlipPayload::consumePayload);
        ServerPlayNetworking.registerGlobalReceiver(TitleClaimPayload.ID, TitleClaimPayload::consumePayload);
        ServerPlayConnectionEvents.INIT.register((handler, server) -> {
            registerReceiver(handler, C2S_OPEN_MEMBER_SCREEN, ServerPacketHandlers::handleMemberScreenOpenRequest);
            registerReceiver(handler, C2S_F_INFO_OPEN, ServerPacketHandlers::handleFishingInfoOpenRequest);
            registerReceiver(handler, C2S_F_SHOP_SELL, ServerPacketHandlers::handleFishingShopSellContainer);
            registerReceiver(handler, C2S_F_SHOP_BUY, ServerPacketHandlers::handleCheckout);
            registerReceiver(handler, C2S_F_DATA_PERK_ADD, ServerPacketHandlers::handlePerkAdd);
            registerReceiver(handler, C2S_CAST_SPELL, ServerPacketHandlers::handleSpellCast);
            registerReceiver(handler, C2S_F_SLOT_SELL, ServerPacketHandlers::handleFishingCardInstantSell);
            registerReceiver(handler, C2S_SUMMON_ACCEPT, ServerPacketHandlers::handleSummonAccept);
            registerReceiver(handler, C2S_REPAIR_ROD, ServerPacketHandlers::handleRepairRod);
            registerReceiver(handler, C2S_BOBBER_MOVEMENT, ServerPacketHandlers::handleBobberMovement);
            registerReceiver(handler, C2S_ACCEPT_DEREK, ServerPacketHandlers::handleAcceptDerek);
            registerReceiver(handler, C2S_REFUSE_DEREK, ServerPacketHandlers::handleRefuseDerek);
            registerReceiver(handler, C2S_RESET_PERKS, ServerPacketHandlers::handleResetPerk);
            registerReceiver(handler, C2S_GET_CAPE_DETAILS, ServerPacketHandlers::handleCapeDetailsRequest);
            registerReceiver(handler, C2S_OPEN_LEADERBOARD_SCREEN, ServerPacketHandlers::handleOpenLeaderboardScreen);
        });
    }
}

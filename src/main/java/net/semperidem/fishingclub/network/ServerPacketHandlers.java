package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;

import java.util.ArrayList;
import java.util.Optional;

public class ServerPacketHandlers {

    public static void handleFishingGameFished(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Fish fish = FishUtil.fishFromPacketBuf(buf);
        server.execute(() -> {
            FishUtil.grantReward(player, fish);
            ServerPacketSender.sendFisherInfoSyncPacket(player);
        });
    }

    public static void handleFishingDataRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> ServerPacketSender.sendFisherInfoSyncPacket(player));
    }

    public static void handleFishingShopOpenRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> ShopScreenUtil.openShopScreen(player));
    }

    public static void handleFishingShopSellContainer(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int creditGained = buf.readInt();
        server.execute(() -> Optional.ofNullable(player.currentScreenHandler)
                .filter(ShopScreenHandler.class::isInstance)
                .map(ShopScreenHandler.class::cast)
                .ifPresent(screenHandler -> screenHandler.soldContainer(player, creditGained)));
    }

    public static void handleFishingShopBuyBasket(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int cost = buf.readInt();
        int basketSize = buf.readInt();
        ArrayList<ItemStack> basket = new ArrayList<>();
        for(int i = 0; i < basketSize; i++) {
            basket.add(buf.readItemStack());
        }
        server.execute(() -> Optional.ofNullable(player.currentScreenHandler)
                    .filter(ShopScreenHandler.class::isInstance)
                    .map(ShopScreenHandler.class::cast)
                    .ifPresent(screenHandler -> screenHandler.boughtContainer(player, basket, cost)));
    }




}

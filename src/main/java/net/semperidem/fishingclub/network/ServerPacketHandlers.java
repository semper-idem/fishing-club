package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreenFactory;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreenHandler;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItem;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartType;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.screen.FishingCardScreenHandler;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class ServerPacketHandlers {

    public static void handleFishingGameFished(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        try {
            Fish fish = new Fish(buf.readNbt());//TO-DO MOVE FISHING GAME TO SERVER CAUSE THIS IS EZ CHEATING123
            int rewardCount = buf.readInt();
            ArrayList<ItemStack> rewards = new ArrayList<>();
            for(int i = 0; i < rewardCount; i++) {
                rewards.add(buf.readItemStack());
            }
            server.execute(() -> {
                FishUtil.fishCaught(player, fish);
                FishUtil.giveReward(player, rewards);
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void handleRepairRod(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            if (!(player.currentScreenHandler instanceof FisherWorkbenchScreenHandler screenHandler)) return;
            screenHandler.repairRod();
        });
    }

    public static void handleFishingGameLost(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            ItemStack fishingRod = player.getStackInHand(player.getActiveHand());
            if (!(fishingRod.getItem() instanceof FishingRodPartItem)) return;
            FItemRegistry.CUSTOM_FISHING_ROD.damageRodPart(fishingRod, FishingRodPartType.LINE);
        });
    }

    public static void handleFishingShopOpenRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> ShopScreenUtil.openShopScreen(player));
    }
    public static void handleFishingInfoOpenRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> player.openHandledScreen(new FishingCardScreenFactory(FishingCard.getPlayerCard(player))));
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


    public static void handlePerkAdd(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String perkName = buf.readString();
            server.execute(() -> FishingCard.getPlayerCard(player).addPerk(perkName));
    }

    public static void handleSpellCast(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String perkName = buf.readString();
        String uuidString = buf.readString();
            server.execute(() -> FishingCard.getPlayerCard(player).useSpell(perkName, server.getPlayerManager().getPlayer(UUID.fromString(uuidString))));
    }


    public static void handleFishingCardInstantSell(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> Optional.ofNullable(player.currentScreenHandler)
                .filter(FishingCardScreenHandler.class::isInstance)
                .map(FishingCardScreenHandler.class::cast)
                .ifPresent(FishingCardScreenHandler::instantSell)
        );
    }

    public static void handleSummonAccept(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> FishingCard.getPlayerCard(player).acceptSummonRequest());
    }
}

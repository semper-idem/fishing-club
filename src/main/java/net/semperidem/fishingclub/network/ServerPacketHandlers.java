package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.client.screen.fisher_info.FisherInfoScreen;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.fisher.FisherInfoManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.Spells;

import java.util.ArrayList;
import java.util.Optional;

public class ServerPacketHandlers {

    public static void handleFishingGameFished(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Fish fish = FishUtil.fishFromPacketBuf(buf);
        boolean boatFishing = buf.readBoolean();
        server.execute(() -> {
            FishUtil.grantReward(player, fish, boatFishing);
        });
    }

    public static void handleFishingShopOpenRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> ShopScreenUtil.openShopScreen(player));
    }
    public static void handleFishingInfoOpenRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> FisherInfoScreen.openScreen(player));
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
        FishingPerks.getPerkFromName(perkName).ifPresent( perk -> {
            server.execute(() -> {
                FisherInfoManager.addPerk(player, perkName);
            });
        });
    }

    public static void handleSpellCast(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String perkName = buf.readString();
        FishingPerks.getPerkFromName(perkName).ifPresent( perk -> {
            if (!Spells.perkHasSpell(perk)) return;
            server.execute(() -> {
                FisherInfoManager.getFisher(player).useSpell(perk);
            });
        });
    }

}

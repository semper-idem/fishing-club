package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreen;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreenHandler;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.spells.Spells;
import net.semperidem.fishingclub.item.FishingRodPartItem;
import net.semperidem.fishingclub.registry.FItemRegistry;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

public class ServerPacketHandlers {

    public static void handleFishingGameFished(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Fish fish = FishUtil.fishFromPacketBuf(buf);
        boolean boatFishing = buf.readBoolean();
        BlockPos caughtPos = buf.readBlockPos();
        server.execute(() -> {
            FishUtil.grantReward(player, fish, boatFishing, caughtPos);
        });
    }

    public static void handleRepairRod(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            if (player.currentScreenHandler instanceof FisherWorkbenchScreenHandler screenHandler) {
                //screenHandler.repairRod();
            }
        });
    }

    public static void handleFishingGameLost(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            ItemStack fishingRod = player.getStackInHand(player.getActiveHand());
            if (!(fishingRod.getItem() instanceof FishingRodPartItem)) return;
            FItemRegistry.CUSTOM_FISHING_ROD.damageRodPart(fishingRod, FishingRodPartItem.PartType.LINE);
        });
    }

    public static void handleFishingShopOpenRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> ShopScreenUtil.openShopScreen(player));
    }
    public static void handleFishingInfoOpenRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> FishingCardScreen.openScreen(player));
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
                FishingCardManager.addPerk(player, perkName);
            });
        });
    }

    public static void handleSpellCast(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String perkName = buf.readString();
        String uuidString = buf.readString();
        FishingPerks.getPerkFromName(perkName).ifPresent( perk -> {
            if (!Spells.perkHasSpell(perk)) return;
            server.execute(() -> {
                FishingCardManager.getPlayerCard(player).useSpell(perk, server.getPlayerManager().getPlayer(UUID.fromString(uuidString)));
            });
        });
    }


    public static void handleSlotSold(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int creditGained = buf.readInt();
        server.execute(() -> {
                    Optional.ofNullable(player.currentScreenHandler)
                            .filter(FishingCardScreenHandler.class::isInstance)
                            .map(FishingCardScreenHandler.class::cast)
                            .ifPresent(screenHandler -> screenHandler.soldSlot(player, creditGained));
                    ServerPacketSender.sendFisherInfo(player);
                }
        );
    }

    public static void handleSummonRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
                    ServerPacketSender.sendSummonRequest(player);
                }
        );
    }

    public static void handleSummonAccept(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            FishingCard fishingCard = FishingCardManager.getPlayerCard(player);
            if (!fishingCard.canAcceptTeleport(player.getWorld().getTime())) return;
            FishingCard.TeleportRequest teleportRequest = fishingCard.getLastTeleportRequest();
            if (teleportRequest == null) return;
            ServerPlayerEntity target = null;
            for(ServerPlayerEntity possibleTarget : player.getWorld().getPlayers()) {
                if  (possibleTarget.getUuidAsString().equalsIgnoreCase(teleportRequest.summonerUUID)) {
                    target = possibleTarget;
                    break;
                }
            }
            if (target == null) return;
            player.teleport(target.getWorld(), target.getX(), target.getY(), target.getZ(), target.getYaw(), target.getPitch());
        });
    }


}

package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingServerWorld;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreenFactory;
import net.semperidem.fishingclub.client.screen.leaderboard.LeaderboardScreenFactory;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreenHandler;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.level_reward.LevelUpEffect;
import net.semperidem.fishingclub.item.fishing_rod.components.ComponentItem;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;
import net.semperidem.fishingclub.screen.fishing_game.FishingGameScreenHandler;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;
import net.semperidem.fishingclub.screen.member.MemberScreenHandlerFactory;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

import static net.semperidem.fishingclub.registry.ItemRegistry.MEMBER_FISHING_ROD;

public class ServerPacketHandlers {

    public static void handleRepairRod(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            if (!(player.currentScreenHandler instanceof FisherWorkbenchScreenHandler screenHandler)) return;
            screenHandler.repairRod();
        });
    }
    public static void handleBobberMovement(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        float reelForce = buf.readFloat();
        boolean isReeling = buf.readBoolean();
        boolean isPulling = buf.readBoolean();
        server.execute(() -> {
            if (!(player.currentScreenHandler instanceof FishingGameScreenHandler screenHandler)) return;
            screenHandler.consumeBobberMovement(reelForce, isReeling, isPulling);
        });
    }

    public static void handleFishingGameLost(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            ItemStack fishingRod = player.getStackInHand(player.getActiveHand());
            MEMBER_FISHING_ROD.damageComponents(fishingRod, 4, ComponentItem.DamageSource.BITE, player);
            player.closeHandledScreen();
        });
    }

    public static void handleMemberScreenOpenRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> player.openHandledScreen(new MemberScreenHandlerFactory()));
    }
    public static void handleFishingInfoOpenRequest(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> player.openHandledScreen(new FishingCardScreenFactory(FishingCard.of(player))));
    }

    public static void handleFishingShopSellContainer(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int fishToSellCount = buf.readInt();
        ArrayList<ItemStack> fishToSell = new ArrayList<>();
        for(int i = 0; i < fishToSellCount; i++) {
            fishToSell.add(buf.readItemStack());
        }
        server.execute(() -> {
            int credit = 0;
            for(ItemStack fish : fishToSell) {
                for(ItemStack inventoryStack : player.getInventory().main) {
                    if (FishUtil.areEqual(fish, inventoryStack)) {
                        credit += FishUtil.getFishValue(fish);
                        inventoryStack.setCount(0);
                    }
                }

            }
            FishingCard fishingCard = FishingCard.of(player);
            fishingCard.addCredit(credit);
            ServerPacketSender.sendCardUpdate(player, fishingCard);
        });
    }

    public static void handleCheckout(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        int total = buf.readInt();
        int cartSize = buf.readInt();
        ArrayList<ItemStack> cart = new ArrayList<>();
        for(int i = 0; i < cartSize; i++) {
            cart.add(buf.readItemStack());
        }
        server.execute(() -> {
            FishingCard playerCard = FishingCard.of(player);
            if (playerCard.getCredit() < total) {
                return;
            }
            playerCard.addCredit(-total);
            for(ItemStack stack : cart) {
                player.giveItemStack(stack);
            }
            player.playSoundToPlayer(SoundEvents.ENTITY_VILLAGER_TRADE, SoundCategory.PLAYERS, 1f , 1f);
            ServerPacketSender.sendCardUpdate(player, playerCard);
        });
    }


    public static void handlePerkAdd(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String perkName = buf.readString();
            server.execute(() -> FishingCard.of(player).addPerk(perkName));
    }

    public static void handleSpellCast(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        String perkName = buf.readString();
        String uuidString = buf.readString();
            server.execute(() -> FishingCard.of(player).useSpell(perkName, server.getPlayerManager().getPlayer(UUID.fromString(uuidString))));
    }


    public static void handleFishingCardInstantSell(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> Optional.ofNullable(player.currentScreenHandler)
                .filter(FishingCardScreenHandler.class::isInstance)
                .map(FishingCardScreenHandler.class::cast)
                .ifPresent(FishingCardScreenHandler::instantSell)
        );
    }

    public static void handleSummonAccept(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> FishingCard.of(player).acceptSummonRequest());
    }

    public static void handleAcceptDerek(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            if (!(server.getOverworld() instanceof FishingServerWorld serverWorld)) {
                return;
            }
            FishermanEntity derek = serverWorld.getDerek();
            if (derek != null) {
                derek.acceptTrade();
            }
        });
    }

    public static void handleRefuseDerek(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            if (!(server.getOverworld() instanceof FishingServerWorld serverWorld)) {
                return;
            }
            FishermanEntity derek = serverWorld.getDerek();
            if (derek != null) {
                derek.refuseTrade();
            }
        });
    }

    public static void handleResetPerk(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> {
            FishingCard playerCard = FishingCard.of(player);
            playerCard.resetPerks();
            ServerPacketSender.sendCardUpdate(player, playerCard);
        });
    }

    public static void handleOpenLeaderboardScreen(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        server.execute(() -> player.openHandledScreen(new LeaderboardScreenFactory()));
    }

}

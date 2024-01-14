package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.game.fish.FishUtil;
import net.semperidem.fishingclub.game.fish.HookedFish;

import java.util.UUID;

import static net.semperidem.fishingclub.network.PacketIdentifiers.*;

public class ServerPacketSender {

    private static void sendPacket(ServerPlayerEntity player, Identifier identifier, PacketByteBuf buf){
        if (player != null && player.networkHandler != null) {
            ServerPlayNetworking.send( player, identifier, buf);
        }
    }

    public static void sendShopScreenInventorySyncPacket(ServerPlayerEntity player){
        player.networkHandler.sendPacket(
                new InventoryS2CPacket(
                        player.playerScreenHandler.syncId,
                        player.playerScreenHandler.getRevision(),
                        player.playerScreenHandler.getStacks(),
                        player.playerScreenHandler.getCursorStack()
                )
        );
    }

    public static void sendFishingStartPacket(ServerPlayerEntity player, ItemStack fishingRod, HookedFish fish){
        PacketByteBuf fishGameStartPacket =  FishUtil.fishToPacketBuf(fish);
        fishGameStartPacket.writeItemStack(fishingRod);
        sendPacket(player, S2C_F_GAME_START, fishGameStartPacket);
    }

    public static void sendFisherInfo(ServerPlayerEntity playerEntity, FishingCard fishingCard){
        PacketByteBuf packet = PacketByteBufs.create();
        packet.writeNbt(fishingCard.toNbt());
        sendPacket(playerEntity, S2C_F_DATA_SEND, packet);
    }
    public static void sendFisherInfo(ServerPlayerEntity playerEntity){
        PacketByteBuf packet = PacketByteBufs.create();
        NbtCompound playerCustomNbt = new NbtCompound();
        playerEntity.writeCustomDataToNbt(playerCustomNbt);
        if (!playerCustomNbt.contains(FishingCard.TAG)) return;
        packet.writeNbt(playerCustomNbt.getCompound(FishingCard.TAG));
        sendPacket(playerEntity, S2C_F_DATA_SEND, packet);
    }

    public static void sendSummonRequest(ServerPlayerEntity summoner){
        for(UUID linkedFisherUUID : FishingCardManager.getPlayerCard(summoner).getLinkedFishers()) {
            for(ServerPlayerEntity linkedFisherPlayer : summoner.getWorld().getPlayers()) {
                if (!linkedFisherPlayer.getUuid().equals(linkedFisherUUID)) continue;
                FishingCardManager.getPlayerCard(linkedFisherPlayer).setTeleportRequest(summoner.getUuid(), summoner.getWorld().getTime());
                PacketByteBuf packet = PacketByteBufs.create();
                packet.writeString(summoner.getDisplayName().getString());
                sendPacket(summoner, S2C_SUMMON_REQUEST, packet);
            }
        }
    }
}

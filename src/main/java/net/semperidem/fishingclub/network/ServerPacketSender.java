package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.InventoryS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FisherInfos;
import net.semperidem.fishingclub.item.FishingRodPartItem;

import java.util.HashMap;

import static net.semperidem.fishingclub.network.PacketIdentifiers.S2C_F_DATA_SYNC;
import static net.semperidem.fishingclub.network.PacketIdentifiers.S2C_F_GAME_START;

public class ServerPacketSender {

    private static void sendPacket(ServerPlayerEntity player, Identifier identifier, PacketByteBuf buf){
        if (player != null) {
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

    public static void sendFisherInfoSyncPacket(ServerPlayerEntity player) {
        sendFisherInfoSyncPacket(player, FisherInfos.getPlayerFisherInfoBuf(player.getUuid()));
    }

    public static void sendFisherInfoSyncPacket(ServerPlayerEntity player, PacketByteBuf buf) {
        sendPacket(player, S2C_F_DATA_SYNC, buf);
    }

    public static void sendFishingStartPacket(ServerPlayerEntity player, HashMap<FishingRodPartItem.PartType, FishingRodPartItem> customParts, Fish fish){
        PacketByteBuf fishGameStartPacket =  FishUtil.fishToPacketBuf(fish);
        if (customParts != null) {
            int partCount = customParts.size();
            fishGameStartPacket.writeInt(partCount);
            for(FishingRodPartItem part : customParts.values()) {
                fishGameStartPacket.writeString(part.getKey());
            }
        }
        sendPacket(player, S2C_F_GAME_START, fishGameStartPacket);
    }
}

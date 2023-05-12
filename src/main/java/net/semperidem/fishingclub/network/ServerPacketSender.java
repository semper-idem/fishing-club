package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;

public class ServerPacketSender {
    public static void sendFisherInfoSyncPacket(ServerPlayerEntity player) {
        ServerPlayNetworking.send(
                player,
                PacketIdentifiers.S2C_SYNC_DATA_ID,
                FisherInfos.getPlayerFisherInfoBuf(player.getUuid())
        );
    }


    public static void sendFishingStartPacket(ServerPlayerEntity player){
        ServerPlayNetworking.send(
                player,
                PacketIdentifiers.S2C_START_GAME,
                PacketByteBufs.create()
        );
    }
}

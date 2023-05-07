package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.FishingClubClient;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkill;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkillManager;

public class ServerPacketSender {
    public static void sendFishingSkillSyncPacket(ServerPlayerEntity player) {
        ServerPlayNetworking.send(
                player,
                FishingClub.S2C_SYNC_DATA_ID,
                getFishingSkillPacketBuf(FishingSkillManager.get(player.getUuid()))
        );
    }

    private static PacketByteBuf getFishingSkillPacketBuf(FishingSkill fishingSkill){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(fishingSkill.level);
        buf.writeInt(fishingSkill.exp);
        return buf;
    }
    public static void sendFishingStartPacket(ServerPlayerEntity player){
        ServerPlayNetworking.send(
                player,
                FishingClubClient.S2C_START_GAME,
                PacketByteBufs.create()
        );
    }
}

package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.FishingClubClient;

public class ClientPacketSender {
    public static void sendFishingSkillGrantExp(int expGained) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(expGained);
        ClientPlayNetworking.send(FishingClubClient.C2S_GRANT_EXP_ID, buf);
    }
    public static void sendFishingSkillDataRequest() {
        PacketByteBuf buf = PacketByteBufs.create();
        ClientPlayNetworking.send(FishingClubClient.C2S_REQUEST_DATA_SYNC_ID, buf);
    }

}

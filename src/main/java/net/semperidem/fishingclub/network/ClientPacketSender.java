package net.semperidem.fishingclub.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.semperidem.fishingclub.fish.Fish;

public class ClientPacketSender {
    public static void sendFishingSkillGrantExp(Fish fish) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeInt(fish.experience);
        buf.writeFloat(fish.weight);
        buf.writeFloat(fish.length);
        buf.writeString(fish.fishType.name);
        ClientPlayNetworking.send(PacketIdentifiers.C2S_GRANT_REWARD, buf);
    }
    public static void sendFishingSkillDataRequest() {
        ClientPlayNetworking.send(PacketIdentifiers.C2S_REQUEST_DATA_SYNC_ID, PacketByteBufs.create());
    }

}

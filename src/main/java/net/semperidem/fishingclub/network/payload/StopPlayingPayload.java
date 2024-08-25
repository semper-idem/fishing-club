package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.client.FishingClubClient;

import java.util.ArrayList;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record StopPlayingPayload(String soundId, double x, double y, double z) implements CustomPayload {
    public static final CustomPayload.Id<StopPlayingPayload> ID = new CustomPayload.Id<>(identifier("s2c_stop_playing"));
    public static final PacketCodec<RegistryByteBuf, StopPlayingPayload> CODEC = PacketCodec.tuple(
      PacketCodecs.STRING, StopPlayingPayload::soundId,
      PacketCodecs.DOUBLE, StopPlayingPayload::x,
      PacketCodecs.DOUBLE, StopPlayingPayload::y,
      PacketCodecs.DOUBLE, StopPlayingPayload::z,
      StopPlayingPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }


    public static void consumePayload(StopPlayingPayload payload, ClientPlayNetworking.Context context) {
        Vec3d pos = new Vec3d(payload.x, payload.y, payload.z);
        String soundKey = pos.toString();
        ArrayList<SoundInstance> soundsToStop = new ArrayList<>();
        SoundManager soundManager = MinecraftClient.getInstance().getSoundManager();
        for(SoundInstance soundInstance : FishingClubClient.getSoundsAtPosition(soundKey)) {
            if (!soundManager.isPlaying(soundInstance)) {
                continue;
            }
            soundsToStop.add(soundInstance);
        }
        for(SoundInstance soundInstance : soundsToStop) {
            soundManager.stop(soundInstance);
        }
    }

}

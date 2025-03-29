package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.screen.fishing.FishingScreenHandler;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record FishingInputMousePayload(float reelForce) implements CustomPayload {
    public static final CustomPayload.Id<FishingInputMousePayload> ID = new CustomPayload.Id<>(identifier("c2s_fishing_game_input_mouse"));
    public static final PacketCodec<RegistryByteBuf, FishingInputMousePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT,
            FishingInputMousePayload::reelForce,
            FishingInputMousePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(FishingInputMousePayload payload, ServerPlayNetworking.Context context) {
        if (!(context.player().currentScreenHandler instanceof FishingScreenHandler screenHandler)) {
            return;
        }
        screenHandler.consumeBobberMove(payload.reelForce);
    }
}

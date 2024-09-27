package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.screen.fishing_game.FishingGameScreenHandler;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record FishingGameInputMousePayload(float reelForce) implements CustomPayload {
    public static final CustomPayload.Id<FishingGameInputMousePayload> ID = new CustomPayload.Id<>(identifier("c2s_fishing_game_input_mouse"));
    public static final PacketCodec<RegistryByteBuf, FishingGameInputMousePayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT,
            FishingGameInputMousePayload::reelForce,
            FishingGameInputMousePayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(FishingGameInputMousePayload payload, ServerPlayNetworking.Context context) {
        if (!(context.player().currentScreenHandler instanceof FishingGameScreenHandler screenHandler)) {
            return;
        }
        screenHandler.consumeBobberMove(payload.reelForce);
    }
}

package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.screen.fishing_game.FishingGameScreenHandler;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record FishingGameInputKeyboardPayload(boolean reel) implements CustomPayload {
    public static final Id<FishingGameInputKeyboardPayload> ID = new Id<>(identifier("c2s_fishing_game_input_keyboard"));
    public static final PacketCodec<RegistryByteBuf, FishingGameInputKeyboardPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL,
            FishingGameInputKeyboardPayload::reel,
            FishingGameInputKeyboardPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(FishingGameInputKeyboardPayload payload, ServerPlayNetworking.Context context) {
        if (!(context.player().currentScreenHandler instanceof FishingGameScreenHandler screenHandler)) {
            return;
        }
        screenHandler.consumeReel(payload.reel);
    }
}

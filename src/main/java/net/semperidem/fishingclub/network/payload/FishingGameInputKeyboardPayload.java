package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.screen.fishing.FishingGameScreenHandler;
import net.semperidem.fishingclub.screen.fishing_post.FishingPostScreenHandler;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record FishingGameInputKeyboardPayload(boolean isPressed) implements CustomPayload {
    public static final Id<FishingGameInputKeyboardPayload> ID = new Id<>(identifier("c2s_fishing_game_input_keyboard"));
    public static final PacketCodec<RegistryByteBuf, FishingGameInputKeyboardPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL,
            FishingGameInputKeyboardPayload::isPressed,
            FishingGameInputKeyboardPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(FishingGameInputKeyboardPayload payload, ServerPlayNetworking.Context context) {
        if ((context.player().currentScreenHandler instanceof FishingGameScreenHandler screenHandler)) {
            handleGame(screenHandler, payload);
            return;
        }

        if ((context.player().currentScreenHandler instanceof FishingPostScreenHandler screenHandler)) {
           handlePost(screenHandler, payload);
        }
    }
    private static void handlePost(FishingPostScreenHandler screenHandler, FishingGameInputKeyboardPayload payload) {
        screenHandler.nextStage(!payload.isPressed);
    }

    private static void handleGame(FishingGameScreenHandler screenHandler, FishingGameInputKeyboardPayload payload) {
        screenHandler.consumeReel(payload.isPressed);
    }
}

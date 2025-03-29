package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.screen.fishing.FishingScreenHandler;
import net.semperidem.fishingclub.screen.fishing_post.FishingPostScreenHandler;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record FishingInputKeyboardPayload(boolean isPressed) implements CustomPayload {
    public static final Id<FishingInputKeyboardPayload> ID = new Id<>(identifier("c2s_fishing_input_keyboard"));
    public static final PacketCodec<RegistryByteBuf, FishingInputKeyboardPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.BOOL,
            FishingInputKeyboardPayload::isPressed,
            FishingInputKeyboardPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(FishingInputKeyboardPayload payload, ServerPlayNetworking.Context context) {
        if ((context.player().currentScreenHandler instanceof FishingScreenHandler screenHandler)) {
            handleGame(screenHandler, payload);
            return;
        }

        if ((context.player().currentScreenHandler instanceof FishingPostScreenHandler screenHandler)) {
           handlePost(screenHandler, payload);
        }
    }
    private static void handlePost(FishingPostScreenHandler screenHandler, FishingInputKeyboardPayload payload) {
        screenHandler.nextStage(!payload.isPressed);
    }

    private static void handleGame(FishingScreenHandler screenHandler, FishingInputKeyboardPayload payload) {
        screenHandler.consumeReel(payload.isPressed);
    }
}

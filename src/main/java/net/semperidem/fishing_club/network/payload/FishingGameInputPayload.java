package net.semperidem.fishing_club.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishing_club.screen.fishing_game.FishingGameScreenHandler;

import static net.semperidem.fishing_club.FishingClub.getIdentifier;

public record FishingGameInputPayload(float reelForce, boolean isReeling, boolean isPulling) implements CustomPayload {
    public static final CustomPayload.Id<FishingGameInputPayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_fishing_game_input"));
    public static final PacketCodec<RegistryByteBuf, FishingGameInputPayload> CODEC = PacketCodec.tuple(
            PacketCodecs.FLOAT,
            FishingGameInputPayload::reelForce,
            PacketCodecs.BOOL,
            FishingGameInputPayload::isReeling,
            PacketCodecs.BOOL,
            FishingGameInputPayload::isPulling,
            FishingGameInputPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(FishingGameInputPayload payload, ServerPlayNetworking.Context context) {
        if (!(context.player().currentScreenHandler instanceof FishingGameScreenHandler screenHandler)) {
            return;
        }
        screenHandler.consumeBobberMovement(payload.reelForce, payload.isReeling, payload.isPulling);
    }
}

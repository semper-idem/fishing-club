package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.semperidem.fishingclub.fisher.FishingKing;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

public record TitleClaimPayload(int amount) implements CustomPayload {
    public static final CustomPayload.Id<TitleClaimPayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_title_claim"));
    public static final PacketCodec<RegistryByteBuf, TitleClaimPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, TitleClaimPayload::amount, TitleClaimPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(TitleClaimPayload payload, ServerPlayNetworking.Context context) {
        try (MinecraftServer server  = context.server()) {
            server.execute(() -> FishingKing.of(server.getScoreboard()).claimCape(context.player(), payload.amount()));
        }
    }
}

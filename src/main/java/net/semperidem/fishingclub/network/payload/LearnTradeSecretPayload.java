package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fisher.FishingCard;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record LearnTradeSecretPayload(int tradeSecretId) implements CustomPayload {
    public static final CustomPayload.Id<LearnTradeSecretPayload> ID = new CustomPayload.Id<>(identifier("c2s_learn_trade_secret"));
    public static final PacketCodec<RegistryByteBuf, LearnTradeSecretPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, LearnTradeSecretPayload::tradeSecretId, LearnTradeSecretPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(LearnTradeSecretPayload payload, ServerPlayNetworking.Context context) {
        FishingCard.of(context.player()).learnTradeSecret(payload.tradeSecretId);
    }
}

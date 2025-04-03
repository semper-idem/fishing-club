package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.Card;

public record CoinFlipPayload(int amount) implements CustomPayload {
    public static final CustomPayload.Id<CoinFlipPayload> ID = new CustomPayload.Id<>(FishingClub.identifier("c2s_coin_flip"));
    public static final PacketCodec<RegistryByteBuf, CoinFlipPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, CoinFlipPayload::amount, CoinFlipPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(CoinFlipPayload payload, ServerPlayNetworking.Context context) {
        if (payload.amount <= 0) {
            return;
        }
        Card card = Card.of(context.player());
        if (payload.amount > card.getGS()) {
            return;
        }
        int resultAmount = payload.amount * Math.random() <= 0.49 ? 1 : -1;
        card.addGS(resultAmount);
        ServerPlayNetworking.send(context.player(), new CoinFlipResultPayload(resultAmount));
        //ServerPacketSender.sendCardUpdate(player, fishingCard);
    }
}

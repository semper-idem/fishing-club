package net.semperidem.fishing_club.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.fisher.FishingCard;

public record CoinFlipPayload(int amount) implements CustomPayload {
    public static final CustomPayload.Id<CoinFlipPayload> ID = new CustomPayload.Id<>(FishingClub.getIdentifier("c2s_coin_flip"));
    public static final PacketCodec<RegistryByteBuf, CoinFlipPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, CoinFlipPayload::amount, CoinFlipPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(CoinFlipPayload payload, ServerPlayNetworking.Context context) {
        if (payload.amount <= 0) {
            return;
        }
        FishingCard fishingCard = FishingCard.of(context.player());
        if (payload.amount > fishingCard.getCredit()) {
            return;
        }
        int resultAmount = payload.amount * Math.random() <= 0.49 ? 1 : -1;
        fishingCard.addCredit(resultAmount);
        ServerPlayNetworking.send(context.player(), new CoinFlipResultPayload(resultAmount));
        //ServerPacketSender.sendCardUpdate(player, fishingCard);
    }
}

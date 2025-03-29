package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.Card;

public record CardSellFishPayload() implements CustomPayload {
    public static final CustomPayload.Id<CardSellFishPayload> ID = new CustomPayload.Id<>(FishingClub.identifier("c2s_card_sell_fish"));
    public static final PacketCodec<RegistryByteBuf, CardSellFishPayload> CODEC = PacketCodec.unit(new CardSellFishPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(CardSellFishPayload payload, ServerPlayNetworking.Context context) {
        Card.of(context.player()).sell();
    }
}

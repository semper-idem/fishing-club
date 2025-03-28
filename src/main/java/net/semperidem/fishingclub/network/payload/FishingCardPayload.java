package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.client.screen.old_fishing_card.FishingCardScreenFactory;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record FishingCardPayload() implements CustomPayload {
    public static final CustomPayload.Id<FishingCardPayload> ID = new CustomPayload.Id<>(identifier("c2s_fishing_card"));
    public static final PacketCodec<RegistryByteBuf, FishingCardPayload> CODEC = PacketCodec.unit(new FishingCardPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(FishingCardPayload payload, ServerPlayNetworking.Context context) {
        context.player().openHandledScreen(new FishingCardScreenFactory());
    }
}

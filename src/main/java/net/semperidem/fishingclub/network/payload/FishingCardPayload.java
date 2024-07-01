package net.semperidem.fishingclub.network.payload;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreen;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreenFactory;

public record FishingCardPayload(int dummy) implements CustomPayload {
    public static final CustomPayload.Id<FishingCardPayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_fishing_card"));
    public static final PacketCodec<RegistryByteBuf, FishingCardPayload> CODEC =
            PacketCodec.tuple(PacketCodecs.INTEGER, FishingCardPayload::dummy, FishingCardPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(FishingCardPayload payload, ServerPlayNetworking.Context context) {
        System.out.println("payload.dummy");
        context.player().openHandledScreen(new FishingCardScreenFactory());

    }
}

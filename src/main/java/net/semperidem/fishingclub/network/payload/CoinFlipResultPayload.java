package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.member.MemberScreen;

public record CoinFlipResultPayload(int amount) implements CustomPayload {
    public static final CustomPayload.Id<CoinFlipResultPayload> ID = new CoinFlipResultPayload.Id<>(FishingClub.identifier("s2c_coin_flip"));
    public static final PacketCodec<RegistryByteBuf, CoinFlipResultPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, CoinFlipResultPayload::amount, CoinFlipResultPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(CoinFlipResultPayload payload, ClientPlayNetworking.Context context) {
        try (MinecraftClient client = context.client()) {
            if (client.currentScreen instanceof MemberScreen memberScreen) {
                memberScreen.getScreenHandler().appendCoinFlipResult(payload.amount);
            }
        }
    }
}

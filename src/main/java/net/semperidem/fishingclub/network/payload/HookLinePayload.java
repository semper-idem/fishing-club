package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.semperidem.fishingclub.entity.HookEntity;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

public record HookLinePayload(int amount) implements CustomPayload {
    public static final CustomPayload.Id<HookLinePayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_set_line_length"));
    public static final PacketCodec<RegistryByteBuf, HookLinePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, HookLinePayload::amount, HookLinePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(HookLinePayload payload, ServerPlayNetworking.Context context) {
        try (MinecraftServer server = context.server()){
            server.execute(() -> {
                if (context.player().fishHook instanceof HookEntity hookEntity) {
                    hookEntity.scrollLine(payload.amount);
                }

            });
        }
    }
}

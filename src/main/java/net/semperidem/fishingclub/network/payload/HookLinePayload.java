package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.entity.HookEntity;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record HookLinePayload(int lineLength) implements CustomPayload {
    public static final CustomPayload.Id<HookLinePayload> ID = new CustomPayload.Id<>(identifier("s2c_set_hook_line_length"));
    public static final PacketCodec<RegistryByteBuf, HookLinePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, HookLinePayload::lineLength, HookLinePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(HookLinePayload payload, ClientPlayNetworking.Context context) {
        if (context.player().fishHook instanceof HookEntity hookEntity) {
            hookEntity.setLineLength(payload.lineLength());
        }
    }
}

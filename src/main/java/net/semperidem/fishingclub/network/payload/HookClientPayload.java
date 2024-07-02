package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreenFactory;
import net.semperidem.fishingclub.entity.HookEntity;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

public record HookClientPayload() implements CustomPayload {
    public static final CustomPayload.Id<HookClientPayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_request_rod"));
    public static final PacketCodec<RegistryByteBuf, HookClientPayload> CODEC = PacketCodec.unit(new HookClientPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(HookClientPayload payload, ServerPlayNetworking.Context context) {
        if (!(context.player().fishHook instanceof HookEntity hookEntity)) {
            return;
        }
        context.responseSender().createPacket(new HookPayload(hookEntity.getFishingRod()));

    }
}

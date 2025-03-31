package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.screen.configuration.RodConfigurationScreenHandlerFactory;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record RodConfigurationPayload() implements CustomPayload {
    static {
        ID = new CustomPayload.Id<>(identifier("c2s_configuration"));
        CODEC = PacketCodec.unit(new RodConfigurationPayload());
    }
    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(RodConfigurationPayload payload, ServerPlayNetworking.Context context) {
        context.player().openHandledScreen(new RodConfigurationScreenHandlerFactory());
    }


    public static final CustomPayload.Id<RodConfigurationPayload> ID;
    public static final PacketCodec<RegistryByteBuf, RodConfigurationPayload> CODEC;
}

package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.screen.configuration.ConfigurationScreenHandlerFactory;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record ConfigurationPayload() implements CustomPayload {
    static {
        ID = new CustomPayload.Id<>(identifier("c2s_configuration"));
        CODEC = PacketCodec.unit(new ConfigurationPayload());
    }
    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(ConfigurationPayload payload, ServerPlayNetworking.Context context) {
        context.player().openHandledScreen(new ConfigurationScreenHandlerFactory());
    }


    public static final CustomPayload.Id<ConfigurationPayload> ID;
    public static final PacketCodec<RegistryByteBuf, ConfigurationPayload> CODEC;
}

package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fisher.FishingCard;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record SummonAcceptPayload() implements CustomPayload{
    public static final CustomPayload.Id<SummonAcceptPayload> ID = new CustomPayload.Id<>(identifier("c2s_summon_accept"));
    public static final PacketCodec<RegistryByteBuf, SummonAcceptPayload> CODEC =
            PacketCodec.unit(new SummonAcceptPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(SummonAcceptPayload payload, ServerPlayNetworking.Context context) {
        FishingCard.of(context.player()).acceptSummonRequest();
    }

}

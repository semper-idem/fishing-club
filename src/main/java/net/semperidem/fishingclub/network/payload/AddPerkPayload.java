package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fisher.FishingCard;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

public record AddPerkPayload(String perkName) implements CustomPayload {
    public static final CustomPayload.Id<AddPerkPayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_add_perk"));
    public static final PacketCodec<RegistryByteBuf, AddPerkPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, AddPerkPayload::perkName, AddPerkPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(AddPerkPayload payload, ServerPlayNetworking.Context context) {
        FishingCard.of(context.player()).addPerk(payload.perkName);
    }
}

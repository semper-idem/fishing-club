package net.semperidem.fishingclub.network.payload;

import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record FishingGamePostS2CPayload(SpecimenData fish) implements CustomPayload {
    public static final CustomPayload.Id<FishingGameStartS2CPayload> ID =
            new CustomPayload.Id<>(identifier("s2c_fishing_game_post"));
    public static final PacketCodec<RegistryByteBuf, FishingGamePostS2CPayload> CODEC =
            PacketCodec.tuple(
                    SpecimenData.PACKET_CODEC,
                    FishingGamePostS2CPayload::fish,
                    FishingGamePostS2CPayload::new);


    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
        }
    }
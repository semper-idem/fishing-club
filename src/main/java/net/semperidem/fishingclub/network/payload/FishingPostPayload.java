package net.semperidem.fishingclub.network.payload;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;

import java.util.List;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record FishingPostPayload(SpecimenData fish, List<ItemStack> rewards, int exp) implements CustomPayload {
    public static final CustomPayload.Id<FishingStartS2CPayload> ID =
            new CustomPayload.Id<>(identifier("s2c_fishing_post"));
    public static final PacketCodec<RegistryByteBuf, FishingPostPayload> CODEC =
            PacketCodec.tuple(
                    SpecimenData.PACKET_CODEC,
                    FishingPostPayload::fish,
                    ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()),
                    FishingPostPayload::rewards,
                    PacketCodecs.INTEGER,
                    FishingPostPayload::exp,
                    FishingPostPayload::new);


    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
        }
    }
package net.semperidem.fishingclub.network.payload;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;

import java.util.ArrayList;
import java.util.List;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record FishingGamePostS2CPayload(SpecimenData fish, List<ItemStack> rewards, int exp) implements CustomPayload {
    public static final CustomPayload.Id<FishingGameStartS2CPayload> ID =
            new CustomPayload.Id<>(identifier("s2c_fishing_game_post"));
    public static final PacketCodec<RegistryByteBuf, FishingGamePostS2CPayload> CODEC =
            PacketCodec.tuple(
                    SpecimenData.PACKET_CODEC,
                    FishingGamePostS2CPayload::fish,
                    ItemStack.PACKET_CODEC.collect(PacketCodecs.toList()),
                    FishingGamePostS2CPayload::rewards,
                    PacketCodecs.INTEGER,
                    FishingGamePostS2CPayload::exp,
                    FishingGamePostS2CPayload::new);


    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
        }
    }
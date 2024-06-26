package net.semperidem.fishingclub.network.payload;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

public record FishingGamePayload(NbtCompound fishNbt) implements CustomPayload {
    public static final CustomPayload.Id<FishingGamePayload> ID = new CustomPayload.Id<>(getIdentifier("s2c_fishing_game_open"));
    public static final PacketCodec<RegistryByteBuf, FishingGamePayload> CODEC = PacketCodec.tuple(PacketCodecs.NBT_COMPOUND, FishingGamePayload::fishNbt, FishingGamePayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

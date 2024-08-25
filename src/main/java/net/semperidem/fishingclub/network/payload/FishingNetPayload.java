package net.semperidem.fishingclub.network.payload;

import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.FishingClub;

import java.util.List;

public record FishingNetPayload(List<ItemStack> fish) implements CustomPayload {
    public static final CustomPayload.Id<FishingNetPayload> ID = new CustomPayload.Id<>(FishingClub.identifier("s2c_fishing_net"));
    public static final PacketCodec<RegistryByteBuf, FishingNetPayload> CODEC = ItemStack.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(FishingNetPayload::new, component -> component.fish);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}

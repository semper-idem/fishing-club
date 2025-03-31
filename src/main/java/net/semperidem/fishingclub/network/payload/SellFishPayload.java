package net.semperidem.fishingclub.network.payload;

import java.util.List;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.registry.Components;

public record SellFishPayload(List<ItemStack> fish) implements CustomPayload {
    public static final CustomPayload.Id<SellFishPayload> ID = new CustomPayload.Id<>(FishingClub.identifier("c2s_sell_fish"));
    public static final PacketCodec<RegistryByteBuf, SellFishPayload> CODEC = ItemStack.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(SellFishPayload::new, component -> component.fish);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(SellFishPayload payload, ServerPlayNetworking.Context context) {
        if (payload.fish().isEmpty()) {
            return;
        }

        int totalValue = 0;
        for(ItemStack fishStack : payload.fish()) {
            totalValue += fishStack.getOrDefault(Components.SPECIMEN_DATA, SpecimenData.DEFAULT).value();
            fishStack.setCount(0);
        }
        Card card = Card.of(context.player());
        card.addCredit(totalValue);
    }
}

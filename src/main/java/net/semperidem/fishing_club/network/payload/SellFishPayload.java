package net.semperidem.fishing_club.network.payload;

import java.util.List;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.fish.FishComponent;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.registry.FCComponents;

public record SellFishPayload(List<ItemStack> fish) implements CustomPayload {
    public static final CustomPayload.Id<SellFishPayload> ID = new CustomPayload.Id<>(FishingClub.getIdentifier("c2s_sell_fish"));
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
            totalValue += fishStack.getOrDefault(FCComponents.FISH, FishComponent.DEFAULT).value();
            fishStack.setCount(0);
        }
        FishingCard fishingCard = FishingCard.of(context.player());
        fishingCard.addCredit(totalValue);
    }
}

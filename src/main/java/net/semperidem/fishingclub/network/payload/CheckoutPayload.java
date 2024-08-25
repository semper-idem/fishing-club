package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.Items;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.shop.OrderItem;
import net.semperidem.fishingclub.fisher.shop.StockEntry;
import net.semperidem.fishingclub.registry.FCItems;

import java.util.List;

public record CheckoutPayload(List<OrderItem> cart) implements CustomPayload {
    public static final CustomPayload.Id<CheckoutPayload> ID = new CustomPayload.Id<>(FishingClub.identifier("c2s_checkout"));
    public static final PacketCodec<RegistryByteBuf, CheckoutPayload> CODEC = OrderItem.PACKET_CODEC
            .collect(PacketCodecs.toList())
            .xmap(CheckoutPayload::new, component -> component.cart);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(CheckoutPayload payload, ServerPlayNetworking.Context context) {
        int total = 0;
        for (OrderItem item : payload.cart) {
            if (item.content().isEmpty()) {
                continue;
            }
            double itemPrice = 0;
            if (item.content().get().isOf(Items.FIREWORK_ROCKET) || item.content().get().isOf(FCItems.ILLEGAL_GOODS)) {
                itemPrice = item.price().orElse(100);
            } else {
                itemPrice = StockEntry.getPriceFor(item.content().get(), item.quantity());
            }
            if (itemPrice < 0) {
                return;//throw item does not exist exception
            }
            total += (int) itemPrice;
        }
        FishingCard card = FishingCard.of(context.player());
        if (card.getCredit() < total) {
            return;
        }
        card.addCredit(-total);
        for (OrderItem item : payload.cart) {
            if (item.content().isEmpty()) {
                continue;
            }
            context.player().giveItemStack(item.content().get());
        }
    }
}

package net.semperidem.fishing_club.fisher.shop;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;

import java.util.Optional;

public record OrderItem(Optional<ItemStack> content, int quantity, Optional<Integer> price) {
    public static final OrderItem DEFAULT = new OrderItem(
            Optional.empty(),
            0,
            Optional.empty()
    );
    public static Codec<OrderItem> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemStack.CODEC.optionalFieldOf("content").forGetter(OrderItem::content),
            PrimitiveCodec.INT.fieldOf("quantity").forGetter(OrderItem::quantity),
            PrimitiveCodec.INT.optionalFieldOf("price").forGetter(OrderItem::price)
    ).apply(instance, OrderItem::new));

    public static PacketCodec<RegistryByteBuf, OrderItem> PACKET_CODEC = PacketCodecs.registryCodec(CODEC);

}

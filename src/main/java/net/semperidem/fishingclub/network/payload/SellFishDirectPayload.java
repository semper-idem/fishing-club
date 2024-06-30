package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.FishComponent;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.ComponentRegistry;

import java.util.Optional;

public record SellFishDirectPayload(ItemStack fish) implements CustomPayload {
    public static final CustomPayload.Id<SellFishDirectPayload> ID = new CustomPayload.Id<>(FishingClub.getIdentifier("c2s_sell_fish_direct"));
    public static final PacketCodec<RegistryByteBuf, SellFishDirectPayload> CODEC = PacketCodec.tuple(
            ItemStack.PACKET_CODEC, SellFishDirectPayload::fish, SellFishDirectPayload::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(SellFishDirectPayload payload, ServerPlayNetworking.Context context) {
        try (MinecraftServer server = context.server()) {
            server.execute(() -> {
                if (payload.fish().isEmpty()) {
                    return;
                }

                int value = payload.fish.getOrDefault(ComponentRegistry.FISH, FishComponent.DEFAULT).value();
                payload.fish.setCount(0);
                FishingCard fishingCard = FishingCard.of(context.player());
                fishingCard.addCredit(value);
            });
        }
    }
}

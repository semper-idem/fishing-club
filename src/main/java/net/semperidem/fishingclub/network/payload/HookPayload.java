package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.entity.HookEntity;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

public record HookPayload(ItemStack fishingRod) implements CustomPayload {
        public static final CustomPayload.Id<HookPayload> ID = new CustomPayload.Id<>(getIdentifier("s2c_set_hook_rod"));
        public static final PacketCodec<RegistryByteBuf, HookPayload> CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, HookPayload::fishingRod, HookPayload::new);

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }

        public static void consumePayload(HookPayload payload, ClientPlayNetworking.Context context) {
                if (context.player().fishHook instanceof HookEntity hookEntity) {
                        hookEntity.initClient(payload.fishingRod());
                }
        }
}

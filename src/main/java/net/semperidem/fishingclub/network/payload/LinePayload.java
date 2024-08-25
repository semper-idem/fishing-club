package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.item.fishing_rod.MemberFishingRodItem;
import net.semperidem.fishingclub.registry.FCItems;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record LinePayload(int amount) implements CustomPayload {
    public static final CustomPayload.Id<LinePayload> ID = new CustomPayload.Id<>(identifier("c2s_set_line_length"));
    public static final PacketCodec<RegistryByteBuf, LinePayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, LinePayload::amount, LinePayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(LinePayload payload, ServerPlayNetworking.Context context) {
        ItemStack stackInHand = context.player().getMainHandStack();
        if (!(stackInHand.getItem() instanceof MemberFishingRodItem)) {
            return;
        }
        int lineLength = FCItems.MEMBER_FISHING_ROD.scrollLineBy(context.player(), stackInHand, payload.amount());
        if (context.player().fishHook instanceof HookEntity hookEntity) {
            hookEntity.setLineLength(lineLength);
            context.responseSender().sendPacket(new HookLinePayload(lineLength));
        }
    }
}

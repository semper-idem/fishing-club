package net.semperidem.fishing_club.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishing_club.screen.member.MemberScreenHandlerFactory;

import static net.semperidem.fishing_club.FishingClub.getIdentifier;

public record MemberPayload() implements CustomPayload {
    public static final CustomPayload.Id<MemberPayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_member_screen"));
    public static final PacketCodec<RegistryByteBuf, MemberPayload> CODEC =
            PacketCodec.unit(new MemberPayload());

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(MemberPayload payload, ServerPlayNetworking.Context context) {
        context.player().openHandledScreen(new MemberScreenHandlerFactory());
    }
}

package net.semperidem.fishing_club.network.payload;

import static net.semperidem.fishing_club.FishingClub.getIdentifier;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishing_club.FishingServerWorld;
import net.semperidem.fishing_club.screen.dialog.Responses;
import net.semperidem.fishing_club.screen.member.MemberScreenHandlerFactory;

public record DialogResponsePayload(String response) implements CustomPayload {
    public static final CustomPayload.Id<DialogResponsePayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_dialog_response"));
    public static final PacketCodec<RegistryByteBuf, DialogResponsePayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, DialogResponsePayload::response, DialogResponsePayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(DialogResponsePayload payload, ServerPlayNetworking.Context context) {
        if (!(context.player().getServerWorld() instanceof FishingServerWorld serverWorld)) {
            return;
        }
        if (serverWorld.getDerek() == null) {
            return;
        }
        switch (payload.response()) {
            case Responses.EXIT -> context.player().closeHandledScreen();
            case Responses.TRADE -> context.player().openHandledScreen(new MemberScreenHandlerFactory());
            case Responses.ACCEPT -> serverWorld.getDerek().acceptTrade();
            case Responses.REFUSE -> serverWorld.getDerek().refuseTrade();
        }
    }
}
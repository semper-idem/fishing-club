package net.semperidem.fishingclub.network.payload;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.world.FishingServerWorld;
import net.semperidem.fishingclub.screen.dialog.DialogNode;
import net.semperidem.fishingclub.screen.member.MemberScreenHandlerFactory;

import static net.semperidem.fishingclub.FishingClub.identifier;

public record DialogResponsePayload(String response) implements CustomPayload {
    public static final CustomPayload.Id<DialogResponsePayload> ID = new CustomPayload.Id<>(identifier("c2s_dialog_response"));
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
        switch (DialogNode.Action.valueOf(payload.response())) {
            case DialogNode.Action.EXIT -> context.player().closeHandledScreen();
            case DialogNode.Action.TRADE -> context.player().openHandledScreen(new MemberScreenHandlerFactory());
            case DialogNode.Action.ACCEPT -> serverWorld.getDerek().acceptTrade();
            case DialogNode.Action.DISMISS -> serverWorld.getDerek().dismiss();
        }
    }
}
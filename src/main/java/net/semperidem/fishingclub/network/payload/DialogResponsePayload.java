package net.semperidem.fishingclub.network.payload;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.semperidem.fishingclub.FishingServerWorld;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.screen.dialog.Responses;
import net.semperidem.fishingclub.screen.member.MemberScreenHandlerFactory;

public record DialogResponsePayload(String response) implements CustomPayload {
    public static final CustomPayload.Id<DialogResponsePayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_dialog_response"));
    public static final PacketCodec<RegistryByteBuf, DialogResponsePayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, DialogResponsePayload::response, DialogResponsePayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(DialogResponsePayload payload, ServerPlayNetworking.Context context) {
        try (MinecraftServer server = context.server()) {
            server.execute(() -> {
                switch (payload.response()) {
                    case Responses.EXIT -> exitResponse(context);
                    case Responses.TRADE -> tradeResponse(context);
                    case Responses.ACCEPT -> giveResponse(server);
                    case Responses.REFUSE -> refuseResponse(server);
                }
            });
        }
    }
    
    private static void exitResponse(ServerPlayNetworking.Context context) {
        context.player().closeHandledScreen();
    }
    
    private static void  tradeResponse(ServerPlayNetworking.Context context) {
        context.player().openHandledScreen(new MemberScreenHandlerFactory());
    }
    
    private static void giveResponse(MinecraftServer server) {
        if (!(server.getOverworld() instanceof FishingServerWorld serverWorld)) {
            return;
        }
        FishermanEntity derek = serverWorld.getDerek();
        if (derek != null) {
            derek.acceptTrade();
        }
    }
    
    private static void refuseResponse(MinecraftServer server) {
        if (!(server.getOverworld() instanceof FishingServerWorld serverWorld)) {
            return;
        }
        FishermanEntity derek = serverWorld.getDerek();
        if (derek != null) {
            derek.refuseTrade();
        }
        
    }
}
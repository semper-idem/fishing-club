package net.semperidem.fishingclub.network.payload;

import static net.semperidem.fishingclub.FishingClub.getIdentifier;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.semperidem.fishingclub.screen.dialog.DialogScreenHandlerFactory;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;

public record DialogPayload(String openingKeys) implements CustomPayload {
    public static final CustomPayload.Id<DialogPayload> ID = new CustomPayload.Id<>(getIdentifier("c2s_dialog"));
    public static final PacketCodec<RegistryByteBuf, DialogPayload> CODEC = PacketCodec.tuple(PacketCodecs.STRING, DialogPayload::openingKeys, DialogPayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void consumePayload(DialogPayload payload, ServerPlayNetworking.Context context) {
        try (MinecraftServer server = context.server()) {
      server.execute(
          () -> {
            context
                .player()
                .openHandledScreen(new DialogScreenHandlerFactory(DialogUtil.getKeysFromString(payload.openingKeys)));
          });
        }
    }
}

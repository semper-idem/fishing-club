package net.semperidem.fishingclub.network.payload;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.PrimitiveCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.game.FishingGameScreen;

public record FishingGameTickPayload(
        float bobberPositionX,
        float fishPositionX,
        float fishPositionY,
        float health,
        float progress,
        boolean canPullTreasure,
        float arrowPos,
        int treasureHookedTicks,
        boolean isWon
) implements CustomPayload {
    public static final CustomPayload.Id<FishingGameTickPayload> ID = new FishingGameTickPayload.Id<>(FishingClub.getIdentifier("s2c_fishing_game_tick"));
    private static final Codec<FishingGameTickPayload> CODEC_DTO = RecordCodecBuilder.create(instance -> instance.group(
            PrimitiveCodec.FLOAT.fieldOf("bobberPositionX").forGetter(FishingGameTickPayload::bobberPositionX),
            PrimitiveCodec.FLOAT.fieldOf("fishPositionX").forGetter(FishingGameTickPayload::fishPositionX),
            PrimitiveCodec.FLOAT.fieldOf("fishPositionY").forGetter(FishingGameTickPayload::fishPositionY),
            PrimitiveCodec.FLOAT.fieldOf("health").forGetter(FishingGameTickPayload::health),
            PrimitiveCodec.FLOAT.fieldOf("progress").forGetter(FishingGameTickPayload::progress),
            PrimitiveCodec.BOOL.fieldOf("canPullTreasure").forGetter(FishingGameTickPayload::canPullTreasure),
            PrimitiveCodec.FLOAT.fieldOf("arrowPos").forGetter(FishingGameTickPayload::arrowPos),
            PrimitiveCodec.INT.fieldOf("treasureHookedTicks").forGetter(FishingGameTickPayload::treasureHookedTicks),
            PrimitiveCodec.BOOL.fieldOf("isWon").forGetter(FishingGameTickPayload::isWon)
    ).apply(instance, FishingGameTickPayload::new));

    public static final PacketCodec<RegistryByteBuf, FishingGameTickPayload> CODEC = PacketCodecs.registryCodec(CODEC_DTO);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(FishingGameTickPayload payload, ClientPlayNetworking.Context context) {
            if (MinecraftClient.getInstance().currentScreen instanceof FishingGameScreen fishingGameScreen) {
                fishingGameScreen.consumeTick(payload);
            }
    }
}

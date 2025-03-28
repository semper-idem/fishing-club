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
import net.semperidem.fishingclub.client.screen.game.FishingScreen;

public record FishingGameTickS2CPayload(
        float bobberPositionX,
        float fishPositionX,
        float fishPositionY,
        float health,
        float progress,
        boolean canPullTreasure,
        float treasureStartProgress,
        float treasureStartPosition,
        float treasureProgress,
        int treasureTicksLeft,
        boolean isReeling,
        boolean isTreasureDone
) implements CustomPayload {
    public static final CustomPayload.Id<FishingGameTickS2CPayload> ID = new FishingGameTickS2CPayload.Id<>(FishingClub.identifier("s2c_fishing_game_tick"));
    private static final Codec<FishingGameTickS2CPayload> CODEC_DTO = RecordCodecBuilder.create(instance -> instance.group(
            PrimitiveCodec.FLOAT.fieldOf("bobberPositionX").forGetter(FishingGameTickS2CPayload::bobberPositionX),
            PrimitiveCodec.FLOAT.fieldOf("fishPositionX").forGetter(FishingGameTickS2CPayload::fishPositionX),
            PrimitiveCodec.FLOAT.fieldOf("fishPositionY").forGetter(FishingGameTickS2CPayload::fishPositionY),
            PrimitiveCodec.FLOAT.fieldOf("health").forGetter(FishingGameTickS2CPayload::health),
            PrimitiveCodec.FLOAT.fieldOf("progress").forGetter(FishingGameTickS2CPayload::progress),
            PrimitiveCodec.BOOL.fieldOf("canPullTreasure").forGetter(FishingGameTickS2CPayload::canPullTreasure),
            PrimitiveCodec.FLOAT.fieldOf("treasureStartProgress").forGetter(FishingGameTickS2CPayload::treasureStartProgress),
            PrimitiveCodec.FLOAT.fieldOf("treasureStartPosition").forGetter(FishingGameTickS2CPayload::treasureStartPosition),
            PrimitiveCodec.FLOAT.fieldOf("treasureProgress").forGetter(FishingGameTickS2CPayload::treasureProgress),
            PrimitiveCodec.INT.fieldOf("treasureTicksLeft").forGetter(FishingGameTickS2CPayload::treasureTicksLeft),
            PrimitiveCodec.BOOL.fieldOf("isReeling").forGetter(FishingGameTickS2CPayload::isReeling),
            PrimitiveCodec.BOOL.fieldOf("isTreasureDone").forGetter(FishingGameTickS2CPayload::isTreasureDone)
    ).apply(instance, FishingGameTickS2CPayload::new));

    public static final PacketCodec<RegistryByteBuf, FishingGameTickS2CPayload> CODEC = PacketCodecs.registryCodec(CODEC_DTO);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(FishingGameTickS2CPayload payload, ClientPlayNetworking.Context context) {
            if (MinecraftClient.getInstance().currentScreen instanceof FishingScreen fishingScreen) {
                fishingScreen.update(payload);
            }
    }
}

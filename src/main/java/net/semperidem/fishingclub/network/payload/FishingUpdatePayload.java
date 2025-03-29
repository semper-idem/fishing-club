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

public record FishingUpdatePayload(
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
    public static final CustomPayload.Id<FishingUpdatePayload> ID = new FishingUpdatePayload.Id<>(FishingClub.identifier("s2c_fishing_update"));
    private static final Codec<FishingUpdatePayload> CODEC_DTO = RecordCodecBuilder.create(instance -> instance.group(
            PrimitiveCodec.FLOAT.fieldOf("bobberPositionX").forGetter(FishingUpdatePayload::bobberPositionX),
            PrimitiveCodec.FLOAT.fieldOf("fishPositionX").forGetter(FishingUpdatePayload::fishPositionX),
            PrimitiveCodec.FLOAT.fieldOf("fishPositionY").forGetter(FishingUpdatePayload::fishPositionY),
            PrimitiveCodec.FLOAT.fieldOf("health").forGetter(FishingUpdatePayload::health),
            PrimitiveCodec.FLOAT.fieldOf("progress").forGetter(FishingUpdatePayload::progress),
            PrimitiveCodec.BOOL.fieldOf("canPullTreasure").forGetter(FishingUpdatePayload::canPullTreasure),
            PrimitiveCodec.FLOAT.fieldOf("treasureStartProgress").forGetter(FishingUpdatePayload::treasureStartProgress),
            PrimitiveCodec.FLOAT.fieldOf("treasureStartPosition").forGetter(FishingUpdatePayload::treasureStartPosition),
            PrimitiveCodec.FLOAT.fieldOf("treasureProgress").forGetter(FishingUpdatePayload::treasureProgress),
            PrimitiveCodec.INT.fieldOf("treasureTicksLeft").forGetter(FishingUpdatePayload::treasureTicksLeft),
            PrimitiveCodec.BOOL.fieldOf("isReeling").forGetter(FishingUpdatePayload::isReeling),
            PrimitiveCodec.BOOL.fieldOf("isTreasureDone").forGetter(FishingUpdatePayload::isTreasureDone)
    ).apply(instance, FishingUpdatePayload::new));

    public static final PacketCodec<RegistryByteBuf, FishingUpdatePayload> CODEC = PacketCodecs.registryCodec(CODEC_DTO);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
    public static void consumePayload(FishingUpdatePayload payload, ClientPlayNetworking.Context context) {
            if (MinecraftClient.getInstance().currentScreen instanceof FishingScreen fishingScreen) {
                fishingScreen.update(payload);
            }
    }
}

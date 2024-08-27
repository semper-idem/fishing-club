package net.semperidem.fishingclub.mixin.common;


import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.semperidem.fishingclub.fish.Species;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.semperidem.fishingclub.world.ChunkQuality.CHUNK_QUALITY;

@Mixin(WaterCreatureEntity.class)
public class WaterCreatureEntityMixin {

    @Inject(
            method = "canSpawn(Lnet/minecraft/entity/EntityType;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/entity/SpawnReason;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)Z",
            at = @At("RETURN"),
            cancellable = true
    )
    private static void fishing_club$canSpawn(EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        String name = type.getUntranslatedName();
        if (name.equals("dolphin")) {
            return;
        }
        double chunkQuality = CHUNK_QUALITY.get(world.getChunk(pos)).getValue();
        double speciesMinChunkQuality = Math.log(Species.Library.ofName(name).rarity()) * 0.5;
        if (chunkQuality < speciesMinChunkQuality) {
            cir.setReturnValue(false);
        }
    }
}

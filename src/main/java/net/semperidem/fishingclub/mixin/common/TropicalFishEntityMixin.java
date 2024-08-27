package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.world.ChunkQuality;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(TropicalFishEntity.class)
public class TropicalFishEntityMixin {

    @Redirect(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"))
    private float fishing_club$nextFloat(net.minecraft.util.math.random.Random instance) {
        return 0; //anything below 0.9 will spawn common variant
    }

    @Inject(method = "canTropicalFishSpawn", at = @At("RETURN"), cancellable = true)
    private static void fishing_club$canTropicalFishSpawn(EntityType<TropicalFishEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random, CallbackInfoReturnable<Boolean> cir) {
        if (!ChunkQuality.isAboveMinimumQuality(Species.Library.TROPICAL_FISH, world, pos)) {
            cir.setReturnValue(false);
        }
    }

}

package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.semperidem.fishingclub.world.ChunkQuality;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.semperidem.fishingclub.world.ChunkQuality.CHUNK_QUALITY;

@Mixin(BucketItem.class)
public class BucketItemMixin {
	@Inject(method = "placeFluid", at = @At("RETURN"))
	private void onPlaceFluid(PlayerEntity player, World world, BlockPos pos, BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			return;
		}
		CHUNK_QUALITY.get(world.getChunk(pos)).influence(ChunkQuality.PlayerInfluence.FLUID);
	}
}

package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BucketItem;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.semperidem.fishingclub.world.ChunkQuality;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BucketItem.class)
public class BucketItemMixin {
	@Inject(method = "placeFluid", at = @At("RETURN"))
	private void onPlaceFluid(LivingEntity user, World world, BlockPos pos, BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValue()) {
			return;
		}
		ChunkQuality.influence(user, ChunkQuality.PlayerInfluence.FLUID);
	}
}

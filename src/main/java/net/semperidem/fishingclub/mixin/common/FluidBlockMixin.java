package net.semperidem.fishingclub.mixin.common;

import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.semperidem.fishingclub.world.ChunkQuality;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.semperidem.fishingclub.world.ChunkQuality.CHUNK_QUALITY;

@Mixin(FluidBlock.class)
public class FluidBlockMixin {

	@Inject(method = "tryDrainFluid", at = @At("RETURN"))
	private void tryDrainFluid(PlayerEntity player, WorldAccess world, BlockPos pos, BlockState state, CallbackInfoReturnable<ItemStack> cir) {
		if (cir.getReturnValue().isEmpty()) {
			return;
		}
		CHUNK_QUALITY.get(world.getChunk(pos)).influence(ChunkQuality.PlayerInfluence.FLUID);
	}
}

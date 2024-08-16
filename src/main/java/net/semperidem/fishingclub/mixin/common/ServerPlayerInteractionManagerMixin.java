package net.semperidem.fishingclub.mixin.common;

import net.minecraft.server.network.ServerPlayerInteractionManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.semperidem.fishingclub.world.ChunkQuality;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.semperidem.fishingclub.world.ChunkQuality.CHUNK_QUALITY;

@Mixin(ServerPlayerInteractionManager.class)
public class ServerPlayerInteractionManagerMixin {


	@Shadow protected ServerWorld world;

	@Inject(method = "tryBreakBlock", at = @At("RETURN"))
	private void onBreakBlock(BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		CHUNK_QUALITY.get(world.getChunk(pos)).influence(ChunkQuality.PlayerInfluence.BLOCK);
	}

}

package net.semperidem.fishingclub.mixin.common;

import net.minecraft.world.chunk.AbstractChunkHolder;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkGenerating;
import net.minecraft.world.chunk.ChunkGenerationContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.semperidem.fishingclub.world.ChunkQuality.CHUNK_QUALITY;

@Mixin(ChunkGenerating.class)
public class ChunkGeneratingMixin {
	@Inject(method = "method_60553", at = @At("RETURN"))
	private static void afterChunkGeneration(Chunk chunk, ChunkGenerationContext chunkGenerationContext, AbstractChunkHolder abstractChunkHolder, CallbackInfoReturnable<Chunk> cir) {
		CHUNK_QUALITY.maybeGet(cir.getReturnValue()).ifPresent(x -> x.init(chunkGenerationContext.world()));
	}
}

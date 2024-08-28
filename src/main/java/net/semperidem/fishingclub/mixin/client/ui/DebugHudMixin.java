package net.semperidem.fishingclub.mixin.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.semperidem.fishingclub.world.ChunkQuality;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(DebugHud.class)
public class DebugHudMixin {

	@Shadow @Final private MinecraftClient client;

	@Inject(method = "getRightText", at = @At("HEAD"),
			cancellable = true
	)
	public void getRightText(CallbackInfoReturnable<List<String>> cir) {
		assert client.player != null;
		ChunkQuality.CHUNK_QUALITY.maybeGet(client.player.getWorld().getChunk(client.player.getBlockPos())).ifPresent(chunk -> {

			List<String> original = new ArrayList<>();
			original.add("chunk_quality");
			original.add("value: " + String.format("%.4f",chunk.getValue()));
			original.add("base: " + String.format("%.4f", chunk.getBase()));
			original.add("ceiling: " + String.format( "%.4f", chunk.getCeiling()));
			cir.setReturnValue(original);
		});
	}
}

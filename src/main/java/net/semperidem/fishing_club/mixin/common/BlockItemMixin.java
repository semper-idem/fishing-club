package net.semperidem.fishing_club.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.block.PlantBlock;
import net.minecraft.block.Waterloggable;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.semperidem.fishing_club.world.ChunkQuality;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.semperidem.fishing_club.world.ChunkQuality.CHUNK_QUALITY;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

	@Shadow public abstract Block getBlock();

	@Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemPlacementContext;getBlockPos()Lnet/minecraft/util/math/BlockPos;"))
	private void onPlaceBlock(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir) {
		ChunkQuality chunk =CHUNK_QUALITY.get(context.getWorld().getChunk(context.getBlockPos()));
		chunk.influence(ChunkQuality.PlayerInfluence.BLOCK);
		Block block = getBlock();
		if (!(block instanceof PlantBlock)) {
			return;
		}

		if (!(this.getBlock() instanceof Waterloggable)) {
			return;
		}
		chunk.needsRecalculation();
	}

}

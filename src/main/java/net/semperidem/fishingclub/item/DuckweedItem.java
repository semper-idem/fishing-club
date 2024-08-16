package net.semperidem.fishingclub.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowerbedBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.PlaceableOnWaterItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.semperidem.fishingclub.registry.FCBlocks;
import net.semperidem.fishingclub.registry.FCItems;

public class DuckweedItem extends PlaceableOnWaterItem {

	public DuckweedItem(Block block, Settings settings) {
		super(block, settings);
	}

	@Override
	public ActionResult useOnBlock(ItemUsageContext context) {
		World world = context.getWorld();
		BlockPos pos = context.getBlockPos();
		BlockState blockState = world.getBlockState(pos);
		ItemStack itemStack = context.getStack();
		if (!blockState.isOf(FCBlocks.DUCKWEED_BLOCK)) {
			return super.useOnBlock(context);
		}
		if (!itemStack.isOf(FCItems.DUCKWEED)) {
			return super.useOnBlock(context);
		}
		int petalAmount = blockState.get(FlowerbedBlock.FLOWER_AMOUNT);
		if (petalAmount < 4) {
			world.setBlockState(pos, blockState.with(FlowerbedBlock.FLOWER_AMOUNT, petalAmount + 1));
			if (!context.getPlayer().isCreative()) {
				itemStack.decrement(1);
			}
			return ActionResult.SUCCESS;
		}
		return super.useOnBlock(context);
	}

}

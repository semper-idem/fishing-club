package net.semperidem.fishing_club.mixin.common;


import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.semperidem.fishing_club.registry.FCBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(LilyPadBlock.class)
public class LilyPadBlockMixin extends PlantBlock {

	protected LilyPadBlockMixin(Settings settings) {
		super(settings);
	}

	@Shadow
	public MapCodec<? extends PlantBlock> getCodec() {
		return LilyPadBlock.CODEC;
	}

	@Override
	protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (!world.getBlockState(pos.down()).isOf(Blocks.WATER)) {
			return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
		}

		if (!(stack.getItem() instanceof BlockItem blockItem)) {
			return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
		}

		world.setBlockState(pos, blockItem.getBlock().getDefaultState());
		world.setBlockState(pos.down(), FCBlocks.WATERLOGGED_LILY_PAD_BLOCK.getDefaultState());

		return ItemActionResult.SUCCESS;
	}

}

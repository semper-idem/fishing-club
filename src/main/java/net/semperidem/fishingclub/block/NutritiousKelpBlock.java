package net.semperidem.fishingclub.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.semperidem.fishingclub.registry.Blocks;
import org.jetbrains.annotations.Nullable;

import static net.semperidem.fishingclub.block.EnergyDenseKelpBlock.SHAPE;

public class NutritiousKelpBlock extends AbstractPlantStemBlock implements FluidFillable {
	public static final MapCodec<NutritiousKelpBlock> CODEC = createCodec(NutritiousKelpBlock::new);
	private static final double GROWTH_CHANCE = 0.05;

	public NutritiousKelpBlock(Settings settings) {
		super(settings, Direction.UP, SHAPE, true, GROWTH_CHANCE);
	}

	@Override
	public MapCodec<NutritiousKelpBlock> getCodec() {
		return CODEC;
	}


	@Override
	protected boolean chooseStemState(BlockState state) {
		return state.isOf(net.minecraft.block.Blocks.WATER);
	}

	@Override
	protected Block getPlant() {
		return Blocks.NUTRITIOUS_KELP_PLANT;
	}


	@Override
	protected boolean canAttachTo(BlockState state) {
		return !state.isOf(net.minecraft.block.Blocks.MAGMA_BLOCK) && !state.isOf(net.minecraft.block.Blocks.KELP) && !state.isOf(Blocks.ENERGY_DENSE_KELP);
	}


	@Override
	public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return true;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return true;
	}

	@Override
	protected int getGrowthLength(Random random) {
		return 1;
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8 ? super.getPlacementState(ctx) : null;
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStill(false);
	}

}

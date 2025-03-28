package net.semperidem.fishingclub.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractPlantBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.semperidem.fishingclub.registry.Blocks;
import org.jetbrains.annotations.Nullable;

import static net.semperidem.fishingclub.block.EnergyDenseKelpPlantBlock.SHAPE;

public class NutritiousKelpPlantBlock extends AbstractPlantBlock implements FluidFillable {
	public static final MapCodec<NutritiousKelpPlantBlock> CODEC = createCodec(NutritiousKelpPlantBlock::new);

	@Override
	public MapCodec<NutritiousKelpPlantBlock> getCodec() {
		return CODEC;
	}

	public NutritiousKelpPlantBlock(Settings settings) {
		super(settings, Direction.UP, SHAPE, true);
	}

	@Override
	protected NutritiousKelpBlock getStem() {
		return Blocks.NUTRITIOUS_KELP;
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStill(false);
	}

	@Override
	protected boolean canAttachTo(BlockState state) {
		return this.getStem().canAttachTo(state);
	}

	@Override
	public boolean canFillWithFluid(@Nullable PlayerEntity player, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return false;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return false;
	}
}
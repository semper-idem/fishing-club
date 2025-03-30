package net.semperidem.fishingclub.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.semperidem.fishingclub.registry.Blocks;
import org.jetbrains.annotations.Nullable;

public class EnergyDenseKelpPlantBlock extends AbstractPlantBlock implements FluidFillable {
	public static final MapCodec<EnergyDenseKelpPlantBlock> CODEC = createCodec(EnergyDenseKelpPlantBlock::new);
	public static final VoxelShape SHAPE = Block.createCuboidShape(7.0, 0.0, 7.0, 9.0, 16.0, 9.0);

	@Override
	public MapCodec<EnergyDenseKelpPlantBlock> getCodec() {
		return CODEC;
	}

	public EnergyDenseKelpPlantBlock(AbstractBlock.Settings settings) {
		super(settings, Direction.UP, SHAPE, true);
	}

	@Override
	protected EnergyDenseKelpBlock getStem() {
		return (EnergyDenseKelpBlock) Blocks.ENERGY_DENSE_KELP;
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
	public boolean canFillWithFluid(@Nullable LivingEntity filler, BlockView world, BlockPos pos, BlockState state, Fluid fluid) {
		return true;
	}

	@Override
	public boolean tryFillWithFluid(WorldAccess world, BlockPos pos, BlockState state, FluidState fluidState) {
		return true;
	}
}
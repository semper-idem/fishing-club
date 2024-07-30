package net.semperidem.fishing_club.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.semperidem.fishing_club.registry.FCStatusEffects;

import java.util.function.BiFunction;

public class DuckweedBlock extends FlowerbedBlock {
	private static final BooleanProperty CUT = BooleanProperty.of("cut");

	public DuckweedBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this.stateManager.getDefaultState()
				.with(FACING, Direction.NORTH)
				.with(FLOWER_AMOUNT, 1)
				.with(CUT, Boolean.FALSE)
		);
	}

	private static final BiFunction<Direction, Integer, VoxelShape> FACING_AND_AMOUNT_TO_SHAPE = Util.memoize(
		(facing, flowerAmount) -> {
			VoxelShape[] voxelShapes = new VoxelShape[]{
				Block.createCuboidShape(8.0, 0.0, 8.0, 16.0, 0.25, 16.0),
				Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 0.25, 8.0),
				Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 0.25, 8.0),
				Block.createCuboidShape(0.0, 0.0, 8.0, 8.0, 0.25, 16.0)
			};
			VoxelShape voxelShape = VoxelShapes.empty();

			for (int i = 0; i < flowerAmount; i++) {
				int j = Math.floorMod(i - facing.getHorizontal(), 4);
				voxelShape = VoxelShapes.union(voxelShape, voxelShapes[j]);
			}

			return voxelShape.asCuboid();
		}
	);

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FACING, FLOWER_AMOUNT, CUT);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int flowerCount = state.get(FLOWER_AMOUNT);
		if (!state.get(CUT) && state.get(FLOWER_AMOUNT) < 4 && Math.random() < 0.05) {
			world.setBlockState(pos, state.with(FLOWER_AMOUNT, flowerCount + 1));
			return;
		}
		world.getOtherEntities(null, new Box(pos)).forEach(o -> {
			tickStillness(o, flowerCount);
		});
	}

	private void tickStillness(Entity entity, int flowerCount) {
		if (!(entity instanceof PlayerEntity playerEntity)) {
			return;
		}
		playerEntity.addStatusEffect(new StatusEffectInstance(FCStatusEffects.MOISTURIZED, 600 * flowerCount));
	}


	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return FACING_AND_AMOUNT_TO_SHAPE.apply(state.get(FACING), state.get(FLOWER_AMOUNT));
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		FluidState fluidState = world.getFluidState(pos);
		FluidState fluidState2 = world.getFluidState(pos.up());
		return (fluidState.getFluid() == Fluids.WATER || floor.getBlock() instanceof IceBlock) && fluidState2.getFluid() == Fluids.EMPTY;
	}
}

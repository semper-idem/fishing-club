package net.semperidem.fishing_club.block;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import org.jetbrains.annotations.Nullable;

public class ReedBlock extends Block implements Waterloggable {

	public static final IntProperty AGE = Properties.AGE_15;
	protected static final VoxelShape SHAPE = Block.createCuboidShape(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

	private static final int MAX_HEIGHT = 2;
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

	public ReedBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, false).with(AGE, 0));
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}

	@Override
	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		BlockState blockState = world.getBlockState(pos.down());
		if (blockState.isOf(this)) {
			return true;
		} else {
			if (blockState.isIn(BlockTags.DIRT) || blockState.isIn(BlockTags.SAND)) {
				BlockPos blockPos = pos.down();

				for (Direction direction : Direction.Type.HORIZONTAL) {
					BlockState blockState2 = world.getBlockState(blockPos.offset(direction));
					FluidState fluidState = world.getFluidState(blockPos.offset(direction));
					if (fluidState.isIn(FluidTags.WATER) || blockState2.isOf(Blocks.FROSTED_ICE)) {
						return true;
					}
				}
			}

			return false;
		}
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		boolean bl = fluidState.getFluid() == Fluids.WATER;
		return super.getPlacementState(ctx).with(WATERLOGGED, bl);
	}


	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {

		if (!world.isAir(pos.up())) {
			return;
		}

		int height = calculateHeight(world, pos);

		if (height >= MAX_HEIGHT) {
			return;
		}

		int age = state.get(AGE);
		boolean waterlogged = state.get(WATERLOGGED);
		boolean grow = age == 15 || (age > 3 && waterlogged);

		if (grow) {
			world.setBlockState(pos.up(), this.getDefaultState());
		}

		world.setBlockState(pos, state.with(AGE, grow ? 0 : age + 1).with(WATERLOGGED, waterlogged), Block.NO_REDRAW);
	}

	private int calculateHeight(ServerWorld world, BlockPos pos) {

		int height = 1;

		while (world.getBlockState(pos.down(height)).isOf(this)) {
			height++;
		}
		BlockState blockBelow = world.getBlockState(pos.down());
		BlockState restingBlock = world.getBlockState(pos.down(2));
		if (blockBelow.isOf(this) && blockBelow.get(WATERLOGGED) && !restingBlock.isOf(this)) {
			height--;
		}

		return height;
	}


	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE, WATERLOGGED);
	}

	@Override
	protected BlockState getStateForNeighborUpdate(
		BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
	) {
		if (!state.canPlaceAt(world, pos)) {
			world.scheduleBlockTick(pos, this, 1);
			return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
		}
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}


}

package net.semperidem.fishingclub.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;

public class WaterloggedLilyPadBlock extends PlantBlock implements Waterloggable {

	public static final MapCodec<WaterloggedLilyPadBlock> CODEC = createCodec(WaterloggedLilyPadBlock::new);

	private static final VoxelShape WATERLOGGED_SHAPE = Block.createCuboidShape(1.0, 15, 1.0, 15.0, 16, 15.0);
	public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;
	public final static BooleanProperty FLOWERING = BooleanProperty.of("flowering");

	public WaterloggedLilyPadBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(WATERLOGGED, true).with(FLOWERING, false));
	}

	public MapCodec<WaterloggedLilyPadBlock> getCodec() {
		return CODEC;
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return Fluids.WATER.getStill(false);
	}

	@Nullable
	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());
		return fluidState.isIn(FluidTags.WATER) && fluidState.getLevel() == 8 ? super.getPlacementState(ctx) : null;
	}


	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.get(FLOWERING)) {
			dropStack(world, pos, new ItemStack(Items.LILY_PAD));
		}
		return super.onBreak(world, pos, state, player);
	}

	@Override
	protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		super.onEntityCollision(state, world, pos, entity);
		if (world instanceof ServerWorld && entity instanceof BoatEntity) {
			world.breakBlock(new BlockPos(pos), true, entity);
		}
	}


	@Override
	protected VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
		return super.getSidesShape(state, world, pos);
	}

	@Override
	protected void neighborUpdate(BlockState state, World world, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
		if(world.getBlockState(pos.up()).isAir() && world.getFluidState(pos).isIn(FluidTags.WATER)) {
			world.setBlockState(pos.up(), Blocks.LILY_PAD.getDefaultState().with(FLOWERING, state.get(FLOWERING)));
			world.setBlockState(pos, Fluids.WATER.getDefaultState().getBlockState());
		}

		super.neighborUpdate(state, world, pos, sourceBlock, sourcePos, notify);
	}

	@Override
	protected BlockState getStateForNeighborUpdate(
		BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
	) {
		if (state.get(WATERLOGGED)) {
			world.scheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));
		}

		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(WATERLOGGED, FLOWERING);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return (world.getFluidState(pos.up()).getFluid() == Fluids.WATER);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return WATERLOGGED_SHAPE;
	}

}

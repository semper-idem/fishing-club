package net.semperidem.fishingclub.block;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.semperidem.fishingclub.registry.StatusEffects;

import java.util.function.BiFunction;

public class DuckweedBlock extends FlowerbedBlock {
	private static final BooleanProperty CUT = BooleanProperty.of("cut");

	public DuckweedBlock(Settings settings) {
		super(settings);
		this.setDefaultState(
			this.stateManager.getDefaultState()
				.with(HORIZONTAL_FACING, Direction.NORTH)
				.with(FLOWER_AMOUNT, 1)
				.with(CUT, Boolean.FALSE)
		);
	}



	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, FLOWER_AMOUNT, CUT);
	}

	private void tickGrowth(BlockState state, ServerWorld world, BlockPos pos, Random random, int flowerCount) {
		if (random.nextFloat() > 0.2) {
			return;
		}
		if (state.get(CUT)) {
			return;
		}
		if (flowerCount < 4) {
			world.setBlockState(pos, state.with(FLOWER_AMOUNT, flowerCount + 1));
			return;
		}

		int neighbourCount = 0;
		for(Direction direction : Direction.values()) {
			if (world.getBlockState(pos.offset(direction)).isOf(this)) {
				neighbourCount++;
			}
		}
		int age = MathHelper.clamp(neighbourCount, 1, 4);

		Direction direction = Direction.random(random);
		BlockPos directionPos = pos.offset(direction);
		BlockState newState = state.with(FLOWER_AMOUNT, age);
		world.setBlockState(pos, newState);
		if (random.nextFloat() > 0.2) {
			return;
		}
		if (!state.canPlaceAt(world, directionPos)) {
			return;
		}
		BlockState stateAtDirection = world.getBlockState(directionPos);
		if (!stateAtDirection.isAir()) {
			return;
		}
		world.setBlockState(pos.offset(direction),newState.with(HORIZONTAL_FACING, direction.getOpposite()));
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		int flowerCount = state.get(FLOWER_AMOUNT);
		this.tickGrowth(state, world, pos, random, flowerCount);

		world.getOtherEntities(null, new Box(pos)).forEach(o -> {
			tickStillness(o, flowerCount);
		});
	}

	private void tickStillness(Entity entity, int flowerCount) {
		if (!(entity instanceof PlayerEntity playerEntity)) {
			return;
		}
		playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.MOISTURIZED, 600 * flowerCount));
	}

	public static boolean growsInBiome(BiomeSelectionContext context) {
		return !(
				context.hasTag(BiomeTags.IS_RIVER) ||
				context.hasTag(BiomeTags.IS_OCEAN) ||
				context.hasTag(BiomeTags.IS_DEEP_OCEAN) ||
				context.hasTag(BiomeTags.IS_BEACH)
		);
	}

	@Override
	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		FluidState fluidState = world.getFluidState(pos);
		FluidState fluidState2 = world.getFluidState(pos.up());
		return (fluidState.getFluid() == Fluids.WATER || floor.getBlock() instanceof IceBlock) && fluidState2.getFluid() == Fluids.EMPTY;
	}
}

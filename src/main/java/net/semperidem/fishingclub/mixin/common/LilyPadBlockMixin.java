package net.semperidem.fishingclub.mixin.common;


import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.semperidem.fishingclub.registry.Blocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.semperidem.fishingclub.block.WaterloggedLilyPadBlock.FLOWERING;

@Mixin(LilyPadBlock.class)
public class LilyPadBlockMixin extends PlantBlock {
	@Unique
	private final static int MAX_AGE = 50;
	@Unique
	private final static int FLOWERING_AGE = 40;
	@Unique
	private final static int MIN_AGE = 0;
	@Unique
	private final static IntProperty AGE = IntProperty.of("age", MIN_AGE, MAX_AGE);

	protected LilyPadBlockMixin(Settings settings) {
		super(settings);
	}

	@Inject(method="<init>", at = @At("TAIL"))
	private void onInit(Settings settings, CallbackInfo ci) {
		this.setDefaultState(
			this.stateManager.getDefaultState()
				.with(FLOWERING, false)
				.with(AGE, MIN_AGE));
	}

	@Shadow
	public MapCodec<? extends PlantBlock> getCodec() {
		return LilyPadBlock.CODEC;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(FLOWERING, AGE);
	}


	@Override
	protected boolean hasRandomTicks(BlockState state) {
		return true;
	}

	@Override
	protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		this.grow(state, world, pos, 1);
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (state.get(FLOWERING)) {
			dropStack(world, pos, new ItemStack(Items.LILY_PAD));
		}
		return super.onBreak(world, pos, state, player);
	}

	@Unique
	private void grow(BlockState state, ServerWorld world, BlockPos pos, int growAmount) {

		int age = state.get(AGE);
		int newAge =  MathHelper.clamp(age + growAmount, MIN_AGE, MAX_AGE);
		if (newAge == MAX_AGE) {
			world.setBlockState(pos, state.with(FLOWERING, false).with(AGE, MIN_AGE), 2);
			return;
		}
		world.setBlockState(pos, state.with(FLOWERING, newAge >= FLOWERING_AGE).with(AGE, newAge), 2);
	}

	@Override
	protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

		if (stack.isOf(Items.BONE_MEAL) && !state.get(FLOWERING)) {
			if (world instanceof ServerWorld serverWorld) {
				this.grow(state, serverWorld, pos, (int) (Math.random() * 5));
			}
			if (!player.isCreative()) {
				stack.decrement(1);
			}
			return ItemActionResult.SUCCESS;
		}

		if (player.getMainHandStack().isEmpty() && state.get(FLOWERING)) {
			ItemStack lilyStack = new ItemStack(Items.LILY_PAD);
			lilyStack.setCount((int) (1 + Math.random() * 3));
			player.giveItemStack(lilyStack);
			world.setBlockState(pos, state.with(FLOWERING, false), 2);
			return ItemActionResult.SUCCESS;
		}

		if (!world.getBlockState(pos.down()).isOf(net.minecraft.block.Blocks.WATER)) {
			return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
		}

		if (!(stack.getItem() instanceof BlockItem blockItem)) {
			return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
		}

		world.setBlockState(pos, blockItem.getBlock().getDefaultState());
		world.setBlockState(pos.down(), Blocks.WATERLOGGED_LILY_PAD_BLOCK.getDefaultState().with(FLOWERING, state.get(FLOWERING)));

		return ItemActionResult.SUCCESS;
	}

}

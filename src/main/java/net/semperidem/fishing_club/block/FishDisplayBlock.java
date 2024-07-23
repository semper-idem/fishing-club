package net.semperidem.fishing_club.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.semperidem.fishing_club.entity.FishDisplayBlockEntity;
import net.semperidem.fishing_club.fish.FishComponent;
import net.semperidem.fishing_club.fish.FishUtil;
import net.semperidem.fishing_club.registry.FCBlocks;
import net.semperidem.fishing_club.registry.FCComponents;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FishDisplayBlock extends BlockWithEntity {
    private final WoodType type;
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public static final MapCodec<WallSignBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(WoodType.CODEC.fieldOf("wood_type").forGetter(FishDisplayBlock::getWoodType), createSettingsCodec())
        .apply(instance, WallSignBlock::new)
    );

    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE = Maps.newEnumMap(
      ImmutableMap.of(
        Direction.NORTH,
        Block.createCuboidShape(0.0, 4.5, 14.0, 16.0, 12.5, 16.0),
        Direction.SOUTH,
        Block.createCuboidShape(0.0, 4.5, 0.0, 16.0, 12.5, 2.0),
        Direction.EAST,
        Block.createCuboidShape(0.0, 4.5, 0.0, 2.0, 12.5, 16.0),
        Direction.WEST,
        Block.createCuboidShape(14.0, 4.5, 0.0, 16.0, 12.5, 16.0)
      )
    );

    public FishDisplayBlock(WoodType woodType, Settings settings) {
        super(settings);
        this.type = woodType;
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }



    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = this.getDefaultState();
        WorldView worldView = ctx.getWorld();
        BlockPos blockPos = ctx.getBlockPos();
        Direction[] directions = ctx.getPlacementDirections();

        for (Direction direction : directions) {
            if (direction.getAxis().isHorizontal()) {
                Direction direction2 = direction.getOpposite();
                blockState = blockState.with(FACING, direction2);
                if (blockState.canPlaceAt(worldView, blockPos)) {
                    return blockState;
                }
            }
        }

        return null;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return (VoxelShape)FACING_TO_SHAPE.get(state.get(FACING));
    }


    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!(world.getBlockEntity(pos) instanceof FishDisplayBlockEntity displayBlockEntity)) {
            return super.onBreak(world, pos, state, player);
        }
        if (!(world instanceof ServerWorld serverWorld)) {
            return super.onBreak(world, pos, state, player);
        }

        FishComponent fishComponent = FishComponent.FISH_COMPONENT.get(displayBlockEntity);
        Box soundBox = new Box(pos);
        soundBox.expand(16);
        if (fishComponent.record() == null) {
            return super.onBreak(world, pos, state, player);
        }

        if (fishComponent.record() != null) {
            displayBlockEntity.stopPlaying();
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, FishUtil.getStackFromFish(fishComponent.record())));
            return super.onBreak(world, pos, state, player);
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
        return world.getBlockState(pos.offset(((Direction)state.get(FACING)).getOpposite())).isSolid();
    }

    @Override
    protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        if (!FishUtil.isFish(stack)) {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!(world.getBlockEntity(pos) instanceof FishDisplayBlockEntity displayBlockEntity)) {
            return ItemActionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (displayBlockEntity.getFishEntity() != null) {
            return ItemActionResult.CONSUME;
        }
        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return ItemActionResult.CONSUME;
        }
        FishComponent.FISH_COMPONENT.get(displayBlockEntity).set(stack.get(FCComponents.FISH));
        stack.decrement(1);
        return ItemActionResult.CONSUME;
    }


    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        if (!(world.getBlockEntity(pos) instanceof FishDisplayBlockEntity displayBlockEntity)) {
            return ActionResult.CONSUME;
        }
        FishComponent fishComponent = FishComponent.FISH_COMPONENT.get(displayBlockEntity);
        if (fishComponent.record() == null) {
            return super.onUse(state, world, pos, player, hit);
        }
        if (player.isSneaking() && fishComponent.record() != null) {
            player.giveItemStack(FishUtil.getStackFromFish(fishComponent.record()));
            displayBlockEntity.stopPlaying();
            fishComponent.set(null);
            return ActionResult.CONSUME;
        }
        displayBlockEntity.startPlaying();
        return ActionResult.CONSUME;
    }

    public WoodType getWoodType() {
        return this.type;
    }

    public static WoodType getWoodType(Block block) {
        WoodType woodType;
        if (block instanceof FishDisplayBlock) {
            woodType = ((FishDisplayBlock)block).getWoodType();
        } else {
            woodType = WoodType.OAK;
        }

        return woodType;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return validateTicker(type, FCBlocks.FISH_DISPLAY, FishDisplayBlockEntity::tick);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new FishDisplayBlockEntity(pos, state);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
      BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        return direction.getOpposite() == state.get(FACING) && !state.canPlaceAt(world, pos)
               ? Blocks.AIR.getDefaultState()
               : super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    protected BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    public float getRotationDegrees(BlockState state) {
        return state.get(FACING).asRotation();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

}

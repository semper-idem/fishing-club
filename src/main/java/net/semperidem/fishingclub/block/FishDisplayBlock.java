package net.semperidem.fishingclub.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.semperidem.fishingclub.entity.FishDisplayBlockEntity;
import net.semperidem.fishingclub.registry.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class FishDisplayBlock extends BlockWithEntity {
    private final WoodType type;
    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;

    public static final MapCodec<WallSignBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(
        WoodType.CODEC.fieldOf("wood_type").forGetter(FishDisplayBlock::getWoodType),
          createSettingsCodec()
        ).apply(instance, WallSignBlock::new)
    );

    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE = Maps.newEnumMap(
      ImmutableMap.of(
        Direction.NORTH, Block.createCuboidShape(0.0, 4.5, 14.0, 16.0, 12.5, 16.0),
        Direction.SOUTH, Block.createCuboidShape(0.0, 4.5, 0.0, 16.0, 12.5, 2.0),
        Direction.EAST, Block.createCuboidShape(0.0, 4.5, 0.0, 2.0, 12.5, 16.0),
        Direction.WEST, Block.createCuboidShape(14.0, 4.5, 0.0, 16.0, 12.5, 16.0)
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

        for (Direction direction : ctx.getPlacementDirections()) {

            if (!direction.getAxis().isHorizontal()) {
                continue;
            }

            blockState = blockState.with(FACING, direction.getOpposite());
            if (blockState.canPlaceAt(ctx.getWorld(), ctx.getBlockPos())) {
                return blockState;
            }
        }

        return null;
    }

    @Override
    protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {

        return FACING_TO_SHAPE.get(state.get(FACING));
    }

    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        if (world.getBlockEntity(pos) instanceof FishDisplayBlockEntity displayBlockEntity) {
            displayBlockEntity.drop();
        }

        super.onStateReplaced(state, world, pos, moved);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {

        if (world.getBlockEntity(pos) instanceof FishDisplayBlockEntity displayBlockEntity) {
            displayBlockEntity.drop();
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {

        return world.getBlockState(pos.offset(state.get(FACING).getOpposite())).isSolidBlock(world, pos);
    }


    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {

        if (!(world.getBlockEntity(pos) instanceof FishDisplayBlockEntity fishDisplayEntity)) {
            return ActionResult.PASS;
        }

        return fishDisplayEntity.use(player, player.getMainHandStack()) ? ActionResult.CONSUME : ActionResult.PASS;
    }

    public WoodType getWoodType() {

        return this.type;
    }

    public static WoodType getWoodType(Block block) {

        return block instanceof FishDisplayBlock displayBlock ? displayBlock.getWoodType() : WoodType.OAK;
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {

        return CODEC;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {

        return validateTicker(type, Blocks.FISH_DISPLAY, FishDisplayBlockEntity::tick);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {

        return new FishDisplayBlockEntity(pos, state);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {
        if (direction.getOpposite() != state.get(FACING)) {
            return state;
        }

        if (state.canPlaceAt(world, pos)) {
            return state;
        }

        return net.minecraft.block.Blocks.AIR.getDefaultState();
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

        return state.get(FACING).getRotationQuaternion().angle();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {

        builder.add(FACING);
    }

}

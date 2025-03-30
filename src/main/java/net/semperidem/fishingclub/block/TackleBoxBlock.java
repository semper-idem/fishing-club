package net.semperidem.fishingclub.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootWorldContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.semperidem.fishingclub.entity.TackleBoxBlockEntity;
import net.semperidem.fishingclub.registry.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static net.minecraft.block.ShulkerBoxBlock.getItemStack;

public class TackleBoxBlock extends BlockWithEntity implements BlockEntityProvider {
    public static final Identifier CONTENTS_DYNAMIC_DROP_ID = Identifier.ofVanilla("contents");
    public static final MapCodec<TackleBoxBlock> CODEC = RecordCodecBuilder.mapCodec(
      instance -> instance.group(createSettingsCodec())
        .apply(instance, TackleBoxBlock::new)
    );

    public TackleBoxBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else if (player.isSpectator()) {
            return ActionResult.CONSUME;
        } else if (world.getBlockEntity(pos) instanceof TackleBoxBlockEntity shulkerBoxBlockEntity) {
                player.openHandledScreen(shulkerBoxBlockEntity);
                player.incrementStat(Stats.OPEN_SHULKER_BOX);

            return ActionResult.CONSUME;
        } else {
            return ActionResult.PASS;
        }
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof TackleBoxBlockEntity shulkerBoxBlockEntity) {
            if (!world.isClient && player.isCreative() && !shulkerBoxBlockEntity.isEmpty()) {
                ItemStack itemStack = new ItemStack(Blocks.TACKLE_BOX_BLOCK);
                itemStack.applyComponentsFrom(blockEntity.createComponentMap());
                ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5, (double)pos.getY() + 0.5, (double)pos.getZ() + 0.5, itemStack);
                itemEntity.setToDefaultPickupDelay();
                world.spawnEntity(itemEntity);
            } else {
                shulkerBoxBlockEntity.generateLoot(player);
            }
        }

        return super.onBreak(world, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDroppedStacks(BlockState state, LootWorldContext.Builder builder) {
        BlockEntity be = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (be instanceof TackleBoxBlockEntity tbbe) {
            builder = builder.addDynamicDrop(CONTENTS_DYNAMIC_DROP_ID, lootConsumer -> {
                for (int i = 0; i < tbbe.size(); i++) {
                    lootConsumer.accept(tbbe.getStack(i));
                }
            });
        }

        return super.getDroppedStacks(state, builder);
    }


    @Override
    protected ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData) {
        return super.getPickStack(world, pos, state, includeData);
    }



    @Override
    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        ItemScatterer.onStateReplaced(state, world, pos);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TackleBoxBlockEntity(pos, state);
    }

}

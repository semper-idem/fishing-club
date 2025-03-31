package net.semperidem.fishingclub.fish;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.world.ChunkQuality;


public abstract class AbstractFishEntity extends FishEntity {

    public AbstractFishEntity(EntityType<? extends FishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onRemoval(ServerWorld world, RemovalReason reason) {
        ChunkQuality.influence(this, ChunkQuality.PlayerInfluence.FISH_CAUGHT);
        super.onRemoval(world, reason);
    }

    public static<T extends WaterCreatureEntity> boolean canSpawn(Species<T> species, EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        if (!ChunkQuality.isAboveMinimumQuality(species, world, pos)) {
            return false;
        }
        int seaLevel = world.getSeaLevel();
        int x = seaLevel - 13;
        if (pos.getY() > seaLevel) {
            return false;
        }
        if (pos.getY() < x) {
            return false;
        }
        if (!world.getFluidState(pos.down()).isIn(FluidTags.WATER)) {
            return false;
        }
        return world.getBlockState(pos.up()).isOf(Blocks.WATER);

    }
}

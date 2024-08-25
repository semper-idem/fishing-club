package net.semperidem.fishingclub.fish;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.semperidem.fishingclub.world.ChunkQuality;

import static net.semperidem.fishingclub.world.ChunkQuality.CHUNK_QUALITY;

public abstract class AbstractFishEntity extends SchoolingFishEntity {

    public AbstractFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onRemoval(RemovalReason reason) {
        CHUNK_QUALITY.get(this.getWorld().getChunk(this.getBlockPos())).influence(ChunkQuality.PlayerInfluence.FISH_CAUGHT);
        super.onRemoval(reason);
    }


    public static boolean canSpawn(Species<? extends AbstractFishEntity> species, EntityType<? extends WaterCreatureEntity> type, WorldAccess world, SpawnReason reason, BlockPos pos, Random random) {
        double chunkQuality = CHUNK_QUALITY.get(world.getChunk(pos)).getValue();
        double speciesMinChunkQuality = Math.log(species.rarity) / 2;
        return WaterCreatureEntity.canSpawn(type, world, reason, pos, random) && (chunkQuality > speciesMinChunkQuality);
    }
}

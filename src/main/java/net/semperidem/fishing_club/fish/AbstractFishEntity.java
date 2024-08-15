package net.semperidem.fishing_club.fish;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.world.World;
import net.semperidem.fishing_club.world.ChunkQuality;

import static net.semperidem.fishing_club.world.ChunkQuality.CHUNK_QUALITY;

public abstract class AbstractFishEntity extends SchoolingFishEntity {

    public AbstractFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onRemoval(RemovalReason reason) {
        CHUNK_QUALITY.get(this.getWorld().getChunk(this.getBlockPos())).influence(ChunkQuality.PlayerInfluence.FISH_CAUGHT);
        super.onRemoval(reason);
    }
}

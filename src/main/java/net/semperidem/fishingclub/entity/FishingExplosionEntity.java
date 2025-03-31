package net.semperidem.fishingclub.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;

public class FishingExplosionEntity implements IHookEntity{
    PlayerEntity causingEntity;
    ChunkPos explosionChunkPos;
    public FishingExplosionEntity(PlayerEntity causingEntity, ChunkPos explosionChunkPos) {
        this.causingEntity = causingEntity;
        this.explosionChunkPos = explosionChunkPos;
    }
    @Override
    public Card getCard() {
        return Card.of(causingEntity);
    }

    @Override
    public RodConfiguration getCaughtUsing() {
        return RodConfiguration.getDefault();
    }

    @Override
    public float getCircumstanceQuality() {
        return 0.35f;
    }

    @Override
    public ChunkPos getChunkPos() {
        return explosionChunkPos;
    }
}

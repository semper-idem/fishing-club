package net.semperidem.fishing_club.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.ChunkPos;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.item.fishing_rod.components.RodConfiguration;

public class FishingExplosionEntity implements IHookEntity{
    PlayerEntity causingEntity;

    public FishingExplosionEntity(PlayerEntity causingEntity) {
        this.causingEntity = causingEntity;
    }
    @Override
    public FishingCard getFishingCard() {
        return FishingCard.of(causingEntity);
    }

    @Override
    public RodConfiguration getCaughtUsing() {
        return RodConfiguration.getDefault();
    }

    @Override
    public float getCircumstanceQuality() {
        return 0.35f;
    }
}

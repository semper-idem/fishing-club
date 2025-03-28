package net.semperidem.fishingclub.entity;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;

public class FishingExplosionEntity implements IHookEntity{
    PlayerEntity causingEntity;

    public FishingExplosionEntity(PlayerEntity causingEntity) {
        this.causingEntity = causingEntity;
    }
    @Override
    public Card getFishingCard() {
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
}

package net.semperidem.fishingclub.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.world.World;

public class FishermanEntity extends VillagerEntity {
    public FishermanEntity(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
    }
}

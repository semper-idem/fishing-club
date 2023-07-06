package net.semperidem.fishingclub.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.world.World;


public class CustomFishingBobberEntity extends FishingBobberEntity {
    public CustomFishingBobberEntity(EntityType<? extends FishingBobberEntity> entityType, World world, int i, int j) {
        super(entityType, world, i, j);
    }

    /* TODO
    *   - buff rain bonus if skill present
    *   - activate mini game instead of normal catch
    *   - adjust for fishing rod catch rate stat
    *   - different texture per bobber part (luxury feature)
    *   - remove "fish coming" indicator
    *   - lengthen "catch" window
    *   - bigger fish pull bobber with greater force
    *   -
    * */
}

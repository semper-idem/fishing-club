package net.semperidem.fishing_club.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.fish.SpeciesLibrary;

public class FCFishEntity extends SchoolingFishEntity {
    public final FishRecord fishRecord;

    public FCFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world, FishRecord fishRecord) {
        super(entityType, world);
        this.fishRecord = fishRecord;
    }

    public FCFishEntity(EntityType<? extends FCFishEntity> entityType, World world) {
        super(entityType, world);
        this.fishRecord = FishRecord.create(SpeciesLibrary.BUTTERFISH);
    }


    @Override
    public ItemStack getBucketItem() {
        return new ItemStack(Items.COD_BUCKET);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_COD_AMBIENT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_COD_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_COD_HURT;
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.ENTITY_COD_FLOP;
    }

}

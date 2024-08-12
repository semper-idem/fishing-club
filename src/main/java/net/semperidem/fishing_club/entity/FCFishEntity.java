package net.semperidem.fishing_club.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import net.semperidem.fishing_club.fish.FishComponent;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.registry.FCEntityTypes;
import net.semperidem.fishing_club.world.ChunkQuality;

import static net.semperidem.fishing_club.world.ChunkQuality.CHUNK_QUALITY;

public class FCFishEntity extends SchoolingFishEntity {

    public FCFishEntity(EntityType<? extends SchoolingFishEntity> entityType, World world) {
       super(entityType, world);
       FishComponent.of(this).set(FishRecord.init());
    }

    public FCFishEntity(World world) {
        super(FCEntityTypes.FISH_ENTITY, world);
    }
    public FCFishEntity(World world, FishRecord fishRecord) {
        super(FCEntityTypes.FISH_ENTITY, world);
        FishComponent.of(this).set(fishRecord);
    }

    @Override
    protected void onRemoval(RemovalReason reason) {
        CHUNK_QUALITY.get(this.getWorld().getChunk(this.getBlockPos())).influence(ChunkQuality.PlayerInfluence.FISH_CAUGHT);
        super.onRemoval(reason);
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

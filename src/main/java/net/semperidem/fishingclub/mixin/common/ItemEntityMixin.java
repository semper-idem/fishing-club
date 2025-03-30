package net.semperidem.fishingclub.mixin.common;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.Items;
import net.semperidem.fishingclub.world.ChunkQuality;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.UUID;

import static net.semperidem.fishingclub.world.ChunkQuality.CHUNK_QUALITY;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity{
    @Shadow private @Nullable Entity thrower;
    @Shadow private int itemAge;
    @Unique UUID summonerUUID;
    @Unique boolean isSummonItem;
    @Unique
    SpecimenData fish;
    @Unique int escapeAge;
    @Unique float maxFallDistance;

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    private void onInit(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {

        if (stack.isOf(Items.GOLD_FISH)) {
            summonerUUID = stack.getOrDefault(Components.CAUGHT_BY, UUID.randomUUID());
            isSummonItem = true;
            return;
        }

        if (!FishUtil.isFish(stack)) {
            return;
        }

        this.fish = stack.get(Components.SPECIMEN);

        if (fish != null && fish.quality() >= 4) {
            summonerUUID = fish.caughtByUUID();
            isSummonItem = true;
            return;
        }

        this.escapeAge = (int) (20 + 80 * Math.abs(new Random(this.getId()).nextGaussian()));
    }


    @Unique protected void produceParticles(ServerWorld serverWorld, ParticleEffect parameters) {
        for (int i = 0; i < 5; i++) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            serverWorld.spawnParticles(parameters, this.getParticleX(this.getWidth()), this.getRandomBodyY() + 1.0, this.getParticleZ(this.getWidth()), 1, d, e, f, 1);
        }
    }

    @Unique private void validateAndTrash() {
        if (this.fallDistance > this.maxFallDistance) {
            this.maxFallDistance = (float) fallDistance;
        }
        if (!isOnGround()) {
            return;
        }
        if (this.maxFallDistance < 2) {
            return;
        }
        if (this.thrower == null) {
            return;
        }
        if (this.supportingBlockPos.isEmpty()) {
            return;
        }
        if (this.getWorld().getFluidState(this.supportingBlockPos.get().up()).getHeight() > 0) {
            return;
        }
        if (this.getWorld().getBlockState(this.supportingBlockPos.get()).getHardness(null ,null) < 1.5f) {
            return;
        }

        if (!(this.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        this.fish = null;
        this.setStack(this.getStack().getItem().getDefaultStack());
        this.playSound(SoundEvents.ENTITY_TROPICAL_FISH_FLOP, 0.2F, 0.2F + serverWorld.random.nextFloat() * 0.4F);
        this.produceParticles(serverWorld, ParticleTypes.SCRAPE);
    }

    @Inject(method = "tick", at = @At("HEAD"))
        private void onTick(CallbackInfo ci) {
        if (this.getWorld().isClient) {
            return;
        }
        if (this.fish == null) {
            return;
        }

        validateAndTrash();
        validateAndSummonDerek();
    }

    @Unique
    private void validateAndSummonDerek() {
        if (!this.isSubmergedInWater()) {
            return;
        }

        if (this.itemAge < this.escapeAge) {
            return;
        }

        if (!this.isSummonItem && this.fish.isAlive()) {
            this.spawnFishEntity();
            this.discard();
            return;
        }

        if (!this.isSummonItem) {
            return;
        }
        if (this.itemAge < 100) {//todo configure properly, bigger number
            return;
        }

//TODO uncomment/add biome condition
//        if (!(world.getBiome(getBlockPos()).isIn(BiomeTags.IS_OCEAN) || world.getBiome(getBlockPos()).isIn(BiomeTags.IS_RIVER))) {
//            return;
//        }

        this.summonDerek();
    }

    @Unique
    private void spawnFishEntity() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
//todo move to SpecimenData like asItemStack
        EntityType<?> entityType = this.fish.species().getEntityType();

        WaterCreatureEntity fishEntity = (WaterCreatureEntity) entityType.create(serverWorld, SpawnReason.BUCKET);
        if (fishEntity == null) {
            return;
        }
        if (entityType.getUntranslatedName().equals("tropical_fish")) {
            NbtCompound nbtCompound = new NbtCompound();
            fishEntity.writeCustomDataToNbt(nbtCompound);
            nbtCompound.putInt("Variant", TropicalFishEntity.COMMON_VARIANTS.get(this.fish.subspecies()).getId());
            fishEntity.readCustomDataFromNbt(nbtCompound);
        }
        SpecimenComponent.of(fishEntity).set(this.fish);
        fishEntity.setPosition(Vec3d.of(this.getBlockPos()).add(0.5f,0.5f,0.5f));
        serverWorld.spawnEntity(fishEntity);
//        fishEntity.setCustomName(Text.of(this.fish.label()));
        fishEntity.damage((ServerWorld) getWorld(), this.getWorld().getDamageSources().playerAttack((PlayerEntity) thrower), 0.1f);
        CHUNK_QUALITY.get(serverWorld.getChunk(this.getBlockPos())).influence(ChunkQuality.PlayerInfluence.FISH_RELEASE);
    }

    @Unique
    private void summonDerek() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        FishermanEntity.summonDerek(getPos(), serverWorld, getStack(), summonerUUID);
        discard();
    }


    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Shadow public abstract ItemStack getStack();
    @Shadow public abstract void setStack(ItemStack stack);
}

package net.semperidem.fishing_club.mixin.common;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishing_club.entity.FCFishEntity;
import net.semperidem.fishing_club.entity.FishermanEntity;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.fish.FishUtil;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCEntityTypes;
import net.semperidem.fishing_club.registry.FCItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity{
    @Shadow private @Nullable Entity thrower;
    @Shadow private int itemAge;
    @Unique UUID summonerUUID;
    @Unique boolean isSummonItem;
    @Unique FishRecord fish;
    @Unique int escapeAge;
    @Unique float maxFallDistance;

    @Inject(method = "<init>(Lnet/minecraft/world/World;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
    private void onInit(World world, double x, double y, double z, ItemStack stack, CallbackInfo ci) {

        if (stack.isOf(FCItems.GOLD_FISH)) {
            summonerUUID = stack.getOrDefault(FCComponents.CAUGHT_BY, UUID.randomUUID());
            isSummonItem = true;
            return;
        }

        if (!stack.isOf(FishUtil.FISH_ITEM)) {
            return;
        }

        this.fish = stack.get(FCComponents.FISH);

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
            this.maxFallDistance = fallDistance;
        }
        if (!isOnGround()) {
            return;
        }
        if (this.isSubmergedInWater()) {
            return;
        }
        if (this.maxFallDistance < 2) {
            return;
        }
        if (this.supportingBlockPos.isEmpty()) {
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
        if (!isSubmergedInWater()) {
            return;
        }

        if (itemAge < escapeAge) {
            return;
        }

        if (!isSummonItem) {
            spawnFishEntity();
            this.discard();
            return;
        }

        if (itemAge < 100) {//todo configure properly, bigger number
            return;
        }

//TODO uncomment/add biome condition
//        if (!(world.getBiome(getBlockPos()).isIn(BiomeTags.IS_OCEAN) || world.getBiome(getBlockPos()).isIn(BiomeTags.IS_RIVER))) {
//            return;
//        }

        summonDerek();
    }

    @Unique
    private void spawnFishEntity() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        FCFishEntity fishEntity = new FCFishEntity(FCEntityTypes.FISH_ENTITY, serverWorld, this.fish);
        fishEntity.setPosition(Vec3d.of(this.getBlockPos()).add(0.5f,0.5f,0.5f));
        serverWorld.spawnEntity(fishEntity);
        fishEntity.setCustomName(Text.of(this.fish.name()));
        fishEntity.damage(this.getWorld().getDamageSources().playerAttack((PlayerEntity) thrower), 0.1f);
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

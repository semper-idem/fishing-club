package net.semperidem.fishing_club.mixin.common;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishing_club.entity.FishermanEntity;
import net.semperidem.fishing_club.fish.FishComponent;
import net.semperidem.fishing_club.fish.FishUtil;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity{
    @Unique UUID summonerUUID;
    @Unique boolean isSummonItem;
    @Unique boolean isFishItem;
    @Unique int escapeAge;
    @Unique boolean initialized = false;

    public ItemEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow public abstract ItemStack getStack();

    @Shadow private int itemAge;

    @Shadow public abstract void setStack(ItemStack stack);

    @Shadow private @Nullable Entity thrower;

    @Override
    public void setOnGround(boolean onGround) {
        super.setOnGround(onGround);
    }

    @Unique
    private void initialize() {
        ItemStack stack = this.getStack();
        if (initialized || stack == null) {
            return;
        }
        initialized = true;
        if (stack.isOf(FCItems.GOLD_FISH)) {
            summonerUUID = stack.getOrDefault(FCComponents.CAUGHT_BY, UUID.randomUUID());
            isSummonItem = true;
            return;
        }

        if (!stack.isOf(FishUtil.FISH_ITEM)) {
            return;
        }
        FishComponent fish = stack.get(FCComponents.FISH);
        this.isFishItem = true;
        if (fish != null && fish.quality() >= 4) {
            summonerUUID = fish.caughtByUUID();
            isSummonItem = true;
            return;
        }
        this.escapeAge = (int) (20 + 80 * Math.abs(random.nextGaussian()));
    }

    @Inject(method = "tick", at = @At("HEAD"))
        private void onTick(CallbackInfo ci) {
        initialize();
        if (!this.isFishItem) {
            return;
        }
        if (isOnGround() && !this.isSubmergedInWater()) {
            if (thrower == null ) {
                return;
            }
            this.isFishItem = false;
            Vec3d pos = this.getPos();
            this.setStack(this.getStack().getItem().getDefaultStack());
            this.playSound(SoundEvents.ENTITY_TROPICAL_FISH_FLOP, 0.2F, 0.2F + this.getWorld().random.nextFloat() * 0.4F);
            if (this.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.SCRAPE,  pos.x, pos.y + 0.5, pos.z, 5,0.25,0.01,0.25,0.01);
            }
        }

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
        TropicalFishEntity fishEntity = new TropicalFishEntity(EntityType.TROPICAL_FISH, serverWorld);
        fishEntity.setPosition(this.getPos());
        serverWorld.spawnEntity(fishEntity);
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
}

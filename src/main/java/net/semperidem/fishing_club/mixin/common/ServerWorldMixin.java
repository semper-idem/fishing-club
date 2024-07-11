package net.semperidem.fishing_club.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.semperidem.fishing_club.FishingServerWorld;
import net.semperidem.fishing_club.entity.FishermanEntity;
import net.semperidem.fishing_club.entity.FishingExplosionEntity;
import net.semperidem.fishing_club.fish.FishUtil;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.fisher.perks.FishingPerks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements FishingServerWorld{
    @Unique
    private FishermanEntity derek;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
    }

    @Shadow public abstract boolean spawnEntity(Entity entity);


    @Inject(method = "createExplosion", at = @At("TAIL"))
    private void onCreateExplosion(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, World.ExplosionSourceType explosionSourceType, ParticleEffect particle, ParticleEffect emitterParticle, RegistryEntry<SoundEvent> soundEvent, CallbackInfoReturnable<Explosion> cir){
        if (!(entity instanceof TntEntity tntEntity)) {
            return;
        }
        if (!(tntEntity.getOwner() instanceof ServerPlayerEntity causingEntity)) {
            return;
        }

        BlockPos explosionPos = new BlockPos((int) x, (int) y, (int) z);
        if (!(this.isWater(explosionPos))) {
            return;
        }

        if (!FishingCard.of(causingEntity).hasPerk(FishingPerks.BOMB_FISHING)) {
            return;
        }

        int fishCount = (int) (Math.random() * power);
        FishingExplosionEntity fee = new FishingExplosionEntity(causingEntity, getWorldChunk(explosionPos).getPos());
        for(int i = 0; i < fishCount; i++) {
            FishUtil.fishCaughtAt(
                    causingEntity,
                    FishUtil.getFishOnHook(fee),
                    explosionPos
            );
        }
    }


    @Unique
    @Override
    public FishermanEntity getDerek(ItemStack summonedUsing, UUID summonedBy) {
        if (derek == null || derek.isRemoved()) {
            derek = new FishermanEntity(this, summonedUsing, summonedBy);
        }
        return derek;
    }


    @Unique
    @Override
    public FishermanEntity getDerek() {
        return derek;
    }

    @Unique
    @Override
    public void setDerek(FishermanEntity derek) {
        this.derek = derek;
    }

}

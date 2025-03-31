package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Ownable;
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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.FishingExplosionEntity;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.world.DerekServerWorld;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;
import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World implements DerekServerWorld {
    @Unique
    private FishermanEntity derek;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, registryManager, dimensionEntry, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }


    @Shadow public abstract boolean spawnEntity(Entity entity);


    @Inject(method = "createExplosion", at = @At("TAIL"))
    private void onCreateExplosion(@Nullable Entity entity, @Nullable DamageSource damageSource, @Nullable ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, ExplosionSourceType explosionSourceType, ParticleEffect smallParticle, ParticleEffect largeParticle, RegistryEntry<SoundEvent> soundEvent, CallbackInfo ci){
        if (!(entity instanceof Ownable explodingEntity)) {
            return;
        }
        if (!(explodingEntity.getOwner() instanceof ServerPlayerEntity causingEntity)) {
            return;
        }


        /*
        * Wind charges actually explode inside blocks they hit so this formula is a workaround for it
        * If it's just wind charges could just wrap if around it, but it's unknown for me atm
        * It's not usual behaviour but when ceiling is charge hit result it'll not return produce fish
        * Other way would be to check for per block in explosion radius
        * */
        BlockPos explosionPos = new BlockPos((int) x, (int)Math.copySign(Math.ceil(Math.abs(y)), y), (int) z);
        if (!(this.isWater(explosionPos))) {
            return;
        }

        if (!Card.of(causingEntity).knowsTradeSecret(TradeSecrets.BOMB_FISHING)) {
            return;
        }

        int fishCount = (int) ((Math.abs(random.nextGaussian()) + 0.5) * power);
        FishingExplosionEntity fee = new FishingExplosionEntity(causingEntity);
        for(int i = 0; i < fishCount; i++) {
            FishUtil.fishOnHook(fee).ifPresent(specimenData -> FishUtil.fishCaughtAt(
                    causingEntity,
                    specimenData,
                    explosionPos
            ));
        }
    }


    @Unique
    @Override
    public FishermanEntity getDerek(ItemStack summonedUsing, UUID summonedBy) {
        if (derek == null || derek.isRemoved()) {
            derek = new FishermanEntity(this);
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

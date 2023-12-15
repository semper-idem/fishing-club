package net.semperidem.fishingclub.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.explosion.Explosion;
import net.minecraft.world.explosion.ExplosionBehavior;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.game.fish.FishUtil;
import net.semperidem.fishingclub.game.fish.HookedFish;
import net.semperidem.fishingclub.registry.FItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin extends World {

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Shadow public abstract boolean spawnEntity(Entity entity);

    @Inject(method = "createExplosion", at = @At("TAIL"))
    private void onCreateExplosion(Entity entity, DamageSource damageSource, ExplosionBehavior behavior, double x, double y, double z, float power, boolean createFire, Explosion.DestructionType destructionType, CallbackInfoReturnable<Explosion> cir){
        if (!(entity instanceof TntEntity tntEntity)) {
            return;
        }
        if (!(tntEntity.getCausingEntity() instanceof ServerPlayerEntity igniter)) {
            return;
        }

        BlockPos explosionPos = new BlockPos(x,y,z);
        if (!isWater(explosionPos)) {
            return;
        }

        if (!FishingCardManager.getPlayerCard(igniter).hasPerk(FishingPerks.BOMB_FISHING)) {
            return;
        }
        int fishCount = (int) (Math.random() * power);
        for(int i = 0; i < fishCount; i++) {
            pickupFish(igniter, explosionPos);
        }
    }


    private void pickupFish(ServerPlayerEntity igniter, BlockPos explosionPos){
        ChunkPos chunkPos = getWorldChunk(explosionPos).getPos();
                FishingCard fishingCard = FishingCardManager.getPlayerCard(igniter).getHarpoonFisherInfo();
                FishingCard.Chunk chunk = new FishingCard.Chunk(chunkPos.x, chunkPos.z);
                HookedFish hFish = FishUtil.getFishOnHook(fishingCard, FItemRegistry.CUSTOM_FISHING_ROD.getDefaultStack(), 1, chunk).applyHarpoonMultiplier(0.35f);
                FishUtil.grantReward(igniter, hFish, igniter.getVehicle() instanceof BoatEntity, explosionPos);
    }
}

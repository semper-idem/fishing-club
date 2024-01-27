package net.semperidem.fishingclub.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
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
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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
        if (!(tntEntity.getCausingEntity() instanceof ServerPlayerEntity causingEntity)) {
            return;
        }

        BlockPos explosionPos = new BlockPos(x,y,z);
        if (!isWater(explosionPos)) {
            return;
        }

        if (!FishingCardManager.getPlayerCard(causingEntity).hasPerk(FishingPerks.BOMB_FISHING)) {
            return;
        }
        int fishCount = (int) (Math.random() * power);
        for(int i = 0; i < fishCount; i++) {
            catchFishWithTnt(causingEntity, explosionPos);
        }
    }


    @Unique
    private void catchFishWithTnt(ServerPlayerEntity causingEntity, BlockPos explosionPos){
        ChunkPos chunkPos = getWorldChunk(explosionPos).getPos();
        FishUtil.fishCaughtAt(
                causingEntity,
                FishUtil.getFishOnHook(new IHookEntity() {
                    @Override
                    public FishingCard getFishingCard() {
                        return FishingCardManager.getPlayerCard(causingEntity);
                    }

                    @Override
                    public ItemStack getCaughtUsing() {
                        return Items.TNT.getDefaultStack();
                    }

                    @Override
                    public ChunkPos getFishedInChunk() {
                        return chunkPos;
                    }
                    @Override
                    public float getFishMultiplier() {
                        return 0.35f;
                    }
                }),
                explosionPos
        );
    }
}

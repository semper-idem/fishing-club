package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.TntEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
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
import net.semperidem.fishingclub.FishingLevelProperties;
import net.semperidem.fishingclub.FishingServerWorld;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.FishingExplosionEntity;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.network.ServerPacketSender;
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
public abstract class ServerWorldMixin extends World implements FishingServerWorld {
    @Unique
    private FishermanEntity derek;

    protected ServerWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, RegistryEntry<DimensionType> dimension, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long seed, int maxChainedNeighborUpdates) {
        super(properties, registryRef, dimension, profiler, isClient, debugWorld, seed, maxChainedNeighborUpdates);
    }

    @Shadow public abstract boolean spawnEntity(Entity entity);

    @Inject(method = "onPlayerConnected", at = @At("TAIL"))
    private void onPlayerConnected(ServerPlayerEntity entity, CallbackInfo ci) {
        if (getLevelProperties() instanceof FishingLevelProperties fishingLevelProperties) {
            ServerPacketSender.sendCapeDetails( entity, fishingLevelProperties.getFishingKingUUID(), fishingLevelProperties.getFishingKingName(), fishingLevelProperties.getClaimPrice());
        }
    }

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

        if (!FishingCard.getPlayerCard(causingEntity).hasPerk(FishingPerks.BOMB_FISHING)) {
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

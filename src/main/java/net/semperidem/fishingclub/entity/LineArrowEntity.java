package net.semperidem.fishingclub.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.game.fish.FishUtil;
import net.semperidem.fishingclub.game.fish.HookedFish;
import net.semperidem.fishingclub.registry.FEntityRegistry;
import net.semperidem.fishingclub.registry.FItemRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class LineArrowEntity extends PersistentProjectileEntity {
    boolean isReturning = false;
    public LineArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    public LineArrowEntity(World world, LivingEntity shooter) {
        super(FEntityRegistry.LINE_ARROW_ENTITY, shooter, world);
        pickupType = PickupPermission.ALLOWED;
    }


    @Override
    public void tick() {
        if (isSubmergedInWater() && !isReturning) {
            if (Math.random() > 0.85) {
                discard();
            }
            isReturning = true;
        }

        Entity owner = this.getOwner();
        if (isReturning && owner != null) {
            if (!this.getOwner().isAlive()) {
                if (!this.world.isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1f);
                }
                this.discard();
            } else {
                this.setNoClip(true);
                Vec3d vec3d = owner.getEyePos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y * 0.06, this.getZ());
                if (this.world.isClient) {
                    this.lastRenderY = this.getY();
                }
                double d = 0.3;
                this.setVelocity(this.getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));
            }
        }

        super.tick();
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return this.isNoClip() && this.isOwner(player) && insertArrowStack(player);
    }

    private boolean insertArrowStack(PlayerEntity player){
        boolean result = player.getInventory().insertStack(asItemStack());
        if (result) {
            pickupFish();
        }
        return result;
    }


    @Override
    @Nullable
    protected EntityHitResult getEntityCollision(Vec3d currentPosition, Vec3d nextPosition) {
        if (this.isReturning) {
            return null;
        }
        return super.getEntityCollision(currentPosition, nextPosition);
    }

    private void pickupFish(){
        if (getOwner() != null) {
            if (getOwner() instanceof ServerPlayerEntity owner) {
                FishingCard fishingCard = FishingCardManager.getPlayerCard(owner).getHarpoonFisherInfo();
                FishingCard.Chunk chunk = new FishingCard.Chunk(getChunkPos().x, getChunkPos().z);
                double range = MathHelper.square(owner.getBlockPos().getSquaredDistance(getPos()));
                HookedFish harpoonedFish = FishUtil.getFishOnHook(fishingCard, FItemRegistry.CUSTOM_FISHING_ROD.getDefaultStack(), 1, chunk)
                        .applyHarpoonMultiplier((float)(1 -  Math.min(0.75, Math.max(0.25, range / 128f + 0.25))));
                FishUtil.grantReward(owner, harpoonedFish, new ArrayList<>());
            }
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return FItemRegistry.LINE_ARROW.getDefaultStack();
    }
}

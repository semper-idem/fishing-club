package net.semperidem.fishingclub.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.registry.FCItems;
import org.jetbrains.annotations.Nullable;

public class LineArrowEntity extends PersistentProjectileEntity implements IHookEntity{
    boolean isReturning = false;
    private double range = 0;
    public LineArrowEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
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
                if (!this.getWorld().isClient && this.pickupType == PersistentProjectileEntity.PickupPermission.ALLOWED) {
                    this.dropStack(this.asItemStack(), 0.1f);
                }
                this.discard();
            } else {
                this.setNoClip(true);
                Vec3d vec3d = owner.getEyePos().subtract(this.getPos());
                this.setPos(this.getX(), this.getY() + vec3d.y * 0.06, this.getZ());
                if (this.getWorld().isClient) {
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
                range = MathHelper.square(owner.getBlockPos().getSquaredDistance(getPos()));
             //   FishUtil.fishCaught(owner, FishUtil.getFishOnHook(this));
            }
        }
    }

    @Override
    protected ItemStack asItemStack() {
        return FCItems.LINE_ARROW.getDefaultStack();
    }

    @Override
    protected ItemStack getDefaultItemStack() {
        return this.getItemStack();
    }

    @Override
    public FishingCard getFishingCard() {
        return FishingCard.of((PlayerEntity) getOwner());
    }

    @Override
    public RodConfiguration getCaughtUsing() {
        return RodConfiguration.getDefault();
    }

    @Override
    public ChunkPos getChunkPos() {
        return getChunkPos();
    }

    @Override
    public float getCircumstanceQuality() {
        return (float) (1 - MathHelper.clamp(range * 0.0078125f + 0.25, 0.25f, 0.75f)); //Between 0.25 and 0.75 based no range(Max at 64blocks)
    }
}

package net.semperidem.fishingclub.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.EntityTypeRegistry;

public class HarpoonEntity extends TridentEntity implements IHookEntity{
    boolean dealtDamage;
    ItemStack harpoonStack;
    double range = 0;


    public HarpoonEntity(EntityType<? extends TridentEntity> entityType, World world) {
        super(EntityTypeRegistry.HARPOON_ENTITY, world);
    }


    public HarpoonEntity(World world, LivingEntity owner, ItemStack stack) {
        super(EntityTypeRegistry.HARPOON_ENTITY, world);
        this.setOwner(owner);
        harpoonStack = stack;
        this.setPosition(owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ());
    }

    @Override
    protected boolean tryPickup(PlayerEntity player) {
        return this.isNoClip() && this.isOwner(player) && insertHarpoonStack(player);
    }

    private boolean insertHarpoonStack(PlayerEntity player){
        boolean result = player.getInventory().insertStack(harpoonStack);
        if (result) {
            pickupFish();
        }
        return result;
    }
    private void pickupFish(){
        if (getOwner() != null) {
            if (getOwner() instanceof ServerPlayerEntity owner) {
                range = MathHelper.square(owner.getBlockPos().getSquaredDistance(getPos()));
                FishUtil.fishCaught(owner, FishUtil.getFishOnHook(this));
            }
        }
    }

    @Override
    public void tick() {
        Entity owner = this.getOwner();

        if (this.inGroundTime > 4) {
            this.dealtDamage = true;
        }

        if ((this.dealtDamage || this.isNoClip() || isTouchingWater()) && owner != null) {
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
                if (this.returnTimer == 0) {
                    this.playSound(SoundEvents.ITEM_TRIDENT_RETURN, 10.0f, 1.0f);
                }
                ++this.returnTimer;
            }
        }
        super.tick();
    }

    @Override
    public FishingCard getFishingCard() {
        return FishingCard.of((PlayerEntity) getOwner());
    }

    @Override
    public ItemStack getCaughtUsing() {
        return harpoonStack;
    }

    @Override
    public ChunkPos getFishedInChunk() {
        return getChunkPos();
    }

    @Override
    public float getFishMethodDebuff() {
        return (float) (1 - MathHelper.clamp(range * 0.0078125f + 0.25, 0.25f, 0.75f)); //Between 0.25 and 0.75 based no range(Max at 64blocks)
    }
}

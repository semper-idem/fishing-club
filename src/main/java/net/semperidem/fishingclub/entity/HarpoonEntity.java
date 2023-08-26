package net.semperidem.fishingclub.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfoManager;
import net.semperidem.fishingclub.registry.FEntityRegistry;
import net.semperidem.fishingclub.registry.FItemRegistry;

public class HarpoonEntity extends TridentEntity {
    boolean dealtDamage;
    ItemStack harpoonStack;

    public HarpoonEntity(EntityType<? extends TridentEntity> entityType, World world) {
        super(FEntityRegistry.HARPOON_ENTITY, world);
    }


    public HarpoonEntity(World world, LivingEntity owner, ItemStack stack) {
        super(FEntityRegistry.HARPOON_ENTITY, world);
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
                FisherInfo fisherInfo = FisherInfoManager.getFisher(owner).getHarpoonFisherInfo();
                FisherInfo.Chunk chunk = new FisherInfo.Chunk(getChunkPos().x, getChunkPos().z);
                double range = MathHelper.square(owner.getBlockPos().getSquaredDistance(getPos()));
                Fish hFish = FishUtil.getFishOnHook(fisherInfo, FItemRegistry.CUSTOM_FISHING_ROD.getDefaultStack(), 1, chunk)
                        .getHarpoonFish((float)(1 -  Math.min(0.75, Math.max(0.25, range / 128f + 0.25))));
                FishUtil.grantReward(owner, hFish, owner.getVehicle() instanceof BoatEntity);
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
}

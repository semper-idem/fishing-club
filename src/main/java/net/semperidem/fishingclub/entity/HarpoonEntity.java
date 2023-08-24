package net.semperidem.fishingclub.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.semperidem.fishingclub.registry.FEntityRegistry;

public class HarpoonEntity extends TridentEntity {
    public HarpoonEntity(EntityType<? extends TridentEntity> entityType, World world) {
        super(entityType, world);
    }


    public HarpoonEntity(World world, LivingEntity owner, ItemStack stack) {
        super(FEntityRegistry.HARPOON_ENTITY, world);
        this.setOwner(owner);
        if (owner instanceof PlayerEntity) {
            this.pickupType = PickupPermission.DISALLOWED;
        }
        this.setPosition(owner.getX(), owner.getEyeY() - (double)0.1f, owner.getZ());
    }


}

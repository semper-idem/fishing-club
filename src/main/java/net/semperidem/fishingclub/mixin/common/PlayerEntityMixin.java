package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.HookEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    @Inject(method = "tickMovement", at = @At("RETURN"))
    private void afterTickMovement(CallbackInfo ci) {

        if (!(this.fishHook instanceof HookEntity hookEntity)) {
            return;
        }

        this.onLanding();
        Vec3d lineVector = hookEntity.getPos().subtract(this.getEyePos());
        double currentLineLength = lineVector.length();

        if (currentLineLength < hookEntity.getLineLength()) {
            return;
        }

        double tensionForce = currentLineLength / hookEntity.getLineLength() * 0.1;
        Vec3d appliedMotion = lineVector.multiply(1.0 / currentLineLength).multiply(tensionForce, tensionForce * 1.2, tensionForce);
        this.addVelocity(appliedMotion.x, appliedMotion.y, appliedMotion.z);
        hookEntity.applyTensionFromOwner(appliedMotion);
    }

    @Shadow
    @Nullable
    public FishingBobberEntity fishHook;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

}

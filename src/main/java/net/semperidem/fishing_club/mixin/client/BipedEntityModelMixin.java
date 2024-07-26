package net.semperidem.fishing_club.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishing_club.registry.FCItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public class BipedEntityModelMixin<T extends LivingEntity> {

    @Shadow public BipedEntityModel.ArmPose leftArmPose;

    @Shadow @Final public ModelPart leftArm;

    @Shadow public BipedEntityModel.ArmPose rightArmPose;

    @Shadow @Final public ModelPart rightArm;

    @Shadow @Final public ModelPart head;

    @Inject(method = "positionLeftArm", at = @At("TAIL"))
    private void onPositionLeftArm(T entity, CallbackInfo ci) {
        onPositionFishingRod(leftArm, leftArmPose, entity);
    }

    @Inject(method = "setAngles(Lnet/minecraft/entity/LivingEntity;FFFFF)V", at = @At("TAIL"))
    private void onSetAngles(T livingEntity, float f, float g, float h, float i, float j, CallbackInfo ci) {
        if (!livingEntity.getMainHandStack().isOf(FCItems.FISH)) {
            return;
        }
        if (livingEntity.getMainHandStack().isEmpty()) {
            return;
        }

        leftArm.yaw = (0.3F) + head.yaw;
        leftArm.pitch = -1.5707964F + head.pitch + 0.1F;
        rightArm.yaw = (-0.6F) + head.yaw;
        rightArm.pitch = -1.5F + head.pitch;

    }

    @Unique
    private void onPositionFishingRod(ModelPart arm, BipedEntityModel.ArmPose armPose, T entity) {
        if (armPose != BipedEntityModel.ArmPose.THROW_SPEAR) {
            return;
        }
        if (!entity.getActiveItem().isOf(FCItems.MEMBER_FISHING_ROD))  {
            return;
        }
        float usedFor = MathHelper.clamp(entity.getItemUseTime() / 100f, 0, 1);
        arm.pitch = (float) Math.PI - usedFor + 1f;
    }

    @Inject(method = "positionRightArm", at = @At("TAIL"))
    private void onPositionRight(T entity, CallbackInfo ci) {
        onPositionFishingRod(rightArm, rightArmPose, entity);
    }


}

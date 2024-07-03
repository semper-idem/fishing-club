package net.semperidem.fishingclub.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.registry.FCItems;
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

    @Inject(method = "positionLeftArm", at = @At("TAIL"))
    private void onPositionLeftArm(T entity, CallbackInfo ci) {
        onPosition(leftArm, leftArmPose, entity);
    }

    @Unique
    private void onPosition(ModelPart arm, BipedEntityModel.ArmPose armPose, T entity) {
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
        onPosition(rightArm, rightArmPose, entity);
    }
}

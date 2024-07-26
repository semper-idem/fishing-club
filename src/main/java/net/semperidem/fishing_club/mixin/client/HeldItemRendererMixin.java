package net.semperidem.fishing_club.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.fish.Species;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCItems;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {


    @Shadow public abstract void renderItem(
            LivingEntity entity,
            ItemStack stack,
            ModelTransformationMode renderMode,
            boolean leftHanded,
            MatrixStack matrices,
            VertexConsumerProvider vertexConsumers,
            int light);

    @Shadow protected abstract void renderMapInBothHands(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress);

    @Shadow protected abstract float getMapAngle(float tickDelta);

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm);

    @Inject(cancellable = true, method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 6, shift = At.Shift.AFTER))
    private void onRenderFirstPersonFishingRod(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!item.isOf(FCItems.MEMBER_FISHING_ROD)) {
            return;
        }
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) MathHelper.clamp((Math.sqrt(player.getItemUseTime() + tickDelta)* 4f), 0, 50)));
        matrices.translate(0, MathHelper.clamp((Math.sqrt(player.getItemUseTime()+tickDelta)/20f), 0, 0.5),0);
        renderItem(player, item, ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, false, matrices, vertexConsumers, light);
        matrices.pop();
        ci.cancel();
    }

    ItemStack heldItem;
    float weightScale = 1;
    float lengthScale = 1;

    @Unique
    private void setupItem(ItemStack itemStack) {
        if (heldItem == itemStack) {
            return;
        }
        this.heldItem = itemStack;
        FishRecord fishRecord = itemStack.get(FCComponents.FISH);
        weightScale = 1;
        lengthScale = 1;
        if (fishRecord == null) {
            return;
        }
        Species species = fishRecord.species();
        weightScale = FishRecord.getWeightScale(species, fishRecord.weight());
        lengthScale = FishRecord.getLengthScale(species, fishRecord.length());
    }

    @Inject(
      cancellable = true,
      method = "renderFirstPersonItem",
      at = @At(
        value = "INVOKE",
        target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/json/ModelTransformationMode;ZLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
        ordinal = 1,
        shift = At.Shift.BEFORE
      )
    )
    private void onRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!item.isOf(FCItems.FISH) || player.isUsingItem()) {
            return;
        }
        this.setupItem(item);
        float swingProgressFunction = MathHelper.sqrt(swingProgress);
        float swingHeight = -0.2F * MathHelper.sin((float) (swingProgress * Math.PI));
        float swingLength = -0.4F * MathHelper.sin((float) (swingProgressFunction * Math.PI));
        matrices.translate(0.0F, -swingHeight / 2.0F, swingLength);
        float i = this.getMapAngle(90);
        matrices.translate(-0.5F, 0.3F, -0.6F);
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(i * -85.0F));
        this.renderArms(player, matrices, vertexConsumers, light);
        matrices.translate(-0.15, -0.2, 0.35);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
        matrices.scale(weightScale, weightScale, lengthScale);
        renderItem(player, item, ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, false, matrices, vertexConsumers, light);
        ci.cancel();
    }

    private void renderArms(AbstractClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        if (player.isInvisible()) {
            return;
        }
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
        this.renderArm(matrices, vertexConsumers, light, Arm.RIGHT);
        this.renderArm(matrices, vertexConsumers, light, Arm.LEFT);
        matrices.pop();
    }

    @Inject(method = "getHandRenderType", at = @At("HEAD"),
            cancellable = true
    )
    private static void getHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> cir) {
        if (player.getMainHandStack().isOf(FCItems.FISH)) {
            cir.setReturnValue(HeldItemRenderer.HandRenderType.RENDER_BOTH_HANDS);
        }
    }
}

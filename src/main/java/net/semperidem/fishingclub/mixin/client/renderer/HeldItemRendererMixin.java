package net.semperidem.fishingclub.mixin.client.renderer;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemDisplayContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.client.screen.game.FishingScreen;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.FishItem;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.Tags;
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



//
//    @Shadow
//    public abstract void renderItem(
//            LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light
//    );
//
//    @Shadow protected abstract void renderMapInBothHands(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, float pitch, float equipProgress, float swingProgress);
//
//    @Shadow protected abstract float getMapAngle(float tickDelta);
//
//    @Shadow @Final private MinecraftClient client;
//
//    @Shadow protected abstract void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, Arm arm);
//
//    @Inject(cancellable = true, method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 6, shift = At.Shift.AFTER))
//    private void onRenderFirstPersonUsingFishingRod(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//        if (!item.isIn(Tags.CORE)) {
//            return;
//        }
//        if (!player.isUsingItem()) {
//            return;
//        }
//        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) MathHelper.clamp((Math.sqrt(player.getItemUseTime() + tickDelta)* 4f), 0, 50)));
//        matrices.translate(0, MathHelper.clamp((Math.sqrt(player.getItemUseTime()+tickDelta)/20f), 0, 0.5),0);
//        renderItem(player, item, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND,  matrices, vertexConsumers, light);
//        matrices.pop();
//        ci.cancel();
//    }
//
//    ItemStack heldItem;
//    float weightScale = 1;
//    float lengthScale = 1;
//
//    @Unique
//    private void setupItem(ItemStack itemStack) {
//        if (heldItem == itemStack) {
//            return;
//        }
//        this.heldItem = itemStack;
//        SpecimenData fishRecord = itemStack.get(Components.SPECIMEN);
//        weightScale = 1;
//        lengthScale = 1;
//        if (fishRecord == null) {
//            return;
//        }
//        weightScale = fishRecord.weightScale();
//        lengthScale = fishRecord.lengthScale();
//    }
//
//    @Inject(
//            method = "renderFirstPersonItem",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
//            )
//    )
//    private void onRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//        if (item.isIn(Tags.CORE)) {
//            this.onRenderFishingRod(player,tickDelta, matrices);
//        }
//    }
//
//    @Inject(
//      cancellable = true,
//      method = "renderFirstPersonItem",
//      at = @At(
//              value = "INVOKE",
//              target = "Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V" ,
//              ordinal = 1,
//              shift = At.Shift.BEFORE
//      )
//    )
//    private void onRenderFirstPersonFish(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//        if (!FishUtil.isFish(item) || player.isUsingItem()) {
//            return;
//        }
//        this.setupItem(item);
//        float swingProgressFunction = MathHelper.sqrt(swingProgress);
//        float swingHeight = -0.2F * MathHelper.sin((float) (swingProgress * Math.PI));
//        float swingLength = -0.4F * MathHelper.sin((float) (swingProgressFunction * Math.PI));
//        matrices.translate(0.0F, -swingHeight / 2.0F, swingLength);
//        float i = this.getMapAngle(90);
//        matrices.translate(-0.5F, 0.3F, -0.6F);
//        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(i * -85.0F));
//        this.renderArms(player, matrices, vertexConsumers, light);
//        matrices.translate(-0.15, -0.2, 0.35);
//        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90));
//        matrices.scale(weightScale, weightScale, lengthScale);
//        renderItem(player, item, ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, matrices, vertexConsumers, light);
//        ci.cancel();
//    }
//
//    private void renderArms(AbstractClientPlayerEntity player, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
//        if (player.isInvisible()) {
//            return;
//        }
//        matrices.push();
//        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(90.0F));
//        this.renderArm(matrices, vertexConsumers, light, Arm.RIGHT);
//        this.renderArm(matrices, vertexConsumers, light, Arm.LEFT);
//        matrices.pop();
//    }
//
//    @Unique
//    private void onRenderFishingRod(LivingEntity entity, float tickDelta, MatrixStack matrices) {
//        if (!(entity instanceof PlayerEntity player)) {
//            return;
//        }
//        int useTime = player.getItemUseTime();
//        boolean isReeling = false;
//
//        if (client.currentScreen instanceof FishingScreen fishingScreen) {
//            isReeling = fishingScreen.controller.isReeling();
//            useTime = fishingScreen.getReelTick();
//        }
//        if (useTime == 0) {
//            return;
//        }
//
//        double swingProgress = Math.pow(useTime + (isReeling ? tickDelta : -tickDelta), 1.3);
//        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) MathHelper.clamp((swingProgress * 4), 0, 40)));
//        matrices.translate(0, MathHelper.clamp((swingProgress / 20f), 0, 0.4),0);
//    }
//
//    @Unique
//    private void onRenderFish(LivingEntity entity, ItemDisplayContext renderMode, MatrixStack matrices) {
//        if (renderMode != ItemDisplayContext.THIRD_PERSON_LEFT_HAND && renderMode != ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
//            return;
//        }
//        matrices.scale(lengthScale, weightScale, weightScale);
//        float pitch = entity.getPitch();
//        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(60 + Math.abs(pitch / 3)));
//        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(5 + pitch / 2.5f));
//        matrices.translate(0f, 0f, -0.1f - 0.3 * Math.abs(pitch / 90));
//    }
//
//
//
//    @Inject(
//            method = "renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
//            at = @At(
//                    value = "INVOKE",
//                    target = "Lnet/minecraft/client/render/item/ItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/world/World;III)V"
//    ))
//    protected void onRenderItem(LivingEntity entity, ItemStack stack, ItemDisplayContext renderMode, MatrixStack matrices, VertexConsumerProvider vertexConsumer, int light, CallbackInfo ci) {
//        if ((FishUtil.isFish(stack))) {
//            this.onRenderFish(entity,renderMode, matrices);
//        }
//
//    }
//    @Inject(method = "getHandRenderType", at = @At("HEAD"),
//            cancellable = true
//    )
//    private static void getHandRenderType(ClientPlayerEntity player, CallbackInfoReturnable<HeldItemRenderer.HandRenderType> cir) {
//        if (player.getMainHandStack().getItem() instanceof FishItem) {
//            cir.setReturnValue(HeldItemRenderer.HandRenderType.RENDER_BOTH_HANDS);
//        }
//    }
}

package net.semperidem.fishingclub.mixin.client;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.registry.FCItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

    @Inject( cancellable = true, method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 6, shift = At.Shift.AFTER))
    private void onRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (!item.isOf(FCItems.MEMBER_FISHING_ROD)) {
            return;
        }
        matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float) MathHelper.clamp((Math.sqrt(player.getItemUseTime() + tickDelta)* 4f), 0, 50)));
        matrices.translate(0, MathHelper.clamp((Math.sqrt(player.getItemUseTime()+tickDelta)/20f), 0, 0.5),0);
        renderItem(player, item, ModelTransformationMode.FIRST_PERSON_RIGHT_HAND, false, matrices, vertexConsumers, light);
        matrices.pop();
        ci.cancel();
    }
}

package net.semperidem.fishingclub.mixin.client;

import net.minecraft.client.render.item.HeldItemRenderer;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(HeldItemRenderer.class)
public abstract class HeldItemRendererMixin {
//
//    @Shadow public abstract void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);
//
//    @Inject( cancellable = true, method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal = 6, shift = At.Shift.AFTER))
//    private void onRenderFirstPersonItem(AbstractClientPlayerEntity player, float tickDelta, float pitch, Hand hand, float swingProgress, ItemStack item, float equipProgress, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
//        if (item.isOf(ItemRegistry.MEMBER_FISHING_ROD)) {
//            FishingClubClient.onPosition1st(player, tickDelta, matrices);
//            renderItem(player, item, ModelTransformation.Mode.FIRST_PERSON_RIGHT_HAND, false, matrices, vertexConsumers, light);
//            matrices.pop();
//            ci.cancel();
//        }
//    }
}

package net.semperidem.fishingclub.mixin.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.TropicalFishEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TropicalFishEntityRenderer.class)
public class TropicalFishEntityRendererMixin {



    @Inject(method = "render(Lnet/minecraft/entity/passive/TropicalFishEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/MobEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void fishing_club$render(TropicalFishEntity tropicalFishEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        SpecimenData data = SpecimenComponent.of(tropicalFishEntity).get();
        if (data == null) {
            return;
        }

        float weightRating = data.weightScale();
        matrixStack.scale(weightRating, weightRating, data.lengthScale());
    }
}

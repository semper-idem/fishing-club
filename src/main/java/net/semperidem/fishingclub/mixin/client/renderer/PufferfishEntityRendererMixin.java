package net.semperidem.fishingclub.mixin.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PufferfishEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PufferfishEntityRenderer.class)
public class PufferfishEntityRendererMixin extends MobEntityRenderer<PufferfishEntity, EntityModel<PufferfishEntity>> {



    @Final
    @Shadow
    private EntityModel<PufferfishEntity> smallModel;
    @Unique
    private static final Identifier TEXTURE = FishingClub.identifier("textures/entity/fish/pufferfish/pufferfish.png");
    @Unique
    private static final Identifier ALTERNATIVE_TEXTURE = FishingClub.identifier("textures/entity/fish/pufferfish/pufferfish_albino.png");

    public PufferfishEntityRendererMixin(EntityRendererFactory.Context context, EntityModel<PufferfishEntity> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Inject(method = "render(Lnet/minecraft/entity/passive/PufferfishEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/MobEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"))
    private void fishing_club$render(PufferfishEntity pufferfishEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
       SpecimenData data = SpecimenComponent.of(pufferfishEntity).get();
       if (data == null) {
           return;
       }

        float weightRating = data.weightScale();
        matrixStack.scale(weightRating, weightRating, data.lengthScale());
    }

    @Override
    public Identifier getTexture(PufferfishEntity entity) {
        SpecimenData data = SpecimenComponent.of(entity).get();
        return data == null ? TEXTURE : SpecimenData.isAlbino(data) ? ALTERNATIVE_TEXTURE : TEXTURE;
    }

}

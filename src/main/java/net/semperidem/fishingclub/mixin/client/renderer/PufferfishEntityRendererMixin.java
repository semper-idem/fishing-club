package net.semperidem.fishingclub.mixin.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.PufferfishEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.semperidem.fishingclub.fish.AbstractFishEntityRenderer.scaleBySpecimenData;

@SuppressWarnings("unused")
@Mixin(PufferfishEntityRenderer.class)
public class PufferfishEntityRendererMixin extends MobEntityRenderer<PufferfishEntity, EntityModel<PufferfishEntity>> {
    private static final Species<?> SPECIES = Species.Library.PUFFERFISH;

    private Identifier texture;
    private Identifier alternativeTexture;

    public PufferfishEntityRendererMixin(EntityRendererFactory.Context context, EntityModel<PufferfishEntity> entityModel, float f) {
        super(context, entityModel, f);
        this.setTextures(SPECIES);
    }

    public void setTextures(Species<?> species) {
        this.texture = species.texture(false);
        this.alternativeTexture = species.texture(true);
    }

    @Override
    public Identifier getTexture(PufferfishEntity entity) {
        return SpecimenComponent.of(entity).getOrDefault().isAlbino() ? this.alternativeTexture : this.texture;
    }

    @Inject(method = "render(Lnet/minecraft/entity/passive/PufferfishEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/MobEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            )
    )
    private void fishing_club$render(
            PufferfishEntity pufferfishEntity,
            float entityYaw,
            float partialTicks,
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int light,
            CallbackInfo ci
    ) {
        scaleBySpecimenData(matrixStack, pufferfishEntity);
    }

}

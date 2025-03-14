package net.semperidem.fishingclub.mixin.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.CodEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CodEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.semperidem.fishingclub.fish.AbstractFishEntityRenderer.*;

@SuppressWarnings("unused")
@Mixin(CodEntityRenderer.class)
public class CodEntityRendererMixin extends MobEntityRenderer<CodEntity, CodEntityModel<CodEntity>> {
    private static final Species<?> SPECIES = Species.Library.COD;

    private Identifier texture;
    private Identifier alternativeTexture;

    public CodEntityRendererMixin(EntityRendererFactory.Context context, CodEntityModel<CodEntity> entityModel, float f) {
        super(context, entityModel, f);
        this.setTextures(SPECIES);
    }

    public void setTextures(Species<?> species) {
        this.texture = species.texture(false);
        this.alternativeTexture = species.texture(true);
    }

    @Override
    public Identifier getTexture(CodEntity entity) {
        return SpecimenComponent.of(entity).getOrDefault().isAlbino() ? this.alternativeTexture : this.texture;
    }

    @Inject(method = "render(Lnet/minecraft/entity/passive/CodEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/entity/MobEntityRenderer;render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"
            )
    )
    private void fishing_club$render(
            CodEntity codEntity,
            float entityYaw,
            float partialTicks,
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int light,
            CallbackInfo ci
    ) {
        scaleBySpecimenData(matrixStack, codEntity);
    }

}

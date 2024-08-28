package net.semperidem.fishingclub.mixin.client.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.CodEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CodEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CodEntityRenderer.class)
public class CodEntityRendererMixin extends MobEntityRenderer<CodEntity, CodEntityModel<CodEntity>> {


    @Unique
    private static final Identifier TEXTURE = FishingClub.identifier("textures/entity/fish/cod/cod.png");
    @Unique
    private static final Identifier ALTERNATIVE_TEXTURE = FishingClub.identifier("textures/entity/fish/cod/cod_albino.png");
    public CodEntityRendererMixin(EntityRendererFactory.Context context, CodEntityModel<CodEntity> entityModel, float f) {
        super(context, entityModel, f);
    }

    @Unique
    public void renderWithSpecimenData(
            SpecimenData specimenData,
            CodEntity livingEntity,
            float entityYaw,
            float partialTicks,
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int light
    ) {

        matrixStack.push();
        float weightRating = specimenData.weightScale();
        matrixStack.scale(weightRating, weightRating, specimenData.lengthScale());
        super.render(livingEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
        matrixStack.pop();

    }
    @Override
    public void render(
            CodEntity livingEntity,
            float entityYaw,
            float partialTicks,
            MatrixStack matrixStack,
            VertexConsumerProvider vertexConsumerProvider,
            int light
    ) {

        SpecimenData specimenData = SpecimenComponent.of(livingEntity).get();
        if (specimenData != null) {
            this.renderWithSpecimenData(specimenData, livingEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
            return;
        }
        super.render(livingEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
    }

    @Override
	public Identifier getTexture(CodEntity entity) {
		SpecimenData data = SpecimenComponent.of(entity).get();
		return data == null ? TEXTURE : SpecimenData.isAlbino(data) ? ALTERNATIVE_TEXTURE : TEXTURE;
	}
}

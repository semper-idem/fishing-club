package net.semperidem.fishingclub.fish;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.specimen.SpecimenComponent;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;


public abstract class AbstractFishEntityRenderer<M extends WaterCreatureEntity, T extends EntityModel<M>> extends MobEntityRenderer<M, T> {

	public AbstractFishEntityRenderer(EntityRendererFactory.Context context, T entityModel, float f) {
		super(context, entityModel, f);
	}

	public void renderWithSpecimenData(
			SpecimenData specimenData,
			M livingEntity,
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
			M livingEntity,
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


	public abstract Identifier getTexture();
	public abstract Identifier getAlternateTexture();
	public Identifier getTexture(M entity) {
		SpecimenData data = SpecimenComponent.of(entity).get();
		return data == null ? getTexture() : SpecimenData.isAlbino(data) ? getAlternateTexture() : getTexture();

	}
}

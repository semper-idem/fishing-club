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


public abstract class AbstractFishEntityRenderer<M extends AbstractFishEntity, T extends EntityModel<M>> extends MobEntityRenderer<M, T> {
	private Identifier texture;
	private Identifier alternativeTexture;

	public AbstractFishEntityRenderer(EntityRendererFactory.Context context, T entityModel, float shadowRadius) {
		super(context, entityModel, shadowRadius);
	}

	public void setTextures(Species<?> species) {
		this.texture = species.texture(false);
		this.alternativeTexture = species.texture(true);
	}

	@Override
	public void render(
			M waterCreatureEntity,
			float entityYaw,
			float partialTicks,
			MatrixStack matrixStack,
			VertexConsumerProvider vertexConsumerProvider,
			int light
	) {
		matrixStack.push();
		scaleBySpecimenData(matrixStack, waterCreatureEntity);
		super.render(waterCreatureEntity, entityYaw, partialTicks, matrixStack, vertexConsumerProvider, light);
		matrixStack.pop();
	}

	public static void scaleBySpecimenData(MatrixStack matrixStack, WaterCreatureEntity waterCreatureEntity){
		SpecimenData specimenData = SpecimenComponent.of(waterCreatureEntity).getOrDefault();
		float weightScale = specimenData.weightScale();
		float lengthScale = specimenData.lengthScale();
		matrixStack.scale(weightScale, weightScale, lengthScale);
	}

	public Identifier getTexture(M entity) {
		return SpecimenComponent.of(entity).getOrDefault().isAlbino() ? this.alternativeTexture : this.texture;//todo find out if this is expensive
	}
}

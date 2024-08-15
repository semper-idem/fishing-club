package net.semperidem.fishing_club.fish.species.butterfish;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishing_club.fish.SpeciesLibrary;

public class ButterfishEntityRenderer<M extends MobEntity> extends MobEntityRenderer<M, ButterfishEntityModel<M>> {
	private static final float SHADOW_RADIUS = 0.3f;
	private static final Identifier TEXTURE = SpeciesLibrary.BUTTERFISH.getTexture();

	public ButterfishEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new ButterfishEntityModel<>(context.getPart(SpeciesLibrary.BUTTERFISH.getLayer())), SHADOW_RADIUS);
	}

	@Override
	public Identifier getTexture(M entity) {
		return TEXTURE;
	}



	protected void setupTransforms(M codEntity, MatrixStack matrixStack, float f, float g, float h, float i) {
		super.setupTransforms(codEntity, matrixStack, f, g, h, i);
		float j = 4.3F * MathHelper.sin(0.6F * f);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
		if (!codEntity.isTouchingWater()) {
			matrixStack.translate(0.1F, 0.1F, -0.1F);
			matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
		}
	}
}

package net.semperidem.fishingclub.fish.species.butterfish;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.fish.AbstractFishEntityRenderer;
import net.semperidem.fishingclub.fish.Species;

public class ButterfishEntityRenderer<T extends ButterfishEntity> extends AbstractFishEntityRenderer<T, ButterfishEntityModel<T>> {

	private static final Species<?> SPECIES = Species.Library.BUTTERFISH;

	public ButterfishEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new ButterfishEntityModel<>(context.getPart(SPECIES.getLayerId())), 0.3f);
		this.setTextures(SPECIES);
	}

	protected void setupTransforms(T butterfishEntity, MatrixStack matrixStack, float animationProgress, float bodyYaw, float tickDelta, float scale) {
		super.setupTransforms(butterfishEntity, matrixStack, animationProgress, bodyYaw, tickDelta, scale);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(4.3F * MathHelper.sin(0.6F * animationProgress)));

		if (butterfishEntity.isTouchingWater()) {
			return;
		}

		matrixStack.translate(0.1F, 0.1F, -0.1F);
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(animationProgress * 6));
	}

}

package net.semperidem.fishingclub.fish.species.butterfish;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.fish.AbstractFishEntity;
import net.semperidem.fishingclub.fish.AbstractFishEntityRenderer;
import net.semperidem.fishingclub.fish.Species;

public class ButterfishEntityRenderer<T extends WaterCreatureEntity> extends AbstractFishEntityRenderer<T, ButterfishEntityModel<T>> {

	private static final float SHADOW_RADIUS = 0.3f;
	private static final Identifier TEXTURE = Species.Library.BUTTERFISH.texture(false);
	private static final Identifier ALBINO_TEXTURE = Species.Library.BUTTERFISH.texture(true);

	public ButterfishEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new ButterfishEntityModel<>(context.getPart(Species.Library.BUTTERFISH.getLayerId())), SHADOW_RADIUS);
	}

	protected void setupTransforms(T fishEntity, MatrixStack matrixStack, float animationProgress, float bodyYaw, float tickDelta, float scalei) {
		super.setupTransforms(fishEntity, matrixStack, animationProgress, bodyYaw, tickDelta, scalei);
		float jumpHeight = 4.3F * MathHelper.sin(0.6F * animationProgress);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(jumpHeight));
		if (!fishEntity.isTouchingWater()) {
			matrixStack.translate(0.1F, 0.1F, -0.1F);
			matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
		}
	}

	@Override
	public Identifier getTexture() {
		return TEXTURE;
	}

	@Override
	public Identifier getAlternateTexture() {
		return ALBINO_TEXTURE;
	}

}

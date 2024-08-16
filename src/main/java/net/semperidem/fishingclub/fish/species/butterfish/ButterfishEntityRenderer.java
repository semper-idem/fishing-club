package net.semperidem.fishingclub.fish.species.butterfish;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.fish.AbstractFishEntityRenderer;
import net.semperidem.fishingclub.fish.SpeciesLibrary;

public class ButterfishEntityRenderer extends AbstractFishEntityRenderer<ButterfishEntity, ButterfishEntityModel<ButterfishEntity>> {
	private static final float SHADOW_RADIUS = 0.3f;
	private static final Identifier TEXTURE = SpeciesLibrary.BUTTERFISH.getTexture(false);
	private static final Identifier ALBINO_TEXTURE = SpeciesLibrary.BUTTERFISH.getTexture(true);

	public ButterfishEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new ButterfishEntityModel<>(context.getPart(SpeciesLibrary.BUTTERFISH.getLayer())), SHADOW_RADIUS);
	}

	protected void setupTransforms(ButterfishEntity fishEntity, MatrixStack matrixStack, float f, float g, float h, float i) {
		super.setupTransforms(fishEntity, matrixStack, f, g, h, i);
		float j = 4.3F * MathHelper.sin(0.6F * f);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(j));
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

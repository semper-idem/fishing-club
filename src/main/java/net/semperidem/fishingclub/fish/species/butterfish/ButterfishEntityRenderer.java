package net.semperidem.fishingclub.fish.species.butterfish;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.texture.MissingSprite;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.registry.EntityModelLayers;

public class ButterfishEntityRenderer extends MobEntityRenderer<ButterfishEntity, ButterfishEntityRenderState, ButterfishEntityModel> {
	public ButterfishEntityRenderer(EntityRendererFactory.Context context) {
		super(context, new ButterfishEntityModel(context.getPart(EntityModelLayers.BUTTERFISH)), 0.3f);
	}

	@Override
	public ButterfishEntityRenderState createRenderState() {
		return new ButterfishEntityRenderState();
	}

	@Override
	protected void setupTransforms(ButterfishEntityRenderState state, MatrixStack matrixStack, float bodyYaw, float baseHeight) {
		super.setupTransforms(state, matrixStack, state.age, bodyYaw);
		matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(4.3F * MathHelper.sin(0.6F * state.age)));

		if (state.touchingWater) {
			return;
		}

		matrixStack.translate(0.1F, 0.1F, -0.1F);
		matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
		matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(state.age * 6));
	}

	@Override
	public Identifier getTexture(ButterfishEntityRenderState state) {
		return state.variant == null ? MissingSprite.getMissingSpriteId() : state.variant.modelAndTexture().asset().texturePath();
	}
}

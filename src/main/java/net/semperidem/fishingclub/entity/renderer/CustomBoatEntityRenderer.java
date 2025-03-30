package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.state.BoatEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;

public class CustomBoatEntityRenderer extends BoatEntityRenderer {

	public CustomBoatEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer) {
		super(ctx, layer);
	}

	@Override
	public void render(BoatEntityRenderState boatEntityRenderState, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
	//
	}
}

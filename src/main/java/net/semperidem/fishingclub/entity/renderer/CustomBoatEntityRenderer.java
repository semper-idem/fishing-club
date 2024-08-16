package net.semperidem.fishingclub.entity.renderer;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.BoatEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.vehicle.BoatEntity;

public class CustomBoatEntityRenderer extends BoatEntityRenderer {

	public CustomBoatEntityRenderer(EntityRendererFactory.Context ctx, boolean chest) {
		super(ctx, chest);
	}


	@Override
	public void render(BoatEntity boatEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {

	}

}

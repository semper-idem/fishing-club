package net.semperidem.fishingclub.fish.species.butterfish;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.util.math.MathHelper;

public class ButterfishEntityModel extends EntityModel<ButterfishEntityRenderState> {

	private final ModelPart tailFin;

	public ButterfishEntityModel(ModelPart root) {
        super(root);
		this.tailFin = root.getChild(EntityModelPartNames.BODY).getChild((EntityModelPartNames.TAIL_FIN));
	}

	public static TexturedModelData getTexturedModelData() {

		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData body = modelPartData.addChild(
			EntityModelPartNames.BODY,
			ModelPartBuilder.create()
				.uv(0, 0).cuboid(-2.0F, -3.0F, -2.0F, 4.0F, 2.0F, 6.0F)
				.uv(7, 5).cuboid(0.0F, -4.0F, 1.0F, 0.001F, 1.0F, 2.0F),
			ModelTransform.origin(0.0F, 24.0F, 0.0F)
		);

		body.addChild(
			EntityModelPartNames.TAIL_FIN,
			ModelPartBuilder.create().uv(8, 10).cuboid(0.0F, -4.0F, 4.0F, 0.001F, 4.0F, 2.0F),
			ModelTransform.origin(0, 0, 0)
		);

		body.addChild(
			EntityModelPartNames.LEFT_FIN,
			ModelPartBuilder.create().uv(7, 11).cuboid(-1.0F, 0.4F, -1.0F, 1.0F, 0.001F, 1.0F),
			ModelTransform.of(3.0F, -1.0F, 1.0F, 0.0F, 0.0F, 0.3927F)
		);
		body.addChild(
			EntityModelPartNames.RIGHT_FIN,
			ModelPartBuilder.create().uv(7, 10).cuboid(0.0F, 0.4F, -1.0F, 1.0F, 0.001F, 1.0F),
			ModelTransform.of(-3.0F, -1.0F, 1.0F, 0.0F, 0.0F, -0.3927F)
		);

		return TexturedModelData.of(modelData, 32, 32);
	}


	public void setAngles(ButterfishEntityRenderState state) {
		float f = 1.0F;
		if (!state.touchingWater) {
			f = 1.5F;
		}

		this.tailFin.yaw = -f * 0.45F * MathHelper.sin(0.6F * state.age);
	}
}

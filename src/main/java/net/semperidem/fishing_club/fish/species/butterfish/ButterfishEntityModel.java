package net.semperidem.fishing_club.fish.species.butterfish;

import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

public class ButterfishEntityModel <T extends Entity> extends SinglePartEntityModel<T> {

	private final ModelPart root;
	private final ModelPart tailFin;

	public ButterfishEntityModel(ModelPart root) {
		this.root = root;
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
			ModelTransform.pivot(0.0F, 24.0F, 0.0F)
		);

		body.addChild(
			EntityModelPartNames.TAIL_FIN,
			ModelPartBuilder.create().uv(8, 10).cuboid(0.0F, -4.0F, 4.0F, 0.001F, 4.0F, 2.0F),
			ModelTransform.pivot(0, 0, 0)
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

	@Override
	public ModelPart getPart() {
		return this.root;
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
		float f = 1.0F;
		if (!entity.isTouchingWater()) {
			f = 1.5F;
		}

		this.tailFin.yaw = -f * 0.45F * MathHelper.sin(0.6F * animationProgress);
	}
}

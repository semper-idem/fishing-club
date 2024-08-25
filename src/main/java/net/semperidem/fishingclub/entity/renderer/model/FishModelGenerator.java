package net.semperidem.fishingclub.entity.renderer.model;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public class FishModelGenerator extends FabricModelProvider {

	public FishModelGenerator(FabricDataOutput output) {
		super(output);
	}

	public static TexturedModelData getDefaultModelData() {
		return getCodModelData();
	}

	public static TexturedModelData getCodModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		modelPartData.addChild(
			EntityModelPartNames.BODY,
			ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -2.0F, 0.0F, 2.0F, 4.0F, 7.0F),
			ModelTransform.pivot(0.0F, 22.0F, 0.0F)
		);
		modelPartData.addChild(
			EntityModelPartNames.HEAD,
			ModelPartBuilder.create().uv(11, 0).cuboid(-1.0F, -2.0F, -3.0F, 2.0F, 4.0F, 3.0F),
			ModelTransform.pivot(0.0F, 22.0F, 0.0F)
		);
		modelPartData.addChild(
			EntityModelPartNames.NOSE,
			ModelPartBuilder.create().uv(0, 0).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 1.0F),
			ModelTransform.pivot(0.0F, 22.0F, -3.0F)
		);
		modelPartData.addChild(
			EntityModelPartNames.RIGHT_FIN,
			ModelPartBuilder.create().uv(22, 1).cuboid(-2.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F),
			ModelTransform.of(-1.0F, 23.0F, 0.0F, 0.0F, 0.0F, (float) (-Math.PI / 4))
		);
		modelPartData.addChild(
			EntityModelPartNames.LEFT_FIN,
			ModelPartBuilder.create().uv(22, 4).cuboid(0.0F, 0.0F, -1.0F, 2.0F, 0.0F, 2.0F),
			ModelTransform.of(1.0F, 23.0F, 0.0F, 0.0F, 0.0F, (float) (Math.PI / 4))
		);
		modelPartData.addChild(
			EntityModelPartNames.TAIL_FIN,
			ModelPartBuilder.create().uv(22, 3).cuboid(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 4.0F),
			ModelTransform.pivot(0.0F, 22.0F, 7.0F)
		);
		modelPartData.addChild(
			EntityModelPartNames.TOP_FIN,
			ModelPartBuilder.create().uv(20, -6).cuboid(0.0F, -1.0F, -1.0F, 0.0F, 1.0F, 6.0F),
			ModelTransform.pivot(0.0F, 20.0F, 0.0F)
		);
		return TexturedModelData.of(modelData, 32, 32);
	}


	@Override
	public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

	}

	@Override
	public void generateItemModels(ItemModelGenerator itemModelGenerator) {
	}

}

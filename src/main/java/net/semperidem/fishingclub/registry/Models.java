package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.renderer.*;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.entity.renderer.model.HarpoonEntityModel;

public class Models {

	public static final EntityModelLayer MODEL_HARPOON_LAYER = new EntityModelLayer(FishingClub.identifier("harpoon_rod"), "main");
	public static final EntityModelLayer MODEL_FISHERMAN_LAYER = new EntityModelLayer(FishingClub.identifier("fisherman"), "main");
	public static final EntityModelLayer MODEL_FISH_DISPLAY_LAYER = new EntityModelLayer(FishingClub.identifier("sign/bamboo"), "main");

	public static void initModels() {

		EntityModelLayerRegistry.registerModelLayer(MODEL_HARPOON_LAYER, HarpoonEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_FISHERMAN_LAYER, FishermanEntityModel::getTextureModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_FISH_DISPLAY_LAYER, FishDisplayEntityRenderer::getTexturedModelData);


		EntityRendererRegistry.register(EntityTypes.DEREK_ENTITY, FishermanEntityRenderer::new);
		EntityRendererRegistry.register(EntityTypes.HOOK_ENTITY, HookEntityRenderer::new);
	}



}

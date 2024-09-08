package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.renderer.*;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.entity.renderer.model.HarpoonEntityModel;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;

import java.util.Collection;
import java.util.HashMap;

public class FCModels {

	public static final EntityModelLayer MODEL_HARPOON_LAYER = new EntityModelLayer(FishingClub.identifier("harpoon_rod"), "main");
	public static final EntityModelLayer MODEL_FISHERMAN_LAYER = new EntityModelLayer(FishingClub.identifier("fisherman"), "main");
	public static final EntityModelLayer MODEL_FISH_DISPLAY_LAYER = new EntityModelLayer(FishingClub.identifier("sign/bamboo"), "main");

	public static void initModels() {

		EntityModelLayerRegistry.registerModelLayer(MODEL_HARPOON_LAYER, HarpoonEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_FISHERMAN_LAYER, FishermanEntityModel::getTextureModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_FISH_DISPLAY_LAYER, FishDisplayEntityRenderer::getTexturedModelData);


		EntityRendererRegistry.register(FCEntityTypes.DEREK_ENTITY, FishermanEntityRenderer::new);
		EntityRendererRegistry.register(FCEntityTypes.HOOK_ENTITY, HookEntityRenderer::new);
	}



}

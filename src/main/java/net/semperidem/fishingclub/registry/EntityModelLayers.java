package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.renderer.*;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.entity.renderer.model.HarpoonEntityModel;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityModel;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityRenderer;

public class EntityModelLayers {

	public static final EntityModelLayer HARPOON = registerMain("harpoon_rod");
	public static final EntityModelLayer FISHERMAN = registerMain("fisherman");
	public static final EntityModelLayer FISH_DISPLAY = registerMain("sign/bamboo");
	public static final EntityModelLayer BUTTERFISH = registerMain(Species.Library.BUTTERFISH.textureName());

	public static void register() {

		EntityModelLayerRegistry.registerModelLayer(HARPOON, HarpoonEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(FISHERMAN, FishermanEntityModel::getTextureModelData);
		EntityModelLayerRegistry.registerModelLayer(FISH_DISPLAY, FishDisplayEntityRenderer::getTexturedModelData);

		EntityModelLayerRegistry.registerModelLayer(BUTTERFISH, ButterfishEntityModel::getTexturedModelData);



		EntityRendererRegistry.register(EntityTypes.FISHERMAN, FishermanEntityRenderer::new);
		EntityRendererRegistry.register(EntityTypes.HOOK, HookEntityRenderer::new);

		EntityRendererRegistry.register(EntityTypes.BUTTERFISH, ButterfishEntityRenderer::new);

	}





	private static EntityModelLayer registerMain(String id) {
		return register(id, "main");
	}

	private static EntityModelLayer register(String id, String layer) {
		return new EntityModelLayer(FishingClub.identifier(id), layer);
	}
}

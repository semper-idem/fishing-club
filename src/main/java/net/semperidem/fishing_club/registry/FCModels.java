package net.semperidem.fishing_club.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.util.Identifier;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.entity.FCFishEntity;
import net.semperidem.fishing_club.entity.renderer.*;
import net.semperidem.fishing_club.entity.renderer.model.FCFishEntityModel;
import net.semperidem.fishing_club.entity.renderer.model.FishModelGenerator;
import net.semperidem.fishing_club.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishing_club.entity.renderer.model.HarpoonEntityModel;
import net.semperidem.fishing_club.fish.FishRecord;
import net.semperidem.fishing_club.fish.Species;
import net.semperidem.fishing_club.fish.SpeciesLibrary;

import java.util.Collection;
import java.util.HashMap;

public class FCModels {

	public static final EntityModelLayer MODEL_HARPOON_LAYER = new EntityModelLayer(FishingClub.getIdentifier("harpoon_rod"), "main");
	public static final EntityModelLayer MODEL_FISHERMAN_LAYER = new EntityModelLayer(FishingClub.getIdentifier("fisherman"), "main");
	public static final EntityModelLayer MODEL_FISH_DISPLAY_LAYER = new EntityModelLayer(FishingClub.getIdentifier("sign/bamboo"), "main");


	private static final HashMap<String, ModelIdentifier> SPECIES_TO_MODEL_ID = new HashMap<>();
	private static final HashMap<String, EntityModelLayer> SPECIES_TO_MODEL_LAYER = new HashMap<>();
	private static final HashMap<String, FCFishEntityModel<FCFishEntity>> SPECIES_TO_MODEL = new HashMap<>();
	private static final HashMap<String, Identifier> SPECIES_TO_TEXTURE = new HashMap<>();

	public static EntityModelLayer DEFAULT_MODEL_LAYER;
	public static ModelIdentifier DEFAULT_MODEL;

	private static void registerModel(Species species) {
		if (species.model() == null) {
			return;
		}
		var speciesName = species.getTextureName(false);
		var albinoName = species.getTextureName(true);

		SPECIES_TO_TEXTURE.put(speciesName, FishingClub.getIdentifier("textures/entity/" + speciesName + ".png"));
		SPECIES_TO_TEXTURE.put(albinoName, FishingClub.getIdentifier("textures/entity/" + albinoName + ".png"));

		EntityModelLayer modelLayer = new EntityModelLayer(FishingClub.getIdentifier(speciesName), "main");
		EntityModelLayerRegistry.registerModelLayer(modelLayer, species.model());

		var modelIdentifier = new ModelIdentifier(FishingClub.getIdentifier(speciesName + "_item_3d"), "inventory");
		var modelIdentifierAlbino = new ModelIdentifier(FishingClub.getIdentifier(albinoName + "_item_3d"), "inventory");
		SPECIES_TO_MODEL_ID.put(speciesName, modelIdentifier);
		SPECIES_TO_MODEL_ID.put(albinoName, modelIdentifierAlbino);

		SPECIES_TO_MODEL_LAYER.put(speciesName, modelLayer);
		SPECIES_TO_MODEL_LAYER.put(albinoName, modelLayer);
	}

	public static ModelIdentifier getModelId(FishRecord fishRecord) {
		return SPECIES_TO_MODEL_ID.getOrDefault(fishRecord.getTextureName(), DEFAULT_MODEL);
	}

	public static EntityModelLayer getModelLayer(String textureName) {
		return SPECIES_TO_MODEL_LAYER.getOrDefault(textureName, DEFAULT_MODEL_LAYER);
	}

	public static FCFishEntityModel<FCFishEntity> getModel(FishRecord fishRecord) {
		return SPECIES_TO_MODEL.get(fishRecord.getTextureName());
	}

	public static Identifier getTexture(FishRecord fishRecord) {
		return SPECIES_TO_TEXTURE.get(fishRecord.getTextureName());
	}

	public static Collection<ModelIdentifier> getModelIds() {
		return SPECIES_TO_MODEL_ID.values();
	}

	public static void initModels() {

		EntityModelLayerRegistry.registerModelLayer(MODEL_HARPOON_LAYER, HarpoonEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_FISHERMAN_LAYER, FishermanEntityModel::getTextureModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_FISH_DISPLAY_LAYER, FishDisplayEntityRenderer::getTexturedModelData);

		SpeciesLibrary.COD.withModel(FishModelGenerator::getCodModelData);
		SpeciesLibrary.BUTTERFISH.withModel(FishModelGenerator::getButterfishModelData);

		SpeciesLibrary.iterator().forEachRemaining(FCModels::registerModel);
		DEFAULT_MODEL_LAYER = SPECIES_TO_MODEL_LAYER.get(SpeciesLibrary.DEFAULT.getTextureName(false));
		DEFAULT_MODEL = SPECIES_TO_MODEL_ID.get(SpeciesLibrary.DEFAULT.getTextureName(false));

		EntityRendererRegistry.register(FCEntityTypes.DEREK_ENTITY, FishermanEntityRenderer::new);
		EntityRendererRegistry.register(FCEntityTypes.HOOK_ENTITY, HookEntityRenderer::new);
		EntityRendererRegistry.register(FCEntityTypes.HARPOON_ENTITY, HarpoonEntityRenderer::new);
		EntityRendererRegistry.register(FCEntityTypes.LINE_ARROW_ENTITY, LineArrowEntityRenderer::new);
		EntityRendererRegistry.register(FCEntityTypes.FISH_ENTITY, FCModels::init);

	}


	public static FCFishEntityRenderer init(EntityRendererFactory.Context context) {
		FCModels.initTextureModels(context);
		return new FCFishEntityRenderer(context);
	}

	public static void initTextureModels(EntityRendererFactory.Context context) {
		if (!SPECIES_TO_MODEL.isEmpty()) {
			return;
		}
		SPECIES_TO_MODEL_ID.keySet().forEach(
			speciesTextureName -> SPECIES_TO_MODEL.put(
				speciesTextureName, new FCFishEntityModel<>(context.getPart(getModelLayer(speciesTextureName)))
			)
		);
	}

}

package net.semperidem.fishing_club.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.entity.*;
import net.semperidem.fishing_club.entity.renderer.*;
import net.semperidem.fishing_club.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishing_club.entity.renderer.model.HarpoonEntityModel;

public class FCEntityTypes {

	public static EntityType<FishermanEntity> DEREK_ENTITY;
	public static EntityType<HookEntity> HOOK_ENTITY;
	public static EntityType<HarpoonEntity> HARPOON_ENTITY;
	public static EntityType<LineArrowEntity> LINE_ARROW_ENTITY;
	public static EntityType<FCFishEntity> FISH_ENTITY;

	public static final EntityModelLayer MODEL_HARPOON_LAYER = new EntityModelLayer(FishingClub.getIdentifier("harpoon_rod"), "main");
	public static final EntityModelLayer MODEL_FISHERMAN_LAYER = new EntityModelLayer(FishingClub.getIdentifier("fisherman"), "main");
	public static final EntityModelLayer MODEL_FISH_DISPLAY_LAYER = new EntityModelLayer(FishingClub.getIdentifier("sign/bamboo"), "main");

	public static void register() {
		EntityModelLayerRegistry.registerModelLayer(MODEL_HARPOON_LAYER, HarpoonEntityModel::getTexturedModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_FISHERMAN_LAYER, FishermanEntityModel::getTextureModelData);
		EntityModelLayerRegistry.registerModelLayer(MODEL_FISH_DISPLAY_LAYER, FishDisplayEntityRenderer::getTexturedModelData);

		DEREK_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("derek_entity"),
				FabricEntityTypeBuilder.<FishermanEntity>create(
						SpawnGroup.CREATURE, (entityType, world) -> new FishermanEntity(world))
					.dimensions(EntityDimensions.fixed(0.6f, 1.95f))
					.build());
		FabricDefaultAttributeRegistry.register(DEREK_ENTITY, FishermanEntity.createMobAttributes());

		HOOK_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("hook_entity"),
				FabricEntityTypeBuilder.<HookEntity>create(SpawnGroup.MISC, HookEntity::new)
					.dimensions(EntityDimensions.fixed(0.25f, 0.25f))
					.disableSaving()
					.trackRangeBlocks(128)
					.trackedUpdateRate(20)
					.build());

		HARPOON_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("harpoon_entity"),
				FabricEntityTypeBuilder.<HarpoonEntity>create(SpawnGroup.MISC, HarpoonEntity::new)
					.dimensions(EntityDimensions.fixed(0.5f, 0.5f))
					.disableSummon()
					.disableSaving()
					.trackRangeBlocks(64)
					.trackedUpdateRate(20)
					.build());

		LINE_ARROW_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("line_arrow_entity"),
				FabricEntityTypeBuilder.<LineArrowEntity>create(SpawnGroup.MISC, LineArrowEntity::new)
					.dimensions(EntityDimensions.fixed(0.5f, 0.5f))
					.disableSummon()
					.disableSaving()
					.trackRangeBlocks(64)
					.trackedUpdateRate(20)
					.build());

		FISH_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("fc_fish_entity"),
				EntityType.Builder.<FCFishEntity>create(FCFishEntity::new, SpawnGroup.WATER_AMBIENT)
					.dimensions(0.5F, 0.3F).eyeHeight(0.195F).maxTrackingRange(4)
					.build());
		FabricDefaultAttributeRegistry.register(FISH_ENTITY, FCFishEntity.createMobAttributes());

		FCModels.initModels();
		EntityRendererRegistry.register(DEREK_ENTITY, FishermanEntityRenderer::new);
		EntityRendererRegistry.register(HOOK_ENTITY, HookEntityRenderer::new);
		EntityRendererRegistry.register(HARPOON_ENTITY, HarpoonEntityRenderer::new);
		EntityRendererRegistry.register(LINE_ARROW_ENTITY, LineArrowEntityRenderer::new);
		EntityRendererRegistry.register(FISH_ENTITY, FCModels::init);
	}

}

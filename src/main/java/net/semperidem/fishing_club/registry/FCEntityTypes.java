package net.semperidem.fishing_club.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.entity.*;

public class FCEntityTypes {

	public static EntityType<FishermanEntity> DEREK_ENTITY;
	public static EntityType<HookEntity> HOOK_ENTITY;
	public static EntityType<HarpoonEntity> HARPOON_ENTITY;
	public static EntityType<LineArrowEntity> LINE_ARROW_ENTITY;
	public static EntityType<FCFishEntity> FISH_ENTITY;
	public static EntityType<CustomBoatEntity> BOAT_ENTITY;



	public static void register() {

		DEREK_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("derek_entity"),
				EntityType.Builder.<FishermanEntity>create(FishermanEntity::new, SpawnGroup.CREATURE)
					.dimensions(0.6F, 1.95F)
					.attachment(EntityAttachmentType.PASSENGER, new Vec3d(0, 0, 0))
					.maxTrackingRange(4)
					.build());

		HOOK_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("hook_entity"),
				EntityType.Builder.<HookEntity>create(HookEntity::new, SpawnGroup.MISC)
					.dimensions(0.25f, 0.25f)
					.disableSaving()
					.maxTrackingRange(10)
					.build());

		HARPOON_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("harpoon_entity"),
				EntityType.Builder.<HarpoonEntity>create(HarpoonEntity::new, SpawnGroup.MISC)
					.dimensions(0.5f, 0.5f)
					.disableSummon()
					.maxTrackingRange(4)
					.trackingTickInterval(20)
					.build());

		LINE_ARROW_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("line_arrow_entity"),
				EntityType.Builder.create(LineArrowEntity::new, SpawnGroup.MISC)
					.dimensions(0.5f, 0.5f)
					.disableSummon()
					.disableSaving()
					.maxTrackingRange(4)
					.trackingTickInterval(20)
					.build());

		BOAT_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("custom_boat_entity"),
				EntityType.Builder.create(CustomBoatEntity::new, SpawnGroup.MISC)
					.dimensions(1.375F, 0.5f)
					.disableSaving()
					.trackingTickInterval(20)
					.maxTrackingRange(4)
					.build());

		FISH_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.getIdentifier("fish_entity"),
				EntityType.Builder.<FCFishEntity>create(FCFishEntity::new, SpawnGroup.WATER_AMBIENT)
					.dimensions(0.5F, 0.3F).eyeHeight(0.195F).maxTrackingRange(4)
					.build());

		FabricDefaultAttributeRegistry.register(DEREK_ENTITY, FishermanEntity.createMobAttributes());
		FabricDefaultAttributeRegistry.register(FISH_ENTITY, FCFishEntity.createMobAttributes());

	}

}

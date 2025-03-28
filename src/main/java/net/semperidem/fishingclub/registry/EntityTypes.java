package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.*;
import net.semperidem.fishingclub.fish.Species;

public class EntityTypes {

	public static EntityType<FishermanEntity> DEREK_ENTITY;
	public static EntityType<HookEntity> HOOK_ENTITY;
	public static EntityType<CustomBoatEntity> BOAT_ENTITY;



	public static void register() {
		Species.Library.register();

		DEREK_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.identifier("derek_entity"),
				EntityType.Builder.<FishermanEntity>create(FishermanEntity::new, SpawnGroup.CREATURE)
					.dimensions(0.6F, 1.95F)
					.attachment(EntityAttachmentType.PASSENGER, new Vec3d(0, 0, 0))
					.maxTrackingRange(4)
					.build());

		HOOK_ENTITY =
			Registry.register(
				Registries.ENTITY_TYPE,
				FishingClub.identifier("hook_entity"),
				EntityType.Builder.<HookEntity>create(HookEntity::new, SpawnGroup.MISC)
					.dimensions(0.25f, 0.25f)
					.disableSaving()
					.maxTrackingRange(10)
					.build());

		BOAT_ENTITY =
				Registry.register(
						Registries.ENTITY_TYPE,
						FishingClub.identifier("custom_boat_entity"),
						EntityType.Builder.create(CustomBoatEntity::new, SpawnGroup.MISC)
								.dimensions(1.375F, 0.5f)
								.disableSaving()
								.trackingTickInterval(20)
								.maxTrackingRange(4)
								.build());


		FabricDefaultAttributeRegistry.register(DEREK_ENTITY, FishermanEntity.createMobAttributes());


	}

}

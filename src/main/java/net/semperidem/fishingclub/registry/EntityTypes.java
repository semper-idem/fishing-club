package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAttachmentType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.Vec3d;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.CustomBoatEntity;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntity;

public class EntityTypes {

    public static EntityType<FishermanEntity> FISHERMAN;
    public static EntityType<HookEntity> HOOK;
    public static EntityType<CustomBoatEntity> BOAT;

    public static EntityType<ButterfishEntity> BUTTERFISH;

    private static <T extends Entity> EntityType<T> register(RegistryKey<EntityType<?>> key, EntityType.Builder<T> type) {
        return Registry.register(Registries.ENTITY_TYPE, key, type.build(key));
    }

    private static <T extends Entity> EntityType<T> register(String id, EntityType.Builder<T> type) {
        return register(keyOf(id), type);
    }

    private static RegistryKey<EntityType<?>> keyOf(String id) {
        return RegistryKey.of(RegistryKeys.ENTITY_TYPE, FishingClub.identifier(id));
    }


    public static void register() {

        FISHERMAN = register("fisherman", EntityType.Builder.<FishermanEntity>create(FishermanEntity::new, SpawnGroup.CREATURE)
                        .dimensions(0.6F, 1.95F)
                        .attachment(EntityAttachmentType.PASSENGER, new Vec3d(0, 0, 0))
                        .maxTrackingRange(4)
        );

        HOOK = register("hook", EntityType.Builder.<HookEntity>create(HookEntity::new, SpawnGroup.MISC)
                        .dimensions(0.25f, 0.25f)
                        .disableSaving()
                        .maxTrackingRange(10)
        );

        BOAT = register("boat", EntityType.Builder.create(CustomBoatEntity::new, SpawnGroup.MISC)
                        .dimensions(1.375F, 0.5f)
                        .disableSaving()
                        .trackingTickInterval(20)
                        .maxTrackingRange(4)
        );
        Species.Library.init();
        BUTTERFISH = register(Species.Library.BUTTERFISH.name(), EntityType.Builder.create(ButterfishEntity::new, SpawnGroup.WATER_AMBIENT)
                        .dimensions(0.5f,0.3f)
                        .eyeHeight(0.2f)
                        .maxTrackingRange(4)
        );
        Species.Library.register();
        FabricDefaultAttributeRegistry.register(FISHERMAN, FishermanEntity.createMobAttributes());


    }

}

package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntityRenderer;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.FishermanEntityRenderer;

public class FEntityRegistry {

    public static final EntityType<FishermanEntity> FISHERMAN = Registry.register(
            Registry.ENTITY_TYPE,
            FishingClub.getIdentifier("fisherman"),
            FabricEntityTypeBuilder.<FishermanEntity>create(SpawnGroup.CREATURE, (entityType, world) -> new FishermanEntity(world))
                    .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                    .build()
    );
    public static final EntityType<CustomFishingBobberEntity> CUSTOM_FISHING_BOBBER = Registry.register(
            Registry.ENTITY_TYPE,
            FishingClub.getIdentifier("custom_fishing_bobber"),
            FabricEntityTypeBuilder.<CustomFishingBobberEntity>create(SpawnGroup.MISC, CustomFishingBobberEntity::new)
                    .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                    .disableSummon()
                    .disableSaving()
                    .trackRangeBlocks(128)
                    .trackedUpdateRate(5)
                    .build()
    );

    public static void register(){
        FabricDefaultAttributeRegistry.register(FISHERMAN, FishermanEntity.createMobAttributes());
        EntityRendererRegistry.register(FISHERMAN, FishermanEntityRenderer::new);
        EntityRendererRegistry.register(CUSTOM_FISHING_BOBBER, CustomFishingBobberEntityRenderer::new);

    }
}

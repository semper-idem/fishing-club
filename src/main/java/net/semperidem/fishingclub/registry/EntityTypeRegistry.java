package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.HarpoonEntity;
import net.semperidem.fishingclub.entity.LineArrowEntity;
import net.semperidem.fishingclub.entity.renderer.CustomFishingBobberEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.FishermanEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.HarpoonEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.LineArrowEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.entity.renderer.model.HarpoonEntityModel;

public class EntityTypeRegistry {

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

    public static final EntityType<HarpoonEntity> HARPOON_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            FishingClub.getIdentifier("harpoon_entity"),
            FabricEntityTypeBuilder.<HarpoonEntity>create(SpawnGroup.MISC, HarpoonEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .disableSummon()
                    .disableSaving()
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(20)
                    .build()
    );

    public static final EntityType<LineArrowEntity> LINE_ARROW_ENTITY = Registry.register(
            Registry.ENTITY_TYPE,
            FishingClub.getIdentifier("line_arrow_entity"),
            FabricEntityTypeBuilder.<LineArrowEntity>create(SpawnGroup.MISC, LineArrowEntity::new)
                    .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                    .disableSummon()
                    .disableSaving()
                    .trackRangeBlocks(64)
                    .trackedUpdateRate(20)
                    .build()
    );
    public static final EntityModelLayer MODEL_HARPOON_LAYER = new EntityModelLayer(FishingClub.getIdentifier("harpoon_rod"), "main");
    public static final EntityModelLayer MODEL_FISHERMAN_LAYER = new EntityModelLayer(FishingClub.getIdentifier("fisherman"), "main");


    public static void register(){
        EntityModelLayerRegistry.registerModelLayer(MODEL_HARPOON_LAYER, HarpoonEntityModel::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(MODEL_FISHERMAN_LAYER, FishermanEntityModel::getTextureModelData);

        FabricDefaultAttributeRegistry.register(FISHERMAN, FishermanEntity.createMobAttributes());

        EntityRendererRegistry.register(FISHERMAN, FishermanEntityRenderer::new);
        EntityRendererRegistry.register(CUSTOM_FISHING_BOBBER, CustomFishingBobberEntityRenderer::new);
        EntityRendererRegistry.register(HARPOON_ENTITY, HarpoonEntityRenderer::new);
        EntityRendererRegistry.register(LINE_ARROW_ENTITY, LineArrowEntityRenderer::new);


    }
}

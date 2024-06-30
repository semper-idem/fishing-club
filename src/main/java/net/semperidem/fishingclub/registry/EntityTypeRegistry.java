package net.semperidem.fishingclub.registry;

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
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.entity.FishermanEntity;
import net.semperidem.fishingclub.entity.HarpoonEntity;
import net.semperidem.fishingclub.entity.LineArrowEntity;
import net.semperidem.fishingclub.entity.renderer.FishermanEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.HarpoonEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.LineArrowEntityRenderer;
import net.semperidem.fishingclub.entity.renderer.model.FishermanEntityModel;
import net.semperidem.fishingclub.entity.renderer.model.HarpoonEntityModel;

public class EntityTypeRegistry {

  public static EntityType<FishermanEntity> DEREK_ENTITY;
  public static EntityType<HookEntity> HOOK_ENTITY;
  public static EntityType<HarpoonEntity> HARPOON_ENTITY;
  public static EntityType<LineArrowEntity> LINE_ARROW_ENTITY;

  public static final EntityModelLayer MODEL_HARPOON_LAYER =
      new EntityModelLayer(FishingClub.getIdentifier("harpoon_rod"), "main");
  public static final EntityModelLayer MODEL_FISHERMAN_LAYER =
      new EntityModelLayer(FishingClub.getIdentifier("fisherman"), "main");

  public static void register() {
    EntityModelLayerRegistry.registerModelLayer(
        MODEL_HARPOON_LAYER, HarpoonEntityModel::getTexturedModelData);
    EntityModelLayerRegistry.registerModelLayer(
        MODEL_FISHERMAN_LAYER, FishermanEntityModel::getTextureModelData);

    FabricDefaultAttributeRegistry.register(DEREK_ENTITY, FishermanEntity.createMobAttributes());

    DEREK_ENTITY =
        Registry.register(
            Registries.ENTITY_TYPE,
            FishingClub.getIdentifier("derek_entity"),
            FabricEntityTypeBuilder.<FishermanEntity>create(
                    SpawnGroup.CREATURE, (entityType, world) -> new FishermanEntity(world))
                .dimensions(EntityDimensions.fixed(0.6f, 1.95f))
                .build());

    HOOK_ENTITY =
        Registry.register(
            Registries.ENTITY_TYPE,
            FishingClub.getIdentifier("hook_entity"),
            FabricEntityTypeBuilder.<HookEntity>create(SpawnGroup.MISC, HookEntity::new)
                .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                .disableSummon()
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
    
    EntityRendererRegistry.register(DEREK_ENTITY, FishermanEntityRenderer::new);
   // EntityRendererRegistry.register(HOOK_ENTITY, HookEntityRenderer::new);
    EntityRendererRegistry.register(HARPOON_ENTITY, HarpoonEntityRenderer::new);
    EntityRendererRegistry.register(LINE_ARROW_ENTITY, LineArrowEntityRenderer::new);
  }
}

package net.semperidem.fishingclub.fish;

import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.Heightmap;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntity;
import net.semperidem.fishingclub.item.FishItem;

import java.util.function.Predicate;

public class SpeciesBuilder<T extends AbstractFishEntity> {
    private final Species<T> species;


    private SpeciesBuilder(String speciesName) {
        this.species = new Species<>();

        this.species.name = speciesName.toLowerCase().replace(" ", "_");
        this.species.label = speciesName;

        this.species.id = Species.Library.SPECIES_BY_NAME.size();

        this.species.entityId = FishingClub.identifier(this.species.name);
        this.species.modelId = new ModelIdentifier(FishingClub.identifier(this.species.textureName() + "_item_3d"), "inventory");
        this.species.albinoModelId =new ModelIdentifier(FishingClub.identifier(this.species.textureName(true) + "_item_3d"), "inventory");
        this.species.layerId =  new EntityModelLayer(FishingClub.identifier(this.species.textureName()), "main");
   }

    public static <T extends AbstractFishEntity> SpeciesBuilder<T> create(String speciesName) {
        return new SpeciesBuilder<>(speciesName);
    }

    public SpeciesBuilder<T> level(int level) {
        this.species.level = level;
        return this;
    }

    public SpeciesBuilder<T> rarity(float rarity) {
        this.species.rarity = rarity;
        return this;
    }

    public SpeciesBuilder<T> lengthMinAndRange(float minLength, float lengthRange) {
        this.species.minLength = minLength;
        this.species.lengthRange = lengthRange;
        return this;
    }

    public SpeciesBuilder<T> weightMinAndRange(float minWeight, float weightRange){
        this.species.minWeight = minWeight;
        this.species.weightRange = weightRange;
        return this;
    }

    public SpeciesBuilder<T> movement(MovementPattern movement) {
        this.species.movement = movement;
        return this;
    }

    public SpeciesBuilder<T> staminaLevel(int staminaLevel) {
        this.species.staminaLevel = staminaLevel;
        return this;
    }
    public SpeciesBuilder<T> spawnBiome(Predicate<BiomeSelectionContext> biomes, int weight, int min, int max) {
        BiomeModifications.addSpawn(
                biomes,
                SpawnGroup.WATER_AMBIENT,
                this.species.entityType,
                weight,
                min,
                max
        );
        SpawnRestriction.register(
                this.species.entityType,
                SpawnLocationTypes.IN_WATER,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES,
                WaterCreatureEntity::canSpawn
        );
        return this;
    }

    public SpeciesBuilder<T> entity(EntityType.EntityFactory<T> entityFactory) {
        this.species.entityType = Registry.register(Registries.ENTITY_TYPE,
                this.species.entityId,
                EntityType.Builder.create(
                                entityFactory, SpawnGroup.WATER_AMBIENT)
                        .dimensions(0.5f,0.3f)
                        .eyeHeight(0.2f)
                        .maxTrackingRange(4)
                        .build()
        );
        FabricDefaultAttributeRegistry.register(
                this.species.entityType,
                ButterfishEntity.createMobAttributes()
        );

        this.species.item = Registry.register(
                Registries.ITEM,
                this.species.entityId,
                new FishItem(new Item.Settings().food(FoodComponents.TROPICAL_FISH))
        );

        return this;
    }

    public SpeciesBuilder<T> renderer(EntityRendererFactory<T> entityRendererSupplier) {
        this.species.entityRendererSupplier = entityRendererSupplier;
        return this;
    }

    public SpeciesBuilder<T> texturedModel(EntityModelLayerRegistry.TexturedModelDataProvider texturedModelDataProvider) {
        this.species.texturedModelDataProvider = texturedModelDataProvider;
        return this;
    }
    public Species<T> build() {
        if (this.species.entityRendererSupplier == null) {//todo logger
            System.out.println("Missing  EntityRender  for:  " + this.species.name);
        }
        Species.Library.add(this.species);
        return this.species;
    }
}

package net.semperidem.fishingclub.fish;

import com.google.common.collect.Range;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnLocationTypes;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.world.Heightmap;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.item.FishItem;
import net.semperidem.fishingclub.registry.Items;

import java.util.function.Predicate;

public class SpeciesBuilder<T extends WaterCreatureEntity> {
    private final Species<T> species;


    private SpeciesBuilder(String speciesName) {
        this.species = new Species<>();

        this.species.name = speciesName.toLowerCase().replace(" ", "_");
        this.species.label = speciesName.substring(0, 1).toUpperCase() + speciesName.substring(1);

        this.species.id = Species.Library.SPECIES_BY_NAME.size();
   }

    private SpeciesBuilder(Species<T> source) {
        this.species = source;
    }

    public static <T extends WaterCreatureEntity> SpeciesBuilder<T> create(String speciesName) {
        return new SpeciesBuilder<>(speciesName);
    }

    public static <T extends WaterCreatureEntity> SpeciesBuilder<T> create(Species<T> source) {
        return new SpeciesBuilder<>(source);
    }

    public SpeciesBuilder<T> level(int level) {
        this.species.level = level;
        return this;
    }

    public SpeciesBuilder <T> rarity(float rarity) {
        this.species.rarity = rarity;
        return this;
    }


    public SpeciesBuilder <T> lengthMinAndRange(float minLength, float lengthRange) {
        this.species.minLength = minLength;
        this.species.lengthRange = lengthRange;
        return this;
    }

    public SpeciesBuilder <T> weightMinAndRange(float minWeight, float weightRange){
        this.species.minWeight = minWeight;
        this.species.weightRange = weightRange;
        return this;
    }

    public SpeciesBuilder <T> movement(MovementPattern movement) {
        this.species.movement = movement;
        return this;
    }

    public SpeciesBuilder <T> staminaLevel(int staminaLevel) {
        this.species.staminaLevel = staminaLevel;
        return this;
    }


    public SpeciesBuilder <T> temperature(Range<Float> temperature) {
        this.species.temperature = temperature;
        return this;
    }

    public SpeciesBuilder <T> depth(Range<Float> depth) {
        this.species.depth = depth;
        return this;
    }

    public SpeciesBuilder <T> altitude(Range<Float> altitude) {
        this.species.altitude = altitude;
        return this;
    }

    public SpeciesBuilder <T> saltiness(Range<Float> saltiness) {
        this.species.saltiness = saltiness;
        return this;
    }

    public SpeciesBuilder <T> spawnBiome(
            Predicate<BiomeSelectionContext> spawnCondition,
            int weight,
            int min,
            int max
    ) {
        BiomeModifications.addSpawn(
                spawnCondition,
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
                (type, world, spawnReason, pos, random) -> AbstractFishEntity.canSpawn(this.species, type, world, spawnReason, pos, random)
        );
        return this;
    }

    public SpeciesBuilder<T> entity(EntityType<T> entityType) {
        this.species.entityType = entityType;

        return this;
    }

    public SpeciesBuilder<T> item(Item item) {
        this.species.item = item;
        return this;
    }
    public SpeciesBuilder <T> item() {
        this.species.item = Registry.register(
                Registries.ITEM,
                FishingClub.identifier(this.species.name),
                new FishItem(new Item.Settings().registryKey(Items.keyOf(this.species.name)).food(FoodComponents.TROPICAL_FISH))
        );

        return this;
    }

    public SpeciesBuilder<T> attributes(DefaultAttributeContainer.Builder attributesBuilder) {

        FabricDefaultAttributeRegistry.register(
                this.species.entityType,
                attributesBuilder
                );
        return this;
    }
    public Species <T> build() {
        Species.Library.add(this.species);
        return this.species;
    }
}

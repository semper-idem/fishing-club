package net.semperidem.fishingclub.fish;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.mob.WaterCreatureEntity;
import net.minecraft.entity.passive.CodEntity;
import net.minecraft.entity.passive.PufferfishEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.entity.passive.TropicalFishEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntity;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityModel;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityRenderer;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.util.MathUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

public class Species<T extends WaterCreatureEntity> {

    int id;

    String label;
    String name;
    MovementPattern movement;
    int level = SpecimenData.MIN_LEVEL;
    int staminaLevel = 1;
    float minLength;
    float lengthRange;
    float minWeight;
    float weightRange;

    /**
     * Positive decimal point number
     * In-direct inverse of weight
     * the lowers the number, the higher chance of getting hooked and/or appearing since minimum acceptable quality of chunk is lower
     * 100 rarity translate to minimum chunk quality of 1 (logarithmic)
     */
    float rarity;

    EntityType<T> entityType;
    Item item;

    EntityRendererFactory<T> entityRendererSupplier;
    EntityModelLayerRegistry.TexturedModelDataProvider texturedModelDataProvider;

    Identifier entityId;
    EntityModelLayer layerId;
    ModelIdentifier modelId;
    ModelIdentifier albinoModelId;

    public String name() {
        return this.name;
    }

    public String label() {
        return this.label;
    }

    public EntityType<T> getEntityType() {
        return this.entityType;
    }

    public EntityModelLayer getLayerId() {
        return this.layerId;
    }

    public float rarity() {
        return rarity;
    }

    public int level() {
        return this.level;
    }

    public float length() {
        return minLength;
    }

    public float lengthInRange(float mean) {
       return (float) MathUtil.normal(minLength, weightRange, mean);
    }

    public float lengthPercentile(float length) {
        return (length - this.minLength) / this.lengthRange;
    }

    public float lengthScale(float length) {
        return this.sizeScale(this.lengthPercentile(length));
    }

    public float weight() {
        return this.minWeight;
    }

    public float weightScale(float weight) {
        return this.sizeScale(this.weightPercentile(weight));
    }

    public float weightPercentile(float weight) {
        return (weight - this.minWeight) / this.weightRange;
    }

    public float weightInRange(float mean) {
        return (float) MathUtil.normal(this.minWeight, this.weightRange, mean);
    }

    public float sizeScale(float scalePercentile) {
        return 1 - HALF_SCALE_RANGE  + scalePercentile * SCALE_RANGE;
    }

    public boolean weird(float weight, float length) {
        return Math.abs(this.weightScale(weight) - this.lengthScale(length)) > SCALE_RANGE * WEIRD_RANGE;
    }

    public Item item() {
        return this.item;
    }

    public MovementPattern movement() {
        return movement;
    }

    public int staminaLevel() {
        return staminaLevel;
    }

    public String textureName() {
        return textureName(false);
    }

    public String textureName(boolean isAlbino) {
        return "fish/" + name + "/" + name + (isAlbino ? "_albino" : "");
    }

    public Identifier texture(boolean isAlbino) {
        return FishingClub.identifier("textures/entity/" + this.textureName(isAlbino) +".png");
    }

    public ModelIdentifier albinoModelId() {
        return this.albinoModelId;
    }

    public ModelIdentifier modelId() {
        return this.modelId;
    }

    public void registerClient() {
        if (entityRendererSupplier == null) {
            return;
        }
        EntityRendererRegistry.register(
                this.entityType,
                this.entityRendererSupplier
        );

        EntityModelLayerRegistry.registerModelLayer(
                this.layerId,
                this.texturedModelDataProvider
        );
}

    public static final double WEIRD_RANGE = 0.7D;
    static final float SCALE_RANGE = 0.6f;
    static final float HALF_SCALE_RANGE = SCALE_RANGE * 0.5f;

    public static class Library<T extends WaterCreatureEntity> {

        private static final Predicate<BiomeSelectionContext> COD_SPAWN = BiomeSelectors.spawnsOneOf(EntityType.COD);
//        private static final Predicate<BiomeSelectionContext> ANY = context -> true;
//        private static final Predicate<BiomeSelectionContext> COLD = context -> context.getBiome().getTemperature() <= -1.2;
//        private static final Predicate<BiomeSelectionContext> NORMAL = context -> context.getBiome().getTemperature() < 1 && context.getBiome().getTemperature() >= 0.2;
//        private static final Predicate<BiomeSelectionContext> HOT = context -> context.getBiome().getTemperature() >= 1;
/*
*
  "entity.minecraft.tropical_fish.predefined.0": "Anemone",
  "entity.minecraft.tropical_fish.predefined.1": "Black Tang",
  "entity.minecraft.tropical_fish.predefined.2": "Blue Tang",
  "entity.minecraft.tropical_fish.predefined.3": "Butterflyfish",
  "entity.minecraft.tropical_fish.predefined.4": "Cichlid",
  "entity.minecraft.tropical_fish.predefined.5": "Clownfish",
  "entity.minecraft.tropical_fish.predefined.6": "Cotton Candy Betta",
  "entity.minecraft.tropical_fish.predefined.7": "Dottyback",
  "entity.minecraft.tropical_fish.predefined.8": "Emperor Red Snapper",
  "entity.minecraft.tropical_fish.predefined.9": "Goatfish",
  "entity.minecraft.tropical_fish.predefined.10": "Moorish Idol",
  "entity.minecraft.tropical_fish.predefined.11": "Ornate Butterflyfish",
  "entity.minecraft.tropical_fish.predefined.12": "Parrotfish",
  "entity.minecraft.tropical_fish.predefined.13": "Queen Angelfish",
  "entity.minecraft.tropical_fish.predefined.14": "Red Cichlid",
  "entity.minecraft.tropical_fish.predefined.15": "Red Lipped Blenny",
  "entity.minecraft.tropical_fish.predefined.16": "Red Snapper",
  "entity.minecraft.tropical_fish.predefined.17": "Threadfin",
  "entity.minecraft.tropical_fish.predefined.18": "Tomato Clownfish",
  "entity.minecraft.tropical_fish.predefined.19": "Triggerfish",
  "entity.minecraft.tropical_fish.predefined.20": "Yellowtail Parrotfish",
  "entity.minecraft.tropical_fish.predefined.21": "Yellow Tang",
* */
        public static Species<ButterfishEntity> BUTTERFISH;
        public static Species<CodEntity> COD;
        public static Species<SalmonEntity> SOCKEYE_SALMON;
        public static Species<PufferfishEntity> PUFFERFISH;
        public static Species<TropicalFishEntity> TROPICAL_FISH;
        public static Species<?> DEFAULT;
        static HashMap<String, Species<?>> SPECIES_BY_NAME = new HashMap<>();

        public static void registerClient() {
            SPECIES_BY_NAME.values().forEach(Species::registerClient);
        }

        public static <T extends WaterCreatureEntity> Iterator<Species<?>> iterator() {
            return SPECIES_BY_NAME.values().iterator();
        }

        public static void register(){
            TROPICAL_FISH = SpeciesBuilder
                    .<TropicalFishEntity>create("Tropical Fish")
                    .level(1)
                    .rarity(1000)
                    .lengthMinAndRange(0, 1)
                    .weightMinAndRange(0, 1)
                    .staminaLevel(4)
                    .vanillaEntity(EntityType.TROPICAL_FISH)
                    .vanillaItem(Items.TROPICAL_FISH)
                    .build();


            PUFFERFISH = SpeciesBuilder
                    .<PufferfishEntity>create("Pufferfish")
                    .level(1)
                    .rarity(1000)
                    .lengthMinAndRange(0, 1)
                    .weightMinAndRange(0, 1)
                    .staminaLevel(4)
                    .vanillaEntity(EntityType.PUFFERFISH)
                    .vanillaItem(Items.PUFFERFISH)
                    .build();

            SOCKEYE_SALMON = SpeciesBuilder
                    .<SalmonEntity>create("Salmon")
                    .level(1)
                    .rarity(1000)
                    .lengthMinAndRange(0, 1)
                    .weightMinAndRange(0, 1)
                    .staminaLevel(4)
                    .vanillaEntity(EntityType.SALMON)
                    .vanillaItem(Items.SALMON)
                    .build();
            COD  = SpeciesBuilder
                    .<CodEntity>create("Cod")
                    .level(1)
                    .rarity(1)
                    .lengthMinAndRange(0, 1)
                    .weightMinAndRange(0, 1)
                    .movement(MovementPatterns.EASY1)
                    .staminaLevel(0)
                    .vanillaEntity(EntityType.COD)
                    .vanillaItem(Items.COD)
                    .build();
            BUTTERFISH = SpeciesBuilder
                    .<ButterfishEntity>create("Butterfish")
                    .level(9)
                    .rarity(1000)
                    .lengthMinAndRange(19, 40)
                    .weightMinAndRange(29, 90)
                    .movement(MovementPatterns.MID4)
                    .staminaLevel(0)
                    .entity(ButterfishEntity::new)
                    .attributes(ButterfishEntity.createFishAttributes())
                    .spawnBiome(COD_SPAWN, 15, 1, 2)
                    .texturedModel(ButterfishEntityModel::getTexturedModelData)
                    .renderer(ButterfishEntityRenderer::new)
                    .build();


            DEFAULT = COD;
        }

        public static <T extends WaterCreatureEntity> void add(Species<T> species) {
            SPECIES_BY_NAME.put(species.name, species);
        }

        public static Species<?> fromName(String name) {
            return SPECIES_BY_NAME.getOrDefault(name, DEFAULT);
        }
    }

}

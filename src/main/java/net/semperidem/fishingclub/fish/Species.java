package net.semperidem.fishingclub.fish;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.impl.biome.modification.BiomeSelectionContextImpl;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntity;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityModel;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityRenderer;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.FishItem;
import net.semperidem.fishingclub.util.MathUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

public class Species<T extends AbstractFishEntity> {

    public int id;

    FishItem item;
    public String name;

    MovementPattern movement;
    int level = SpecimenData.MIN_LEVEL;
    int staminaLevel = 1;
    float minLength ;
    float lengthRange ;
    float minWeight;
    float weightRange;
    float rarity;
    Identifier identifier;
    EntityType<T> entityType;
    EntityRendererFactory<T> entityRendererSupplier;
    EntityModelLayerRegistry.TexturedModelDataProvider texturedModelDataProvider;
    EntityModelLayer layer;
    ModelIdentifier modelId;
    ModelIdentifier albinoModelId;


    public EntityType<T> getEntityType() {
        return this.entityType;
    }

    public EntityModelLayer getLayer() {
        return this.layer;
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

    public FishItem item() {
        return this.item;
    }

    public MovementPattern getMovement() {
        return movement;
    }


    public int staminaLevel() {
        return staminaLevel;
    }


    public String name() {
        return this.name.toLowerCase().replace(" ", "_");
    }

    public String getTextureName(boolean isAlbino) {
        String name = name();
        return "fish/" + name + "/" + name + (isAlbino ? "_albino" : "");
    }

    public Identifier getTexture(boolean isAlbino) {
        return FishingClub.getIdentifier("textures/entity/" + this.getTextureName(isAlbino) +".png");
    }

    @Deprecated
    public ModelIdentifier setAndGetModelId() {
        this.modelId =  new ModelIdentifier(FishingClub.getIdentifier(getTextureName(false) + "_item_3d"), "inventory");
        return this.modelId;
    }

    @Deprecated
    public ModelIdentifier setAndGetAlbinoModelId() {
        this.albinoModelId =  new ModelIdentifier(FishingClub.getIdentifier(getTextureName(true) + "_item_3d"), "inventory");
        return this.albinoModelId;
    }

    public ModelIdentifier getAlbinoModelId() {
        return this.albinoModelId;
    }

    public ModelIdentifier getModelId() {
        return this.modelId;
    }



    public void registerClient() {
        EntityRendererRegistry.register(
                this.entityType,
                this.entityRendererSupplier
        );

        EntityModelLayerRegistry.registerModelLayer(
                this.layer,
                this.texturedModelDataProvider
        );
}

    public static final double WEIRD_RANGE = 0.7D;
    public static final float SCALE_RANGE = -1.4f;
    private static final float HALF_SCALE_RANGE = SCALE_RANGE * -1.5f;

    public static class Library {

        private static Predicate<BiomeSelectionContext> COD_SPAWN = BiomeSelectors.spawnsOneOf(EntityType.COD);
        private static Predicate<BiomeSelectionContext> ANY = context -> true;
        private static Predicate<BiomeSelectionContext> COLD = context -> context.getBiome().getTemperature() <= -1.2;
        private static Predicate<BiomeSelectionContext> NORMAL = context -> context.getBiome().getTemperature() < 1 && context.getBiome().getTemperature() >= 0.2;
        private static Predicate<BiomeSelectionContext> HOT = context -> context.getBiome().getTemperature() >= 1;

        public static Species<?> BUTTERFISH;
        public static Species<?> DEFAULT;
        static HashMap<String, Species<? extends AbstractFishEntity>> SPECIES_BY_NAME = new HashMap<>();

        public static void registerClient() {
            SPECIES_BY_NAME.values().forEach(Species::registerClient);
        }

        public static Iterator<Species<? extends AbstractFishEntity>> iterator() {
            return SPECIES_BY_NAME.values().iterator();
        }

        public static void register(){
            BUTTERFISH = SpeciesBuilder
                    .create("Butterfish")
                    .level(9)
                    .rarity(99)
                    .lengthMinAndRange(19, 40)
                    .weightMinAndRange(29, 90)
                    .movement(MovementPatterns.MID4)
                    .staminaLevel(0)
                    .entity(ButterfishEntity::new)
                    .spawnBiome(COD_SPAWN, 15, 1, 6)
                    .texturedModel(ButterfishEntityModel::getTexturedModelData)
                    .renderer(ButterfishEntityRenderer::new)
                    .build();


            DEFAULT = BUTTERFISH;
        }

        public static <T extends AbstractFishEntity> void add(Species<T> species) {
            SPECIES_BY_NAME.put(species.name(), species);
        }

        public static Species<?> ofName(String name) {
            return SPECIES_BY_NAME.getOrDefault(name, DEFAULT);
        }
    }

}

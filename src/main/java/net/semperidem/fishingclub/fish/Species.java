package net.semperidem.fishingclub.fish;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.CodEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SalmonEntityModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntity;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityModel;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityRenderer;
import net.semperidem.fishingclub.fish.species.cod.CodEntity;
import net.semperidem.fishingclub.fish.species.cod.CodEntityRenderer;
import net.semperidem.fishingclub.fish.species.sockeyesalmon.SockeyeSalmonEntity;
import net.semperidem.fishingclub.fish.species.sockeyesalmon.SockeyeSalmonEntityRenderer;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.FishItem;
import net.semperidem.fishingclub.util.MathUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Predicate;

public class Species<T extends AbstractFishEntity> {

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
    FishItem item;

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

    public FishItem item() {
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
    public static final float SCALE_RANGE = -1.4f;
    private static final float HALF_SCALE_RANGE = SCALE_RANGE * -1.5f;

    public static class Library {

        private static final Predicate<BiomeSelectionContext> COD_SPAWN = BiomeSelectors.spawnsOneOf(EntityType.COD);
//        private static final Predicate<BiomeSelectionContext> ANY = context -> true;
//        private static final Predicate<BiomeSelectionContext> COLD = context -> context.getBiome().getTemperature() <= -1.2;
//        private static final Predicate<BiomeSelectionContext> NORMAL = context -> context.getBiome().getTemperature() < 1 && context.getBiome().getTemperature() >= 0.2;
//        private static final Predicate<BiomeSelectionContext> HOT = context -> context.getBiome().getTemperature() >= 1;

        public static Species<?> BUTTERFISH;
        public static Species<?> COD;
        public static Species<?> SOCKEYE_SALMON;
        public static Species<?> DEFAULT;
        static HashMap<String, Species<? extends AbstractFishEntity>> SPECIES_BY_NAME = new HashMap<>();

        public static void registerClient() {
            SPECIES_BY_NAME.values().forEach(Species::registerClient);
        }

        public static Iterator<Species<? extends AbstractFishEntity>> iterator() {
            return SPECIES_BY_NAME.values().iterator();
        }

        public static void register(){
            SOCKEYE_SALMON = SpeciesBuilder.create("Salmon")
                    .level(1)
                    .rarity(100)
                    .lengthMinAndRange(0, 1)
                    .weightMinAndRange(0, 1)
                    .staminaLevel(4)
                    .entity(SockeyeSalmonEntity::new)
                    .texturedModel(SalmonEntityModel::getTexturedModelData)
                    .renderer(SockeyeSalmonEntityRenderer::new)
                    .build();
            COD  = SpeciesBuilder
                    .create("Cod")
                    .level(1)
                    .rarity(1)
                    .lengthMinAndRange(0, 1)
                    .weightMinAndRange(0, 1)
                    .movement(MovementPatterns.EASY1)
                    .staminaLevel(0)
                    .entity(CodEntity::new)//need to add OUR vanilla fish
                    .texturedModel(CodEntityModel::getTexturedModelData)
                    .renderer(CodEntityRenderer::new)
                    .build();
            BUTTERFISH = SpeciesBuilder
                    .create("Butterfish")
                    .level(9)
                    .rarity(99)
                    .lengthMinAndRange(19, 40)
                    .weightMinAndRange(29, 90)
                    .movement(MovementPatterns.MID4)
                    .staminaLevel(0)
                    .entity(ButterfishEntity::new)
                    .spawnBiome(COD_SPAWN, 15, 1, 2)
                    .texturedModel(ButterfishEntityModel::getTexturedModelData)
                    .renderer(ButterfishEntityRenderer::new)
                    .build();


            DEFAULT = COD;
        }

        public static <T extends AbstractFishEntity> void add(Species<T> species) {
            SPECIES_BY_NAME.put(species.name, species);
        }

        public static Species<?> ofName(String name) {
            return SPECIES_BY_NAME.getOrDefault(name, DEFAULT);
        }
    }

}

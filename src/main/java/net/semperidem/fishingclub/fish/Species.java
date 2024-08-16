package net.semperidem.fishingclub.fish;

import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
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
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.FishItem;
import net.semperidem.fishingclub.util.MathUtil;

import java.util.function.Predicate;

public class Species<T extends AbstractFishEntity> {

    public static int ID_COUNTER = 1;
    public final int id;

    private FishItem item;
    private EntityType<FishEntity> type;

    public final String name;

    private MovementPattern movement;
    private int level = SpecimenData.MIN_LEVEL;
    private int staminaLevel = 1;
    private float minLength ;
    private float lengthRange ;
    private float minWeight;
    private float weightRange;
    private float rarity;
    private Predicate<BiomeSelectionContext> spawnBiomes = ANY;
    private Identifier identifier;
    private EntityType<T> entityType;
    private EntityType.EntityFactory<T> entityFactory;
    private EntityRendererFactory<T> entityRendererSupplier;
    private EntityModelLayerRegistry.TexturedModelDataProvider texturedModelDataProvider;
    private EntityModelLayer layer;
    private ModelIdentifier modelId;
    private ModelIdentifier albinoModelId;


    public EntityType<T> getEntityType() {
        return this.entityType;
    }
    public void register() {
        this.entityType = Registry.register(Registries.ENTITY_TYPE,
            this.identifier,
            EntityType.Builder.create(
               entityFactory, SpawnGroup.WATER_AMBIENT)
                .dimensions(0.5f,0.3f)
                .eyeHeight(0.2f)
                .maxTrackingRange(4)
                .build()
            );
        FabricDefaultAttributeRegistry.register(
            this.entityType,
            ButterfishEntity.createMobAttributes()
        );

        this.item = Registry.register(
            Registries.ITEM,
            this.identifier,
            new FishItem(new Item.Settings().food(FoodComponents.TROPICAL_FISH))
        );
    }

    public void registerClient() {

        EntityRendererRegistry.register(this.entityType, this.entityRendererSupplier);
        EntityModelLayer modelLayer = new EntityModelLayer(FishingClub.getIdentifier(getTextureName(false)), "main");
        EntityModelLayerRegistry.registerModelLayer(modelLayer, this.texturedModelDataProvider);
        this.layer = modelLayer;
    }

    public static Species<?> of(String name) {
        return SpeciesLibrary.ALL_FISH_TYPES.getOrDefault( name, SpeciesLibrary.DEFAULT);
    }
    Species(String name) {
        this.name = name;
        this.id = ID_COUNTER;
        ID_COUNTER++;
        this.identifier = FishingClub.getIdentifier(this.name());
    }

    public Species<T> build() {
        SpeciesLibrary.ALL_FISH_TYPES.put(this.name, this);
        return this;
    }

    public EntityModelLayer getLayer() {
        return this.layer;
    }

    public  Species<T> withEntity(EntityType.EntityFactory<T> entityFactory) {
        this.entityFactory =  entityFactory;
        return this;
    }

    public Species<T> withRenderer(EntityRendererFactory<T> entityRendererSupplier) {
        this.entityRendererSupplier = entityRendererSupplier;
        return this;
    }

    public  Species<T> withTexturedModel(EntityModelLayerRegistry.TexturedModelDataProvider texturedModelDataProvider) {
        this.texturedModelDataProvider = texturedModelDataProvider;
        return this;
    }

    public  Species<T> level(int level) {
        this.level = level;
        return this;
    }

    public int level() {
        return this.level;
    }

    public  Species<T> rarity(float rarity) {
        this.rarity = rarity;
        return this;
    }

    public float rarity() {
        return rarity;
    }

    public  Species<T> length(float minLength, float lengthRange) {
        this.minLength = minLength;
        this.lengthRange = lengthRange;
        return this;
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

    public  Species<T> weight(float minWeight, float weightRange){
       this.minWeight = minWeight;
       this.weightRange = weightRange;
       return this;
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

    public  Species<T> item(FishItem item) {
        this.item = item;
        return this;
    }

    public MovementPattern getMovement() {
        return movement;
    }

    public  Species<T> movement(MovementPattern movement) {
        this.movement = movement;
        return this;
    }

    public int staminaLevel() {
        return staminaLevel;
    }

    public  Species<T> staminaLevel(int staminaLevel) {
        this.staminaLevel = staminaLevel;
        return this;
    }


    public String name() {
        return this.name.toLowerCase().replace(" ", "_");
    }

    public String getTextureName(boolean isAlbino) {
        String name = name();
        return "fish/" + name + "/" + name + (isAlbino ? "_albino" : "");
    }

    public  Species<T> withSpawnBiome(Predicate<BiomeSelectionContext> biomes) {
        this.spawnBiomes = biomes;
        return this;
    }

    public boolean canSpawnIn(BiomeSelectionContext context) {
        return this.spawnBiomes != null && this.spawnBiomes.test(context);
    }

    public Identifier getTexture(boolean isAlbino) {
        return FishingClub.getIdentifier("textures/entity/" + this.getTextureName(isAlbino) +".png");
    }

    public ModelIdentifier setAndGetModelId() {
        this.modelId =  new ModelIdentifier(FishingClub.getIdentifier(getTextureName(false) + "_item_3d"), "inventory");
        return this.modelId;
    }


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

    public static final double WEIRD_RANGE = 0.7D;

    public static final float SCALE_RANGE = 0.4f;
    private static final float HALF_SCALE_RANGE = SCALE_RANGE * 0.5f;
    public static Predicate<BiomeSelectionContext> ANY = context -> true;
    public static Predicate<BiomeSelectionContext> COLD = context -> context.getBiome().getTemperature() <= 0.2;
    public static Predicate<BiomeSelectionContext> NORMAL = context -> context.getBiome().getTemperature() < 2 && context.getBiome().getTemperature() >= 0.2;
    public static Predicate<BiomeSelectionContext> HOT = context -> context.getBiome().getTemperature() >= 2;
}

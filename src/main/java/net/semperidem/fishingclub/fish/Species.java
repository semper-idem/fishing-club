package net.semperidem.fishingclub.fish;

import com.google.common.collect.Range;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntity;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityModel;
import net.semperidem.fishingclub.fish.species.butterfish.ButterfishEntityRenderer;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.util.MathUtil;

import net.minecraft.util.math.random.Random;
import java.util.*;
import java.util.function.Predicate;

public class Species<T extends WaterCreatureEntity> {
    public static Random RANDOM = Random.create();

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
    //-2 for end, -1 for freezing, 0 for other,1 for desert 2 for nether
    Range<Float> temperature = Range.closed(-2f, 2f);
    //0 for river, 1 for other, 2 for ocean
    Range<Float> saltiness = Range.closed(-2f, 2f);

    Range<Float> depth = Range.closed(0f, 256f);
    Range<Float> altitude = Range.closed(-128f, 374f);

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

    public float maxLength() {
        return this.minLength + this.lengthRange;
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

    public float maxWeight() {
        return this.minWeight + this.weightRange;
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

    public boolean isGiant(float weight, float length) {
        return this.weightScale(weight) > 1 || this.lengthScale(length) > 1;
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
        if (this.albinoModelId == null) {
            this.albinoModelId =new ModelIdentifier(FishingClub.identifier(this.textureName(true) + "_item_3d"), "inventory");
        }
        return this.albinoModelId;
    }

    public ModelIdentifier modelId() {
        if (this.modelId == null) {
            this.modelId = new ModelIdentifier(FishingClub.identifier(this.textureName() + "_item_3d"), "inventory");
        }
        return this.modelId;
    }

    public void registerClient() {
        if (entityRendererSupplier == null) {
            return;
        }

        this.layerId =  new EntityModelLayer(FishingClub.identifier(this.textureName()), "main");

        EntityRendererRegistry.register(
                this.entityType,
                this.entityRendererSupplier
        );

        EntityModelLayerRegistry.registerModelLayer(
                this.layerId,
                this.texturedModelDataProvider
        );
    }

    public boolean canHook(HookEntity hookEntity) {
        RodConfiguration.AttributeComposite attributes = hookEntity.getCaughtUsing().attributes();
        if (attributes.maxFishWeight() < this.minWeight) {
            return false;
        }
        RegistryEntry<Biome> biomeEntry = hookEntity.getWorld().getBiome(hookEntity.getBlockPos());
        if (!this.saltiness.contains(biomeSaltiness(biomeEntry))){
            return false;
        }
        if (!this.temperature.contains(biomeTemperature(biomeEntry))) {
            return false;
        }
        if (!this.altitude.contains((float) hookEntity.getY())) {
            return false;
        }
        PlayerEntity fisher = hookEntity.getPlayerOwner();
        if (fisher == null) {
            return true;
        }
        float castDepth = attributes.maxLineLength() - (float) fisher.getEyePos().relativize(hookEntity.getPos()).length();
        return this.depth.contains(castDepth);
    }
    static final float SCALE_RANGE = 0.6f;
    static final float HALF_SCALE_RANGE = SCALE_RANGE * 0.5f;

    private static float biomeSaltiness(RegistryEntry<Biome> biomeEntry) {
        if (biomeEntry.isIn(BiomeTags.IS_OCEAN)) {
            return 2;
        }
        if (!biomeEntry.isIn(BiomeTags.IS_RIVER)) {
            return 1;
        }
        return 0;
    }

    //prob could be done smarter:)
    private static float biomeTemperature(RegistryEntry<Biome> biomeEntry) {
        if (biomeEntry.isIn(BiomeTags.IS_NETHER)) {
            return 2;
        }
        if (biomeEntry.isIn(BiomeTags.IS_END)) {
            return -2;
        }
        float vanillaTemperature = biomeEntry.value().getTemperature();
        if (vanillaTemperature > 1) {
            return 1;
        }
        if (vanillaTemperature <= 0) {
            return -1;
        }
        return 0;
    }
    public static class Library {
        public static final Predicate<BiomeSelectionContext> COD_SPAWN = BiomeSelectors.spawnsOneOf(EntityType.COD);
        public static final Predicate<BiomeSelectionContext> NONE = context -> false;
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
        private static HashSet<Item> SELLABLE_ITEMS = new HashSet<>();

        public static void registerClient() {
            BUTTERFISH = SpeciesBuilder.create(BUTTERFISH)
                    .texturedModel(ButterfishEntityModel::getTexturedModelData)
                    .renderer(ButterfishEntityRenderer::new)
                    .build();
            SPECIES_BY_NAME.values().forEach(Species::registerClient);
        }

        public static <T extends WaterCreatureEntity> Iterator<Species<?>> iterator() {
            return SPECIES_BY_NAME.values().iterator();
        }

        public static Optional<Species<?>> drawRandom(IHookEntity hookEntity) {
            Random r = hookEntity.getRandom();
            int rNormal = (int) (Math.abs(r.nextGaussian() + 1) + 1) * 100;
            List<Species<?>> availableSpecies = Species.Library.values().stream().filter(s -> s.rarity() > rNormal).toList();
            if (availableSpecies.isEmpty()) {
                return Optional.empty();
            }
            return Optional.of(availableSpecies.get(r.nextInt(availableSpecies.size())));
        }

        public static Optional<Species<?>> drawRandom(HookEntity hookEntity) {
            final int[] lastWeight = {0};
            RodConfiguration.AttributeComposite rodAttributes = hookEntity.getCaughtUsing().attributes();
            float rarityMultiplier = rodAttributes.fishRarity();
            HashMap<Integer, Species<?>> weightedSpecies = new HashMap<>();
            SPECIES_BY_NAME.values().stream().filter(species -> species.canHook(hookEntity)).forEach(
                    species -> {
                        int range = (int) (lastWeight[0] + species.rarity * rarityMultiplier);
                        weightedSpecies.put(range, species);
                        lastWeight[0] = range;
                    }
            );
            float r = (float) (lastWeight[0] * Math.random());

            for(Integer weight : weightedSpecies.keySet()) {
                if (weight > r) {
                    return Optional.ofNullable(weightedSpecies.get(weight));
                }
            }
            return Optional.empty();
        }

        public static boolean isSellable(ItemStack itemStack) {
            return SELLABLE_ITEMS.contains(itemStack.getItem());
        }

        public static Collection<Species<?>> values() {
            return SPECIES_BY_NAME.values();
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
                    .saltiness(Range.atLeast(2F))
                    .temperature(Range.closed(1F, 1F))
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
                    .saltiness(Range.atLeast(2F))
                    .temperature(Range.closed(1F, 1F))
                    .build();

            SOCKEYE_SALMON = SpeciesBuilder
                    .<SalmonEntity>create("Salmon")
                    .level(1)
                    .rarity(1000)
                    .lengthMinAndRange(0, 1)
                    .weightMinAndRange(0, 1)
                    .staminaLevel(4)
                    .vanillaEntity(EntityType.SALMON)
                    .saltiness(Range.closed(0F, 2F))
                    .temperature(Range.closed(-1F, 0F))
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
                    .saltiness(Range.closed(1F, 2F))
                    .temperature(Range.closed(-1F, 0F))
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
                    .saltiness(Range.closed(1F, 1F))
                    .temperature(Range.closed(0F, 0F))
                    .build();


            DEFAULT = COD;
        }

        public static <T extends WaterCreatureEntity> void add(Species<T> species) {
            SPECIES_BY_NAME.put(species.name, species);
            SELLABLE_ITEMS.add(species.item);
        }

        public static Species<?> fromName(String name) {
            return SPECIES_BY_NAME.getOrDefault(name, DEFAULT);
        }
    }

}

package net.semperidem.fishing_club.fish;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.semperidem.fishing_club.item.FishItem;

public class Species {

    public FishItem item;

    public String name;

    final MovementPattern fishPattern;
    final int minLevel;
    final int staminaLevel;
    public final float fishMinLength;
    public final float fishRandomLength;
    public final float fishMinWeight;
    public final float fishRandomWeight;
    final float fishRarity;
    EntityModelLayerRegistry.TexturedModelDataProvider modelDataProvider;

    public static int idCount = 1;
    public final int id;

    //TODO refactor into factory
    public Species(
            String name,
            MovementPattern fishPattern,
            int minLevel,
            int fishEnergyLevel,
            float fishMinLength,
            float fishRandomLength,
            float fishMinWeight,
            float fishRandomWeight,
            float fishRarity)
    {
        this.name = name;
        this.fishPattern = fishPattern;
        this.minLevel = minLevel;
        this.staminaLevel = fishEnergyLevel;
        this.fishMinLength = fishMinLength;
        this.fishRandomLength = fishRandomLength;
        this.fishMinWeight = fishMinWeight;
        this.fishRandomWeight = fishRandomWeight;
        this.fishRarity = fishRarity;
        SpeciesLibrary.ALL_FISH_TYPES.put(name, this);
        this.id = idCount;
        idCount++;
    }

    public MovementPattern getFishPattern() {
        return fishPattern;
    }

    public int getStaminaLevel() {
        return staminaLevel;
    }

    public static Species ofName(String speciesName) {
        return SpeciesLibrary.ALL_FISH_TYPES.getOrDefault(speciesName, SpeciesLibrary.DEFAULT);
    }


    public EntityModelLayerRegistry.TexturedModelDataProvider model() {
        return this.modelDataProvider;
    }

    public Species withModel(EntityModelLayerRegistry.TexturedModelDataProvider modelDataProvider) {
        this.modelDataProvider = modelDataProvider;
        return this;
    }

    public Species setItem(FishItem item) {
        this.item = item;
        return this;
    }

    public String name() {
        return this.name.toLowerCase().replace(" ", "_");
    }

    public String getTextureName(boolean isAlbino) {
        String name = name();
        return "fish/" + name + "/" + name + (isAlbino ? "_albino" : "");
    }
}

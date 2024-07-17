package net.semperidem.fishing_club.item.fishing_rod.components;

import net.semperidem.fishing_club.fish.Species;

import java.util.HashMap;

public class BaitPartItem extends PartItem {
    HashMap<Species, Float> speciesBoost = new HashMap<>();
    float treasureBonus = 0;
    float treasureRarityBonus = 0;
    float fishRarity = 0;
    float fishRarityMultiplier = 1;
    boolean isMeat = false;
    boolean isPlant = false;
    float timeUntilHookedMultiplier = 1;
    float timeHookedMultiplier = 1;
    float fishQuantityBonus = 0;

    public BaitPartItem(Settings settings) {

        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 3);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 1);
        setDamageMultiplier(DamageSource.REEL_WATER, 1.5f);
        setDamageMultiplier(DamageSource.REEL_GROUND, 1);
    }

    public BaitPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature, float fishQuality) {

        this(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature);
        this.fishQuality = fishQuality;
    }

    public BaitPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public BaitPartItem(Settings settings,int weightCapacity) {

        this(settings);
        this.weightCapacity = weightCapacity;
    }

    public BaitPartItem speciesBonus(Species species, float bonus) {

        this.speciesBoost.put(species, bonus);
        return this;
    }

    public HashMap<Species, Float> getSpeciesBoost() {

        return speciesBoost;
    }

    public BaitPartItem treasureBonus(float treasureBonus) {

        this.treasureBonus = treasureBonus;
        return this;
    }

    public float getTreasureBonus() {

        return treasureBonus;
    }

    public BaitPartItem treasureRarityBonus(float treasureRarityBonus) {

        this.treasureRarityBonus = treasureRarityBonus;
        return this;
    }

    public float getTreasureRarityBonus() {

        return treasureRarityBonus;
    }

    public BaitPartItem fishRarity(float fishRarity) {

        this.fishRarity = fishRarity;
        return this;
    }

    public BaitPartItem fishRarityMultiplier(float fishRarityMultiplier) {

        this.fishRarityMultiplier = fishRarityMultiplier;
        return this;
    }

    public BaitPartItem fishQuality(int fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    public BaitPartItem timeUntilHookedMultiplier(float timeUntilHookedMultiplier) {

        this.timeUntilHookedMultiplier = timeUntilHookedMultiplier;
        return this;
    }

    public BaitPartItem timeHookedMultiplier(float timeHookedMultiplier) {

        this.timeHookedMultiplier = timeHookedMultiplier;
        return this;
    }

    public BaitPartItem fishQuantityBonus(float fishQuantityBonus) {

        this.fishQuantityBonus = fishQuantityBonus;
        return this;
    }

    public BaitPartItem meat() {

        this.isMeat = true;
        return this;
    }

    public BaitPartItem plant() {

        this.isPlant = false;
        return this;
    }

    @Override
    void applyComponent(RodConfiguration.AttributeProcessor configuration) {

        configuration.timeUntilHookedMultiplier *= this.timeUntilHookedMultiplier;
        configuration.timeHookedMultiplier *= this.timeHookedMultiplier;
        configuration.fishRarity += this.fishRarity;
        configuration.fishRarityMultiplier += this.fishRarityMultiplier;
        configuration.treasureBonus += this.treasureBonus;
        configuration.treasureRarityBonus += this.treasureRarityBonus;
        super.applyComponent(configuration);
    }

}

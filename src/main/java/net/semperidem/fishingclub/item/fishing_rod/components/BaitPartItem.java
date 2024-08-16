package net.semperidem.fishingclub.item.fishing_rod.components;

import net.semperidem.fishingclub.fish.Species;

import java.util.HashMap;

public class BaitPartItem extends PartItem {
    HashMap<Species, Float> speciesBoost = new HashMap<>();
    ItemStat treasureBonus = ItemStat.BASE_T1;
    ItemStat treasureRarityBonus = ItemStat.BASE_T1;
    ItemStat fishRarity = ItemStat.BASE_T1;
    ItemStat fishRarityMultiplier = ItemStat.MULTIPLIER_T3;
    boolean isMeat = false;
    boolean isPlant = false;
    ItemStat waitTimeReductionMultiplier = ItemStat.MULTIPLIER_T3;
    ItemStat timeHookedMultiplier = ItemStat.MULTIPLIER_T3;
    ItemStat fishQuantityBonus = ItemStat.BASE_T1;

    public BaitPartItem(Settings settings) {

        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 3);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 1);
        setDamageMultiplier(DamageSource.REEL_WATER, 1.5f);
        setDamageMultiplier(DamageSource.REEL_GROUND, 1);
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

    public BaitPartItem treasureBonus(ItemStat treasureBonus) {

        this.treasureBonus = treasureBonus;
        return this;
    }

    public float getTreasureBonus() {

        return treasureBonus.value;
    }

    public BaitPartItem treasureRarityBonus(ItemStat treasureRarityBonus) {

        this.treasureRarityBonus = treasureRarityBonus;
        return this;
    }

    public float getTreasureRarityBonus() {

        return treasureRarityBonus.value;
    }

    public BaitPartItem fishRarity(ItemStat fishRarity) {

        this.fishRarity = fishRarity;
        return this;
    }

    public BaitPartItem fishRarityMultiplier(ItemStat fishRarityMultiplier) {

        this.fishRarityMultiplier = fishRarityMultiplier;
        return this;
    }

    public BaitPartItem fishQuality(int fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    public BaitPartItem waitTimeReductionMultiplier(ItemStat waitTimeReductionMultiplier) {

        this.waitTimeReductionMultiplier = waitTimeReductionMultiplier;
        return this;
    }

    public BaitPartItem timeHookedMultiplier(ItemStat timeHookedMultiplier) {

        this.timeHookedMultiplier = timeHookedMultiplier;
        return this;
    }

    public BaitPartItem fishQuantityBonus(ItemStat fishQuantityBonus) {

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

        configuration.waitTimeReductionMultiplier *= this.waitTimeReductionMultiplier.value;
        configuration.timeHookedMultiplier *= this.timeHookedMultiplier.value;
        configuration.fishRarity += this.fishRarity.value;
        configuration.fishRarityMultiplier += this.fishRarityMultiplier.value;
        configuration.treasureBonus += this.treasureBonus.value;
        configuration.treasureRarityBonus += this.treasureRarityBonus.value;
        super.applyComponent(configuration);
    }

}

package net.semperidem.fishingclub.item.fishing_rod.components;

import net.semperidem.fishingclub.fish.Species;

import java.util.HashMap;

public class BaitPartItem extends PartItem{
    HashMap<Species<?>, Float> speciesBoost = new HashMap<>();
    ItemStat fishControl = ItemStat.BASE_T1;
    ItemStat fishControlMultiplier = ItemStat.MULTIPLIER_T3;
    ItemStat treasureBonus = ItemStat.BASE_T1;
    ItemStat treasureRarityBonus = ItemStat.BASE_T1;
    ItemStat fishRarity = ItemStat.BASE_T1;
    ItemStat fishRarityMultiplier = ItemStat.MULTIPLIER_T3;
    ItemStat waitTimeReductionMultiplier = ItemStat.MULTIPLIER_T3;
    ItemStat timeHookedMultiplier = ItemStat.MULTIPLIER_T3;
    ItemStat fishQuantityBonus = ItemStat.BASE_T1;
    boolean containsMeat = false;
    boolean containsPlant = false;

    public BaitPartItem(Settings settings) {
        super(settings);
        this.type = RodConfiguration.PartType.BAIT;
    }

    public BaitPartItem minOperatingTemperature(int minOperatingTemperature) {
        this.minOperatingTemperature = minOperatingTemperature;
        return this;
    }

    public BaitPartItem maxOperatingTemperature(int maxOperatingTemperature) {
        this.maxOperatingTemperature = maxOperatingTemperature;
        return this;
    }

    public BaitPartItem speciesBonus(Species<?> species, float bonus) {
        this.speciesBoost.put(species, bonus);
        return this;
    }

    public BaitPartItem treasureBonus(ItemStat treasureBonus) {
        this.treasureBonus = treasureBonus;
        return this;
    }

    public BaitPartItem treasureRarityBonus(ItemStat treasureRarityBonus) {
        this.treasureRarityBonus = treasureRarityBonus;
        return this;
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
        this.containsMeat = true;
        return this;
    }

    public BaitPartItem plant() {
        this.containsPlant = false;
        return this;
    }

    public HashMap<Species<?>, Float> getSpeciesBoost() {
        return speciesBoost;
    }

    public float getTreasureBonus() {
        return treasureBonus.value;
    }

    public float getTreasureRarityBonus() {
        return treasureRarityBonus.value;
    }

    @Override
    void apply(RodConfiguration.AttributeComposite attributes) {
        attributes.fishControl += this.fishControl.value;
        attributes.fishControlMultiplier *= this.fishControlMultiplier.value;
        attributes.waitTimeReductionMultiplier *= this.waitTimeReductionMultiplier.value;
        attributes.timeHookedMultiplier *= this.timeHookedMultiplier.value;
        attributes.fishRarity += this.fishRarity.value;
        attributes.fishRarityMultiplier += this.fishRarityMultiplier.value;
        attributes.treasureBonus += this.treasureBonus.value;
        attributes.treasureRarityBonus += this.treasureRarityBonus.value;
        super.apply(attributes);
    }

}

package net.semperidem.fishingclub.item.fishing_rod.components;

public class ReelPartItem extends PartItem {
    ItemStat fishControl = ItemStat.BASE_T1;
    ItemStat fishControlMultiplier = ItemStat.MULTIPLIER_T3;
    ItemStat bobberControl = ItemStat.BASE_T1;
    ItemStat timeHookedMultiplier = ItemStat.MULTIPLIER_T3;

    public ReelPartItem(Settings settings) {
        super(settings);
        this.type = RodConfiguration.PartType.REEL;
    }

    public ReelPartItem weightClass(int weightClass) {
        this.weightClass = weightClass;
        return this;
    }

    public ReelPartItem minOperatingTemperature(int minOperatingTemperature) {
        this.minOperatingTemperature = minOperatingTemperature;
        return this;
    }

    public ReelPartItem maxOperatingTemperature(int maxOperatingTemperature) {
        this.maxOperatingTemperature = maxOperatingTemperature;
        return this;
    }

    public ReelPartItem fishQuality(int fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    public ReelPartItem fishControl(ItemStat fishControl) {
        this.fishControl = fishControl;
        return this;
    }

    public ReelPartItem fishControlMultiplier(ItemStat fishControlMultiplier) {
        this.fishControlMultiplier = fishControlMultiplier;
        return this;
    }

    public ReelPartItem bobberControl(ItemStat bobberControl) {
        this.bobberControl = bobberControl;
        return this;
    }

    public ReelPartItem timeHookedMultiplier(ItemStat timeHookedMultiplier) {
        this.timeHookedMultiplier = timeHookedMultiplier;
        return this;
    }

    @Override
    void apply(RodConfiguration.AttributeComposite attributes) {
        attributes.fishControl += this.fishControl.value;
        attributes.fishControlMultiplier *= this.fishControlMultiplier.value;
        attributes.bobberControl += this.bobberControl.value;
        attributes.timeHookedMultiplier *= this.timeHookedMultiplier.value;
        super.apply(attributes);
    }
}

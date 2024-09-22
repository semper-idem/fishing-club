package net.semperidem.fishingclub.item.fishing_rod.components;

public class LinePartItem extends PartItem {
    ItemStat fishControl = ItemStat.BASE_T1;
    ItemStat fishControlMultiplier = ItemStat.MULTIPLIER_T3;
    int maxLineLength;

    public LinePartItem(Settings settings) {
        super(settings);
        this.type = RodConfiguration.PartType.LINE;
    }

    public LinePartItem weightClass(int weightClass) {
        this.weightClass = weightClass;
        return this;
    }

    public LinePartItem minOperatingTemperature(int minOperatingTemperature) {
        this.minOperatingTemperature = minOperatingTemperature;
        return this;
    }

    public LinePartItem maxOperatingTemperature(int maxOperatingTemperature) {
        this.maxOperatingTemperature = maxOperatingTemperature;
        return this;
    }

    public LinePartItem maxLineLength(int maxLineLength) {
        this.maxLineLength = maxLineLength;
        return this;
    }

    public LinePartItem fishQuality(int fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    public LinePartItem fishControl(ItemStat fishControl) {
        this.fishControl = fishControl;
        return this;
    }

    public LinePartItem fishControlMultiplier(ItemStat fishControlMultiplier) {
        this.fishControlMultiplier = fishControlMultiplier;
        return this;
    }

    @Override
    void apply(RodConfiguration.AttributeComposite attributes) {
        attributes.maxLineLength = this.maxLineLength;
        attributes.fishControl += this.fishControl.value;
        attributes.fishControlMultiplier *= this.fishControlMultiplier.value;
        super.apply(attributes);
    }
}

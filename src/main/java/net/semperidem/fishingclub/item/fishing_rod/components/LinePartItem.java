package net.semperidem.fishingclub.item.fishing_rod.components;

public class LinePartItem extends PartItem {
    int maxLineLength;

    ItemStat fishControl = ItemStat.BASE_T1;
    ItemStat fishControlMultiplier = ItemStat.MULTIPLIER_T3;

    public LinePartItem(Settings settings) {
        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 2);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 5);
    }

    public LinePartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public LinePartItem(Settings settings,int weightCapacity) {

        this(settings);
        this.weightClass = weightCapacity;
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
    void applyComponent(RodConfiguration.AttributeComposite configuration) {


        configuration.maxLineLength = this.maxLineLength;
        configuration.fishControl += this.fishControl.value;
        configuration.fishControlMultiplier *= this.fishControlMultiplier.value;
        super.applyComponent(configuration);
    }
}

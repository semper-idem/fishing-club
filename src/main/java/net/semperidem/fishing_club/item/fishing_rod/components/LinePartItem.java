package net.semperidem.fishing_club.item.fishing_rod.components;

public class LinePartItem extends PartItem {
    int maxLineLength;
    public LinePartItem(Settings settings) {
        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 2);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 5);
    }


    public LinePartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature, float fishQuality) {

        this(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature);
        this.fishQuality = fishQuality;
    }

    public LinePartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public LinePartItem(Settings settings,int weightCapacity) {

        this(settings);
        this.weightCapacity = weightCapacity;
    }

    public LinePartItem maxLineLength(int maxLineLength) {
        this.maxLineLength = maxLineLength;
        return this;
    }

    @Override
    void applyComponent(RodConfiguration.Controller configuration) {
        super.applyComponent(configuration);
        configuration.maxLineLength = this.maxLineLength;
    }
}

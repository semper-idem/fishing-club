package net.semperidem.fishing_club.item.fishing_rod.components;

public class ReelPartItem extends PartItem {

    public ReelPartItem(Settings settings) {

        super(settings);
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.REEL_FISH, 1);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 3);
    }

    public ReelPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature, float fishQuality) {

        this(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature);
        this.fishQuality = fishQuality;
    }

    public ReelPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public ReelPartItem(Settings settings,int weightCapacity) {

        this(settings);
        this.weightCapacity = weightCapacity;
    }

    @Override
    void applyComponent(RodConfiguration.Controller configuration) {

        super.applyComponent(configuration);
    }
}

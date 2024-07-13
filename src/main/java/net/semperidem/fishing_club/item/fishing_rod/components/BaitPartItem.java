package net.semperidem.fishing_club.item.fishing_rod.components;

public class BaitPartItem extends PartItem {

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


    @Override
    void applyComponent(RodConfiguration.Controller configuration) {

        super.applyComponent(configuration);
    }
}

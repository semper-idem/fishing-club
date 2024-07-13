package net.semperidem.fishing_club.item.fishing_rod.components;

public class HookPartItem extends PartItem {

    public HookPartItem(Settings settings) {

        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 2);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 5);
        setDamageMultiplier(DamageSource.REEL_GROUND, 1);
    }

    public HookPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature, float fishQuality) {

        this(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature);
        this.fishQuality = fishQuality;
    }

    public HookPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public HookPartItem(Settings settings,int weightCapacity) {

        this(settings);
        this.weightCapacity = weightCapacity;
    }


    @Override
    void applyComponent(RodConfiguration.Controller configuration) {

        super.applyComponent(configuration);
    }
}

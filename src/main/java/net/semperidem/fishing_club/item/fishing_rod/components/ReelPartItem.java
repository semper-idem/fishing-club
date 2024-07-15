package net.semperidem.fishing_club.item.fishing_rod.components;

public class ReelPartItem extends PartItem {
    float bobberControl;
    float timeHookedMultiplier;

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
    public ReelPartItem fishControl(float fishControl) {

        this.fishControl = fishControl;
        return this;
    }

    @Override
    public ReelPartItem fishControlMultiplier(float fishControlMultiplier) {

        this.fishControlMultiplier = fishControlMultiplier;
        return this;
    }

    public ReelPartItem bobberControl(float bobberControl) {

        this.bobberControl = bobberControl;
        return this;
    }

    public ReelPartItem timeHookedMultiplier(float timeHookedMultiplier) {

        this.timeHookedMultiplier = timeHookedMultiplier;
        return this;
    }

    @Override
    void applyComponent(RodConfiguration.AttributeProcessor configuration) {

        configuration.bobberControl += this.bobberControl;
        configuration.timeUntilHookedMultiplier *= this.timeHookedMultiplier;

        super.applyComponent(configuration);
    }
}

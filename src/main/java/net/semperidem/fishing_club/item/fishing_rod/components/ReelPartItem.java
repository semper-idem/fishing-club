package net.semperidem.fishing_club.item.fishing_rod.components;

public class ReelPartItem extends PartItem {
    ItemStat bobberControl = ItemStat.BASE_T1;
    ItemStat timeHookedMultiplier = ItemStat.MULTIPLIER_T3;

    public ReelPartItem(Settings settings) {

        super(settings);
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.REEL_FISH, 1);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 3);
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
    public ReelPartItem fishControl(ItemStat fishControl) {

        this.fishControl = fishControl;
        return this;
    }

    public ReelPartItem fishQuality(int fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    @Override
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
    void applyComponent(RodConfiguration.AttributeProcessor configuration) {

        configuration.bobberControl += this.bobberControl.value;
        configuration.timeHookedMultiplier *= this.timeHookedMultiplier.value;

        super.applyComponent(configuration);
    }
}

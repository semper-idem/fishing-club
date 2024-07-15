package net.semperidem.fishing_club.item.fishing_rod.components;

import java.util.function.Consumer;

public class BobberPartItem extends PartItem {

    float bobberWidth;
    float bobberControl;
    float timeUntilHookedMultiplier;
    float timeHookedMultiplier;
    Runnable fishBiteEffect = () -> {};

    public BobberPartItem(Settings settings) {

        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.CAST, 0);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 0);
        setDamageMultiplier(DamageSource.REEL_WATER, 1);
        setDamageMultiplier(DamageSource.REEL_GROUND, 2);
    }

    public BobberPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature, float fishQuality) {

        this(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature);
        this.fishQuality = fishQuality;
    }

    public BobberPartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public BobberPartItem(Settings settings,int weightCapacity) {

        this(settings);
        this.weightCapacity = weightCapacity;
    }

    public BobberPartItem bobberControl(float bobberControl) {

        this.bobberControl = bobberControl;
        return this;
    }

    public BobberPartItem bobberWidth(float bobberWidth) {

        this.bobberWidth = bobberWidth;
        return this;
    }

    public void onFishBiteEffect() {
        this.fishBiteEffect.run();
    }

    public BobberPartItem setFishBiteEffect(Runnable fishBiteEffect) {
        this.fishBiteEffect = fishBiteEffect;
        return this;
    }

    @Override
    public BobberPartItem fishControl(float fishControl) {
        this.fishControl = fishControl;
        return this;
    }

    @Override
    public BobberPartItem fishControlMultiplier(float fishControlMultiplier) {
        this.fishControlMultiplier = fishControlMultiplier;
        return this;
    }

    public BobberPartItem timeUntilHookedMultiplier(float timeUntilHookedMultiplier) {

        this.timeUntilHookedMultiplier = timeUntilHookedMultiplier;
        return this;
    }

    public BobberPartItem timeHookedMultiplier(float timeHookedMultiplier) {

        this.timeHookedMultiplier = timeHookedMultiplier;
        return this;
    }
    @Override
    void applyComponent(RodConfiguration.AttributeProcessor configuration) {

        configuration.bobberControl += this.bobberControl;
        configuration.timeHookedMultiplier *= this.timeHookedMultiplier;
        configuration.timeUntilHookedMultiplier *= this.timeUntilHookedMultiplier;
        configuration.bobberWidth = this.bobberWidth;
        super.applyComponent(configuration);
    }
}

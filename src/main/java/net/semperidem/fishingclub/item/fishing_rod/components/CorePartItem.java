package net.semperidem.fishingclub.item.fishing_rod.components;

public class CorePartItem extends PartItem {
    float castPowerMultiplier;

    public CorePartItem(Settings settings) {
        super(settings);
        setDamageMultiplier(DamageSource.REEL_FISH, 1);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 2.5f);
    }

    public CorePartItem weightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
        return this;
    }

    public CorePartItem castPowerMultiplier(float castPowerMultiplier) {
        this.castPowerMultiplier = castPowerMultiplier;
        return this;
    }

    @Override
    void applyComponent(RodConfiguration.Controller configuration) {
        super.applyComponent(configuration);
        configuration.castPower *= castPowerMultiplier;
    }
}

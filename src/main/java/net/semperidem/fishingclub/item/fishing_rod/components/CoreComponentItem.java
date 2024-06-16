package net.semperidem.fishingclub.item.fishing_rod.components;

public class CoreComponentItem extends ComponentItem{
    int weightCapacity;
    float castPowerMultiplier;

    public CoreComponentItem(Settings settings) {
        super(settings);
    }

    public CoreComponentItem weightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
        return this;
    }

    public CoreComponentItem castPowerMultiplier(float castPowerMultiplier) {
        this.castPowerMultiplier = castPowerMultiplier;
        return this;
    }

    @Override
    void equipComponent(FishingRodConfiguration configuration) {
        calculateWeightCapacity(configuration, weightCapacity);
        configuration.castPower.value *= castPowerMultiplier;
    }
}

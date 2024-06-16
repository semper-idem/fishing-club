package net.semperidem.fishingclub.item.fishing_rod.components;

public class LineComponentItem extends ComponentItem{
    int weightCapacity;
    int castRangeLimit;
    public LineComponentItem(Settings settings) {
        super(settings);
    }

    public LineComponentItem weightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
        return this;
    }

    public LineComponentItem castRangeLimit(int castRangeLimit) {
        this.castRangeLimit = castRangeLimit;
        return this;
    }

    @Override
    void equipComponent(FishingRodConfiguration configuration) {
        calculateWeightCapacity(configuration, weightCapacity);
        configuration.castRangeLimit.value = this.castRangeLimit;
    }
}

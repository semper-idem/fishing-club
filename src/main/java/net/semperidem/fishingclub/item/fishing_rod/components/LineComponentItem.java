package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.item.ItemStack;

public class LineComponentItem extends ComponentItem{
    int lineLength;
    public LineComponentItem(Settings settings) {
        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 2);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 5);
    }

    public LineComponentItem weightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
        return this;
    }

    public LineComponentItem lineLength(int lineLength) {
        this.lineLength = lineLength;
        return this;
    }

    @Override
    void applyComponent(FishingRodConfiguration configuration, ItemStack componentStack) {
        super.applyComponent(configuration, componentStack);
        configuration.lineLength.value = this.lineLength;
    }
}

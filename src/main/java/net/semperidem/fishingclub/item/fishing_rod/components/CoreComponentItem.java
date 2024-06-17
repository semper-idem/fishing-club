package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.item.ItemStack;

public class CoreComponentItem extends ComponentItem{
    float castPowerMultiplier;

    public CoreComponentItem(Settings settings) {
        super(settings);
        setDamageMultiplier(DamageSource.REEL_FISH, 1);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 2.5f);
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
    void applyComponent(FishingRodConfiguration configuration, ItemStack componentStack) {
        super.applyComponent(configuration, componentStack);
        configuration.castPower.value *= castPowerMultiplier;
    }
}

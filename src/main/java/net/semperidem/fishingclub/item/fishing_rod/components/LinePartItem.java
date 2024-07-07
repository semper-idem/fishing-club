package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.item.ItemStack;

public class LinePartItem extends PartItem {
    int maxLineLength;
    public LinePartItem(Settings settings) {
        super(settings);
        this.destroyOnBreak = true;
        setDamageMultiplier(DamageSource.BITE, 1);
        setDamageMultiplier(DamageSource.REEL_FISH, 2);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 5);
    }

    public LinePartItem weightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
        return this;
    }

    public LinePartItem maxLineLength(int maxLineLength) {
        this.maxLineLength = maxLineLength;

        return this;
    }

    @Override
    void applyComponent(RodConfigurationController configuration, ItemStack part) {
        super.applyComponent(configuration, part);
        configuration.maxLineLength = this.maxLineLength;
    }

    @Override
    public void equip(RodConfigurationComponent configuration, ItemStack componentStack) {
        configuration.equipLine(RodPartComponent.of(componentStack));
    }
}

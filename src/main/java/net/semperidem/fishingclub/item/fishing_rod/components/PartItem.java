package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.registry.ComponentRegistry;

import java.util.function.Consumer;

public class PartItem extends Item {
    int weightCapacity;
    boolean destroyOnBreak = false;
    public enum DamageSource {
        CAST(0),
        BITE(1),
        REEL_FISH(2),
        REEL_ENTITY(3),
        REEL_WATER(4),
        REEL_GROUND(5);

        public final int value;
        DamageSource(int value) {
            this.value = value;
        }

    }
    float[] durabilityMultiplier = {0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f};

    protected void setDamageMultiplier(DamageSource source, float value){
        durabilityMultiplier[source.value] = value;
    }

    public float getDamageMultiplier(DamageSource source) {
        return durabilityMultiplier[source.value];
    }

    public PartItem(Settings settings) {
        super(settings);
    }


    private boolean shouldDamage(int quality) {
        return Math.random() > MathHelper.clamp((quality - 1) * 0.25f, 0, 1);
    }

    public <T extends LivingEntity> void damage(ItemStack componentStack, int amount, DamageSource damageSource, T entity, Consumer<T> breakCallback) {
        damage(componentStack, (int) Math.ceil(getDamageMultiplier(damageSource) * amount), entity, breakCallback);
    }

    <T extends LivingEntity> void damage(ItemStack componentStack, int amount, T entity, Consumer<T> breakCallback) {
        int quality = componentStack.getOrDefault(ComponentRegistry.PART_QUALITY, 1);
        if (!shouldDamage(quality)) {
            return;
        }
        int currentDamage = componentStack.getDamage();
        if (currentDamage + amount >= componentStack.getMaxDamage() && !destroyOnBreak) {
            componentStack.setDamage(componentStack.getMaxDamage() - 1);
            componentStack.set(ComponentRegistry.BROKEN, true);
            breakCallback.accept(entity);
            return;
        }
        componentStack.setDamage(currentDamage + amount);
    }

    void applyComponent(RodConfigurationController configuration, ItemStack componentStack) {
        if (isBroken(componentStack)) {
            configuration.canCast = false;
            return;
        }
        validateWeightCapacity(configuration, weightCapacity);
    }

    boolean isBroken(ItemStack componentStack) {
        return componentStack.getOrDefault(ComponentRegistry.BROKEN, false);
    }

    void validateWeightCapacity(RodConfigurationController configuration, int weightCapacity) {
        if (configuration.weightCapacity > weightCapacity || configuration.weightCapacity == 0) {
            configuration.setWeightCapacity(weightCapacity);
        }
    }
}

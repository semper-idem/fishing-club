package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class ComponentItem extends Item {
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

    private final String QUALITY_TAG = "quality";
    private final String BROKEN_TAG = "broken";

    public ComponentItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        return getStack(1);
    }

    public ItemStack getStack(int quality) {
        ItemStack stack = new ItemStack(this);
        stack.getOrCreateNbt().putInt(QUALITY_TAG, quality);
        return stack;
    }

    private boolean shouldDamage(int quality) {
        return Math.random() > MathHelper.clamp((quality - 1) * 0.25f, 0, 1);
    }

    public <T extends LivingEntity> void damage(ItemStack componentStack, int amount, DamageSource damageSource, T entity, Consumer<T> breakCallback) {
        damage(componentStack, (int) Math.ceil(getDamageMultiplier(damageSource) * amount), entity, breakCallback);
    }

    <T extends LivingEntity> void damage(ItemStack componentStack, int amount, T entity, Consumer<T> breakCallback) {
        int quality = componentStack.getOrCreateNbt().getInt(QUALITY_TAG);
        if (!shouldDamage(quality)) {
            return;
        }
        if (componentStack.getDamage() + amount > componentStack.getMaxDamage() && !destroyOnBreak) {
            componentStack.getOrCreateSubNbt(BROKEN_TAG);
            breakCallback.accept(entity);
            return;
        }
        componentStack.damage(amount, entity, breakCallback);
    }

    void applyComponent(FishingRodConfiguration configuration, ItemStack componentStack) {
        if (isBroken(componentStack)) {
            configuration.canCast = false;
            return;
        }
        calculateWeightCapacity(configuration, weightCapacity);
    }

    boolean isBroken(ItemStack componentStack) {
        return componentStack.getOrCreateNbt().contains(BROKEN_TAG);
    }

    void calculateWeightCapacity(FishingRodConfiguration configuration, int weightCapacity) {
        if (configuration.weightCapacity.value > weightCapacity || configuration.weightCapacity.value == 0) {
            configuration.weightCapacity.value = weightCapacity;
        }
    }
}

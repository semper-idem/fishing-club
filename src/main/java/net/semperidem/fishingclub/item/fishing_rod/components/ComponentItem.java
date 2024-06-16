package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

public class ComponentItem extends Item {
    private final String QUALITY_TAG = "quality";

    public ComponentItem(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getDefaultStack() {
        return getStack(1);
    }

    public ItemStack getStack(int quality) {
        ItemStack stack = new ItemStack(this);
        NbtCompound nbt = stack.getOrCreateNbt();
        nbt.putInt(QUALITY_TAG, quality);
        return stack;
    }

    private boolean shouldDamage(int quality) {
        return Math.random() > MathHelper.clamp((quality - 1) * 0.25f, 0, 1);
    }

    <T extends LivingEntity> void damage(ItemStack componentStack, int amount, T entity, Consumer<T> breakCallback) {
        int quality = componentStack.getOrCreateNbt().getInt(QUALITY_TAG);
        if (shouldDamage(quality)) {
            componentStack.damage(amount, entity, breakCallback);
        }
    }

    void equipComponent(FishingRodConfiguration configuration) {}

    void calculateWeightCapacity(FishingRodConfiguration configuration, int weightCapacity) {
        if (configuration.weightCapacity.value > weightCapacity || configuration.weightCapacity.value == 0) {
            configuration.weightCapacity.value = weightCapacity;
        }
    }
}

package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;

import java.util.HashSet;
import java.util.function.Consumer;

import static net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItems.*;

public class FishingRodConfiguration {
    private final HashSet<ConfigurationAttribute<?>> configurationAttributes = new HashSet<>();
    final ConfigurationAttribute<Integer> weightCapacity = new ConfigurationAttribute<>(0);
    final ConfigurationAttribute<Integer> castRangeLimit = new ConfigurationAttribute<>(8);
    final ConfigurationAttribute<Float> castPower = new ConfigurationAttribute<>(1f);

    final HashSet<Component> components = new HashSet<>();
    final ItemStack fishingRod;
    boolean canCast = true;
    final Component coreComponent = createComponent("core", CORE_WOODEN_OAK);
    final Component lineComponent = createComponent("line", LINE_SPIDER);

    static final String CONFIGURATION_TAG = "configuration";

    public FishingRodConfiguration(ItemStack fishingRod) {
        this.fishingRod = fishingRod;
        NbtCompound configurationTag = fishingRod.getOrCreateSubNbt(CONFIGURATION_TAG);
        for(Component component : components) {
            component.set(ItemStack.fromNbt((NbtCompound) configurationTag.get(component.tag)));
        }
        recalculateAttributes();
    }

    public ItemStack getFishingRod() {
        return this.fishingRod;
    }

    public int getWeightCapacity(){
        return weightCapacity.value;
    }

    private Component createComponent(String tag, ComponentItem componentItem) {
        Component component = new Component(tag, componentItem);
        components.add(component);
        return component;
    }

    //Should be coupled with castCharge and max out at 3.3
    //e.g max 2 here and max 1.6 on full charge cast
    public float getCastPower(){
        return castPower.value;
    }

    public int getCastRangeLimit() {
        return castRangeLimit.value;
    }

    public void equipCoreComponent(ItemStack coreStack){
        equipComponent(coreComponent, coreStack);
    }

    public void equipLineComponent(ItemStack lineStack){
        equipComponent(lineComponent, lineStack);
    }

    public void unEquipLineComponent() {
        equipComponent(lineComponent, EMPTY_COMPONENT.getDefaultStack());
    }

    private void equipComponent(Component component, ItemStack stack) {
        component.set(stack);
        recalculateAttributes();
        component.toNbt(fishingRod.getOrCreateSubNbt(CONFIGURATION_TAG));
    }

    public void recalculateAttributes() {
        configurationAttributes.iterator().forEachRemaining(ConfigurationAttribute::reset);
        float averageDamage = 0;
        int componentCount = 0;
        for(Component component : components) {
            if (component.type == EMPTY_COMPONENT) {
                continue;
            }
            component.type.applyComponent(this, component.componentStack);
            averageDamage += component.getDamagePercentInt();
            componentCount++;
        }
        averageDamage /= componentCount;
        averageDamage /= 100f;
        fishingRod.setDamage((int) (fishingRod.getMaxDamage() * averageDamage));
    }

    public <T extends LivingEntity> void damage(int amount, ComponentItem.DamageSource damageSource, T entity, Consumer<T> breakCallback) {
        float averageDamage = 0;
        int componentCount = 0;
        for(Component component : components) {
            if (component.type == EMPTY_COMPONENT) {
                continue;
            }
            component.damage(amount, damageSource, entity, breakCallback);
            averageDamage += component.getDamagePercentInt();
            componentCount++;
        }
        averageDamage /= componentCount;
        averageDamage /= 100f;
        fishingRod.setDamage((int) (fishingRod.getMaxDamage() * averageDamage));
    }

    public void onComponentBroken(LivingEntity e) {
        recalculateAttributes();
    }


    class ConfigurationAttribute<T> {
        T value;
        T baseValue;

        ConfigurationAttribute(T baseValue) {
            this.baseValue = baseValue;
            reset();
            configurationAttributes.add(this);
        }

        void reset() {
            this.value = baseValue;
        }
    }

    static class Component{
        final String tag;
        private ComponentItem type;
        private ItemStack componentStack;

        public Component(String tag, ComponentItem type){
            this.tag = tag;
            this.type = type;
            this.componentStack = type.getDefaultStack();
        }

        public ItemStack get() {
            return componentStack;
        }

        <T extends LivingEntity> void damage(int amount, ComponentItem.DamageSource damageSource, T entity, Consumer<T> breakCallback) {
            type.damage(componentStack, amount, damageSource, entity, breakCallback);
        }

        public int getDamagePercentInt() {
            return (int) ((componentStack.getDamage()) * 1f / componentStack.getMaxDamage() * 100);
        }

        public void set(ItemStack itemStack) {
            if (!(itemStack.getItem() instanceof ComponentItem)) {
                return;
            }
            componentStack = itemStack;
            type = (ComponentItem) itemStack.getItem();
        }

        void toNbt(NbtCompound nbtCompound) {
            nbtCompound.put(tag, componentStack.writeNbt(new NbtCompound()));
        }
    }
}


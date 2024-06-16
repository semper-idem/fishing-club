package net.semperidem.fishingclub.item.fishing_rod.components;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItems;
import net.semperidem.fishingclub.item.fishing_rod.MemberFishingRodItem;

import java.util.HashSet;
import java.util.Optional;

import static net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItems.*;

public class FishingRodConfiguration {
    private final HashSet<ConfigurationAttribute<?>> configurationAttributes = new HashSet<>();
    final ConfigurationAttribute<Integer> weightCapacity = new ConfigurationAttribute<>(0);
    final ConfigurationAttribute<Integer> castRangeLimit = new ConfigurationAttribute<>(8);
    final ConfigurationAttribute<Float> castPower = new ConfigurationAttribute<>(1f);

    final HashSet<Component> components = new HashSet<>();
    final Component coreComponent = createComponent("core", CORE_WOODEN_OAK);
    final Component lineComponent = createComponent("line", LINE_SPIDER);

    static final String CONFIGURATION_TAG = "configuration";

    public FishingRodConfiguration(ItemStack rodStack) {
        NbtCompound configurationTag = rodStack.getOrCreateSubNbt(CONFIGURATION_TAG);
        components.forEach(component -> component.set(ItemStack.fromNbt((NbtCompound) configurationTag.get(component.tag))));
        recalculateAttributes();
    }

    public FishingRodConfiguration() {}

    public int getWeightCapacity(){
        return weightCapacity.value;
    }

    public void setNbt(ItemStack itemStack) {
        NbtCompound configurationTag = itemStack.getOrCreateSubNbt(CONFIGURATION_TAG);
        coreComponent.toNbt(configurationTag);
        lineComponent.toNbt(configurationTag);
    }

    private Component createComponent(String tag, ComponentItem componentItem) {
        Component component = new Component(tag, componentItem);
        components.add(component);
        return component;
    }

    public static FishingRodConfiguration getDefault() {
        return new FishingRodConfiguration();
    }


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
    }

    public void recalculateAttributes() {
        configurationAttributes.iterator().forEachRemaining(ConfigurationAttribute::reset);
        components.forEach(component -> component.type.equipComponent(this));
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
        ComponentItem type;
        Optional<ItemStack> optionalStack;

        public Component(String tag, ComponentItem type){
            this.tag = tag;
            this.type = type;
            this.optionalStack = Optional.of(type.getDefaultStack());
        }

        public ItemStack get() {
            return optionalStack.orElse(EMPTY_COMPONENT.getDefaultStack());
        }

        public void set(ItemStack itemStack) {
            if (!(itemStack.getItem() instanceof ComponentItem)) {
                optionalStack = Optional.of(EMPTY_COMPONENT.getDefaultStack());
                type = EMPTY_COMPONENT;
                return;
            }
            optionalStack = Optional.of(itemStack);
            type = (ComponentItem) itemStack.getItem();
        }

        void toNbt(NbtCompound nbtCompound) {
            nbtCompound.put(tag, get().writeNbt(new NbtCompound()));
        }
    }
}


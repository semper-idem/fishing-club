package net.semperidem.fishing_club.item.fishing_rod.components;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCItems;

public class CorePartItem extends PartItem {
    float castPowerMultiplier;
    float bobberControl = 25;

    public CorePartItem(Settings settings) {
        super(settings);
        this.fishControl = 25;
        setDamageMultiplier(DamageSource.REEL_FISH, 1);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 2.5f);
    }

    public CorePartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature, float fishQuality) {

        this(settings, weightCapacity, minOperatingTemperature, maxOperatingTemperature);
        this.fishQuality = fishQuality;
    }

    public CorePartItem(Settings settings,int weightCapacity,  int minOperatingTemperature, int maxOperatingTemperature) {

        this(settings, weightCapacity);
        this.minOperatingTemperature = minOperatingTemperature;
        this.maxOperatingTemperature = maxOperatingTemperature;
    }

    public CorePartItem(Settings settings,int weightCapacity) {

        this(settings);
        this.weightCapacity = weightCapacity;
    }

    public CorePartItem fishQuality(int fishQuality) {
        this.fishQuality = fishQuality;
        return this;
    }

    @Override
    public CorePartItem fishControl(float fishControl) {
        this.fishControl = fishControl;
        return this;
    }

    @Override
    public CorePartItem fishControlMultiplier(float fishControlMultiplier) {
        this.fishControlMultiplier = fishControlMultiplier;
        return this;
    }

    public CorePartItem bobberControl(float bobberControl) {

        this.bobberControl = bobberControl;
        return this;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {

        ItemStack coreStack = user.getStackInHand(hand);
        ItemStack fishingRod = FCItems.MEMBER_FISHING_ROD.getDefaultStack();
        fishingRod.set(FCComponents.ROD_CONFIGURATION, RodConfiguration.EMPTY.equip(coreStack, RodConfiguration.PartType.CORE));
        float rodDamage = getPartDamagePercentage(coreStack);
        fishingRod.setDamage((int) (fishingRod.getMaxDamage() * rodDamage));
        //user.getStackInHand(hand).setCount(0);
        user.setStackInHand(hand, fishingRod);
        return super.use(world, user, hand);
    }

    public CorePartItem castPowerMultiplier(float castPowerMultiplier) {
        this.castPowerMultiplier = castPowerMultiplier;
        return this;
    }

    @Override
    void applyComponent(RodConfiguration.AttributeProcessor configuration) {

        configuration.bobberControl += this.bobberControl;
        configuration.castPower *= castPowerMultiplier;
        super.applyComponent(configuration);
    }
}

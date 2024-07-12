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

    public CorePartItem(Settings settings) {
        super(settings);
        setDamageMultiplier(DamageSource.REEL_FISH, 1);
        setDamageMultiplier(DamageSource.REEL_ENTITY, 2.5f);
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

    public CorePartItem weightCapacity(int weightCapacity) {
        this.weightCapacity = weightCapacity;
        return this;
    }

    public CorePartItem castPowerMultiplier(float castPowerMultiplier) {
        this.castPowerMultiplier = castPowerMultiplier;
        return this;
    }

    @Override
    void applyComponent(RodConfiguration.Controller configuration) {
        super.applyComponent(configuration);
        configuration.castPower *= castPowerMultiplier;
    }
}

package net.semperidem.fishingclub.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishingclub.item.fishing_rod.MemberFishingRodItem;
import net.semperidem.fishingclub.registry.ComponentRegistry;

public class ClonedFishingRod extends MemberFishingRodItem {
    private static final int DURATION = 6000;
    public ClonedFishingRod(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack clonedRod = user.getStackInHand(hand);
        long creationTick = clonedRod.getOrDefault(ComponentRegistry.CREATION_TICK, 0L);
        float maxDamage = clonedRod.getMaxDamage();
        clonedRod.setDamage((int) ((float) world.getTime() / (creationTick + DURATION) * maxDamage));
        if (creationTick + DURATION < world.getTime()) {
            clonedRod.decrement(1);
            return TypedActionResult.consume(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}

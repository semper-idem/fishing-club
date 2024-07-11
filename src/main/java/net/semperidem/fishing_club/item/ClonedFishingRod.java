package net.semperidem.fishing_club.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishing_club.item.fishing_rod.MemberFishingRodItem;
import net.semperidem.fishing_club.registry.FCComponents;

public class ClonedFishingRod extends MemberFishingRodItem {
    private static final int DURATION = 6000;
    public ClonedFishingRod(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack clonedRod = user.getStackInHand(hand);
        long creationTick = clonedRod.getOrDefault(FCComponents.CREATION_TICK, 0L);
        float maxDamage = clonedRod.getMaxDamage();
        clonedRod.setDamage((int) ((float) world.getTime() / (creationTick + DURATION) * maxDamage));
        if (creationTick + DURATION < world.getTime()) {
            clonedRod.decrement(1);
            return TypedActionResult.consume(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}

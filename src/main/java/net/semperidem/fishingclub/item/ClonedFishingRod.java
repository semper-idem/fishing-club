package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class ClonedFishingRod extends CustomFishingRod{
    private static final int DURATION = 6000;
    public ClonedFishingRod(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack selfStack = user.getStackInHand(hand);
        long creationTick = selfStack.getOrCreateNbt().getLong("creation_tick");
        float maxDamage = selfStack.getMaxDamage();
        selfStack.setDamage((int) (world.getTime() / (creationTick + DURATION) * maxDamage));
        if (creationTick + DURATION < world.getTime()) {
            selfStack.decrement(1);
            return TypedActionResult.consume(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}

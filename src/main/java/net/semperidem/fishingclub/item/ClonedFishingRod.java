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

public class ClonedFishingRod extends MemberFishingRodItem {
    private static final int DURATION = 6000;
    public ClonedFishingRod(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack clonedRod = user.getStackInHand(hand);
        //clonedRod.get(DataComponentTypes.CUSTOM_DATA).
        clonedRod.set(NbtComponent.of())
        long creationTick = clonedRod.getOrCreateNbt().getLong("creation_tick");
        float maxDamage = clonedRod.getMaxDamage();
        clonedRod.setDamage((int) (world.getTime() / (creationTick + DURATION) * maxDamage));
        if (creationTick + DURATION < world.getTime()) {
            clonedRod.decrement(1);
            return TypedActionResult.consume(user.getStackInHand(hand));
        }
        return super.use(world, user, hand);
    }
}

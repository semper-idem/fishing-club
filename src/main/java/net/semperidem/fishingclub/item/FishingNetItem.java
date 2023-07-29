package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishingclub.client.screen.fishing_net.FishingNetScreenFactory;
public class FishingNetItem extends Item {

    public FishingNetItem(Settings settings) {
        super(settings);
    }

    public static void openScreen(PlayerEntity player, ItemStack fishingNetStack) {
        if (player.world != null && !player.world.isClient) {
            player.openHandledScreen(new FishingNetScreenFactory(fishingNetStack));
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        openScreen(user, user.getMainHandStack());
        return TypedActionResult.success(user.getMainHandStack());
    }
}

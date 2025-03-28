package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class DebugItem extends Item {
    private static int mode = 0;
    public DebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return super.use(world, user, hand);
        }
        if (user.isSneaking()) {
            mode++;
            if (mode > 5) {
                mode = 0;
            }
            user.sendMessage(Text.literal("[DEBUG MODE]" + mode));
            return super.use(world, user, hand);
        }
        return super.use(world, user, hand);
    }
}

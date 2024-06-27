package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItems;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent;
import net.semperidem.fishingclub.registry.ItemRegistry;

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
        ItemStack rod = user.getInventory().getStack(0);
        if (!rod.isOf(ItemRegistry.MEMBER_FISHING_ROD)) {
            return super.use(world, user, hand);
        }
        if (mode == 2) {
            user.sendMessage(Text.literal("[DEBUG MODE]" + "CORE_WOODEN_OAK"));
            RodConfigurationComponent rodConfiguration = ItemRegistry.MEMBER_FISHING_ROD.getRodConfiguration(rod);
            rodConfiguration.equipCoreComponent(FishingRodPartItems.CORE_WOODEN_OAK.getDefaultStack());

        }
        if (mode == 1) {
            user.sendMessage(Text.literal("[DEBUG MODE]" + "LINE_WOOL"));
            RodConfigurationComponent rodConfiguration = ItemRegistry.MEMBER_FISHING_ROD.getRodConfiguration(rod);
            rodConfiguration.equipLineComponent(FishingRodPartItems.LINE_SPIDER.getDefaultStack());

        }
        if (mode == 0) {
            user.sendMessage(Text.literal("[DEBUG MODE]" + "invalidateCache"));
            System.out.println(rod.getNbt());

        }
        return super.use(world, user, hand);
    }
}

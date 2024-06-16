package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItems;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodConfiguration;
import net.semperidem.fishingclub.registry.ItemRegistry;

public class DebugItem extends Item {
    public DebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient) {
            user.sendMessage(Text.literal("[DEBUG]"));
        }
        ItemStack rod = user.getInventory().getStack(0);
        if (!rod.isOf(ItemRegistry.MEMBER_FISHING_ROD)) {
            return super.use(world, user, hand);
        }
        ItemRegistry.MEMBER_FISHING_ROD.invalidateCache();
        FishingRodConfiguration rodConfiguration = ItemRegistry.MEMBER_FISHING_ROD.getRodConfiguration(rod);
        //rodConfiguration.equipLineComponent(FishingRodPartItems.LINE_WOOL.getDefaultStack());
        rodConfiguration.equipCoreComponent(FishingRodPartItems.CORE_WOODEN_OAK.getDefaultStack());
        return super.use(world, user, hand);
    }
}

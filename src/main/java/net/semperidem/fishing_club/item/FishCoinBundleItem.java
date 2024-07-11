package net.semperidem.fishing_club.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCItems;

public class FishCoinBundleItem extends Item {
    public FishCoinBundleItem(Settings settings) {
        super(settings);
    }


    public static ItemStack ofValue(int value){
        ItemStack bundleStack = FCItems.FISH_COIN_BUNDLE.getDefaultStack();
        bundleStack.set(FCComponents.COIN, value);
        return bundleStack;
    }
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack bundleStack = user.getStackInHand(hand);
        int value = bundleStack.getOrDefault(FCComponents.COIN, 1);
        if (!user.getWorld().isClient) {
            FishingCard.of(user).addCredit(value);
        } else {
            user.sendMessage(Text.of("Added: " + value + " fish credit"), false);
        }
        bundleStack.setCount(0);
        return TypedActionResult.consume(bundleStack);
    }
}

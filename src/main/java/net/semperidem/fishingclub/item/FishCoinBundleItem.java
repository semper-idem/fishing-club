package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.Items;

public class FishCoinBundleItem extends Item {
    public FishCoinBundleItem(Settings settings) {
        super(settings);
    }


    public static ItemStack ofValue(int value){
        ItemStack bundleStack = Items.FISH_COIN_BUNDLE.getDefaultStack();
        bundleStack.set(Components.COIN, value);
        return bundleStack;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return super.getUseAction(stack);
    }


    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack bundleStack = user.getStackInHand(hand);
        int value = bundleStack.getOrDefault(Components.COIN, 1);
        if (!user.getWorld().isClient) {
            Card.of(user).addGS(value);
        } else {
            user.sendMessage(Text.of("Added: " + value + " fish credit"), false);
        }
        bundleStack.setCount(0);
        return ActionResult.CONSUME;
    }
}

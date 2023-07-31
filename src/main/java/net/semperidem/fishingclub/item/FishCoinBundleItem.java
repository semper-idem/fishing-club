package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fisher.FisherInfoManager;

public class FishCoinBundleItem extends Item {
    public FishCoinBundleItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack bundleStack = user.getStackInHand(hand);
        NbtCompound bundleStackTag = bundleStack.getOrCreateNbt();
        int value = 5;
        if (bundleStackTag.contains("value")) {
            value = bundleStackTag.getInt("value");
        }
        if (!user.world.isClient) {
            FisherInfoManager.addCredit(user, value);
        } else {
            user.sendMessage(Text.of("Added: " + value + " fish credit"), false);
        }
        bundleStack.setCount(0);
        return TypedActionResult.consume(bundleStack);
    }
}

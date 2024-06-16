package net.semperidem.fishingclub.game.treasure;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.LocalRandom;
import net.semperidem.fishingclub.item.FishCoinBundleItem;
import net.semperidem.fishingclub.registry.ItemRegistry;

import java.util.Random;

public class TreasureReward {
    private static final Random random = new Random(42L);
    private static final LocalRandom localRandom = new LocalRandom(42L);
    int tier; //1-7
    int cost;
    int rarity;
    float quality; //quantity // durability
    Item item;

    TreasureReward(int tier, int cost, float quality, Item item, int rarity){
        this.tier = tier;
        this.cost = cost;
        this.quality = quality;
        this.item = item;
        this.rarity = rarity;
    }


    ItemStack roll(){
        ItemStack rewardStack = item.getDefaultStack();
        if (rewardStack.isOf(ItemRegistry.MEMBER_FISHING_ROD)) {
            return ItemRegistry.MEMBER_FISHING_ROD.getDefaultStack();
        }

        if (rewardStack.isEnchantable()) {
            float percentageDurability = (float) (quality * 0.75 + quality * Math.random() * 0.25);
            float curseChance = (1 - quality);
            int enchantingLevel = (int) Math.max(5, Math.min(50, 8 / Math.pow(quality, 1.5)));
            rewardStack = EnchantmentHelper.enchant(localRandom, rewardStack, enchantingLevel, false);
            rewardStack.setDamage((int) (item.getMaxDamage() * (1f - percentageDurability)));
            if (Math.random() < curseChance) {
                if (item instanceof ArmorItem) {
                    rewardStack.addEnchantment(Enchantments.BINDING_CURSE, 1);
                }
                rewardStack.addEnchantment(Enchantments.VANISHING_CURSE, 1);
            }
            return rewardStack;
        }

        if (item == ItemRegistry.FISH_COIN_BUNDLE) {
           return FishCoinBundleItem.ofValue((int) (quality * (1 + MathHelper.clamp(0, 4, Math.abs(random.nextGaussian())))));
        }
        if (quality != 1) {
            rewardStack.setCount(Math.max(1, (int) (quality * 0.5f + quality * Math.random() * 1.5f)));
        }
        return rewardStack;
    }

}

package net.semperidem.fishing_club.game.treasure;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.registry.FCItems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Rewards {
    private static final ArrayList<TreasureReward> ALL_REWARDS = new ArrayList<>();
    private static final Random random = new Random(42);

    static {
        //TIER COST QUALITY ITEM RARITY
        ALL_REWARDS.add(new TreasureReward(1,80,1, Items.OAK_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, Items.BIRCH_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, Items.SPRUCE_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, Items.JUNGLE_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, Items.DARK_OAK_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, Items.ACACIA_BOAT, 1));

        ALL_REWARDS.add(new TreasureReward(1,80,8, Items.WHEAT_SEEDS, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,8, Items.POTATO, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, Items.CARROT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, Items.BAMBOO, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, Items.BEETROOT_SEEDS, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, Items.PUMPKIN_SEEDS, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, Items.MELON_SEEDS, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, Items.SUGAR_CANE, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, Items.VINE, 1));
        ALL_REWARDS.add(new TreasureReward(1,40,16, Items.SEAGRASS, 5));
        ALL_REWARDS.add(new TreasureReward(1,40,4, Items.SEA_PICKLE, 3));

        ALL_REWARDS.add(new TreasureReward(1,60,4, Items.BONE, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,8, Items.STICK, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,2, Items.BOWL, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,2, Items.LILY_PAD, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,4, Items.ROTTEN_FLESH, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,2, Items.STRING, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,4, Items.INK_SAC, 2));

        ALL_REWARDS.add(new TreasureReward(1,30,4, FCItems.FISH_COIN_BUNDLE, 10));



        ALL_REWARDS.add(new TreasureReward(2,100,1, Items.OAK_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, Items.BIRCH_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, Items.SPRUCE_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, Items.JUNGLE_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, Items.DARK_OAK_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, Items.ACACIA_CHEST_BOAT, 1));

        ALL_REWARDS.add(new TreasureReward(2,120,1, FCItems.MEMBER_FISHING_ROD, 1));

        ALL_REWARDS.add(new TreasureReward(2,160,6, Items.COD, 5));
        ALL_REWARDS.add(new TreasureReward(2,160,1, Items.TROPICAL_FISH, 3));
        ALL_REWARDS.add(new TreasureReward(2,160,4, Items.SALMON, 4));

        ALL_REWARDS.add(new TreasureReward(2,60,8, Items.COAL, 4));

        ALL_REWARDS.add(new TreasureReward(2,30,10, FCItems.FISH_COIN_BUNDLE, 10));

        ALL_REWARDS.add(new TreasureReward(3,80,16, Items.IRON_NUGGET, 5));
        ALL_REWARDS.add(new TreasureReward(3,80,12, Items.GOLD_NUGGET, 10));


        ALL_REWARDS.add(new TreasureReward(3,160,0.8f, Items.LEATHER_BOOTS, 1));
        ALL_REWARDS.add(new TreasureReward(3,160,0.8f, Items.LEATHER_CHESTPLATE, 1));
        ALL_REWARDS.add(new TreasureReward(3,160,0.8f, Items.LEATHER_HELMET, 1));
        ALL_REWARDS.add(new TreasureReward(3,160,0.8f, Items.LEATHER_LEGGINGS, 1));

        ALL_REWARDS.add(new TreasureReward(3,45,12, FCItems.FISH_COIN_BUNDLE, 5));


        ALL_REWARDS.add(new TreasureReward(4,120,3, Items.IRON_INGOT, 5));
        ALL_REWARDS.add(new TreasureReward(4,120,3, Items.GOLD_INGOT, 10));

        ALL_REWARDS.add(new TreasureReward(4,60,16, FCItems.FISH_COIN_BUNDLE, 5));

        ALL_REWARDS.add(new TreasureReward(5,200,0.8f, Items.GOLDEN_BOOTS, 1));
        ALL_REWARDS.add(new TreasureReward(5,200,0.8f, Items.GOLDEN_CHESTPLATE, 1));
        ALL_REWARDS.add(new TreasureReward(5,200,0.8f, Items.GOLDEN_HELMET, 1));
        ALL_REWARDS.add(new TreasureReward(5,200,0.8f, Items.GOLDEN_LEGGINGS, 1));

        ALL_REWARDS.add(new TreasureReward(5,250,1, Items.NAUTILUS_SHELL, 1));
        ALL_REWARDS.add(new TreasureReward(5,250,1, Items.SADDLE, 2));
        ALL_REWARDS.add(new TreasureReward(5,250,1, Items.NAME_TAG, 3));

        ALL_REWARDS.add(new TreasureReward(5,300,8, Items.EMERALD, 3));

        ALL_REWARDS.add(new TreasureReward(5,80,20, FCItems.FISH_COIN_BUNDLE, 5));


        ALL_REWARDS.add(new TreasureReward(6,250,1, FCItems.FISHING_NET, 2));
        ALL_REWARDS.add(new TreasureReward(6,350,1, FCItems.DOUBLE_FISHING_NET, 1));

        ALL_REWARDS.add(new TreasureReward(6,300,0.6f, Items.IRON_BOOTS, 1));
        ALL_REWARDS.add(new TreasureReward(6,300,0.6f, Items.IRON_CHESTPLATE, 1));
        ALL_REWARDS.add(new TreasureReward(6,300,0.6f, Items.IRON_HELMET, 1));
        ALL_REWARDS.add(new TreasureReward(6,300,0.6f, Items.IRON_LEGGINGS, 1));

        ALL_REWARDS.add(new TreasureReward(6,100,30, FCItems.FISH_COIN_BUNDLE, 5));

        ALL_REWARDS.add(new TreasureReward(7,800,0.2f, Items.DIAMOND_BOOTS, 1));
        ALL_REWARDS.add(new TreasureReward(7,800,0.2f, Items.DIAMOND_CHESTPLATE, 1));
        ALL_REWARDS.add(new TreasureReward(7,800,0.2f, Items.DIAMOND_HELMET, 1));
        ALL_REWARDS.add(new TreasureReward(7,800,0.2f, Items.DIAMOND_LEGGINGS, 1));

        ALL_REWARDS.add(new TreasureReward(7,800,4, Items.DIAMOND, 1));
        ALL_REWARDS.add(new TreasureReward(7,100,40, FCItems.FISH_COIN_BUNDLE, 5));

    }


    public static Reward roll(FishingCard fisher){
        int grade = getGrade(fisher);
        return new Reward(roll(fisher, getCost(fisher), grade), grade);
    }

    public  static int getCost(FishingCard fisher){
        int avgCost = (int) (60 * Math.sqrt(fisher.getLevel()));
        return (int) (60 + (60 + avgCost * Math.abs(random.nextGaussian() / 2)));
    }

    public static int getGrade(FishingCard fisher){
        float avgGrade = (float) Math.max(1, Math.min(7, Math.sqrt(fisher.getLevel()) / 3));
        return (int)  Math.max(1, Math.min(7, (avgGrade * Math.abs(random.nextGaussian()) / 1.5)));
    }

    public static ArrayList<ItemStack> roll(FishingCard card, int costTotal, int grade){
        ArrayList<ItemStack> rollResult = new ArrayList<>();
        int costLeft = costTotal;
        for(int i = 0; i < ((3 + grade) / 2f); i++) {
            ArrayList<TreasureReward> possibleRewards = getPossibleRewards(costLeft, grade);
            if (possibleRewards.size() == 0) break;
            int randomIndex = (int) (Math.random() * possibleRewards.size() - 1);
            TreasureReward reward = possibleRewards.get(randomIndex);
            rollResult.add(reward.roll(card.getHolder()));
            costLeft -= reward.cost;
        }
        return rollResult;
    }

    private static ArrayList<TreasureReward> getPossibleRewards(int totalCost, int grade){
        ArrayList<TreasureReward> result = new ArrayList<>();
        ALL_REWARDS.forEach(reward -> {
            if (reward.cost <= totalCost && reward.tier <= grade) {
                for(int i = 0; i < reward.rarity; i++) result.add(reward);
            }
        });
        result.sort(Comparator.comparingInt(o -> o.tier));
        return result;
    }
}

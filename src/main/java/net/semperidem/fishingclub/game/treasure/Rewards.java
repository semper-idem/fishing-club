package net.semperidem.fishingclub.game.treasure;

import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfiguration;
import net.semperidem.fishingclub.registry.Items;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class Rewards {
    private static final ArrayList<TreasureReward> ALL_REWARDS = new ArrayList<>();
    private static final Random random = new Random(42);
    private static final float TREASURE_MIN_CHANCE = 0.05f;

    static {
        //TIER COST QUALITY ITEM RARITY
        ALL_REWARDS.add(new TreasureReward(1,80,1, net.minecraft.item.Items.OAK_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, net.minecraft.item.Items.BIRCH_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, net.minecraft.item.Items.SPRUCE_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, net.minecraft.item.Items.JUNGLE_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, net.minecraft.item.Items.DARK_OAK_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,1, net.minecraft.item.Items.ACACIA_BOAT, 1));

        ALL_REWARDS.add(new TreasureReward(1,80,8, net.minecraft.item.Items.WHEAT_SEEDS, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,8, net.minecraft.item.Items.POTATO, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, net.minecraft.item.Items.CARROT, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, net.minecraft.item.Items.BAMBOO, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, net.minecraft.item.Items.BEETROOT_SEEDS, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, net.minecraft.item.Items.PUMPKIN_SEEDS, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, net.minecraft.item.Items.MELON_SEEDS, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, net.minecraft.item.Items.SUGAR_CANE, 1));
        ALL_REWARDS.add(new TreasureReward(1,80,4, net.minecraft.item.Items.VINE, 1));
        ALL_REWARDS.add(new TreasureReward(1,40,16, net.minecraft.item.Items.SEAGRASS, 5));
        ALL_REWARDS.add(new TreasureReward(1,40,4, net.minecraft.item.Items.SEA_PICKLE, 3));

        ALL_REWARDS.add(new TreasureReward(1,60,4, net.minecraft.item.Items.BONE, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,8, net.minecraft.item.Items.STICK, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,2, net.minecraft.item.Items.BOWL, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,2, net.minecraft.item.Items.LILY_PAD, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,4, net.minecraft.item.Items.ROTTEN_FLESH, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,2, net.minecraft.item.Items.STRING, 2));
        ALL_REWARDS.add(new TreasureReward(1,60,4, net.minecraft.item.Items.INK_SAC, 2));

        ALL_REWARDS.add(new TreasureReward(1,30,4, Items.FISH_COIN_BUNDLE, 10));
        ALL_REWARDS.add(new TreasureReward(1,30,4, Items.MESSAGE_IN_BOTTLE_ITEM, 1));



        ALL_REWARDS.add(new TreasureReward(2,100,1, net.minecraft.item.Items.OAK_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, net.minecraft.item.Items.BIRCH_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, net.minecraft.item.Items.SPRUCE_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, net.minecraft.item.Items.JUNGLE_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, net.minecraft.item.Items.DARK_OAK_CHEST_BOAT, 1));
        ALL_REWARDS.add(new TreasureReward(2,100,1, net.minecraft.item.Items.ACACIA_CHEST_BOAT, 1));

        ALL_REWARDS.add(new TreasureReward(2,120,1, Items.CORE_COPPER, 1));

        ALL_REWARDS.add(new TreasureReward(2,160,6, net.minecraft.item.Items.COD, 5));
        ALL_REWARDS.add(new TreasureReward(2,160,1, net.minecraft.item.Items.TROPICAL_FISH, 3));
        ALL_REWARDS.add(new TreasureReward(2,160,4, net.minecraft.item.Items.SALMON, 4));

        ALL_REWARDS.add(new TreasureReward(2,60,8, net.minecraft.item.Items.COAL, 4));

        ALL_REWARDS.add(new TreasureReward(2,30,10, Items.FISH_COIN_BUNDLE, 10));

        ALL_REWARDS.add(new TreasureReward(3,80,16, net.minecraft.item.Items.IRON_NUGGET, 5));
        ALL_REWARDS.add(new TreasureReward(3,80,12, net.minecraft.item.Items.GOLD_NUGGET, 10));


        ALL_REWARDS.add(new TreasureReward(3,160,0.8f, net.minecraft.item.Items.LEATHER_BOOTS, 1));
        ALL_REWARDS.add(new TreasureReward(3,160,0.8f, net.minecraft.item.Items.LEATHER_CHESTPLATE, 1));
        ALL_REWARDS.add(new TreasureReward(3,160,0.8f, net.minecraft.item.Items.LEATHER_HELMET, 1));
        ALL_REWARDS.add(new TreasureReward(3,160,0.8f, net.minecraft.item.Items.LEATHER_LEGGINGS, 1));

        ALL_REWARDS.add(new TreasureReward(3,45,12, Items.FISH_COIN_BUNDLE, 5));


        ALL_REWARDS.add(new TreasureReward(4,120,3, net.minecraft.item.Items.IRON_INGOT, 5));
        ALL_REWARDS.add(new TreasureReward(4,120,3, net.minecraft.item.Items.GOLD_INGOT, 10));

        ALL_REWARDS.add(new TreasureReward(4,60,16, Items.FISH_COIN_BUNDLE, 5));

        ALL_REWARDS.add(new TreasureReward(5,200,0.8f, net.minecraft.item.Items.GOLDEN_BOOTS, 1));
        ALL_REWARDS.add(new TreasureReward(5,200,0.8f, net.minecraft.item.Items.GOLDEN_CHESTPLATE, 1));
        ALL_REWARDS.add(new TreasureReward(5,200,0.8f, net.minecraft.item.Items.GOLDEN_HELMET, 1));
        ALL_REWARDS.add(new TreasureReward(5,200,0.8f, net.minecraft.item.Items.GOLDEN_LEGGINGS, 1));

        ALL_REWARDS.add(new TreasureReward(5,250,1, net.minecraft.item.Items.NAUTILUS_SHELL, 1));
        ALL_REWARDS.add(new TreasureReward(5,250,1, net.minecraft.item.Items.SADDLE, 2));
        ALL_REWARDS.add(new TreasureReward(5,250,1, net.minecraft.item.Items.NAME_TAG, 3));

        ALL_REWARDS.add(new TreasureReward(5,300,8, net.minecraft.item.Items.EMERALD, 3));

        ALL_REWARDS.add(new TreasureReward(5,80,20, Items.FISH_COIN_BUNDLE, 5));


        ALL_REWARDS.add(new TreasureReward(6,250,1, Items.FISHING_NET, 2));
        ALL_REWARDS.add(new TreasureReward(6,350,1, Items.DOUBLE_FISHING_NET, 1));

        ALL_REWARDS.add(new TreasureReward(6,300,0.6f, net.minecraft.item.Items.IRON_BOOTS, 1));
        ALL_REWARDS.add(new TreasureReward(6,300,0.6f, net.minecraft.item.Items.IRON_CHESTPLATE, 1));
        ALL_REWARDS.add(new TreasureReward(6,300,0.6f, net.minecraft.item.Items.IRON_HELMET, 1));
        ALL_REWARDS.add(new TreasureReward(6,300,0.6f, net.minecraft.item.Items.IRON_LEGGINGS, 1));

        ALL_REWARDS.add(new TreasureReward(6,100,30, Items.FISH_COIN_BUNDLE, 5));

        ALL_REWARDS.add(new TreasureReward(7,800,0.2f, net.minecraft.item.Items.DIAMOND_BOOTS, 1));
        ALL_REWARDS.add(new TreasureReward(7,800,0.2f, net.minecraft.item.Items.DIAMOND_CHESTPLATE, 1));
        ALL_REWARDS.add(new TreasureReward(7,800,0.2f, net.minecraft.item.Items.DIAMOND_HELMET, 1));
        ALL_REWARDS.add(new TreasureReward(7,800,0.2f, net.minecraft.item.Items.DIAMOND_LEGGINGS, 1));

        ALL_REWARDS.add(new TreasureReward(7,800,4, net.minecraft.item.Items.DIAMOND, 1));
        ALL_REWARDS.add(new TreasureReward(7,100,40, Items.FISH_COIN_BUNDLE, 5));

    }


    public static Reward roll(Card fisher, RodConfiguration rodConfiguration, int luckOfTheSeaLevel){
        int grade = getGrade(fisher, rodConfiguration, luckOfTheSeaLevel);
        return new Reward(roll(fisher, getCost(fisher), grade), grade);
    }

    public  static int getCost(Card fisher){
        int avgCost = (int) (60 * Math.sqrt(fisher.getLevel()));
        return (int) (60 + (60 + avgCost * Math.abs(random.nextGaussian() / 2)));
    }

    public static int getGrade(Card fisher, RodConfiguration rodConfiguration, int luckOfTheSeaLevel){
        float avgGrade = (float) Math.max(1, Math.min(7, Math.sqrt(fisher.getLevel()) / 3));
        avgGrade *= (1 + rodConfiguration.attributes().treasureRarityBonus());
        avgGrade += (float) (Math.random() * luckOfTheSeaLevel);
        return (int)  Math.max(1, Math.min(7, (avgGrade * Math.abs(random.nextGaussian()) / 1.5)));
    }

    public static ArrayList<ItemStack> roll(Card card, int costTotal, int grade){
        ArrayList<ItemStack> rollResult = new ArrayList<>();
        int costLeft = costTotal;
        for(int i = 0; i < ((3 + grade) / 2f); i++) {
            ArrayList<TreasureReward> possibleRewards = getPossibleRewards(costLeft, grade);
            if (possibleRewards.size() == 0) break;
            int randomIndex = (int) (Math.random() * possibleRewards.size() - 1);
            TreasureReward reward = possibleRewards.get(randomIndex);
            rollResult.add(reward.roll(card.holder()));
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

    public static boolean draw(RodConfiguration rodConfiguration, Card card) {
        float treasureChance = TREASURE_MIN_CHANCE;
        treasureChance *= (1 + rodConfiguration.attributes().treasureBonus());
        treasureChance *= (1 + card.tradeSecretValue(TradeSecrets.TREASURE_CHANCE_BOAT));
        return Math.random() < treasureChance;
    }
}

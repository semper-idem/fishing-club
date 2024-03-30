package net.semperidem.fishingclub.fisher.shop;

import net.minecraft.item.Items;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItems;

import java.util.ArrayList;
import java.util.HashMap;

public class MemberStock {
    public final static String FISHER_STOCK_KEY = "FISHER";
    public final static String LUMBERJACK_STOCK_KEY = "LUMBERJACK";
    public final static HashMap<String, ArrayList<StockEntry>> STOCK = new HashMap<>();
    public final static ArrayList<StockEntry> FISHER_STOCK = new ArrayList<>();
    public final static ArrayList<StockEntry> LUMBERJACK_STOCK = new ArrayList<>();
    static {
        FISHER_STOCK.add(new StockEntry(0, Items.KELP, 10, 10, 8, 5));
        FISHER_STOCK.add(new StockEntry(0, Items.BARREL, 250, 4, 1, 100));
        FISHER_STOCK.add(new StockEntry(0, FishingRodPartItems.CORE_BAMBOO, 500));
        FISHER_STOCK.add(new StockEntry(0, FishingRodPartItems.BOBBER_WOODEN, 400));
        FISHER_STOCK.add(new StockEntry(0, FishingRodPartItems.LINE_WOOL_THREAD, 300));
        FISHER_STOCK.add(new StockEntry(0, FishingRodPartItems.HOOK_COPPER, 300));
        FISHER_STOCK.add(new StockEntry(0, FishingRodPartItems.BAIT_WORM, 100));

        FISHER_STOCK.add(new StockEntry(15, FishingRodPartItems.CORE_COMPOSITE, 2500));
        FISHER_STOCK.add(new StockEntry(15, FishingRodPartItems.CORE_GOLDEN, 4000));
        FISHER_STOCK.add(new StockEntry(15, FishingRodPartItems.BOBBER_PLANT, 2000));
        FISHER_STOCK.add(new StockEntry(15, FishingRodPartItems.LINE_SPIDER_SILK, 1500));
        FISHER_STOCK.add(new StockEntry(15, FishingRodPartItems.LINE_FIBER_THREAD, 1500));
        FISHER_STOCK.add(new StockEntry(15, FishingRodPartItems.HOOK_IRON, 1500));
        FISHER_STOCK.add(new StockEntry(15, FishingRodPartItems.HOOK_GOLD, 1500));
        FISHER_STOCK.add(new StockEntry(15, FishingRodPartItems.BAIT_FEATHER, 500));
        FISHER_STOCK.add(new StockEntry(15, FishingRodPartItems.BAIT_CRAFTED, 500));


        FISHER_STOCK.add(new StockEntry(30, FishingRodPartItems.CORE_NETHERITE, 10000));
        FISHER_STOCK.add(new StockEntry(30, FishingRodPartItems.BOBBER_ANCIENT, 8000));
        FISHER_STOCK.add(new StockEntry(30, FishingRodPartItems.LINE_STRIDER_THREAD, 7000));
        FISHER_STOCK.add(new StockEntry(30, FishingRodPartItems.HOOK_NETHERITE, 8000));
        FISHER_STOCK.add(new StockEntry(30, FishingRodPartItems.BAIT_MAGNET, 750));
        STOCK.put(FISHER_STOCK_KEY, FISHER_STOCK);


        LUMBERJACK_STOCK.add(new StockEntry(10, Items.OAK_LOG, 100, 4, 8, 20));
        LUMBERJACK_STOCK.add(new StockEntry(10, Items.OAK_SAPLING, 500, 2, 1, 250));
        LUMBERJACK_STOCK.add(new StockEntry(10, Items.APPLE, 500, 2, 1, 250));

        LUMBERJACK_STOCK.add(new StockEntry(15, Items.BIRCH_LOG, 120, 4, 8, 25));
        LUMBERJACK_STOCK.add(new StockEntry(15, Items.BIRCH_SAPLING, 500, 2, 1, 250));
        LUMBERJACK_STOCK.add(new StockEntry(15, Items.BAMBOO, 25, 4, 1, 5));//todo replace with planks

        LUMBERJACK_STOCK.add(new StockEntry(20, Items.SPRUCE_LOG, 150, 4, 8, 30));
        LUMBERJACK_STOCK.add(new StockEntry(20, Items.SPRUCE_SAPLING, 500, 2, 1, 250));
        LUMBERJACK_STOCK.add(new StockEntry(20, Items.DARK_OAK_LOG, 100, 4, 8, 20));
        LUMBERJACK_STOCK.add(new StockEntry(20, Items.DARK_OAK_SAPLING, 250, 2, 1, 100));
        LUMBERJACK_STOCK.add(new StockEntry(20, Items.JUNGLE_LOG, 100, 4, 8, 20));
        LUMBERJACK_STOCK.add(new StockEntry(20, Items.JUNGLE_SAPLING, 250, 2, 1, 100));
        LUMBERJACK_STOCK.add(new StockEntry(20, Items.GOLDEN_APPLE, 2000, 2.5, 1, 1000));

        LUMBERJACK_STOCK.add(new StockEntry(25, Items.ACACIA_LOG, 120, 4, 8, 25));
        LUMBERJACK_STOCK.add(new StockEntry(25, Items.ACACIA_SAPLING, 500, 2, 1, 250));
        LUMBERJACK_STOCK.add(new StockEntry(25, Items.SPRUCE_LOG, 150, 4, 8, 30));//replace with cherry
        LUMBERJACK_STOCK.add(new StockEntry(25, Items.SPRUCE_SAPLING, 500, 2, 1, 250));//replace with cherry
        LUMBERJACK_STOCK.add(new StockEntry(25, Items.MANGROVE_LOG, 150, 4, 8, 30));
        LUMBERJACK_STOCK.add(new StockEntry(25, Items.MANGROVE_PROPAGULE, 500, 2, 1, 250));

        LUMBERJACK_STOCK.add(new StockEntry(30, Items.CRIMSON_HYPHAE, 100, 4, 8, 20));
        LUMBERJACK_STOCK.add(new StockEntry(30, Items.CRIMSON_FUNGUS, 500, 2, 1, 250));
        LUMBERJACK_STOCK.add(new StockEntry(30, Items.WARPED_HYPHAE, 100, 4, 8, 20));
        LUMBERJACK_STOCK.add(new StockEntry(30, Items.WARPED_FUNGUS, 500, 2, 1, 250));
        LUMBERJACK_STOCK.add(new StockEntry(30, Items.ENCHANTED_GOLDEN_APPLE, 10000, 2, 1, 5000));
        STOCK.put(LUMBERJACK_STOCK_KEY, LUMBERJACK_STOCK);

    }
}

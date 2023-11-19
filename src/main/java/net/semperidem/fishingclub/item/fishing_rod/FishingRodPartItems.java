package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.Item;

import static net.minecraft.util.Rarity.*;
import static net.semperidem.fishingclub.item.fishing_rod.FishingRodStat.*;
import static net.semperidem.fishingclub.registry.FItemRegistry.registerItem;

public class FishingRodPartItems {
    public static FishingRodPartItem CORE_BAMBOO;
    public static FishingRodPartItem CORE_COMPOSITE;
    public static FishingRodPartItem CORE_GOLDEN;
    public static FishingRodPartItem CORE_NETHERITE;

    public static FishingRodPartItem BOBBER_WOODEN;
    public static FishingRodPartItem BOBBER_PLANT;
    public static FishingRodPartItem BOBBER_ANCIENT;

    public static FishingRodPartItem LINE_SPIDER_SILK;
    public static FishingRodPartItem LINE_WOOL_THREAD;
    public static FishingRodPartItem LINE_FIBER_THREAD;
    public static FishingRodPartItem LINE_STRIDER_THREAD;

    public static FishingRodPartItem HOOK_COPPER;
    public static FishingRodPartItem HOOK_IRON;
    public static FishingRodPartItem HOOK_GOLD;
    public static FishingRodPartItem HOOK_NETHERITE;

    public static FishingRodPartItem BAIT_WORM;
    public static FishingRodPartItem BAIT_FEATHER;
    public static FishingRodPartItem BAIT_CRAFTED;
    public static FishingRodPartItem BAIT_MAGNET;

    static {
        CORE_BAMBOO = createCorePart(new Item.Settings())
                .withStat(ROD_DAMAGE_CHANCE, 0.85f);
        CORE_COMPOSITE = createCorePart(new Item.Settings().rarity(UNCOMMON))
                .withStat(ROD_DAMAGE_CHANCE, 0.2f);
        CORE_GOLDEN = createCorePart(new Item.Settings().rarity(UNCOMMON))
                .withStat(ROD_DAMAGE_CHANCE, 1.8f);
        CORE_NETHERITE = createCorePart(new Item.Settings().fireproof().rarity(RARE))
                .withStat(ROD_DAMAGE_CHANCE, 0.3f);


        BOBBER_WOODEN = createBobberPart(new Item.Settings())
                .withStat(BITE_WINDOW_MULTIPLIER, 5f)
                .withStat(BOBBER_WIDTH, 0.05f);
        BOBBER_PLANT = createBobberPart(new Item.Settings())
                .withStat(BITE_WINDOW_MULTIPLIER, 10f)
                .withStat(BOBBER_WIDTH, 0.1f);
        BOBBER_ANCIENT = createBobberPart(new Item.Settings().rarity(RARE))
                .withStat(BITE_WINDOW_MULTIPLIER, 20f)
                .withStat(BOBBER_WIDTH, 0.2f);

        LINE_WOOL_THREAD = createLinePart(new Item.Settings().rarity(COMMON).maxDamage(4))
                .withStat(LINE_HEALTH, 10f);
        LINE_SPIDER_SILK = createLinePart(new Item.Settings().rarity(COMMON).maxDamage(4))
                .withStat(PROGRESS_MULTIPLIER, 1.1f)
                .withStat(FISH_MAX_WEIGHT_MULTIPLIER, 1.1f)
                .withStat(LINE_HEALTH, 25f);
        LINE_FIBER_THREAD = createLinePart(new Item.Settings().rarity(UNCOMMON).maxDamage(8))
                .withStat(PROGRESS_MULTIPLIER, 1.1f)
                .withStat(FISH_MAX_WEIGHT_MULTIPLIER, 1.5f)
                .withStat(LINE_HEALTH, 50f);
        LINE_STRIDER_THREAD = createLinePart(new Item.Settings().fireproof().rarity(UNCOMMON).maxDamage(32))
                .withStat(PROGRESS_MULTIPLIER, 1.5f)
                .withStat(FISH_MAX_WEIGHT_MULTIPLIER, 1.1f)
                .withStat(LINE_HEALTH, 100f);

        HOOK_COPPER = createHookPart(new Item.Settings().maxDamage(1))
                .withStat(FISH_MAX_WEIGHT_MULTIPLIER, 0.75f)
                .withStat(FISH_MAX_LENGTH_MULTIPLIER, 0.75f);
        HOOK_IRON = createHookPart(new Item.Settings().maxDamage(4))
                .withStat(DAMAGE_REDUCTION, 0.1f);
        HOOK_GOLD = createHookPart(new Item.Settings().rarity(UNCOMMON).maxDamage(2))
                .withStat(FISH_MAX_WEIGHT_MULTIPLIER, 0.75f)
                .withStat(FISH_MAX_LENGTH_MULTIPLIER, 0.75f)
                .withStat(CATCH_RATE, 0.20f)
                .withStat(FISH_RARITY_BONUS, 1.25f);
        HOOK_NETHERITE = createHookPart(new Item.Settings().fireproof().rarity(RARE).maxDamage(8))
                .withStat(DAMAGE_REDUCTION, 0.3f)
                .withStat(FISH_MAX_WEIGHT_MULTIPLIER, 1.1f)
                .withStat(FISH_MAX_LENGTH_MULTIPLIER, 1.1f)
                .withStat(CATCH_RATE, 0.1f)
                .withStat(FISH_RARITY_BONUS, 1.1f);

        BAIT_WORM = createBaitPart(new Item.Settings().maxDamage(16));
        BAIT_FEATHER = createBaitPart(new Item.Settings().maxDamage(8));
        BAIT_CRAFTED = createBaitPart(new Item.Settings().maxDamage(32));
        BAIT_MAGNET = createBaitPart(new Item.Settings().maxDamage(64));
    }

    public static void registerParts(){
        registerItem("bait_magnet",          BAIT_MAGNET);
        registerItem("bait_crafted",         BAIT_CRAFTED);
        registerItem("bait_feather",         BAIT_FEATHER);
        registerItem("bait_worm",            BAIT_WORM);

        registerItem("hook_netherite",       HOOK_NETHERITE);
        registerItem("hook_gold",            HOOK_GOLD);
        registerItem("hook_iron",            HOOK_IRON);
        registerItem("hook_copper",          HOOK_COPPER);

        registerItem("line_strider_thread",  LINE_STRIDER_THREAD);
        registerItem("line_fiber_thread",    LINE_FIBER_THREAD);
        registerItem("line_wool_thread",     LINE_WOOL_THREAD);
        registerItem("line_spider_silk",     LINE_SPIDER_SILK);

        registerItem("bobber_ancient",       BOBBER_ANCIENT);
        registerItem("bobber_plant",         BOBBER_PLANT);
        registerItem("bobber_wooden",        BOBBER_WOODEN);

        registerItem("core_bamboo",          CORE_BAMBOO);
        registerItem("core_composite",       CORE_COMPOSITE);
        registerItem("core_golden",          CORE_GOLDEN);
        registerItem("core_netherite",       CORE_NETHERITE);

    }


    public static FishingRodPartItem createBaitPart(Item.Settings settings){
        return new FishingRodPartItem(settings, FishingRodPartType.BAIT);
    }
    public static FishingRodPartItem createHookPart(Item.Settings settings){
        return new FishingRodPartItem(settings, FishingRodPartType.HOOK);
    }
    public static FishingRodPartItem createLinePart(Item.Settings settings){
        return new FishingRodPartItem(settings, FishingRodPartType.LINE);
    }
    public static FishingRodPartItem createCorePart(Item.Settings settings){
        return new FishingRodPartItem(settings, FishingRodPartType.CORE);
    }
    public static FishingRodPartItem createBobberPart(Item.Settings settings){
        return new FishingRodPartItem(settings, FishingRodPartType.BOBBER);
    }

}

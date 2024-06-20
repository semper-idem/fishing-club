package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.Item;
import net.semperidem.fishingclub.item.fishing_rod.components.ComponentItem;
import net.semperidem.fishingclub.item.fishing_rod.components.CoreComponentItem;
import net.semperidem.fishingclub.item.fishing_rod.components.LineComponentItem;

import static net.minecraft.util.Rarity.*;
import static net.semperidem.fishingclub.item.fishing_rod.FishingRodStatType.*;
import static net.semperidem.fishingclub.registry.ItemRegistry.registerItem;

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


    public static final ComponentItem EMPTY_COMPONENT = new ComponentItem(new Item.Settings());


    public static final CoreComponentItem CORE_WOODEN_OAK = new CoreComponentItem(new Item.Settings().maxDamage(64).rarity(COMMON))
            .weightCapacity(10)
            .castPowerMultiplier(0.9f);

    public static final CoreComponentItem CORE_IRON = new CoreComponentItem(new Item.Settings().maxDamage(128).rarity(COMMON))
            .weightCapacity(15)
            .castPowerMultiplier(0.9f);



    public static final LineComponentItem LINE_SPIDER = new LineComponentItem(new Item.Settings().maxDamage(32).rarity(COMMON))
            .weightCapacity(15)
            .maxLineLength(8);

    public static final LineComponentItem LINE_WOOL = new LineComponentItem(new Item.Settings().maxDamage(32).rarity(COMMON))
            .weightCapacity(10)
            .maxLineLength(64);



    static {
        CORE_BAMBOO = createCorePart(new Item.Settings(),
                ROD_DAMAGE_CHANCE.of(0.85f));
        CORE_COMPOSITE = createCorePart(new Item.Settings().rarity(UNCOMMON),
                ROD_DAMAGE_CHANCE.of(0.2f));
        CORE_GOLDEN = createCorePart(new Item.Settings().rarity(UNCOMMON),
                ROD_DAMAGE_CHANCE.of(1.8f));
        CORE_NETHERITE = createCorePart(new Item.Settings().fireproof().rarity(RARE),
                ROD_DAMAGE_CHANCE.of(0.3f));


        BOBBER_WOODEN = createBobberPart(new Item.Settings(),
                BITE_WINDOW_MULTIPLIER.of(5f), BOBBER_WIDTH.of(0.05f));
        BOBBER_PLANT = createBobberPart(new Item.Settings(),
                BITE_WINDOW_MULTIPLIER.of(10f), BOBBER_WIDTH.of(0.1f));
        BOBBER_ANCIENT = createBobberPart(new Item.Settings().rarity(RARE),
                BITE_WINDOW_MULTIPLIER.of(20f), BOBBER_WIDTH.of(0.2f));

        LINE_WOOL_THREAD = createLinePart(new Item.Settings().rarity(COMMON).maxDamage(4),
                LINE_HEALTH.of(10f));
        LINE_SPIDER_SILK = createLinePart(new Item.Settings().rarity(COMMON).maxDamage(4),
                LINE_HEALTH.of(25f), PROGRESS_MULTIPLIER_BONUS.of(0.1f), FISH_MAX_WEIGHT_MULTIPLIER.of(1.1f));
        LINE_FIBER_THREAD = createLinePart(new Item.Settings().rarity(UNCOMMON).maxDamage(8),
                LINE_HEALTH.of(50f), PROGRESS_MULTIPLIER_BONUS.of(0.1f), FISH_MAX_WEIGHT_MULTIPLIER.of(1.5f));
        LINE_STRIDER_THREAD = createLinePart(new Item.Settings().fireproof().rarity(UNCOMMON).maxDamage(32),
                LINE_HEALTH.of(100f), PROGRESS_MULTIPLIER_BONUS.of(0.5f), FISH_MAX_LENGTH_MULTIPLIER.of(1.1f));

        HOOK_COPPER = createHookPart(new Item.Settings().maxDamage(1),
                FISH_MAX_WEIGHT_MULTIPLIER.of(0.75f), FISH_MAX_LENGTH_MULTIPLIER.of(0.75f));
        HOOK_IRON = createHookPart(new Item.Settings().maxDamage(4),
                DAMAGE_REDUCTION.of(0.1f));
        HOOK_GOLD = createHookPart(new Item.Settings().rarity(UNCOMMON).maxDamage(2),
                FISH_MAX_WEIGHT_MULTIPLIER.of(0.75f), FISH_MAX_LENGTH_MULTIPLIER.of(0.75f), CATCH_RATE.of(0.2f), FISH_RARITY_BONUS.of(1.25f));
        HOOK_NETHERITE = createHookPart(new Item.Settings().fireproof().rarity(RARE).maxDamage(8),
                DAMAGE_REDUCTION.of(0.3f), FISH_MAX_WEIGHT_MULTIPLIER.of(1.1f), FISH_MAX_LENGTH_MULTIPLIER.of(1.1f), CATCH_RATE.of(0.1f), FISH_RARITY_BONUS.of(1.1f));

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

        registerItem("empty_component", EMPTY_COMPONENT);
        registerItem("core_wooden_oak", CORE_WOODEN_OAK);
        registerItem("core_iron", CORE_IRON);
        registerItem("line_spider", LINE_SPIDER);
        registerItem("line_wool", LINE_WOOL);

    }


    public static FishingRodPartItem createBaitPart(Item.Settings settings, FishingRodStat... stats){
        return new FishingRodPartItem(settings, FishingRodPartType.BAIT, stats);
    }
    public static FishingRodPartItem createHookPart(Item.Settings settings, FishingRodStat... stats){
        return new FishingRodPartItem(settings, FishingRodPartType.HOOK, stats);
    }
    public static FishingRodPartItem createLinePart(Item.Settings settings, FishingRodStat... stats){
        return new FishingRodPartItem(settings, FishingRodPartType.LINE, stats);
    }
    public static FishingRodPartItem createCorePart(Item.Settings settings, FishingRodStat... stats){
        return new FishingRodPartItem(settings, FishingRodPartType.CORE, stats);
    }
    public static FishingRodPartItem createBobberPart(Item.Settings settings, FishingRodStat... stats){
        return new FishingRodPartItem(settings, FishingRodPartType.BOBBER, stats);
    }

}

package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FishingRodPartItems {
    public static HashMap<String, FishingRodPartItem> KEY_TO_PART_MAP = new HashMap<>();
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
        CORE_BAMBOO = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON),
                FishingRodPartType.CORE,
                "CORE_BAMBOO"
        );
        CORE_COMPOSITE = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.UNCOMMON),
                FishingRodPartType.CORE,
                "CORE_COMPOSITE"
        );
        CORE_GOLDEN = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.UNCOMMON),
                FishingRodPartType.CORE,
                "CORE_GOLDEN"
        );
        CORE_NETHERITE = new FishingRodPartItem(
                new Item.Settings().fireproof().rarity(Rarity.RARE),
                FishingRodPartType.CORE,
                "CORE_NETHERITE"
        );


        BOBBER_WOODEN = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON),
                FishingRodPartType.BOBBER,
                "BOBBER_WOODEN")
                .withStat(FishingRodStat.BITE_WINDOW_MULTIPLIER, 5f)
                .withStat(FishingRodStat.BOBBER_WIDTH, 0.05f);
        BOBBER_PLANT = new FishingRodPartItem(new Item.Settings().rarity(Rarity.COMMON),
                FishingRodPartType.BOBBER,
                "BOBBER_PLANT")
                .withStat(FishingRodStat.BITE_WINDOW_MULTIPLIER, 10f)
                .withStat(FishingRodStat.BOBBER_WIDTH, 0.1f);
        BOBBER_ANCIENT = new FishingRodPartItem(new Item.Settings().rarity(Rarity.RARE),
                FishingRodPartType.BOBBER,
                "BOBBER_ANCIENT")
                .withStat(FishingRodStat.BITE_WINDOW_MULTIPLIER, 20f)
                .withStat(FishingRodStat.BOBBER_WIDTH, 0.2f);

        LINE_WOOL_THREAD = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON).maxDamage(4),
                FishingRodPartType.LINE,
                "LINE_WOOL_THREAD")
                .withStat(FishingRodStat.LINE_HEALTH, 10f);
        LINE_SPIDER_SILK = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON).maxDamage(4),
                FishingRodPartType.LINE,
                "LINE_SPIDER_SILK")
                .withStat(FishingRodStat.PROGRESS_MULTIPLIER, 1.1f)
                .withStat(FishingRodStat.FISH_MAX_WEIGHT_MULTIPLIER, 1.1f)
                .withStat(FishingRodStat.LINE_HEALTH, 25f);
        LINE_FIBER_THREAD = new FishingRodPartItem(new Item.Settings().rarity(Rarity.UNCOMMON).maxDamage(8),
                FishingRodPartType.LINE,
                "LINE_FIBER_THREAD")
                .withStat(FishingRodStat.PROGRESS_MULTIPLIER, 1.1f)
                .withStat(FishingRodStat.FISH_MAX_WEIGHT_MULTIPLIER, 1.5f)
                .withStat(FishingRodStat.LINE_HEALTH, 50f);
        LINE_STRIDER_THREAD = new FishingRodPartItem(
                new Item.Settings().fireproof().rarity(Rarity.UNCOMMON).maxDamage(32),
                FishingRodPartType.LINE,
                "LINE_STRIDER_THREAD")
                .withStat(FishingRodStat.PROGRESS_MULTIPLIER, 1.5f)
                .withStat(FishingRodStat.FISH_MAX_WEIGHT_MULTIPLIER, 1.1f)
                .withStat(FishingRodStat.LINE_HEALTH, 100f);

        HOOK_COPPER = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON),
                FishingRodPartType.HOOK,
                "HOOK_COPPER")
                .withStat(FishingRodStat.FISH_MAX_WEIGHT_MULTIPLIER, 0.75f)
                .withStat(FishingRodStat.FISH_MAX_LENGTH_MULTIPLIER, 0.75f);
        HOOK_IRON = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON).maxDamage(4),
                FishingRodPartType.HOOK,
                "HOOK_IRON")
                .withStat(FishingRodStat.DAMAGE_REDUCTION, 0.1f);
        HOOK_GOLD = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.UNCOMMON).maxDamage(2),
                FishingRodPartType.HOOK,
                "HOOK_GOLD")
                .withStat(FishingRodStat.FISH_MAX_WEIGHT_MULTIPLIER, 0.75f)
                .withStat(FishingRodStat.FISH_MAX_LENGTH_MULTIPLIER, 0.75f)
                .withStat(FishingRodStat.CATCH_RATE, 0.20f)
                .withStat(FishingRodStat.FISH_RARITY_BONUS, 1.25f);
        HOOK_NETHERITE = new FishingRodPartItem(
                new Item.Settings().fireproof().rarity(Rarity.RARE).maxDamage(8),
                FishingRodPartType.HOOK,
                "HOOK_NETHERITE")
                .withStat(FishingRodStat.DAMAGE_REDUCTION, 0.3f)
                .withStat(FishingRodStat.FISH_MAX_WEIGHT_MULTIPLIER, 1.1f)
                .withStat(FishingRodStat.FISH_MAX_LENGTH_MULTIPLIER, 1.1f)
                .withStat(FishingRodStat.CATCH_RATE, 0.1f)
                .withStat(FishingRodStat.FISH_RARITY_BONUS, 1.1f);

        BAIT_WORM = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON).maxDamage(16),
                FishingRodPartType.BAIT,
                "BAIT_WORM");
        BAIT_FEATHER = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON).maxDamage(8),
                FishingRodPartType.BAIT,
                "BAIT_FEATHER");
        BAIT_CRAFTED = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON).maxDamage(32),
                FishingRodPartType.BAIT,
                "BAIT_CRAFTED");
        BAIT_MAGNET = new FishingRodPartItem(
                new Item.Settings().rarity(Rarity.COMMON).maxDamage(64),
                FishingRodPartType.BAIT,
                "BAIT_MAGNET");
    }

    public static void registerParts(){
        for(FishingRodPartItem part : KEY_TO_PART_MAP.values()) {
            Registry.register(Registry.ITEM, new Identifier(MOD_ID, part.getKey().toLowerCase()), part);
        }
    }

    public static ItemStack getStackFromCompound(NbtCompound partNbt){
        if (!partNbt.contains("key")) {
            return ItemStack.EMPTY;
        }
        String partKey = partNbt.getString("key");
        ItemStack partStack =  KEY_TO_PART_MAP.get(partKey).getDefaultStack();
        if (!partStack.hasNbt()) {
            partStack.setNbt(new NbtCompound());
        }
        partNbt = partStack.getNbt();
        partNbt.putString("key", partKey);
        partStack.setNbt(partNbt);
        return partStack;
    }
}

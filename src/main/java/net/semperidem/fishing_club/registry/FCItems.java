package net.semperidem.fishing_club.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.item.*;
import net.semperidem.fishing_club.item.fishing_rod.MemberFishingRodItem;
import net.semperidem.fishing_club.item.fishing_rod.components.*;

import java.util.ArrayList;

import static net.minecraft.util.Rarity.*;

public class FCItems {

    public static DoubleFishingNetItem DOUBLE_FISHING_NET;
    public static MemberFishingRodItem MEMBER_FISHING_ROD;
    public static FishingNetItem FISHING_NET;
    public static Item FISH_COIN_BUNDLE;
    public static Item FISHER_HAT;
    public static Item FISHER_VEST;
    public static Item HARPOON_ROD;
    public static Item LINE_ARROW;
    public static Item CLONED_ROD;
    public static Item ILLEGAL_GOODS;
    public static Item GOLD_FISH;
    public static Item DEBUG;
    public static ItemGroup FISHING_CLUB_GROUP;

    public static CorePartItem CORE_BAMBOO;
    public static CorePartItem CORE_BAMBOO_WOOD;
    public static CorePartItem CORE_OAK_WOOD;
    public static CorePartItem CORE_BIRCH_WOOD;
    public static CorePartItem CORE_SPRUCE_WOOD;
    public static CorePartItem CORE_ACACIA_WOOD;
    public static CorePartItem CORE_JUNGLE_WOOD;
    public static CorePartItem CORE_CHERRY_WOOD;
    public static CorePartItem CORE_DARK_OAK_WOOD;
    public static CorePartItem CORE_MANGROVE_WOOD;
    public static CorePartItem CORE_CRIMSON_WOOD;
    public static CorePartItem CORE_WARPED_WOOD;

    public static CorePartItem CORE_BONE;
    public static CorePartItem CORE_WITHER_BONE;
    public static CorePartItem CORE_IRON;
    public static CorePartItem CORE_BAMBOO_IRON;
    public static CorePartItem CORE_OAK_IRON;
    public static CorePartItem CORE_BIRCH_IRON;
    public static CorePartItem CORE_SPRUCE_IRON;
    public static CorePartItem CORE_ACACIA_IRON;
    public static CorePartItem CORE_JUNGLE_IRON;
    public static CorePartItem CORE_CHERRY_IRON;
    public static CorePartItem CORE_DARK_OAK_IRON;
    public static CorePartItem CORE_MANGROVE_IRON;
    public static CorePartItem CORE_CRIMSON_IRON;
    public static CorePartItem CORE_WARPED_IRON;
    public static CorePartItem CORE_COPPER;
    public static CorePartItem CORE_BAMBOO_COPPER;
    public static CorePartItem CORE_OAK_COPPER;
    public static CorePartItem CORE_BIRCH_COPPER;
    public static CorePartItem CORE_SPRUCE_COPPER;
    public static CorePartItem CORE_ACACIA_COPPER;
    public static CorePartItem CORE_JUNGLE_COPPER;
    public static CorePartItem CORE_CHERRY_COPPER;
    public static CorePartItem CORE_DARK_OAK_COPPER;
    public static CorePartItem CORE_MANGROVE_COPPER;
    public static CorePartItem CORE_CRIMSON_COPPER;
    public static CorePartItem CORE_WARPED_COPPER;
    public static CorePartItem CORE_PRISMARINE_BLAZE;
    public static CorePartItem CORE_NETHERITE;
    public static CorePartItem CORE_NAUTILUS_BREEZE;
    public static CorePartItem CORE_NAUTILUS_BLAZE;
    public static CorePartItem CORE_NAUTILUS_END;


    public static ReelPartItem REEL_WOOD;
    public static ReelPartItem REEL_BONE;
    public static ReelPartItem REEL_PRISMARINE;
    public static ReelPartItem REEL_IRON;
    public static ReelPartItem REEL_IRON_PRISMARINE;
    public static ReelPartItem REEL_AMETHYST_PRISMARINE;
    public static ReelPartItem REEL_NAUTILUS_ECHO;
    public static ReelPartItem REEL_NAUTILUS_AMETHYST;
    public static ReelPartItem REEL_HEART_AMETHYST;
    public static ReelPartItem REEL_HEART_ECHO;
    public static BobberPartItem BOBBER_PLANT_SLIME;
    public static BobberPartItem BOBBER_LEATHER_SLIME;
    public static BobberPartItem BOBBER_TURTLE_SLIME;
    public static BobberPartItem BOBBER_ARMADILLO_SLIME;
    public static BobberPartItem BOBBER_TURTLE_ENDER;
    public static BobberPartItem BOBBER_ARMADILLO_MAGMA;
    public static BobberPartItem BOBBER_AMETHYST_ENDER;
    public static BobberPartItem BOBBER_NAUTILUS;
    public static BobberPartItem BOBBER_TURTLE_ECHO;
    public static BobberPartItem BOBBER_ARMADILLO_ECHO;
    public static BobberPartItem BOBBER_AMETHYST_ECHO;
    public static BobberPartItem BOBBER_NAUTILUS_ECHO;
    public static BobberPartItem BOBBER_HEART;
    public static LinePartItem LINE_PLANT;
    public static LinePartItem LINE_WOOL;
    public static LinePartItem LINE_SPIDER;
    public static LinePartItem LINE_STRIDER;
    public static LinePartItem LINE_PHANTOM;
    public static LinePartItem LINE_EVOKER;
    public static LinePartItem LINE_PIGLIN;
    public static LinePartItem LINE_HEART;
    public static LinePartItem LINE_HEART_PIGLIN;
    public static LinePartItem LINE_HEART_EVOKER;
    public static HookPartItem HOOK_FLINT;
    public static HookPartItem HOOK_FLINT_POISON;
    public static HookPartItem HOOK_FLINT_SHARP;
    public static HookPartItem HOOK_FLINT_SERRATED;
    public static HookPartItem HOOK_BONE;
    public static HookPartItem HOOK_BONE_POISON;
    public static HookPartItem HOOK_BONE_SHARP;
    public static HookPartItem HOOK_BONE_SERRATED;
    public static HookPartItem HOOK_COPPER;
    public static HookPartItem HOOK_COPPER_POISON;
    public static HookPartItem HOOK_COPPER_SHARP;
    public static HookPartItem HOOK_COPPER_SERRATED;
    public static HookPartItem HOOK_COPPER_AUTO;
    public static HookPartItem HOOK_IRON;
    public static HookPartItem HOOK_IRON_POISON;
    public static HookPartItem HOOK_IRON_SHARP;
    public static HookPartItem HOOK_IRON_SERRATED;
    public static HookPartItem HOOK_IRON_AUTO;
    public static HookPartItem HOOK_GOLD;
    public static HookPartItem HOOK_GOLD_POISON;
    public static HookPartItem HOOK_GOLD_SHARP;
    public static HookPartItem HOOK_GOLD_SERRATED;
    public static HookPartItem HOOK_GOLD_AUTO;
    public static HookPartItem HOOK_PRISMARINE;
    public static HookPartItem HOOK_PRISMARINE_POISON;
    public static HookPartItem HOOK_PRISMARINE_SHARP;
    public static HookPartItem HOOK_PRISMARINE_SERRATED;
    public static HookPartItem HOOK_PRISMARINE_AUTO;
    public static HookPartItem HOOK_NAUTILUS;
    public static HookPartItem HOOK_NAUTILUS_POISON;
    public static HookPartItem HOOK_NAUTILUS_SHARP;
    public static HookPartItem HOOK_NAUTILUS_SERRATED;
    public static HookPartItem HOOK_NAUTILUS_AUTO;
    public static HookPartItem HOOK_NETHERITE;
    public static HookPartItem HOOK_NETHERITE_POISON;
    public static HookPartItem HOOK_NETHERITE_SHARP;
    public static HookPartItem HOOK_NETHERITE_SERRATED;
    public static HookPartItem HOOK_NETHERITE_AUTO;
    public static BaitPartItem BAIT_PLANT;
    public static BaitPartItem BAIT_ROTTEN_FLESH;
    public static BaitPartItem BAIT_MEAT;
    public static BaitPartItem BAIT_ORCHID;
    public static BaitPartItem BAIT_GLOW_BERRIES;
    public static BaitPartItem BAIT_BLAZE_POWDER;
    public static BaitPartItem BAIT_IRON;
    public static BaitPartItem BAIT_GHAST;
    public static BaitPartItem BAIT_RABBIT;
    public static BaitPartItem BAIT_CHORUS;
    public static BaitPartItem BAIT_BREEZE_BLAZE;
    public static BaitPartItem BAIT_TORCHFLOWER;
    public static BaitPartItem BAIT_PITCHER_PLANT;
    public static BaitPartItem BAIT_SPECIAL;


    public static <T extends Item> T registerItem(String id, T item) {
        Registry.register(Registries.ITEM, FishingClub.getIdentifier(id), item);
        FISHING_ITEMS.add(item.getDefaultStack());
        return item;
    }

    public static void register() {
        registerParts();
        FISHING_NET = registerItem(("fishing_net"), new FishingNetItem(new Item.Settings().maxCount(1).maxDamage(64)));
        DOUBLE_FISHING_NET = registerItem(("double_fishing_net"), new DoubleFishingNetItem(new Item.Settings().maxCount(1).maxDamage(64)));
        MEMBER_FISHING_ROD = registerItem(("member_fishing_rod"), new MemberFishingRodItem(new Item.Settings().maxCount(1).maxDamage(100).component(FCComponents.ROD_CONFIGURATION, RodConfiguration.getDefault())));
        FISH_COIN_BUNDLE = registerItem(("fish_coin_bundle"), new FishCoinBundleItem(new Item.Settings().maxCount(1)));
        FISHER_HAT = registerItem(("fisher_hat"), new ArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Settings()));
        FISHER_VEST = registerItem(("fisher_vest"), new ArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Settings()));
        HARPOON_ROD = registerItem(("harpoon_rod"), new HarpoonRodItem(new Item.Settings().maxCount(1).maxDamage(64)));
        LINE_ARROW = registerItem(("line_arrow"), new LineArrowItem(new Item.Settings()));
        CLONED_ROD = registerItem(("cloned_rod"), new ClonedFishingRod(new Item.Settings().maxCount(1).maxDamage(128)));
        ILLEGAL_GOODS = registerItem(("illegal_goods"), new IllegalGoodsItem(new Item.Settings().rarity(RARE).maxCount(1)));
        GOLD_FISH = registerItem(("gold_fish"), new Item(new Item.Settings().maxCount(1)));
        DEBUG = registerItem("debug", new DebugItem(new Item.Settings()));
        registerItemGroup();
    }

    public static void registerCore() {
        CORE_BAMBOO = registerItem("core_bamboo", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 5)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T1)
          .bobberControl(ItemStat.BASE_T4)
          .fishControl(ItemStat.BASE_T4)
        );
        CORE_BAMBOO_WOOD = registerItem("core_bamboo_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 15)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T2)
          .fishControl(ItemStat.BASE_T2)
        );
        CORE_OAK_WOOD = registerItem("core_oak_wood", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_BIRCH_WOOD = registerItem("core_birch_wood", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_SPRUCE_WOOD = registerItem("core_spruce_wood", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_ACACIA_WOOD = registerItem("core_acacia_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_JUNGLE_WOOD = registerItem("core_jungle_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_CHERRY_WOOD = registerItem("core_cherry_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_DARK_OAK_WOOD = registerItem("core_dark_oak_wood", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T1)
          .fishControl(ItemStat.BASE_T1)
        );
        CORE_MANGROVE_WOOD = registerItem("core_mangrove_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T4)
          .bobberControl(ItemStat.BASE_T4)
        );
        CORE_CRIMSON_WOOD = registerItem("core_crimson_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10, 0, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)

        );
        CORE_WARPED_WOOD = registerItem("core_warped_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10, 0, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_BONE = registerItem("core_bone", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 5, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T2)
          .bobberControl(ItemStat.BASE_T3)
        );
        CORE_WITHER_BONE = registerItem("core_wither_bone", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 15, -1, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T2)
          .bobberControl(ItemStat.BASE_T3)

        );
        CORE_IRON = registerItem("core_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(COMMON), 15, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T2)
          .bobberControl(ItemStat.BASE_T3)
        );
        CORE_BAMBOO_IRON = registerItem("core_bamboo_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON),25, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T2)
          .bobberControl(ItemStat.BASE_T3)

        );
        CORE_OAK_IRON = registerItem("core_oak_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)

        );
        CORE_BIRCH_IRON = registerItem("core_birch_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)

        );
        CORE_SPRUCE_IRON = registerItem("core_spruce_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)

        );
        CORE_ACACIA_IRON = registerItem("core_acacia_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)

        );
        CORE_JUNGLE_IRON = registerItem("core_jungle_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)

        );
        CORE_CHERRY_IRON = registerItem("core_cherry_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)

        );
        CORE_DARK_OAK_IRON = registerItem("core_dark_oak_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 50, -1, 0)
          .bobberControl(ItemStat.BASE_T1)
          .fishControl(ItemStat.BASE_T1)
        );
        CORE_MANGROVE_IRON = registerItem("core_mangrove_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T4)
          .fishControl(ItemStat.BASE_T4)
        );
        CORE_CRIMSON_IRON = registerItem("core_crimson_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)

        );
        CORE_WARPED_IRON = registerItem("core_warped_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)

        );
        CORE_COPPER = registerItem("core_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(COMMON), 15)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_BAMBOO_COPPER = registerItem("core_bamboo_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_OAK_COPPER = registerItem("core_oak_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
          .fishQuality(1)
        );
        CORE_BIRCH_COPPER = registerItem("core_birch_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_SPRUCE_COPPER = registerItem("core_spruce_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_ACACIA_COPPER = registerItem("core_acacia_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_JUNGLE_COPPER = registerItem("core_jungle_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_CHERRY_COPPER = registerItem("core_cherry_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_DARK_OAK_COPPER = registerItem("core_dark_oak_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 50)
          .bobberControl(ItemStat.BASE_T2)
          .fishControl(ItemStat.BASE_T2));
        CORE_MANGROVE_COPPER = registerItem("core_mangrove_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T4)
          .fishControl(ItemStat.BASE_T4)
        );
        CORE_CRIMSON_COPPER = registerItem("core_crimson_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, 0, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_WARPED_COPPER = registerItem("core_warped_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, 0, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_PRISMARINE_BLAZE = registerItem("core_prismarine_blaze", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(RARE), 25, 0, 1)
          .fishQuality(3)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        CORE_NETHERITE = registerItem("core_netherite", new CorePartItem(new Item.Settings().maxDamage(20480).rarity(RARE), 100, -1, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T4)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
          .fishQuality(1));
        CORE_NAUTILUS_BREEZE = registerItem("core_nautilus_breeze", new CorePartItem(new Item.Settings().maxDamage(10240).rarity(EPIC), 100, -1, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T5)
          .bobberControl(ItemStat.BASE_T4)
          .fishControl(ItemStat.BASE_T4)
          .fishQuality(2));
        CORE_NAUTILUS_BLAZE = registerItem("core_nautilus_blaze", new CorePartItem(new Item.Settings().maxDamage(10240).rarity(EPIC), 100, -1, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T4)
          .bobberControl(ItemStat.BASE_T4)
          .fishControl(ItemStat.BASE_T4)
          .fishQuality(4));
        CORE_NAUTILUS_END = registerItem("core_nautilus_end", new CorePartItem(new Item.Settings().maxDamage(20480).rarity(EPIC), 300, -1, 1)
          .castPowerMultiplier(ItemStat.MULTIPLIER_T4)
          .fishControl(ItemStat.BASE_T5)
          .bobberControl(ItemStat.BASE_T5)
          .fishQuality(1));
    }

    public static void registerReel() {
        REEL_WOOD = registerItem("reel_wood", new ReelPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10)
          .bobberControl(ItemStat.BASE_T1)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T2)
        );
        REEL_BONE = registerItem("reel_bone", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 5)
          .bobberControl(ItemStat.BASE_T2)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T1)
        );
        REEL_PRISMARINE = registerItem("reel_prismarine", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 15)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T2)
        );
        REEL_IRON = registerItem("reel_iron", new ReelPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 25, -1, 0)
          .bobberControl(ItemStat.BASE_T2)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T2)
        );
        REEL_IRON_PRISMARINE = registerItem("reel_iron_prismarine", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 50, -1, 0)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        REEL_AMETHYST_PRISMARINE = registerItem("reel_amethyst_prismarine", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, -1, 1)
          .bobberControl(ItemStat.BASE_T2)
          .fishControl(ItemStat.BASE_T3)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
        );
        REEL_NAUTILUS_ECHO = registerItem("reel_nautilus_echo", new ReelPartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 100, -1, 1)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T4)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T4)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
        );
        REEL_NAUTILUS_AMETHYST = registerItem("reel_nautilus_amethyst", new ReelPartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 100, -1, 1)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
          .fishQuality(1)
        );
        REEL_HEART_AMETHYST = registerItem("reel_heart_amethyst", new ReelPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .bobberControl(ItemStat.BASE_T5)
          .fishControl(ItemStat.BASE_T3)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
          .fishQuality(3)
        );
        REEL_HEART_ECHO = registerItem("reel_heart_echo", new ReelPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
          .fishQuality(1)
        );
    }

    public static void registerBobber() {
        BOBBER_PLANT_SLIME = registerItem("bobber_plant_slime", new BobberPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 5)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
          .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .bobberWidth(ItemStat.MULTIPLIER_T1)
        );
        BOBBER_LEATHER_SLIME = registerItem("bobber_leather_slime", new BobberPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 15)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
          .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .bobberWidth(ItemStat.MULTIPLIER_T1)
        );
        BOBBER_TURTLE_SLIME = registerItem("bobber_turtle_slime", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .bobberControl(ItemStat.BASE_T2)
        );
        BOBBER_ARMADILLO_SLIME = registerItem("bobber_armadillo_slime", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25)
          .bobberWidth(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T2)
        );
        BOBBER_TURTLE_ENDER = registerItem("bobber_turtle_ender", new BobberPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 25, -1, 0)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberWidth(ItemStat.MULTIPLIER_T4)
          .bobberControl(ItemStat.BASE_T3)
        );
        BOBBER_ARMADILLO_MAGMA = registerItem("bobber_armadillo_magma", new BobberPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 25, 0, 1)
          .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4)
          .bobberWidth(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T3)
        );
        BOBBER_AMETHYST_ENDER = registerItem("bobber_amethyst_ender", new BobberPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
          .bobberControl(ItemStat.BASE_T2)
          .fishControl(ItemStat.BASE_T2)
          .fishQuality(2)
        );
        BOBBER_NAUTILUS = registerItem("bobber_nautilus", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(RARE), 100, -1, 0)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
        );
        BOBBER_TURTLE_ECHO = registerItem("bobber_turtle_echo", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(RARE), 75, -1, 1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .bobberWidth(ItemStat.MULTIPLIER_T4)
          .bobberControl(ItemStat.BASE_T4)
          .fishControl(ItemStat.BASE_T2)
          .fishQuality(1)
        );
        BOBBER_ARMADILLO_ECHO = registerItem("bobber_armadillo_echo", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(RARE), 75, -1, 1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T4)
          .bobberWidth(ItemStat.MULTIPLIER_T2)
          .bobberControl(ItemStat.BASE_T2)
          .fishControl(ItemStat.BASE_T4)
          .fishQuality(1)
        );
        BOBBER_AMETHYST_ECHO = registerItem("bobber_amethyst_echo", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(RARE), 75, -1, 1)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
          .fishQuality(1));
        BOBBER_NAUTILUS_ECHO = registerItem("bobber_nautilus_echo", new BobberPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
          .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
          .bobberWidth(ItemStat.MULTIPLIER_T5)
          .bobberControl(ItemStat.BASE_T5)
          .fishControl(ItemStat.BASE_T5)
          .fishQuality(2));
        BOBBER_HEART  = registerItem("bobber_heart", new BobberPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
          .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T4)
          .bobberWidth(ItemStat.MULTIPLIER_T4)
          .bobberControl(ItemStat.BASE_T3)
          .fishControl(ItemStat.BASE_T3)
          .fishQuality(4));
    }

    public static void registerLine() {

        LINE_PLANT = registerItem("line_plant", new LinePartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 5)
          .maxLineLength(4)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
        );
        LINE_WOOL = registerItem("line_wool", new LinePartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10)
          .maxLineLength(8)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
        );
        LINE_SPIDER = registerItem("line_spider", new LinePartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15)
          .maxLineLength(16)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
        );
        LINE_STRIDER = registerItem("line_strider", new LinePartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 1, 1)
          .maxLineLength(16)
          .fishControl(ItemStat.BASE_T3)
        );
        LINE_PHANTOM = registerItem("line_phantom", new LinePartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, -1, 0)
          .maxLineLength(32)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
        );
        LINE_EVOKER = registerItem("line_evoker", new LinePartItem(new Item.Settings().maxDamage(1280).rarity(RARE), 50)
          .maxLineLength(64)
          .fishControl(ItemStat.BASE_T3)
          .fishQuality(1)
        );
        LINE_PIGLIN = registerItem("line_piglin", new LinePartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 100, 0, 1)
          .maxLineLength(64)
          .fishControl(ItemStat.BASE_T3)
        );
        LINE_HEART = registerItem("line_heart", new LinePartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 50, -1, 1)
          .maxLineLength(128)
          .fishControl(ItemStat.BASE_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
        );
        LINE_HEART_PIGLIN = registerItem("line_heart_piglin", new LinePartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .maxLineLength(128)
          .fishControl(ItemStat.BASE_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
          .fishQuality(1)
        );
        LINE_HEART_EVOKER = registerItem("line_heart_evoker", new LinePartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .maxLineLength(256)
          .fishControl(ItemStat.BASE_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
          .fishQuality(3)
        );
    }

    private static void registerHook() {
        HOOK_FLINT = registerItem("hook_flint", new HookPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10, -1, 0)
          .fishRarity(ItemStat.BASE_T1)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
        );
        HOOK_FLINT_POISON = registerItem("hook_flint_poison", new HookPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10, -1, 0)
          .fishRarity(ItemStat.BASE_T1)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_FLINT_SHARP = registerItem("hook_flint_sharp", new HookPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10, -1, 0)
          .fishRarity(ItemStat.BASE_T1)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .damage(2)
          .biteFailChance(new ItemStat(0.1f, 1))
        );
        HOOK_FLINT_SERRATED = registerItem("hook_flint_serrated", new HookPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10, -1, 0)
          .fishRarity(ItemStat.BASE_T1)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(-1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
          .reelDamage(3)
        );
        HOOK_BONE = registerItem("hook_bone", new HookPartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15, -1, 0)
          .fishRarity(ItemStat.BASE_T2)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
        );
        HOOK_BONE_POISON = registerItem("hook_bone_poison", new HookPartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15, -1, 0)
          .fishRarity(ItemStat.BASE_T2)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_BONE_SHARP = registerItem("hook_bone_sharp", new HookPartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15, -1, 0)
          .fishRarity(ItemStat.BASE_T2)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .damage(2)
          .biteFailChance(ItemStat.MULTIPLIER_T01)
        );
        HOOK_BONE_SERRATED = registerItem("hook_bone_serrated", new HookPartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15, -1, 0)
          .fishRarity(ItemStat.BASE_T2)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .fishQuality(-1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
          .reelDamage(3)
        );
        HOOK_COPPER = registerItem("hook_copper", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
        );
        HOOK_COPPER_POISON = registerItem("hook_copper_poison", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T8)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_COPPER_SHARP = registerItem("hook_copper_sharp", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .damage(3)
          .biteFailChance(ItemStat.MULTIPLIER_T0125)
        );
        HOOK_COPPER_SERRATED = registerItem("hook_copper_serrated", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .fishQuality(-1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T6)
          .reelDamage(4)
        );
        HOOK_COPPER_AUTO = registerItem("hook_copper_auto", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T1)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T1)
          .autoHookChance(ItemStat.MULTIPLIER_T1)
        );
        HOOK_IRON = registerItem("hook_iron", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(ItemStat.BASE_T1)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
        );
        HOOK_IRON_POISON = registerItem("hook_iron_poison", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(ItemStat.BASE_T1)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T8)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_IRON_SHARP = registerItem("hook_iron_sharp", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(ItemStat.BASE_T1)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .damage(3)
          .biteFailChance(ItemStat.MULTIPLIER_T0125)
        );
        HOOK_IRON_SERRATED = registerItem("hook_iron_serrated", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(ItemStat.BASE_T1)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .fishQuality(-1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T6)
          .reelDamage(4)
        );
        HOOK_IRON_AUTO = registerItem("hook_iron_auto",  new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(ItemStat.BASE_T1)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .autoHookChance(ItemStat.MULTIPLIER_T1)
        );
        HOOK_GOLD = registerItem("hook_gold", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
        );
        HOOK_GOLD_POISON = registerItem("hook_gold_poison", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T9)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_GOLD_SHARP = registerItem("hook_gold_sharp", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
          .damage(4)
          .biteFailChance(ItemStat.MULTIPLIER_T015)
        );
        HOOK_GOLD_SERRATED = registerItem("hook_gold_serrated", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
          .reelDamage(5)
        );
        HOOK_GOLD_AUTO = registerItem("hook_gold_auto", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T2)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T2)
          .fishQuality(1)
          .autoHookChance(ItemStat.MULTIPLIER_T2)
        );
        HOOK_PRISMARINE = registerItem("hook_prismarine", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(ItemStat.BASE_T2)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
        );
        HOOK_PRISMARINE_POISON = registerItem("hook_prismarine_poison", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(ItemStat.BASE_T2)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T9)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_PRISMARINE_SHARP = registerItem("hook_prismarine_sharp", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(ItemStat.BASE_T2)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .damage(4)
          .biteFailChance(ItemStat.MULTIPLIER_T015)
        );
        HOOK_PRISMARINE_SERRATED = registerItem("hook_prismarine_serrated", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(ItemStat.BASE_T2)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T5)
          .reelDamage(5)
        );
        HOOK_PRISMARINE_AUTO = registerItem("hook_prismarine_auto", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(ItemStat.BASE_T2)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T2)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .autoHookChance(ItemStat.MULTIPLIER_T2)
        );
        HOOK_NAUTILUS = registerItem("hook_nautilus", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 100, -1, 1)
          .fishRarity(ItemStat.BASE_T5)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T5)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .fishQuality(2)
        );
        HOOK_NAUTILUS_POISON = registerItem("hook_nautilus_poison", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .fishRarity(ItemStat.BASE_T5)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T5)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .fishQuality(2)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_WITHER)
        );
        HOOK_NAUTILUS_SHARP = registerItem("hook_nautilus_sharp", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .fishRarity(ItemStat.BASE_T5)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T5)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .fishQuality(2)
          .damage(6)
          .biteFailChance(ItemStat.MULTIPLIER_T01)
        );
        HOOK_NAUTILUS_SERRATED = registerItem("hook_nautilus_serrated", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .fishRarity(ItemStat.BASE_T5)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T5)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
          .reelDamage(7)
        );
        HOOK_NAUTILUS_AUTO = registerItem("hook_nautilus_auto", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .fishRarity(ItemStat.BASE_T5)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T5)
          .fishControl(ItemStat.BASE_T3)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T3)
          .fishQuality(2)
          .autoHookChance(ItemStat.MULTIPLIER_T3)
        );
        HOOK_NETHERITE = registerItem("hook_netherite", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(RARE), 300, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
        );
        HOOK_NETHERITE_POISON = registerItem("hook_netherite_poison", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_FIRE)
        );
        HOOK_NETHERITE_SHARP = registerItem("hook_netherite_sharp", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
          .damage(9)
          .biteFailChance(ItemStat.MULTIPLIER_T02)
        );
        HOOK_NETHERITE_SERRATED = registerItem("hook_netherite_serrated", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
          .fishQuality(-2)
          .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
          .reelDamage(10)
        );
        HOOK_NETHERITE_AUTO = registerItem("hook_netherite_auto", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .fishRarity(ItemStat.BASE_T3)
          .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
          .fishControl(ItemStat.BASE_T5)
          .fishControlMultiplier(ItemStat.MULTIPLIER_T5)
          .autoHookChance(ItemStat.MULTIPLIER_T3)
        );
    }

    private static void registerBait() {
       BAIT_PLANT = registerItem("bait_plant", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(ItemStat.BASE_T1)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
         .plant()
         .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T3)
       );
       BAIT_ROTTEN_FLESH = registerItem("bait_rotten_flesh", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(ItemStat.BASE_T1)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T3)
         .meat()
         .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T3)
       );
       BAIT_MEAT = registerItem("bait_meat", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(ItemStat.BASE_T2)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T4)
         .meat()
         .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4)
       );
       BAIT_ORCHID = registerItem("bait_orchid", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(ItemStat.BASE_T2)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T4)
         .plant()
         .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T4)
       );
       BAIT_GLOW_BERRIES = registerItem("bait_glow_berries", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(ItemStat.BASE_T3)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T4)
         .plant()
         .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
         .fishQuantityBonus(ItemStat.BASE_T2)
         .fishQuality(1)
       );
       BAIT_BLAZE_POWDER = registerItem("bait_blaze_powder", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, 1, 1)
         .fishQuality(1)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T8)
       );//todo add nether species bonuses
       BAIT_IRON = registerItem("bait_iron", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .treasureBonus(ItemStat.BASE_T3)
         .treasureRarityBonus(ItemStat.BASE_T3)
         .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T0125)
         .fishQuality(-1)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
       );
       BAIT_GHAST = registerItem("bait_ghast", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 1)
         .fishQuality(2)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T10)
       );//todo add nether species bonuses
       BAIT_RABBIT = registerItem("bait_rabbit", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(ItemStat.BASE_T3)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T4)
         .meat()
         .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T10)
         .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
         .fishQuality(1)
       );
       BAIT_CHORUS = registerItem("bait_chorus", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 1)
         .fishRarity(ItemStat.BASE_T3)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T5)
         .plant()
         .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
         .fishQuantityBonus(ItemStat.BASE_T3)
         .fishQuality(2)
       );
       BAIT_BREEZE_BLAZE = registerItem("bait_breeze_blaze", new BaitPartItem(new Item.Settings().maxDamage(32).rarity(COMMON), 300, -1, 1)
         .treasureBonus(ItemStat.BASE_T6)
         .treasureRarityBonus(ItemStat.BASE_T6)
         .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T025)
         .fishQuality(-1)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T1)
       );
       BAIT_TORCHFLOWER = registerItem("bait_torchflower", new BaitPartItem(new Item.Settings().maxDamage(128).rarity(COMMON), 300, -1, 1)
         .fishRarity(ItemStat.BASE_T5)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T10)
         .meat()
         .plant()
         .timeHookedMultiplier(ItemStat.MULTIPLIER_T4)
         .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T5)
         .fishQuantityBonus(ItemStat.BASE_T3)
         .fishQuality(5)
         .treasureBonus(ItemStat.BASE_T2)
         .treasureRarityBonus(ItemStat.BASE_T2)
       );
       BAIT_PITCHER_PLANT = registerItem("bait_pitcher_plant", new BaitPartItem(new Item.Settings().maxDamage(32).rarity(COMMON), 300, -1, 1)
         .fishRarity(ItemStat.BASE_T5)
         .fishRarityMultiplier(ItemStat.MULTIPLIER_T10)
         .meat()
         .plant()
         .timeHookedMultiplier(ItemStat.MULTIPLIER_T10)
         .waitTimeReductionMultiplier(ItemStat.MULTIPLIER_T10)
         .fishQuantityBonus(ItemStat.BASE_T5)
         .fishQuality(5)
         .treasureBonus(ItemStat.BASE_T2)
         .treasureRarityBonus(ItemStat.BASE_T2)
       );
       BAIT_SPECIAL = registerItem("bait_special", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(UNCOMMON), 300)
       );

    }

    public static void registerParts() {
        registerCore();
        registerReel();
        registerBobber();
        registerLine();
        registerHook();
        registerBait();
    }

    private static void registerItemGroup() {
        FISHING_CLUB_GROUP = Registry.register(Registries.ITEM_GROUP, FishingClub.getIdentifier("fishing_club"),
          FabricItemGroup.builder()
            .displayName(Text.literal("Fishing Club"))
            .icon(Items.COD::getDefaultStack)
            .entries((displayContext, entries) -> entries.addAll(FISHING_ITEMS
//              .stream().sorted(
//              Comparator.comparing(o -> o.getName().getString(), String.CASE_INSENSITIVE_ORDER)).toList()
            ))
            .build());
    }

    private static final ArrayList<ItemStack> FISHING_ITEMS = new ArrayList<>();

}

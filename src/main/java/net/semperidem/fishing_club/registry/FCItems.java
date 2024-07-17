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
          .castPowerMultiplier(0.6f)
          .bobberControl(37.5f)
          .fishControl(37.5f));
        CORE_BAMBOO_WOOD = registerItem("core_bamboo_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 15)
          .castPowerMultiplier(0.8f)
          .bobberControl(12.5f)
          .fishControl(12.5f));
        CORE_OAK_WOOD = registerItem("core_oak_wood", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_BIRCH_WOOD = registerItem("core_birch_wood", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_SPRUCE_WOOD = registerItem("core_spruce_wood", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_ACACIA_WOOD = registerItem("core_acacia_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_JUNGLE_WOOD = registerItem("core_jungle_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_CHERRY_WOOD = registerItem("core_cherry_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_DARK_OAK_WOOD = registerItem("core_dark_oak_wood", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25)
          .castPowerMultiplier(0.8f)
          .bobberControl(0)
          .fishControl(0));
        CORE_MANGROVE_WOOD = registerItem("core_mangrove_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f)
          .fishControl(37.5f)
          .bobberControl(37.5f));
        CORE_CRIMSON_WOOD = registerItem("core_crimson_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10, 0, 1)
          .castPowerMultiplier(0.8f));
        CORE_WARPED_WOOD = registerItem("core_warped_wood", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10, 0, 1)
          .castPowerMultiplier(0.8f));
        CORE_BONE = registerItem("core_bone", new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 5, -1, 0)
          .castPowerMultiplier(0.6f)
          .fishControl(12.5f));
        CORE_WITHER_BONE = registerItem("core_wither_bone", new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 15, -1, 1)
          .castPowerMultiplier(0.8f)
          .fishControl(12.5f));
        CORE_IRON = registerItem("core_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(COMMON), 15, -1, 0)
          .castPowerMultiplier(0.8f)
          .fishControl(12.5f));
        CORE_BAMBOO_IRON = registerItem("core_bamboo_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(0.8f)
          .fishControl(12.5f)
        );
        CORE_OAK_IRON = registerItem("core_oak_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 0)
          .castPowerMultiplier(0.8f)
          .fishQuality(1)
        );
        CORE_BIRCH_IRON = registerItem("core_birch_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 0)
          .castPowerMultiplier(0.8f)
          .fishQuality(1)
        );
        CORE_SPRUCE_IRON = registerItem("core_spruce_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 0)
          .castPowerMultiplier(0.8f)
          .fishQuality(1)
        );
        CORE_ACACIA_IRON = registerItem("core_acacia_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(0.8f)
        );
        CORE_JUNGLE_IRON = registerItem("core_jungle_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(0.8f)
        );
        CORE_CHERRY_IRON = registerItem("core_cherry_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(0.8f)
        );
        CORE_DARK_OAK_IRON = registerItem("core_dark_oak_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 50, -1, 0)
          .bobberControl(0)
          .fishControl(0)
        );
        CORE_MANGROVE_IRON = registerItem("core_mangrove_iron", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(0.8f)
          .bobberControl(37.5f)
          .fishControl(37.5f)
        );
        CORE_CRIMSON_IRON = registerItem("core_crimson_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 1)
          .castPowerMultiplier(0.8f)
        );
        CORE_WARPED_IRON = registerItem("core_warped_iron", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 1)
          .castPowerMultiplier(0.8f)
        );
        CORE_COPPER = registerItem("core_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(COMMON), 15)
          .castPowerMultiplier(0.8f));
        CORE_BAMBOO_COPPER = registerItem("core_bamboo_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(0.8f));
        CORE_OAK_COPPER = registerItem("core_oak_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(0.8f)
          .fishQuality(1));
        CORE_BIRCH_COPPER = registerItem("core_birch_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(0.8f)
          .fishQuality(1));
        CORE_SPRUCE_COPPER = registerItem("core_spruce_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(0.8f)
          .fishQuality(1));
        CORE_ACACIA_COPPER = registerItem("core_acacia_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
        );
        CORE_JUNGLE_COPPER = registerItem("core_jungle_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
        );
        CORE_CHERRY_COPPER = registerItem("core_cherry_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
        );
        CORE_DARK_OAK_COPPER = registerItem("core_dark_oak_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 50)
          .bobberControl(12.5f)
          .fishControl(12.5f));
        CORE_MANGROVE_COPPER = registerItem("core_mangrove_copper", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
          .castPowerMultiplier(0.8f)
          .bobberControl(37.5f)
          .fishControl(37.5f));
        CORE_CRIMSON_COPPER = registerItem("core_crimson_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, 0, 1)
          .castPowerMultiplier(0.8f));
        CORE_WARPED_COPPER = registerItem("core_warped_copper", new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, 0, 1)
          .castPowerMultiplier(0.8f));
        CORE_PRISMARINE_BLAZE = registerItem("core_prismarine_blaze", new CorePartItem(new Item.Settings().maxDamage(5120).rarity(RARE), 25, 0, 1)
          .fishQuality(3));
        CORE_NETHERITE = registerItem("core_netherite", new CorePartItem(new Item.Settings().maxDamage(20480).rarity(RARE), 100, -1, 1)
          .castPowerMultiplier(1.2f)
          .fishQuality(1));
        CORE_NAUTILUS_BREEZE = registerItem("core_nautilus_breeze", new CorePartItem(new Item.Settings().maxDamage(10240).rarity(EPIC), 100, -1, 1)
          .castPowerMultiplier(1.4f)
          .bobberControl(37.5f)
          .fishControl(37.5f)
          .fishQuality(2));
        CORE_NAUTILUS_BLAZE = registerItem("core_nautilus_blaze", new CorePartItem(new Item.Settings().maxDamage(10240).rarity(EPIC), 100, -1, 1)
          .castPowerMultiplier(1.2f)
          .bobberControl(37.5f)
          .fishControl(37.5f)
          .fishQuality(4));
        CORE_NAUTILUS_END = registerItem("core_nautilus_end", new CorePartItem(new Item.Settings().maxDamage(20480).rarity(EPIC), 300, -1, 1)
          .castPowerMultiplier(1.2f)
          .fishControl(50)
          .bobberControl(50)
          .fishQuality(1));
    }

    public static void registerReel() {
        REEL_WOOD = registerItem("reel_wood", new ReelPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10)
          .bobberControl(0)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
          .timeHookedMultiplier(0.8f)
        );
        REEL_BONE = registerItem("reel_bone", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 5)
          .bobberControl(12.5f)
          .fishControl(12.5f)
          .fishControlMultiplier(0.6f)
          .timeHookedMultiplier(0.6f)
        );
        REEL_PRISMARINE = registerItem("reel_prismarine", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 15)
          .bobberControl(25)
          .fishControl(25)
          .timeHookedMultiplier(0.8f)
        );
        REEL_IRON = registerItem("reel_iron", new ReelPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 25, -1, 0)
          .bobberControl(12.5f)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .timeHookedMultiplier(0.8f)
        );
        REEL_IRON_PRISMARINE = registerItem("reel_iron_prismarine", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 50, -1, 0)
          .bobberControl(25)
          .fishControl(25)
        );
        REEL_AMETHYST_PRISMARINE = registerItem("reel_amethyst_prismarine", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, -1, 1)
          .bobberControl(12.5f)
          .fishControl(25)
          .timeHookedMultiplier(0.8f)
          .fishQuality(1)
        );
        REEL_NAUTILUS_ECHO = registerItem("reel_nautilus_echo", new ReelPartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 100, -1, 1)
          .bobberControl(25)
          .fishControl(37.5f)
          .fishControlMultiplier(1.2f)
          .timeHookedMultiplier(1.2f)
        );
        REEL_NAUTILUS_AMETHYST = registerItem("reel_nautilus_amethyst", new ReelPartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 100, -1, 1)
          .bobberControl(25)
          .fishControl(25)
          .fishQuality(1)
        );
        REEL_HEART_AMETHYST = registerItem("reel_heart_amethyst", new ReelPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .bobberControl(50)
          .fishControl(25)
          .timeHookedMultiplier(1.4f)
          .fishQuality(3)
        );
        REEL_HEART_ECHO = registerItem("reel_heart_echo", new ReelPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .bobberControl(25)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
          .timeHookedMultiplier(1.4f)
          .fishQuality(1)
        );
    }

    public static void registerBobber() {
        BOBBER_PLANT_SLIME = registerItem("bobber_plant_slime", new BobberPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 5)
          .timeHookedMultiplier(1.2f)
          .timeUntilHookedMultiplier(0.8f)
          .fishControlMultiplier(0.6f)
          .bobberWidth(0.6f)
        );
        BOBBER_LEATHER_SLIME = registerItem("bobber_leather_slime", new BobberPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 15)
          .timeHookedMultiplier(1.2f)
          .timeUntilHookedMultiplier(0.8f)
          .fishControlMultiplier(0.6f)
          .bobberWidth(0.6f)
        );
        BOBBER_TURTLE_SLIME = registerItem("bobber_turtle_slime", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25)
          .fishControlMultiplier(0.6f)
          .bobberControl(12.5f)
        );
        BOBBER_ARMADILLO_SLIME = registerItem("bobber_armadillo_slime", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25)
          .bobberWidth(0.8f)
          .fishControl(12.5f)
        );
        BOBBER_TURTLE_ENDER = registerItem("bobber_turtle_ender", new BobberPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 25, -1, 0)
          .fishControlMultiplier(0.8f)
          .bobberWidth(1.2f)
          .bobberControl(25)
        );
        BOBBER_ARMADILLO_MAGMA = registerItem("bobber_armadillo_magma", new BobberPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 25, 0, 1)
          .timeUntilHookedMultiplier(0.6f)
          .bobberWidth(0.8f)
          .fishControl(25)
        );
        BOBBER_AMETHYST_ENDER = registerItem("bobber_amethyst_ender", new BobberPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .timeHookedMultiplier(1.2f)
          .bobberControl(12.5f)
          .fishControl(12.5f)
          .fishQuality(2)
        );
        BOBBER_NAUTILUS = registerItem("bobber_nautilus", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(RARE), 100, -1, 0)
          .timeHookedMultiplier(1.4f)
          .bobberControl(25)
          .fishControl(25)
        );
        BOBBER_TURTLE_ECHO = registerItem("bobber_turtle_echo", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(RARE), 75, -1, 1)
          .fishControlMultiplier(0.8f)
          .bobberWidth(1.2f)
          .bobberControl(37.5f)
          .fishControl(12.5f)
          .fishQuality(1)
        );
        BOBBER_ARMADILLO_ECHO = registerItem("bobber_armadillo_echo", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(RARE), 75, -1, 1)
          .fishControlMultiplier(1.2f)
          .bobberWidth(0.8f)
          .bobberControl(12.5f)
          .fishControl(37.5f)
          .fishQuality(1)
        );
        BOBBER_AMETHYST_ECHO = registerItem("bobber_amethyst_echo", new BobberPartItem(new Item.Settings().maxDamage(640).rarity(RARE), 75, -1, 1)
          .bobberControl(25)
          .fishControl(25)
          .fishQuality(1));
        BOBBER_NAUTILUS_ECHO = registerItem("bobber_nautilus_echo", new BobberPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .timeHookedMultiplier(1.2f)
          .timeUntilHookedMultiplier(0.6f)
          .fishControlMultiplier(1.4f)
          .bobberWidth(1.4f)
          .bobberControl(50)
          .fishControl(50)
          .fishQuality(2));
        BOBBER_HEART  = registerItem("bobber_heart", new BobberPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .timeHookedMultiplier(1.4f)
          .timeUntilHookedMultiplier(0.6f)
          .fishControlMultiplier(1.2f)
          .bobberWidth(1.2f)
          .bobberControl(25)
          .fishControl(25)
          .fishQuality(4));
    }

    public static void registerLine() {

        LINE_PLANT = registerItem("line_plant", new LinePartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 5)
          .maxLineLength(4)
          .fishControlMultiplier(0.6f)
        );
        LINE_WOOL = registerItem("line_wool", new LinePartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10)
          .maxLineLength(8)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
        );
        LINE_SPIDER = registerItem("line_spider", new LinePartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15)
          .maxLineLength(16)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
        );
        LINE_STRIDER = registerItem("line_strider", new LinePartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 1, 1)
          .maxLineLength(16)
          .fishControl(25)
        );
        LINE_PHANTOM = registerItem("line_phantom", new LinePartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, -1, 0)
          .maxLineLength(32)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .fishQuality(1)
        );
        LINE_EVOKER = registerItem("line_evoker", new LinePartItem(new Item.Settings().maxDamage(1280).rarity(RARE), 50)
          .maxLineLength(64)
          .fishControl(25)
          .fishQuality(1)
        );
        LINE_PIGLIN = registerItem("line_piglin", new LinePartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 100, 0, 1)
          .maxLineLength(64)
          .fishControl(25)
        );
        LINE_HEART = registerItem("line_heart", new LinePartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 50, -1, 1)
          .maxLineLength(128)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
        );
        LINE_HEART_PIGLIN = registerItem("line_heart_piglin", new LinePartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .maxLineLength(128)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
          .fishQuality(1)
        );
        LINE_HEART_EVOKER = registerItem("line_heart_evoker", new LinePartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .maxLineLength(256)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
          .fishQuality(3)
        );
    }

    private static void registerHook() {
        HOOK_FLINT = registerItem("hook_flint", new HookPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10, -1, 0)
          .fishRarity(0)
          .fishRarityMultiplier(0.6f)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
        );
        HOOK_FLINT_POISON = registerItem("hook_flint_poison", new HookPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10, -1, 0)
          .fishRarity(0)
          .fishRarityMultiplier(0.6f)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .timeHookedMultiplier(1.25f)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_FLINT_SHARP = registerItem("hook_flint_sharp", new HookPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10, -1, 0)
          .fishRarity(0)
          .fishRarityMultiplier(0.6f)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .damage(2)
          .biteFailChance(0.1f)
        );
        HOOK_FLINT_SERRATED = registerItem("hook_flint_serrated", new HookPartItem(new Item.Settings().maxDamage(320).rarity(COMMON), 10, -1, 0)
          .fishRarity(0)
          .fishRarityMultiplier(0.6f)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .fishQuality(-1)
          .timeHookedMultiplier(1.2f)
          .reelDamage(3)
        );
        HOOK_BONE = registerItem("hook_bone", new HookPartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15, -1, 0)
          .fishRarity(12.5f)
          .fishRarityMultiplier(0.8f)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
        );
        HOOK_BONE_POISON = registerItem("hook_bone_poison", new HookPartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15, -1, 0)
          .fishRarity(12.5f)
          .fishRarityMultiplier(0.8f)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
          .timeHookedMultiplier(1.25f)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_BONE_SHARP = registerItem("hook_bone_sharp", new HookPartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15, -1, 0)
          .fishRarity(12.5f)
          .fishRarityMultiplier(0.8f)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
          .damage(2)
          .biteFailChance(0.1f)
        );
        HOOK_BONE_SERRATED = registerItem("hook_bone_serrated", new HookPartItem(new Item.Settings().maxDamage(160).rarity(COMMON), 15, -1, 0)
          .fishRarity(12.5f)
          .fishRarityMultiplier(0.8f)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
          .fishQuality(-1)
          .timeHookedMultiplier(1.2f)
          .reelDamage(3)
        );
        HOOK_COPPER = registerItem("hook_copper", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
        );
        HOOK_COPPER_POISON = registerItem("hook_copper_poison", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
          .timeHookedMultiplier(1.5f)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_COPPER_SHARP = registerItem("hook_copper_sharp", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
          .damage(3)
          .biteFailChance(0.125f)
        );
        HOOK_COPPER_SERRATED = registerItem("hook_copper_serrated", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
          .fishQuality(-1)
          .timeHookedMultiplier(1.3f)
          .reelDamage(4)
        );
        HOOK_COPPER_AUTO = registerItem("hook_copper_auto", new HookPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(0)
          .fishControlMultiplier(0.6f)
          .autoHookChance(0.6f)
        );
        HOOK_IRON = registerItem("hook_iron", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(0)
          .fishRarityMultiplier(0.6f)
          .fishControl(25)
          .fishControlMultiplier(1)
        );
        HOOK_IRON_POISON = registerItem("hook_iron_poison", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(0)
          .fishRarityMultiplier(0.6f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .timeHookedMultiplier(1.5f)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_IRON_SHARP = registerItem("hook_iron_sharp", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(0)
          .fishRarityMultiplier(0.6f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .damage(3)
          .biteFailChance(0.125f)
        );
        HOOK_IRON_SERRATED = registerItem("hook_iron_serrated", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(0)
          .fishRarityMultiplier(0.6f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .fishQuality(-1)
          .timeHookedMultiplier(1.3f)
          .reelDamage(4)
        );
        HOOK_IRON_AUTO = registerItem("hook_iron_auto",  new HookPartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 50, -1, 1)
          .fishRarity(0)
          .fishRarityMultiplier(0.6f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .autoHookChance(0.6f)
        );
        HOOK_GOLD = registerItem("hook_gold", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .fishQuality(1)
        );
        HOOK_GOLD_POISON = registerItem("hook_gold_poison", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .fishQuality(1)
          .timeHookedMultiplier(1.75f)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_GOLD_SHARP = registerItem("hook_gold_sharp", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .fishQuality(1)
          .damage(4)
          .biteFailChance(0.15f)
        );
        HOOK_GOLD_SERRATED = registerItem("hook_gold_serrated", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .timeHookedMultiplier(1.4f)
          .reelDamage(5)
        );
        HOOK_GOLD_AUTO = registerItem("hook_gold_auto", new HookPartItem(new Item.Settings().maxDamage(640).rarity(UNCOMMON), 25, 0, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(12.5f)
          .fishControlMultiplier(0.8f)
          .fishQuality(1)
          .autoHookChance(0.8f)
        );
        HOOK_PRISMARINE = registerItem("hook_prismarine", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(12.5f)
          .fishRarityMultiplier(0.8f)
          .fishControl(25)
          .fishControlMultiplier(1)
        );
        HOOK_PRISMARINE_POISON = registerItem("hook_prismarine_poison", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(12.5f)
          .fishRarityMultiplier(0.8f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .timeHookedMultiplier(1.75f)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
        );
        HOOK_PRISMARINE_SHARP = registerItem("hook_prismarine_sharp", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(12.5f)
          .fishRarityMultiplier(0.8f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .damage(4)
          .biteFailChance(0.15f)
        );
        HOOK_PRISMARINE_SERRATED = registerItem("hook_prismarine_serrated", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(12.5f)
          .fishRarityMultiplier(0.8f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .timeHookedMultiplier(1.4f)
          .reelDamage(5)
        );
        HOOK_PRISMARINE_AUTO = registerItem("hook_prismarine_auto", new HookPartItem(new Item.Settings().maxDamage(1280).rarity(UNCOMMON), 50, -1, 0)
          .fishRarity(12.5f)
          .fishRarityMultiplier(0.8f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .autoHookChance(0.8f)
        );
        HOOK_NAUTILUS = registerItem("hook_nautilus", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(RARE), 100, -1, 1)
          .fishRarity(50)
          .fishRarityMultiplier(1.4f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .fishQuality(2)
        );
        HOOK_NAUTILUS_POISON = registerItem("hook_nautilus_poison", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .fishRarity(50)
          .fishRarityMultiplier(1.4f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .fishQuality(2)
          .timeHookedMultiplier(2)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_WITHER)
        );
        HOOK_NAUTILUS_SHARP = registerItem("hook_nautilus_sharp", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .fishRarity(50)
          .fishRarityMultiplier(1.4f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .fishQuality(2)
          .damage(6)
          .biteFailChance(0.1f)
        );
        HOOK_NAUTILUS_SERRATED = registerItem("hook_nautilus_serrated", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .fishRarity(50)
          .fishRarityMultiplier(1.4f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .timeHookedMultiplier(2f)
          .reelDamage(7)
        );
        HOOK_NAUTILUS_AUTO = registerItem("hook_nautilus_auto", new HookPartItem(new Item.Settings().maxDamage(2560).rarity(EPIC), 100, -1, 1)
          .fishRarity(50)
          .fishRarityMultiplier(1.4f)
          .fishControl(25)
          .fishControlMultiplier(1)
          .fishQuality(2)
          .autoHookChance(1)
        );
        HOOK_NETHERITE = registerItem("hook_netherite", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(RARE), 300, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
        );
        HOOK_NETHERITE_POISON = registerItem("hook_netherite_poison", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
          .timeHookedMultiplier(2)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_POISON)
          .addOnEntityHitEffects(HookPartItem.ON_HIT_FIRE)
        );
        HOOK_NETHERITE_SHARP = registerItem("hook_netherite_sharp", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
          .damage(9)
          .biteFailChance(0.2f)
        );
        HOOK_NETHERITE_SERRATED = registerItem("hook_netherite_serrated", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
          .fishQuality(-2)
          .timeHookedMultiplier(2f)
          .reelDamage(10)
        );
        HOOK_NETHERITE_AUTO = registerItem("hook_netherite_auto", new HookPartItem(new Item.Settings().maxDamage(5120).rarity(EPIC), 300, -1, 1)
          .fishRarity(25)
          .fishRarityMultiplier(1)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
          .autoHookChance(1)
        );
    }

    private static void registerBait() {
       BAIT_PLANT = registerItem("bait_plant", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(0)
         .fishRarityMultiplier(1)
         .plant()
         .timeUntilHookedMultiplier(1)
       );
       BAIT_ROTTEN_FLESH = registerItem("bait_rotten_flesh", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(0)
         .fishRarityMultiplier(1)
         .meat()
         .timeUntilHookedMultiplier(1)
       );
       BAIT_MEAT = registerItem("bait_meat", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(12.5f)
         .fishRarityMultiplier(1.2f)
         .meat()
         .timeUntilHookedMultiplier(0.8f)
       );
       BAIT_ORCHID = registerItem("bait_orchid", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(12.5f)
         .fishRarityMultiplier(1.2f)
         .plant()
         .timeUntilHookedMultiplier(0.8f)
       );
       BAIT_GLOW_BERRIES = registerItem("bait_glow_berries", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(25)
         .fishRarityMultiplier(1.2f)
         .plant()
         .timeHookedMultiplier(1.2f)
         .fishQuantityBonus(12.5f)
         .fishQuality(1)
       );
       BAIT_BLAZE_POWDER = registerItem("bait_blaze_powder", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, 1, 1)
         .fishQuality(1)
         .fishRarityMultiplier(1.5f)
       );//todo add nether species bonuses
       BAIT_IRON = registerItem("bait_iron", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .treasureBonus(25)
         .treasureRarityBonus(25)
         .timeUntilHookedMultiplier(8f)
         .fishQuality(-1)
         .fishRarityMultiplier(0.6f)
       );
       BAIT_GHAST = registerItem("bait_ghast", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 1)
         .fishQuality(2)
         .fishRarityMultiplier(2f)
       );//todo add nether species bonuses
       BAIT_RABBIT = registerItem("bait_rabbit", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 0)
         .fishRarity(25)
         .fishRarityMultiplier(1.2f)
         .meat()
         .timeUntilHookedMultiplier(0.5f)
         .timeHookedMultiplier(2)
         .fishQuality(1)
       );
       BAIT_CHORUS = registerItem("bait_chorus", new BaitPartItem(new Item.Settings().maxDamage(64).rarity(COMMON), 300, -1, 1)
         .fishRarity(25)
         .fishRarityMultiplier(1.4f)
         .plant()
         .timeHookedMultiplier(2)
         .fishQuantityBonus(25)
         .fishQuality(2)
       );
       BAIT_BREEZE_BLAZE = registerItem("bait_breeze_blaze", new BaitPartItem(new Item.Settings().maxDamage(32).rarity(COMMON), 300, -1, 1)
         .treasureBonus(100)
         .treasureRarityBonus(100)
         .timeUntilHookedMultiplier(4f)
         .fishQuality(-1)
         .fishRarityMultiplier(0.6f)
       );
       BAIT_TORCHFLOWER = registerItem("bait_torchflower", new BaitPartItem(new Item.Settings().maxDamage(128).rarity(COMMON), 300, -1, 1)
         .fishRarity(50)
         .fishRarityMultiplier(2)
         .meat()
         .plant()
         .timeHookedMultiplier(1.2f)
         .timeUntilHookedMultiplier(0.8f)
         .fishQuantityBonus(25)
         .fishQuality(5)
         .treasureBonus(12.5f)
         .treasureRarityBonus(12.5f)
       );
       BAIT_PITCHER_PLANT = registerItem("bait_pitcher_plant", new BaitPartItem(new Item.Settings().maxDamage(32).rarity(COMMON), 300, -1, 1)
         .fishRarity(50)
         .fishRarityMultiplier(2)
         .meat()
         .plant()
         .timeHookedMultiplier(2f)
         .timeUntilHookedMultiplier(0.5f)
         .fishQuantityBonus(50)
         .fishQuality(5)
         .treasureBonus(12.5f)
         .treasureRarityBonus(12.5f)
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

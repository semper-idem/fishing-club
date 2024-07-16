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
    public static LinePartItem HOOK_FLINT;
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
    public static HookPartItem HOOK_COPPER_DOUBLE;
    public static HookPartItem HOOK_IRON;
    public static HookPartItem HOOK_IRON_POISON;
    public static HookPartItem HOOK_IRON_SHARP;
    public static HookPartItem HOOK_IRON_SERRATED;
    public static HookPartItem HOOK_IRON_DOUBLE;
    public static HookPartItem HOOK_GOLD;
    public static HookPartItem HOOK_GOLD_POISON;
    public static HookPartItem HOOK_GOLD_SHARP;
    public static HookPartItem HOOK_GOLD_SERRATED;
    public static HookPartItem HOOK_GOLD_DOUBLE;
    public static HookPartItem HOOK_PRISMARINE;
    public static HookPartItem HOOK_PRISMARINE_POISON;
    public static HookPartItem HOOK_PRISMARINE_SHARP;
    public static HookPartItem HOOK_PRISMARINE_SERRATED;
    public static HookPartItem HOOK_PRISMARINE_DOUBLE;
    public static HookPartItem HOOK_NAUTILUS;
    public static HookPartItem HOOK_NAUTILUS_POISON;
    public static HookPartItem HOOK_NAUTILUS_SHARP;
    public static HookPartItem HOOK_NAUTILUS_SERRATED;
    public static HookPartItem HOOK_NAUTILUS_DOUBLE;
    public static HookPartItem HOOK_NAUTILUS_TRIPLE;
    public static HookPartItem HOOK_NETHERITE;
    public static HookPartItem HOOK_NETHERITE_POISON;
    public static HookPartItem HOOK_NETHERITE_SHARP;
    public static HookPartItem HOOK_NETHERITE_SERRATED;
    public static HookPartItem HOOK_NETHERITE_DOUBLE;
    public static HookPartItem HOOK_NETHERITE_TRIPLE;
    public static BaitPartItem BAIT_PLANT;
    public static BaitPartItem BAIT_ROTTEN_FLESH;
    public static BaitPartItem BAIT_MEAT;
    public static BaitPartItem BAIT_ORCHID;
    public static BaitPartItem BAIT_GLOW_BERRIES;
    public static BaitPartItem BAIT_BLADE_POWDER;
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
        REEL_IRON_PRISMARINE = registerItem("reel_iron_prismarine", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 50, -1, 0)
          .bobberControl(25)
          .fishControl(25)
        );
        REEL_AMETHYST_PRISMARINE = registerItem("reel_amethyst_prismarine", new ReelPartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25, -1, 1)
          .bobberControl(12.5f)
          .fishControl(25)
          .timeHookedMultiplier(0.8f)
          .fishQuality(1)
        );
        REEL_NAUTILUS_ECHO = registerItem("reel_nautilus_echo", new ReelPartItem(new Item.Settings().maxDamage(2560).rarity(COMMON), 100, -1, 1)
          .bobberControl(25)
          .fishControl(37.5f)
          .fishControlMultiplier(1.2f)
          .timeHookedMultiplier(1.2f)
        );
        REEL_NAUTILUS_AMETHYST = registerItem("reel_nautilus_amethyst", new ReelPartItem(new Item.Settings().maxDamage(2560).rarity(COMMON), 100, -1, 1)
          .bobberControl(25)
          .fishControl(25)
          .fishQuality(1)
        );
        REEL_HEART_AMETHYST = registerItem("reel_heart_amethyst", new ReelPartItem(new Item.Settings().maxDamage(5120).rarity(COMMON), 300, -1, 1)
          .bobberControl(50)
          .fishControl(25)
          .timeHookedMultiplier(1.4f)
          .fishQuality(3)
        );
        REEL_HEART_ECHO = registerItem("reel_heart_echo", new ReelPartItem(new Item.Settings().maxDamage(5120).rarity(COMMON), 300, -1, 1)
          .bobberControl(25)
          .fishControl(50)
          .fishControlMultiplier(1.4f)
          .timeHookedMultiplier(1.4f)
          .fishQuality(1)
        );
    }

    public static void registerParts() {
        registerCore();
        registerReel();
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

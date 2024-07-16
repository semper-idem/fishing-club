package net.semperidem.fishing_club.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.item.*;
import net.semperidem.fishing_club.item.fishing_rod.MemberFishingRodItem;
import net.semperidem.fishing_club.item.fishing_rod.components.*;

import java.util.Comparator;
import java.util.HashSet;

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
    public static PartItem EMPTY_COMPONENT;

    public static CorePartItem CORE_WOODEN_OAK;
;
    public static CorePartItem CORE_BAMBOO_SHOOT;
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

    public static LinePartItem LINE_SPIDER;
    public static LinePartItem LINE_WOOL;

    public static BobberPartItem BOBBER_PLANT;

    public static ReelPartItem REEL_WOODEN;

    public static BaitPartItem BAIT_WORM;

    public static HookPartItem HOOK_IRON;
    public static HookPartItem HOOK_GOLD;

    public static <T extends Item> T registerItem(String id, T item) {
        Registry.register(Registries.ITEM, FishingClub.getIdentifier(id), item);
        FISHING_ITEMS.add(item.getDefaultStack());
        return item;
    }

    public static void register() {
        registerParts();
        FISHING_NET = registerItem(("fishing_net"), new FishingNetItem(new Item.Settings().maxCount(1).maxDamage(64)));
        DOUBLE_FISHING_NET = registerItem(("double_fishing_net"), new DoubleFishingNetItem(new Item.Settings().maxCount(1).maxDamage(64)));
        EMPTY_COMPONENT = registerItem("empty_component", new PartItem(new Item.Settings()));
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
        CORE_WOODEN_OAK = registerItem("core_wooden_oak", new CorePartItem(new Item.Settings()
          .maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(0.9f));
        CORE_BAMBOO_SHOOT = registerItem("core_bamboo_shoot",  new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 5)
          .castPowerMultiplier(0.6f)
          .bobberControl(37.5f)
          .fishControl(37.5f));
        CORE_BAMBOO_WOOD = registerItem("core_bamboo_wood",  new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 15)
          .castPowerMultiplier(0.8f)
          .bobberControl(12.5f)
          .fishControl(12.5f));
        CORE_OAK_WOOD = registerItem("core_oak_wood",  new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_BIRCH_WOOD = registerItem("core_birch_wood",  new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_SPRUCE_WOOD = registerItem("core_spruce_wood",  new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_ACACIA_WOOD = registerItem("core_acacia_wood",  new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_JUNGLE_WOOD = registerItem("core_jungle_wood",  new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_CHERRY_WOOD = registerItem("core_cherry_wood",  new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f));
        CORE_DARK_OAK_WOOD = registerItem("core_dark_oak_wood",  new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 25)
          .castPowerMultiplier(0.8f)
          .bobberControl(0)
          .fishControl(0));
        CORE_MANGROVE_WOOD = registerItem("core_mangrove_wood",  new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10)
          .castPowerMultiplier(0.8f)
          .fishControl(37.5f)
          .bobberControl(37.5f));
        CORE_CRIMSON_WOOD = registerItem("core_crimson_wood",  new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10, 0 ,1)
          .castPowerMultiplier(0.8f));
        CORE_WARPED_WOOD = registerItem("core_warped_wood",  new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 10, 0, 1)
          .castPowerMultiplier(0.8f));
        CORE_BONE = registerItem("core_bone",  new CorePartItem(new Item.Settings().maxDamage(640).rarity(COMMON), 5, -1, 0)
          .castPowerMultiplier(0.6f)
          .fishControl(12.5f));
        CORE_WITHER_BONE = registerItem("core_wither_bone",  new CorePartItem(new Item.Settings().maxDamage(1280).rarity(COMMON), 15, -1, 1)
          .castPowerMultiplier(0.8f)
          .fishControl(12.5f));
        CORE_IRON = registerItem("core_iron",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(COMMON), 15, -1, 0)
          .castPowerMultiplier(0.8f)
          .fishControl(12.5f));
        CORE_BAMBOO_IRON = registerItem("core_bamboo_iron",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
            .castPowerMultiplier(0.8f)
          .fishControl(12.5f)
        );
        CORE_OAK_IRON = registerItem("core_oak_iron",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 0)
          .castPowerMultiplier(0.8f)
          .fishQuality(1)
        );
        CORE_BIRCH_IRON = registerItem("core_birch_iron",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 0)
          .castPowerMultiplier(0.8f)
          .fishQuality(1)
        );
        CORE_SPRUCE_IRON = registerItem("core_spruce_iron",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 0)
          .castPowerMultiplier(0.8f)
          .fishQuality(1)
        );
        CORE_ACACIA_IRON = registerItem("core_acacia_iron",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(0.8f)
        );
        CORE_JUNGLE_IRON = registerItem("core_jungle_iron",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(0.8f)
        );
        CORE_CHERRY_IRON = registerItem("core_cherry_iron",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(0.8f)
        );
        CORE_DARK_OAK_IRON = registerItem("core_dark_oak_iron",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 50, -1, 0)
          .bobberControl(0)
          .fishControl(0)
        );
        CORE_MANGROVE_IRON = registerItem("core_mangrove_iron",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25, -1, 0)
          .castPowerMultiplier(0.8f)
          .bobberControl(37.5f)
          .fishControl(37.5f)
        );
        CORE_CRIMSON_IRON = registerItem("core_crimson_iron",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 1)
          .castPowerMultiplier(0.8f)
        );
        CORE_WARPED_IRON = registerItem("core_warped_iron",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, -1, 1)
          .castPowerMultiplier(0.8f)
        );
        CORE_COPPER = registerItem("core_copper",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(COMMON), 15)
          .castPowerMultiplier(0.8f));
        CORE_BAMBOO_COPPER = registerItem("core_bamboo_copper",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(0.8f));
        CORE_OAK_COPPER = registerItem("core_oak_copper",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(0.8f)
          .fishQuality(1));
        CORE_BIRCH_COPPER = registerItem("core_birch_copper",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(0.8f)
          .fishQuality(1));
        CORE_SPRUCE_COPPER = registerItem("core_spruce_copper",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15)
          .castPowerMultiplier(0.8f)
          .fishQuality(1));
        CORE_ACACIA_COPPER = registerItem("core_acacia_copper",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
          );
        CORE_JUNGLE_COPPER = registerItem("core_jungle_copper",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
          );
        CORE_CHERRY_COPPER = registerItem("core_cherry_copper",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
          );
        CORE_DARK_OAK_COPPER = registerItem("core_dark_oak_copper",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 50)
          .bobberControl(12.5f)
          .fishControl(12.5f));
        CORE_MANGROVE_COPPER = registerItem("core_mangrove_copper",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(UNCOMMON), 25)
          .castPowerMultiplier(0.8f)
          .bobberControl(37.5f)
          .fishControl(37.5f));
        CORE_CRIMSON_COPPER = registerItem("core_crimson_copper",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, 0 ,1)
          .castPowerMultiplier(0.8f));
        CORE_WARPED_COPPER = registerItem("core_warped_copper",  new CorePartItem(new Item.Settings().maxDamage(2560).rarity(UNCOMMON), 15, 0, 1)
          .castPowerMultiplier(0.8f));
        CORE_PRISMARINE_BLAZE = registerItem("core_prismarine_blaze",  new CorePartItem(new Item.Settings().maxDamage(5120).rarity(RARE), 25, 0, 1)
          .fishQuality(3));
        CORE_NETHERITE = registerItem("core_netherite",  new CorePartItem(new Item.Settings().maxDamage(20480).rarity(RARE), 75, -1, 1)
          .castPowerMultiplier(1.2f)
          .fishQuality(1));
        CORE_NAUTILUS_BREEZE = registerItem("core_nautilus_breeze",  new CorePartItem(new Item.Settings().maxDamage(10240).rarity(EPIC), 75, -1, 1)
          .castPowerMultiplier(1.4f)
          .bobberControl(37.5f)
          .fishControl(37.5f)
          .fishQuality(2));
        CORE_NAUTILUS_BLAZE = registerItem("core_nautilus_blaze",  new CorePartItem(new Item.Settings().maxDamage(10240).rarity(EPIC), 75, -1, 1)
          .castPowerMultiplier(1.2f)
          .bobberControl(37.5f)
          .fishControl(37.5f)
          .fishQuality(4));
        CORE_NAUTILUS_END = registerItem("core_nautilus_end",  new CorePartItem(new Item.Settings().maxDamage(20480).rarity(EPIC), 150, -1, 1)
          .castPowerMultiplier(1.2f)
          .fishControl(50)
          .bobberControl(50)
          .fishQuality(1));
    }

    public static void registerParts() {
        registerCore();

        LINE_SPIDER = registerItem("line_spider", new LinePartItem(new Item.Settings()
          .maxDamage(320).rarity(COMMON), 15)
          .maxLineLength(8));
        LINE_WOOL = registerItem("line_wool", new LinePartItem(new Item.Settings()
          .maxDamage(320).rarity(COMMON), 10)
          .maxLineLength(64));

        BOBBER_PLANT = registerItem("bobber_plant", new BobberPartItem(new Item.Settings()
          .maxDamage(320).rarity(COMMON))
          .bobberWidth(1.2f));

        REEL_WOODEN = registerItem("reel_wooden", new ReelPartItem(new Item.Settings()
          .maxDamage(320).rarity(COMMON))
          );

        BAIT_WORM = registerItem("bait_worm", new BaitPartItem(new Item.Settings()
          .maxDamage(320).rarity(COMMON),
            50, 0, 1)//todo change later this is incorrect
          );

        HOOK_IRON = registerItem("hook_iron", new HookPartItem(new Item.Settings()
          .maxDamage(320).rarity(COMMON),
          50, -1, 0)
          .reelDamage(4)
          .sticky(true)
        );
        HOOK_GOLD = registerItem("hook_gold", new HookPartItem(new Item.Settings()
          .maxDamage(320).rarity(COMMON),
          50)
          .autoHookChance(1f)
          .sharp(true)
          .setOnEntityHit(HookPartItem.ON_HIT_POISON)
          .damage(5)
        );
    }

    private static void registerItemGroup() {
        FISHING_CLUB_GROUP = Registry.register(Registries.ITEM_GROUP, FishingClub.getIdentifier("fishing_club"),
          FabricItemGroup.builder()
            .displayName(Text.literal("Fishing Club"))
            .icon(Items.COD::getDefaultStack)
            .entries((displayContext, entries) -> entries.addAll(FISHING_ITEMS.stream().sorted(
              Comparator.comparing(o -> o.getName().getString(), String.CASE_INSENSITIVE_ORDER)).toList()))
            .build());
    }

    private static final HashSet<ItemStack> FISHING_ITEMS = new HashSet<>();

}

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

import static net.minecraft.util.Rarity.COMMON;

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
    public static CorePartItem CORE_IRON;

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
        ILLEGAL_GOODS = registerItem(("illegal_goods"), new IllegalGoodsItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1)));
        GOLD_FISH = registerItem(("gold_fish"), new Item(new Item.Settings().maxCount(1)));
        DEBUG = registerItem("debug", new DebugItem(new Item.Settings()));
        registerItemGroup();
    }

    public static void registerParts() {

        CORE_WOODEN_OAK = registerItem("core_wooden_oak", new CorePartItem(new Item.Settings()
          .maxDamage(640).rarity(COMMON), 10)
          .castPowerMultiplier(0.9f));
        CORE_IRON = registerItem("core_iron", new CorePartItem(new Item.Settings()
          .maxDamage(1280).rarity(COMMON), 15, -1, 0)
          .castPowerMultiplier(0.9f));

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
        );
        HOOK_GOLD = registerItem("hook_gold", new HookPartItem(new Item.Settings()
          .maxDamage(320).rarity(COMMON),
          50)
          .autoHookChance(1f)
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

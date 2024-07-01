package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.item.*;
import net.semperidem.fishingclub.item.fishing_rod.RodPartItems;
import net.semperidem.fishingclub.item.fishing_rod.MemberFishingRodItem;
import net.semperidem.fishingclub.item.fishing_rod.components.PartItem;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent;

import java.util.HashSet;

public class ItemRegistry {
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

  private static final HashSet<ItemStack> FISHING_ITEMS = new HashSet<>();

  public static void register() {
    FISHING_NET =registerItem(("fishing_net"), new FishingNetItem(new Item.Settings().maxCount(1).maxDamage(64)));
    DOUBLE_FISHING_NET = registerItem(("double_fishing_net"), new DoubleFishingNetItem(new Item.Settings().maxCount(1).maxDamage(64)));
    EMPTY_COMPONENT = registerItem("empty_component", new PartItem(new Item.Settings()));
    MEMBER_FISHING_ROD = registerItem(("member_fishing_rod"), new MemberFishingRodItem(new Item.Settings().maxCount(1).maxDamage(100).component(ComponentRegistry.ROD_CONFIGURATION, RodConfigurationComponent.DEFAULT)));
    FISH_COIN_BUNDLE = registerItem(("fish_coin_bundle"), new FishCoinBundleItem(new Item.Settings().maxCount(1)));
    FISHER_HAT = registerItem(("fisher_hat"), new ArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.HELMET, new Item.Settings()));
    FISHER_VEST = registerItem(("fisher_vest"), new ArmorItem(ArmorMaterials.LEATHER, ArmorItem.Type.CHESTPLATE, new Item.Settings()));
    HARPOON_ROD= registerItem(("harpoon_rod"), new HarpoonRodItem(new Item.Settings().maxCount(1).maxDamage(64)));
    LINE_ARROW = registerItem(("line_arrow"), new LineArrowItem(new Item.Settings()));
    CLONED_ROD = registerItem(("cloned_rod"), new ClonedFishingRod(new Item.Settings().maxCount(1).maxDamage(128)));
    ILLEGAL_GOODS = registerItem(("illegal_goods"), new IllegalGoodsItem(new Item.Settings().rarity(Rarity.RARE).maxCount(1)));
    GOLD_FISH = registerItem(("gold_fish"), new Item(new Item.Settings().maxCount(1)));
    DEBUG =registerItem("debug", new DebugItem(new Item.Settings()));

    RodPartItems.registerParts();

    FISHING_CLUB_GROUP =
        FabricItemGroup.builder()
                .displayName(Text.literal("Fishing Club"))
            .icon(Items.COD::getDefaultStack)
            .entries((displayContext, entries) -> entries.addAll(FISHING_ITEMS))
            .build();
  }

  public  static <T extends Item> T registerItem(String id, T item) {
    Registry.register(Registries.ITEM, FishingClub.getIdentifier(id), item);
    FISHING_ITEMS.add(item.getDefaultStack());
    return item;
  }
}

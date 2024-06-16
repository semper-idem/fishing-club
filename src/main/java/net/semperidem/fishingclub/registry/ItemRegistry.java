package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.item.*;
import net.semperidem.fishingclub.item.armor.FisherMaterial;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItems;
import net.semperidem.fishingclub.item.fishing_rod.MemberFishingRodItem;

public class ItemRegistry {

    public static final ItemGroup FISHING_CLUB_GROUP = FabricItemGroupBuilder.build( FishingClub.getIdentifier("fishing_club_group"), () -> new ItemStack(Items.COD));

    public static final DoubleFishingNetItem DOUBLE_FISHING_NET = new DoubleFishingNetItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(64));
    public static final MemberFishingRodItem MEMBER_FISHING_ROD = new MemberFishingRodItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1));
    public static final Item FISHER_WORKBENCH = new BlockItem(BlockRegistry.FISHER_WORKBENCH_BLOCK, new Item.Settings().group(FISHING_CLUB_GROUP));
    public static final FishingNetItem FISHING_NET = new FishingNetItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(64));
    public static final Item FISH_COIN_BUNDLE = new FishCoinBundleItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1));
    public static final ArmorMaterial FISHER_MATERIAL = new FisherMaterial();
    public static final Item FISHER_HAT = new ArmorItem(FISHER_MATERIAL, EquipmentSlot.HEAD, new Item.Settings().group(FISHING_CLUB_GROUP));
    public static final Item FISHER_VEST = new ArmorItem(FISHER_MATERIAL, EquipmentSlot.CHEST, new Item.Settings().group(FISHING_CLUB_GROUP));
    public static final Item HARPOON_ROD = new HarpoonRodItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(64));
    public static final Item LINE_ARROW = new LineArrowItem(new Item.Settings().group(FISHING_CLUB_GROUP));
    public static final Item CLONED_ROD = new ClonedFishingRod(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(128));
    public static final Item ILLEGAL_GOODS = new IllegalGoodsItem(new Item.Settings().group(FISHING_CLUB_GROUP).rarity(Rarity.RARE).maxCount(1));
    public static final Item GOLD_FISH = new Item(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1)){
        @Override
        public ItemStack getDefaultStack() {
            System.out.println("is using default stack");
            return super.getDefaultStack();
        }
    };

    public static final Item DEBUG = new DebugItem(new Item.Settings().group(FISHING_CLUB_GROUP));


    public static void register(){
        registerItem(("fisher_workbench"), FISHER_WORKBENCH);
        registerItem(("fishing_net"), FISHING_NET);
        registerItem(("double_fishing_net"), DOUBLE_FISHING_NET);
        registerItem(("custom_fishing_rod"), MEMBER_FISHING_ROD);
        registerItem(("fish_coin_bundle"), FISH_COIN_BUNDLE);
        registerItem(("fisher_hat"), FISHER_HAT);
        registerItem(("fisher_vest"), FISHER_VEST);
        registerItem(("harpoon_rod"), HARPOON_ROD);
        registerItem(("line_arrow"), LINE_ARROW);
        registerItem(("cloned_rod"), CLONED_ROD);
        registerItem(("illegal_goods"), ILLEGAL_GOODS);
        registerItem(("gold_fish"), GOLD_FISH);
        registerItem("debug", DEBUG);
        FishingRodPartItems.registerParts();
    }

    public static void registerItem(String id, Item item){
        Registry.register(Registry.ITEM, FishingClub.getIdentifier(id), item);
    }
}

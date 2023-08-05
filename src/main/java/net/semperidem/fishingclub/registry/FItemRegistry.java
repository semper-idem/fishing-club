package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.item.*;
import net.semperidem.fishingclub.item.armor.FisherMaterial;

public class FItemRegistry {

    public static final ItemGroup FISHING_CLUB_GROUP = FabricItemGroupBuilder.build( FishingClub.getIdentifier("fishing_club_group"), () -> new ItemStack(Items.COD));

    public static final Item DOUBLE_FISHING_NET = new DoubleFishingNetItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(64));
    public static final CustomFishingRod CUSTOM_FISHING_ROD = new CustomFishingRod(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(128));
    public static final Item FISHER_WORKBENCH = new BlockItem(FBlockRegistry.FISHER_WORKBENCH_BLOCK, new Item.Settings().group(FISHING_CLUB_GROUP));
    public static final Item FISHING_NET = new FishingNetItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(64));
    public static final Item FISH_COIN_BUNDLE = new FishCoinBundleItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1));
    public static final ArmorMaterial FISHER_MATERIAL = new FisherMaterial();
    public static final Item FISHER_HAT = new ArmorItem(FISHER_MATERIAL, EquipmentSlot.HEAD, new Item.Settings().group(FISHING_CLUB_GROUP));
    public static final Item FISHER_VEST = new ArmorItem(FISHER_MATERIAL, EquipmentSlot.CHEST, new Item.Settings().group(FISHING_CLUB_GROUP));

    public static void register(){
        Registry.register(Registry.ITEM, FishingClub.getIdentifier("fisher_workbench"), FISHER_WORKBENCH);
        Registry.register(Registry.ITEM, FishingClub.getIdentifier("fishing_net"), FISHING_NET);
        Registry.register(Registry.ITEM, FishingClub.getIdentifier("double_fishing_net"), DOUBLE_FISHING_NET);
        Registry.register(Registry.ITEM, FishingClub.getIdentifier("custom_fishing_rod"), CUSTOM_FISHING_ROD);
        Registry.register(Registry.ITEM, FishingClub.getIdentifier("fish_coin_bundle"), FISH_COIN_BUNDLE);
        Registry.register(Registry.ITEM, FishingClub.getIdentifier("fisher_hat"), FISHER_HAT);
        Registry.register(Registry.ITEM, FishingClub.getIdentifier("fisher_vest"), FISHER_VEST);
        FishingRodPartItems.registerParts();

    }
}

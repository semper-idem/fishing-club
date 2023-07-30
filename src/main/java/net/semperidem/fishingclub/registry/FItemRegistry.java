package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.item.CustomFishingRod;
import net.semperidem.fishingclub.item.DoubleFishingNetItem;
import net.semperidem.fishingclub.item.FishingNetItem;
import net.semperidem.fishingclub.item.FishingRodPartItems;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FItemRegistry {

    public static final ItemGroup FISHING_CLUB_GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "fishing_club_group"), () -> new ItemStack(Items.COD));

    public static final Item DOUBLE_FISHING_NET = new DoubleFishingNetItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(64));
    public static final CustomFishingRod CUSTOM_FISHING_ROD = new CustomFishingRod(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(128));
    public static final Item FISHER_WORKBENCH = new BlockItem(FBlockRegistry.FISHER_WORKBENCH_BLOCK, new Item.Settings().group(FISHING_CLUB_GROUP));
    public static final Item FISHING_NET = new FishingNetItem(new Item.Settings().group(FISHING_CLUB_GROUP).maxCount(1).maxDamage(64));

    public static void register(){
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "fisher_workbench"), FISHER_WORKBENCH);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "fishing_net"), FISHING_NET);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "double_fishing_net"), DOUBLE_FISHING_NET);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "custom_fishing_rod"), CUSTOM_FISHING_ROD);
        FishingRodPartItems.registerParts();

    }
}

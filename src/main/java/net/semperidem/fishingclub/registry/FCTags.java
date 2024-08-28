package net.semperidem.fishingclub.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.semperidem.fishingclub.FishingClub;

public class FCTags {
    public static TagKey<Enchantment> ENCHANTMENT_REPAIR_TAG;
    public static TagKey<Item> FISH_ITEM_TAG;
    public static TagKey<Item> ROD_PARTS_CORE;

    public static void register() {
        ROD_PARTS_CORE = TagKey.of(RegistryKeys.ITEM, FishingClub.identifier("core"));
        FISH_ITEM_TAG = TagKey.of(RegistryKeys.ITEM, FishingClub.identifier("fish"));
        ENCHANTMENT_REPAIR_TAG = TagKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("exclusive_set/repair"));//could be repair.json
    }
}

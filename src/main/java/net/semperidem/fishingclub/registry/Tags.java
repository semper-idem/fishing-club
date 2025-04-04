package net.semperidem.fishingclub.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.semperidem.fishingclub.FishingClub;

public class Tags {
    public static final TagKey<Enchantment> ENCHANTMENT_REPAIR_TAG = TagKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("exclusive_set/repair"));
    public static final TagKey<Item> FISH_ITEM = TagKey.of(RegistryKeys.ITEM, FishingClub.identifier("fish"));
    public static final TagKey<Item> CORE = TagKey.of(RegistryKeys.ITEM, FishingClub.identifier("core"));
    public static final TagKey<Item> BAIT = TagKey.of(RegistryKeys.ITEM, FishingClub.identifier("bait"));
    public static final TagKey<Item> CONTAINER = TagKey.of(RegistryKeys.ITEM, FishingClub.identifier("container"));

}

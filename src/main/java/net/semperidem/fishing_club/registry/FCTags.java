package net.semperidem.fishing_club.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.semperidem.fishing_club.FishingClub;

public class FCTags {
    public static TagKey<Enchantment> ENCHANTMENT_REPAIR_TAG;

    public static void register() {
        ENCHANTMENT_REPAIR_TAG = TagKey.of(RegistryKeys.ENCHANTMENT, FishingClub.getIdentifier("exclusive_set/repair"));//could be repair.json
    }
}

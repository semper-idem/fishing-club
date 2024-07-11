package net.semperidem.fishing_club.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.semperidem.fishing_club.FishingClub;

public class FCEnchantments {
    public static RegistryKey<Enchantment> CURSE_OF_MORTALITY;

    public static void register() {
        CURSE_OF_MORTALITY = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.getIdentifier("curse_of_mortality"));
    }
}

package net.semperidem.fishingclub.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.semperidem.fishingclub.FishingClub;

public class FCEnchantments {
    public static RegistryKey<Enchantment> CURSE_OF_MORTALITY;

    public static void register() {
        CURSE_OF_MORTALITY = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.getIdentifier("curse_of_mortality"));
    }
}

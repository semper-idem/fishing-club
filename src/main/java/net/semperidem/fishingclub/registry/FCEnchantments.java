package net.semperidem.fishingclub.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.semperidem.fishingclub.FishingClub;

public class FCEnchantments {
    public static RegistryKey<Enchantment> CURSE_OF_MORTALITY;
    public static RegistryKey<Enchantment> MAGIC_REEL;
    public static RegistryKey<Enchantment> MAGIC_HOOK;

    public static void register() {
        CURSE_OF_MORTALITY = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("curse_of_mortality"));
        MAGIC_REEL = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("magic_reel"));
        MAGIC_HOOK = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("magic_hook"));
    }
}

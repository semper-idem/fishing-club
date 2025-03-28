package net.semperidem.fishingclub.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.semperidem.fishingclub.FishingClub;

public class Enchantments {
    public static RegistryKey<Enchantment> CURSE_OF_MORTALITY;
    public static RegistryKey<Enchantment> MAGIC_REEL;
    public static RegistryKey<Enchantment> MAGIC_HOOK;
    public static RegistryKey<Enchantment> FROST_PROTECTION;
    public static RegistryKey<Enchantment> CURSE_OF_CLUTTER;
    public static void register() {
        CURSE_OF_MORTALITY = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("curse_of_mortality"));
        MAGIC_REEL = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("magic_reel"));
        MAGIC_HOOK = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("magic_hook"));
        FROST_PROTECTION = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("frost_protection"));
        CURSE_OF_CLUTTER = RegistryKey.of(RegistryKeys.ENCHANTMENT, FishingClub.identifier("curse_of_clutter"));
    }

    public static int getEnchantmentLevel(LivingEntity livingEntity, ItemStack itemStack, RegistryKey<Enchantment> enchantmentRegistryKey) {
        return livingEntity
                .getRegistryManager()
                .get(RegistryKeys.ENCHANTMENT)
                .getEntry(enchantmentRegistryKey)
                .map(enchantmentReference -> itemStack
                                .getEnchantments()
                                .getLevel(enchantmentReference)
                )
                .orElse(0);
    }
}

package net.semperidem.fishingclub.registry;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.enchantments.CurseOfMortality;

public class EnchantmentRegistry {
    public static final Enchantment CURSE_OF_MORTALITY = new CurseOfMortality(Enchantment.Rarity.VERY_RARE);

    public static void register(){
        Registry.register(Registry.ENCHANTMENT, FishingClub.getIdentifier("curse_of_mortality"), CURSE_OF_MORTALITY);
    }
}

package net.semperidem.fishingclub.enchantments;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.VanishingCurseEnchantment;

public class CurseOfMortality extends VanishingCurseEnchantment {
    public CurseOfMortality(Rarity weight) {
        super(weight);
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        if (other == Enchantments.MENDING) return false;
        return super.canAccept(other);
    }
}

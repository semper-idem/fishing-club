package net.semperidem.fishingclub.item.armor;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;

public class FisherMaterial implements ArmorMaterial {
    @Override
    public int getDurability(EquipmentSlot slot) {
        return ArmorMaterials.LEATHER.getDurability(slot);
    }

    @Override
    public int getProtectionAmount(EquipmentSlot slot) {
        return ArmorMaterials.LEATHER.getProtectionAmount(slot);
    }

    @Override
    public int getEnchantability() {
        return ArmorMaterials.LEATHER.getEnchantability();
    }

    @Override
    public SoundEvent getEquipSound() {
        return ArmorMaterials.LEATHER.getEquipSound();
    }

    @Override
    public Ingredient getRepairIngredient() {
        return ArmorMaterials.LEATHER.getRepairIngredient();
    }

    @Override
    public String getName() {
        return "fisher";
    }

    @Override
    public float getToughness() {
        return ArmorMaterials.LEATHER.getToughness();
    }

    @Override
    public float getKnockbackResistance() {
        return ArmorMaterials.LEATHER.getKnockbackResistance();
    }
}

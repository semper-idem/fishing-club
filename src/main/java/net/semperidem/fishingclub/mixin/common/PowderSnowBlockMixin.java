package net.semperidem.fishingclub.mixin.common;


import net.minecraft.block.PowderSnowBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.semperidem.fishingclub.registry.FCEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PowderSnowBlock.class)
public class PowderSnowBlockMixin {

    @Inject(method = "canWalkOnPowderSnow", at = @At("RETURN"), cancellable = true)
    private static void fishingclub$onCanWalkOnPowderSnow(Entity entity, CallbackInfoReturnable<Boolean> cir) {
       if (!(entity instanceof LivingEntity livingEntity)) {
           return;
       }
       ItemStack feetStack = livingEntity.getEquippedStack(EquipmentSlot.FEET);
       livingEntity.getRegistryManager().get(RegistryKeys.ENCHANTMENT).getEntry(FCEnchantments.FROST_PROTECTION).ifPresent(frostEnchantment -> {
           cir.setReturnValue(feetStack.getEnchantments().getLevel(frostEnchantment) > 0);
       });
    }

}

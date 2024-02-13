package net.semperidem.fishingclub.mixin.common;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.registry.EnchantmentRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "hasVanishingCurse", at = @At("RETURN"), cancellable = true)
    private static void hasVanishingCurse(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        boolean result = cir.getReturnValueZ();
        result = result || EnchantmentHelper.getLevel(EnchantmentRegistry.CURSE_OF_MORTALITY, stack) > 0;
        cir.setReturnValue(result);
    }
}

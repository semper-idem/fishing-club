package net.semperidem.fishingclub.mixin.common;


import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.semperidem.fishingclub.registry.FCStatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    private static void fishingclub$registerDefaults(BrewingRecipeRegistry.Builder builder, CallbackInfo ci) {
        builder.registerRecipes(Items.POTATO, FCStatusEffects.FISHING_JUICE);
        builder.registerPotionRecipe(FCStatusEffects.FISHING_JUICE, Items.GLOWSTONE, FCStatusEffects.STRONG_FISHING_JUICE);
        builder.registerPotionRecipe(FCStatusEffects.FISHING_JUICE, Items.REDSTONE, FCStatusEffects.LONG_FISHING_JUICE);
    }
}

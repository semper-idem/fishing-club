package net.semperidem.fishingclub.mixin.common;


import net.minecraft.item.Items;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.semperidem.fishingclub.registry.StatusEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin {

    @Inject(method = "registerDefaults", at = @At("TAIL"))
    private static void fishingclub$registerDefaults(BrewingRecipeRegistry.Builder builder, CallbackInfo ci) {
        builder.registerRecipes(Items.POTATO, StatusEffects.FISHING_JUICE);
        builder.registerPotionRecipe(StatusEffects.FISHING_JUICE, Items.GLOWSTONE, StatusEffects.STRONG_FISHING_JUICE);
        builder.registerPotionRecipe(StatusEffects.FISHING_JUICE, Items.REDSTONE, StatusEffects.LONG_FISHING_JUICE);
    }
}

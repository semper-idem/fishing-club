package net.semperidem.fishingclub.mixin.common;

import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.semperidem.fishingclub.registry.Tags;
import net.semperidem.fishingclub.util.RecipeLocker;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingResultInventory.class)
public class CraftingResultInventoryMixin implements RecipeUnlocker {


    @Override
    public boolean shouldCraftRecipe(World world, ServerPlayerEntity player, RecipeEntry<?> recipe) {
        ItemStack recipeResult = recipe.value().getResult(player.getRegistryManager());
        if (!recipeResult.isIn(Tags.BAIT)) {
            return RecipeUnlocker.super.shouldCraftRecipe(world, player, recipe);
        }
        return RecipeLocker.canCraft(player, recipeResult.getItem());
    }

    @Shadow
    public void setLastRecipe(@Nullable RecipeEntry<?> recipe) {

    }

    @Shadow
    public @Nullable RecipeEntry<?> getLastRecipe() {
        return null;
    }
}

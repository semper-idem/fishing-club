package net.semperidem.fishingclub.mixin;


import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FisherInfos;
import net.semperidem.fishingclub.fisher.FishingPerks;
import net.semperidem.fishingclub.registry.FItemRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CraftingResultInventory.class)
public class CraftingResultInventoryMixin implements RecipeUnlocker{

    @Override
    public boolean shouldCraftRecipe(World world, ServerPlayerEntity player, Recipe<?> recipe){
        if (!recipe.getId().getNamespace().equals(FishingClub.MOD_ID)) RecipeUnlocker.super.shouldCraftRecipe(world, player, recipe);

        if (recipe.getOutput().isOf(FItemRegistry.FISHING_NET) && !FisherInfos.getFisher(player).hasPerk(FishingPerks.FISHING_NET)) return false;
        if (recipe.getOutput().isOf(FItemRegistry.DOUBLE_FISHING_NET) && !FisherInfos.getFisher(player).hasPerk(FishingPerks.UPGRADE_NET)) return false;

        return RecipeUnlocker.super.shouldCraftRecipe(world, player, recipe);
    }

    //TODO CLEANUP


    @Shadow
    public void setLastRecipe(@Nullable Recipe<?> recipe) {

    }

    @Nullable
    @Shadow
    public Recipe<?> getLastRecipe() {
        return null;
    }
}

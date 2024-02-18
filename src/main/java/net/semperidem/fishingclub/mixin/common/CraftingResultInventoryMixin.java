package net.semperidem.fishingclub.mixin.common;


import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.item.Item;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeUnlocker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.FishingRodPartItems;
import net.semperidem.fishingclub.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;

@Mixin(CraftingResultInventory.class)
public class CraftingResultInventoryMixin implements RecipeUnlocker{
    @Unique
    private static final HashMap<Item, FishingPerk> ITEM_TO_REQUIRED_PERK_MAP = new HashMap<>();
    static {
        ITEM_TO_REQUIRED_PERK_MAP.put(ItemRegistry.FISHING_NET, FishingPerks.FISHING_NET);
        ITEM_TO_REQUIRED_PERK_MAP.put(ItemRegistry.DOUBLE_FISHING_NET, FishingPerks.UPGRADE_NET);
        ITEM_TO_REQUIRED_PERK_MAP.put(ItemRegistry.FISHER_HAT, FishingPerks.FISHER_HAT);
        ITEM_TO_REQUIRED_PERK_MAP.put(ItemRegistry.FISHER_VEST, FishingPerks.FISHER_VEST);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.BAIT_FEATHER, FishingPerks.BAIT_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.BAIT_MAGNET, FishingPerks.BAIT_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.BAIT_CRAFTED, FishingPerks.BAIT_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.BOBBER_WOODEN, FishingPerks.LINE_BOBBER_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.BOBBER_PLANT, FishingPerks.LINE_BOBBER_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.BOBBER_ANCIENT, FishingPerks.LINE_BOBBER_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.LINE_FIBER_THREAD, FishingPerks.LINE_BOBBER_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.LINE_SPIDER_SILK, FishingPerks.LINE_BOBBER_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.LINE_WOOL_THREAD, FishingPerks.LINE_BOBBER_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.HOOK_COPPER, FishingPerks.HOOK_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.HOOK_IRON, FishingPerks.HOOK_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.HOOK_GOLD, FishingPerks.HOOK_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(FishingRodPartItems.HOOK_NETHERITE, FishingPerks.HOOK_CRAFTING);
        ITEM_TO_REQUIRED_PERK_MAP.put(ItemRegistry.HARPOON_ROD, FishingPerks.HARPOON_ROD);
        ITEM_TO_REQUIRED_PERK_MAP.put(ItemRegistry.LINE_ARROW, FishingPerks.BOW_FISHING);
    }

    @Override
    public boolean shouldCraftRecipe(World world, ServerPlayerEntity player, Recipe<?> recipe){
        if (!recipe.getId().getNamespace().equals(FishingClub.MOD_ID)) RecipeUnlocker.super.shouldCraftRecipe(world, player, recipe);

        Item output = recipe.getOutput().getItem();
        if (ITEM_TO_REQUIRED_PERK_MAP.containsKey(output)) {
            if (!FishingCard.getPlayerCard(player).hasPerk(ITEM_TO_REQUIRED_PERK_MAP.get(output))) {
                return false;
            }
        }

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

package net.semperidem.fishingclub.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.collection.DefaultedList;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.HashMap;
import java.util.HashSet;

import static net.semperidem.fishingclub.registry.FCItems.*;

public class RecipeLocker {
    private static final HashMap<Item, Integer> ITEM_TO_UNLOCK_LEVEL = new HashMap<>();
    public static void init() {
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_PLANT, 3);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_ROTTEN_FLESH, 3);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_MEAT, 5);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_ORCHID, 10);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_GLOW_BERRIES, 15);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_BLAZE_POWDER, 15);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_IRON, 20);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_GHAST, 25);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_RABBIT, 25);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_CHORUS, 30);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_BREEZE_BLAZE, 50);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_TORCHFLOWER, 50);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_PITCHER_PLANT, 50);
        ITEM_TO_UNLOCK_LEVEL.put(BAIT_BLAZE_POWDER, 50);
    }

    public static DefaultedList<Ingredient> ingredients(ServerPlayerEntity player, Item item) {
        DefaultedList<Ingredient> result = DefaultedList.of();
        player.getRegistryManager().get(RegistryKeys.RECIPE).getIndexedEntries().forEach(
                recipeEntry -> {
                    Recipe<?> recipe = recipeEntry.value();
                    if (recipe.getResult(player.getRegistryManager()).isOf(item)) {//this can break if new table for bait crafting will be added
                        result.addAll(recipe.getIngredients());
                    }
                }
        );
        return result;
    }

    public static HashSet<Item> unlockedAt(int level) {
        HashSet<Item> result = new HashSet<>();
        ITEM_TO_UNLOCK_LEVEL.forEach((item, unlockLevel) -> {
            if (unlockLevel == level) {
                result.add(item);
            }
        });
        return result;
    }

    public static boolean canCraft(PlayerEntity player, Item item) {
        if (!ITEM_TO_UNLOCK_LEVEL.containsKey(item)) {
            return true;
        }
        return FishingCard.of(player).getLevel() > ITEM_TO_UNLOCK_LEVEL.get(item);
    }


}

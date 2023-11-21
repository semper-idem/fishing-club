package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.ItemStack;

public enum FishingRodStatType {
    BOBBER_WIDTH, //Percentage increase of bobber width DONE
    DAMAGE_REDUCTION, // Percentage damage reduction uncapped atm DONE
    LINE_HEALTH, // Additional health point DONE
    CATCH_RATE,//Percentage reduction of time until bite DONE
    PROGRESS_MULTIPLIER,//Self-explanatory DONE
    FISH_MAX_WEIGHT_MULTIPLIER,//Moves upper barrier of max fish weight (on average heavier fish) DONE
    FISH_MAX_LENGTH_MULTIPLIER,//Moves upper barrier of max fish length (on average longer fish) DONE
    FISH_RARITY_BONUS,//Percentage chance to gain fish on higher grade DONE
    BITE_WINDOW_MULTIPLIER,//Extends ticks in which fish can be hooked DONE
    ROD_DAMAGE_CHANCE;

    public static float getStat(ItemStack fishingRod, FishingRodStatType stat){
        float result = 0;
        for(ItemStack partStack : FishingRodPartController.getParts(fishingRod)) {
            if (!(partStack.getItem() instanceof FishingRodPartItem partItem)) continue;
            result += partItem.getStat(stat);
        }
        return result;
    }

    public FishingRodStat of(float value) {
        return new FishingRodStat(this, value);
    }
}

package net.semperidem.fishingclub.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.semperidem.fishingclub.client.game.FishGameLogic;
import net.semperidem.fishingclub.item.FishingRodPartItem;
import net.semperidem.fishingclub.item.FishingRodPartItems;

import java.util.ArrayList;

public class FishingRodUtil {

    public static ArrayList<ItemStack> getRodParts(ItemStack rodStack){
        ArrayList<ItemStack> rodParts = new ArrayList<>();
        if (!rodStack.hasNbt()) {
            return rodParts;
        }
        NbtCompound rodTag = rodStack.getNbt();
        if (!rodTag.contains("parts")) {
            return rodParts;
        }
        NbtCompound partsTag = rodTag.getCompound("parts");
        for(FishingRodPartItem.PartType partType : FishingRodPartItem.PartType.values()) {
            if (!partsTag.contains(partType.name())) {
                continue;
            }
            rodParts.add(FishingRodPartItems.getStackFromCompound(partsTag.getCompound(partType.name())));
        }
        return rodParts;
    }

    public static ItemStack getRodPart(ItemStack rodStack, FishingRodPartItem.PartType partType){
        if (!rodStack.hasNbt()) {
            return ItemStack.EMPTY;
        }
        NbtCompound rodTag = rodStack.getNbt();
        if (!rodTag.contains("parts")) {
            return ItemStack.EMPTY;
        }
        NbtCompound partsTag = rodTag.getCompound("parts");
        if (!partsTag.contains(partType.name())) {
            return ItemStack.EMPTY;
        }

        return FishingRodPartItems.getStackFromCompound(partsTag.getCompound(partType.name()));
    }

    //TODO MOVE TO UTIL
    public static float getStat(ItemStack fishingRod, FishGameLogic.Stat stat){
        float result = 0;
        for(ItemStack partStack : getRodParts(fishingRod)) {
            FishingRodPartItem partItem = (FishingRodPartItem) partStack.getItem();
            result += partItem.getStatBonuses().getOrDefault(stat, 0f);
        }
        return result;
    }

    public static int getPower(int usageTick){
        return Math.max(1, Math.min(5, (usageTick + 40) / 40));
    }
}

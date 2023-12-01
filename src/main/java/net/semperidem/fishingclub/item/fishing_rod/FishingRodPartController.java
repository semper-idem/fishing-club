package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

import java.util.ArrayList;

public class FishingRodPartController {
    public static ArrayList<ItemStack> getParts(ItemStack fishingRodStack){
        if (!hasParts(fishingRodStack)) return new ArrayList<>();
        return getPartsFromList(fishingRodStack.getOrCreateNbt().getList("parts", NbtElement.COMPOUND_TYPE));
    }

    public static float getStat(ItemStack fishingRodStack, FishingRodStatType statType){
        float combinedStat = 0;
        for (ItemStack fishingRodPart : getParts(fishingRodStack)){
            if (fishingRodPart.getItem() instanceof FishingRodPartItem fishingRodPartItem) {
                combinedStat = fishingRodPartItem.getStat(statType);
            }
        }
        return combinedStat;
    }

    public static boolean hasBait(ItemStack fishingRod){
        return hasPart(fishingRod, FishingRodPartType.BAIT);
    }

    private static boolean hasPart(ItemStack fishingRod, FishingRodPartType partType){
        return getPart(fishingRod, partType) != ItemStack.EMPTY;
    }

    public static boolean hasParts(ItemStack fishingRod){
        return fishingRod.hasNbt() && fishingRod.getOrCreateNbt().contains("parts");
    }

    public static ItemStack putPart(ItemStack fishingRod, ItemStack replacingPart){
        ItemStack replacedPart = ItemStack.EMPTY;
        if (!(replacingPart.getItem() instanceof FishingRodPartItem partItem)) return replacedPart;
        NbtCompound rodNbt = fishingRod.getOrCreateNbt();
        NbtList listOfParts = rodNbt.contains("parts") ? rodNbt.getList("parts", NbtElement.COMPOUND_TYPE) : new NbtList();

        FishingRodPartType replacingPartType = partItem.getPartType();
        for(int i = 0; i < listOfParts.size(); i++) {
            ItemStack currentPart = ItemStack.fromNbt(listOfParts.getCompound(i));
            if (!(currentPart.getItem() instanceof FishingRodPartItem currentPartItem)) continue;
            if (replacingPartType != currentPartItem.getPartType()) continue;
            replacedPart = currentPart;
            listOfParts.set(i, replacingPart.writeNbt(new NbtCompound()));
            break;
        }

        if (replacedPart == ItemStack.EMPTY) {
            listOfParts.add(replacingPart.writeNbt(new NbtCompound()));
        }

        replacingPart.setCount(0);
        rodNbt.put("parts", listOfParts);
        return replacedPart;
    }

    public static ItemStack removePart(ItemStack fishingRod, FishingRodPartType partType){
        ItemStack removedPart = ItemStack.EMPTY;
        NbtCompound rodNbt = fishingRod.getOrCreateNbt();
        NbtList listOfParts = rodNbt.contains("parts") ? rodNbt.getList("parts", NbtElement.COMPOUND_TYPE) : new NbtList();

        for(int i = 0; i < listOfParts.size(); i++) {
            ItemStack currentPart = ItemStack.fromNbt(listOfParts.getCompound(i));
            if (!(currentPart.getItem() instanceof FishingRodPartItem currentPartItem)) continue;
            if (partType != currentPartItem.getPartType()) continue;
            removedPart = currentPart;
            listOfParts.remove(i);
            break;
        }
        rodNbt.put("parts", listOfParts);

        return removedPart;
    }

    private static ArrayList<ItemStack> getPartsFromList(NbtList nbtListOfParts){
        ArrayList<ItemStack> parts = new ArrayList<>();
        for(int i = 0 ; i < nbtListOfParts.size(); i++) {
            parts.add(ItemStack.fromNbt(nbtListOfParts.getCompound(i)));
        }
        return parts;
    }

    public static ItemStack getPart(ItemStack fishingRodStack, FishingRodPartType partType){
        if (fishingRodStack == null) return ItemStack.EMPTY;
        if (!hasParts(fishingRodStack)) return ItemStack.EMPTY;
        for(ItemStack partStack : getParts(fishingRodStack)) {
            if (!(partStack.getItem() instanceof FishingRodPartItem partItem)) continue;
            if (!partItem.getPartType().equals(partType)) continue;
            return partStack;
        }
        return ItemStack.EMPTY;
    }
}

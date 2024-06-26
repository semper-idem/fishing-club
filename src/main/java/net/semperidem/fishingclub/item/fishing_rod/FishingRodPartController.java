package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

public class FishingRodPartController {

    public static ArrayList<ItemStack> getParts(ItemStack fishingRodStack){
        if (fishingRodStack.getItem() != ItemRegistry.MEMBER_FISHING_ROD) return new ArrayList<>();
        return (fishingRodStack
                .getOrCreateNbt()
                .getList("parts", NbtElement.COMPOUND_TYPE))
                .stream()
                .map(nbtElement -> ItemStack.fromNbt((NbtCompound) nbtElement))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<FishingRodPartItem> getItemParts(ItemStack fishingRodStack){
        return (fishingRodStack
                .getOrCreateNbt()
                .getList("parts", NbtElement.COMPOUND_TYPE))
                .stream()
                .map(nbtElement -> (FishingRodPartItem)Registry.ITEM.get(new Identifier(((NbtCompound)nbtElement).getString("id"))))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static float getStat(ItemStack fishingRodStack, FishingRodStatType statType){
        return getParts(fishingRodStack)
                .stream()
                .filter(itemStack -> itemStack.getItem() instanceof FishingRodPartItem)
                .map(itemStack -> ((FishingRodPartItem)itemStack.getItem()).getStat(statType))
                .reduce(Float::sum)
                .orElse(0f);
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

    public static void putPart(ItemStack fishingRod, ItemStack partToPut){
        if (!(partToPut.getItem() instanceof FishingRodPartItem)) return;

        NbtCompound rodNbt = fishingRod.getOrCreateNbt();
        NbtList listOfParts = rodNbt.getList("parts", NbtElement.COMPOUND_TYPE);
        listOfParts.add(partToPut.writeNbt(new NbtCompound()));
        partToPut.setCount(0);
        rodNbt.put("parts", listOfParts);
    }

    public static ArrayList<ItemStack> takeParts(ItemStack fishingRod){
        ArrayList<ItemStack> result = getParts(fishingRod);
        fishingRod.getOrCreateNbt().put("parts", new NbtList());
        return result;
    }

    public static void removePart(ItemStack fishingRod, FishingRodPartType partType){
        NbtCompound rodNbt = fishingRod.getOrCreateNbt();
        NbtList listOfParts = rodNbt.getList("parts", NbtElement.COMPOUND_TYPE);
        listOfParts.remove(getIndexOfPartType(listOfParts, partType));
        rodNbt.put("parts", listOfParts);
    }

    private static int getIndexOfPartType(NbtList listOfParts,  FishingRodPartType partType){
        for(int i = 0; i < listOfParts.size(); i++) {
            if (isTagPartType(listOfParts.getCompound(i), partType)) return i;
        }
        return -1;
    }

    private static boolean isTagPartType(NbtCompound itemStackTag, FishingRodPartType partType){
        return ((FishingRodPartItem)ItemStack.fromNbt(itemStackTag).getItem()).getPartType() == partType;
    }


    public static ItemStack getPart(ItemStack fishingRodStack, FishingRodPartType partType){
        return getParts(fishingRodStack)
                .stream()
                .filter(itemStack -> ((FishingRodPartItem)itemStack.getItem()).getPartType() == partType)
                .findFirst()
                .orElse(ItemStack.EMPTY);
    }

    public static Item getItemPart(ItemStack fishingRodStack, FishingRodPartType partType){
        Optional<FishingRodPartItem> rodPart =  getItemParts(fishingRodStack)
                .stream()
                .filter(itemPart -> itemPart.getPartType() == partType)
                .findFirst();
        return rodPart.isPresent() ? rodPart.get() : Items.AIR;
    }
}

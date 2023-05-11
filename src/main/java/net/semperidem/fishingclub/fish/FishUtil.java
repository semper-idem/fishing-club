package net.semperidem.fishingclub.fish;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.fishingskill.FishingSkill;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FishUtil {


    public static ItemStack prepareFishItemStack(String fishName, float weight, float length){
        ItemStack fishReward = new ItemStack(Items.TROPICAL_FISH).setCustomName(Text.of(fishName));
        setFishDetails(fishReward, fishName, weight, length);
        return fishReward;
    }

    private static void setFishDetails(ItemStack stack, String fishName, float weight, float length){
        if (!stack.hasNbt()) {
            stack.setNbt(new NbtCompound());
        }

        setLore(stack, getDetailsAsLore(fishName, weight, length));
        setDetails(stack, fishName, weight, length);
    }
    private static List<Text> getDetailsAsLore(String fishName, float weight, float length){
        FishType fishType = FishType.allFishTypes.get(fishName);
        int weightGrade = getGrade(weight, fishType.fishMinWeight, fishType.fishMinWeight + fishType.fishRandomWeight);
        int lengthGrade = getGrade(length, fishType.fishMinLength, fishType.fishMinLength + fishType.fishRandomLength);
        return Arrays.asList(
                getWeightText(weight, weightGrade),
                getLengthText(length, lengthGrade),
                getCaughtText()
        );
    }

    private static void setDetails(ItemStack stack, String fishName, float weight, float length){
        NbtCompound fishDetails = new NbtCompound();
        fishDetails.putString("name", fishName);
        fishDetails.putFloat("weight", weight);
        fishDetails.putFloat("length", length);
        stack.getNbt().put("fish_details", fishDetails);
    }

    private static Text getWeightText(float weight, int grade){
        return Text.of("§3Weight:§" + getGradeColor(grade)+ " " + String.format("%.2f", weight) + "kg");
    }

    private static Text getLengthText(float length, int grade){
        return Text.of("§3Length:§" + getGradeColor(grade)+ " " + String.format("%.2f", length) + "cm");
    }

    private static String getGradeColor(int grade){
        return switch (grade) {
            case 1 -> "8";
            case 2 -> "7";
            case 3 -> "f";
            case 4 -> "e";
            case 5 -> "6";
            default -> "k";
        };
    }

    public static int getGrade(float caught, float min, float max){
        float percentile = (caught - min) / (max - min);
        if (percentile < 0.1) {
            return 1;
        }
        if (percentile < 0.25) {
            return 2;
        }
        if (percentile < 0.6) {
            return 3;
        }
        if (percentile < 0.9) {
            return 4;
        }
        return 5;
    }

    private static Text getCaughtText(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("EEE, dd LLL yyyy HH:mm");
        LocalDateTime now = LocalDateTime.now();
        String caughtDate = (dtf.format(now));
        return Text.of("§3Caught: §f" + caughtDate);
    }

    private static void setLore(ItemStack stack, List<Text> lore) {
        NbtCompound displayTag;
        if (stack.getNbt().contains("display")) {
            displayTag = stack.getNbt().getCompound("display");
        } else {
            displayTag = new NbtCompound();
            stack.getNbt().put("display", displayTag);
        }

        NbtList loreTag = new NbtList();
        for (Text line : lore) {
            loreTag.add(NbtString.of(Text.Serializer.toJson(line)));
        }
        displayTag.put("Lore", loreTag);
    }

    public static Fish getFishOnHook(FishingSkill fishingSkill){
        int totalRarity = 0;
        HashMap<FishType, Integer> fishTypeToThreshold = new HashMap<>();
        ArrayList<FishType> availableFish = new ArrayList<>();
        for (FishType fishType : FishType.allFishTypes.values()) {
            if (fishingSkill.level > fishType.fishMinLevel) {
                availableFish.add(fishType);
            }
        }
        for (FishType fishType : availableFish) {
            totalRarity += fishType.fishRarity;
            fishTypeToThreshold.put(fishType, totalRarity);
        }
        int randomFish = (int) (Math.random() * totalRarity);
        for (FishType fishType : availableFish) {
            if (randomFish < fishTypeToThreshold.get(fishType)) {
                return new Fish(fishType, fishingSkill);
            }
        }
        return new Fish();
    }

}

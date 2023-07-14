package net.semperidem.fishingclub.client.game.fish;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfos;
import net.semperidem.fishingclub.util.Point;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FishUtil {
    public static final Item FISH_ITEM = Items.TROPICAL_FISH;


    public static ItemStack prepareFishItemStack(Fish fish){
        ItemStack fishReward = new ItemStack(FISH_ITEM).setCustomName(Text.of(fish.name));
        setFishDetails(fishReward, fish);
        return fishReward;
    }

    public static void grantReward(ServerPlayerEntity player, Fish fish){
        FisherInfos.grantExperience(player.getUuid(), fish.experience);
        player.addExperience(Math.max(1, fish.experience / 10));
        ItemStack fishReward = FishUtil.prepareFishItemStack(fish);
        if (player.getInventory().getEmptySlot() == -1) {
            player.dropItem(fishReward, false);
        } else {
            player.giveItemStack(fishReward);
        }

    }

    public static void grantExp(ServerPlayerEntity player, int exp){
        FisherInfos.grantExperience(player.getUuid(), exp);
        player.addExperience(Math.max(1, exp / 10));
    }

    private static void setFishDetails(ItemStack stack, Fish fish){
        if (!stack.hasNbt()) {
            stack.setNbt(new NbtCompound());
        }

        setLore(stack, getDetailsAsLore(fish));
        setDetails(stack, fish);
    }
    private static List<Text> getDetailsAsLore(Fish fish){
        int weightGrade = getWeightGrade(fish);
        int lengthGrade = getLengthGrade(fish);
        return Arrays.asList(
                getGradeText(Math.max(lengthGrade, weightGrade)),
                getWeightText(fish.weight, weightGrade),
                getLengthText(fish.length, lengthGrade),
                getCaughtText()
        );
    }

    private static void setDetails(ItemStack stack, Fish fish){
        if (stack.getNbt() != null) {
            stack.getNbt().put("fish_details", FishUtil.toNbt(fish));
        }
    }

    private static Text getGradeText(int grade){
        return Text.of("§3Grade:§" + getGradeColor(grade)+ " " + integerToRoman(grade));
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

    public static int getWeightGrade(Fish fish){
        float percentile = (fish.weight - fish.getFishType().fishMinWeight) / fish.weight;
        return getGrade(percentile);
    }
    public static int getLengthGrade(Fish fish){
        float percentile = (fish.length - fish.getFishType().fishMinLength) / fish.length;
        return getGrade(percentile);
    }

    private static int getGrade(float percentile){
        if (percentile < 0.1) {
            return 1;
        }
        if (percentile < 0.50) {
            return 2;
        }
        if (percentile < 0.80) {
            return 3;
        }
        if (percentile < 0.95) {
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

    public static Fish getFishOnHook(FisherInfo fisherInfo){
        int totalRarity = 0;
        HashMap<FishType, Integer> fishTypeToThreshold = new HashMap<>();
        ArrayList<FishType> availableFish = new ArrayList<>();
        for (FishType fishType : FishType.allFishTypes.values()) {
            if (fisherInfo.getLevel() > fishType.fishMinLevel) {
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
                return new Fish(fishType, fisherInfo);
            }
        }
        return new Fish();
    }



    public static float getPseudoRandomValue(float base, float randomAdjustment, float skew){
        return (float) (base + randomAdjustment / 2 * skew + randomAdjustment / 2 * Math.random());
    }
    public static int getPseudoRandomValue(int base, int randomAdjustment, int skew){
        return (int) (base + randomAdjustment / 2 * skew + randomAdjustment / 2 * Math.random());
    }


    public static float getRandomValue(float base, float randomAdjustment){
        return (float) ((base + randomAdjustment * Math.random()));
    }
    public static int getRandomValue(int base, int randomAdjustment){
        return (int) ((base + randomAdjustment * Math.random()));
    }

    public static float clampValue(float from, float to, float value){
            return Math.max(Math.min(to, value), from);
    }
    public static int clampValue(int from, int to, int value){
        return Math.max(Math.min(to, value), from);
    }

    public static PacketByteBuf fishToPacketBuf(Fish fish){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(toNbt(fish));
        return buf;
    }


    public static Fish fishFromPacketBuf(PacketByteBuf fishBuf){
        NbtCompound fishNbt = fishBuf.readNbt();
        if (fishNbt == null){
            return new Fish();
        }
        if (fishNbt.contains("fish_details")){
            return fromNbt(fishNbt.getCompound("fish_details"));
        }
        return fromNbt(fishNbt);
    }


    private static final TreeMap<Integer, String> treemap = new TreeMap<Integer, String>();
    static {
        treemap.put(1000, "M");
        treemap.put(900, "CM");
        treemap.put(500, "D");
        treemap.put(400, "CD");
        treemap.put(100, "C");
        treemap.put(90, "XC");
        treemap.put(50, "L");
        treemap.put(40, "XL");
        treemap.put(10, "X");
        treemap.put(9, "IX");
        treemap.put(5, "V");
        treemap.put(4, "IV");
        treemap.put(1, "I");

    }

    public static String integerToRoman(int number) {
        int l = treemap.floorKey(number);
        if (number == l) {
            return treemap.get(number);
        }
        return treemap.get(l) + integerToRoman(number - l);
    }

    public static int getFishValue(Fish fish){
        float gradeMultiplier;
        float levelMultiplier;
        float rarityBase;
        float weightMultiplier;
        try {
            if (fish.grade <= 3) {
                gradeMultiplier = (float) (0.5 + (fish.grade / 3f));
            } else {
                gradeMultiplier = (float) Math.pow(2, (fish.grade - 2));
            }
            levelMultiplier = 1 + (float) Math.pow(2, fish.fishLevel / 50f);
            rarityBase = 1 + ((125 - fish.getFishType().fishRarity) / 100) * 0.5f;
            weightMultiplier = 1 + fish.weight / 100;
        } catch (NullPointerException nullPointerException) {
            nullPointerException.printStackTrace();
            return 1;
        }
        return (int) (rarityBase * gradeMultiplier * levelMultiplier * weightMultiplier);
    }


    public static int getFishValue(ItemStack fishStack){
        NbtCompound fishNbt = fishStack.getNbt();
        if (fishNbt != null) {
            return fishNbt.getCompound("fish_details").getInt("value");
        }
        return 1;
     }



    static NbtCompound toNbt(Fish fish){
        NbtCompound fishNbt = new NbtCompound();
        fishNbt.putString("name", fish.name);
        fishNbt.putInt("grade", fish.grade);
        fishNbt.putInt("fishLevel", fish.fishLevel);
        fishNbt.putInt("experience", fish.experience);
        fishNbt.putInt("value", fish.value);
        fishNbt.putFloat("weight", fish.weight);
        fishNbt.putFloat("length", fish.length);
        fishNbt.putString("curvePoints", curveToString(fish.curvePoints));
        fishNbt.putString("curveControlPoints", curveToString(fish.curveControlPoints));
        return fishNbt;
    }

    static Fish fromNbt(NbtCompound nbtCompound){
        Fish fish = new Fish();
        fish.name = nbtCompound.getString("name");
        fish.setFishType(FishType.allFishTypes.get(fish.name));
        fish.grade = nbtCompound.getInt("grade");
        fish.fishLevel = nbtCompound.getInt("fishLevel");
        fish.experience = nbtCompound.getInt("experience");
        fish.value = nbtCompound.getInt("value");
        fish.weight = nbtCompound.getFloat("weight");
        fish.length = nbtCompound.getFloat("length");
        fish.curvePoints = FishUtil.curveFromString(nbtCompound.getString("curvePoints"));
        fish.curveControlPoints = FishUtil.curveFromString(nbtCompound.getString("curveControlPoints"));
        return fish;
    }

    static Point[] curveFromString(String curvePointString){
        String[] input = curvePointString.split(";");
        int pointCount = input.length;
        Point[] curvePoints = new Point[pointCount];
        for(int i = 0; i < pointCount; i++) {
            curvePoints[i] = new Point(input[i]);
        }
        return curvePoints;
    }

    static String curveToString(Point[] curvePoints){
        StringBuilder result = new StringBuilder();
        for(Point point : curvePoints) {
            result.append(point.toString()).append(";");
        }
        return result.toString();
    }
}

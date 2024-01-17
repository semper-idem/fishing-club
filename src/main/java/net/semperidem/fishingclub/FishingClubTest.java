package net.semperidem.fishingclub;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.HookedFish;
import net.semperidem.fishingclub.fish.Species;
import net.semperidem.fishingclub.fish.SpeciesLibrary;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.game.treasure.Rewards;
import net.semperidem.fishingclub.registry.FItemRegistry;

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class FishingClubTest {
    private static final int N = 5000;
    private static final ArrayList<String> RUNTIME_OUTPUT = new ArrayList<>();


    private static HashMap<Species, Integer> countUp(HashMap<Species, Integer> map, Species key){
        if(map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, 1);
        }
        return map;
    }
    private static TreeMap<Integer, Integer> countUp(TreeMap<Integer, Integer> map, Integer key){
        if(map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, 1);
        }
        return map;
    }
    private static TreeMap<Float, Integer> countUp(TreeMap<Float, Integer> map, Float key){
        if(map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, 1);
        }
        return map;
    }
    private static TreeMap<Float, Integer> sortMap(TreeMap<Float, Integer> inputMap){
        Set<Float> keySet = inputMap.keySet();
        TreeMap<Float, Integer> resultMap = new TreeMap<>();
        keySet.forEach(key -> {
            resultMap.put(key, inputMap.get(key));
        });

        return resultMap;
    }

    private static TreeMap<Float, Integer> mergeMap(TreeMap<Float, Integer> inputMap, int mergeTimes){
        if (mergeTimes < 2) return inputMap;
        if (mergeTimes > inputMap.size()) return inputMap;
        inputMap = sortMap(inputMap);
        float[][] rawArray = new float[inputMap.size()][2];
        ArrayList<Float> keyList = new ArrayList<>(inputMap.keySet());
        for(int i = 0; i < keyList.size(); i++) {
            rawArray[i][0] = keyList.get(i);
            rawArray[i][1] = inputMap.get(keyList.get(i));
        }

        TreeMap<Float, Integer> mergedMap = new TreeMap<>();
        for(int i = 0; i <= rawArray.length / mergeTimes; i++) {
            float avgKey = 0;
            float sumValue = 0;
            for(int j = 0; j < mergeTimes; j++) {
                if (i * mergeTimes + j >= rawArray.length) break;
                avgKey += rawArray[i * mergeTimes + j][0];
                sumValue += rawArray[i * mergeTimes + j][1];
            }
            avgKey /= mergeTimes;
            mergedMap.put(avgKey, (int) sumValue);
        }
        return mergedMap;
    }

    private static void printResultFloat(TreeMap<Float, Integer> resultMap, String resultCategory, int mergeTimes){
        final float[] avg = {0};
        final float[] min = {0};
        final float[] max = {0};
        println("====================================");
        resultMap.keySet().forEach(key -> {
            if (key > max[0]) max[0] = key;
            if (key < min[0]) min[0] = key;
            avg[0] += (key * resultMap.get(key));
//            println(resultCategory + ": " + key);
//            println("Count: " + finalResultMap.get(key));
//            println("%: " + (finalResultMap.get(key) / (N / 100f)));
        });
        println("Avg: " + (avg[0]/N));
        println("Min: " + (min[0]));
        println("Max: " + (max[0]));
        drawGraph(resultMap, resultCategory);
        println("====================================");
    }

    private static void printResultFishType(HashMap<Species, Integer> resultMap, String resultCategory){
        println("====================================");
        resultMap.keySet().stream().sorted().forEach(key -> {
            println(
            (resultCategory + ": " + key.name) +
            (" Count: " + resultMap.get(key)) +
            (" %: " + (resultMap.get(key) / (N * 100f)))
            );
        });
        println("====================================");

    }

    private static void printResultInteger(TreeMap<Integer, Integer> resultMap, String resultCategory, int mergeTimes){
        TreeMap<Float, Integer> floatResultMap = new TreeMap<>();
        resultMap.forEach((k,v) -> floatResultMap.put(Float.valueOf(k),v));
        printResultFloat(floatResultMap, resultCategory, mergeTimes);
    }

    private static void runFishTest(FishingCard fishingCard){
        println("####################################");
        println("RUNNING TEST FOR FISHER LEVEL:" + fishingCard.getLevel());
        ItemStack fishingRod = FItemRegistry.CUSTOM_FISHING_ROD.getDefaultStack();
        TreeMap<Integer, Integer> gradeResult = new TreeMap<>();
        TreeMap<Integer, Integer> levelResult = new TreeMap<>();
        HashMap<Species, Integer> typeResult = new HashMap<>();
        TreeMap<Float, Integer> weightResult = new TreeMap<>();
        TreeMap<Float, Integer> sizeResult = new TreeMap<>();
        for(int i = 0; i < N; i++) {
            HookedFish fish = FishUtil.getFishOnHook(fishingCard, fishingRod, 1, new FishingCard.Chunk(0,0));
            countUp(gradeResult,fish.grade);
            countUp(levelResult,fish.level);
            countUp(typeResult,fish.getSpecies());
            if (fish.getSpecies() == SpeciesLibrary.COD) {
                countUp(weightResult,Float.valueOf(String.format("%.0f", fish.weight)));
                countUp(sizeResult,Float.valueOf(String.format("%.0f", fish.length)));
            }
        }

       printResultFloat(weightResult, "Weight", 1);
        printResultFloat(sizeResult, "Length", 1);
        printResultInteger(gradeResult, "Grade", 1);
        printResultInteger(levelResult, "Level", 1);
        printResultFishType(typeResult, "Fish Type");

    }

    private static void runTreasureTest(){
        //treasureTestCost();
        //treasureTestGrade();
        treasureTestLoot();
    }

    private static void treasureTestGrade(){
        for(int i = 2; i < 12; i++) {
            HashMap<Integer, Integer> resultMap = new HashMap<>();
            for(int j = 1; j < 8; j++) {
                resultMap.put(j, 0);
            }
            int avgGrade = 0;
            int maxCount = 0;
            FishingCard fishingCard = new FishingCard((int) Math.pow(2, i));
            for(int j = 0 ; j < N; j++) {
                int grade = Rewards.getGrade(fishingCard);
                for(Integer key : resultMap.keySet().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new))) {
                    if (grade == key) {
                        resultMap.put(key, resultMap.get(key) + 1);
                        avgGrade+= grade;
                        if (resultMap.get(key) > maxCount) {
                            maxCount = resultMap.get(key);
                        }
                        break;
                    }
                }
            }
            println("============================================================");
            println("Result for Grade Test - Fisher Level:" + fishingCard.getLevel());
            int scale = maxCount / 30;
            for(Integer grade : resultMap.keySet().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new))) {
                int count = resultMap.get(grade);
                if (count == 0) continue;

                println("Grade: " + grade + " - " + count + " ".repeat(5 - (int)(Math.log10(count) + 1)) + " " + ".".repeat(count / scale));
            }
            println("Avg Grade: " + (avgGrade * 1f / N));
            println("============================================================");
        }
        writeFile("grade_test");
    }

    private static void treasureTestCost(){
        for(int i = 2; i < 12; i++) {
            HashMap<Integer, Integer> resultMap = new HashMap<>();
            for(int j = 0; j < 1500; j+=25) {
                resultMap.put(j, 0);
            }
            int avgCost = 0;
            int maxCount = 0;
            FishingCard fishingCard = new FishingCard((int) Math.pow(2, i));
            for(int j = 0 ; j < N; j++) {
                int cost = Rewards.getCost(fishingCard);
                for(Integer key : resultMap.keySet().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new))) {
                    if (cost < key) {
                        resultMap.put(key, resultMap.get(key) + 1);
                        avgCost+= cost;
                        if (resultMap.get(key) > maxCount) {
                            maxCount = resultMap.get(key);
                        }
                        break;
                    }
                }
            }
            println("============================================================");
            println("Result for Cost Test - Fisher Level:" + fishingCard.getLevel());
            int scale = maxCount / 30;
            for(Integer cost : resultMap.keySet().stream().sorted().collect(Collectors.toCollection(LinkedHashSet::new))) {
                int count = resultMap.get(cost);
                if (count == 0) continue;

                println("Cost <" + cost + " - " + count + " ".repeat(5 - (int)(Math.log10(count) + 1)) + " " + ".".repeat(count / scale));
            }
            println("Avg Cost: " + (avgCost / N));
            println("============================================================");
        }
        writeFile("cost_test");
    }

    private static void treasureTestLoot(){
        for(int i = 0; i <= 5; i++) {
            HashMap<Item, Integer> resultMap = new HashMap<>();
            FishingCard fishingCard = new FishingCard(i * 50);
            for(int j = 0 ; j < N; j++) {
                ArrayList<ItemStack> rewards = Rewards.roll(fishingCard).getContent();
                for(ItemStack reward : rewards) {
                    Item asItem = reward.getItem();
                    if (resultMap.containsKey(asItem)) {
                        resultMap.put(asItem, resultMap.get(asItem) + 1);
                    } else {
                        resultMap.put(asItem, 1);
                    }
                }
            }
            println("============================================================");
            println("Result for Loot Test - Fisher Level:" + fishingCard.getLevel());
            for(Item item : resultMap.keySet().stream().sorted(Comparator.comparingInt(resultMap::get).reversed()).collect(Collectors.toCollection(LinkedHashSet::new))) {
                println("Item: " + item.getName().getString() + " count: " + resultMap.get(item));
            }
            println("============================================================");
        }
        writeFile("loot_test");
    }

    private static void drawGraph(TreeMap<Float, Integer> inputMap, String title){
        println("+++++++++++++++++++++++++++++++++++++");
        println("---------- TITLE: " + title);
        inputMap = mergeMap(inputMap, inputMap.size() / 10);
        AtomicInteger maxCount = new AtomicInteger();
        TreeMap<Float, Integer> finalInputMap = inputMap;
        inputMap.keySet().forEach(key -> {
            int value = finalInputMap.get(key);
            if (maxCount.get() < value) maxCount.set(value);
        });
        inputMap.keySet().forEach(key -> {
            int count = finalInputMap.get(key);
            int dotCount = (int) (count / (maxCount.get() / 20f));
            println(".".repeat(dotCount) + " ".repeat(25 - dotCount) + key);
        });
        println("+++++++++++++++++++++++++++++++++++++");
    }
    public static void runTest() {
        runFishTest(new FishingCard(1));
        runTreasureTest();
        throw new RuntimeException();
    }
    
    private static void println(String line){
        System.out.println(line);
        RUNTIME_OUTPUT.add(line);
    }

    public static void writeFile(String testName) {
        Date date = new Date() ;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
        FileWriter fw;
        try {
            fw = new FileWriter("test/" + testName + "_" + dateFormat.format(date) +".txt");
   
        for (int i = 0; i < RUNTIME_OUTPUT.size(); i++) {
            fw.write(RUNTIME_OUTPUT.get(i) +"\n");
        }
        fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RUNTIME_OUTPUT.clear();
    }
}

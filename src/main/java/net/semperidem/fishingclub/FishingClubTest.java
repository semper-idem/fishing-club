package net.semperidem.fishingclub;

import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.client.game.fish.Fish;
import net.semperidem.fishingclub.client.game.fish.FishType;
import net.semperidem.fishingclub.client.game.fish.FishTypes;
import net.semperidem.fishingclub.client.game.fish.FishUtil;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.registry.FItemRegistry;

import java.util.HashMap;

public class FishingClubTest {


    private static HashMap<FishType, Integer> countUp(HashMap<FishType, Integer> map, FishType key){
        if(map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, 1);
        }
        return map;
    }
    private static HashMap<Integer, Integer> countUp(HashMap<Integer, Integer> map, Integer key){
        if(map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, 1);
        }
        return map;
    }
    private static HashMap<Float, Integer> countUp(HashMap<Float, Integer> map, Float key){
        if(map.containsKey(key)) {
            map.put(key, map.get(key) + 1);
        } else {
            map.put(key, 1);
        }
        return map;
    }

    private static void runTestFor(FisherInfo fisherInfo){
        System.out.println("====================================");
        System.out.println("====================================");
        System.out.println("====================================");
        System.out.println("RUNNIG TEST FOR: " + fisherInfo.getLevel());
        ItemStack fishingRod = FItemRegistry.CUSTOM_FISHING_ROD.getDefaultStack();
        HashMap<Integer, Integer> gradeResult = new HashMap<>();
        HashMap<Integer, Integer> levelResult = new HashMap<>();
        HashMap<FishType, Integer> typeResult = new HashMap<>();
        HashMap<Float, Integer> weightResult = new HashMap<>();
        HashMap<Float, Integer> sizeResult = new HashMap<>();
        for(int i = 0; i < 1000; i++) {
            Fish fish = FishUtil.getFishOnHook(fisherInfo, fishingRod, 1);
            countUp(gradeResult,fish.grade);
            countUp(levelResult,fish.fishLevel);
            countUp(typeResult,fish.getFishType());
            if (fish.getFishType() == FishTypes.COD) {
                countUp(weightResult,Float.valueOf(String.format("%.0f", fish.weight)));
                countUp(sizeResult,Float.valueOf(String.format("%.0f", fish.length)));
            }
        }

        System.out.println("====================================");
        gradeResult.keySet().forEach(grade -> {
            System.out.println("Grade " + grade + " count: " + gradeResult.get(grade));
        });

        final float[] avgLevel = {0};
        System.out.println("====================================");
        levelResult.keySet().forEach(level -> {
            avgLevel[0] += (level * levelResult.get(level));
            System.out.println("Level " + level + " count: " + levelResult.get(level));
        });
        System.out.println("Avg Level: " + (avgLevel[0]/1000));

        System.out.println("====================================");
        typeResult.keySet().forEach(type -> {
            System.out.println("Fish Type " + type.name + " count: " + typeResult.get(type));
        });

        final float[] avgWeight = {0};
        System.out.println("====================================");
        weightResult.keySet().stream().sorted().forEach(weight -> {
            avgWeight[0] += (weight * weightResult.get(weight));
            System.out.println("Weight " + weight + " count: " + weightResult.get(weight) + ".".repeat(weightResult.get(weight)/5));
        });
        System.out.println("Avg Weight: " + (avgWeight[0]/1000));
        System.out.println("====================================");


//        System.out.println("====================================");
//        sizeResult.keySet().stream().sorted().forEach(length -> {
//            System.out.println("Length " + length + " count: " + sizeResult.get(length));
//        });
//        System.out.println("====================================");
        System.out.println("====================================");
        System.out.println("====================================");
        System.out.println("====================================");
    }

    public static void runTest(){
        runTestFor(new FisherInfo(1));
        runTestFor(new FisherInfo(100));
        runTestFor(new FisherInfo(1000));
        throw new RuntimeException();
    }
}

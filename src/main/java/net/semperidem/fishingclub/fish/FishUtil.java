package net.semperidem.fishingclub.fish;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.registry.FStatusEffectRegistry;
import net.semperidem.fishingclub.util.Point;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FishUtil {
    public static final Item FISH_ITEM = Items.TROPICAL_FISH;
    private static final Random RANDOM = new java.util.Random(42L);


    public static ItemStack prepareFishItemStack(HookedFish fish){
        ItemStack fishReward = new ItemStack(FISH_ITEM).setCustomName(Text.of(fish.name));
        setFishDetails(fishReward, fish);
        return fishReward;
    }

    public static void grantReward(ServerPlayerEntity player, HookedFish fish, ArrayList<ItemStack> treasureReward){
        FishingCardManager.fishCaught(player, fish);
        player.addExperience(Math.max(1, fish.experience / 10));
        ItemStack fishReward = FishUtil.prepareFishItemStack(fish);
        FishingCard fishingCard = FishingCardManager.getPlayerCard(player);
        fishReward.setCount(getRewardMultiplier(fishingCard));
        if (fish.grade >= 4 && fishingCard.hasPerk(FishingPerks.QUALITY_SHARING) && !player.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF) && !fish.oneTimeBuffed) {
            Box box = new Box(player.getBlockPos());
            box.expand(3);
            for(Entity entity : player.getEntityWorld().getOtherEntities(null, box)) {
                if (entity instanceof ServerPlayerEntity serverPlayerEntity && !serverPlayerEntity.hasStatusEffect(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF)) {
                    serverPlayerEntity.addStatusEffect(new StatusEffectInstance(FStatusEffectRegistry.ONE_TIME_QUALITY_BUFF, 2400));
                }
            }
        }
        if (fish.caughtAt == null) {
            treasureReward.add(fishReward);
            for(ItemStack reward : treasureReward) {
                if (player.getInventory().getEmptySlot() == -1) {
                    player.dropItem(reward, false);
                } else {
                    player.giveItemStack(reward);
                }
            }
        } else {
            player.world.spawnEntity(throwRandomly(player.world, fish.caughtAt, fishReward));
        }
    }

    private static ItemEntity throwRandomly(World world, BlockPos pos, ItemStack stack){
        if (stack.isEmpty()) {
            return null;
        }
        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
        itemEntity.setPickupDelay(40);
        float f = world.random.nextFloat() * 0.25f;
        float g = world.random.nextFloat() * ((float)Math.PI * 2);
        itemEntity.setVelocity(-MathHelper.sin(g) * f, 0.75f, MathHelper.cos(g) * f);
        return itemEntity;
    }

    public static void grantReward(ServerPlayerEntity player, HookedFish fish){
        grantReward(player, fish, new ArrayList<>());
    }

    private static int getRewardMultiplier(FishingCard fishingCard){
        int rewardMultiplier = 1;
        if (!fishingCard.isFishingFromBoat()) {
            return rewardMultiplier;
        }
        if (fishingCard.hasPerk(FishingPerks.DOUBLE_FISH_BOAT) && Math.random() < 0.09) {
            rewardMultiplier = 2;
        }
        if (fishingCard.hasPerk(FishingPerks.TRIPLE_FISH_BOAT) && (Math.random() < 0.06)) {
            rewardMultiplier = 3;
        }
        while (fishingCard.hasPerk(FishingPerks.INFINITY_FISH) && Math.random() < 0.03) {
                rewardMultiplier++;
        }

        return rewardMultiplier;
    }


    private static void setFishDetails(ItemStack stack, HookedFish fish){
        stack.getOrCreateNbt();
        setLore(stack, getDetailsAsLore(fish));
        setDetails(stack, fish);
    }
    private static List<Text> getDetailsAsLore(HookedFish fish){
        int weightGrade = getWeightGrade(fish);
        int lengthGrade = getLengthGrade(fish);
        return Arrays.asList(
                getGradeText(Math.max(lengthGrade, weightGrade)),
                getWeightText(fish.weight, weightGrade),
                getLengthText(fish.length, lengthGrade),
                getCaughtText(),
                getValueText(fish.value)
        );
    }

    private static void setDetails(ItemStack stack, HookedFish fish){
        if (stack.getNbt() != null) {
            stack.getNbt().put("fish_details", FishUtil.toNbt(fish));
        }
    }

    private static Text getValueText(int value){
        return Text.of("§3Value: §6" + value + "$");
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

    public static int getWeightGrade(HookedFish fish){
        FishType fishType = fish.getFishType();
        float percentile = (fish.weight) / (fishType.fishMinWeight + fishType.fishRandomWeight);
        return getGrade(percentile);
    }
    public static int getLengthGrade(HookedFish fish){
        FishType fishType = fish.getFishType();
        float percentile = (fish.length) / (fishType.fishMinLength + fishType.fishRandomLength);
        return getGrade(percentile);
    }

    private static int getGrade(float percentile){
        if (percentile < 0.25) {
            return 1;
        }
        if (percentile < 0.50) {
            return 2;
        }
        if (percentile < 0.80) {
            return 3;
        }
        if (percentile < 0.90) {
            return 4;
        }
        return 5;
    }

    public static float getPercentile(int grade){
        switch (grade){
            case 2: return 0.25f;
            case 3: return 0.50f;
            case 4: return 0.80f;
            case 5: return 0.90f;
            default: return 0;
        }
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

    public static HookedFish getFishOnHook(FishingCard fishingCard, ItemStack fishingRod, float fishTypeRarityMultiplier, FishingCard.Chunk chunk){
        return getFishOnHook(fishingCard, fishingRod, fishTypeRarityMultiplier, chunk, null);
    }

    public static HookedFish getFishOnHook(FishingCard fishingCard, ItemStack fishingRod, float fishTypeRarityMultiplier, FishingCard.Chunk chunk, BlockPos caughtAt) {
        int totalRarity = 0;
        HashMap<FishType, Integer> fishTypeToThreshold = new HashMap<>();
        ArrayList<FishType> availableFish = FishTypes.getFishTypesForFisher(fishingCard);
        for (FishType fishType : availableFish) {
            totalRarity += fishType.fishRarity;
            fishTypeToThreshold.put(fishType, totalRarity);
        }
        int randomFishRarity = (int) (Math.random() * totalRarity * fishTypeRarityMultiplier);
        for (FishType fishType : availableFish) {
            if (randomFishRarity < fishTypeToThreshold.get(fishType)) {
                return new HookedFish(fishType, fishingCard, fishingRod, chunk, caughtAt);
            }
        }
        return new HookedFish(FishTypes.COD, fishingCard, fishingRod, chunk, caughtAt);
    }

        public static float getPseudoRandomValue(float base, float randomAdjustment, float skew){
        float skewPercentage = 0.5f;
        skew = (float) Math.sqrt(skew) * 0.7f; //This deliberately lefts top 15% of randomAdjustments "unused" so we leave space for future bonuses
        double skewPart = randomAdjustment * skew * skewPercentage;
        double nextG = Math.max(-3, Math.min( 3, RANDOM.nextGaussian()));
        double mappedG = (nextG + 3) / 6;
        double randomPart = randomAdjustment * mappedG * (1 - skewPercentage);
        return (float) (base + skewPart + randomPart);
    }
    public static int getPseudoRandomValue(int base, int randomAdjustment, float skew){
        return (int) getPseudoRandomValue(Float.valueOf(base), Float.valueOf(randomAdjustment), skew);
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

    public static PacketByteBuf fishToPacketBuf(HookedFish fish){
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(toNbt(fish));
        return buf;
    }


    public static HookedFish fishFromPacketBuf(PacketByteBuf fishBuf){
        NbtCompound fishNbt = fishBuf.readNbt();
        if (fishNbt == null){
            return new HookedFish();
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

    public static int getFishValue(HookedFish fish){
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



    static NbtCompound toNbt(HookedFish fish){
        NbtCompound fishNbt = new NbtCompound();
        fishNbt.putString("name", fish.name);
        fishNbt.putInt("grade", fish.grade);
        fishNbt.putInt("fishLevel", fish.fishLevel);
        fishNbt.putInt("experience", fish.experience);
        fishNbt.putInt("value", fish.value);
        fishNbt.putFloat("weight", fish.weight);
        fishNbt.putFloat("length", fish.length);
        fishNbt.put("caughtUsing", fish.caughtUsing.getOrCreateNbt());
        fishNbt.putString("caughtIn", fish.caughtIn.toString());
        fishNbt.putString("curvePoints", curveToString(fish.curvePoints));
        fishNbt.putString("curveControlPoints", curveToString(fish.curveControlPoints));
        return fishNbt;
    }

    static HookedFish fromNbt(NbtCompound nbtCompound){
        HookedFish fish = new HookedFish();
        fish.name = nbtCompound.getString("name");
        fish.setFishType(FishTypes.ALL_FISH_TYPES.get(fish.name));
        fish.grade = nbtCompound.getInt("grade");
        fish.fishLevel = nbtCompound.getInt("fishLevel");
        fish.experience = nbtCompound.getInt("experience");
        fish.value = nbtCompound.getInt("value");
        fish.weight = nbtCompound.getFloat("weight");
        fish.length = nbtCompound.getFloat("length");
        fish.caughtUsing = ItemStack.fromNbt(nbtCompound.getCompound("caughtUsing"));
        fish.caughtIn = new FishingCard.Chunk(nbtCompound.getString("caughtIn"));
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


    public static boolean hasFishingHat(PlayerEntity owner){
        final boolean[] result = {false};
        owner.getArmorItems().forEach(armorStack -> {
            if (armorStack.isOf(FItemRegistry.FISHER_HAT)) result[0] = true;
        });
        return result[0];
    }

    public static boolean hasFishingVest(PlayerEntity owner){
        final boolean[] result = {false};
        owner.getArmorItems().forEach(armorStack -> {
            if (armorStack.isOf(FItemRegistry.FISHER_VEST)) result[0] = true;
        });
        return result[0];
    }

    public static boolean hasNonFishingEquipment(PlayerEntity owner){
        final boolean[] result = {false};
        owner.getArmorItems().forEach(armorStack -> {
            if (!(armorStack.isOf(FItemRegistry.FISHER_VEST) || armorStack.isOf(FItemRegistry.FISHER_HAT) || armorStack.isEmpty())) result[0] = true;
        });
        return result[0];
    }
}

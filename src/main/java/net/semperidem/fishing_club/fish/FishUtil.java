package net.semperidem.fishing_club.fish;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.semperidem.fishing_club.entity.IHookEntity;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.fisher.perks.FishingPerks;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCItems;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FishUtil {
    public static final Item FISH_ITEM = FCItems.FISH;
    private static final Random RANDOM = new Random(42L);
    private static final String CAUGHT_BY_TAG = "caught_by";


    public static ItemStack getStackFromFish(FishRecord fish){
        return getStackFromFish(fish, 1);
    }

    public static ItemStack getStackFromFish(FishRecord fish, int count){
        ItemStack fishReward = new ItemStack(FISH_ITEM);
        fishReward.set(FCComponents.FISH, fish);
        fishReward.set(DataComponentTypes.CUSTOM_NAME, Text.of(fish.name()));
        setLore(fishReward, fish);
        fishReward.setCount(count);
        return fishReward;
    }

    public static void fishCaught(ServerPlayerEntity player, FishRecord fish){
        FishingCard fishingCard = FishingCard.of(player);
        fishingCard.fishCaught(fish);
        giveItemStack(player, getStackFromFish(fish, getRewardMultiplier(fishingCard)));
    }

    public static void fishCaughtAt(ServerPlayerEntity player, FishRecord fish, BlockPos caughtAt) {
        FishingCard fishingCard = FishingCard.of(player);
        fishingCard.fishCaught(fish);
        throwRandomly(player.getWorld(), caughtAt, getStackFromFish(fish));
    }

    public static void giveReward(ServerPlayerEntity player, ArrayList<ItemStack> treasureReward){
        for(ItemStack reward : treasureReward) {
            giveItemStack(player, reward);
        }
    }

    private static void giveItemStack(ServerPlayerEntity player, ItemStack itemStack){
        if (player.getInventory().getEmptySlot() == -1) {
            player.dropItem(itemStack, false);
        } else {
            player.giveItemStack(itemStack);
        }
    }

    private static void throwRandomly(World world, BlockPos pos, ItemStack stack){
        if (stack.isEmpty()) {
            return;
        }
        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
        itemEntity.setPickupDelay(40);
        float f = world.random.nextFloat() * 0.25f;
        float g = world.random.nextFloat() * ((float)Math.PI * 2);
        itemEntity.setVelocity(-MathHelper.sin(g) * f, 0.75f, MathHelper.cos(g) * f);
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


    private static void setLore(ItemStack stack, FishRecord fish){
        stack.set(DataComponentTypes.LORE, new LoreComponent(getDetailsAsLore(fish)));
    }
    private static List<Text> getDetailsAsLore(FishRecord fish){
        int weightGrade = getWeightGrade(fish);
        int lengthGrade = getLengthGrade(fish);
        return Arrays.asList(
                getGradeText(Math.max(lengthGrade, weightGrade)),
                getWeightText(fish.weight(), weightGrade),
                getLengthText(fish.length(), lengthGrade),
                getCaughtBy(fish.caughtBy()),
                getCaughtAt(fish.caughtAt()),
                getValueText(fish.value())
        );
    }


    public static boolean isFish(ItemStack fish) {
        return fish.isOf(FISH_ITEM);
    }


    private static Text getValueText(int value){
        return Text.of("§3Value: §6" + value + "$");
    }

    private static Text getGradeText(int grade){
        return Text.of("§3Grade:§" + getGradeColor(grade)+ " " + getGradeString(grade));
    }

    private static String getGradeString(int grade) {
        return switch(grade) {
            case 1 -> "Defect";
            case 2 -> "Poor";
            case 3 -> "Standard";
            case 4 -> "Good";
            case 5 -> "Excellent";
            default -> "Unidentifiable";
        };
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

    public static int getWeightGrade(FishRecord fish){
        Species fishType = SpeciesLibrary.ALL_FISH_TYPES.get(fish.speciesName());
        if (fishType == null) {
            return 1;
        }
        float percentile = (fish.weight()) / (fishType.fishMinWeight + fishType.fishRandomWeight);
        return getGrade(percentile);
    }
    public static int getLengthGrade(FishRecord fish){
        Species fishType = SpeciesLibrary.ALL_FISH_TYPES.get(fish.speciesName());
        if (fishType == null) {
            return 1;
        }
        float percentile = (fish.length()) / (fishType.fishMinLength + fishType.fishRandomLength);
        return getGrade(percentile);
    }

    private static int getGrade(float percentile){
        if (percentile < 0.25) {
            return 1;
        } else if (percentile < 0.5) {
            return 2;
        } else if (percentile < 0.8) {
            return 3;
        } else if (percentile < 0.9) {
            return 4;
        } else {
            return 5;
        }
    }

    private static Text getCaughtBy(String caughtBy){
        return Text.of("§3Caught by: §f"+ caughtBy);
    }


    private static Text getCaughtAt(long caughtAtEpoch) {
        String caughtAt = DateTimeFormatter
          .ofPattern("HH:mm, dd.MM.yyyy")
          .format(LocalDateTime.ofInstant(
            Instant.ofEpochMilli(caughtAtEpoch),
            ZoneOffset.systemDefault()
          ));
        return Text.of("§3Caught at: §f" + caughtAt);
    }

    private static void setLore(ItemStack stack, List<Text> lore) {
        stack.set(DataComponentTypes.LORE, new LoreComponent(lore));
    }

    private static Species getRandomSpecies(int level) {
        int totalWeight = 0;
        HashMap<Species, Integer> speciesToTotalWeight = new HashMap<>();
        for (Species species : SpeciesLibrary.getSpeciesForLevel(level)) {
            totalWeight += (int) species.fishRarity;
            speciesToTotalWeight.put(species, totalWeight);
        }

        int randomWeight = (int) (Math.random() * totalWeight);
        Species randomSpecies = SpeciesLibrary.COD;
        for(Species species : speciesToTotalWeight.keySet()) {
            randomSpecies = species;
            if (randomWeight < speciesToTotalWeight.get(randomSpecies)) {
                break;
            }
        }
        return randomSpecies;
    }

    public static FishRecord getFishOnHook(IHookEntity hookEntity) {
        return FishRecord.create(hookEntity);
    }

        public static float getPseudoRandomValue(float base, float randomAdjustment, float skew){
        float skewPercentage = 0.5f;
        skew = MathHelper.clamp(skew, 0.01f, 1);
        double skewPart = randomAdjustment * (float) Math.sqrt(skew) * 0.8f * skewPercentage;
        double nextG = Math.max(-3, Math.min( 3, RANDOM.nextGaussian()));
        double mappedG = (nextG + 3) / 6;
        double randomPart = randomAdjustment * mappedG * (1 - skewPercentage);
        return (float) (base + skewPart + randomPart);
    }

    public static int getPseudoRandomValue(int base, int randomAdjustment, float skew){
        return (int) getPseudoRandomValue(Float.valueOf(base), Float.valueOf(randomAdjustment), skew);
    }

    public static boolean hasFishingHat(PlayerEntity owner){
        final boolean[] result = {false};
        owner.getArmorItems().forEach(armorStack -> {
            if (armorStack.isOf(FCItems.FISHER_HAT)) result[0] = true;
        });
        return result[0];
    }

    public static boolean hasFishingVest(PlayerEntity owner){
        final boolean[] result = {false};
        owner.getArmorItems().forEach(armorStack -> {
            if (armorStack.isOf(FCItems.FISHER_VEST)) result[0] = true;
        });
        return result[0];
    }

    public static boolean hasProperFishingEquipment(PlayerEntity owner){
        final boolean[] result = {false};
        owner.getArmorItems().forEach(armorStack -> {
            if (!(armorStack.isOf(FCItems.FISHER_VEST) || armorStack.isOf(FCItems.FISHER_HAT) || armorStack.isEmpty())) result[0] = true;
        });
        return !result[0];
    }
}

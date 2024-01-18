package net.semperidem.fishingclub.fish;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.game.FishingAtlas;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.util.MathUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FishUtil {
    public static final Item FISH_ITEM = Items.TROPICAL_FISH;
    private static final Random RANDOM = new Random(42L);


    public static ItemStack getFishStack(Fish fish){
        return getFishStack(fish, 1);
    }

    public static ItemStack getFishStack(Fish fish, int count){
        ItemStack fishReward = new ItemStack(FISH_ITEM).setCustomName(Text.of(fish.name));
        setFishDetails(fishReward, fish);
        fishReward.setCount(count);
        return fishReward;
    }

    public static void fishCaught(ServerPlayerEntity player, Fish fish){
        FishingCard fishingCard = FishingAtlas.getCard(player.getUuid());
        fishingCard.fishCaught(fish);
        giveItemStack(player, getFishStack(fish, getRewardMultiplier(fishingCard)));
    }

    public static void fishCaughtAt(ServerPlayerEntity player, Fish fish, BlockPos caughtAt) {
        FishingCard fishingCard = FishingAtlas.getCard(player.getUuid());
        fishingCard.fishCaught(fish);
        throwRandomly(player.getWorld(), caughtAt, getFishStack(fish));
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


    private static void setFishDetails(ItemStack stack, Fish fish){
        stack.getOrCreateNbt();
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
                getCaughtText(),
                getValueText(fish.value)
        );
    }

    private static void setDetails(ItemStack stack, Fish fish){
        if (stack.getNbt() != null) {
            stack.getNbt().put("fish_details", fish.getNbt());
        }
    }

    private static Text getValueText(int value){
        return Text.of("§3Value: §6" + value + "$");
    }

    private static Text getGradeText(int grade){
        return Text.of("§3Grade:§" + getGradeColor(grade)+ " " + MathUtil.integerToRoman(grade));
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
        Species fishType = fish.getSpecies();
        float percentile = (fish.weight) / (fishType.fishMinWeight + fishType.fishRandomWeight);
        return getGrade(percentile);
    }
    public static int getLengthGrade(Fish fish){
        Species fishType = fish.getSpecies();
        float percentile = (fish.length) / (fishType.fishMinLength + fishType.fishRandomLength);
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

    public static Fish getFishOnHook(IHookEntity hookEntity) {
        FishingCard fishingCard = hookEntity.getFishingCard();
        Species species = getRandomSpecies(fishingCard.getLevel());
        Fish fishOnHook = new Fish(species, hookEntity.getCaughtUsing(), fishingCard);
        fishOnHook.applyMultiplier(hookEntity.getFishMultiplier());
        fishingCard.fishHooked(hookEntity);
        return fishOnHook;
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

    public static int getFishValue(ItemStack fishStack){
        NbtCompound fishNbt = fishStack.getNbt();
        if (fishNbt != null) {
            return fishNbt.getCompound("fish_details").getInt("value");
        }
        return 1;
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

    public static boolean hasProperFishingEquipment(PlayerEntity owner){
        final boolean[] result = {false};
        owner.getArmorItems().forEach(armorStack -> {
            if (!(armorStack.isOf(FItemRegistry.FISHER_VEST) || armorStack.isOf(FItemRegistry.FISHER_HAT) || armorStack.isEmpty())) result[0] = true;
        });
        return !result[0];
    }
}

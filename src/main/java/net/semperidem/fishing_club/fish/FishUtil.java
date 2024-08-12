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
import net.semperidem.fishing_club.item.FishItem;
import net.semperidem.fishing_club.item.FishingNetItem;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCItems;
import net.semperidem.fishing_club.world.ChunkQuality;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.semperidem.fishing_club.fish.FishRecord.MAX_QUALITY;
import static net.semperidem.fishing_club.fish.FishRecord.MIN_QUALITY;
import static net.semperidem.fishing_club.world.ChunkQuality.CHUNK_QUALITY;

public class FishUtil {
    public static final Item DEFAULT_FISH_ITEM = FCItems.FISH;
    private static final String CAUGHT_BY_TAG = "caught_by";



    public static ItemStack getStackFromFish(FishRecord fish){
        return getStackFromFish(fish, 1);
    }

    public static ItemStack getStackFromFish(FishRecord fish, int count){
        ItemStack fishReward = new ItemStack(fish.species().item);
        fishReward.set(FCComponents.FISH, fish);
        fishReward.set(DataComponentTypes.CUSTOM_NAME, Text.of(fish.name()));
        setLore(fishReward, fish);
        fishReward.setCount(count);
        return fishReward;
    }


private static ItemStack getFishingNet(ServerPlayerEntity player, ItemStack fishStack) {
        for(int i = 0; i < 9; i++) {
            ItemStack stackInHotbar = player.getInventory().getStack(i);
            if (!(stackInHotbar.getItem() instanceof FishingNetItem fishingNetItem)) {
                continue;
            }
            if (fishingNetItem.canInsert(stackInHotbar, fishStack)) {
                return stackInHotbar;
            }
        }
        return FishingCard.of(player).getFishingNet(fishStack);
    }

    public static void fishCaught(ServerPlayerEntity player, FishRecord fish){
        FishingCard fishingCard = FishingCard.of(player);
        fishingCard.fishCaught(fish);
        ItemStack fishStack = getStackFromFish(fish, getRewardMultiplier(fishingCard));
        ItemStack fishingNetStack = getFishingNet(player, fishStack);
        if (!fishingNetStack.isEmpty() && FCItems.FISHING_NET.insertStack(fishingNetStack, fishStack, player)) {
            return;
        }
        CHUNK_QUALITY.get(player.getWorld().getChunk(player.getBlockPos())).influence(ChunkQuality.PlayerInfluence.FISH_CAUGHT);
        giveItemStack(player, fishStack);
    }

    public static void fishCaughtAt(ServerPlayerEntity player, FishRecord fish, BlockPos caughtAt) {
        FishingCard fishingCard = FishingCard.of(player);
        fishingCard.fishCaught(fish);
        CHUNK_QUALITY.get(player.getWorld().getChunk(player.getBlockPos())).influence(ChunkQuality.PlayerInfluence.EXPLOSION);
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

    public static void throwRandomly(World world, BlockPos pos, ItemStack stack){
        if (stack.isEmpty()) {
            return;
        }
        ItemEntity itemEntity = new ItemEntity(world, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, stack);
        itemEntity.setPickupDelay(40);
        float f = world.random.nextFloat() * 0.25f;
        float g = world.random.nextFloat() * ((float)Math.PI * 2);
        itemEntity.setVelocity(-MathHelper.sin(g) * f, 0.75f, MathHelper.cos(g) * f);
        world.spawnEntity(itemEntity);
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


    private static void setLore(ItemStack stack, FishRecord fish) {
        stack.set(DataComponentTypes.LORE, new LoreComponent(getDetailsAsLore(fish)));
    }

    private static List<Text> getDetailsAsLore(FishRecord fish) {
        ArrayList<Text> result = new ArrayList<>();
        result.add(getQualityText(fish.quality()));
        result.add(getWeightText(fish.weight(), getWeightGrade(fish)));
        result.add(getLengthText(fish.length(), getLengthGrade(fish)));
        result.add(getCaughtBy(fish.caughtBy()));
        result.add(getCaughtAt(fish.caughtAt()));
        result.add(getValueText(fish.value()));
        if (!fish.isAlive()) {
            result.add(Text.of("<x)))><"));
        }
        return result;
    }


    public static boolean isFish(ItemStack fish) {
        return fish.getItem() instanceof FishItem;
    }


    private static Text getValueText(int value){
        return Text.of("§3Value: §6" + value + "$");
    }

    private static Text getQualityText(int grade){
        return Text.of("§3Grade:§" + getGradeColor(grade)+ " " + getQualityString(grade));
    }

    private static String getQualityString(int grade) {
        return switch(grade) {
            case MIN_QUALITY -> "Defect";
            case 2 -> "Poor";
            case 3 -> "Standard";
            case 4 -> "Good";
            case MAX_QUALITY -> "Excellent";
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
            case MIN_QUALITY -> "8";
            case 2 -> "7";
            case 3 -> "f";
            case 4 -> "e";
            case MAX_QUALITY -> "6";
            default -> "k";
        };
    }

    public static int getWeightGrade(FishRecord fish){
        Species fishType = SpeciesLibrary.ALL_FISH_TYPES.get(fish.speciesName());
        if (fishType == null) {
            return MIN_QUALITY;
        }
        float percentile = (fish.weight()) / (fishType.fishMinWeight + fishType.fishRandomWeight);
        return getGrade(percentile);
    }
    public static int getLengthGrade(FishRecord fish){
        Species fishType = SpeciesLibrary.ALL_FISH_TYPES.get(fish.speciesName());
        if (fishType == null) {
            return MIN_QUALITY;
        }
        float percentile = (fish.length()) / (fishType.fishMinLength + fishType.fishRandomLength);
        return getGrade(percentile);
    }

    private static int getGrade(float percentile){
        if (percentile < 0.25) {
            return MIN_QUALITY;
        } else if (percentile < 0.5) {
            return 2;
        } else if (percentile < 0.8) {
            return 3;
        } else if (percentile < 0.9) {
            return 4;
        } else {
            return MAX_QUALITY;
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
        Species randomSpecies = SpeciesLibrary.DEFAULT;
        for(Species species : speciesToTotalWeight.keySet()) {
            randomSpecies = species;
            if (randomWeight < speciesToTotalWeight.get(randomSpecies)) {
                break;
            }
        }
        return randomSpecies;
    }

    public static FishRecord getFishOnHook(IHookEntity hookEntity) {
        return FishRecord.init(hookEntity);
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

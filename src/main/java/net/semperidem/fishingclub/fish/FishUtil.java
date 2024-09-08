package net.semperidem.fishingclub.fish;

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
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.item.FishingNetItem;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.registry.FCItems;
import net.semperidem.fishingclub.registry.FCTags;
import net.semperidem.fishingclub.world.ChunkQuality;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.semperidem.fishingclub.fish.specimen.SpecimenData.MAX_QUALITY;
import static net.semperidem.fishingclub.fish.specimen.SpecimenData.MIN_QUALITY;
import static net.semperidem.fishingclub.world.ChunkQuality.CHUNK_QUALITY;

public class FishUtil {
    public static final Item DEFAULT_FISH_ITEM = FCItems.FISH;

    public static ItemStack getStackFromFish(SpecimenData fish){
        return getStackFromFish(fish, 1);
    }

    public static ItemStack getStackFromFish(SpecimenData fish, int count){
        ItemStack fishReward = fish.asItemStack();
        if (fish.isAlive()) {

            setLore(fishReward, fish);
        }
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

    public static void fishCaught(ServerPlayerEntity player, SpecimenData fish){
        FishingCard fishingCard = FishingCard.of(player);
        fishingCard.fishCaught(fish);
        ItemStack fishStack = getStackFromFish(fish, getRewardMultiplier(fishingCard));
        ItemStack fishingNetStack = getFishingNet(player, fishStack);
        CHUNK_QUALITY.get(player.getWorld().getChunk(player.getBlockPos())).influence(ChunkQuality.PlayerInfluence.FISH_CAUGHT);
        if (!fishingNetStack.isEmpty() && FCItems.FISHING_NET.insertStack(fishingNetStack, fishStack, player)) {
            return;
        }
        giveItemStack(player, fishStack);
    }

    public static void fishCaughtAt(ServerPlayerEntity player, SpecimenData fish, BlockPos caughtAt) {
        FishingCard fishingCard = FishingCard.of(player);
        fishingCard.fishCaught(fish);
        CHUNK_QUALITY.get(player.getWorld().getChunk(player.getBlockPos())).influence(ChunkQuality.PlayerInfluence.EXPLOSION);
        throwRandomly(player.getWorld(), caughtAt, getStackFromFish(fish));
    }

    public static void giveReward(ServerPlayerEntity player, List<ItemStack> treasureReward){
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
            int luck = 0;
            if (fishingCard.holder() != null) {
                luck = (int) fishingCard.holder().getLuck();
            }
            return Math.random() > 0.02 * luck ? 2 : rewardMultiplier;
        }
        if (fishingCard.hasPerk(TradeSecrets.FISH_QUANTITY_BOAT) && Math.random() < 0.09) {
            rewardMultiplier = 2;
        }

        return rewardMultiplier;
    }


    private static void setLore(ItemStack stack, SpecimenData fish) {
        stack.set(DataComponentTypes.LORE, new LoreComponent(getDetailsAsLore(fish)));
    }

    private static List<Text> getDetailsAsLore(SpecimenData fish) {
        ArrayList<Text> result = new ArrayList<>();
        result.add(getQualityText(fish.quality()));
        result.add(getWeightText(fish.weight(), getWeightGrade(fish)));
        result.add(getLengthText(fish.length(), getLengthGrade(fish)));
        result.add(getCaughtBy(fish.caughtBy()));
        result.add(getCaughtAt(fish.caughtAt()));
        result.add(getValueText(fish.value()));
        return result;
    }


    public static boolean isFish(ItemStack itemStack) {
        return itemStack.isIn(FCTags.FISH_ITEM_TAG) || itemStack.get(FCComponents.SPECIMEN) != null;
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

    public static int getWeightGrade(SpecimenData fish){
        return getGrade(fish.weightPercentile());
    }
    public static int getLengthGrade(SpecimenData fish){
        return getGrade(fish.lengthPercentile());
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

    public static Optional<SpecimenData> getFishOnHook(IHookEntity hookEntity) {
        float luck = 0.5f;
        if (hookEntity.getFishingCard().holder() instanceof PlayerEntity playerEntity) {
            luck = (playerEntity.getBlockY() + 128) / 384f;
            luck *= (1 + playerEntity.getLuck());
        }

        if (Math.random() < 0.05 / luck) {
           return Optional.empty();
        }

        int attempts = 10;
        int maxWeight = hookEntity.maxWeight();
        for(int i = 0; i < attempts; i++) {
            SpecimenData hooked = SpecimenData.init(hookEntity);
            if (hooked.weight() > maxWeight) {
                return Optional.of(hooked);
            }
        }
        return Optional.empty();
    }

    public static int getTemperature (World world, BlockPos pos) {
        return world.getBiome(pos).value().doesNotSnow(pos) ? (world.getDimension().ultrawarm() ? 1 : 0) : -1;
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

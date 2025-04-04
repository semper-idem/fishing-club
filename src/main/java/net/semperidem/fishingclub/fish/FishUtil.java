package net.semperidem.fishingclub.fish;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.IHookEntity;
import net.semperidem.fishingclub.fish.specimen.SpecimenData;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.item.FishingNetItem;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.Enchantments;
import net.semperidem.fishingclub.registry.Items;
import net.semperidem.fishingclub.registry.Tags;
import net.semperidem.fishingclub.world.ChunkQuality;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class FishUtil {
    public static List<ItemStack> getFishingReward(SpecimenData fish, Card card){
        ArrayList<ItemStack> caughtFishStacks = new ArrayList<>();
        int count = getRewardMultiplier(card);
        ItemStack fishReward = fish.asItemStack();
        caughtFishStacks.add(fishReward);
        if (count == 1) {
            return caughtFishStacks;
        }
        for(int i = count - 1; i > 0; i--) {
            caughtFishStacks.add(fish.sibling().asItemStack());
        }
        return caughtFishStacks;
    }


private static ItemStack getFishingNet(PlayerEntity player, ItemStack fishStack) {
        for(int i = 0; i < 9; i++) {
            ItemStack stackInHotbar = player.getInventory().getStack(i);
            if (!(stackInHotbar.getItem() instanceof FishingNetItem fishingNetItem)) {
                continue;
            }
            if (fishingNetItem.canInsert(stackInHotbar, fishStack)) {
                return stackInHotbar;
            }
        }
        return Card.of(player).getFishingNet(fishStack);
    }

    public static ItemStack fishCaught(PlayerEntity player, SpecimenData fish){
        Card card = Card.of(player);
        card.fishCaught(fish);
        List<ItemStack> fishCaughtStacks = getFishingReward(fish, card);
        ItemStack fishStack = fishCaughtStacks.getFirst().copy();
        for(ItemStack fishCaughtStack : fishCaughtStacks) {
            ItemStack fishingNetStack = getFishingNet(player, fishCaughtStack);
            ChunkQuality.influence(player, ChunkQuality.PlayerInfluence.FISH_CAUGHT);
            if (!fishingNetStack.isEmpty() && ((FishingNetItem)Items.FISHING_NET).insertStack(fishingNetStack, fishCaughtStack, player)) {
                continue;
            }
            giveItemStack(player, fishCaughtStack);
        }
        return fishStack;
    }

    public static void fishCaughtAt(ServerPlayerEntity player, SpecimenData fish, BlockPos caughtAt) {
        Card card = Card.of(player);
        card.fishCaught(fish);
        ChunkQuality.influence(player, ChunkQuality.PlayerInfluence.EXPLOSION);
        throwRandomly(player.getWorld(), caughtAt, fish.asItemStack());
    }

    public static void giveReward(ServerPlayerEntity player, List<ItemStack> treasureReward){
        for(ItemStack reward : treasureReward) {
            giveItemStack(player, reward);
        }
    }

    private static void giveItemStack(PlayerEntity player, ItemStack itemStack){
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

    private static int getRewardMultiplier(Card card){
        int rewardMultiplier = 1;
        if (card.owner() == null) {
            return 1;
        }
        int luck = (int) card.owner().getLuck();
        if (card.knowsTradeSecret(TradeSecrets.FISH_WHISPERER)) {
            luck++;
        }
        while (Math.random() < 0.02f * luck) {
            rewardMultiplier++;
        }

        if (Math.random() < card.tradeSecretValue(TradeSecrets.BOAT_FISH_QUANTITY)) {
            rewardMultiplier++;
        }

        if (Math.random() < card.tradeSecretValue(TradeSecrets.FISH_QUANTITY)) {
            rewardMultiplier++;
        }

        return rewardMultiplier;
    }


    public static boolean isFish(ItemStack itemStack) {
        return itemStack.isIn(Tags.FISH_ITEM) || itemStack.get(Components.SPECIMEN_DATA) != null;
    }

    private static float getJunkChance(IHookEntity hookEntity) {
        float luck = 0.5f;
        Card card = hookEntity.getCard();
        if (card == null) {
            return 0.025f;
        }
        PlayerEntity owner = card.owner();
        luck *= (1 + owner.getLuck());
        if (card.knowsTradeSecret(TradeSecrets.FISH_WHISPERER)) {
            luck++;
        }
        luck *= (float) MathHelper.clamp((owner.getBlockY() + 128) / 192f, 0, 1.5);
        int curseOfClutterLevel = Enchantments.getEnchantmentLevel(owner, owner.getEquippedStack(EquipmentSlot.MAINHAND), Enchantments.CURSE_OF_CLUTTER);
        return (float) (0.05 * (1 + curseOfClutterLevel * 3) / luck);
    }

    public static Optional<SpecimenData> fishOnHook(IHookEntity hookEntity) {
       if (Math.random() < getJunkChance(hookEntity)) {
           return Optional.empty();
        }
        return SpecimenData.init(hookEntity);
    }

    public static int getTemperature (World world, BlockPos pos) {
        return world.getBiome(pos).value().doesNotSnow(pos, world.getSeaLevel()) ? (world.getDimension().ultrawarm() ? 1 : 0) : -1;
    }


    public static boolean hasFishingHat(PlayerEntity owner){
        return owner.getEquippedStack(EquipmentSlot.HEAD).isOf(Items.FISHER_HAT);
    }

    public static boolean hasFishingVest(PlayerEntity owner){
        final boolean[] result = {false};
        return owner.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.FISHER_VEST);
    }

    public static boolean hasProperFishingEquipment(PlayerEntity owner){
        return hasFishingHat(owner) && hasFishingVest(owner);
    }
}

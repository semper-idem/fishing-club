package net.semperidem.fishingclub.item;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.collection.Weighting;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.semperidem.fishingclub.registry.EnchantmentRegistry;
import net.semperidem.fishingclub.registry.ItemRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IllegalGoodsItem extends Item {
    private static final HashMap<Integer, ArrayList<Item>> tieredLootItems = new HashMap<>();
    private static final ArrayList<Item> tierOne = new ArrayList<>();
    private static final ArrayList<Item> tierTwo = new ArrayList<>();
    private static final ArrayList<Item> tierThree = new ArrayList<>();
    private static final ArrayList<Item> tierFour = new ArrayList<>();
    private static final ArrayList<Item> tierFive = new ArrayList<>();

    static {
        tierOne.addAll(List.of(Items.LEATHER_CHESTPLATE,Items.LEATHER_BOOTS,Items.LEATHER_HELMET,Items.LEATHER_LEGGINGS, Items.STONE_PICKAXE, Items.STONE_SWORD, Items.STONE_SHOVEL, Items.STONE_AXE, Items.STONE_HOE));
        tierTwo.addAll(List.of(Items.GOLDEN_CHESTPLATE,Items.GOLDEN_BOOTS,Items.GOLDEN_HELMET,Items.GOLDEN_LEGGINGS, Items.GOLDEN_PICKAXE, Items.GOLDEN_SWORD, Items.GOLDEN_SHOVEL, Items.GOLDEN_AXE, Items.GOLDEN_HOE));
        tierThree.addAll(List.of(Items.CHAINMAIL_CHESTPLATE,Items.CHAINMAIL_BOOTS,Items.CHAINMAIL_HELMET,Items.CHAINMAIL_LEGGINGS, Items.BOW, Items.CROSSBOW, Items.SHIELD, Items.FISHING_ROD));
        tierFour.addAll(List.of(Items.IRON_CHESTPLATE,Items.IRON_BOOTS,Items.IRON_HELMET,Items.IRON_LEGGINGS, Items.IRON_PICKAXE, Items.IRON_SWORD, Items.IRON_SHOVEL, Items.IRON_AXE, Items.IRON_HOE));
        tierFive.addAll(List.of(Items.DIAMOND_CHESTPLATE,Items.DIAMOND_BOOTS,Items.DIAMOND_HELMET,Items.DIAMOND_LEGGINGS, Items.DIAMOND_PICKAXE, Items.DIAMOND_SWORD, Items.DIAMOND_SHOVEL, Items.DIAMOND_AXE, Items.DIAMOND_HOE));


        tieredLootItems.put(1, tierOne);
        tieredLootItems.put(2, tierTwo);
        tieredLootItems.put(3, tierThree);
        tieredLootItems.put(4, tierFour);
        tieredLootItems.put(5, tierFive);
    }

    public IllegalGoodsItem(Settings settings) {
        super(settings);
    }


    @Override
    public ItemStack getDefaultStack() {
        ItemStack defaultStack = new ItemStack(this);
        this.setTier(defaultStack, 1);
        return defaultStack;
    }

    public static ItemStack getStackWithTier(int tier){
        ItemStack defaultStack = new ItemStack(ItemRegistry.ILLEGAL_GOODS);
        ((IllegalGoodsItem) ItemRegistry.ILLEGAL_GOODS).setTier(defaultStack, tier);
        return defaultStack;
    }

    public void setTier(ItemStack illegalGood, int tier){
        NbtCompound nbt = illegalGood.getOrCreateNbt();
        nbt.putInt("tier", tier);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        int tier = itemStack.getOrCreateNbt().getInt("tier");
        if (!world.isClient){
            ItemStack lootStack = generateLoot(tier);
            forbiddenEnchant(world.random, lootStack, (int) ((0.5f + Math.random()) * 8 * tier), true);
            if (EnchantmentHelper.getLevel(Enchantments.MENDING, lootStack) > 0) {
                removeMending(lootStack);
            }
            if (EnchantmentHelper.getLevel(EnchantmentRegistry.CURSE_OF_MORTALITY, lootStack) == 0) {
                lootStack.addEnchantment(EnchantmentRegistry.CURSE_OF_MORTALITY, 1);
            }
            if (!user.getAbilities().creativeMode){
                itemStack.decrement(1);
            }
            user.giveItemStack(lootStack);
            ((ServerWorld) world).spawnParticles(ParticleTypes.SOUL, user.getX(), user.getY(), user.getZ(), (int)(10 * Math.pow(tier, 2)), 0.5, 0.5, 0.5, 0.1);
        } else {
            user.swingHand(hand);
        }
        world.playSound(user, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, user.getSoundCategory(), 0.5f + 0.1f * tier, 0.1f);
        return super.use(world, user, hand);
    }

    private void removeMending(ItemStack lootStack){
        NbtList enchantmentsNbt = lootStack.getEnchantments();
        for(int i = 0; i < enchantmentsNbt.size(); i++) {
            NbtCompound enchantmentTag = enchantmentsNbt.getCompound(i);
            boolean isMending = enchantmentTag.getString("id").equals("minecraft:mending");
            if (isMending) {
                enchantmentsNbt.remove(i);
                return;
            }
        }
    }

    private ItemStack generateLoot(int tier) {
        float lootTier = (tier - 1) * 0.5f + 1;
        java.util.Random r = new java.util.Random();
        lootTier += (Math.abs(r.nextGaussian() * 1.5));
        int lootX = Math.max(1, Math.min(5, (int) Math.floor(lootTier)));
        ArrayList<Item> lootTierList = tieredLootItems.get(lootX);
        int lootY = r.nextInt(lootTierList.size());
        Item loot = lootTierList.get(lootY);
        ItemStack lootStack =  new ItemStack(loot);
        double durabilityPercentage = MathHelper.clamp(Math.sqrt((Math.abs(lootTier - lootX)) / (lootX + 1)), 0.1, 0.9);
        lootStack.setDamage((int) (lootStack.getMaxDamage() * (1 - durabilityPercentage)));
        return lootStack;
    }

    public static ItemStack forbiddenEnchant(Random random, ItemStack target, int level, boolean treasureAllowed) {
        List<EnchantmentLevelEntry> list = generateIllegalEnchantments(random, target, level, treasureAllowed);
        boolean bl = target.isOf(Items.BOOK);
        if (bl) {
            target = new ItemStack(Items.ENCHANTED_BOOK);
        }
        for (EnchantmentLevelEntry enchantmentLevelEntry : list) {
            if (bl) {
                EnchantedBookItem.addEnchantment(target, enchantmentLevelEntry);
                continue;
            }
            target.addEnchantment(enchantmentLevelEntry.enchantment, enchantmentLevelEntry.level);
        }
        return target;
    }

    public static List<EnchantmentLevelEntry> generateIllegalEnchantments(Random random, ItemStack stack, int level, boolean treasureAllowed) {
        ArrayList<EnchantmentLevelEntry> list = Lists.newArrayList();
        Item item = stack.getItem();
        int i = item.getEnchantability();
        if (i <= 0) {
            return list;
        }
        level += 1 + random.nextInt(i / 4 + 1) + random.nextInt(i / 4 + 1);
        float f = (random.nextFloat() + random.nextFloat() - 1.0f) * 0.15f;
        List<EnchantmentLevelEntry> list2 = EnchantmentHelper.getPossibleEntries(level = MathHelper.clamp(Math.round((float) level + (float) level * f), 1, Integer.MAX_VALUE), stack, treasureAllowed);
        if (!list2.isEmpty()) {
            Weighting.getRandom(random, list2).ifPresent(list::add);
            while (random.nextInt(50) <= level) {
                if (!list.isEmpty()) {
                    removeDuplicates(list2, Util.getLast(list));
                }

                if (list2.isEmpty()) break;
                Weighting.getRandom(random, list2).ifPresent(list::add);
                level /= 2;
            }
        }
        return list;
    }


    public static void removeDuplicates(List<EnchantmentLevelEntry> possibleEntries, EnchantmentLevelEntry pickedEntry) {
        possibleEntries.removeIf(enchantmentLevelEntry -> pickedEntry.enchantment == enchantmentLevelEntry.enchantment);

    }
}

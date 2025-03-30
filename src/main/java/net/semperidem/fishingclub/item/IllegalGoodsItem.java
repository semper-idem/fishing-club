package net.semperidem.fishingclub.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.semperidem.fishingclub.registry.Components;
import net.semperidem.fishingclub.registry.Enchantments;
import net.semperidem.fishingclub.registry.Items;
import net.semperidem.fishingclub.registry.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class IllegalGoodsItem extends Item {
  private static final HashMap<Integer, ArrayList<Item>> TIER_TO_LOOT = new HashMap<>();

  static {
    TIER_TO_LOOT.put(
        1,
        new ArrayList<>(
            List.of(
                net.minecraft.item.Items.LEATHER_CHESTPLATE,
                net.minecraft.item.Items.LEATHER_BOOTS,
                net.minecraft.item.Items.LEATHER_HELMET,
                net.minecraft.item.Items.LEATHER_LEGGINGS,
                net.minecraft.item.Items.STONE_PICKAXE,
                net.minecraft.item.Items.STONE_SWORD,
                net.minecraft.item.Items.STONE_SHOVEL,
                net.minecraft.item.Items.STONE_AXE,
                net.minecraft.item.Items.STONE_HOE)));

    TIER_TO_LOOT.put(
        2,
        new ArrayList<>(
            List.of(
                net.minecraft.item.Items.GOLDEN_CHESTPLATE,
                net.minecraft.item.Items.GOLDEN_BOOTS,
                net.minecraft.item.Items.GOLDEN_HELMET,
                net.minecraft.item.Items.GOLDEN_LEGGINGS,
                net.minecraft.item.Items.GOLDEN_PICKAXE,
                net.minecraft.item.Items.GOLDEN_SWORD,
                net.minecraft.item.Items.GOLDEN_SHOVEL,
                net.minecraft.item.Items.GOLDEN_AXE,
                net.minecraft.item.Items.GOLDEN_HOE)));

    TIER_TO_LOOT.put(
        3,
        new ArrayList<>(
            List.of(
                net.minecraft.item.Items.CHAINMAIL_CHESTPLATE,
                net.minecraft.item.Items.CHAINMAIL_BOOTS,
                net.minecraft.item.Items.CHAINMAIL_HELMET,
                net.minecraft.item.Items.CHAINMAIL_LEGGINGS,
                net.minecraft.item.Items.BOW,
                net.minecraft.item.Items.CROSSBOW,
                net.minecraft.item.Items.SHIELD,
                net.minecraft.item.Items.FISHING_ROD // silly
                )));

    TIER_TO_LOOT.put(
        4,
        new ArrayList<>(
            List.of(
                net.minecraft.item.Items.IRON_CHESTPLATE,
                net.minecraft.item.Items.IRON_BOOTS,
                net.minecraft.item.Items.IRON_HELMET,
                net.minecraft.item.Items.IRON_LEGGINGS,
                net.minecraft.item.Items.IRON_PICKAXE,
                net.minecraft.item.Items.IRON_SWORD,
                net.minecraft.item.Items.IRON_SHOVEL,
                net.minecraft.item.Items.IRON_AXE,
                net.minecraft.item.Items.IRON_HOE)));

    TIER_TO_LOOT.put(
        5,
        new ArrayList<>(
            List.of(
                net.minecraft.item.Items.DIAMOND_CHESTPLATE,
                net.minecraft.item.Items.DIAMOND_BOOTS,
                net.minecraft.item.Items.DIAMOND_HELMET,
                net.minecraft.item.Items.DIAMOND_LEGGINGS,
                net.minecraft.item.Items.DIAMOND_PICKAXE,
                net.minecraft.item.Items.DIAMOND_SWORD,
                net.minecraft.item.Items.DIAMOND_SHOVEL,
                net.minecraft.item.Items.DIAMOND_AXE,
                net.minecraft.item.Items.DIAMOND_HOE)));
  }

  public IllegalGoodsItem(Settings settings) {
    super(settings);
  }

  public static HashMap<Integer, ArrayList<Item>> getPossibleLoot() {
    return new HashMap<>(TIER_TO_LOOT);
  }

  public static ItemStack getStackWithTier(int tier) {
    ItemStack defaultStack = new ItemStack(Items.ILLEGAL_GOODS);
    defaultStack.set(Components.TIER, tier);
    return defaultStack;
  }

  @Override
  public ActionResult use(World world, PlayerEntity user, Hand hand) {
    ItemStack itemStack = user.getStackInHand(hand);
    if (world.isClient) {
      user.swingHand(hand);
      return ActionResult.CONSUME;
    }
    int tier = itemStack.getOrDefault(Components.TIER, 1);

    if (!user.getAbilities().creativeMode) {
      itemStack.decrement(1);
    }

    user.giveItemStack(generateLoot(world, user, tier));
    ((ServerWorld) world)
        .spawnParticles(
            ParticleTypes.SOUL,
            user.getX(),
            user.getY(),
            user.getZ(),
            (int) (10 * Math.pow(tier, 2)),
            0.5,
            0.5,
            0.5,
            0.1);
    world.playSound(
        user,
        user.getBlockPos(),
        SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME,
        user.getSoundCategory(),
        0.5f + 0.1f * tier,
        0.1f);
    return super.use(world, user, hand);
  }

  private ItemStack generateLoot(World world, PlayerEntity user, int tier) {
    float lootTier = (tier - 1) * 0.5f + 1;
    java.util.Random r = new java.util.Random();
    lootTier += (Math.abs(r.nextGaussian() * 1.5));
    int lootX = Math.max(1, Math.min(5, (int) Math.floor(lootTier)));
    ArrayList<Item> lootTierList = TIER_TO_LOOT.get(lootX);
    int lootY = r.nextInt(lootTierList.size());
    Item loot = lootTierList.get(lootY);
    ItemStack lootStack = new ItemStack(loot);
    double durabilityPercentage =
        MathHelper.clamp(Math.sqrt((Math.abs(lootTier - lootX)) / (lootX + 1)), 0.1, 0.9);
    lootStack.setDamage((int) (lootStack.getMaxDamage() * (1 - durabilityPercentage)));
    return enchantLoot(lootStack, world, user, tier);
  }

  private ItemStack enchantLoot(ItemStack lootStack, World world, PlayerEntity user, int tier) {
    DynamicRegistryManager dynamicRegistryManager = world.getRegistryManager();
    Optional<Registry<Enchantment>> enchantmentRegistry =
        dynamicRegistryManager.getOptional(RegistryKeys.ENCHANTMENT);
//
//    ItemStack normalLootStack =
//        EnchantmentHelper.enchant(
//            user.getRandom(),
//            lootStack,
//            tier * 15,
//            dynamicRegistryManager,
//            enchantmentRegistry(EnchantmentTags.NON_TREASURE));
//
//    ItemStack treasureLootStack =
//        EnchantmentHelper.enchant(
//            user.getRandom(),
//            normalLootStack,
//            tier * 10,
//            dynamicRegistryManager,
//            enchantmentRegistry.getEntryList(EnchantmentTags.TREASURE));
//
//    ItemStack cursedLootStack =
//        EnchantmentHelper.enchant(
//            user.getRandom(),
//            treasureLootStack,
//            tier * 5,
//            dynamicRegistryManager,
//            enchantmentRegistry.getEntryList(EnchantmentTags.CURSE));
//
//    EnchantmentHelper.apply(
//        cursedLootStack,
//        components ->
//            components.remove(enchantment -> enchantment.isIn(Tags.ENCHANTMENT_REPAIR_TAG)));
//
//    world
//        .getRegistryManager()
//        .getOptional(RegistryKeys.ENCHANTMENT).
//        .getEntry(Enchantments.CURSE_OF_MORTALITY.getValue())
//        .ifPresent(curseOfMortality -> cursedLootStack.addEnchantment(curseOfMortality, 1)));
    return lootStack;
  }
}

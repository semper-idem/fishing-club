package net.semperidem.fishing_club.item;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.semperidem.fishing_club.registry.FCComponents;
import net.semperidem.fishing_club.registry.FCEnchantments;
import net.semperidem.fishing_club.registry.FCItems;
import net.semperidem.fishing_club.registry.FCTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IllegalGoodsItem extends Item {
  private static final HashMap<Integer, ArrayList<Item>> TIER_TO_LOOT = new HashMap<>();

  static {
    TIER_TO_LOOT.put(
        1,
        new ArrayList<>(
            List.of(
                Items.LEATHER_CHESTPLATE,
                Items.LEATHER_BOOTS,
                Items.LEATHER_HELMET,
                Items.LEATHER_LEGGINGS,
                Items.STONE_PICKAXE,
                Items.STONE_SWORD,
                Items.STONE_SHOVEL,
                Items.STONE_AXE,
                Items.STONE_HOE)));

    TIER_TO_LOOT.put(
        2,
        new ArrayList<>(
            List.of(
                Items.GOLDEN_CHESTPLATE,
                Items.GOLDEN_BOOTS,
                Items.GOLDEN_HELMET,
                Items.GOLDEN_LEGGINGS,
                Items.GOLDEN_PICKAXE,
                Items.GOLDEN_SWORD,
                Items.GOLDEN_SHOVEL,
                Items.GOLDEN_AXE,
                Items.GOLDEN_HOE)));

    TIER_TO_LOOT.put(
        3,
        new ArrayList<>(
            List.of(
                Items.CHAINMAIL_CHESTPLATE,
                Items.CHAINMAIL_BOOTS,
                Items.CHAINMAIL_HELMET,
                Items.CHAINMAIL_LEGGINGS,
                Items.BOW,
                Items.CROSSBOW,
                Items.SHIELD,
                Items.FISHING_ROD // silly
                )));

    TIER_TO_LOOT.put(
        4,
        new ArrayList<>(
            List.of(
                Items.IRON_CHESTPLATE,
                Items.IRON_BOOTS,
                Items.IRON_HELMET,
                Items.IRON_LEGGINGS,
                Items.IRON_PICKAXE,
                Items.IRON_SWORD,
                Items.IRON_SHOVEL,
                Items.IRON_AXE,
                Items.IRON_HOE)));

    TIER_TO_LOOT.put(
        5,
        new ArrayList<>(
            List.of(
                Items.DIAMOND_CHESTPLATE,
                Items.DIAMOND_BOOTS,
                Items.DIAMOND_HELMET,
                Items.DIAMOND_LEGGINGS,
                Items.DIAMOND_PICKAXE,
                Items.DIAMOND_SWORD,
                Items.DIAMOND_SHOVEL,
                Items.DIAMOND_AXE,
                Items.DIAMOND_HOE)));
  }

  public IllegalGoodsItem(Settings settings) {
    super(settings);
  }

  public static HashMap<Integer, ArrayList<Item>> getPossibleLoot() {
    return new HashMap<>(TIER_TO_LOOT);
  }

  public static ItemStack getStackWithTier(int tier) {
    ItemStack defaultStack = new ItemStack(FCItems.ILLEGAL_GOODS);
    defaultStack.set(FCComponents.TIER, tier);
    return defaultStack;
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    ItemStack itemStack = user.getStackInHand(hand);
    if (world.isClient) {
      user.swingHand(hand);
      return TypedActionResult.consume(itemStack);
    }
    int tier = itemStack.getOrDefault(FCComponents.TIER, 1);

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
    Registry<Enchantment> enchantmentRegistry =
        dynamicRegistryManager.get(RegistryKeys.ENCHANTMENT);
    ItemStack normalLootStack =
        EnchantmentHelper.enchant(
            user.getRandom(),
            lootStack,
            tier * 15,
            dynamicRegistryManager,
            enchantmentRegistry.getEntryList(EnchantmentTags.NON_TREASURE));
    ItemStack treasureLootStack =
        EnchantmentHelper.enchant(
            user.getRandom(),
            normalLootStack,
            tier * 10,
            dynamicRegistryManager,
            enchantmentRegistry.getEntryList(EnchantmentTags.TREASURE));
    ItemStack cursedLootStack =
        EnchantmentHelper.enchant(
            user.getRandom(),
            treasureLootStack,
            tier * 5,
            dynamicRegistryManager,
            enchantmentRegistry.getEntryList(EnchantmentTags.CURSE));

    EnchantmentHelper.apply(
        cursedLootStack,
        components ->
            components.remove(enchantment -> enchantment.isIn(FCTags.ENCHANTMENT_REPAIR_TAG)));

    world
        .getRegistryManager()
        .get(RegistryKeys.ENCHANTMENT)
        .getEntry(FCEnchantments.CURSE_OF_MORTALITY.getValue())
        .ifPresent(curseOfMortality -> cursedLootStack.addEnchantment(curseOfMortality, 1));
    return cursedLootStack;
  }
}

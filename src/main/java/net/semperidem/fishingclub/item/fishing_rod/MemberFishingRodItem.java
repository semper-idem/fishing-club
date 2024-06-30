package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.components.PartItem;
import net.semperidem.fishingclub.item.fishing_rod.components.RodConfigurationComponent;
import net.semperidem.fishingclub.registry.ComponentRegistry;

public class MemberFishingRodItem extends FishingRodItem {
  private final String CAST_CHARGE_TAG = "lastCastCharge";
  private final String LINE_LENGTH = "lineLenght";

  public MemberFishingRodItem(Settings settings) {
    super(settings);
  }

  @Override
  public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
    ItemStack fishingRod = user.getStackInHand(hand);

    if (isCast(user)) {
      reelRod(world, user, hand, fishingRod);
      return TypedActionResult.success(fishingRod, world.isClient);
    }

    if (!canCast(fishingRod)) {
      return TypedActionResult.fail(fishingRod);
    }

    if (FishingCard.of(user).hasPerk(FishingPerks.BOBBER_THROW_CHARGE)) {
      user.setCurrentHand(hand); // Sets hand as "active"
      return TypedActionResult.success(fishingRod, world.isClient);
    }

    castHook(world, user, fishingRod);
    return TypedActionResult.success(fishingRod, world.isClient);
  }

  @Override
  public void onStoppedUsing(
      ItemStack fishingRod, World world, LivingEntity user, int remainingUseTicks) {
    castHook(world, (PlayerEntity) user, fishingRod);
  }

  private void castHook(World world, PlayerEntity user, ItemStack fishingRod) {
    if (fishingRod.getMaxDamage() - fishingRod.getDamage() == 1) {
      return;
    }
    if (!world.isClient) {
      RodConfigurationComponent configuration = getRodConfiguration(fishingRod);
      float power = 1 + (1 - getChargePower(user.getItemUseTime())) * 0.15f;
      fishingRod.set(ComponentRegistry.CAST_POWER, power);
      configuration.damage(2, PartItem.DamageSource.CAST, user);
      world.spawnEntity(new HookEntity(user, world, configuration));
    }
    world.playSound(
        null,
        user.getX(),
        user.getY(),
        user.getZ(),
        SoundEvents.ENTITY_FISHING_BOBBER_THROW,
        SoundCategory.NEUTRAL,
        0.5f,
        0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
    user.incrementStat(Stats.USED.getOrCreateStat(this));
    user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
  }

  private void serverReelRod(World world, PlayerEntity user, Hand hand, ItemStack itemStack) {
    user.fishHook.use(itemStack);
  }

  private void reelRod(World world, PlayerEntity user, Hand hand, ItemStack fishingRod) {
    serverReelRod(world, user, hand, fishingRod);
    world.playSound(
        null,
        user.getX(),
        user.getY(),
        user.getZ(),
        SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE,
        SoundCategory.NEUTRAL,
        1.0f,
        0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
    user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
  }

  public boolean canCast(ItemStack fishingRod) {
    return fishingRod.getMaxDamage() - fishingRod.getDamage() > 1;
  }

  public RodConfigurationComponent getRodConfiguration(ItemStack fishingRod) {
    return fishingRod.get(ComponentRegistry.ROD_CONFIGURATION);
  }

  @Override
  public boolean isUsedOnRelease(ItemStack stack) {
    return true;
  }

  public static int getChargePower(int usageTick) {
    return Math.max(1, Math.min(5, (usageTick + 40) / 40));
  }

  @Override
  public int getMaxUseTime(ItemStack stack, LivingEntity user) {
    return 12000;
  }

  private boolean isCast(PlayerEntity user) {
    return user.fishHook != null;
  }

  @Override
  public UseAction getUseAction(ItemStack stack) {
    return UseAction.SPEAR;
  }

  public boolean holdsFishingRod(PlayerEntity playerEntity) {
    return playerEntity.getMainHandStack().isOf(this) || playerEntity.getOffHandStack().isOf(this);
  }
}

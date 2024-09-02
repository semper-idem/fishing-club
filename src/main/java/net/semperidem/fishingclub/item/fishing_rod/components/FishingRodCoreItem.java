package net.semperidem.fishingclub.item.fishing_rod.components;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityEquipmentUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.TradeSecrets;
import net.semperidem.fishingclub.registry.FCComponents;

import java.util.List;

public class FishingRodCoreItem extends FishingRodItem {
    ItemStat castPower = ItemStat.MULTIPLIER_T3;
    ItemStat bobberControlCeiling;
    ItemStat fishControlCeiling;
    int minOperatingTemperature = 0;
    int maxOperatingTemperature = 0;
    int luck = 0;
    int weightClass = 0;

    public FishingRodCoreItem(Settings settings) {
        super(settings);
    }



    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        ItemStack fishingRod = user.getStackInHand(hand);
        long time = world.getTime();
        if (fishingRod.getOrDefault(FCComponents.EXPIRATION_TIME, time).intValue() < time) {
            fishingRod.damage(fishingRod.getMaxDamage() + 1, user, EquipmentSlot.MAINHAND);
        }
        boolean canCast = fishingRod.getOrDefault(FCComponents.ROD_CONFIGURATION, RodConfiguration.getDefault()).attributes().canCast();
        if (!canCast) {
            user.sendMessage(Text.of("Can't use without line"), true);
            return TypedActionResult.fail(fishingRod);
        }

        if (FishingCard.of(user).isMember()) {
            return super.use(world, user, hand);
        }

        if (user.fishHook != null) {
            this.reelRod(world, user, fishingRod);
            return TypedActionResult.success(fishingRod, world.isClient());
        }
        if (fishingRod.getOrDefault(FCComponents.BROKEN, false)) {
            user.sendMessage(Text.of("Broken."), true);
            return TypedActionResult.fail(fishingRod);
        }
        if (hasNoFishingRod(user)) {
            return TypedActionResult.fail(fishingRod);
        }

        if (FishingCard.of(user).hasPerk(TradeSecrets.BOBBER_THROW_CHARGE)) {
            user.setCurrentHand(hand); // Sets hand as "active"
            return TypedActionResult.consume(fishingRod);
        }

        this.castHook(world, user, fishingRod);
        return TypedActionResult.success(fishingRod, world.isClient());
    }

    @Override
    public void onStoppedUsing(ItemStack fishingRod, World world, LivingEntity user, int remainingUseTicks) {
        this.castHook(world, (PlayerEntity) user, fishingRod);
    }

    private void updateClientInventory(ServerPlayerEntity user) {
        List<Pair<EquipmentSlot, ItemStack>> list = Lists.newArrayList();
        list.add(Pair.of(EquipmentSlot.MAINHAND, user.getEquippedStack(EquipmentSlot.MAINHAND)));
        user.networkHandler.sendPacket(new EntityEquipmentUpdateS2CPacket(user.getId(), list));
    }

    private void castHook(World world, PlayerEntity user, ItemStack fishingRod) {
        if (!world.isClient) {
            float power = 1 + (1 - getChargePower(user.getItemUseTime())) * (user.isSneaking() ? 1 : -1) * 0.15f;
            fishingRod.set(FCComponents.CAST_POWER, power);
            fishingRod.damage(1, user, EquipmentSlot.MAINHAND);
            if (fishingRod.getDamage() >= fishingRod.getMaxDamage()) {
               RodConfiguration.dropContent(user, fishingRod);
            }
            updateClientInventory((ServerPlayerEntity) user);
            world.spawnEntity(new HookEntity(user, world, fishingRod));
        }
        world.playSound(
                user,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_FISHING_BOBBER_THROW,
                SoundCategory.NEUTRAL,
                0.5f,
                0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        );
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
    }

    private void reelRod(World world, PlayerEntity user, ItemStack fishingRod) {
        if (user.fishHook == null) {
            return;
        }
        user.fishHook.use(fishingRod);
        world.playSound(
                user,
                user.getX(),
                user.getY(),
                user.getZ(),
                SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE,
                SoundCategory.NEUTRAL,
                1.0f,
                0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f)
        );
        user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;//FishingCard.of(user).hasPerk(FishingPerks.BOBBER_THROW_CHARGE) untested but should be correct
    }

    public static int getChargePower(int usageTick) {
        return Math.max(1, Math.min(5, (usageTick + 40) / 40));
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 12000;
    }

    public int scrollLineBy(PlayerEntity user, ItemStack fishingRod, int amount) {
        RodConfiguration configuration = RodConfiguration.of(fishingRod);
        int maxLineLength = configuration.attributes().maxLineLength();
        int length = MathHelper.clamp((fishingRod.getOrDefault(FCComponents.LINE_LENGTH, maxLineLength) + amount), 4, maxLineLength);
        fishingRod.set(FCComponents.LINE_LENGTH, length);
        user.sendMessage(Text.literal("Line length:" + length), true);
        return length;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public boolean hasNoFishingRod(PlayerEntity playerEntity) {//todo verify how is this possible and if it's even needed
        return !(playerEntity.getMainHandStack().getItem() instanceof FishingRodCoreItem);
    }

    public int maxWeight() {
        return WEIGHT_CLASS[weightClass];
    }

    public FishingRodCoreItem weightClass(int weightClass) {
        this.weightClass = weightClass;
        return this;
    }

    public FishingRodCoreItem castPower(ItemStat castPower) {
        this.castPower = castPower;
        return this;
    }


    public float castPower() {
        return this.castPower.value;
    }

    public int minOperatingTemperature() {
        return this.minOperatingTemperature;
    }

    public int maxOperatingTemperature() {
        return this.maxOperatingTemperature;
    }

    public FishingRodCoreItem minOperatingTemperature(int minOperatingTemperature) {
        this.minOperatingTemperature = minOperatingTemperature;
        return this;
    }

    public FishingRodCoreItem maxOperatingTemperature(int maxOperatingTemperature) {
        this.maxOperatingTemperature = maxOperatingTemperature;
        return this;
    }

    public FishingRodCoreItem luck(int luck) {
        this.luck = luck;
        return this;
    }

    public FishingRodCoreItem fishControlCeiling(ItemStat fishControl) {
        this.fishControlCeiling = fishControl;
        return this;
    }


    public FishingRodCoreItem bobberControlCeiling(ItemStat bobberControl) {

        this.bobberControlCeiling = bobberControl;
        return this;
    }


    public static int[] WEIGHT_CLASS = {0,5,25,50,100,999999};
}

package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;

import java.util.ArrayList;

public class MemberFishingRodItem extends FishingRodItem {
    public MemberFishingRodItem(Settings settings) {
        super(settings);
    }



    @Override
        public ItemStack getDefaultStack() {
        return FishingRodUtil.getBasicRod();
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int power = FishingRodUtil.getFishingRodChargePower(getMaxUseTime(stack) - remainingUseTicks);
        castHook(world, (PlayerEntity) user, power, stack);
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 1200;
    }

    private void castHook(World world, PlayerEntity user, int power, ItemStack fishingRod){
        if (fishingRod.getMaxDamage() - fishingRod.getDamage() == 1) return;
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            world.spawnEntity(new CustomFishingBobberEntity(user, world, fishingRod, power));
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
    }



    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        try {

            ItemStack fireworkStack = Items.FIREWORK_ROCKET.getDefaultStack();
            NbtCompound nbt = fireworkStack.getOrCreateNbt();
            NbtList explosionsNbt = new NbtList();
            NbtCompound explosionNbt = new NbtCompound();
            explosionNbt.putByte("Type", (byte) FireworkRocketItem.Type.valueOf("SMALL_BALL").getId());
            NbtList colors = new NbtList();
            colors.add(NbtInt.of(0xF9FFFE));
            NbtList fadeColors = new NbtList();
            int color = DyeColor.WHITE.getFireworkColor();
            fadeColors.add(NbtInt.of(color));
            explosionNbt.putIntArray(FireworkRocketItem.COLORS_KEY, new int[]{color});
            explosionNbt.putIntArray(FireworkRocketItem.FADE_COLORS_KEY,  new int[]{color});
            explosionNbt.putBoolean(FireworkRocketItem.FLICKER_KEY, true);
            explosionNbt.putBoolean(FireworkRocketItem.TRAIL_KEY, true);
            explosionsNbt.add(explosionNbt);
            NbtCompound fireworkNbt = new NbtCompound();
            fireworkNbt.put(FireworkRocketItem.EXPLOSIONS_KEY, explosionsNbt);
            fireworkNbt.putByte(FireworkRocketItem.FLIGHT_KEY, (byte)2);
            nbt.put(FireworkRocketItem.FIREWORKS_KEY, fireworkNbt);
            fireworkStack.setNbt(nbt);
            Items.FIREWORK_ROCKET.appendTooltip(fireworkStack, world, new ArrayList<>(), null);
            System.out.println(fireworkNbt);
            user.giveItemStack(fireworkStack);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TypedActionResult.consume(user.getStackInHand(hand));
        /*
        ItemStack fishingRod = user.getStackInHand(hand);

        if (isCasting(user)) {
            reelRod(world,user,hand, fishingRod);
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        boolean hasBobberChargePerk;
        if (world.isClient) {
            hasBobberChargePerk = FishingClubClient.getPerks().containsKey(FishingPerks.BOBBER_THROW_CHARGE.getName());
        } else {
            hasBobberChargePerk = FishingCard.getPlayerCard(user).hasPerk(FishingPerks.BOBBER_THROW_CHARGE);
        }
        if (!hasBobberChargePerk) {
            castHook(world, user, 1, fishingRod);
            float damageChance = FishingRodUtil.getDamageChance(fishingRod);
            if (Math.random() < damageChance && fishingRod.getDamage() < fishingRod.getMaxDamage() - 1) {
                fishingRod.damage(1, user, (e) -> {
                    e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                });
            }
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        user.setCurrentHand(hand); //Logic continues in onStoppedUsing
        return TypedActionResult.consume(user.getStackInHand(hand));

         */
    }

    private boolean isCasting(PlayerEntity user){
        return user.fishHook != null;
    }

    private void serverReelRod(World world, PlayerEntity user, Hand hand, ItemStack itemStack){
        if (!world.isClient) {
            int i = user.fishHook.use(itemStack);
            itemStack.damage(i, user, playerEntity -> playerEntity.sendToolBreakStatus(hand));
        }
    }

    public void damageRodPart(ItemStack rod,  FishingRodPartType partType){
        ItemStack partToDamage = FishingRodPartController.getPart(rod, partType);
        int damage = partToDamage.getDamage();
        partToDamage.setDamage(damage + 1);
        if (partToDamage.getDamage() >= partToDamage.getMaxDamage()) {
            FishingRodPartController.removePart(rod, partType);
        }
    }

    private void reelRod(World world, PlayerEntity user, Hand hand, ItemStack fishingRod){
        serverReelRod(world, user, hand, fishingRod);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

}

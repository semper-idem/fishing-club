package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.FishingCardManager;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;

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
            boolean boatFishing = user.getVehicle() instanceof BoatEntity;
            world.spawnEntity(new CustomFishingBobberEntity(user, world, fishingRod, power, FishingCardManager.getPlayerCard((ServerPlayerEntity) user), boatFishing));
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
    }

    private float getDamageChance(ItemStack fishingRod) {
        float coreDamageChance = FishingRodUtil.getRodDamageChance(fishingRod);

        if (!FishingRodUtil.hasHandmadeUsage(fishingRod)) return coreDamageChance;
        FishingRodUtil.consumeHandmadeUsage(fishingRod);

        return coreDamageChance * 0.5f;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack fishingRod = user.getStackInHand(hand);

        if (isCasting(user)) {
            reelRod(world,user,hand, fishingRod);
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        FishingCard fishingCard = world.isClient ? FishingClubClient.CLIENT_INFO : FishingCardManager.getPlayerCard((ServerPlayerEntity) user);
        if (!fishingCard.hasPerk(FishingPerks.BOBBER_THROW_CHARGE)) {
            castHook(world, user, 1, fishingRod);
            float damageChance = getDamageChance(fishingRod);
            if (Math.random() < damageChance && fishingRod.getDamage() < fishingRod.getMaxDamage() - 1) {
                fishingRod.damage(1, user, (e) -> {
                    e.sendEquipmentBreakStatus(EquipmentSlot.MAINHAND);
                });
            }
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        user.setCurrentHand(hand); //Logic continues in onStoppedUsing
        return TypedActionResult.consume(user.getStackInHand(hand));
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

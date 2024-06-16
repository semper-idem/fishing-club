package net.semperidem.fishingclub.item.fishing_rod;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
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
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodConfiguration;
import net.semperidem.fishingclub.registry.ItemRegistry;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class MemberFishingRodItem extends FishingRodItem {
    Cache<String, FishingRodConfiguration> cachedRodConfiguration = CacheBuilder.newBuilder()
            .maximumSize(128)
            .expireAfterWrite(3, TimeUnit.MINUTES)
            .build();

    public final String ID_TAG = "rodId";
    public final String CAST_POWER_TAG = "lastCastPower";

    public MemberFishingRodItem(Settings settings) {
        super(settings);
    }

    public float getPower(ItemStack itemStack) {
        return itemStack.getOrCreateNbt().getFloat(CAST_POWER_TAG);
    }

    public void setPower(ItemStack itemStack, float power) {
        itemStack.getOrCreateNbt().putFloat(CAST_POWER_TAG, power);
    }

    @Override
        public ItemStack getDefaultStack() {
        ItemStack defaultRod = new ItemStack(ItemRegistry.MEMBER_FISHING_ROD);
        FishingRodConfiguration configuration = FishingRodConfiguration.getDefault();
        configuration.setNbt(defaultRod);
        return defaultRod;
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        FishingRodConfiguration configuration = getRodConfiguration(stack);
        float power = (FishingRodUtil.getFishingRodChargePower(getMaxUseTime(stack) - remainingUseTicks) * configuration.getCastPower());
        castHook(world, (PlayerEntity) user, power, stack);
    }

    public String getIdentifier(ItemStack rodStack) {
        NbtCompound nbtCompound = rodStack.getOrCreateNbt();
        if (nbtCompound.contains(ID_TAG)) {
            return nbtCompound.getUuid(ID_TAG).toString();
        }
        UUID rodId = UUID.randomUUID();
        nbtCompound.putUuid(ID_TAG, rodId);
        return rodId.toString();
    }

    public void invalidateCache() {
        cachedRodConfiguration.invalidateAll();
    }

    public FishingRodConfiguration getRodConfiguration(ItemStack rodStack) {
        try {
            String identifier = getIdentifier(rodStack);
            return cachedRodConfiguration.get(identifier, () -> {
                FishingRodConfiguration fishingRodConfiguration = new FishingRodConfiguration(rodStack);
                cachedRodConfiguration.put(identifier, fishingRodConfiguration);
                return fishingRodConfiguration;
            });
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return FishingRodConfiguration.getDefault();
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 6000;
    }

    private void castHook(World world, PlayerEntity user, float power, ItemStack fishingRod){
        if (fishingRod.getMaxDamage() - fishingRod.getDamage() == 1) return;
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            setPower(fishingRod, power);
            world.spawnEntity(new CustomFishingBobberEntity(user, world, fishingRod));
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
    }



    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
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

package net.semperidem.fishingclub.item;

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
import net.semperidem.fishingclub.client.game.FishGameLogic;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;
import net.semperidem.fishingclub.fisher.FisherInfoDB;
import net.semperidem.fishingclub.fisher.FishingPerks;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomFishingRod extends FishingRodItem {
    HashMap<FishingRodPartItem.PartType, ItemStack> customParts = new HashMap<>();
    public CustomFishingRod(Settings settings) {
        super(settings);
    }

    @Override
        public ItemStack getDefaultStack() {
        ItemStack defaultStack = new ItemStack(this);
        NbtCompound partsNbt = new NbtCompound();
        defaultStack.setNbt(partsNbt);
        addPart(defaultStack, FishingRodPartItems.CORE_BAMBOO.getDefaultStack(), FishingRodPartItem.PartType.CORE);
        addPart(defaultStack, FishingRodPartItems.LINE_WOOL_THREAD.getDefaultStack(), FishingRodPartItem.PartType.LINE);
        return defaultStack;
    }

    public boolean hasPart(ItemStack rodStack, FishingRodPartItem.PartType partType){
        NbtCompound rodNbt = rodStack.getNbt();
        if (rodNbt.contains("parts")) {
            NbtCompound partsNbt = rodNbt.getCompound("parts");
            if (partsNbt.contains(partType.name())) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getPart(ItemStack rodStack, FishingRodPartItem.PartType partType){
        NbtCompound rodNbt = rodStack.getNbt();
        if (rodNbt.contains("parts")) {
            NbtCompound partsNbt = rodNbt.getCompound("parts");
            if (partsNbt.contains(partType.name())) {
                NbtCompound partNbt = partsNbt.getCompound(partType.name());
                if (partNbt.contains("key")) {
                    FishingRodPartItem partItem = FishingRodPartItems.KEY_TO_PART_MAP.get(partNbt.getString("key"));
                    ItemStack partStack = partItem.getDefaultStack();
                    partStack.setNbt(partNbt);
                    return partStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public ItemStack addPart(ItemStack rodStack, ItemStack partStack, FishingRodPartItem.PartType partType) {
        NbtCompound rodNbt;
        if (!rodStack.hasNbt()) {
            rodNbt = new NbtCompound();
            rodNbt.put("parts", new NbtCompound());
            rodStack.setNbt(rodNbt);
        } else {
            rodNbt = rodStack.getNbt();
        }
        ItemStack replacedStack = ItemStack.EMPTY;
        if (!rodNbt.contains("parts")) {
            rodNbt.put("parts", new NbtCompound());
        }
        NbtCompound partsNbt = rodNbt.getCompound("parts");
        if (partsNbt.contains(partType.name())) {
            replacedStack = removePart(rodStack, partType);
        }
        FishingRodPartItem partItem = (FishingRodPartItem) partStack.getItem();
        partStack.getNbt().putString("key", partItem.getKey());
        partsNbt.put(partItem.getPartType().name(), partStack.getNbt());
        return replacedStack;
    }

    public ArrayList<ItemStack> getRodParts(ItemStack rodStack){
        ArrayList<ItemStack> rodParts = new ArrayList<>();
        if (!rodStack.hasNbt()) {
            return rodParts;
        }
        NbtCompound rodTag = rodStack.getNbt();
        if (!rodTag.contains("parts")) {
            return rodParts;
        }
        NbtCompound partsTag = rodTag.getCompound("parts");
        for(FishingRodPartItem.PartType partType : FishingRodPartItem.PartType.values()) {
            if (!partsTag.contains(partType.name())) {
                continue;
            }
            rodParts.add(FishingRodPartItems.getStackFromCompound(partsTag.getCompound(partType.name())));
        }

        return rodParts;
    }

    public ItemStack removePart(ItemStack rodStack, FishingRodPartItem.PartType slot){
        NbtCompound rodNbt = rodStack.getNbt();
        if (rodNbt.contains("parts")) {
            NbtCompound partsNbt = rodNbt.getCompound("parts");
            if (partsNbt.contains(slot.name())) {
                NbtCompound partNbt = partsNbt.getCompound(slot.name());
                if (partNbt.contains("key")) {
                    FishingRodPartItem partItem = FishingRodPartItems.KEY_TO_PART_MAP.get(partNbt.getString("key"));
                    ItemStack partStack = partItem.getDefaultStack();
                    partStack.setNbt(partNbt);
                    partsNbt.remove(slot.name());
                    return partStack;
                }
            }
        }
        return ItemStack.EMPTY;
    }

    public void changePart(ItemStack partStack){
        FishingRodPartItem partItem = (FishingRodPartItem) partStack.getItem();
        if (customParts.containsKey(partItem.getPartType())) {
            customParts.remove(partItem.getPartType());
            //Drop item
        }
        customParts.put(partItem.getPartType(), partStack);
        //Consume item
    }

    public HashMap<FishingRodPartItem.PartType, ItemStack> getParts(){
        return customParts;
    }

    public float getStat(FishGameLogic.Stat stat){
        return getStat(stat, customParts);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int power = getPower(getMaxUseTime(stack) - remainingUseTicks);
        castHook(world, (PlayerEntity) user, power);
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 1200;
    }

    private void castHook(World world, PlayerEntity user, int power){
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            world.spawnEntity(new CustomFishingBobberEntity(user, world, this, power));
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (isCasting(user)) {
            reelRod(world,user,hand);
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        if (!FisherInfoDB.hasPerk(user, FishingPerks.BOBBER_THROW_CHARGE)) {
            castHook(world, user, 1);
            return TypedActionResult.success(user.getStackInHand(hand));
        }

        user.setCurrentHand(hand); //Logic continues in onStoppedUsing
        return TypedActionResult.consume(user.getStackInHand(hand));
    }

    private boolean isCasting(PlayerEntity user){
        return user.fishHook != null;
    }

    private void serverReelRod(World world, PlayerEntity user, Hand hand){
        if (!world.isClient) {
            ItemStack itemStack = user.getStackInHand(hand);
            int i = user.fishHook.use(itemStack);
            itemStack.damage(i, user, playerEntity -> playerEntity.sendToolBreakStatus(hand));
        }
    }

    private void reelRod(World world, PlayerEntity user, Hand hand){
        serverReelRod(world, user, hand);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    //TODO MOVE TO UTIL
    public static float getStat(FishGameLogic.Stat stat, HashMap<FishingRodPartItem.PartType, ItemStack> customParts){
        float result = 0;
        for(ItemStack partStack : customParts.values()) {
            FishingRodPartItem partItem = (FishingRodPartItem) partStack.getItem();
            result += partItem.getStatBonuses().getOrDefault(stat, 0f);
        }
        return result;
    }

    public static int getPower(int usageTick){
        return Math.max(1, Math.min(5, (usageTick + 60) / 60));
    }
}

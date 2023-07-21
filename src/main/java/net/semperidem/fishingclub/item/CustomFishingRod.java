package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.semperidem.fishingclub.client.game.FishGameLogic;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;

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
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity2, Hand hand) {
        ItemStack itemStack = playerEntity2.getStackInHand(hand);
        if (playerEntity2.fishHook != null) {
            if (!world.isClient) {
                int i = playerEntity2.fishHook.use(itemStack);
                itemStack.damage(i, playerEntity2, playerEntity -> playerEntity.sendToolBreakStatus(hand));
            }
            world.playSound(null, playerEntity2.getX(), playerEntity2.getY(), playerEntity2.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            playerEntity2.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
        } else {
            world.playSound(null, playerEntity2.getX(), playerEntity2.getY(), playerEntity2.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
            if (!world.isClient) {
                world.spawnEntity(new CustomFishingBobberEntity(playerEntity2, world, this));
            }
            playerEntity2.incrementStat(Stats.USED.getOrCreateStat(this));
            playerEntity2.emitGameEvent(GameEvent.ITEM_INTERACT_START);
        }
        return TypedActionResult.success(itemStack, world.isClient());
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
}

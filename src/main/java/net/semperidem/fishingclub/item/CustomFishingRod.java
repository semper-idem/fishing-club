package net.semperidem.fishingclub.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
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
import net.semperidem.fishingclub.util.FishingRodUtil;

public class CustomFishingRod extends FishingRodItem {
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

    public static FishingRodPartItem getBait(ItemStack rodStack){
        NbtCompound rodNbt = rodStack.getNbt();
        if (rodNbt.contains("parts")) {
            NbtCompound partsNbt = rodNbt.getCompound("parts");
            if (partsNbt.contains(FishingRodPartItem.PartType.BAIT.name())) {
                NbtCompound partNbt = partsNbt.getCompound(FishingRodPartItem.PartType.BAIT.name());
                if (partNbt.contains("key")) {
                    return  FishingRodPartItems.KEY_TO_PART_MAP.get(partNbt.getString("key"));
                }
            }
        }
        return null;
    }

    public Item getPartItem(ItemStack rodStack, FishingRodPartItem.PartType partType){
        NbtCompound rodNbt = rodStack.getNbt();
        if (rodNbt.contains("parts")) {
            NbtCompound partsNbt = rodNbt.getCompound("parts");
            if (partsNbt.contains(partType.name())) {
                NbtCompound partNbt = partsNbt.getCompound(partType.name());
                if (partNbt.contains("key")) {
                    return FishingRodPartItems.KEY_TO_PART_MAP.get(partNbt.getString("key"));
                }
            }
        }
        return Items.AIR;
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

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int power = FishingRodUtil.getPower(getMaxUseTime(stack) - remainingUseTicks);
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
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f / (world.getRandom().nextFloat() * 0.4f + 0.8f));
        if (!world.isClient) {
            boolean boatFishing = user.getVehicle() instanceof BoatEntity;
            world.spawnEntity(new CustomFishingBobberEntity(user, world, fishingRod, power, FishingCardManager.getPlayerCard((ServerPlayerEntity) user), boatFishing));
        }
        user.incrementStat(Stats.USED.getOrCreateStat(this));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
    }

    private float getDamageChance(ItemStack fishingRod) {
        NbtCompound rodNbt = fishingRod.getOrCreateNbt();
        int handmadeUsages;
        if (rodNbt.contains("handmade")) {
            handmadeUsages = rodNbt.getInt("handmade");
            if (handmadeUsages > 0) {
                rodNbt.putInt("handmade", handmadeUsages - 1);
                fishingRod.setNbt(rodNbt);
                return 0;
            }
        }
        float damageChance = 1f;
        Item coreItem = getPartItem(fishingRod, FishingRodPartItem.PartType.CORE);
        if (coreItem.equals(FishingRodPartItems.CORE_BAMBOO)) damageChance = 0.85f;
        else if (coreItem.equals(FishingRodPartItems.CORE_COMPOSITE)) damageChance = 0.2f;
        else if (coreItem.equals(FishingRodPartItems.CORE_GOLDEN)) damageChance = 1.8f;
        else if (coreItem.equals(FishingRodPartItems.CORE_NETHERITE)) damageChance = 0.3f;
        return damageChance * 1;
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
            if (Math.random() < damageChance) {
                fishingRod.setDamage((int) (fishingRod.getDamage() + damageChance));
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

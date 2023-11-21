package net.semperidem.fishingclub.item.fishing_rod;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.semperidem.fishingclub.registry.FItemRegistry;

public class FishingRodUtil {

    public static int getFishingRodChargePower(int usageTick){
        return Math.max(1, Math.min(5, (usageTick + 40) / 40));
    }

    public static boolean hasHandmadeUsage(ItemStack fishingRod){
        return hasHandmadeUsage(fishingRod.getOrCreateNbt());
    }
    public static boolean hasHandmadeUsage(NbtCompound fishingRodNbt){
        return fishingRodNbt.contains("handmade");
    }

    public static void consumeHandmadeUsage(ItemStack fishingRod){
        consumeHandmadeUsage(fishingRod, 1);
    }

    public static void consumeHandmadeUsage(ItemStack fishingRod, int amountConsumed){
        NbtCompound fishingRodNbt = fishingRod.getOrCreateNbt();
        if (!hasHandmadeUsage(fishingRodNbt)) return;
        int currentCount = fishingRodNbt.getInt("handmade");
        fishingRodNbt.putInt("handmade", currentCount - amountConsumed);

        fishingRod.setNbt(fishingRodNbt); //TODO TEST IF  THIS LINE IS REQUIRED
    }

    public static void setHandmadeUsageCount(ItemStack fishingRod, int amount){
        NbtCompound fishingRodNbt = fishingRod.getOrCreateNbt();
        fishingRodNbt.putInt("handmade", amount);
        fishingRod.setNbt(fishingRodNbt); //TODO TEST IF  THIS LINE IS REQUIRED
    }

    public static float getRodDamageChance(ItemStack fishingRod) {
        if (!FishingRodPartController.hasParts(fishingRod)) return 1;
        ItemStack rodPart = FishingRodPartController.getPart(fishingRod, FishingRodPartType.CORE);
        if (rodPart == ItemStack.EMPTY) return 1;
        if (!(rodPart.getItem() instanceof FishingRodPartItem core)) return 1;

        return core.getStat(FishingRodStatType.ROD_DAMAGE_CHANCE);
    }

    public static ItemStack getBasicRod(){
        ItemStack starterStack = new ItemStack(FItemRegistry.CUSTOM_FISHING_ROD);
        FishingRodPartController.putPart(starterStack, FishingRodPartItems.CORE_BAMBOO.getDefaultStack());
        FishingRodPartController.putPart(starterStack, FishingRodPartItems.HOOK_COPPER.getDefaultStack());
        FishingRodPartController.putPart(starterStack, FishingRodPartItems.LINE_FIBER_THREAD.getDefaultStack());
        return starterStack;
    }

    public static ItemStack getStarterRod(){
        ItemStack starterStack = new ItemStack(FItemRegistry.CUSTOM_FISHING_ROD);
        FishingRodPartController.putPart(starterStack, FishingRodPartItems.CORE_BAMBOO.getDefaultStack());
        FishingRodPartController.putPart(starterStack, FishingRodPartItems.HOOK_COPPER.getDefaultStack());
        FishingRodPartController.putPart(starterStack, FishingRodPartItems.LINE_FIBER_THREAD.getDefaultStack());
        return starterStack;
    }

    public static ItemStack getAdvancedRod(){
        ItemStack advancedStack = new ItemStack(FItemRegistry.CUSTOM_FISHING_ROD);
        FishingRodPartController.putPart(advancedStack, FishingRodPartItems.CORE_COMPOSITE.getDefaultStack());
        FishingRodPartController.putPart(advancedStack, FishingRodPartItems.HOOK_IRON.getDefaultStack());
        FishingRodPartController.putPart(advancedStack, FishingRodPartItems.LINE_SPIDER_SILK.getDefaultStack());
        FishingRodPartController.putPart(advancedStack, FishingRodPartItems.BAIT_CRAFTED.getDefaultStack());
        FishingRodPartController.putPart(advancedStack, FishingRodPartItems.BOBBER_PLANT.getDefaultStack());
        return advancedStack;
    }
}

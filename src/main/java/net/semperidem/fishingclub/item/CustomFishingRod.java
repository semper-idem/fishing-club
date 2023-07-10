package net.semperidem.fishingclub.item;

import it.unimi.dsi.fastutil.Hash;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;

import java.util.HashMap;

public class CustomFishingRod extends FishingRodItem {
    HashMap<FishingRodPartItem.PartType, FishingRodPartItem> customParts = new HashMap<>();
    public CustomFishingRod(Settings settings) {
        super(settings);
    }

    public void initParts(HashMap<FishingRodPartItem.PartType, FishingRodPartItem> customParts){
        this.customParts = customParts;
    }


    public void initDefault(){
        this.customParts.put(FishingRodPartItem.PartType.CORE, FishingRodPartItems.CORE_BAMBOO);
        this.customParts.put(FishingRodPartItem.PartType.HOOK, FishingRodPartItems.HOOK_COPPER);
        this.customParts.put(FishingRodPartItem.PartType.LINE, FishingRodPartItems.LINE_WOOL_THREAD);
    }

    public void changePart(FishingRodPartItem partItem){
        if (customParts.containsKey(partItem.getPartType())) {
            customParts.remove(partItem.getPartType());
            //Drop item
        }
        customParts.put(partItem.getPartType(), partItem);
        //Consume item
    }

    public HashMap<FishingRodPartItem.PartType, FishingRodPartItem> getParts(){
        return customParts;
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
                world.spawnEntity(new CustomFishingBobberEntity(playerEntity2, world));
            }
            playerEntity2.incrementStat(Stats.USED.getOrCreateStat(this));
            playerEntity2.emitGameEvent(GameEvent.ITEM_INTERACT_START);
        }
        return TypedActionResult.success(itemStack, world.isClient());
    }

}

package net.semperidem.fishingclub.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.semperidem.fishingclub.network.ServerPacketSender;

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

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (playerEntity.fishHook != null) {
            if (!world.isClient) {
                if (playerEntity.fishHook.getHookedEntity() != null) {
                    ServerPacketSender.sendFisherInfoSyncPacket((ServerPlayerEntity)playerEntity);
                    ServerPacketSender.sendFishingStartPacket((ServerPlayerEntity)playerEntity, this.customParts);
                }
                world.playSound(
                        null,
                        playerEntity.getX(),
                        playerEntity.getY(),
                        playerEntity.getZ(),
                        SoundEvents.ENTITY_FISHING_BOBBER_RETRIEVE,
                        SoundCategory.NEUTRAL,
                        1.0F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
                );
                playerEntity.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
                //TODO Indicate player is fising, maybe marker on top of head?
            }

        } else {
            world.playSound(
                    null,
                    playerEntity.getX(),
                    playerEntity.getY(),
                    playerEntity.getZ(),
                    SoundEvents.ENTITY_FISHING_BOBBER_THROW,
                    SoundCategory.NEUTRAL,
                    0.5F,
                    0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F)
            );
            if (!world.isClient) {
                world.spawnEntity(new FishingBobberEntity(playerEntity, world,0,0));
            }

            playerEntity.incrementStat(Stats.USED.getOrCreateStat(Items.FISHING_ROD));
            playerEntity.emitGameEvent(GameEvent.ITEM_INTERACT_START);
        }
        return super.use(world,playerEntity, hand);
    }

}

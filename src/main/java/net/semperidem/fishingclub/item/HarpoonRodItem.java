package net.semperidem.fishingclub.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.semperidem.fishingclub.entity.HarpoonEntity;

public class HarpoonRodItem extends FishingRodItem {
    public HarpoonRodItem(Settings settings) {
        super(settings);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {

        if (!(user instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity playerEntity = (PlayerEntity)user;
        int usePower = getPower(this.getMaxUseTime(stack) - remainingUseTicks);
        if (usePower < 1) {
            return;
        }



        stack.damage(1, playerEntity, p -> p.sendToolBreakStatus(user.getActiveHand()));
        HarpoonEntity harpoonEntity = new HarpoonEntity(world, playerEntity, stack);
        harpoonEntity.setVelocity(playerEntity, playerEntity.getPitch(), playerEntity.getYaw(), 0.0f, Math.min(4f, 1.5f + usePower * 0.5f), 1.0f);
        world.spawnEntity(harpoonEntity);
        world.playSoundFromEntity(null, harpoonEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0f, 1.0f);
        playerEntity.getInventory().removeOne(stack);

        playerEntity.incrementStat(Stats.USED.getOrCreateStat(this));
        user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
    }

    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 12000;
    }


    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand); //Logic continues in onStoppedUsing
        return TypedActionResult.consume(user.getStackInHand(hand));
    }


    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    public static int getPower(int useTime){
        return useTime / 40;
    }
}

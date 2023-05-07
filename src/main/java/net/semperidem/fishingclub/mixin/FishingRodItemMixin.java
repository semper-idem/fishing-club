package net.semperidem.fishingclub.mixin;


import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import net.semperidem.fishingclub.network.ServerPacketSender;
import net.semperidem.fishingclub.screen.FishingScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingRodItem.class)
public class FishingRodItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    public void startMinigameOnFishHook(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);

        // Check if the player is currently fishing

        if (user.fishHook != null) {


            if (!world.isClient) {
                // Handle your mini-game start here
                startFishingMinigame(user);
            }

            // Cancel the original method to prevent the standard behavior
        } else {
            world.playSound((PlayerEntity)null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (world.getRandom().nextFloat() * 0.4F + 0.8F));
            if (!world.isClient) {
                int i = EnchantmentHelper.getLure(stack);
                int j = EnchantmentHelper.getLuckOfTheSea(stack);
                world.spawnEntity(new FishingBobberEntity(user, world, j, i));
            }

            user.incrementStat(Stats.USED.getOrCreateStat(Items.FISHING_ROD));
            user.emitGameEvent(GameEvent.ITEM_INTERACT_START);
        }

        cir.setReturnValue(TypedActionResult.success(stack, world.isClient()));
        cir.cancel();
    }

    private void startFishingMinigame(PlayerEntity player) {
        ServerPacketSender.sendFishingSkillSyncPacket((ServerPlayerEntity)player);
        ServerPacketSender.sendFishingStartPacket((ServerPlayerEntity)player);
    }
}

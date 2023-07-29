package net.semperidem.fishingclub.mixin;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.util.InventoryUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Shadow public abstract ItemStack getStack();

    @Shadow private int pickupDelay;

    @Inject(method = "onPlayerCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;insertStack(Lnet/minecraft/item/ItemStack;)Z"), cancellable = true)
    private void onPlayerCollision(PlayerEntity player, CallbackInfo ci){
        ItemStack incomingStack = this.getStack();
        if (!incomingStack.isOf(FishingClub.FISHING_NET ) && !incomingStack.isOf(FishingClub.DOUBLE_FISHING_NET)) return;
        if (InventoryUtil.inventoryContainsUsedFishingNet(player.getInventory())) {
            pickupDelay = 40;
            ci.cancel();
        }
    }
}

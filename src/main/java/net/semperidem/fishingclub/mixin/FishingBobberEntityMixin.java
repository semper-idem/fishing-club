package net.semperidem.fishingclub.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.FishingClub;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public class FishingBobberEntityMixin {

//    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootManager;getTable(Lnet/minecraft/util/Identifier;)Lnet/minecraft/loot/LootTable;"), cancellable = true)
//    void onUse(ItemStack itemStack, CallbackInfoReturnable<Integer> cir) {
//        //TODO Implement golden fish mechanic here
//    }

    @Inject(method = "removeIfInvalid", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;discard()V"), cancellable = true)
    void removeIfInvalid(PlayerEntity playerEntity, CallbackInfoReturnable<Boolean> cir) {
        ItemStack itemStack = playerEntity.getMainHandStack();
        ItemStack itemStack2 = playerEntity.getOffHandStack();
        boolean bl = itemStack.isOf(FishingClub.CUSTOM_FISHING_ROD);
        boolean bl2 = itemStack2.isOf(FishingClub.CUSTOM_FISHING_ROD);
        boolean hasNoFishingRod = !bl && !bl2;
        if (!hasNoFishingRod) {
            cir.setReturnValue(false);
        }
    }
}

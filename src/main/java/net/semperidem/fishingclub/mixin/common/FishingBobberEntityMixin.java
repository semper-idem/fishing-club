package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.registry.ItemRegistry;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin{
    @Shadow public abstract @Nullable PlayerEntity getPlayerOwner();

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private void onUse(ItemStack fishedItemStack, CallbackInfoReturnable<Integer> cir) {
        if (!fishedItemStack.isOf(ItemRegistry.GOLD_FISH)) {
            return;
        }
        if (this.getPlayerOwner() == null) {
            return;
        }
        FishUtil.putCaughtBy(fishedItemStack, this.getPlayerOwner().getUuid());
    }
}

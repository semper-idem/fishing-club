package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.registry.FCComponents;
import net.semperidem.fishingclub.registry.FCItems;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FishingBobberEntity.class)
public abstract class FishingBobberEntityMixin extends ProjectileEntity {
    public FishingBobberEntityMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Shadow public abstract @Nullable PlayerEntity getPlayerOwner();

    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;spawnEntity(Lnet/minecraft/entity/Entity;)Z"))
    private void onUse(ItemStack fishedItemStack, CallbackInfoReturnable<Integer> cir) {
        if (!fishedItemStack.isOf(FCItems.GOLD_FISH)) {
            return;
        }
        if (this.getPlayerOwner() == null) {
            return;
        }
        fishedItemStack.set(FCComponents.CAUGHT_BY, this.getPlayerOwner().getUuid());
    }


    //This would be more "optimal" if we 'OR' bl and bl2 with MEMBER_FISHING_ROD check
    //But it doesn't have to be optimal
    @Redirect(method = "removeIfInvalid", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z"))
    private boolean fishing_club$isOf(ItemStack instance, Item item) {
        return instance.isOf(item) || instance.isOf(FCItems.MEMBER_FISHING_ROD);
    }

    @Inject(method = "tick", cancellable = true, at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/projectile/FishingBobberEntity;getPlayerOwner()Lnet/minecraft/entity/player/PlayerEntity;"))
    private void onTick(CallbackInfo ci) {
        if ((Object)this instanceof HookEntity){
            ci.cancel();
        }
    }
}

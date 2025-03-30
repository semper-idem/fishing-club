package net.semperidem.fishingclub.mixin.common;


import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodCoreItem;
import net.semperidem.fishingclub.registry.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;


@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{

    @Unique private int pullPower = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "stopUsingItem", at = @At("TAIL"))
    private void onStopUsingItem(CallbackInfo ci){
        this.pullPower = 0;
    }

    @Inject(method = "tickItemStackUsage", at = @At("TAIL"))
    private void onTickItemStackUsage(ItemStack activeStack, CallbackInfo ci){
        if (!((LivingEntity)(Object)this instanceof PlayerEntity player)) {
            return;
        }
        if (!activeStack.isIn(Tags.CORE)) {
            return;
        }
        int currentPower = FishingRodCoreItem.getChargePower(getItemUseTime());
        if (currentPower == this.pullPower || currentPower == 0) {
            return;
        }
        this.pullPower = currentPower;
        player.sendMessage(Text.of("[" + "✦".repeat(currentPower) + "]"), true);
    }



    @Redirect(
            method = "travelMidAir", at = @At(
                    value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V"
    )
    )
    private void onTravel(
            LivingEntity instance,
            double x,
            double y,
            double z,
            @Local(ordinal = 1) Vec3d vec3d6
    ) {
        if ((LivingEntity)(Object)this instanceof PlayerEntity player) {
            if (player.fishHook instanceof HookEntity hookEntity) {
                Vec3d lineVector = hookEntity.getPos().subtract(this.getEyePos());
                double currentLineLength = lineVector.length();
                if (!this.isOnGround() && currentLineLength > hookEntity.getLineLength()) {
                    double reverseGravity = 1.015f * y;
                    float g = 0.95F;
                    this.setVelocity(
                            vec3d6.x * g,
                            reverseGravity,
                            vec3d6.z * g
                    );
                    return;
                }
            }
        }
        this.setVelocity(x, y, z);
    }


    @Inject(method="addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("TAIL"))
    private void onAddStatusEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if (!isPlayer() || getWorld().isClient) {
            return;
        }
        Card card = Card.of((ServerPlayerEntity)(Object)this);
        if (card != null) {
            card.shareStatusEffect(effect, (LivingEntity)(Object)this, new HashSet<>());
        }
    }

    @Shadow public abstract int getItemUseTime();

}

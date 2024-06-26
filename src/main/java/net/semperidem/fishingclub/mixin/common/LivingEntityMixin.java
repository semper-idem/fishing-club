package net.semperidem.fishingclub.mixin.common;


import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.MinecraftClient;
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
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.item.HarpoonRodItem;
import net.semperidem.fishingclub.item.fishing_rod.MemberFishingRodItem;
import net.semperidem.fishingclub.registry.ItemRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.semperidem.fishingclub.registry.ItemRegistry.MEMBER_FISHING_ROD;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{

    @Shadow public abstract void updateLimbs(LivingEntity entity, boolean flutter);

    @Unique private int pullPower = 0;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(method = "stopUsingItem", at = @At("TAIL"))
    private void onStopUsingItem(CallbackInfo ci){
        pullPower = 0;
    }

    @Inject(method = "tickItemStackUsage", at = @At("TAIL"))
    private void onTickItemStackUsage(ItemStack activeStack, CallbackInfo ci){
        int power;
        if (activeStack.isOf(MEMBER_FISHING_ROD)){
            power = MemberFishingRodItem.getChargePower(getItemUseTime());
            tickPullPower(power);
        } else if (activeStack.isOf(ItemRegistry.HARPOON_ROD)) {
            power = HarpoonRodItem.getPower(getItemUseTime());
        } else {
            return;
        }
        tickPullPower(power);
    }



    @Redirect(
            method = "travel", at = @At(
                    value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;setVelocity(DDD)V",
            ordinal = 3
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
            if (player.fishHook instanceof HookEntity bobber) {
                if (!this.isOnGround()) {
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
        if (!isPlayer() || world.isClient) {
            return;
        }
        FishingCard fishingCard = FishingCard.of((ServerPlayerEntity)(Object)this);
        if (fishingCard != null) {
            fishingCard.shareStatusEffect(effect);
        }
    }

    private void tickPullPower(int power){
        if (power != this.pullPower && power != 0) {
            this.pullPower = power;
            MinecraftClient.getInstance().player.sendMessage(Text.of("[" + ">".repeat(power) + "]"), true);
        }
    }
    @Shadow public abstract int getItemUseTime();

}

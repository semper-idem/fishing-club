package net.semperidem.fishingclub.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.util.FishingRodUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.semperidem.fishingclub.FishingClub.CUSTOM_FISHING_ROD;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique private int rodPullPower = 0;


    @Inject(method = "tickItemStackUsage", at = @At("TAIL"))
    private void onTickItemStackUsage(ItemStack activeStack, CallbackInfo ci){
        if (!activeStack.isOf(CUSTOM_FISHING_ROD)) return;
        tickRodPullPower();
    }

    private void tickRodPullPower(){
        int power = FishingRodUtil.getPower(getItemUseTime());
        if (power != this.rodPullPower) {
            this.rodPullPower = power;
            MinecraftClient.getInstance().player.sendMessage(Text.of("[" + ">".repeat(power) + "]"), true);
        }
    }
    @Shadow public abstract int getItemUseTime();
}

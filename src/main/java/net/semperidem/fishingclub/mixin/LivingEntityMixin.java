package net.semperidem.fishingclub.mixin;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.item.HarpoonRodItem;
import net.semperidem.fishingclub.registry.FItemRegistry;
import net.semperidem.fishingclub.util.FishingRodUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.semperidem.fishingclub.registry.FItemRegistry.CUSTOM_FISHING_ROD;

@Environment(EnvType.CLIENT)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique private int pullPower = 0;


    @Inject(method = "stopUsingItem", at = @At("TAIL"))
    private void onStopUsingItem(CallbackInfo ci){
        pullPower = 0;
    }

    @Inject(method = "tickItemStackUsage", at = @At("TAIL"))
    private void onTickItemStackUsage(ItemStack activeStack, CallbackInfo ci){
        int power;
        if (activeStack.isOf(CUSTOM_FISHING_ROD)){
            power = FishingRodUtil.getPower(getItemUseTime());
            tickPullPower(power);
        } else if (activeStack.isOf(FItemRegistry.HARPOON_ROD)) {
            power = HarpoonRodItem.getPower(getItemUseTime());
        } else {
            return;
        }
        tickPullPower(power);
    }


    private void tickPullPower(int power){
        if (power != this.pullPower && power != 0) {
            this.pullPower = power;
            MinecraftClient.getInstance().player.sendMessage(Text.of("[" + ">".repeat(power) + "]"), true);
        }
    }
    @Shadow public abstract int getItemUseTime();

}

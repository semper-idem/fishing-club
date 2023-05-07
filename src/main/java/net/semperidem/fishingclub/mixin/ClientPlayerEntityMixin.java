package net.semperidem.fishingclub.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.semperidem.fishingclub.network.ClientPacketSender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo ci){
        ClientPacketSender.sendFishingSkillDataRequest();
    }

}

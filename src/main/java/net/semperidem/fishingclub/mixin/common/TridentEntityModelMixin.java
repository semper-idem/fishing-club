package net.semperidem.fishingclub.mixin.common;


import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.TridentEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TridentEntityModel.class)
public class TridentEntityModelMixin {

    @Inject(method ="getTexturedModelData", at = @At("HEAD"))
    private static void getHarpoonModelData(CallbackInfoReturnable<TexturedModelData> cir){

    }
}

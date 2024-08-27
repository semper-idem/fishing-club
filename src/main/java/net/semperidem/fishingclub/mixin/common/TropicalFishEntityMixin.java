package net.semperidem.fishingclub.mixin.common;

import net.minecraft.entity.passive.TropicalFishEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;


@Mixin(TropicalFishEntity.class)
public class TropicalFishEntityMixin {

    @Redirect(method = "initialize", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/random/Random;nextFloat()F"))
    private float fishing_club$nextFloat(net.minecraft.util.math.random.Random instance) {
        return 0; //anything below 0.9 will spawn common variant
    }

}

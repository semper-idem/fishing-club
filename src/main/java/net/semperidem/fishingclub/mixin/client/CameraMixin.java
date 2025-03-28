package net.semperidem.fishingclub.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.semperidem.fishingclub.client.screen.game.FishingPostScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Camera.class)
public abstract class CameraMixin {

    @Shadow private Entity focusedEntity;

    @Inject(method = "getFocusedEntity", at = @At("HEAD"), cancellable = true)
    private void fishing_club$getFocusedEntity(CallbackInfoReturnable<Entity> cir) {
        if (this.focusedEntity.getCustomName() == FishingPostScreen.PRESENTING_CAMERA) {
            cir.setReturnValue(MinecraftClient.getInstance().player);
        }
    }
}

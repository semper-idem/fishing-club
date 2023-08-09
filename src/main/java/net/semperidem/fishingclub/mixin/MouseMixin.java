package net.semperidem.fishingclub.mixin;

import net.minecraft.client.Mouse;
import net.semperidem.fishingclub.client.screen.hud.SpellListWidget;
import net.semperidem.fishingclub.registry.FKeybindingRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow private double eventDeltaWheel;

    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci){
        if (FKeybindingRegistry.SPELL_SELECT_KB.isPressed()) {
            SpellListWidget.scrollSpells((int) vertical);
            ci.cancel();
        }
    }
}

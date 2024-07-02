package net.semperidem.fishingclub.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.semperidem.fishingclub.client.screen.hud.SpellListWidget;
import net.semperidem.fishingclub.entity.HookEntity;
import net.semperidem.fishingclub.network.payload.HookLinePayload;
import net.semperidem.fishingclub.registry.KeybindingRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow @Final private MinecraftClient client;

    @Inject(method = "onMouseScroll", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerInventory;scrollInHotbar(D)V"), cancellable = true)
    private void onScroll(long window, double horizontal, double vertical, CallbackInfo ci){
        if (this.client.player == null) {
            return;
        }
        if (KeybindingRegistry.SPELL_SELECT_KB.isPressed()) {
            SpellListWidget.scrollSpells((int) vertical);
            ci.cancel();
        }

        if (this.client.player.fishHook instanceof HookEntity hookEntity) {
            ClientPlayNetworking.send(new HookLinePayload((int) vertical));
            hookEntity.scrollLine((int)vertical);
            ci.cancel();
        }
    }
}

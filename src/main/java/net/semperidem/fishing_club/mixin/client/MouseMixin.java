package net.semperidem.fishing_club.mixin.client;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishing_club.client.screen.hud.SpellListWidget;
import net.semperidem.fishing_club.entity.HookEntity;
import net.semperidem.fishing_club.item.fishing_rod.MemberFishingRodItem;
import net.semperidem.fishing_club.network.payload.LinePayload;
import net.semperidem.fishing_club.registry.FCKeybindings;
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
        PlayerEntity player = this.client.player;
        if (player == null) {
            return;
        }
        if (FCKeybindings.SPELL_SELECT.isPressed()) {
            SpellListWidget.scrollSpells((int) vertical);
            ci.cancel();
            return;
        }
        boolean isFishing = player.fishHook instanceof HookEntity;
        boolean isSneakingWithRod = player.getMainHandStack().getItem() instanceof MemberFishingRodItem && player.isSneaking();
        if (isFishing || isSneakingWithRod) {
            ClientPlayNetworking.send(new LinePayload((int) vertical));
            ci.cancel();
        }
    }
}

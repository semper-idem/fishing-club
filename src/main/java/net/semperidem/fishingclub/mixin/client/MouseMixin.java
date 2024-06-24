package net.semperidem.fishingclub.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import net.minecraft.item.ItemStack;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.client.screen.hud.SpellListWidget;
import net.semperidem.fishingclub.entity.CustomFishingBobberEntity;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.registry.ItemRegistry;
import net.semperidem.fishingclub.registry.KeybindingRegistry;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mouse.class)
public class MouseMixin {

    @Shadow private double eventDeltaWheel;

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
        ItemStack mainHand = this.client.player.getMainHandStack();
        ItemStack offHand = this.client.player.getOffHandStack();

        if (!mainHand.isOf(ItemRegistry.MEMBER_FISHING_ROD) && !offHand.isOf(ItemRegistry.MEMBER_FISHING_ROD)) {
            return;
        }
        if (!this.client.player.isSneaking()) {
            return;
        }
        ClientPacketSender.sendLineScroll((int)vertical);
        if (this.client.player.fishHook instanceof CustomFishingBobberEntity customFishingBobberEntity) {
            customFishingBobberEntity.scrollLine((int)vertical);
        }
        ci.cancel();
    }
}

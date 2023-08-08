package net.semperidem.fishingclub.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.semperidem.fishingclub.client.screen.hud.SpellListWidget;
import net.semperidem.fishingclub.fisher.FisherInfoManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Unique
    SpellListWidget spellListWidget;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(MinecraftClient client, ItemRenderer itemRenderer, CallbackInfo ci){
        spellListWidget = new SpellListWidget(FisherInfoManager.getFisher(client.player));
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void afterRender(MatrixStack matrices, float tickDelta, CallbackInfo ci){
        spellListWidget.render(matrices, tickDelta);
    }
}

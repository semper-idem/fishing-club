package net.semperidem.fishingclub.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.semperidem.fishingclub.client.screen.hud.SpellListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    SpellListWidget spellListWidget;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(MinecraftClient client, CallbackInfo ci){
        spellListWidget = new SpellListWidget();
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void afterRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci){
        spellListWidget.render(context, tickCounter.getTickDelta(true));
    }


}

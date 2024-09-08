package net.semperidem.fishingclub.mixin.client.ui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.LayeredDrawer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.semperidem.fishingclub.client.screen.hud.SpellListWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique
    SpellListWidget spellListWidget;

    @ModifyVariable(method = "<init>", at = @At("STORE"), ordinal = 0)
    private LayeredDrawer layeredDrawer(LayeredDrawer layeredDrawer){
        spellListWidget = new SpellListWidget();
        layeredDrawer.addLayer(this.spellListWidget::render);
        return layeredDrawer;
    }

}

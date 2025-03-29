package net.semperidem.fishingclub.mixin.client.ui;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.network.payload.ConfigurationPayload;
import net.semperidem.fishingclub.network.payload.CardPayload;
import net.semperidem.fishingclub.registry.Tags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> {
    @Shadow public abstract RecipeBookWidget getRecipeBookWidget();

    private static final ButtonTextures BUTTON_TEXTURES = new ButtonTextures(
            FishingClub.identifier("card/button"),
            FishingClub.identifier("card/button_highlighted")
    );

    @Unique
    private TexturedButtonWidget fishingCardButton;

    InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(
            method = "init",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/gui/screen/ingame/InventoryScreen;addSelectableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"
            )
    )
    private void fishing_club$addFishingCardButton(CallbackInfo ci){
//todo uncomment when releasing
//        if (client != null && !FishingCard.of(client.player).isMember()) {
//            return;
//        }

        this.fishingCardButton = new TexturedButtonWidget(this.x + 126, this.height / 2 - 22, 20, 18, BUTTON_TEXTURES, button -> {
            if (client != null && client.player != null && client.player.getMainHandStack().isIn(Tags.ROD_CORE)) {
                ClientPlayNetworking.send(new ConfigurationPayload());
                return;
            }
            ClientPlayNetworking.send(new CardPayload());
        });
        this.addDrawableChild(this.fishingCardButton);

    }

    @Inject(
            method = "method_19891",
            at = @At("TAIL")
    )
    private void fishing_club$onRecipeBookClick(ButtonWidget button, CallbackInfo ci) {
       if (fishingCardButton == null) {
           return;
       }
       this.x = this.getRecipeBookWidget().findLeftEdge(this.width, this.backgroundWidth);
       fishingCardButton.setPosition(this.x + 126, this.height / 2 - 22);
    }

    @Shadow
    protected abstract void drawBackground(DrawContext context, float delta, int mouseX, int mouseY);
}

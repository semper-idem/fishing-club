package net.semperidem.fishingclub.client.screen.old_fishing_card;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;

import java.awt.*;
import java.util.function.Supplier;

public class PerkButtonWidget extends ButtonWidget {
    private static final int ICON_SIZE = 20;
    private static final int BORDER_SIZE = 22;
    private static final Identifier SKILL_ICON = FishingClub.identifier("textures/gui/skill_icon_placeholder.png");
    private static final Identifier SELECTED_SKILL_BORDER = FishingClub.identifier("textures/gui/skill/selected_skill.png");

    public boolean isSelected;
    TradeSecret tradeSecret;
    Card card;
    public PerkButtonWidget(int x, int y, Card card, TradeSecret tradeSecret, OldFishingCardScreen parent) {
        super(x, y, ICON_SIZE, ICON_SIZE, Text.empty(), button -> parent.setPerk((PerkButtonWidget) button, tradeSecret), Supplier::get);
        this.card = card;
        this.tradeSecret = tradeSecret;
        this.isSelected = false;
    }


    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!visible) {
            return;
        }
//        RenderSystem.enableBlend();
        renderBackground(context);
        renderLink(context);
        renderIcon(context);
        renderSelection(context);
//        RenderSystem.disableBlend();
    }

    private void renderSelection(DrawContext context) {
        if (isSelected) {
//            context.drawTexture(SELECTED_SKILL_BORDER, getX() - 1, getY() - 1, 0, 0, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE);
        }
    }

    private void renderBackground(DrawContext context){
        if (!card.hasRequiredPerk(tradeSecret)) {
            RenderSystem.setShaderColor(0.6f,0.6f,0.6f,0.6f);
        } else if (card.knowsTradeSecret(tradeSecret)) {
            RenderSystem.setShaderColor(0.5f,1,0.5f,1);
        }
//        context.drawTexture(SKILL_ICON, getX(), getY(), 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }

    private void renderLink(DrawContext context){
       // if (tradeSecret.getRequiredSecrets() == null) {
         //   return;
        //}
        context.fill(getX(), getY() + 9, getX() - 6, getY() + 11, card.knowsTradeSecret(tradeSecret) ? Color.GREEN.getRGB() : Color.WHITE.getRGB());
    }

    private void renderIcon(DrawContext context){
        Identifier icon;
        if ((icon = tradeSecret.getTexture()) == null) {
            return;
        }
        RenderSystem.setShaderColor(1,1,1,1);
//        context.drawTexture(icon, getX(), getY(), 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }
}

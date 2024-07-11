package net.semperidem.fishing_club.client.screen.fishing_card;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.fisher.FishingCard;
import net.semperidem.fishing_club.fisher.perks.FishingPerk;

import java.awt.*;
import java.util.function.Supplier;

public class PerkButtonWidget extends ButtonWidget {
    private static final int ICON_SIZE = 20;
    private static final int BORDER_SIZE = 22;
    private static final Identifier SKILL_ICON = FishingClub.getIdentifier("textures/gui/skill_icon_placeholder.png");
    private static final Identifier SELECTED_SKILL_BORDER = FishingClub.getIdentifier("textures/gui/skill/selected_skill.png");

    public boolean isSelected;
    FishingPerk fishingPerk;
    FishingCard fishingCard;
    public PerkButtonWidget(int x, int y, FishingCard fishingCard, FishingPerk fishingPerk, FishingCardScreen parent) {
        super(x, y, ICON_SIZE, ICON_SIZE, Text.empty(), button -> parent.setPerk((PerkButtonWidget) button, fishingPerk), Supplier::get);
        this.fishingCard = fishingCard;
        this.fishingPerk = fishingPerk;
        this.visible = false;
        this.isSelected = false;
    }


    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!visible) {
            return;
        }
        RenderSystem.enableBlend();
        renderBackground(context);
        renderLink(context);
        renderIcon(context);
        renderSelection(context);
        RenderSystem.disableBlend();
    }

    private void renderSelection(DrawContext context) {
        if (isSelected) {
            context.drawTexture(SELECTED_SKILL_BORDER, getX() - 1, getY() - 1, 0, 0, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE);
        }
    }

    private void renderBackground(DrawContext context){
        if (!fishingCard.hasRequiredPerk(fishingPerk)) {
            RenderSystem.setShaderColor(0.6f,0.6f,0.6f,0.6f);
        } else if (fishingCard.hasPerk(fishingPerk)) {
            RenderSystem.setShaderColor(0.5f,1,0.5f,1);
        }
        context.drawTexture(SKILL_ICON, getX(), getY(), 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }

    private void renderLink(DrawContext context){
        if (fishingPerk.getParent() == null) {
            return;
        }
        context.fill(getX(), getY() + 9, getX() - 6, getY() + 11, fishingCard.hasPerk(fishingPerk) ? Color.GREEN.getRGB() : Color.WHITE.getRGB());
    }

    private void renderIcon(DrawContext context){
        Identifier icon;
        if ((icon = fishingPerk.getIcon()) == null) {
            return;
        }
        RenderSystem.setShaderColor(1,1,1,1);
        context.drawTexture(icon, getX(), getY(), 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }
}

package net.semperidem.fishingclub.client.screen.fishing_card;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;

import java.awt.*;

public class PerkButtonWidget extends ButtonWidget {
    private static final int ICON_SIZE = 20;
    private static final int BORDER_SIZE = 22;
    private static final Identifier SKILL_ICON = new Identifier(FishingClub.MOD_ID, "textures/gui/skill_icon_placeholder.png");
    private static final Identifier SELECTED_SKILL_BORDER = new Identifier(FishingClub.MOD_ID, "textures/gui/skill/selected_skill.png");

    public boolean isSelected;
    FishingPerk fishingPerk;
    FishingCard fishingCard;
    public PerkButtonWidget(int x, int y, FishingCard fishingCard, FishingPerk fishingPerk, FishingCardScreen parent) {
        super(x, y, ICON_SIZE, ICON_SIZE, Text.empty(), button -> parent.setPerk((PerkButtonWidget) button, fishingPerk));
        this.fishingCard = fishingCard;
        this.fishingPerk = fishingPerk;
        this.visible = false;
        this.isSelected = false;
    }


    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!visible) {
            return;
        }
        RenderSystem.enableBlend();
        renderBackground(matrices);
        renderLink(matrices);
        renderIcon(matrices);
        renderSelection(matrices);
        RenderSystem.disableBlend();
    }

    private void renderSelection(MatrixStack matrices) {
        if (isSelected) {
            RenderSystem.setShaderTexture(0, SELECTED_SKILL_BORDER);
            drawTexture(matrices, x - 1, y - 1, 0, 0, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE);
        }
    }

    private void renderBackground(MatrixStack matrices){
        if (!fishingCard.hasRequiredPerk(fishingPerk)) {
            RenderSystem.setShaderColor(0.6f,0.6f,0.6f,0.6f);
        } else if (fishingCard.hasPerk(fishingPerk)) {
            RenderSystem.setShaderColor(0.5f,1,0.5f,1);
        }
        RenderSystem.setShaderTexture(0, SKILL_ICON);
        drawTexture(matrices, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }

    private void renderLink(MatrixStack matrices){
        if (fishingPerk.getParent() == null) {
            return;
        }
        fill(matrices, x, y + 9, x - 6, y + 11, fishingCard.hasPerk(fishingPerk) ? Color.GREEN.getRGB() : Color.WHITE.getRGB());
    }

    private void renderIcon(MatrixStack matrices){
        Identifier icon;
        if ((icon = fishingPerk.getIcon()) == null) {
            return;
        }
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, icon);
        drawTexture(matrices, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }
}

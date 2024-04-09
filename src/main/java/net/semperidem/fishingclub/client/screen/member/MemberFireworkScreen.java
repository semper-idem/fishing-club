package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import static net.minecraft.client.gui.DrawableHelper.drawCenteredText;
import static net.minecraft.text.Text.literal;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.*;

public class MemberFireworkScreen extends MemberSubScreen {
    private static final Text NO_FIREWORKS = literal("Not implemented yet :(");
    private int centerX;
    private int centerY;

    public MemberFireworkScreen(MemberScreen parent, Text title) {
        super(parent, title);
    }

    @Override
    public void init() {
        super.init();
        this.centerX = (int) (parent.x + TILE_SIZE * 5 + (TEXTURE.renderWidth - BUTTON_WIDTH - TILE_SIZE) * 0.5f);
        this.centerY = (int) (parent.y + TEXTURE.renderHeight * 0.5f);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        super.render(matrixStack, mouseX, mouseY, delta);
        drawCenteredText(matrixStack, textRenderer, NO_FIREWORKS, centerX, centerY, BEIGE_TEXT_COLOR);
    }
}

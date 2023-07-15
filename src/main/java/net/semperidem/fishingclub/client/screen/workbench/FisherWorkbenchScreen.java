package net.semperidem.fishingclub.client.screen.workbench;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FisherWorkbenchScreen extends HandledScreen<FisherWorkbenchScreenHandler> implements ScreenHandlerProvider<FisherWorkbenchScreenHandler> {
    private static final Identifier BACKGROUND = new Identifier(MOD_ID,"textures/gui/fisher_workbench_gui.png");
    public FisherWorkbenchScreen(FisherWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.empty());
        this.backgroundHeight = 222;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
    }


    public void render(MatrixStack matrixStack, int i, int j, float f) {
        renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        renderTextRightSide(matrixStack, "Core:", this.x + 143, this.y + 20);
        renderTextRightSide(matrixStack, "Bobber:", this.x + 143, this.y + 20 + 26);
        renderTextRightSide(matrixStack, "Line:", this.x + 143, this.y + 20 + 26 * 2);
        renderTextRightSide(matrixStack, "Hook:", this.x + 143, this.y + 20 + 26 * 3);
        renderTextRightSide(matrixStack, "Bait:", this.x + 52, this.y + 20 + 26 * 3);
        drawMouseoverTooltip(matrixStack, i, j);
    }

    private void renderTextRightSide(MatrixStack matrixStack, String text, int x, int y){
        int textLength = textRenderer.getWidth(text);
        this.textRenderer.drawWithShadow(matrixStack, text,x - textLength,y, Color.WHITE.getRGB());
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        this.drawTexture(matrices, this.x, this.y, 0, 0, 256, 256);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 0x404040);
    }
}

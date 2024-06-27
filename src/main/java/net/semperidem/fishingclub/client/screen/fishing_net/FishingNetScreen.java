package net.semperidem.fishingclub.client.screen.fishing_net;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;

public class FishingNetScreen extends HandledScreen<FishingNetScreenHandler> {
    private static final Identifier BACKGROUND = FishingClub.getIdentifier("textures/gui/fishing_net.png");
    int rows;

    public FishingNetScreen(FishingNetScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        int i = 222;
        int j = 114;
        this.rows = handler.getRows();
        this.backgroundHeight = 114 + this.rows * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        super.render(context, mouseX, mouseY, delta);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(BACKGROUND,i, j, 0, 0, this.backgroundWidth, this.rows * 18 + 17);
        context.drawTexture(BACKGROUND, i, j + this.rows * 18 + 17, 0, 126, this.backgroundWidth, 96);
    }

}

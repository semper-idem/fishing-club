package net.semperidem.fishingclub.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;

public class Texture {
    Identifier identifier;
    public final int textureWidth;
    public final int textureHeight;
    public final int renderWidth;
    public final int renderHeight;
    public final int segmentsX;
    public final int segmentsY;

    public Texture(Identifier identifier, int textureWidth, int textureHeight) {
        this(identifier, textureWidth, textureHeight, textureWidth, textureHeight, 1, 1);
    }

    public Texture(Identifier identifier, int textureWidth, int textureHeight, int renderWidth, int renderHeight, int segmentsX, int segmentsY) {
        this.identifier = identifier;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.renderWidth = renderWidth;
        this.renderHeight = renderHeight;
        this.segmentsX = segmentsX;
        this.segmentsY = segmentsY;
    }

    public void render(DrawContext context, int x, int y) {
        render(context, x, y, (int) ((float) renderWidth / segmentsX), (int) ((float) renderHeight / segmentsY));
    }

    public void render(DrawContext context, int x, int y, int segmentX, int segmentY) {
        context.drawTexture(
                identifier,
                x, y,
                ((float) renderWidth / segmentsX) * segmentX, ((float) renderHeight / segmentsY) * segmentY,
                renderWidth / segmentsX, renderHeight / segmentsY,
                textureWidth, textureHeight
        );
    }

    public void render(DrawContext drawContext, int x, int y, int segmentX, int segmentY,  int renderWidth, int renderHeight) {
        setTexture(identifier);
        drawContext.drawTexture(
                identifier,
                x, y,
                segmentX, segmentY,
                renderWidth / 2, renderHeight / 2,
                textureWidth, textureHeight
        );
        drawContext.drawTexture(
                identifier,
                (int) (x + (renderWidth / 2f)), y,
                segmentX + textureWidth - (renderWidth / 2f), segmentY,
                renderWidth / 2, renderHeight / 2,
                textureWidth, textureHeight
        );
        drawContext.drawTexture(
                identifier,
                x, (int) (y + (renderHeight / 2f)),
                segmentX, segmentY + (renderHeight / 2f),
                renderWidth / 2, renderHeight / 2,
                textureWidth, textureHeight
        );
        drawContext.drawTexture(
                identifier,
                (int) (x + (renderWidth / 2f)), (int) (y + (renderHeight / 2f)),
                segmentX + textureWidth - (renderWidth / 2f), segmentY + (renderHeight / 2f),
                renderWidth / 2, renderHeight / 2,
                textureWidth, textureHeight
        );
    }

    public static void setTexture(Identifier texture){
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
    }
}
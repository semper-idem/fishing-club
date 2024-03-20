package net.semperidem.fishingclub.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class Texture {
    Identifier identifier;
    public final int textureWidth;
    public final int textureHeight;
    public final int renderWidth;
    public final int renderHeight;

    public Texture(Identifier identifier, int textureWidth, int textureHeight) {
        this(identifier, textureWidth, textureHeight, textureWidth, textureHeight);
    }

    Texture(Identifier identifier, int textureWidth, int textureHeight, int renderWidth, int renderHeight) {
        this.identifier = identifier;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.renderWidth = renderWidth;
        this.renderHeight = renderHeight;
    }

    public void render(MatrixStack matrixStack, int x, int y) {
        setTexture(identifier);
        DrawableHelper.drawTexture(
                matrixStack,
                x, y,
                0, 0,
                renderWidth, renderHeight,
                textureWidth, textureHeight
        );
    }

    public void render(MatrixStack matrixStack, int x, int y, int u, int v,  int renderWidth, int renderHeight) {
        setTexture(identifier);
        DrawableHelper.drawTexture(
                matrixStack,
                x, y,
                u, v,
                renderWidth / 2, renderHeight / 2,
                textureWidth, textureHeight
        );
        DrawableHelper.drawTexture(
                matrixStack,
                (int) (x + (renderWidth / 2f)), y,
                u + textureWidth - (renderWidth / 2f), v,
                renderWidth / 2, renderHeight / 2,
                textureWidth, textureHeight
        );
        DrawableHelper.drawTexture(
                matrixStack,
                x, (int) (y + (renderHeight / 2f)),
                u, v + (renderHeight / 2f),
                renderWidth / 2, renderHeight / 2,
                textureWidth, textureHeight
        );
        DrawableHelper.drawTexture(
                matrixStack,
                (int) (x + (renderWidth / 2f)), (int) (y + (renderHeight / 2f)),
                u + textureWidth - (renderWidth / 2f), v + (renderHeight / 2f),
                renderWidth / 2, renderHeight / 2,
                textureWidth, textureHeight
        );
    }

    public static void setTexture(Identifier texture){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
    }
}
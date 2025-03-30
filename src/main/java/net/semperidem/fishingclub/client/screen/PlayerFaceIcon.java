package net.semperidem.fishingclub.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class PlayerFaceIcon implements Drawable {
    private static final int faceTextureSize = 32;
    private static final int hatSkinTextureOffsetX = 160;
    private static final int faceSkinTextureOffsetX = 32;
    private static final int hatSkinTextureOffsetY = 32;
    private static final int faceSkinTextureOffsetY = 32;
    private final float scale;
    private final int x;
    private final int y;
    private final Identifier playerTexture;

    public PlayerFaceIcon(Identifier playerTexture, int x, int y, float scale) {
        this.playerTexture = playerTexture;
        this.x = x;
        this.y = y;
        this.scale = scale;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        render(context, playerTexture, x, y, scale);
    }

    public static void render(DrawContext context, Identifier playerTexture, int x, int y, float scale) {
        if (playerTexture == null) {
            return;//log maybe?
        }
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, scale);
        drawTexture(context, playerTexture, x, y, faceSkinTextureOffsetX,faceSkinTextureOffsetY, scale);
        drawTexture(context, playerTexture, x, y, hatSkinTextureOffsetX, hatSkinTextureOffsetY, scale);
        context.getMatrices().pop();
    }


    private static void drawTexture(DrawContext context, Identifier playerTexture, int x, int y, float u, float v, float scale) {
        context.drawTexture(
                RenderLayer::getGuiTextured,
                playerTexture,
                (int) (x / scale),
                (int) (y / scale),
                u,
                v,
                faceTextureSize,
                faceTextureSize,
                256,
                128
        );
    }
}
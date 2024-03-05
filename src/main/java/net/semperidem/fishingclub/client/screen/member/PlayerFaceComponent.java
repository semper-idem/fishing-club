package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.client.screen.Texture;

public class PlayerFaceComponent extends DrawableHelper implements Drawable {
    private static final int faceTextureSize = 32;
    private static final int hatSkinTextureOffsetX = 160, faceSkinTextureOffsetX = 32, hatSkinTextureOffsetY = 32, faceSkinTextureOffsetY = 32;
    int x, y;
    private Identifier playerIcon;;

    public PlayerFaceComponent(Identifier playerIcon, int x, int y) {
        this.playerIcon = playerIcon;
        this.x = x;
        this.y = y;
    }
    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        Texture.setTexture(playerIcon);
        drawTexture(matrixStack, x, y, faceSkinTextureOffsetX,faceSkinTextureOffsetY, faceTextureSize, faceTextureSize);
        drawTexture(matrixStack, x, y, hatSkinTextureOffsetX,hatSkinTextureOffsetY,faceTextureSize,faceTextureSize);

    }
}
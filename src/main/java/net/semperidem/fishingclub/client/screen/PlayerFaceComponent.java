package net.semperidem.fishingclub.client.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.util.Identifier;

public class PlayerFaceComponent implements Drawable {
    private static final int faceTextureSize = 32;
    private static final int hatSkinTextureOffsetX = 160,
            faceSkinTextureOffsetX = 32, hatSkinTextureOffsetY = 32, faceSkinTextureOffsetY = 32;
    int x, y;
    private Identifier playerIcon;;

    public PlayerFaceComponent(Identifier playerIcon, int x, int y) {
        this.playerIcon = playerIcon;
        this.x = x;
        this.y = y;
    }
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        Texture.setTexture(playerIcon);
        context.drawTexture(playerIcon, x, y, faceSkinTextureOffsetX,faceSkinTextureOffsetY, faceTextureSize, faceTextureSize);
        context.drawTexture(playerIcon, x, y, hatSkinTextureOffsetX,hatSkinTextureOffsetY,faceTextureSize,faceTextureSize);

    }
}
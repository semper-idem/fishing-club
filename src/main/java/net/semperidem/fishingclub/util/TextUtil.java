package net.semperidem.fishingclub.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextUtil {
    public static final int TEXT_HEIGHT = 12;

    /**
     * Draws text centered at the given x coordinate.
     * @param matrixStack The MatrixStack for rendering.
     * @param text The text to render.
     * @param x The x coordinate to center the text on.
     * @param y The y coordinate for the text.
     * @param color The color of the text.
     */
    public static void drawTextCenteredAt(TextRenderer textRenderer, MatrixStack matrixStack, Text text, int x, int y, int color) {
        int textWidth = textRenderer.getWidth(text);
        int textX = x - textWidth / 2;
        textRenderer.drawWithShadow(matrixStack, text, textX, y, color);
    }

    /**
     * Draws text right-aligned to the given x coordinate.
     * @param matrixStack The MatrixStack for rendering.
     * @param text The text to render.
     * @param x The x coordinate to align the right end of the text to.
     * @param y The y coordinate for the text.
     * @param color The color of the text.
     */
    public static void drawTextRightAlignedTo(TextRenderer textRenderer, MatrixStack matrixStack, Text text, int x, int y, int color) {
        int textWidth = textRenderer.getWidth(text);
        int textX = x - textWidth;
        textRenderer.drawWithShadow(matrixStack, text, textX, y, color);
    }

    public static void drawOutlinedTextRightAlignedTo(TextRenderer textRenderer, MatrixStack matrixStack, Text text, int x, int y, int color, int outline) {
        int textWidth = textRenderer.getWidth(text);
        int textX = x - textWidth;
        drawTextOutline(textRenderer, matrixStack, text, textX, y, outline);
        textRenderer.draw(matrixStack, text, textX, y, color);
    }


    public static void drawTextOutline(TextRenderer textRenderer, MatrixStack matrixStack, Text text, int x, int y, int outline){
        textRenderer.draw(matrixStack, text, x + 1, y, outline);
        textRenderer.draw(matrixStack, text, x, y + 1, outline);
        textRenderer.draw(matrixStack, text, x - 1, y, outline);
        textRenderer.draw(matrixStack, text, x, y - 1, outline);
        textRenderer.draw(matrixStack, text, x + 1, y + 1, outline);
        textRenderer.draw(matrixStack, text, x - 1, y + 1, outline);
        textRenderer.draw(matrixStack, text, x - 1, y - 1, outline);
        textRenderer.draw(matrixStack, text, x + 1, y - 1, outline);

    }

}

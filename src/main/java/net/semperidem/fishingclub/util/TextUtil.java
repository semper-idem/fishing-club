package net.semperidem.fishingclub.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TextUtil {
    public static final int TEXT_HEIGHT = 12;

    /**
     * Draws text centered at the given x coordinate.
     * @param matrixStack The MatrixStack for rendering.
     * @param text The text to render.
     * @param centerX The x coordinate to center the text on.
     * @param y The y coordinate for the text.
     * @param color The color of the text.
     */
    public static void drawTextCenteredAt(TextRenderer textRenderer, MatrixStack matrixStack, Text text, int centerX, int y, int color) {
        int textWidth = textRenderer.getWidth(text);
        int textX = centerX - textWidth / 2;
        drawText(textRenderer, matrixStack, text, textX, y, color);
    }

    public static void drawTextCenteredAt(MatrixStack matrixStack, Text text, int centerX, int y, int color) {
        drawTextCenteredAt(MinecraftClient.getInstance().textRenderer, matrixStack, text, centerX, y, color);
    }

    /**
     * Draws text right-aligned to the given x coordinate.
     * @param matrixStack The MatrixStack for rendering.
     * @param text The text to render.
     * @param rightX The x coordinate to align the right end of the text to.
     * @param y The y coordinate for the text.
     * @param color The color of the text.
     */
    public static void drawTextRightAlignedTo(TextRenderer textRenderer, MatrixStack matrixStack, Text text, int rightX, int y, int color) {
        int textWidth = textRenderer.getWidth(text);
        int textX = rightX - textWidth;
        drawText(textRenderer, matrixStack, text, textX, y, color);
    }

    public static void drawTextRightAlignedTo(MatrixStack matrixStack, Text text, int rightX, int y, int color) {
        drawTextRightAlignedTo(MinecraftClient.getInstance().textRenderer, matrixStack, text, rightX, y, color);
    }


    public static void drawText(TextRenderer textRenderer, MatrixStack matrixStack, Text text, int x, int y, int color) {
        textRenderer.drawWithShadow(matrixStack, text, x, y, color);
    }

    public static void drawText(MatrixStack matrixStack, Text text, int x, int y, int color) {
        drawText(MinecraftClient.getInstance().textRenderer, matrixStack, text, x, y, color);
    }

}

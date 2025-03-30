package net.semperidem.fishingclub.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;

public class TextUtil {
    public static final int TEXT_HEIGHT = 12;

    /**
     * Draws text centered at the given x coordinate.
     * @param context The DrawContext for rendering.
     * @param text The text to render.
     * @param x The x coordinate to center the text on.
     * @param y The y coordinate for the text.
     * @param color The color of the text.
     */
    public static void drawTextCenteredAt(TextRenderer textRenderer, DrawContext context, Text text, int x, int y, int color) {
        int textWidth = textRenderer.getWidth(text);
        int textX = x - textWidth / 2;
        context.drawText(textRenderer, text, textX, y, color, true);
    }

    public static void drawTextCenteredAt(TextRenderer textRenderer, DrawContext context, Text text, int x, int y, int color, int outline) {
        int textWidth = textRenderer.getWidth(text);
        int textX = x - textWidth / 2;
        drawTextOutline(textRenderer, context, text, textX, y, outline);
        context.drawText(textRenderer, text, textX, y, color, true);
    }
    public static void drawTextRightAlignedTo(TextRenderer textRenderer, DrawContext context, Text text, int x, int y, int color) {
        int textWidth = textRenderer.getWidth(text);
        int textX = x - textWidth;
        context.drawText(textRenderer, text, textX, y, color, false);
    }

    public static void drawTextRightAlignedTo(TextRenderer textRenderer, DrawContext context, Text text, int x, int y, int color, int outline) {
        int textWidth = textRenderer.getWidth(text);
        int textX = x - textWidth;
        drawTextOutline(textRenderer, context, text, textX, y, outline);
        context.drawText(textRenderer, text, textX, y, color, false);
    }


    public static void drawTextOutline(TextRenderer textRenderer, DrawContext context, Text text, int x, int y, int outline){
        context.drawText(textRenderer, text, x + 1, y, outline, false);
        context.drawText(textRenderer, text, x, y + 1, outline, false);
        context.drawText(textRenderer, text, x - 1, y, outline, false);
        context.drawText(textRenderer, text, x, y - 1, outline, false);
        context.drawText(textRenderer, text, x + 1, y + 1, outline, false);
        context.drawText(textRenderer, text, x - 1, y + 1, outline, false);
        context.drawText(textRenderer, text, x - 1, y - 1, outline, false);
        context.drawText(textRenderer, text, x + 1, y - 1, outline, false);

    }

    public static void drawTextOutline(TextRenderer textRenderer, DrawContext context, OrderedText text, int x, int y, int color, int outline){
        //textRenderer.drawWithOutline(text, x, y, color, outline, context.getMatrices().peek().getPositionMatrix(), context.getVertexConsumers(), 15728880);
    }

    public static OrderedText toOrderedText(TextRenderer textRenderer, Text text) {
        return textRenderer.wrapLines(text, 1000).getFirst();
    }
}

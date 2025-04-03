package net.semperidem.fishingclub.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

public interface TextHelper {

    TextRenderer getTextRenderer();

    default void drawTextCentered( DrawContext context, Text text, int x, int y, int color) {
        int textWidth = getTextRenderer().getWidth(text);
        int textX = x - textWidth / 2;
        context.drawText(getTextRenderer(), text, textX, y, color, true);
    }

    default void drawTextCentered(DrawContext context, Text text, int x, int y, int color, int outline) {
        int textX = x - getTextRenderer().getWidth(text) / 2;
        drawThickTextOutline(context, text, textX, y, outline);
        context.drawText(getTextRenderer(), text, textX, y, color, true);
    }
    default void drawTextRight(DrawContext context, Text text, int x, int y, int color) {
        context.drawText(getTextRenderer(), text, x - getTextRenderer().getWidth(text), y, color, false);
    }

    default void drawTextRight(DrawContext context, Text text, int x, int y, int color, int outline) {
        int textX = x - getTextRenderer().getWidth(text);
        drawThickTextOutline(context, text, textX, y, outline);
        context.drawText(getTextRenderer(), text, textX, y, color, false);
    }


    default void drawTextOutline(DrawContext context, Text text, int x, int y, int outline){
        context.drawText(getTextRenderer(), text, x + 1, y, outline, false);
        context.drawText(getTextRenderer(), text, x, y + 1, outline, false);
        context.drawText(getTextRenderer(), text, x - 1, y, outline, false);
        context.drawText(getTextRenderer(), text, x, y - 1, outline, false);
    }

    default void drawThickTextOutline(DrawContext context, Text text, int x, int y, int outline){
        drawTextOutline(context, text, x, y, outline);
        context.drawText(getTextRenderer(), text, x + 1, y + 1, outline, false);
        context.drawText(getTextRenderer(), text, x - 1, y + 1, outline, false);
        context.drawText(getTextRenderer(), text, x - 1, y - 1, outline, false);
        context.drawText(getTextRenderer(), text, x + 1, y - 1, outline, false);
    }

    default void drawText(DrawContext context, Text text, int x, int y, int color) {
        context.drawText(getTextRenderer(), text, x, y, color, false);
    }

    default void drawText(DrawContext context, Text text, int x, int y, int color, float scale) {
        drawText(context, text, x, y, color, scale, false);
    }

    default void drawText(DrawContext context, Text text, int x, int y, int color, float scale, boolean shadow) {
        context.getMatrices().push();
        context.getMatrices().scale(scale, scale, scale);
        context.drawText(getTextRenderer(), text, (int) (x / scale), (int) (y / scale), color, shadow);
        context.getMatrices().pop();
    }
}

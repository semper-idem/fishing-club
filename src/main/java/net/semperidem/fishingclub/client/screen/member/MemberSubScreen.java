package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public interface MemberSubScreen {
    void init();
    ArrayList<Drawable> getComponents();
    void handledScreenTick();
    boolean mouseScrolled(double mouseX, double mouseY, double amount);

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }
    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }
    void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta);

    default boolean charTyped(char chr, int modifiers) {
        return false;
    }
}

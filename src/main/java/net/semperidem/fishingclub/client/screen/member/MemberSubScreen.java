package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;

public interface MemberSubScreen {
    void init();
    ArrayList<Drawable> getComponents();
    void handledScreenTick();
    boolean mouseScrolled(double mouseX, double mouseY, double amount);
    boolean keyPressed(int keyCode, int scanCode, int modifiers);
    void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta);
}

package net.semperidem.fishing_club.client.screen.member;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;

import java.util.ArrayList;

public interface IMemberSubScreen {
    void init();
    ArrayList<Drawable> getComponents();
    default void handledScreenTick() {}

    default boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        for (Drawable component : getComponents()) {
            if (!(component instanceof ScrollableWidget scrollableWidget)) {
                continue;
            }

            if (scrollableWidget.mouseScrolled(mouseX, mouseY, horizontal, vertical)) {
                return true;
            }
        }
        return false;
    }
    int unlockLevel();

    default boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Drawable component : Lists.reverse(getComponents())) {
            if (!(component instanceof ClickableWidget clickableWidget)) {
                continue;
            }
            if (clickableWidget.mouseClicked(mouseX, mouseY, button)) {

                    return true;
            }
        }
        return false;
    }

    default boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for (Drawable component : getComponents()) {
            if ((component instanceof PressableWidget pressableWidget)) {
                if (pressableWidget.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }
            } else if (component instanceof TextFieldWidget textFieldWidget) {
                if (textFieldWidget.keyPressed(keyCode, scanCode, modifiers)) {
                    return true;
                }
            }
        }
        return false;
    }

    void render(DrawContext context, int mouseX, int mouseY, float delta);

    default boolean charTyped(char chr, int modifiers) {
        for (Drawable component : getComponents()) {
            if (!(component instanceof TextFieldWidget textFieldWidget)) {
                continue;
            }

            if (textFieldWidget.charTyped(chr, modifiers)) {
                return true;
            }
        }
        return false;
    }
}

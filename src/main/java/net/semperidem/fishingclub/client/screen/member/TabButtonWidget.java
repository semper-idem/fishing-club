package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class TabButtonWidget extends ButtonWidget  {
    private final MemberScreen parent;
    private final MemberSubScreen tabView;
    public TabButtonWidget(int x, int y, int width, int height, Text message, MemberScreen parent, MemberSubScreen tabView) {
        super(x, y, width, height, message, button -> parent.setCurrentView(tabView));
        this.tabView = tabView;
        this.parent = parent;
    }

    public void resize(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private boolean isSelected() {
        return parent.getCurrentView() == tabView;
    }

}

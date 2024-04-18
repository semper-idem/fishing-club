package net.semperidem.fishingclub.client.screen;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.client.screen.member.MemberButton;

public class CacheAwareButtonWidget extends MemberButton {
    Cacheable parent;
    public CacheAwareButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, Cacheable parent) {
        super(x, y, width, height, message, onPress);
        this.parent = parent;
    }

    @Override
    public void onPress() {
        super.onPress();
        parent.updateCache();
    }
}

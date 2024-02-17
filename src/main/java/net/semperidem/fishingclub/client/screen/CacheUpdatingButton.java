package net.semperidem.fishingclub.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class CacheUpdatingButton extends ButtonWidget {
    public CacheUpdatingButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }

    @Override
    public void onPress() {
        super.onPress();
        if (MinecraftClient.getInstance().currentScreen instanceof Cacheable cacheable) {
            cacheable.updateCache();
        }
    }
}

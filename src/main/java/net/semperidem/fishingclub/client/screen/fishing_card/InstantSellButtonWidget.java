package net.semperidem.fishingclub.client.screen.fishing_card;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.client.screen.CacheAwareButtonWidget;
import net.semperidem.fishingclub.client.screen.Cacheable;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;

public class InstantSellButtonWidget extends CacheAwareButtonWidget {
    private static final String title = "Sell";
    private final FishingCardScreenHandler parentHandler;
    public InstantSellButtonWidget(int x, int y, int width, int height, FishingCardScreenHandler handler, Cacheable parent) {
        super(x, y, width, height, Text.of(title), button -> {
            handler.instantSell();
        }, parent);
        this.parentHandler = handler;
    }

    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!parentHandler.shouldRenderSellButton()) {
            return;
        }
        super.renderWidget(context, mouseX, mouseY, delta);
    }
}

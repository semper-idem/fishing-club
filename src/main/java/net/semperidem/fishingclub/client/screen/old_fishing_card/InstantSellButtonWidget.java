package net.semperidem.fishingclub.client.screen.old_fishing_card;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.client.screen.CacheAwareButtonWidget;
import net.semperidem.fishingclub.client.screen.Cacheable;
import net.semperidem.fishingclub.screen.old_card.OldFishingCardScreenHandler;

public class InstantSellButtonWidget extends CacheAwareButtonWidget {
    private static final String title = "Sell";
    private final OldFishingCardScreenHandler parentHandler;
    public InstantSellButtonWidget(int x, int y, int width, int height, OldFishingCardScreenHandler handler, Cacheable parent) {
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

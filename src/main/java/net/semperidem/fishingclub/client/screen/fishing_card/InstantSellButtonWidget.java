package net.semperidem.fishingclub.client.screen.fishing_card;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.client.screen.CacheAwareButtonWidget;
import net.semperidem.fishingclub.client.screen.Cacheable;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;

public class InstantSellButtonWidget extends CacheAwareButtonWidget {
    private static final String title = "Sell";
    private final FishingCardScreenHandler parentHandler;
    public InstantSellButtonWidget(int x, int y, int width, int height, FishingCardScreenHandler handler, Cacheable parent) {
        super(x, y, width, height, Text.of(title), button -> {
            handler.instantSell();
            ClientPacketSender.instantSellSlot();
        }, parent);
        this.parentHandler = handler;
    }

    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!parentHandler.shouldRenderSellButton()) {
            return;
        }
        super.renderButton(matrices, mouseX, mouseY, delta);
    }
}

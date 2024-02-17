package net.semperidem.fishingclub.client.screen.fishing_card;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.client.screen.CacheUpdatingButton;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.screen.FishingCardScreenHandler;

public class InstantSellButton extends CacheUpdatingButton {
    private static final String title = "Sell";
    private final FishingCardScreenHandler parent;
    public InstantSellButton(int x, int y, int width, int height, FishingCardScreenHandler handler) {
        super(x, y, width, height, Text.of(title), button -> {
            handler.instantSell();
            ClientPacketSender.instantSellSlot();
        });
        this.parent = handler;
    }

    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!parent.shouldRenderSellButton()) {
            return;
        }
        super.renderButton(matrices, mouseX, mouseY, delta);
    }
}

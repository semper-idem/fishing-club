package net.semperidem.fishingclub.client.screen.shop.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.client.screen.shop.ShopBuyScreen;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class OfferEntry extends AlwaysSelectedEntryListWidget.Entry<OfferEntry> {
    private static final Identifier TEXTURE_EMPTY = new Identifier(MOD_ID,"textures/gui/shop_buy_empty.png");
    private final OfferListWidget offerListWidget;
    final OfferEntryData offer;
    ShopBuyScreen screen;

    public OfferEntry(OfferListWidget offerListWidget, ShopBuyScreen shopBuyScreen, OfferEntryData offer) {
        this.offerListWidget = offerListWidget;
        this.offer = offer;
        this.screen = shopBuyScreen;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int itemWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float tickDelta) {
        matrices.push();
        if (y < screen.getY()) {
            return;
        }
        renderBackground(matrices, x, y,itemWidth, itemHeight, isSelected);
        // Render the offer here. You can use the TextRenderer to render text.
        ItemStack renderedStack = offer.offerItem.getDefaultStack();
        renderedStack.setCount(offer.batchSize);
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(renderedStack, x + 1,y+1);
        if (offer.batchSize == 1) {
            MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, renderedStack, x,y+1, "1");
        }
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, renderedStack, x,y+1);
        String priceString = "$"+offer.lastPrice;
        int priceStringLength = offerListWidget.textRenderer.getWidth(priceString);
        offerListWidget.textRenderer.drawWithShadow(matrices, priceString, x + itemWidth - priceStringLength - 2, y + 8, 0xFFFFFF);
        matrices.pop();
    }


    private void renderBackground(MatrixStack matrices, int x, int y, int itemWidth, int itemHeight, boolean isSelected){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_EMPTY);
        screen.drawTexture(matrices, x, y, 0, 236, itemWidth, itemHeight);
    }

    @Override
    public Text getNarration() {
        return Text.of(offer.getName() + " - " + offer.getPrice());
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        screen.orderListWidget.addOrUpdateEntry(new OrderEntry(screen.orderListWidget, screen, new OrderEntryData(offer)));
        int countInBasket = screen.orderListWidget.getCountFor(this);
        offer.lastPrice = offer.getPriceForNth(countInBasket + 1);
        return true;
    }

    public boolean keyPressed(int i, int j, int k) {
            OfferListWidget offerListWidget = this.screen.offerListWidget;
            int l = offerListWidget.children().indexOf(this);
            if (l == -1) {
                return true;
            }

            if (i == 264 && l < this.screen.offers.size() - 1 || i == 265 && l > 0) {
                this.swapEntries(l, i == 264 ? l + 1 : l - 1);
                return true;
            }
        return super.keyPressed(i, j, k);
    }


    private void swapEntries(int i, int j) {
        OfferEntryData temp = this.screen.offers.get(i);
        this.screen.offers.set(i, this.screen.offers.get(j));
        this.screen.offers.set(j, temp);
        OfferEntry entry = this.screen.offerListWidget.children().get(j);
        this.screen.offerListWidget.setSelected(entry);
        this.screen.offerListWidget.ensureVisible(entry);
    }
}

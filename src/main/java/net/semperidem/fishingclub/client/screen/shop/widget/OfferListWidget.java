package net.semperidem.fishingclub.client.screen.shop.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.semperidem.fishingclub.client.screen.shop.ShopBuyScreen;

import java.util.ArrayList;

public class OfferListWidget extends AlwaysSelectedEntryListWidget<OfferEntry> {
    TextRenderer textRenderer;

    public OfferListWidget(MinecraftClient client, int width, int height, int x, int y, int itemHeight) {
        super(client, width, height, y, y + height, itemHeight);
        this.left = x;
        this.right = x + width;
        textRenderer = MinecraftClient.getInstance().textRenderer;
        setRenderBackground(false);
        setRenderHeader(false, 0);
        setRenderSelection(false);
        setRenderHorizontalShadows(false);
    }

    public void ensureVisible(OfferEntry entry){
        super.ensureVisible(entry);
    }


    @Override
    public int getRowWidth() {
        return width;
    }


    protected int getScrollbarPositionX() {
        return right + 4;
    }

    protected int getRowTop(int i) {
        return this.top + 1 - (int)this.getScrollAmount() + i * this.itemHeight;
    }

    public int getMaxScroll() {
        return Math.max(0, this.getMaxPosition() - (this.bottom - this.top) - 1);
    }

    private int getRowBottom(int i) {
        return this.getRowTop(i) + this.itemHeight;
    }

    public void addEntries(ShopBuyScreen shopBuyScreen, ArrayList<OfferEntryData> entries){
        for(OfferEntryData offerEntryData : entries) {
            this.addEntry(new OfferEntry(this, shopBuyScreen, offerEntryData));
        }
        setSelected(getEntry(0));
    }

    protected void renderList(MatrixStack matrixStack, int i, int j, float f) {
        int k = this.getRowLeft();
        int l = this.getRowWidth();
        int m = this.itemHeight;
        int n = this.getEntryCount();

        for(int o = 0; o < n; ++o) {
            int p = this.getRowTop(o);
            int q = this.getRowBottom(o);
            if (q >= this.top && p <= this.bottom) {
                this.renderEntry(matrixStack, i, j, f, o, k, p, l, m);
            }
        }

    }
}
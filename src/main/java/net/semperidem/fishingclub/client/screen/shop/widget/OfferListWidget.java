package net.semperidem.fishingclub.client.screen.shop.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.semperidem.fishingclub.client.screen.shop.ShopScreen;

public class OfferListWidget extends AlwaysSelectedEntryListWidget<OfferEntry> {
    TextRenderer textRenderer;
    ShopScreen parent;

    public OfferListWidget(ShopScreen parent, int width, int height, int x, int y) {
        super(parent.getClient(), width, height, y, y + height, 20);
        if(parent.getClient() == null) {
            return;
        }
        this.parent = parent;
        this.left = x;
        this.right = x + width;
        textRenderer = parent.getClient().textRenderer;
        setRenderBackground(false);
        setRenderHeader(false, 0);
        setRenderSelection(false);
        setRenderHorizontalShadows(false);

        initCatalog();
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

    protected void renderList(MatrixStack matrixStack, int i, int j, float f) {
        for(int o = 0; o < this.getEntryCount(); ++o) {
            int p = this.getRowTop(o);
            int q = this.getRowBottom(o);
            if (q >= this.top && p <= this.bottom) {
                this.renderEntry(matrixStack, i, j, f, o, this.getRowLeft(), p, this.getRowWidth(), this.itemHeight);
            }
        }

    }

    public void updatePrices(){
        int entryCount = this.getEntryCount();
        for(int i = 0; i < entryCount; i++) {
            OfferEntry entry = this.getEntry(i);
            int countInBasket = parent.orderListWidget.getCountFor(entry);
            entry.offer.lastPrice = entry.offer.getPriceForNth(countInBasket + 1);
        }
    }


    public void initCatalog(){
        for(int i = 0; i < 20; i++) {
            this.addEntry(new OfferEntry(this, parent, new OfferEntryData(
                            Items.DIAMOND,
                            i,
                            i,
                            1f,
                            1f
                    )));
        }
        this.addEntry(new OfferEntry(this, parent, new OfferEntryData(
                Items.EMERALD,
                8,
                4,
                .1f,
                0.5f
        )));
        setSelected(getEntry(0));
    }
}
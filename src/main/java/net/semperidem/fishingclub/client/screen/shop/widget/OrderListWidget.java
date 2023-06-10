package net.semperidem.fishingclub.client.screen.shop.widget;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

public class OrderListWidget extends AlwaysSelectedEntryListWidget<OrderEntry> {
    TextRenderer textRenderer;

    public OrderListWidget(MinecraftClient client, int width, int height, int x, int y, int itemHeight) {
        super(client, width, height, y, y + height, itemHeight);
        this.left = x;
        this.right = x + width;
        textRenderer = MinecraftClient.getInstance().textRenderer;
        setRenderBackground(false);
        setRenderHeader(false, 0);
        setRenderSelection(false);
        setRenderHorizontalShadows(false);
    }

    public void ensureVisible(OrderEntry entry){
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


    public void addOrUpdateEntry(OrderEntry entry){
        for(OrderEntry orderEntry : this.children()){
            if (orderEntry.matches(entry)) {
                orderEntry.increaseCount();
                return;
            }
        }
        this.children().add(entry);
    }
    public void decreaseCount(OrderEntry orderEntry){
        if (orderEntry.decreaseCount()) {
            removeEntry(orderEntry);
        }
    }


    protected void renderList(MatrixStack matrixStack, int i, int j, float f) {
        int k = this.getRowLeft();
        int l = this.getRowWidth();
        int n = this.getEntryCount();

        for(int o = 0; o < n; ++o) {
            int p = this.getRowTop(o);
            int q = this.getRowBottom(o);
            if (q >= this.top && p <= this.bottom) {
                this.renderEntry(matrixStack, i, j, f, o, k, p, l, this.itemHeight);
            }
        }
    }

    public int getCountFor(OfferEntry offerEntry) {
        for(int i = 0; i < getEntryCount(); i++) {
            OrderEntry oe = getEntry(i);
            if (oe.matches(offerEntry)) {
                return oe.getCount();
            }

        }
        return 0;
    }

    public int getBasketTotal(){
        int basketTotal = 0;
        for(OrderEntry orderEntry : this.children()) {
            basketTotal += orderEntry.getTotal();
        }
        return basketTotal;
    }
}

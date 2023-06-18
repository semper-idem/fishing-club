package net.semperidem.fishingclub.client.screen.shop.widget;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.semperidem.fishingclub.client.screen.shop.ShopScreen;

import java.util.ArrayList;

public class OrderListWidget extends AlwaysSelectedEntryListWidget<OrderEntry> {
    TextRenderer textRenderer;
    ShopScreen parent;

    public OrderListWidget(ShopScreen parent, int width, int height, int x, int y) {
        super(parent.getClient(), width, height, y, y + height, 20);
        OrderListWidget previousInstance = parent.orderListWidget;
        if (previousInstance != null) {
            int entryCount = previousInstance.getEntryCount();
            if (entryCount > 0) {
                for( int i = 0; i < entryCount; i++) {
                    this.addEntry(previousInstance.getEntry(i));
                }
                parent.offerListWidget.updatePrices();
            }
        }
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

    public ArrayList<OrderEntryData> getBasektData(){
        ArrayList<OrderEntryData> basket = new ArrayList<>();
        int entryCount = getEntryCount();
        for(int i = 0; i < entryCount; i++) {
            basket.add(this.getEntry(i).getData());
        }
        return basket;
    }


    public void clear(){
        clearEntries();
        parent.offerListWidget.updatePrices();
    }
}

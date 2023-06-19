package net.semperidem.fishingclub.client.screen.shop;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.util.ArrayList;
import java.util.Objects;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler.ROW_COUNT;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class ShopScreen extends HandledScreen<ShopScreenHandler> implements ScreenHandlerProvider<ShopScreenHandler> {
    private static final Identifier TEXTURE_SELL = new Identifier(MOD_ID,"textures/gui/shop_sell.png");
    private static final Identifier TEXTURE_BUY = new Identifier(MOD_ID,"textures/gui/shop_buy.png");
    private static final Identifier MONEY_WIDGET = new Identifier(MOD_ID,"textures/gui/money_widget.png");
    private static final Identifier SHOP_BUTTONS = new Identifier(MOD_ID,"textures/gui/shop_buttons_64x64.png");
    private static final Identifier TEXTURE_EMPTY = new Identifier(MOD_ID,"textures/gui/shop_buy_empty.png");
    private int animationTick = 0;

    public OfferListWidget offerListWidget;
    public OrderListWidget orderListWidget;
    ScreenType screenType;

    private int lastBalanceChange;


    public ShopScreen(ShopScreenHandler shopSellScreenHandler, PlayerInventory playerInventory, Text text) {
        super(shopSellScreenHandler, playerInventory, text);
        this.screenType = ScreenType.SELL;
        this.passEvents = false;
        this.backgroundHeight = 114 + ROW_COUNT * SLOT_SIZE;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    public MinecraftClient getClient(){
        return MinecraftClient.getInstance();
    }


    protected void init() {
        this.clearChildren();
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        switch (screenType) {
            case SELL -> this.initSell();
            case BUY -> this.initBuy();
        }
        this.addSwapScreenButton();
    }

    public void initSell(){
        addSellButton();
    }
    public void initBuy(){
        this.offerListWidget = new OfferListWidget( 53, 107, x + 6, y + 17);
        this.orderListWidget = new OrderListWidget( 88, 107, x + 69, y + 17);
        this.addCheckoutButton();
    }

    protected void addSellButton() {
        this.addDrawableChild(new ButtonWidget(this.x + 175, this.y + 103, 70, 20, Text.of("Sell"), (buttonWidget) -> {
            if (this.handler.sellContainer()) {
                this.animationTick = 300;
            }
        }));
    }

    protected void addSwapScreenButton() {
        this.addDrawableChild(new TexturedButtonWidget(this.x + 178, this.y + 128, 32 , 32,(screenType !=  ScreenType.BUY ? 0 : 32), 0, 0, SHOP_BUTTONS, 64, 64,(buttonWidget) -> {
            for(ScreenType nextScreenType : ScreenType.values()) {
                if (screenType != nextScreenType) {
                    this.screenType = nextScreenType;
                    this.init();
                    break;
                }
            }
        }, ScreenTexts.EMPTY));
    }


    public void render(MatrixStack matrixStack, int i, int j, float f) {
        renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        drawBalanceWidget(matrixStack);
        drawMouseoverTooltip(matrixStack, i, j);
    }


    protected void drawBackground(MatrixStack matrixStack, float f, int i, int j) {
        switch (screenType) {
            case SELL -> drawSellBackground(matrixStack,f,i,j);
            case BUY -> drawBuyBackground(matrixStack,f,i,j);
        }
    }


    protected void drawSellBackground(MatrixStack matrixStack, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_SELL);
        this.drawTexture(matrixStack, this.x, this.y, 0, 0, 256, ROW_COUNT * SLOT_SIZE + 17);
        this.drawTexture(matrixStack, this.x, this.y + ROW_COUNT * SLOT_SIZE + 17, 0, 126, 256, 96);
    }


    protected void drawBuyBackground(MatrixStack matrixStack, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_BUY);
        this.drawTexture(matrixStack, this.x, this.y, 0, 0, 256, ROW_COUNT * SLOT_SIZE + 17);
        this.drawTexture(matrixStack, this.x, this.y + ROW_COUNT * SLOT_SIZE + 17, 0, 126, 256, 96);

        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();

        this.orderListWidget.render(matrixStack,i,j,f);
        this.offerListWidget.render(matrixStack,i,j,f);

        RenderSystem.setShaderTexture(0, TEXTURE_EMPTY);
        this.drawTexture(matrixStack, this.x, this.y, 0, 0, 256, ROW_COUNT * SLOT_SIZE + 17);
        this.drawTexture(matrixStack, this.x, this.y + ROW_COUNT * SLOT_SIZE + 17, 0, 126, 256, 96);
        matrixStack.push();
        matrixStack.translate(x, y, 200.0F);
        drawForeground(matrixStack,i,j);

        matrixStack.pop();
    }


    protected void addCheckoutButton() {
        this.addDrawableChild(new ButtonWidget(this.x + 175, this.y + 103, 70, 20, Text.of("Checkout"), (buttonWidget) -> {
            if(orderListWidget.buyContainer()){
                //Handle Success
            } else {
                //Handle Fail
            }
        }));
    }


    private void drawBalanceWidget(MatrixStack matrixStack){
        matrixStack.push();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MONEY_WIDGET);
        int widgetX = this.x + 172;
        int widgetY = this.y + 17;
        if (animationTick > 0) {
            animationTick-=2;
            // matrixStack.push();
            //matrixStack.translate(0, 0, -100); good idea bad execution
            drawTexture(matrixStack, widgetX, widgetY + Math.min(animationTick, 200) /10, 0, 0, 80, 40, 128, 128);
            String gainString = "+$";
            int gainTextWidth = textRenderer.getWidth(gainString);
            this.textRenderer.drawWithShadow(matrixStack,gainString, widgetX + 74 -gainTextWidth , widgetY + Math.min(animationTick, 200) /10f + 25, 0xcdcdf7);
            //matrixStack.pop();
        }
        RenderSystem.setShaderTexture(0, MONEY_WIDGET);
        drawTexture(matrixStack, widgetX, widgetY, 0, 0, 80, 40, 128, 128);

        this.textRenderer.drawWithShadow(matrixStack,"Balance:", widgetX + 6, widgetY + 6, 0xcdcdf7);
        String balanceString = "$" + FisherInfos.getClientInfo().getFisherCredit();
        int balanceStringWidth = textRenderer.getWidth(balanceString);
        this.textRenderer.drawWithShadow(matrixStack, balanceString, widgetX + 74 - balanceStringWidth, widgetY + 25, 0xcdcdf7);
        matrixStack.pop();
    }



    protected void drawForeground(MatrixStack matrixStack, int i, int j) {
        int containerValue = getContainerValue();
        String containerValueString = "Total: $" + containerValue;
        int containerValueStringWidth = textRenderer.getWidth(containerValueString);

        String title = screenType.toString().toLowerCase();
        this.textRenderer.draw(matrixStack, title.substring(0, 1).toUpperCase() + title.substring(1), (float)this.titleX, (float)this.titleY, 4210752);
        this.textRenderer.draw(matrixStack, containerValueString, 169 -containerValueStringWidth , (float)this.titleY, 4210752);
        this.textRenderer.draw(matrixStack, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);
    }

    private int getContainerValue(){
        switch (screenType) {
            case SELL -> {
                return this.handler.getSellContainerValue();
            }
            case BUY -> {
                return this.orderListWidget.getBasketTotal();
            }
        }
        return 0;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (Objects.requireNonNull(screenType) == ScreenType.BUY) {
            return mouseBuyClicked(mouseX, mouseY, button);
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseBuyClicked(double mouseX, double mouseY, int button) {
        if (mouseX > x + 69) {
            return this.orderListWidget.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
        }
        return this.offerListWidget.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (Objects.requireNonNull(screenType) == ScreenType.BUY) {
            return mouseBuyReleased(mouseX, mouseY, button);
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseBuyReleased(double mouseX, double mouseY, int button) {
        return this.offerListWidget.mouseReleased(mouseX, mouseY, button) || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (Objects.requireNonNull(screenType) == ScreenType.BUY) {
            return mouseBuyScrolled(mouseX, mouseY, amount);
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    public boolean mouseBuyScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX > x + 69) {
            return this.orderListWidget.mouseScrolled(mouseX, mouseY, amount) || super.mouseScrolled(mouseX, mouseY, amount);
        }
        return this.offerListWidget.mouseScrolled(mouseX, mouseY, amount) || super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (Objects.requireNonNull(screenType) == ScreenType.BUY) {
            return keyBuyPressed(keyCode, scanCode, modifiers);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean keyBuyPressed(int keyCode, int scanCode, int modifiers) {
        return this.offerListWidget.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (Objects.requireNonNull(screenType) == ScreenType.BUY) {
            return keyBuyReleased(keyCode, scanCode, modifiers);
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }

    public boolean keyBuyReleased(int keyCode, int scanCode, int modifiers) {
        return this.offerListWidget.keyReleased(keyCode, scanCode, modifiers) || super.keyReleased(keyCode, scanCode, modifiers);
    }


    class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry> {
        final Item offerItem;
        final int basePrice;
        final int batchSize;
        final float discountIncrement;
        final float discountMinimum;

        int currentPrice;
        int count;
        int totalPrice;
        boolean isEnabled;

        public Entry(Item offerItem, int basePrice, int batchSize, float discountIncrement, float discountMinimum) {
            this.offerItem = offerItem;
            this.basePrice = basePrice;
            this.batchSize = Math.max(1,batchSize);
            this.discountIncrement = discountIncrement;
            this.discountMinimum = discountMinimum;

            this.currentPrice = basePrice;
            this.count = 0;
            this.totalPrice = 0;
            this.isEnabled = true;
        }

        public ItemStack getItemStack(){
            ItemStack asItemStack = offerItem.getDefaultStack();
            asItemStack.setCount(count * batchSize);
            return asItemStack;
        }


        public int getPriceForCount(int count){
            int total = 0;
            for(int i = 1; i <= count; i++) {
                total += getPriceForNth(i);
            }
            return total;
        }

        public int getPriceForNth(int n){
            return (int)(basePrice * Math.max(discountMinimum, 1 - discountIncrement * (n / 4)));//INTENTIONAL INTEGER IN FLOAT CONTEXT
        }

        public String getName(){
            return offerItem.getName().getString();
        }

        @Override
        public void render(MatrixStack matrices, int index, int y, int x, int itemWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float tickDelta) {
            matrices.push();
            if (y < ShopScreen.this.y) {
                return;
            }
            renderBackground(matrices, x, y,itemWidth, itemHeight);
            renderEntryIcon(x,y);
            renderEntryText(matrices, x, y, itemWidth);
            matrices.pop();
        }

        private void renderEntryIcon(int x, int y){
            ItemStack renderedStack = offerItem.getDefaultStack();
            itemRenderer.renderGuiItemIcon(renderedStack, x + 1,y+1);
            itemRenderer.renderGuiItemOverlay(textRenderer, renderedStack, x,y+1, String.valueOf(batchSize)) ;
        }
        
        private void renderEntryText(MatrixStack matrices, int x, int y, int itemWidth){
            String priceString = "$"+ currentPrice;
            int priceStringLength = textRenderer.getWidth(priceString);
            textRenderer.drawWithShadow(matrices, priceString, x + itemWidth - priceStringLength - 2, y + 8, 0xFFFFFF);
        }


        private void renderBackground(MatrixStack matrices, int x, int y, int itemWidth, int itemHeight){
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, TEXTURE_EMPTY);
            drawTexture(matrices, x, y, isEnabled ? 0 : itemWidth, 236, itemWidth, itemHeight);
        }

        @Override
        public boolean mouseClicked(double d, double e, int i) {
            if(isEnabled) {
                orderListWidget.addToBasket(this);
                currentPrice = getPriceForNth(this.count + 1);
            }
            return true;
        }

        public boolean equals(Entry entry){
            return Objects.equals(entry.getName(), this.getName()) && entry.batchSize == this.batchSize;
        }

        @Override
        public Text getNarration() {
            return Text.of(this.toString());
        }

        @Override
        public String toString(){
            return "Order Entry: " + offerItem.toString() + " x " + batchSize + " x " + count + " for " + totalPrice;
        }
    }



    class OfferListWidget extends AlwaysSelectedEntryListWidget<ShopScreen.Entry> {

        public OfferListWidget(int width, int height, int x, int y) {
            super(getClient(), width, height, y, y + height, 20);
            this.left = x;
            this.right = x + width;

            setRenderBackground(false);
            setRenderHeader(false, 0);
            setRenderSelection(false);
            setRenderHorizontalShadows(false);

            initCatalog();
        }

        public void ensureVisible(ShopScreen.Entry entry){
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
            for(ShopScreen.Entry entry : this.children()) {
                entry.currentPrice = entry.getPriceForNth(entry.count + 1);
            }
        }


        public void initCatalog(){
            for(int i = 0; i < 20; i++) {
                this.addEntry(new ShopScreen.Entry(
                        Items.DIAMOND,
                        i,
                        i,
                        1f,
                        1f
                ));
            }
            this.addEntry(new ShopScreen.Entry(
                    Items.EMERALD,
                    8,
                    4,
                    .1f,
                    0.5f
            ));
        }
    }

    class OrderListWidget extends AlwaysSelectedEntryListWidget<Entry> {
        public OrderListWidget(int width, int height, int x, int y) {
            super(getClient(), width, height, y, y + height, 20);
            this.left = x;
            this.right = x + width;

            setRenderBackground(false);
            setRenderHeader(false, 0);
            setRenderSelection(false);
            setRenderHorizontalShadows(false);
        }
        @Override
        public int getRowWidth() {
            return width;
        }

        protected int getScrollbarPositionX() {
            return right + 4;
        }

        protected int getRowTop(int i) {
            return this.top + 1 - (int) this.getScrollAmount() + i * this.itemHeight;
        }

        public int getMaxScroll() {
            return Math.max(0, this.getMaxPosition() - (this.bottom - this.top) - 1);
        }

        private int getRowBottom(int i) {
            return this.getRowTop(i) + this.itemHeight;
        }

        protected void renderList(MatrixStack matrixStack, int i, int j, float f) {
            int k = this.getRowLeft();
            int l = this.getRowWidth();
            int n = this.getEntryCount();

            for (int o = 0; o < n; ++o) {
                int p = this.getRowTop(o);
                int q = this.getRowBottom(o);
                if (q >= this.top && p <= this.bottom) {
                    this.renderEntry(matrixStack, i, j, f, o, k, p, l, this.itemHeight);
                }
            }
        }

        public boolean buyContainer(){
            int cost = getBasketTotal();
            int currentCredit =  FisherInfos.getClientInfo().getFisherCredit();
            if (cost <= currentCredit) {
                lastBalanceChange = cost;
                ClientPacketSender.buyShopContainer(cost, getBasket());
                clear();
                return true;
            }
            return false;
        }

        public ArrayList<ItemStack> getBasket(){
            ArrayList<ItemStack> basket = new ArrayList<>();
            for(ShopScreen.Entry entry : this.children()) {
                basket.add(entry.getItemStack());
            }
            return basket;
        }


        public void addToBasket(ShopScreen.Entry entry) {
            for (ShopScreen.Entry orderEntry : this.children()) {
                if (orderEntry.equals(entry)) {
                    ((Entry)orderEntry).increaseCount();
                    return;
                }
            }
            this.children().add(entry);
        }

        public void removeFromBasket(Entry entry) {
            entry.decreaseCount();
            if (entry.count == 0) {
                orderListWidget.removeFromBasket(entry);
            }
        }

        public int getBasketTotal() {
            int basketTotal = 0;
            for (ShopScreen.Entry orderEntry : this.children()) {
                basketTotal += orderEntry.totalPrice;
            }
            return basketTotal;
        }

        public void clear() {
            clearEntries();
            offerListWidget.updatePrices();
        }

        private class Entry extends ShopScreen.Entry {
            public Entry(Item offerItem, int basePrice, int batchSize, float discountIncrement, float discountMinimum) {
                super(offerItem, basePrice, batchSize, discountIncrement, discountMinimum);
            }


            @Override
            public void render(MatrixStack matrices, int index, int y, int x, int itemWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float tickDelta) {
                matrices.push();
                if (y < ShopScreen.this.y) {
                    return;
                }
                renderBackground(matrices, x, y,itemWidth, itemHeight);
                renderEntryIcon(x,y);
                renderEntryText(matrices, x, y,itemWidth);
                matrices.pop();
            }


            private void renderBackground(MatrixStack matrices, int x, int y, int itemWidth, int itemHeight){
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, TEXTURE_EMPTY);
                drawTexture(matrices, x, y, 106, 236, itemWidth, itemHeight);
            }

            private void renderEntryIcon(int x, int y){
                ItemStack renderedStack = offerItem.getDefaultStack();
                itemRenderer.renderGuiItemIcon(renderedStack, x + 1,y+1);
                itemRenderer.renderGuiItemOverlay(textRenderer, renderedStack,x,y+1, String.valueOf(batchSize));
            }
            private void renderEntryText(MatrixStack matrices, int x, int y, int itemWidth){
                String priceString = "$"+totalPrice;
                int priceStringLength = textRenderer.getWidth(priceString);
                textRenderer.drawWithShadow(matrices, priceString, x + itemWidth - priceStringLength - 2, y + 8, 0xFFFFFF);
                textRenderer.drawWithShadow(matrices, "x"+count, x + 21, y + 8, 0xFFFFFF);
            }


            @Override
            public boolean mouseClicked(double d, double e, int i) {
                orderListWidget.removeFromBasket(this);
                return true;
            }

            public void decreaseCount(){
                this.count--;
                totalPrice = getPriceForCount(count);
            }

            public void increaseCount(){
                if (count < 99) {
                    count++;
                    totalPrice = getPriceForCount(count);
                }
            }
        }

    }



    enum ScreenType {
        SELL,
        BUY
    }
}

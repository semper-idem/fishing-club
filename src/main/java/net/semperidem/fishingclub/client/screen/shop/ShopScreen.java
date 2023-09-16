package net.semperidem.fishingclub.client.screen.shop;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
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
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.util.ArrayList;
import java.util.Objects;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler.ROW_COUNT;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;

public class ShopScreen extends HandledScreen<ShopScreenHandler> implements ScreenHandlerProvider<ShopScreenHandler> {
    private static final Identifier SELL_BACKGROUND = new Identifier(MOD_ID,"textures/gui/shop_sell.png");
    private static final Identifier BUY_BACKGROUND = new Identifier(MOD_ID,"textures/gui/shop_buy.png");
    private static final Identifier BALANCE_WIDGET = new Identifier(MOD_ID,"textures/gui/money_widget.png");
    private static final Identifier SHOP_BUTTONS = new Identifier(MOD_ID,"textures/gui/shop_buttons_64x64.png");
    private static final Identifier BUY_FRAME = new Identifier(MOD_ID,"textures/gui/shop_buy_frame.png");
    private int animationTick = 0;

    public OfferListWidget offerListWidget;
    public OrderListWidget orderListWidget;
    public BalanceWidget balanceWidget;
    public ButtonWidget checkoutButton;
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
        this.addDrawableChild(new BalanceWidget( this.x + 171, this.y + 17, 80,40));
        this.addSwapScreenButton();
    }

    public void initSell(){
        addSellButton();
    }
    public void initBuy(){
        if (this.offerListWidget == null) {
            this.offerListWidget = new OfferListWidget( 53, 107, x + 6, y + 17);
        }
        if (this.orderListWidget == null) {
            this.orderListWidget = new OrderListWidget( 88, 107, x + 69, y + 17);
        }
        this.addDrawableChild(offerListWidget);
        this.addDrawableChild(orderListWidget);
        this.addCheckoutButton();
    }

    protected void addSellButton() {
        this.addDrawableChild(new ButtonWidget(this.x + 175, this.y + 103, 70, 20, Text.of("Sell"), (buttonWidget) -> {
            if (this.handler.sellContainer()) {
                this.lastBalanceChange = this.handler.getSellContainerValue();
                this.animationTick = 500;
            }
        }));
    }


    protected void addCheckoutButton() {
        checkoutButton = (new ButtonWidget(this.x + 175, this.y + 103, 70, 20, Text.of("Checkout"), (buttonWidget) -> {
            if(orderListWidget.buyContainer()){
                if (lastBalanceChange < 0) {
                    this.animationTick = 500;
                }
            } else {
                //Handle Fail
            }
        }));
        this.addDrawableChild(checkoutButton);
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
        drawMouseoverTooltip(matrixStack, i, j);
    }


    protected void drawBackground(MatrixStack matrixStack, float f, int i, int j) {
        switch (screenType) {
            case SELL -> drawSellBackground(matrixStack,f,i,j);
            case BUY -> drawBuyBackground(matrixStack,f,i,j);
        }
    }


    protected void drawSellBackground(MatrixStack matrixStack, float f, int i, int j) {
        setTexture(SELL_BACKGROUND);
        this.drawTexture(matrixStack, this.x, this.y, 0, 0, 256, ROW_COUNT * SLOT_SIZE + 17);
        this.drawTexture(matrixStack, this.x, this.y + ROW_COUNT * SLOT_SIZE + 17, 0, 126, 256, 96);
    }


    protected void drawBuyBackground(MatrixStack matrixStack, float f, int i, int j) {
        setTexture(BUY_BACKGROUND);
        this.drawTexture(matrixStack, this.x, this.y, 0, 0, 256, ROW_COUNT * SLOT_SIZE + 17);
        this.drawTexture(matrixStack, this.x, this.y + ROW_COUNT * SLOT_SIZE + 17, 0, 126, 256, 96);
    }


    protected void drawForeground(MatrixStack matrixStack, int i, int j) {
        matrixStack.push();
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        matrixStack.translate(0, 0, 256.0F);//Why exactly 256?

        if (screenType == ScreenType.BUY) {
            setTexture(BUY_FRAME);
            this.drawTexture(matrixStack, 0, 0, 0, 0, 256, ROW_COUNT * SLOT_SIZE + 17);
            this.drawTexture(matrixStack, 0, ROW_COUNT * SLOT_SIZE + 17, 0, 126, 256, 96);
        }

        int containerValue = getContainerValue();
        String containerValueString = "Total: $" + containerValue;
        int containerValueStringWidth = textRenderer.getWidth(containerValueString);
        this.textRenderer.draw(matrixStack, containerValueString, 169 -containerValueStringWidth , (float)this.titleY, 4210752);

        String title = screenType.toString().toLowerCase();
        this.textRenderer.draw(matrixStack, title.substring(0, 1).toUpperCase() + title.substring(1), (float)this.titleX, (float)this.titleY, 4210752);

        this.textRenderer.draw(matrixStack, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);
        matrixStack.pop();
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



    private void setTexture(Identifier texture){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

    }

    class BalanceWidget extends TextFieldWidget {
        public BalanceWidget(int x, int y, int width, int height) {
            super(textRenderer, x, y, width, height, Text.empty());
        }

        @Override
        public void render(MatrixStack matrixStack, int mouseX, int mouseY, float f) {
                matrixStack.push();
                setTexture(BALANCE_WIDGET);
//                int widgetX = this.x + 171;
//                int widgetY = this.y + 17;
                if (animationTick > 0) {
                    animationTick-=2;
                    matrixStack.push();
                    matrixStack.translate(0, 0, -300);
                    drawTexture(matrixStack, x, y + Math.min(animationTick, 200) /10, 0, 0, width, height, 128, 128);

                    int gainAmount = lastBalanceChange;
                    boolean isGain = gainAmount > 0;
                    String gainString = (isGain ? "+" : "-") + "$" + Math.abs(gainAmount);
                    int gainTextWidth = textRenderer.getWidth(gainString);
                    int textColor = isGain ? 0x6fde49 : 0xcf4a3e;
                    textRenderer.drawWithShadow(matrixStack,gainString, x + 74 -gainTextWidth , y + Math.min(animationTick, 200) /10f + 25, textColor);
                    matrixStack.pop();
                }
                setTexture(BALANCE_WIDGET);
                drawTexture(matrixStack, x, y, 0, 0, width, height, 128, 128);

                textRenderer.drawWithShadow(matrixStack,"Balance:", x + 6, y + 6, 0xcdcdf7);
                String balanceString = "$" + getScreenHandler().fishingCard.getCredit();
                int balanceStringWidth = textRenderer.getWidth(balanceString);
                textRenderer.drawWithShadow(matrixStack, balanceString, x + 74 - balanceStringWidth, y + 25, 0xcdcdf7);
                matrixStack.pop();

        }
    }

    class Entry extends AlwaysSelectedEntryListWidget.Entry<Entry>{

        final boolean isOffer;
        final Item offerItem;
        final int basePrice;
        final int batchSize;
        final float discountIncrement;
        final float discountMinimum;

        int currentPrice;
        int count;
        int totalPrice;
        boolean isEnabled;

        public Entry(Item offerItem, int basePrice, int batchSize, float discountIncrement, float discountMinimum, boolean isOffer) {
            this.isOffer = isOffer;
            this.offerItem = offerItem;
            this.basePrice = basePrice;
            this.batchSize = batchSize;
            this.discountIncrement = discountIncrement;
            this.discountMinimum = discountMinimum;

            this.currentPrice = basePrice;
            this.count = 0;
            this.totalPrice = 0;
            this.isEnabled = true;
        }

        public Entry(Item offerItem, int basePrice, int batchSize, float discountIncrement, float discountMinimum) {
            this(offerItem, basePrice, batchSize, discountIncrement, discountMinimum, false);
        }

        public Entry ofOrderEntry(){
            Entry orderEntry = new Entry(this.offerItem, this.basePrice, this.batchSize, this.discountIncrement, this.discountMinimum);
            orderEntry.count = 1;
            orderEntry.totalPrice =  basePrice;
            return orderEntry;
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
            if (isOffer) {
                renderOfferBackground(matrices, x, y,itemWidth, itemHeight);
                renderOfferEntryText(matrices, x, y, itemWidth);
            } else {
                renderOrderBackground(matrices, x, y,itemWidth, itemHeight);
                renderOrderEntryText(matrices, x, y, itemWidth);
            }
            renderEntryIcon(x,y);
            matrices.pop();
        }

        private void renderEntryIcon(int x, int y){
            ItemStack renderedStack = offerItem.getDefaultStack();
            itemRenderer.renderGuiItemIcon(renderedStack, x+1,y+1);
            itemRenderer.renderGuiItemOverlay(textRenderer, renderedStack, x+1,y+1, String.valueOf(batchSize)) ;
        }

        private void renderOfferEntryText(MatrixStack matrices, int x, int y, int itemWidth){
            renderEntryText(matrices, x, y, itemWidth, "$"+ currentPrice);
        }

        private void renderEntryText(MatrixStack matrices, int x, int y, int itemWidth, String text){
            int textLength = textRenderer.getWidth(text);
            textRenderer.drawWithShadow(matrices, text, x + itemWidth - textLength - 2, y + 8, 0xFFFFFF);

        }

        private void renderOrderEntryText(MatrixStack matrices, int x, int y, int itemWidth){
            renderEntryText(matrices, x, y, itemWidth, "$"+ totalPrice);
            textRenderer.drawWithShadow(matrices, "x"+count, x + SLOT_SIZE + 4, y + 8, 0xFFFFFF);
        }

        private void renderOrderBackground(MatrixStack matrices, int x, int y, int itemWidth, int itemHeight){
            setTexture(BUY_FRAME);
            drawTexture(matrices, x, y, 106, 236, itemWidth, itemHeight);
        }

        private void renderOfferBackground(MatrixStack matrices, int x, int y, int itemWidth, int itemHeight){
            setTexture(BUY_FRAME);
            drawTexture(matrices, x, y, isEnabled ? 0 : itemWidth, 236, itemWidth, itemHeight);
        }

        public void decreaseCount(){
            if (count > 0) {
                this.count--;
                totalPrice = getPriceForCount(count);
            }
        }

        public void increaseCount(){
            if (count < 99) {
                count++;
                totalPrice = getPriceForCount(count);
            }
        }

        @Override
        public boolean mouseClicked(double d, double e, int i) {
            if(isOffer) {
                if(isEnabled) {
                    int countInBasket = orderListWidget.addToBasket(this).count;
                    currentPrice = getPriceForNth(countInBasket + 1);
                }
            } else {
                orderListWidget.removeFromBasket(this);
            }
            checkoutButton.active = orderListWidget.getBasketTotal() <= getScreenHandler().fishingCard.getCredit();
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



    class OfferListWidget extends AlwaysSelectedEntryListWidget<ShopScreen.Entry>{

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
                        1f,
                        true
                ));
            }
            this.addEntry(new ShopScreen.Entry(
                    Items.EMERALD,
                    8,
                    4,
                    .1f,
                    0.5f,
                    true
            ));
        }

    }

    class OrderListWidget extends AlwaysSelectedEntryListWidget<ShopScreen.Entry> {
        private int basketTotal = 0;

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
            int entryCount = this.getEntryCount();

            for (int o = 0; o < entryCount; ++o) {
                int p = this.getRowTop(o);
                int q = this.getRowBottom(o);
                if (q >= this.top && p <= this.bottom) {
                    this.renderEntry(matrixStack, i, j, f, o, k, p, l, this.itemHeight);
                }
            }
        }

        public boolean buyContainer(){
            int cost = getBasketTotal();
            int currentCredit =  handler.fishingCard.getCredit();
            if (cost <= currentCredit) {
                lastBalanceChange = cost * -1;
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


        public ShopScreen.Entry addToBasket(ShopScreen.Entry entry) {
            for (ShopScreen.Entry orderEntry : this.children()) {
                if (orderEntry.equals(entry)) {
                    orderEntry.increaseCount();
                    calculateBasketTotal();
                    return orderEntry;
                }
            }
            ShopScreen.Entry orderEntry = entry.ofOrderEntry();
            this.children().add(orderEntry);
            calculateBasketTotal();
            return orderEntry;
        }

        public void removeFromBasket(ShopScreen.Entry entry) {
            entry.decreaseCount();
            if (entry.count == 0) {
                orderListWidget.removeEntry(entry);
            }
            calculateBasketTotal();
        }

        public int getBasketTotal() {
            return basketTotal;
        }

        private void calculateBasketTotal(){
            int result = 0;
            for (ShopScreen.Entry orderEntry : this.children()) {
                result += orderEntry.totalPrice;
            }
            basketTotal = result;
        }

        public void clear() {
            clearEntries();
            calculateBasketTotal();
            offerListWidget.updatePrices();
        }
    }



    enum ScreenType {
        SELL,
        BUY
    }
}

package net.semperidem.fishingclub.client.screen.shop;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.client.screen.shop.widget.OfferListWidget;
import net.semperidem.fishingclub.client.screen.shop.widget.OrderListWidget;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;

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
    private ScreenType screenType;



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

//    public void initCatalog(){
//        this.catalog = new ArrayList<>();
//        for(int i = 0; i < 20; i++) {
//
//            catalog.add(new OfferEntryData(
//                    Items.DIAMOND,
//                    i,
//                    i,
//                    1f,
//                    1f
//            ));
//        }
//        catalog.add(new OfferEntryData(
//                Items.EMERALD,
//                8,
//                4,
//                .1f,
//                0.5f
//        ));
//    }

    public void initSell(){
        addSellButton();
    }
    public void initBuy(){
        this.offerListWidget = new OfferListWidget(this, 53, 107, x + 6, y + 17);
        this.orderListWidget = new OrderListWidget(this, 88, 107, x + 69, y + 17);
        this.addSelectableChild(offerListWidget);
        this.addSelectableChild(orderListWidget);
    }


    public int getY(){
        return this.y;
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
                    this.handler.loadScreenHandler(screenType);
                    this.init();
                    break;
                }
            }
        }, ScreenTexts.EMPTY));
    }


    public void render(MatrixStack matrixStack, int i, int j, float f) {
        switch (screenType) {
            case SELL -> renderSellScreen(matrixStack, i, j, f);
            case BUY -> renderBuyScreen(matrixStack, i, j, f);
        }
    }

    public void renderSellScreen(MatrixStack matrixStack, int i, int j, float f) {
        renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        drawBalanceWidget(matrixStack);
        drawMouseoverTooltip(matrixStack, i, j);
        String containerValueString = "$" + this.handler.getSellContainerValue();
        int containerValueStringWidth = textRenderer.getWidth(containerValueString);
        textRenderer.draw(matrixStack,containerValueString, this.x + 167 -containerValueStringWidth , this.y + 6, 4210752);
    }
    public void renderBuyScreen(MatrixStack matrixStack, int i, int j, float f) {
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
        drawBalanceWidget(matrixStack);
        matrixStack.push();
        matrixStack.translate((double)x, (double)y, 2.0);
        matrixStack.translate(0.0, 0.0, (double)(200.0F));
        drawForeground(matrixStack,i,j);

        matrixStack.pop();
    }


    protected void addCheckoutButton() {
        this.addDrawableChild(new ButtonWidget(this.x + 175, this.y + 103, 70, 20, Text.of("Checkout"), (buttonWidget) -> {
        }){
            @Override
            public void renderButton(MatrixStack matrixStack, int i, int j, float f){
//TODO
            }
        });
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
//        String containerValueString = "Total: $" + orderListWidget.getBasketTotal();
//        int containerValueStringWidth = textRenderer.getWidth(containerValueString);
        this.textRenderer.draw(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
      //  this.textRenderer.draw(matrixStack, containerValueString, 158 -containerValueStringWidth, (float)this.titleY, 4210752);
        this.textRenderer.draw(matrixStack, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);
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


    enum ScreenType {
        SELL,
        BUY
    }
}

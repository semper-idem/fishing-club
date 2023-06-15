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
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.client.screen.shop.widget.OfferEntryData;
import net.semperidem.fishingclub.client.screen.shop.widget.OfferListWidget;
import net.semperidem.fishingclub.client.screen.shop.widget.OrderListWidget;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.util.ArrayList;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;
import static net.semperidem.fishingclub.client.screen.shop.ShopSellScreenHandler.ROW_COUNT;

public class ShopBuyScreen extends HandledScreen<ShopBuyScreenHandler> implements ScreenHandlerProvider<ShopBuyScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(MOD_ID,"textures/gui/shop_buy.png");
    private static final Identifier TEXTURE_EMPTY = new Identifier(MOD_ID,"textures/gui/shop_buy_empty.png");
    private static final Identifier MONEY_WIDGET = new Identifier(MOD_ID,"textures/gui/money_widget.png");
    private static final Identifier SHOP_BUTTONS = new Identifier(MOD_ID,"textures/gui/shop_buttons_64x64.png");
    private int animationTick = 0;
    public OfferListWidget offerListWidget;
    public OrderListWidget orderListWidget;
    public ArrayList<OfferEntryData> offers = new ArrayList<>();


    public ShopBuyScreen(ShopBuyScreenHandler shopBuyScreenHandler, PlayerInventory playerInventory, Text text) {
        super(shopBuyScreenHandler, playerInventory, text);
        this.client = MinecraftClient.getInstance();
        this.passEvents = false;
        this.backgroundHeight = 114 + ROW_COUNT * SLOT_SIZE;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        // Create the offer list and populate it

        this.addSelectableChild(offerListWidget);
    }


    protected void init() {
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        this.offerListWidget = new OfferListWidget(client, 53, 107, x + 6, y + 17, 20);
        this.orderListWidget = new OrderListWidget(client, 88, 107, x + 69, y + 17, 20);
        for(int i = 0; i < 20; i++) {

            offers.add(new OfferEntryData(
                    Items.DIAMOND,
                    i,
                    i,
                    1f,
                    1f
            ));
        }
        offers.add(new OfferEntryData(
                Items.EMERALD,
                8,
                4,
                .1f,
                0.5f
        ));
        this.offerListWidget.addEntries(this, offers);
        this.addCheckoutButton();
        this.addOpenSellScreenButton();
    }


    protected void addOpenSellScreenButton() {
        MinecraftClient.getInstance().mouse.unlockCursor();
        this.addDrawableChild(new TexturedButtonWidget(this.x + 178, this.y + 128, 32, 32, 32, 0, 0, SHOP_BUTTONS, 64, 64,(buttonWidget) -> {
            MinecraftClient.getInstance().mouse.lockCursor();
            ClientPacketSender.sendOpenSellShopRequest();
        }, ScreenTexts.EMPTY));
    }


    public int getY(){
        return this.y;
    }

    protected void addCheckoutButton() {
        this.addDrawableChild(new ButtonWidget(this.x + 175, this.y + 103, 70, 20, Text.of("Checkout"), (buttonWidget) -> {
        }){
            @Override
            public void renderButton(MatrixStack matrixStack, int i, int j, float f){

            }
        });
    }

    public void render(MatrixStack matrixStack, int i, int j, float f) {
        renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        drawMouseoverTooltip(matrixStack, i, j);
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

    protected void drawBackground(MatrixStack matrixStack, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.drawTexture(matrixStack, this.x, this.y, 0, 0, 256, ROW_COUNT * SLOT_SIZE + 17);
        this.drawTexture(matrixStack, this.x, this.y + ROW_COUNT * SLOT_SIZE + 17, 0, 126, 256, 96);
        RenderSystem.enableTexture();
        RenderSystem.enableDepthTest();
        RenderSystem.enableBlend();
        this.offerListWidget.render(matrixStack, i, j, f);
        this.orderListWidget.render(matrixStack,i,j,f);
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


    protected void drawForeground(MatrixStack matrixStack, int i, int j) {
        String containerValueString = "Total: $" + orderListWidget.getBasketTotal();
        int containerValueStringWidth = textRenderer.getWidth(containerValueString);
        this.textRenderer.draw(matrixStack, this.title, (float)this.titleX, (float)this.titleY, 4210752);
        this.textRenderer.draw(matrixStack, containerValueString, 158 -containerValueStringWidth, (float)this.titleY, 4210752);
        this.textRenderer.draw(matrixStack, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 4210752);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }


    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX > x + 69) {
            return this.orderListWidget.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
        }
        return this.offerListWidget.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.offerListWidget.mouseReleased(mouseX, mouseY, button) || super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX > x + 69) {
            return this.orderListWidget.mouseScrolled(mouseX, mouseY, amount) || super.mouseScrolled(mouseX, mouseY, amount);
        }
        return this.offerListWidget.mouseScrolled(mouseX, mouseY, amount) || super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.offerListWidget.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        return this.offerListWidget.keyReleased(keyCode, scanCode, modifiers) || super.keyReleased(keyCode, scanCode, modifiers);
    }

}
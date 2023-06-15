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
import net.semperidem.fishingclub.fish.fisher.FisherInfos;
import net.semperidem.fishingclub.network.ClientPacketSender;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;
import static net.semperidem.fishingclub.client.screen.shop.ShopScreenUtil.SLOT_SIZE;
import static net.semperidem.fishingclub.client.screen.shop.ShopSellScreenHandler.ROW_COUNT;

public class ShopSellScreen extends HandledScreen<ShopSellScreenHandler> implements ScreenHandlerProvider<ShopSellScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(MOD_ID,"textures/gui/shop_sell.png");
    private static final Identifier MONEY_WIDGET = new Identifier(MOD_ID,"textures/gui/money_widget.png");
    private static final Identifier SHOP_BUTTONS = new Identifier(MOD_ID,"textures/gui/shop_buttons_64x64.png");
    private int animationTick = 0;


    public ShopSellScreen(ShopSellScreenHandler shopSellScreenHandler, PlayerInventory playerInventory, Text text) {
        super(shopSellScreenHandler, playerInventory, text);
        this.passEvents = false;
        this.backgroundHeight = 114 + ROW_COUNT * SLOT_SIZE;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }


    protected void init() {
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        MinecraftClient.getInstance().mouse.onResolutionChanged();
        this.addSellButton();
        this.addOpenBuyScreenButton();
    }

    protected void addSellButton() {
        this.addDrawableChild(new ButtonWidget(this.x + 175, this.y + 103, 70, 20, Text.of("Sell"), (buttonWidget) -> {
            if (this.handler.sellContainer()) {
                this.animationTick = 300;
            }
        }));
    }
    protected void addOpenBuyScreenButton() {
        this.addDrawableChild(new TexturedButtonWidget(this.x + 178, this.y + 128, 32, 32, 0, 0, 0, SHOP_BUTTONS, 64, 64,(buttonWidget) -> {
            ClientPacketSender.sendOpenBuyShopRequest();
        }, ScreenTexts.EMPTY));
    }

    public void render(MatrixStack matrixStack, int i, int j, float f) {
        renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        drawBalanceWidget(matrixStack);
        drawMouseoverTooltip(matrixStack, i, j);
        String containerValueString = "$" + this.handler.getSellContainerValue();
        int containerValueStringWidth = textRenderer.getWidth(containerValueString);
        textRenderer.draw(matrixStack,containerValueString, this.x + 167 -containerValueStringWidth , this.y + 6, 4210752);
    }

    private void drawBalanceWidget(MatrixStack matrixStack){

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
            String gainString = "+$" + this.handler.lastSellValue;
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
    }

    protected void drawBackground(MatrixStack matrixStack, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.drawTexture(matrixStack, this.x, this.y, 0, 0, 256, ROW_COUNT * SLOT_SIZE + 17);
        this.drawTexture(matrixStack, this.x, this.y + ROW_COUNT * SLOT_SIZE + 17, 0, 126, 256, 96);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
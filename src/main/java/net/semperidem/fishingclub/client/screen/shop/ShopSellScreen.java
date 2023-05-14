package net.semperidem.fishingclub.client.screen.shop;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.fish.fisher.FisherInfo;
import net.semperidem.fishingclub.fish.fisher.FisherInfos;
import org.jetbrains.annotations.Nullable;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class ShopSellScreen extends HandledScreen<ShopSellScreenHandler> implements ScreenHandlerProvider<ShopSellScreenHandler> {
    private static final Identifier TEXTURE = new Identifier(MOD_ID,"textures/gui/shop_sell.png");
    private static final Identifier MONEY_WIDGET = new Identifier(MOD_ID,"textures/gui/shop_sell_money_widget.png");
    private final int rows = 6;
    FisherInfo clientInfo;
    private int animationTick = 0;


    public ShopSellScreen(ShopSellScreenHandler shopSellScreenHandler, PlayerInventory playerInventory, Text text) {
        super(shopSellScreenHandler, playerInventory, text);
        this.passEvents = false;
        this.backgroundHeight = 114 + this.rows * 18;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        this.clientInfo = FisherInfos.getClientInfo();
    }


    protected void init() {
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        this.addSellButton();
    }

    protected void addSellButton() {
        this.addDrawableChild(new ButtonWidget(this.x + 175, this.y + 103, 70, 20, Text.of("Sell"), (buttonWidget) -> {
            this.animationTick = 300;
        }));
    }

    public void render(MatrixStack matrixStack, int i, int j, float f) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        this.drawMouseoverTooltip(matrixStack, i, j);
        this.drawBalanceWidget(matrixStack);
    }

    private void drawBalanceWidget(MatrixStack matrixStack){

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, MONEY_WIDGET);
        int widgetX = this.x + 174;
        int widgetY = this.y + 17;
        if (animationTick > 0) {
            animationTick-=2;
            this.drawTexture(matrixStack, widgetX, widgetY + Math.min(animationTick, 200) /10, 0, 0, 80, 40, 80, 80);
            String gainString = "+$123";
            int gainTextWidth = textRenderer.getWidth(gainString);
            this.textRenderer.drawWithShadow(matrixStack,"+$123", widgetX + 69 -gainTextWidth , widgetY + Math.min(animationTick, 200) /10f + 24, 0xcdcdf7);
        }
        RenderSystem.setShaderTexture(0, MONEY_WIDGET);
        this.drawTexture(matrixStack, widgetX, widgetY, 0, 0, 80, 40, 80, 80);

        this.textRenderer.drawWithShadow(matrixStack,"Balance:", widgetX + 6, widgetY + 6, 0xcdcdf7);
        String balanceString = "$" + clientInfo.getFisherCredit();
        int balanceStringWidth = textRenderer.getWidth(balanceString);
        this.textRenderer.drawWithShadow(matrixStack, "$" + clientInfo.getFisherCredit(), widgetX + 69 - balanceStringWidth, widgetY + 24, 0xcdcdf7);
    }

    protected void drawBackground(MatrixStack matrixStack, float f, int i, int j) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        this.drawTexture(matrixStack, this.x, this.y, 0, 0, 256, this.rows * 18 + 17);
        this.drawTexture(matrixStack, this.x, this.y + this.rows * 18 + 17, 0, 126, 256, 96);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }


    public static void openScreen(PlayerEntity player){
        if(player.world != null && !player.world.isClient) {
            player.openHandledScreen(new NamedScreenHandlerFactory() {

                @Override
                public Text getDisplayName() {
                    return Text.translatable("Sell Inventory");
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new ShopSellScreenHandler(syncId, inv);
                }
            });
        }
    }
}
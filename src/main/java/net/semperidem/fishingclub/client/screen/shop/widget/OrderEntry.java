package net.semperidem.fishingclub.client.screen.shop.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.client.screen.shop.ShopScreen;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class OrderEntry extends AlwaysSelectedEntryListWidget.Entry<OrderEntry> {
    private static final Identifier TEXTURE_EMPTY = new Identifier(MOD_ID,"textures/gui/shop_buy_empty.png");
    private final OrderListWidget orderListWidget;
    private final OrderEntryData orderEntryData;
    ShopScreen screen;

    public OrderEntry(OrderListWidget orderListWidget, ShopScreen shopScreen, OrderEntryData orderEntryData) {
        this.orderListWidget = orderListWidget;
        this.orderEntryData = orderEntryData;
        this.screen = shopScreen;
    }

    @Override
    public void render(MatrixStack matrices, int index, int y, int x, int itemWidth, int itemHeight, int mouseX, int mouseY, boolean isSelected, float tickDelta) {
        matrices.push();
        if (y < screen.getY()) {
            return;
        }
        renderBackground(matrices, x, y,itemWidth, itemHeight, isSelected);
        ItemStack renderedStack = orderEntryData.getItem().getDefaultStack();
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(renderedStack, x + 1,y+1);
        MinecraftClient.getInstance().getItemRenderer().renderGuiItemOverlay(MinecraftClient.getInstance().textRenderer, renderedStack,x,y+1, String.valueOf(orderEntryData.offerEntryData.batchSize));
        String priceString = "$"+orderEntryData.total;
        int priceStringLength = orderListWidget.textRenderer.getWidth(priceString);
        orderListWidget.textRenderer.drawWithShadow(matrices, priceString, x + itemWidth - priceStringLength - 2, y + 8, 0xFFFFFF);
        orderListWidget.textRenderer.drawWithShadow(matrices, "x"+orderEntryData.count, x + 21, y + 8, 0xFFFFFF);
        matrices.pop();
    }


    private void renderBackground(MatrixStack matrices, int x, int y, int itemWidth, int itemHeight, boolean isSelected){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE_EMPTY);
        screen.drawTexture(matrices, x, y, 106, 236, itemWidth, itemHeight);
    }

    @Override
    public Text getNarration() {
        return Text.of("micheal jackson");
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        screen.orderListWidget.decreaseCount(this);
        return true;
    }

    public boolean decreaseCount(){
        return orderEntryData.remove().count == 0;
    }
    public OrderEntryData increaseCount(){
        if (orderEntryData.count < 99) {
            orderEntryData.add();
        }
        return orderEntryData;
    }

    public String getName(){
        return this.orderEntryData.getName();
    }
    public boolean matches(OrderEntry orderEntry){
        return orderEntry.orderEntryData.matches(this.orderEntryData);
    }
    public boolean matches(OfferEntry offerEntry){
        return this.orderEntryData.matches(offerEntry.offer);
    }

    public int getCount(){
        return orderEntryData.getCount();
    }

    public int getTotal(){
        return orderEntryData.total;
    }

}
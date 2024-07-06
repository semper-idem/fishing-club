package net.semperidem.fishingclub.client.screen.configuration;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.semperidem.fishingclub.registry.FCItems;
import net.semperidem.fishingclub.screen.configuration.ConfigurationScreenHandler;

public class ConfigurationScreen extends HandledScreen<ConfigurationScreenHandler> implements ScreenHandlerProvider<ConfigurationScreenHandler> {
    public ConfigurationScreen(ConfigurationScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }


    @Override
    protected void init() {
        this.backgroundWidth = 182;
        this.backgroundHeight = 240;
        this.x = (int) ((this.width - this.backgroundWidth) * 0.5f);
        this.y = this.height - backgroundHeight;
    }

    @Override
    protected void drawSlot(DrawContext context, Slot slot) {
        super.drawSlot(context, slot);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int middleX = context.getScaledWindowWidth() / 2;
        Identifier hotbar = Identifier.ofVanilla("hud/hotbar");
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        matrixStack.translate(0,0, 200);
        super.render(context, mouseX, mouseY, delta);
        RenderSystem.enableBlend();
        context.drawGuiTexture(hotbar, middleX - 91, context.getScaledWindowHeight() - 82, 182, 22);
        context.drawGuiTexture(hotbar, middleX - 91, context.getScaledWindowHeight() - 62, 182, 22);
        context.drawGuiTexture(hotbar, middleX - 91, context.getScaledWindowHeight() - 42, 182, 22);
        context.drawGuiTexture(hotbar, middleX - 91, context.getScaledWindowHeight() - 22, 182, 22);
        matrixStack.translate(this.x, this.y, 200);
        for(int i = 0; i < 5; i++) {
          Slot slot = this.handler.slots.get(i + 36);
            context.fill(slot.x, slot.y, slot.x+16, slot.y+16, 0x88888888);
        }
        matrixStack.pop();
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

    private void renderFishingRod(DrawContext context, int mouseX, int mouseY, float delta) {
        MatrixStack matrixStack = context.getMatrices();
        matrixStack.push();
        float scale = 12;
        matrixStack.translate((context.getScaledWindowWidth()) * 0.5, -90 + this.y,0);
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(45));
        matrixStack.scale(scale,scale,1);
        float shadow = 0.25f;
        context.setShaderColor(shadow,shadow,shadow, 1f);
        matrixStack.push();
        matrixStack.translate(0, shadow, 0);
        context.drawItem(FCItems.MEMBER_FISHING_ROD.getDefaultStack(), 0, 0);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(shadow, shadow, 0);
        context.drawItem(FCItems.MEMBER_FISHING_ROD.getDefaultStack(), 0, 0);
        matrixStack.pop();
        matrixStack.push();
        matrixStack.translate(shadow, 0, 0);
        context.drawItem(FCItems.MEMBER_FISHING_ROD.getDefaultStack(), 0, 0);
        matrixStack.pop();
        context.setShaderColor(1,1,1, 1f);
        context.drawItem(FCItems.MEMBER_FISHING_ROD.getDefaultStack(), 0, 0);
        matrixStack.pop();
    }
    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderBackground(context, mouseX, mouseY, delta);
        renderFishingRod(context, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }
}

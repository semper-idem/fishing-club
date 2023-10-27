package net.semperidem.fishingclub.client.screen.workbench;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.awt.*;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FisherWorkbenchScreen extends HandledScreen<FisherWorkbenchScreenHandler> implements ScreenHandlerProvider<FisherWorkbenchScreenHandler> {
    private static final Identifier BACKGROUND_DEFAULT = new Identifier(MOD_ID,"textures/gui/fisher_workbench_gui.png");
    private static final Identifier BACKGROUND_REPAIR = new Identifier(MOD_ID,"textures/gui/fisher_workbench_gui_repair.png");
    private static final Identifier WRENCH_ICON = new Identifier(MOD_ID,"textures/gui/wrench.png");
    private static Identifier BACKGROUND = BACKGROUND_DEFAULT;

    ButtonWidget repairButton;
    ButtonWidget wrenchButton;


    boolean wrenchVisible = false;
    boolean repairMode = false;
    public FisherWorkbenchScreen(FisherWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, Text.empty());
        this.backgroundHeight = 222;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
        handler.setScreenCallback(this);
    }


    public void changeRepairMode(boolean repairMode){
        this.repairMode = repairMode;
        BACKGROUND = repairMode ? BACKGROUND_REPAIR : BACKGROUND_DEFAULT;
        this.repairButton.visible = repairMode;
    }

    public void setWrenchVisible(boolean wrenchVisible){
        this.wrenchVisible = wrenchVisible;
        this.wrenchButton.visible = wrenchVisible;
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        repairButton = new ButtonWidget(x + 12,y + 16 + 26 * 3,40,20, Text.of("Repair"), repairClick -> {
            handler.repairRod();
            ClientPacketSender.sendFishingRodRepairRequest();
        });
        repairButton.visible = repairMode;
        repairButton.active = false;
        addDrawableChild(repairButton);

        wrenchButton = new ButtonWidget(x + 34,y + 16,8,8, Text.of(""), wrenchClick -> {
            handler.setRepairMode(!repairMode);
        }) {

            @Override
            public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, WRENCH_ICON);
                drawTexture(matrices, x, y, 0, 0, 8, 8, 8, 8);
            }
        };
        wrenchButton.visible = wrenchVisible;
        addDrawableChild(wrenchButton);
    }


    public void render(MatrixStack matrixStack, int i, int j, float f) {
        renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        renderSlotLabels(matrixStack);
        drawMouseoverTooltip(matrixStack, i, j);
    }

    private void renderWrenchButton(){

    }

    private void renderSlotLabels(MatrixStack matrixStack){
        if (repairMode) {
            renderRepairModeLabels(matrixStack);
        } else {
            renderDefaultLabels(matrixStack);
        }
    }

    private void renderRepairModeLabels(MatrixStack matrixStack){
        renderTextRightSide(matrixStack, "Material:", this.x + 143, this.y + 20 + 26 * 3);
    }

    private void renderDefaultLabels(MatrixStack matrixStack){
        renderTextRightSide(matrixStack, "Core:", this.x + 143, this.y + 20);
        renderTextRightSide(matrixStack, "Bobber:", this.x + 143, this.y + 20 + 26);
        renderTextRightSide(matrixStack, "Line:", this.x + 143, this.y + 20 + 26 * 2);
        renderTextRightSide(matrixStack, "Hook:", this.x + 143, this.y + 20 + 26 * 3);
        renderTextRightSide(matrixStack, "Bait:", this.x + 52, this.y + 20 + 26 * 3);

    }

    private void renderTextRightSide(MatrixStack matrixStack, String text, int x, int y){
        int textLength = textRenderer.getWidth(text);
        this.textRenderer.drawWithShadow(matrixStack, text,x - textLength,y, Color.WHITE.getRGB());
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        this.drawTexture(matrices, this.x, this.y, 0, 0, 256, 256);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 0x404040);
    }
}

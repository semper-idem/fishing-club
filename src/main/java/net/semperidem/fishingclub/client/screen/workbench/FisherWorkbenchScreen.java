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

import java.awt.*;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;

public class FisherWorkbenchScreen extends HandledScreen<FisherWorkbenchScreenHandler> implements ScreenHandlerProvider<FisherWorkbenchScreenHandler> {
    private static final Identifier WRENCH_ICON = new Identifier(MOD_ID,"textures/gui/wrench.png");
    private static final Identifier BACKGROUND = new Identifier(MOD_ID,"textures/gui/fisher_workbench_gui.png");

    private static final int PART_LABEL_X = FisherWorkbenchScreenHandler.PART_SLOT_X - 3;
    private static final int PART_BAIT_LABEL_X = FisherWorkbenchScreenHandler.PART_SLOT_X - 2;
    private static final int PART_LABEL_Y = FisherWorkbenchScreenHandler.ROD_SLOT_Y + 3;
    private static final int PART_LABEL_OFFSET = FisherWorkbenchScreenHandler.PART_SLOT_OFFSET;

    private WrenchButtonWidget wrenchButton;


    public FisherWorkbenchScreen(FisherWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 222;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.width - this.backgroundWidth) / 2;
        this.y = (this.height - this.backgroundHeight) / 2;
        wrenchButton = new WrenchButtonWidget(x + 34,y + 16,8,8, Text.of(""));
    }


    public void render(MatrixStack matrixStack, int i, int j, float f) {
        renderBackground(matrixStack);
        super.render(matrixStack, i, j, f); //Slots
        renderLabels(matrixStack);
        drawMouseoverTooltip(matrixStack, i, j);
    }


    private void renderLabels(MatrixStack matrixStack){
        renderLabelFromRight(matrixStack, "Core:", x + PART_LABEL_X, y + PART_LABEL_Y);
        renderLabelFromRight(matrixStack, "Bobber:", x + PART_LABEL_X, y + PART_LABEL_Y + PART_LABEL_OFFSET);
        renderLabelFromRight(matrixStack, "Line:", x + PART_LABEL_X, y + PART_LABEL_Y + PART_LABEL_OFFSET * 2);
        renderLabelFromRight(matrixStack, "Hook:", x + PART_LABEL_X, y + PART_LABEL_Y + PART_LABEL_OFFSET * 3);
        renderLabelFromRight(matrixStack, "Bait:", x + PART_BAIT_LABEL_X, y + PART_LABEL_Y + PART_LABEL_OFFSET * 3);
    }

    private void renderLabelFromRight(MatrixStack matrixStack, String text, int x, int y){
        this.textRenderer.drawWithShadow(matrixStack, text,x - textRenderer.getWidth(text),y, Color.WHITE.getRGB());
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND);
        this.drawTexture(matrices, this.x, this.y, 0, 0, 256, 256);
    }

    //Cancel title render?
    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        this.textRenderer.draw(matrices, this.playerInventoryTitle, (float)this.playerInventoryTitleX, (float)this.playerInventoryTitleY, 0x404040);
    }

    class WrenchButtonWidget extends ButtonWidget{
        public WrenchButtonWidget(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message,  buttonClick -> {
                //C2S_OPEN_FISHER_WORKBENCH_REPAIR_SCREEN
            });
            this.visible = false;
            addDrawableChild(this);
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0, WRENCH_ICON);
            drawTexture(matrices, x, y, 0, 0, 8, 8, 8, 8);
        }
    }
}

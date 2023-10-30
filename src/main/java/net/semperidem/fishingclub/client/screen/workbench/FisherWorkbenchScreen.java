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
import java.util.ArrayList;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;
import static net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreenHandler.*;

public class FisherWorkbenchScreen extends HandledScreen<FisherWorkbenchScreenHandler> implements ScreenHandlerProvider<FisherWorkbenchScreenHandler> {
    private static final int TEXT_TO_SLOT_OFFSET = (SLOT_SIZE - 9) / 2; // 9 is text height
    private static final Identifier BACKGROUND_DEFAULT = new Identifier(MOD_ID,"textures/gui/fisher_workbench_gui.png");
    private static final Identifier BACKGROUND_REPAIR = new Identifier(MOD_ID,"textures/gui/fisher_workbench_gui_repair.png");
    private static final Identifier WRENCH_ICON = new Identifier(MOD_ID,"textures/gui/wrench.png");
    private static final int TEXT_COLOR = Color.WHITE.getRGB();


    private ButtonWidget repairButton;

    private Identifier background = BACKGROUND_DEFAULT;
    private boolean repairMode = false;

    private ArrayList<SlotLabel> repairLabels = new ArrayList<>();
    private ArrayList<SlotLabel> standardLabels = new ArrayList<>();
    private ArrayList<SlotLabel> labels = standardLabels;

    //INIT
    public FisherWorkbenchScreen(FisherWorkbenchScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.backgroundHeight = 222;
        this.playerInventoryTitleY = this.backgroundHeight - 94;
    }

    @Override
    protected void init() {
        super.init();
        x = (width - backgroundWidth) / 2;
        y = (height - backgroundHeight) / 2;

        initLabels();
        initRepairButton();
        initToggleModeButton();
    }


    private void initLabels(){
        repairLabels = initRepairLabels();
        standardLabels = initStandardLabels();
        labels = standardLabels;
    }

    private ArrayList<SlotLabel> initStandardLabels(){
        ArrayList<SlotLabel> labels = new ArrayList<>();
        labels.add(new SlotLabel("Core: ", x + SLOTS_X, y + SLOTS_Y));
        labels.add(new SlotLabel("Bobber: ", x + SLOTS_X, y + SLOTS_Y + SLOT_OFFSET + TEXT_TO_SLOT_OFFSET));
        labels.add(new SlotLabel("Line: ", x + SLOTS_X, y + SLOTS_Y + SLOT_OFFSET * 2 + TEXT_TO_SLOT_OFFSET));
        labels.add(new SlotLabel("Hook: ", x + SLOTS_X, y + SLOTS_Y + SLOT_OFFSET * 3 + TEXT_TO_SLOT_OFFSET));
        labels.add(new SlotLabel("Bait: ", x + SLOTS_X_BAIT, y + SLOTS_Y + SLOT_OFFSET * 3 + TEXT_TO_SLOT_OFFSET));
        return labels;
    }
    private ArrayList<SlotLabel> initRepairLabels(){
        ArrayList<SlotLabel> slotLabels = new ArrayList<>();
        slotLabels.add(new SlotLabel("Material: ", x + SLOTS_X, y + SLOTS_Y + SLOT_OFFSET * 3 + TEXT_TO_SLOT_OFFSET));
        return slotLabels;
    }

    private void initRepairButton(){
        repairButton = new ButtonWidget(x + ROD_X,y + SLOTS_Y + SLOT_OFFSET * 3 - 1,40,20, Text.of("Repair"), repairClick -> ClientPacketSender.sendFishingRodRepairRequest());
        repairButton.visible = false;
        repairButton.active = false;
        addDrawableChild(repairButton);
    }

    private void initToggleModeButton(){
        addDrawableChild(new ButtonWidget(x + ROD_X + SLOT_SIZE, y + ROD_Y, 8, 8, Text.of(""), wrenchClick -> toggleMode()){
            @Override
            public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                setTexture(WRENCH_ICON);
                drawTexture(matrices, x, y, 0, 0, 8, 8, 8, 8);
            }
        });
    }

    private void toggleMode(){
        repairMode = !repairMode;
        if (repairMode) {
            setRepairMode();
        } else {
            setStandardMode();
        }
    }

    private void setStandardMode() {
        handler.activateStandardSlots();
        background = BACKGROUND_DEFAULT;
        repairButton.visible = false;
        repairButton.active = false;
        labels = standardLabels;
    }

    private void setRepairMode(){
        handler.activateRepairSlots();
        background = BACKGROUND_REPAIR;
        repairButton.visible = true;
        repairButton.active = true;
        labels = repairLabels;
    }


    //RENDERING
    public void render(MatrixStack matrixStack, int i, int j, float f) {
        renderBackground(matrixStack);
        super.render(matrixStack, i, j, f);
        renderLabels(matrixStack);
        drawMouseoverTooltip(matrixStack, i, j);
    }

    private void renderLabels(MatrixStack matrixStack){
        for(SlotLabel label : labels) {
            renderTextRightSide(matrixStack, label.text, label.x, label.y);
        }
    }

    private void renderTextRightSide(MatrixStack matrixStack, String text, int x, int y){
        this.textRenderer.drawWithShadow(matrixStack, text,x - textRenderer.getWidth(text),y, TEXT_COLOR);
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        setTexture(background);
        this.drawTexture(matrices, x, y, 0, 0, 256, 256);
    }

    private void setTexture(Identifier texture){
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);

    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        textRenderer.draw(matrices, playerInventoryTitle, (float)playerInventoryTitleX, (float)playerInventoryTitleY, 0x404040);
    }



    //PER TICK UPDATES
    @Override
    protected void handledScreenTick() {
        if (repairMode) {
            repairButton.active = handler.isRepairPossible();
        }
        super.handledScreenTick();
    }



    private static class SlotLabel{
        String text;
        int x;
        int y;

        SlotLabel(String text, int x, int y){
            this.text = text;
            this.x = x;
            this.y = y;
        }
    }
}

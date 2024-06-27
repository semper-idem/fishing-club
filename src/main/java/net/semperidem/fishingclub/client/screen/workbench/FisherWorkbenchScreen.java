package net.semperidem.fishingclub.client.screen.workbench;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.awt.*;
import java.util.ArrayList;
import java.util.function.Supplier;

import static net.semperidem.fishingclub.FishingClub.MOD_ID;
import static net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreenHandler.*;

public class FisherWorkbenchScreen extends HandledScreen<FisherWorkbenchScreenHandler> implements ScreenHandlerProvider<FisherWorkbenchScreenHandler> {

    private static final int TEXT_TO_SLOT_OFFSET = (SLOT_SIZE - 9) / 2; // 9 is text height
    private static final Identifier BACKGROUND_DEFAULT = FishingClub.getIdentifier("textures/gui/fisher_workbench_gui.png");
    private static final Identifier BACKGROUND_REPAIR = FishingClub.getIdentifier("textures/gui/fisher_workbench_gui_repair.png");
    private static final Identifier WRENCH_ICON = FishingClub.getIdentifier("textures/gui/wrench.png");
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
        repairButton =  ButtonWidget.builder(Text.of("Repair"), repairClick -> ClientPacketSender.sendFishingRodRepairRequest()).dimensions(
                x + ROD_X,
                y + SLOTS_Y + SLOT_OFFSET * 3 - 1,
                40,20
        ).build();
        repairButton.active = false;
        repairButton.visible = false;
        addDrawableChild(repairButton);
    }

    private void initToggleModeButton(){
        addDrawableChild(new ButtonWidget(x + ROD_X + SLOT_SIZE, y + ROD_Y, 8, 8, Text.of(""), wrenchClick -> toggleMode(), Supplier::get){
            @Override
            public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                context.drawTexture(WRENCH_ICON, x, y, 0, 0, 8, 8, 8, 8);
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
        repairButton.active = false;
        repairButton.visible = false;
        labels = standardLabels;
    }

    private void setRepairMode(){
        handler.activateRepairSlots();
        background = BACKGROUND_REPAIR;
        repairButton.active = true;
        repairButton.visible = true;
        labels = repairLabels;
    }
    
    public void render(DrawContext context, int i, int j, float f) {
        renderBackground(context, i, j, f);
        super.render(context, i, j, f);
        renderLabels(context);
        drawMouseoverTooltip(context, i, j);
    }

    private void renderLabels(DrawContext context){
        for(SlotLabel label : labels) {
            renderTextRightSide(context, label.text, label.x, label.y);
        }
    }

    private void renderTextRightSide(DrawContext context, String text, int x, int y){
        context.drawText(textRenderer, text,x - textRenderer.getWidth(text),y, TEXT_COLOR, true);
    }


    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        context.drawTexture(background, x, y, 0, 0, 256, 256);
    }


    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
        context.drawText(textRenderer, playerInventoryTitle, playerInventoryTitleX, playerInventoryTitleY, 0x404040, false);
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

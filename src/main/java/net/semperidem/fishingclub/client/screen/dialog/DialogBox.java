package net.semperidem.fishingclub.client.screen.dialog;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DialogBox extends ScrollableWidget{
    ArrayList<String> renderedMessages = new ArrayList<>();
    TextRenderer textRenderer;
    String renderedLine = "";
    String lineToRender = "";
    DialogNode response;
    ArrayList<String> responseLines = new ArrayList<>();
    ArrayList<String> possibleQuestions = new ArrayList<>();
    int nextLineLength = 0;
    int tick = 0;
    int lineHeight = 11;
    public int textSpeed = 1;

    public DialogBox(int x, int y, int width, int height, TextRenderer textRenderer) {
        super(x,y, width, height, Text.empty());
        this.textRenderer = textRenderer;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (!this.visible) {
            return;
        }
        enableScissor(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1);
        matrices.push();
        matrices.translate(0.0, -this.getScrollY(), 0.0);
        this.renderContents(matrices, mouseX, mouseY, delta);
        matrices.pop();
        disableScissor();
        this.renderOverlay(matrices);
    }

    @Override
    protected int getContentsHeight() {
        return renderedMessages.size() * lineHeight;
    }

    @Override
    protected boolean overflows() {
        return false;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 0;
    }

    private void tick() {
        tick+=textSpeed;
        if (finishedRenderingLine() && !responseLines.isEmpty()) {
            consumeMessage();
        }
        renderedLine = DialogHelper.getTextForTick(lineToRender, tick/5);
        textSpeed = 1;
    }

    public void addMessage(DialogNode nextNode) {
        response = nextNode;
        ArrayList<String> temp = new ArrayList<>();
        if (!response.title.isEmpty()) {
            temp.add(" - " + response.title);
        }
        temp.addAll(List.of(response.content.split("\n")));
        for(String responseLine : temp) {
            responseLines.add(DialogHelper.replaceTemplates(responseLine));
        }
        possibleQuestions.clear();
        for(DialogNode question : response.questions) {
            possibleQuestions.add(DialogHelper.replaceTemplates(question.title));
        }
        lineToRender = responseLines.get(0);
        tick = 0;
    }

    private void consumeMessage() {
        if (renderedLine.isEmpty()) {
            return;
        }
        renderedMessages.add(renderedLine);
        responseLines.remove(renderedLine);
        lineToRender = "";
        if (!responseLines.isEmpty()) {
            lineToRender = responseLines.get(0);
            tick = 0;
        }
    }

    private boolean finishedRenderingLine() {
        return renderedLine.length() == 0 || (lineToRender.length() == renderedLine.length() && lineToRender != "");
    }

    public void finishLine(){
        tick = 1000;
        renderedLine = lineToRender;
    }

    @Override
    protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        tick();
        for(int i = 0; i < renderedMessages.size(); i++) {
            drawTextWithShadow(matrices, textRenderer, Text.of(renderedMessages.get(i)), x + getPadding(), y + i * lineHeight + getPadding(), Color.WHITE.getRGB());
        }
        if (responseLines.isEmpty()) {
            for(int i = 0; i < response.questions.size(); i++) {
                drawTextWithShadow(matrices, textRenderer, Text.of("["+(i+1)+"] " + possibleQuestions.get(i)), x + getPadding(), y + renderedMessages.size() * lineHeight + i * lineHeight + getPadding() + 2, Color.LIGHT_GRAY.getRGB());
            }
        } else {
            drawTextWithShadow(matrices, textRenderer, Text.of(renderedLine), x + getPadding(), y + renderedMessages.size() * lineHeight + getPadding(), Color.WHITE.getRGB());
        }
    }


    @Override
    protected int getPadding() {
        return 4;
    }

    protected int getMaxScrollY() {
        return Math.max(0, this.getContentsHeightWithPadding() - (this.height - 4));
    }

    private int getContentsHeightWithPadding() {
        return this.getContentsHeight() + 4;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {}
}

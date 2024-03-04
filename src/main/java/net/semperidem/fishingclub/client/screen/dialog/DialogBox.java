package net.semperidem.fishingclub.client.screen.dialog;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.screen.dialog.DialogNode;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;
import net.semperidem.fishingclub.screen.dialog.Responses;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class DialogBox extends ScrollableWidget{
    ArrayList<String> responseLines = new ArrayList<>();
    TextRenderer textRenderer;
    String trailingLine = "";
    String lineInQueue = "";
    DialogNode response;
    ArrayList<String> responseLinesQueue = new ArrayList<>();
    ArrayList<String> possibleQuestions = new ArrayList<>();
    int lineInQueueFinishTick = 0;
    int tick = 0;
    int lineHeight = 11;
    public int textSpeed = 1;


    public DialogBox(int x, int y, int width, int height, TextRenderer textRenderer) {
        super(x,y, width, height, Text.empty());
        this.textRenderer = textRenderer;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        if (!this.visible) {
            return;
        }
        enableScissor(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1);
        matrixStack.push();
        matrixStack.translate(0.0, -this.getScrollY(), 0.0);
        this.renderContents(matrixStack, mouseX, mouseY, delta);
        matrixStack.pop();
        disableScissor();
        if (overflows()) {
            renderScrollbar(matrixStack);
        }
    }

    private void renderScrollbar(MatrixStack matrixStack) {
        int i = MathHelper.clamp((int)((int)((float)(this.height * this.height) / (float)this.getContentsHeightWithPadding())), (int)32, (int)this.height);
        int l = Math.max(this.y, (int)this.getScrollY() * (this.height - i) / this.getMaxScrollY() + this.y);
        int m = l + i;
        int scrollBarX = x + width;
        int scrollBarWidth = 4;
        int scrollBarShadowWidth = 1;
        fill(
                matrixStack,
                scrollBarX - scrollBarWidth,
                l,
                scrollBarX,
                m,
                Color.BLACK.getRGB()
        );
        fill(
                matrixStack,
                scrollBarX - scrollBarWidth  + scrollBarShadowWidth,
                l + scrollBarShadowWidth,
                scrollBarX - scrollBarShadowWidth,
                m - scrollBarShadowWidth,
                Color.DARK_GRAY.darker().darker().darker().getRGB()
        );
    }

    @Override
    protected int getContentsHeight() {
        if (responseLinesQueue.isEmpty()) {
            return responseLines.size() * lineHeight + possibleQuestions.size() * lineHeight + 1;
        }
        return responseLines.size() * (lineHeight + 1);
    }

    @Override
    protected boolean overflows() {
        return getContentsHeight() > height;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 5;
    }

    void tick() {
        if (finishedRenderingLine() && !responseLinesQueue.isEmpty()) {
            consumeMessage();
        }
        trailingLine = DialogUtil.getTextForTick(lineInQueue, tick);
        textSpeed = 1;
        tick+=textSpeed;
    }

    public void addMessage(DialogNode nextNode) {
        response = nextNode;
        ArrayList<String> temp = new ArrayList<>();
        if (response.title != null && !response.title.isEmpty()) {
            temp.add(" - " + response.title);
        }
        temp.addAll(List.of(response.content.split("\n")));
        for(String responseLine : temp) {
            responseLinesQueue.add(responseLine.replace("$PLAYER_NAME", MinecraftClient.getInstance().player.getName().getString()));
        }
        possibleQuestions.clear();
        for(DialogNode question : response.questions) {
            possibleQuestions.add(question.title.replace("$PLAYER_NAME", MinecraftClient.getInstance().player.getName().getString()));
        }
        lineInQueue = responseLinesQueue.get(0);
        lineInQueueFinishTick = DialogUtil.getTickForText(lineInQueue);
        tick = 0;
        setFocused(true);
    }

    private void consumeMessage() {
        if (trailingLine.isEmpty()) {
            return;
        }
        responseLines.add(trailingLine);
        responseLinesQueue.remove(trailingLine);
        lineInQueue = "";
        setScrollY(getMaxScrollY());
        if (!responseLinesQueue.isEmpty()) {
            lineInQueue = responseLinesQueue.get(0);
            lineInQueueFinishTick = DialogUtil.getTickForText(lineInQueue);
            tick = -5;
            processAction(lineInQueue);
        }
    }

    private void processAction(String lineInQueue) {
        switch (lineInQueue) {
            case Responses.EXIT -> MinecraftClient.getInstance().currentScreen.close();
            case Responses.TRADE -> ClientPacketSender.sendOpenSellShopRequest();
        }
    }

    private boolean finishedRenderingLine() {
        return tick > lineInQueueFinishTick;
    }

    public void finishLine(){
        tick = lineInQueueFinishTick;
        trailingLine = lineInQueue;
    }

    @Override
    protected void renderContents(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        renderMessages(matrixStack);
        if (responseLinesQueue.isEmpty()) {
            renderPossibleQuestions(matrixStack);
            return;
        }
        renderTrailingLine(matrixStack);
    }

    private void renderMessages(MatrixStack matrixStack) {
        for(int i = 0; i < responseLines.size(); i++) {
            drawTextWithShadow(matrixStack, textRenderer, Text.of(responseLines.get(i)), x + getPadding(), y + i * lineHeight + getPadding(), Color.LIGHT_GRAY.getRGB());
        }
    }

    private void renderPossibleQuestions(MatrixStack matrixStack) {
        for(int i = 0; i < response.questions.size(); i++) {
            drawTextWithShadow(matrixStack, textRenderer, Text.of("["+(i+1)+"] " + possibleQuestions.get(i)), x + getPadding(), y + responseLines.size() * lineHeight + i * lineHeight + getPadding() + 2, Color.WHITE.getRGB());
        }
    }

    private void renderTrailingLine(MatrixStack matrixStack) {
        if (trailingLine.isEmpty()) {
            return;
        }
        drawTextWithShadow(matrixStack, textRenderer, Text.of(trailingLine), x + getPadding(), y + responseLines.size() * lineHeight + getPadding(), Color.WHITE.getRGB());
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

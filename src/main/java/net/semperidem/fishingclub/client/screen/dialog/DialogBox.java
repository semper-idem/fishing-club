package net.semperidem.fishingclub.client.screen.dialog;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.network.payload.DialogResponsePayload;
import net.semperidem.fishingclub.screen.dialog.DialogNode;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;

public class DialogBox extends ScrollableWidget {
    ArrayList<String> responseLines = new ArrayList<>();
    String trailingLine = "";
    String lineInQueue = "";
    DialogNode response;
    ArrayList<String> responseLinesQueue = new ArrayList<>();
    ArrayList<String> possibleQuestions = new ArrayList<>();
    int lineInQueueFinishTick = 0;
    int tick = 0;
    int lineHeight = 11;
    public int textSpeed = 1;


    public DialogBox(int x, int y, int width, int height) {
        super(x,y, width, height, Text.empty());
    }

    public void resize(int x, int y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.width = width;
        this.height = height;
    }


    private void renderScrollbar(DrawContext context) {
        int i = MathHelper.clamp((int)((int)((float)(this.height * this.height) / (float)this.getContentsHeightWithPadding())), (int)32, (int)this.height);
        int l = Math.max(getY(), (int)this.getScrollY() * (this.height - i) / this.getMaxScrollY() + this.getY());
        int m = l + i;
        int scrollBarX = getX() + width;
        int scrollBarWidth = 4;
        int scrollBarShadowWidth = 1;
        context.fill(
                scrollBarX - scrollBarWidth,
                l,
                scrollBarX,
                m,
                Color.BLACK.getRGB()
        );
        context.fill(
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

    @Override
    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
        renderMessages(context);
        if (responseLinesQueue.isEmpty()) {
            renderPossibleQuestions(context);
            return;
        }
        renderTrailingLine(context);
    }

    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.visible) {
            return;
        }
        context.enableScissor(getX() + 1, getY() + 1, getX() + this.width - 1, getY() + this.height - 1);
        context.getMatrices().push();
        context.getMatrices().translate(0.0, -this.getScrollY(), 0.0);
        this.renderContents(context, mouseX, mouseY, delta);
        context.getMatrices().pop();
        context.disableScissor();
        if (overflows()) {
            renderScrollbar(context);
        }
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
            processAction();
        }
    }

    private void processAction() {
        if (response.specialAction == null) {
            return;
        }
        ClientPlayNetworking.send(new DialogResponsePayload(response.specialAction));
    }

    private boolean finishedRenderingLine() {
        return tick > lineInQueueFinishTick;
    }

    public void finishLine(){
        tick = lineInQueueFinishTick;
        trailingLine = lineInQueue;
    }

    private void renderMessages(DrawContext context) {
        for(int i = 0; i < responseLines.size(); i++) {
            context.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    Text.of(responseLines.get(i)),
                    getX() + getPadding(),
                    getY() + i * lineHeight + getPadding(),
                    Color.LIGHT_GRAY.getRGB(),
                    true
            );
        }
    }

    private void renderPossibleQuestions(DrawContext context) {
        for(int i = 0; i < response.questions.size(); i++) {
            context.drawText(
                    MinecraftClient.getInstance().textRenderer,
                    Text.of("["+(i+1)+"] " + possibleQuestions.get(i)),
                    getX() + getPadding(),
                    getY() + responseLines.size() * lineHeight + i * lineHeight + getPadding() + 2,
                    Color.WHITE.getRGB(),
                    true);
        }
    }

    private void renderTrailingLine(DrawContext context) {
        if (trailingLine.isEmpty()) {
            return;
        }
        context.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.of(trailingLine), getX() + getPadding(),
                getY() + responseLines.size() * lineHeight + getPadding(),
                Color.WHITE.getRGB(),
                true);
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
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}

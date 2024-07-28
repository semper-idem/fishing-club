package net.semperidem.fishing_club.client.screen.dialog;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishing_club.network.payload.DialogResponsePayload;
import net.semperidem.fishing_club.screen.dialog.DialogNode;

import java.awt.*;
import java.util.ArrayList;

public class DialogBox extends ScrollableWidget {
    private static final int LINE_HEIGHT = 11;
    private static final int HISTORY_FONT_COLOR = Color.LIGHT_GRAY.getRGB();

    ArrayList<DialogNode> history = new ArrayList<>();
    ArrayList<Text> currentFinishedLines =  new ArrayList<>();
    DialogNode current;
    Text currentText;
    boolean isCurrentNodeFinished = false;
    boolean isCurrentLineFinished = false;
    int charCredit;
    int lineCount;
    int creditGain = 1;

    int contentHeight = 0;
    TextRenderer textRenderer;



    public DialogBox(int x, int y, int width, int height, TextRenderer textRenderer, DialogNode startNode) {
        super(x,y, width, height, Text.empty());
        this.textRenderer = textRenderer;
        this.current = startNode;
        this.tick();
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
       return contentHeight;
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
        this.lineCount = 0;
        this.renderHistory(context);
        this.renderCurrent(context);
        this.contentHeight = this.lineCount * LINE_HEIGHT;
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
        this.charCredit += this.creditGain;
        this.calculateAffordableCurrentText();
        if (!this.isCurrentLineFinished) {
            return;
        }
        if (this.currentFinishedLines.size() >= this.current.responseLines.size()) {
            return;
        }
        this.currentFinishedLines.add(this.currentText);
        this.currentText = Text.empty();
        this.setScrollY(this.getMaxScrollY());
        this.charCredit = 0;
    }

    public void pickDialog(DialogNode node) {
        this.history.add(this.current);
        this.current = node;
        this.isCurrentLineFinished = false;
        this.isCurrentNodeFinished = false;
        this.charCredit = 0;
        this.currentFinishedLines.clear();
        this.setFocused(true);
        this.processAction();
    }


    private void processAction() {
         if (this.current.action == null) {
            return;
        }
        ClientPlayNetworking.send(new DialogResponsePayload(this.current.action.toString()));
    }

    public void finishLine(){
        this.charCredit = 9999;//getting really weird
    }

    private void renderCurrent(DrawContext context) {
        if (this.current.parent != null) {
            this.renderHistoryLine(context, this.current.playerSays);
        }
        for(Text currentFinishedLine : this.currentFinishedLines){
            this.renderHistoryLine(context, currentFinishedLine);
        }
        this.renderCurrentLine(context);
        if (this.isCurrentNodeFinished) {
            this.renderCurrentQuestions(context);
        }
    }

    private void renderCurrentQuestions(DrawContext context) {

        for(DialogNode questionNode : this.current.questions) {
            String question = questionNode.playerSays.getString().replace("-", "["+ (this.current.questions.indexOf(questionNode) + 1)+"]");
            context.drawText(
                this.textRenderer,
                question,
                this.getX() + this.getPadding(),
                this.getY() + lineCount * LINE_HEIGHT,
                HISTORY_FONT_COLOR,
                false
            );
            this.lineCount++;
        }
    }

    private void renderCurrentLine(DrawContext context) {
        if (this.currentText.getString().isEmpty()) {
            return;
        }
        context.drawText(
            this.textRenderer,
            this.currentText,
            this.getX() + this.getPadding(),
            this.getY() + lineCount * LINE_HEIGHT,
            HISTORY_FONT_COLOR,
            false
        );
        this.lineCount++;
    }

    private void calculateAffordableCurrentText() {
        int currentLineIndex = this.currentFinishedLines.size();
        if (this.current.responseLines.size() <= currentLineIndex) {
            this.isCurrentNodeFinished = true;
            this.currentText = Text.empty();
            return;
        }
        String currentLineString = this.current.responseLines.get(this.currentFinishedLines.size()).getString();
        int currentLineFinish = 0;
        int currentLineCost = 0;
        this.isCurrentLineFinished = true;
        while(currentLineFinish < currentLineString.length()){
            currentLineCost += getCharCost(currentLineString.charAt(currentLineFinish));
            currentLineFinish++;
            if (currentLineCost > this.charCredit) {
                this.isCurrentLineFinished = false;
                break;
            }
        }
		this.currentText = Text.of(currentLineString.substring(0, currentLineFinish));
    }

    private static int getCharCost(char c) {
        return switch (c) {
            case '.' -> 15;
            case '?', '!' -> 10;
            case ',' -> 3;
            case ' ' -> 2;
            default -> 1;
        };
    }
    private void renderHistoryLine(DrawContext context, Text line) {
        context.drawText(
            this.textRenderer,
            line,
            this.getX() + this.getPadding(),
            this.getY() + lineCount * LINE_HEIGHT,
            HISTORY_FONT_COLOR,
            false
        );
        this.lineCount++;
    }

    private void renderHistoryNode(DrawContext context, DialogNode historyNode) {
        if (historyNode.parent != null) {
            this.renderHistoryLine(context, historyNode.parent.playerSays);
        }
        for (Text historyLine : historyNode.responseLines) {
            this.renderHistoryLine(context, historyLine);
        }
    }

    private void renderHistory(DrawContext context) {
        for (DialogNode historyNode : this.history) {
            this.renderHistoryNode(context, historyNode);
        }

    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_SPACE && !this.isCurrentLineFinished) {
            this.finishLine();
            return true;
        }
        if (!this.isCurrentNodeFinished) {
            return false;
        }
        if (keyCode > InputUtil.GLFW_KEY_0 && keyCode <= InputUtil.GLFW_KEY_0 + this.current.questions.size()) {
            this.pickDialog(this.current.questions.get(keyCode - InputUtil.GLFW_KEY_0 - 1));
            return true;
        }
        return false;
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

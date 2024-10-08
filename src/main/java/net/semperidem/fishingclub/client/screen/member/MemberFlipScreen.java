package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.util.TextUtil;

import java.util.ArrayList;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.*;
import static net.semperidem.fishingclub.util.TextUtil.TEXT_HEIGHT;


public class MemberFlipScreen extends MemberSubScreen {
    String playerChoice;
    MemberButton tailsButton;
    MemberButton headsButton;
    MemberButton maxAmountButton;
    ClampedFieldWidget flipAmountField;
    private static final int buttonWidth = 50;
    private static final int buttonHeight = 20;
    private static final int amountFieldWidth = 100;

    int baseX,baseY;
    int titleX, titleY;
    int historyXLeft, historyXMiddle, historyXMiddle2,  historyXRight;
    int historyY, historyMaxY;
    int headsX, headsY;
    int tailsX, tailsY;
    int amountFieldX, amountFieldY;
    int buttonBoxX0, buttonBoxX1, buttonBoxY0, buttonBoxY1;


    MemberFlipScreen(MemberScreen parent, Text title) {
        super(parent, title);
    }



    @Override
    public void init() {
        super.init();
        baseX = parent.x + TILE_SIZE * 12;
        baseY = parent.y + TILE_SIZE * 2;

        titleX = baseX;
        titleY = baseY - 1;



        amountFieldX = titleX;
        amountFieldY = parent.height - 2 * TILE_SIZE - buttonHeight - 2;

        historyXLeft = titleX + amountFieldWidth + 2 * TILE_SIZE;
        historyXMiddle = historyXLeft + 10 * TILE_SIZE;
        historyXMiddle2 = historyXMiddle + 10 * TILE_SIZE;
        historyXRight = parent.creditX;
        historyY = parent.height - TILE_SIZE - TEXT_HEIGHT + 2;
        historyMaxY = parent.creditY + TEXT_HEIGHT;

        headsX = amountFieldX - 1;
        headsY = amountFieldY - buttonHeight - 2;

        tailsX = headsX + buttonWidth + 2;
        tailsY = headsY;
        flipAmountField = new ClampedFieldWidget(textRenderer, amountFieldX,amountFieldY,amountFieldWidth,buttonHeight, Text.of("Toss Amount:"), parent);

        tailsButton = new MemberButton(tailsX,tailsY,buttonWidth,buttonHeight, Text.of("Tails"), tossCoin());
        headsButton = new MemberButton(headsX,headsY,buttonWidth,buttonHeight, Text.of("Heads"), tossCoin());

        maxAmountButton = new MemberButton(amountFieldX + amountFieldWidth - 30,amountFieldY,30,buttonHeight, Text.of("Max"), o -> {
            flipAmountField.setMaxAmount();
        });
        components.add(tailsButton);
        components.add(headsButton);
        components.add(flipAmountField);
        components.add(maxAmountButton);

        buttonBoxX0 = amountFieldX - 1;
        buttonBoxX1 = amountFieldX + amountFieldWidth + 1;
        buttonBoxY0 = headsY;
        buttonBoxY1 = amountFieldY + buttonHeight + 1;
    }


    @Override
    public int unlockLevel() {
        return 15;
    }

    public ButtonWidget.PressAction tossCoin() {
        return button -> {
            playerChoice = button.getMessage().getString();
            int amount = flipAmountField.getValidAmount();
            this.parent.getScreenHandler().tossCoin(amount, playerChoice);
        };
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        parent.drawContainerBox(context, buttonBoxX0, buttonBoxY0, buttonBoxX1, buttonBoxY1, true);
        parent.drawContainerBox(context, historyXLeft - 2, historyMaxY + 1, historyXRight + 3, historyY + 10, true);
        context.drawTextWithShadow(textRenderer,  Text.of("Coin Toss"), titleX , titleY, BEIGE_TEXT_COLOR);
        int historyEntryY = historyY;
        ArrayList<String> tossHistory = parent.getScreenHandler().getCoinFlipHistory();
        for(int i = tossHistory.size() - 1; i >= 0; i--) {
            String tossEntry = tossHistory.get(i);
            if (historyEntryY  < historyMaxY) {
                break;
            }
            String[] parts = tossEntry.split(";");
            if (parts.length != 4) {
                continue;
            }
            context.drawTextWithShadow(textRenderer, Text.of(parts[0]), historyXLeft, historyEntryY, BEIGE_TEXT_COLOR);
            context.drawTextWithShadow(textRenderer, Text.of(parts[1]), historyXMiddle, historyEntryY, BEIGE_TEXT_COLOR);
            context.drawTextWithShadow(textRenderer, Text.of(parts[2]), historyXMiddle2, historyEntryY, BEIGE_TEXT_COLOR);
            TextUtil.drawTextRightAlignedTo(textRenderer, context, Text.of(parts[3]), historyXRight, historyEntryY, CREDIT_COLOR, CREDIT_OUTLINE_COLOR);
            historyEntryY -= TEXT_HEIGHT;

        }


        super.render(context, mouseX, mouseY, delta);
    }
}

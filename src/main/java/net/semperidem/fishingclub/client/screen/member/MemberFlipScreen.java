package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.util.TextUtil;

import java.util.HashMap;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.*;
import static net.semperidem.fishingclub.util.TextUtil.TEXT_HEIGHT;


public class MemberFlipScreen extends MemberSubScreen {
    String playerChoice;
    ButtonWidget tailsButton;
    ButtonWidget headsButton;
    ButtonWidget maxAmountButton;
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


    MemberFlipScreen(MemberScreen parent) {
        super(parent);
    }



    @Override
    public void init() {
        super.init();
        baseX = parent.x + TILE_SIZE * 12;
        baseY = parent.y + TILE_SIZE * 2;

        titleX = baseX;
        titleY = baseY;



        amountFieldX = titleX;
        amountFieldY = parent.height - 2 * TILE_SIZE - buttonHeight - 2;

        historyXLeft = titleX + amountFieldWidth + 2 * TILE_SIZE;
        historyXMiddle = historyXLeft + 10 * TILE_SIZE;
        historyXMiddle2 = historyXMiddle + 10 * TILE_SIZE;
        historyXRight = parent.creditX;
        historyY = parent.height - TILE_SIZE - TEXT_HEIGHT;
        historyMaxY = parent.creditY + TEXT_HEIGHT;

        headsX = amountFieldX - 1;
        headsY = amountFieldY - buttonHeight - 2;

        tailsX = headsX + buttonWidth + 2;
        tailsY = headsY;
        flipAmountField = new ClampedFieldWidget(parent.getTextRenderer(), amountFieldX,amountFieldY,amountFieldWidth,buttonHeight, Text.of("Toss Amount:"), parent);

        tailsButton = new ButtonWidget(tailsX,tailsY,buttonWidth,buttonHeight, Text.of("Tails"), tossCoin());
        headsButton = new ButtonWidget(headsX,headsY,buttonWidth,buttonHeight, Text.of("Heads"), tossCoin());

        maxAmountButton = new ButtonWidget(amountFieldX + amountFieldWidth - 24,amountFieldY,24,buttonHeight, Text.of("Max"), o -> {
            flipAmountField.setMaxAmount();
        });
        components.add(tailsButton);
        components.add(headsButton);
        components.add(flipAmountField);
        components.add(maxAmountButton);
    }

    public ButtonWidget.PressAction tossCoin() {
        return button -> {
            playerChoice = button.getMessage().getString();
            int amount = flipAmountField.getValidAmount();
            this.parent.getScreenHandler().tossCoin(amount, playerChoice);
        };
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of("Coin Toss"), titleX , titleY, BEIGE_TEXT_COLOR);
        int historyEntryY = historyY;
        HashMap<Integer, String> tossHistory = parent.getScreenHandler().getTossHistory();
        for(int i = tossHistory.size(); i > 0; i--) {
            String tossEntry = tossHistory.get(i);
            if (historyEntryY  < historyMaxY) {
                break;
            }
            String[] parts = tossEntry.split(";");
            if (parts.length != 4) {
                continue;
            }
            parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of(parts[0]), historyXLeft, historyEntryY, WHITE_TEXT_COLOR);
            parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of(parts[1]), historyXMiddle, historyEntryY, WHITE_TEXT_COLOR);
            parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of(parts[2]), historyXMiddle2, historyEntryY, WHITE_TEXT_COLOR);
            TextUtil.drawOutlinedTextRightAlignedTo(parent.getTextRenderer(), matrixStack, Text.of(parts[3]), historyXRight, historyEntryY, CREDIT_COLOR, CREDIT_OUTLINE_COLOR);
            historyEntryY -= TEXT_HEIGHT;

        }


        super.render(matrixStack, mouseX, mouseY, delta);
    }
}

package net.semperidem.fishingclub.client.screen.member;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.*;
import static net.semperidem.fishingclub.util.TextUtil.TEXT_HEIGHT;


public class MemberFlipScreen extends MemberSubScreen {
    HashMap<Integer, String> tossResult = new HashMap<>();
    String playerChoice;
    ButtonWidget tailsButton;
    ButtonWidget headsButton;
    ButtonWidget maxAmountButton;
    String currentPlayerCredit;
    ClampedFieldWidget flipAmountField;
    private static final int buttonWidth = 50;
    private static final int buttonHeight = 20;
    private static final int amountFieldWidth = 100;

    int baseX,baseY;
    int titleX, titleY;
    int historyXLeft, historyXMiddle, historyXRight;
    int historyY, historyMaxY;
    int headsX, headsY;
    int tailsX, tailsY;
    int creditX, creditY;
    int amountFieldX, amountFieldY;
    ArrayList<String> tossHistory = new ArrayList<>();


    MemberFlipScreen(MemberScreen parent) {
        super(parent);
        this.currentPlayerCredit = String.valueOf(parent.getScreenHandler().getCard().getCredit());
    }



    @Override
    public void init() {
        super.init();
        baseX = parent.x + TILE_SIZE * 12;
        baseY = parent.y + TILE_SIZE * 2;

        titleX = baseX;
        titleY = baseY;


        creditX = parent.x + TEXTURE.renderWidth - BUTTON_WIDTH - 3 * TILE_SIZE;
        creditY = titleY;


        amountFieldX = titleX;
        amountFieldY = parent.height - 2 * TILE_SIZE - buttonHeight - 2;

        historyXLeft = titleX + amountFieldWidth + 2 * TILE_SIZE;
        historyXMiddle = historyXLeft + 10 * TILE_SIZE;
        historyXRight = creditX;
        historyY = parent.height - TILE_SIZE - TEXT_HEIGHT;
        historyMaxY = creditY + TEXT_HEIGHT;

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
            tossHistory.add("Picked;" + playerChoice + ";For:  " + amount + "$");
        };
    }

    @Override
    public void handledScreenTick() {
        HashMap<Integer, String> serverTossResult = parent.getScreenHandler().getTossResult();
        if (tossResult.size() != serverTossResult.size()) {
            tossResult.clear();
            tossResult.putAll(serverTossResult);
            tossHistory.add(tossResult.get(tossResult.size()));
        }
        this.currentPlayerCredit = String.valueOf(parent.getScreenHandler().getCard().getCredit());
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of("Coin Toss"), titleX , titleY, Color.WHITE.getRGB());
        int historyEntryY = historyY;
        for(String tossEntry : Lists.reverse(tossHistory)) {
            if (historyEntryY  < historyMaxY) {
                break;
            }
            String[] parts = tossEntry.split(";");
            if (parts.length != 3) {
                break;
            }
            parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of(parts[0]), historyXLeft, historyEntryY, Color.LIGHT_GRAY.getRGB());
            parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of(parts[1]), historyXMiddle, historyEntryY, Color.LIGHT_GRAY.getRGB());
            int rightPartWidth = parent.getTextRenderer().getWidth(parts[2]);
            parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of(parts[2]), historyXRight - rightPartWidth, historyEntryY, Color.LIGHT_GRAY.getRGB());
            historyEntryY -= TEXT_HEIGHT;

        }

        String creditString = "Credit: " + currentPlayerCredit + "$";
        int creditOffset = parent.getTextRenderer().getWidth(creditString);
        parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of(creditString), creditX - creditOffset, creditY, Color.WHITE.getRGB());

        super.render(matrixStack, mouseX, mouseY, delta);
    }
}

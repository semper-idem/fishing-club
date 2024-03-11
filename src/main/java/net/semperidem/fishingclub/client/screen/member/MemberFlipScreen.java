package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TILE_SIZE;


public class MemberFlipScreen implements MemberSubScreen{
    String lastTossWinner;
    String playerChoice;
    ButtonWidget flipButton;
    ButtonWidget tailsButton;
    ButtonWidget headsButton;
    String currentPlayerCredit;
    TextFieldWidget flipAmountField;
    MemberScreen parent;
    private static final int buttonWidth = TILE_SIZE * 10;
    private static final int buttonHeight = TILE_SIZE * 5;
    private static final int amountFieldWidth = TILE_SIZE * 20;

    int baseX,baseY;
    int titleX, titleY;
    int winnerX, winnerY;
    int flipX, flipY;
    int headsX, headsY;
    int tailsX, tailsY;
    int creditX, creditY;
    int amountFieldX, amountFieldY;

    private static final int TEXT_HEIGHT = 12;

    MemberFlipScreen(MemberScreen parent) {
        this.parent = parent;
    }


    public ButtonWidget.PressAction setChoice() {
        return button -> playerChoice = button.getMessage().getString().toLowerCase();
    }

    @Override
    public void init() {
        baseX = parent.x + TILE_SIZE * 11;
        baseY = parent.y + TILE_SIZE * 2;

        titleX = baseX;
        titleY = baseY;

        winnerX = baseX + TILE_SIZE * 20;
        winnerY = baseY;

        creditX = baseX + TILE_SIZE * 20;
        creditY = winnerY + TEXT_HEIGHT + TILE_SIZE;

        amountFieldY = baseY;
        amountFieldX = baseX;

        flipX = creditX;
        flipY = baseY;

        tailsX = baseX;
        tailsY = baseY + buttonHeight + TILE_SIZE;

        headsX = baseX + buttonWidth + TILE_SIZE;
        headsY = baseY + buttonHeight + TILE_SIZE;

        flipButton = new ButtonWidget(flipX ,flipY ,buttonWidth,buttonHeight, Text.of("Toss"), button -> tossCoin());
        tailsButton = new ButtonWidget(tailsX,tailsY,buttonWidth,buttonHeight, Text.of("Tails"), setChoice());
        headsButton = new ButtonWidget(headsX,headsY,buttonWidth,buttonHeight, Text.of("Heads"), setChoice());
        flipAmountField = new TextFieldWidget(parent.getTextRenderer(), amountFieldX,amountFieldY,amountFieldWidth,buttonHeight, Text.of("Toss Amount:"));

    }

    @Override
    public ArrayList<Drawable> getComponents() {
        init();
        return new ArrayList<>(List.of(flipButton, tailsButton, headsButton, flipAmountField));
    }

    @Override
    public void handledScreenTick() {
        this.lastTossWinner = parent.getScreenHandler().getWinner();
        this.currentPlayerCredit = String.valueOf(parent.getScreenHandler().getCard().getCredit());
    }

    public void tossCoin() {
        parseAmount(flipAmountField.getText()).ifPresent(amount ->  this.parent.getScreenHandler().tossCoin(amount, playerChoice));
    }

    private Optional<Integer> parseAmount(String input) {
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

        // Text Rendering
        parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of("Coin Toss"), titleX , titleY, Color.WHITE.getRGB());
        if (!lastTossWinner.isEmpty()) {
            parent.getTextRenderer().draw(matrixStack,  Text.of("Last Winner: " + lastTossWinner), winnerX, winnerY, Color.BLACK.getRGB());
        }
        parent.getTextRenderer().draw(matrixStack,  Text.of("Your Credit: " + currentPlayerCredit), creditX, creditY, Color.WHITE.getRGB());

        // Render all components from getComponents()
        for (Drawable drawable : this.getComponents()) {
            drawable.render(matrixStack, mouseX, mouseY, delta);
        }
    }
}

package net.semperidem.fishingclub.client.screen.member;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TEXTURE;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TILE_SIZE;


public class MemberFlipScreen implements MemberSubScreen{
    HashMap<Integer, String> tossResult = new HashMap<>();
    String playerChoice;
    ButtonWidget tailsButton;
    ButtonWidget headsButton;
    ButtonWidget maxAmountButton;
    String currentPlayerCredit = "0";
    ClampedFieldWidget flipAmountField;
    MemberScreen parent;
    private static final int buttonWidth = TILE_SIZE * 10;
    private static final int buttonHeight = TILE_SIZE * 5;
    private static final int amountFieldWidth = TILE_SIZE * 20;

    int baseX,baseY;
    int titleX, titleY;
    int historyXLeft, historyXMiddle, historyXRight;
    int historyY, historyMaxY;
    int headsX, headsY;
    int tailsX, tailsY;
    int creditX, creditY;
    int amountFieldX, amountFieldY;
    ArrayList<String> tossHistory = new ArrayList<>();
    ArrayList<Drawable> components = new ArrayList<>();

    private static final int TEXT_HEIGHT = 12;

    MemberFlipScreen(MemberScreen parent) {
        this.parent = parent;
        this.currentPlayerCredit = String.valueOf(parent.getScreenHandler().getCard().getCredit());
    }



    @Override
    public void init() {
        baseX = parent.x + TILE_SIZE * 11;
        baseY = parent.y + TILE_SIZE * 2;

        titleX = baseX;
        titleY = baseY;


        creditX = parent.x + TEXTURE.renderWidth - TILE_SIZE * 20;;
        creditY = titleY;


        amountFieldX = titleX;
        amountFieldY = parent.height - 2 * TILE_SIZE - buttonHeight;

        historyXLeft = titleX + amountFieldWidth + TILE_SIZE;
        historyXMiddle = historyXLeft + 10 * TILE_SIZE;
        historyXRight = creditX;
        historyY = parent.height - TILE_SIZE - TEXT_HEIGHT;
        historyMaxY = creditY + TEXT_HEIGHT;

        headsX = amountFieldX - 1;
        headsY = amountFieldY - buttonHeight - 2;

        tailsX = headsX + buttonWidth + 2;
        tailsY = headsY;

        tailsButton = new ButtonWidget(tailsX,tailsY,buttonWidth,buttonHeight, Text.of("Tails"), tossCoin());
        headsButton = new ButtonWidget(headsX,headsY,buttonWidth,buttonHeight, Text.of("Heads"), tossCoin());
        flipAmountField = new ClampedFieldWidget(parent.getTextRenderer(), amountFieldX,amountFieldY,amountFieldWidth,buttonHeight, Text.of("Toss Amount:"), parent);

        maxAmountButton = new ButtonWidget(amountFieldX + amountFieldWidth - 24,amountFieldY,24,buttonHeight, Text.of("Max"), o -> {
            flipAmountField.setMaxAmount();
        });
        components.clear();
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
    public ArrayList<Drawable> getComponents() {
        return components;
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
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        boolean consumed = false;
        for(ClickableWidget clickable : List.of(tailsButton, headsButton, flipAmountField)) {
            if (consumed) {
                return true;
            }
            consumed = clickable.mouseScrolled(mouseX, mouseY, amount);
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean consumed = false;
        for(ClickableWidget clickable : List.of(tailsButton, headsButton, maxAmountButton, flipAmountField)) {
            if (consumed) {
                return true;
            }
            consumed = clickable.mouseClicked(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return flipAmountField.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return flipAmountField.charTyped(chr, modifiers);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

        // Text Rendering
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

        String creditString = "Your Credit: " + currentPlayerCredit + "$";
        int creditOffset = parent.getTextRenderer().getWidth(creditString);
        parent.getTextRenderer().drawWithShadow(matrixStack,  Text.of(creditString), creditX - creditOffset, creditY, Color.WHITE.getRGB());

        // Render all components from getComponents()
        for (Drawable drawable : components) {
            drawable.render(matrixStack, mouseX, mouseY, delta);
        }
    }
}

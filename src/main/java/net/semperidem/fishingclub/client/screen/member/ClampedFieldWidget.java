package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

import java.util.Optional;

public class ClampedFieldWidget extends TextFieldWidget {
    MemberScreen parent;
    public ClampedFieldWidget(TextRenderer textRenderer, int x, int y, int width, int height, Text text, MemberScreen parent) {
        super(textRenderer, x, y, width, height, text);
        this.parent = parent;
    }

    private Optional<Integer> parseAmount(String input) {
        try {
            return Optional.of(Integer.parseInt(input));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    void setMaxAmount() {
        setText(String.valueOf(getMaxAmount()));
    }

    int getValidAmount() {
        int amount = getAmount();
        int maxAmount = getMaxAmount();
        if (amount > maxAmount) {
            amount = maxAmount;
            setText(String.valueOf(amount));
        }
        if (amount <= 0) {
            amount = 0;
            setText(String.valueOf(amount));
        }
        return amount;
    }

    private int getAmount() {
        return parseAmount(getText()).orElse(0);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (chr >= InputUtil.GLFW_KEY_0 && chr <= InputUtil.GLFW_KEY_9) {
            boolean consumed = super.charTyped(chr, modifiers);
            if (consumed) {
                validateAmount();
            }
            return consumed;
        }
        return false;
    }

    @Override
    protected void onFocusedChanged(boolean newFocused) {
        validateAmount();
        super.onFocusedChanged(newFocused);
    }

    private int getMaxAmount() {
        return parent.getScreenHandler().getCard().getCredit();
    }

    private void validateAmount() {
        getValidAmount();
        if (getText().charAt(0) == '0' && getText().length() > 1) {
            setText(getText().substring(1));
        }
    }
}

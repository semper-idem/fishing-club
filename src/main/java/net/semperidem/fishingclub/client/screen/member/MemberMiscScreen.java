package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.util.Optional;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TEXTURE;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TILE_SIZE;
import static net.semperidem.fishingclub.util.TextUtil.*;

public class MemberMiscScreen extends MemberSubScreen {
    ButtonWidget resetPerksButton;
    ButtonWidget submitBidButton;
    ClampedFieldWidget bidField;
    String resetCostString = "Free";
    String creditString = "0";
    String minBidString = "0";
    String capeHolderString = "";
    Text resetText = Text.literal("Reset Perks");
    Text resetCostText = Text.literal("Cost: " + resetCostString);
    Text creditText = Text.literal("Credit: " + creditString + "$");
    Text minBidText = Text.literal("Min: " + minBidString + "$");
    Text title = Text.literal("Special Services");
    Text capeHolderText = Text.literal(capeHolderString);
    Text fishingKingText = Text.literal("Fishing King Title");

    private final static int BUTTON_WIDTH = 100, BUTTON_HEIGHT = 20;
    private int baseX, baseY;
    private int titleX, titleY;
    private int resetCostX, resetCostY;
    private int resetButtonX, resetButtonY;
    private int resetX, resetY;
    private int bidButtonX, bidButtonY;//TODO Rename to claim
    private int kingTitleX, kingTitleY;
    private int capeHolderX, capeHolderY;
    private int bidFieldX, bidFieldY;
    private int minimumBidX, minimumBidY;
    private int creditX, creditY;
    private int bidFieldWidth = 100;
    private int bidButtonWidth = 40;

    public MemberMiscScreen(MemberScreen parent) {
        super(parent);
    }

    @Override
    public void init() {
        super.init();

        baseX = parent.x + TILE_SIZE * 12;
        baseY = parent.y + TILE_SIZE * 2;

        titleX = baseX;
        titleY = baseY;

        resetButtonX = parent.x + TEXTURE.textureWidth - TILE_SIZE * 26 - BUTTON_WIDTH;
        resetButtonY = parent.y + TEXTURE.textureHeight - TILE_SIZE * 2 - BUTTON_HEIGHT - 2;

        resetCostX = resetButtonX + BUTTON_WIDTH / 2;
        resetCostY = resetButtonY - TEXT_HEIGHT;

        resetX = resetCostX;
        resetY = resetCostY - TEXT_HEIGHT * 3;

        bidFieldX = baseX;
        bidFieldY = parent.y + TEXTURE.textureHeight - BUTTON_HEIGHT - 2 * TILE_SIZE - 2;

        bidButtonX = bidFieldX + bidFieldWidth - bidButtonWidth;
        bidButtonY = bidFieldY;


        minimumBidX = bidFieldX + bidFieldWidth / 2;
        minimumBidY = bidButtonY - TEXT_HEIGHT;

        capeHolderX = kingTitleX;
        capeHolderY = minimumBidY - 2 * TEXT_HEIGHT;

        kingTitleX = minimumBidX;
        kingTitleY = capeHolderY - TEXT_HEIGHT;

        creditX = parent.x + TEXTURE.renderWidth - MemberScreen.BUTTON_WIDTH - 3 * TILE_SIZE;
        creditY = titleY;


        resetPerksButton = new ButtonWidget(resetButtonX, resetButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal("Reset"), button -> {
            if (parent.getScreenHandler().getCard().canResetPerks()) {
                ClientPacketSender.sendResetPerk();
            }
        });

        bidField = new ClampedFieldWidget(parent.getTextRenderer(), bidFieldX, bidFieldY, bidFieldWidth, BUTTON_HEIGHT, Text.literal(""), parent);
        bidField.setMaxLength(10);
        bidField.setText(minBidString);

        submitBidButton = new ButtonWidget(bidButtonX, bidButtonY, bidButtonWidth, BUTTON_HEIGHT, Text.literal("Claim"), button -> {
            // TODO: Add logic for submitting a bid
        });


        components.add(resetPerksButton);
        components.add(bidField);
        components.add(submitBidButton);
    }

    @Override
    public void handledScreenTick() {
        int resetCost = parent.getScreenHandler().getCard().getResetCost();
        resetCostString = resetCost == 0 ? "Free (Once)" : (resetCost + "$");
        creditString = String.valueOf(parent.getScreenHandler().getCard().getCredit());
        creditText = Text.literal("Credit: " + creditString + "$");
        Optional.ofNullable(resetPerksButton).ifPresent(o -> o.active = parent.getScreenHandler().getCard().canResetPerks());
        resetCostText = Text.literal("Cost: " + resetCostString);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        drawText(parent.getTextRenderer(), matrixStack, title, titleX, titleY, 0xFFFFFF);
        drawTextCenteredAt(parent.getTextRenderer(), matrixStack, resetText, resetX, resetY, 0xFFFFFF);
        drawTextCenteredAt(parent.getTextRenderer(), matrixStack, resetCostText, resetCostX, resetCostY, 0xFFFFFF);
        drawTextRightAlignedTo(parent.getTextRenderer(), matrixStack, creditText, creditX, creditY, 0xFFFFFF);
        drawTextCenteredAt(parent.getTextRenderer(), matrixStack, minBidText, minimumBidX, minimumBidY, 0xFFFFFF);
        drawTextCenteredAt(parent.getTextRenderer(), matrixStack, capeHolderText, capeHolderX, capeHolderY, 0xFFFFFF);
        drawTextCenteredAt(parent.getTextRenderer(), matrixStack, fishingKingText, kingTitleX, kingTitleY, 0xFFFFFF);

        super.render(matrixStack, mouseX, mouseY, delta);
    }
}

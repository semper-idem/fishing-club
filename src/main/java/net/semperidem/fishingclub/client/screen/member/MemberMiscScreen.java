package net.semperidem.fishingclub.client.screen.member;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.network.payload.TitleClaimPayload;

import java.util.Optional;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.*;
import static net.semperidem.fishingclub.util.TextUtil.*;

public class MemberMiscScreen extends MemberSubScreen {
    DemandingButtonWidget resetPerksButton;
    DemandingButtonWidget submitClaimButton;
    ClampedFieldWidget claimField;
    String resetCostString = "Free";
    String minClaimString = "0";
    String capeHolderString = "";
    Text resetText = Text.literal("Reset Perks");
    Text resetCostText = Text.literal("Cost: " + resetCostString);
    Text minClaimText = Text.literal("Min: " + minClaimString + "$");
    Text title = Text.literal("Special Services");
    Text capeHolderText = Text.literal(capeHolderString);
    Text fishingKingText = Text.literal("Fishing King Title");


    private final static int BUTTON_WIDTH = 100, BUTTON_HEIGHT = 20;
    private int baseX, baseY;
    private int resetCostX, resetCostY;
    private int resetButtonX, resetButtonY;
    private int resetX, resetY;
    private int claimButtonX, claimButtonY;//TODO Rename to claim
    private int kingTitleX, kingTitleY;
    private int capeHolderX, capeHolderY;
    private int claimFieldX, claimFieldY;
    private int minimumClaimX, minimumClaimY;
    private int claimFieldWidth = 100;
    private int claimButtonWidth = 40;

    public MemberMiscScreen(MemberScreen parent, Text title) {
        super(parent, title);
    }

    @Override
    public void init() {
        super.init();

        baseX = parent.x + TILE_SIZE * 12;
        baseY = parent.y + TILE_SIZE * 2;

        resetButtonX = parent.x + TEXTURE.textureWidth - TILE_SIZE * 26 - BUTTON_WIDTH;
        resetButtonY = parent.y + TEXTURE.textureHeight - TILE_SIZE * 2 - BUTTON_HEIGHT - 2;

        resetCostX = resetButtonX + BUTTON_WIDTH / 2;
        resetCostY = resetButtonY - TEXT_HEIGHT;

        resetX = resetCostX;
        resetY = resetCostY - TEXT_HEIGHT * 3;

        claimFieldX = baseX;
        claimFieldY = parent.y + TEXTURE.textureHeight - BUTTON_HEIGHT - 2 * TILE_SIZE - 2;

        claimButtonX = claimFieldX + claimFieldWidth - claimButtonWidth;
        claimButtonY = claimFieldY;


        minimumClaimX = claimFieldX + claimFieldWidth / 2;
        minimumClaimY = claimButtonY - TEXT_HEIGHT;

        capeHolderX = minimumClaimX;
        capeHolderY = minimumClaimY - 2 * TEXT_HEIGHT;

        kingTitleX = capeHolderX;
        kingTitleY = capeHolderY - TEXT_HEIGHT;


        resetPerksButton = new DemandingButtonWidget(resetButtonX, resetButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal("Reset"), button -> {
            if (parent.getScreenHandler().getCard().canResetPerks()) {
                ClientPacketSender.sendResetPerk();
                button.active = false;
            }
        }, () -> parent.getScreenHandler().getCard().canResetPerks());

        claimField = new ClampedFieldWidget(textRenderer, claimFieldX, claimFieldY, claimFieldWidth, BUTTON_HEIGHT, Text.literal(""), parent);
        claimField.setMaxLength(10);
        claimField.setText("0");

        submitClaimButton = new DemandingButtonWidget(claimButtonX, claimButtonY, claimButtonWidth, BUTTON_HEIGHT, Text.literal("Claim"), button -> {
            int claimAmount = claimField.getValidAmount();
            if (claimAmount >= parent.getScreenHandler().getMinCapePrice()) {
                ClientPlayNetworking.send(new TitleClaimPayload(claimAmount));
            }
        }, () -> claimField.getValidAmount() >= parent.getScreenHandler().getMinCapePrice() &&
                parent.getScreenHandler().getCard().getCredit() > parent.getScreenHandler().getMinCapePrice());
        components.add(resetPerksButton);
        components.add(claimField);
        components.add(submitClaimButton);

    }

    @Override
    public void handledScreenTick() {
        int resetCost = parent.getScreenHandler().getCard().getResetCost();
        resetCostString = resetCost == 0 ? "Free (Once)" : (resetCost + "$");
        resetCostText = Text.literal("Cost: " + resetCostString);

        minClaimString = String.valueOf(parent.getScreenHandler().getMinCapePrice());
        minClaimText = Text.literal("Min: " + minClaimString + "$");
        capeHolderText = Text.literal(parent.getScreenHandler().getCapeHolder());


        Optional.ofNullable(resetPerksButton).ifPresent(DemandingButtonWidget::tick);
        Optional.ofNullable(submitClaimButton).ifPresent(DemandingButtonWidget::tick);
    }


    @Override
    public int unlockLevel() {
        return 5;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        parent.drawContainerBox(context, claimFieldX - 1, kingTitleY - 2, claimFieldX + claimFieldWidth + 1, claimButtonY + BUTTON_HEIGHT + 1, true);
        parent.drawContainerBox(context, resetButtonX - 1, resetY - 2, resetButtonX + BUTTON_WIDTH + 1, resetButtonY + BUTTON_HEIGHT + 1, true);
        drawTextCenteredAt(textRenderer, context, resetText, resetX, resetY, BEIGE_TEXT_COLOR);
        drawTextCenteredAt(textRenderer, context, resetCostText, resetCostX, resetCostY, BEIGE_TEXT_COLOR);
        drawTextCenteredAt(textRenderer, context, minClaimText, minimumClaimX, minimumClaimY, BEIGE_TEXT_COLOR);
        drawOutlinedTextCenteredAt(textRenderer, context, capeHolderText, capeHolderX, capeHolderY, CREDIT_COLOR, CREDIT_OUTLINE_COLOR);
        drawTextCenteredAt(textRenderer, context, fishingKingText, kingTitleX, kingTitleY, BEIGE_TEXT_COLOR);

        super.render(context, mouseX, mouseY, delta);
    }
}

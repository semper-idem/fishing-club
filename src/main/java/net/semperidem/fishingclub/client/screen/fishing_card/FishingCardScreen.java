package net.semperidem.fishingclub.client.screen.fishing_card;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.CacheAwareButtonWidget;
import net.semperidem.fishingclub.client.screen.Cacheable;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.Path;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;

import java.awt.*;
import java.util.ArrayList;

public class FishingCardScreen extends HandledScreen<FishingCardScreenHandler> implements ScreenHandlerProvider<FishingCardScreenHandler>, Cacheable {
    private static final Identifier BACKGROUND_GENERAL_TAB = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info_inv.png");
    private static final Identifier BACKGROUND_PERK_TAB = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info.png");

    private static final int TEXT_COLOR = Color.WHITE.getRGB();

    private static final int BACKGROUND_TEXTURE_WIDTH = 384;
    private static final int BACKGROUND_TEXTURE_HEIGHT = 264;
    private static final int TAB_WIDTH = 69;
    private static final int TAB_HEIGHT = 20;
    private static final int BANNER_WIDTH = 96;
    private static final int BORDER_THICKNESS = 6;
    private static final int TABS_WIDTH = 4 * TAB_WIDTH;

    private static final String sFishingCard = "Fishing Card:";
    private static final String sUnlock = "Unlock";
    private static final String sPerkPoint = "Perk points:";
    private static final String sLevel = "Lvl.";
    private static final String sCredit = "$";
    private static final String sExp = "Exp.";
    private static final String sDiv = "_____________";
    private static final String labelFormatter = "§6§l";

    private String name;
    private String level;
    private String exp;
    private String credit;
    private String perkPoints;
    private boolean hasPerkPoints;

    private Identifier backgroundTexture = BACKGROUND_GENERAL_TAB;

    private FishingPerk selectedPerk;
    private PerkButtonWidget selectedPerkButton;

    private ButtonWidget generalButton;
    private ButtonWidget hobbyistButton;
    private ButtonWidget opportunistButton;
    private ButtonWidget socialistButton;
    private ButtonWidget activeTabButton;
    private ButtonWidget unlockButton;
    public ButtonWidget instantSellSlotButton;
    private final ArrayList<PerkButtonWidget> perkButtons = new ArrayList<>();

    private int buttonsX;
    private int buttonsY;
    private int nextButtonX;

    private int perksX;
    private int perksY;

    public FishingCardScreen(FishingCardScreenHandler fishingCardScreenHandler, PlayerInventory playerInventory, Text text) {
        super(fishingCardScreenHandler, playerInventory, text);
        updateCache();
    }

    public void updateCache(){
        name = MinecraftClient.getInstance().player.getName().getString();
        level = String.valueOf(handler.fishingCard.getLevel());
        exp = handler.fishingCard.getExp() + "/" + handler.fishingCard.nextLevelXP();
        credit = String.valueOf(handler.fishingCard.getCredit());
        hasPerkPoints = handler.fishingCard.hasPerkPoints();
        perkPoints = String.valueOf(handler.fishingCard.getPerkPoints());
    }

    @Override
    protected void init() {
        x = (width - BACKGROUND_TEXTURE_WIDTH) / 2;
        y = (height - BACKGROUND_TEXTURE_HEIGHT) / 2;
        backgroundWidth = BACKGROUND_TEXTURE_WIDTH;
        backgroundHeight = BACKGROUND_TEXTURE_HEIGHT;

        buttonsX = x + BANNER_WIDTH + (BACKGROUND_TEXTURE_WIDTH - BANNER_WIDTH - TABS_WIDTH - BORDER_THICKNESS) / 2;
        buttonsY = y + BORDER_THICKNESS + 1;
        nextButtonX = buttonsX;

        perksX = x + BANNER_WIDTH + BORDER_THICKNESS + 4;
        perksY = buttonsY + TAB_HEIGHT + 6;

        initButtons();
        generalButton.onPress();
    }

    private void initButtons(){
        initTabButton();
        initUnlockButton();
        initSellButton();
        addButtons();
    }

    private void addButtons(){
        addDrawableChild(generalButton);
        addDrawableChild(hobbyistButton);
        addDrawableChild(opportunistButton);
        addDrawableChild(socialistButton);
        addDrawableChild(unlockButton);
        addDrawableChild(instantSellSlotButton);
    }


    private void initTabButton(){
        generalButton = getButtonForTab(Path.GENERAL);
        hobbyistButton = getButtonForTab(Path.HOBBYIST);
        opportunistButton = getButtonForTab(Path.OPPORTUNIST);
        socialistButton = getButtonForTab(Path.SOCIALIST);
        setupPerkButtons();
    }


    private void initSellButton() {
        instantSellSlotButton = new InstantSellButtonWidget(
                x + 343,
                y + 224,
                25,
                TAB_HEIGHT,
                handler,
                this
        );
    }

    private void initUnlockButton(){
        unlockButton = new CacheAwareButtonWidget(
                x + BACKGROUND_TEXTURE_WIDTH - 62  ,
                y + BACKGROUND_TEXTURE_HEIGHT - 114,
                50,
                TAB_HEIGHT,
                Text.of(sUnlock),
                unlockButtonAction(),
                this
        );
        unlockButton.visible = false;
    }

    private ButtonWidget getButtonForTab(Path tab) {
        ButtonWidget tabButton = new ButtonWidget(
                nextButtonX,
                buttonsY,
                TAB_WIDTH,
                TAB_HEIGHT,
                Text.of(tab.name),
                tabButtonAction(tab)
        );
        nextButtonX += TAB_WIDTH;
        return tabButton;
    }

    private ButtonWidget.PressAction tabButtonAction(Path tab) {
        return button -> setTab(button, tab);
    }



    private ButtonWidget.PressAction unlockButtonAction(){
        return button -> {
            handler.fishingCard.addPerk(selectedPerk.getName());
            ClientPacketSender.unlockPerk(selectedPerk.getName());
            unlockButton.visible = false;
        };
    }

    private void setTab(ButtonWidget button, Path tab){
        backgroundTexture = tab == Path.GENERAL ? BACKGROUND_GENERAL_TAB : BACKGROUND_PERK_TAB;
        if (activeTabButton != null) {
            activeTabButton.active = true;
        }
        activeTabButton = button;
        button.active = false;
        handler.setActiveTab(tab);
        togglePerkButtons(tab);
    }

    private void togglePerkButtons(Path tab) {
        for(PerkButtonWidget perkButton : perkButtons) {
            perkButton.visible = perkButton.fishingPerk.getPath() == tab;
        }
    }

    protected void setPerk(PerkButtonWidget newSelectedPerkButton, FishingPerk perk) {
        selectedPerk = perk;
        if (selectedPerkButton != null) {
            selectedPerkButton.isSelected = false;
        }
        newSelectedPerkButton.isSelected = true;
        selectedPerkButton = newSelectedPerkButton;
        unlockButton.visible = handler.fishingCard.canUnlockPerk(perk);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, backgroundTexture);
        drawTexture(
                matrices,
                x,
                y,
                0,
                0,
                BACKGROUND_TEXTURE_WIDTH,
                BACKGROUND_TEXTURE_HEIGHT,
                BACKGROUND_TEXTURE_WIDTH,
                BACKGROUND_TEXTURE_HEIGHT)
        ;
    }

    private void renderInfoLine(MatrixStack matrices, int x, int y, String left, String right, int lineWidth){
        textRenderer.drawWithShadow(matrices,left, x, y, TEXT_COLOR);
        int rightLength = textRenderer.getWidth(right);
        int rightX = x + lineWidth - rightLength;
        textRenderer.drawWithShadow(matrices,right, rightX, y, TEXT_COLOR);

    }

    public void renderFisherInfo(MatrixStack matrices){
        int dividerWidth = textRenderer.getWidth(sDiv);
        int infoLineX = x + (BANNER_WIDTH - dividerWidth) / 2;
        int infoLineY = y + 8;
        textRenderer.drawWithShadow(matrices, sFishingCard, infoLineX, infoLineY, TEXT_COLOR);
        renderInfoLine(matrices, infoLineX, infoLineY + 11, "", name, dividerWidth);
        textRenderer.drawWithShadow(matrices,sDiv, infoLineX  , infoLineY + 14 , TEXT_COLOR);
        renderInfoLine(matrices, infoLineX, infoLineY + 26, sLevel, level, dividerWidth);
        renderInfoLine(matrices, infoLineX, infoLineY + 37, sExp, exp, dividerWidth);
        renderInfoLine(matrices, infoLineX, infoLineY + 48, sCredit, credit, dividerWidth);
        if (hasPerkPoints) {
            renderInfoLine(matrices, infoLineX, infoLineY + 59, sPerkPoint, perkPoints, dividerWidth);
        }

    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderFisherInfo(matrices);
        renderSelectedPerkDescription(matrices);
        renderTooltip(matrices, mouseX, mouseY);
    }


    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        renderBackground(matrices);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
    }

    private void renderTooltip(MatrixStack matrices, int mouseX, int mouseY){
        if (handler.getCursorStack().isEmpty() && focusedSlot != null && focusedSlot.hasStack()) {
            super.renderTooltip(matrices, focusedSlot.getStack(), mouseX, mouseY);
        }
        hoveredElement(mouseX, mouseY).ifPresent(hovered -> {
            if (!(hovered instanceof PerkButtonWidget)) return;
            FishingPerk fishingPerk = ((PerkButtonWidget) hovered).fishingPerk;
            ArrayList<Text> lines = new ArrayList<>();
            lines.add(Text.of(labelFormatter + fishingPerk.getLabel()));//Optimize Later TODO
            lines.addAll(fishingPerk.getDescription());
            renderTooltip(matrices, lines, mouseX, mouseY);

        });
    }

    private void renderSelectedPerkDescription(MatrixStack matrices){
        if (selectedPerk == null || activeTabButton == generalButton) {
            return;
        }

        int selectedPerkNameY = y + BACKGROUND_TEXTURE_HEIGHT - 107;

        textRenderer.drawWithShadow(
                matrices,
                labelFormatter + selectedPerk.getLabel(),
                buttonsX,
                selectedPerkNameY,
                TEXT_COLOR
        );

        ArrayList<Text> lines = selectedPerk.getDetailedDescription();
        int offset = 0;
        for(Text line : lines) {
            textRenderer.drawWithShadow(matrices, line, buttonsX, selectedPerkNameY + 20  + offset, TEXT_COLOR);
            offset += textRenderer.fontHeight + 2;
        }
    }

    private void setupPerkButtons(){
        for(Path path : FishingPerks.SKILL_TREE.keySet()) {
            int treeHeight = 0;
            for(FishingPerk perk : FishingPerks.SKILL_TREE.get(path)) {
                setupPerkButton(perk, treeHeight);
                treeHeight = treeHeight + 24;
            }
        }
    }

    private void setupPerkButton(FishingPerk fishingPerk, int perkY) {
        FishingPerk perkToAdd = fishingPerk;
        int perkX = 0;
        while(perkToAdd != null) {
            PerkButtonWidget perkButtonWidget = new PerkButtonWidget(
                    perksX + perkX,
                    perksY + perkY,
                    handler.fishingCard,
                    perkToAdd,
                    this
            );
            perkButtons.add(perkButtonWidget);
            addDrawableChild(perkButtonWidget);
            perkToAdd = perkToAdd.getChild();
            perkX += 26;
        }
    }



    @Override
    public boolean shouldPause() {
        return false;
    }

}
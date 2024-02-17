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
import net.semperidem.fishingclub.client.screen.CacheUpdatingButton;
import net.semperidem.fishingclub.client.screen.Cacheable;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.Path;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.screen.FishingCardScreenHandler;

import java.awt.*;
import java.util.ArrayList;

public class FishingCardScreen extends HandledScreen<FishingCardScreenHandler> implements ScreenHandlerProvider<FishingCardScreenHandler>, Cacheable {
    private static final Identifier BACKGROUND_INV = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info_inv.png");
    private static final Identifier BACKGROUND_SKILL = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info.png");
    private static Identifier BACKGROUND = BACKGROUND_INV;

    private static final int TEXT_COLOR = Color.WHITE.getRGB();

    private static final int BACKGROUND_TEXTURE_WIDTH = 384;
    private static final int BACKGROUND_TEXTURE_HEIGHT = 264;
    private static final int BUTTON_WIDTH = 69;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BANNER_WIDTH = 96;
    private static final int BORDER_THICKNESS = 6;
    private static final int BUTTON_GAP = 0;
    private static final int BUTTONS_WIDTH = 276;

    private static final String sFishingCard = "Fishing Card:";
    private static final String sUnlock = "Unlock";
    private static final String sSkillPoint = "Skill points:";
    private static final String sLevel = "Lvl.";
    private static final String sCredit = "$";
    private static final String sExp = "Exp.";
    private static final String sDiv = "_____________";
    private static final String labelFormatter = "§6§l";

    private String name;
    private String level;
    private String exp;
    private String credit;
    private String skillPoints;
    private boolean hasSkillPoints;
    private final ArrayList<PerkButtonWidget> perkButtons = new ArrayList<>();


    FishingPerk selectedPerk;
    PerkButtonWidget selectedPerkButton;

    private ButtonWidget infoButton;
    private ButtonWidget hButton;
    private ButtonWidget oButton;
    private ButtonWidget sButton;
    private ButtonWidget activeTabButton;
    ButtonWidget unlockButton;
    public ButtonWidget sellButton;


    private int buttonsX;
    private int buttonsY;
    private int lastButtonX;

    private int perksX;
    private int perksY;



    public FishingCardScreen(FishingCardScreenHandler fishingCardScreenHandler, PlayerInventory playerInventory, Text text) {
        super(fishingCardScreenHandler, playerInventory, text);
        updateCache();
    }

    public void updateCache(){
        this.name = MinecraftClient.getInstance().player.getName().getString();
        this.level = String.valueOf(this.handler.fishingCard.getLevel());
        this.exp = this.handler.fishingCard.getExp() + "/" + this.handler.fishingCard.nextLevelXP();
        this.credit = String.valueOf(this.handler.fishingCard.getCredit());
        this.hasSkillPoints = this.handler.fishingCard.hasSkillPoints();
        this.skillPoints = String.valueOf(this.handler.fishingCard.getSkillPoints());
    }

    @Override
    protected void init() {
        this.x = (width - BACKGROUND_TEXTURE_WIDTH) / 2;
        this.y = (height - BACKGROUND_TEXTURE_HEIGHT) / 2;
        this.backgroundWidth = BACKGROUND_TEXTURE_WIDTH;
        this.backgroundHeight = BACKGROUND_TEXTURE_HEIGHT;

        this.buttonsX = x + BANNER_WIDTH + (BACKGROUND_TEXTURE_WIDTH - BANNER_WIDTH - BUTTONS_WIDTH - BORDER_THICKNESS) / 2;
        this.buttonsY = y + BORDER_THICKNESS + 1;
        this.lastButtonX = buttonsX;

        this.perksX = x + BANNER_WIDTH + BORDER_THICKNESS + 4;
        this.perksY = buttonsY + BUTTON_HEIGHT + 6;

        initButtons();
        infoButton.onPress();
    }




    private void initButtons(){
        initTabButton();
        initUnlockButton();
        initSellButton();
        addButtons();
    }

    private void addButtons(){
        addDrawableChild(infoButton);
        addDrawableChild(hButton);
        addDrawableChild(oButton);
        addDrawableChild(sButton);
        addDrawableChild(unlockButton);
        addDrawableChild(sellButton);
    }


    private void initTabButton(){
        infoButton = getButtonForTab(Path.GENERAL);
        hButton = getButtonForTab(Path.HOBBYIST);
        oButton = getButtonForTab(Path.OPPORTUNIST);
        sButton = getButtonForTab(Path.SOCIALIST);
        setupPerkButtons();
    }


    private void initSellButton() {
        sellButton = new InstantSellButton(
                x + 343,
                y + 224,
                25,
                BUTTON_HEIGHT,
                handler
        );
    }

    private void initUnlockButton(){
        unlockButton = new CacheUpdatingButton(
                x + BACKGROUND_TEXTURE_WIDTH - 62  ,
                y + BACKGROUND_TEXTURE_HEIGHT - 114,
                50,
                BUTTON_HEIGHT,
                Text.of(sUnlock),
                unlockButtonAction()
        );
        unlockButton.visible = false;
    }

    private ButtonWidget getButtonForTab(Path tab) {
        ButtonWidget tabButton = new ButtonWidget(
                lastButtonX,
                buttonsY,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                Text.of(tab.name),
                tabButtonAction(tab)
        );
        lastButtonX += BUTTON_WIDTH + BUTTON_GAP;
        return tabButton;
    }

    private ButtonWidget.PressAction tabButtonAction(Path tab) {
        return button -> setTab(button, tab);
    }



    private ButtonWidget.PressAction unlockButtonAction(){
        return button -> {
            this.handler.fishingCard.addPerk(selectedPerk.getName());
            ClientPacketSender.unlockPerk(selectedPerk.getName());
            unlockButton.visible = false;
        };
    }

    private void setTab(ButtonWidget button, Path tab){
        BACKGROUND = tab == Path.GENERAL ? BACKGROUND_INV : BACKGROUND_SKILL;
        if (activeTabButton != null) {
            activeTabButton.active = true;
        }
        activeTabButton = button;
        button.active = false;
        handler.setActiveTab(tab);
        selectedPerk = null;
        if (selectedPerkButton != null) {
            selectedPerkButton.isSelected = false;
        }
        togglePerkButtons(tab);
    }

    private void togglePerkButtons(Path tab) {
        for(PerkButtonWidget perkButton : perkButtons) {
            perkButton.visible = perkButton.fishingPerk.getPath() == tab;
        }
    }
     protected void setPerk(PerkButtonWidget newSelectedPerkButton, FishingPerk perk) {
        this.selectedPerk = perk;
        if (this.selectedPerkButton != null) {
            this.selectedPerkButton.isSelected = false;
        }
        newSelectedPerkButton.isSelected = true;
        this.selectedPerkButton = newSelectedPerkButton;
        this.unlockButton.visible = handler.fishingCard.canUnlockPerk(perk);
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, BACKGROUND);
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
        if (hasSkillPoints) {
            renderInfoLine(matrices, infoLineX, infoLineY + 59, sSkillPoint, skillPoints, dividerWidth);
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
        if (this.handler.getCursorStack().isEmpty() && this.focusedSlot != null && this.focusedSlot.hasStack()) {
            super.renderTooltip(matrices, this.focusedSlot.getStack(), mouseX, mouseY);
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
        if (selectedPerk == null) {
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
                setupPerkButton(perk, 0, treeHeight);
                treeHeight = treeHeight + 24;
            }
        }
    }

    private void setupPerkButton(FishingPerk fishingPerk, int perkX, int perkY) {
        FishingPerk perkToAdd = fishingPerk;
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
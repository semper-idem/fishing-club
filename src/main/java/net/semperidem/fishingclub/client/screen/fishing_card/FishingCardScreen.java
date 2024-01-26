package net.semperidem.fishingclub.client.screen.fishing_card;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.FishingClubClient;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.awt.*;
import java.util.ArrayList;

public class FishingCardScreen extends HandledScreen<FishingCardScreenHandler> implements ScreenHandlerProvider<FishingCardScreenHandler> {
    private static final Identifier BACKGROUND_INV = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info_inv.png");
    private static final Identifier BACKGROUND_SKILL = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info.png");
    private static final Identifier SKILL_ICON = new Identifier(FishingClub.MOD_ID, "textures/gui/skill_icon_placeholder.png");
    private static final Identifier SELECTED_SKILL_BORDER = new Identifier(FishingClub.MOD_ID, "textures/gui/skill/selected_skill.png");
    private static Identifier BACKGROUND;

    private static final int TEXT_COLOR = Color.WHITE.getRGB();

    private static final int BACKGROUND_TEXTURE_WIDTH = 384;
    private static final int BACKGROUND_TEXTURE_HEIGHT = 264;
    private static final int BUTTON_WIDTH = 72;
    private static final int INFO_BUTTON_WIDTH = 40;
    private static final int BUTTON_HEIGHT = 20;
    private static final int BANNER_WIDTH = 96;
    private static final int BORDER_THICKNESS = 6;
    private static final int BUTTON_GAP = 2;
    private static final int BUTTONS_WIDTH = 262;
    private static final int ICON_SIZE = 20;

    private static final String sFishingCard = "Fishing Card:";
    private static final String sInfo = "Info";
    private static final String sUnlock = "Unlock";
    private static final String sSkillPoint = "Skill points:";
    private static final String sLevel = "Lvl.";
    private static final String sCredit = "$";
    private static final String sExp = "Exp.";
    private static final String sDiv = "_____________";
    private static final String sSell = "Sell";
    private static final String labelFormatter = "§6§l";

    private String name;
    private String level;
    private String exp;
    private String credit;
    private String skillPoints;
    private boolean hasSkillPoints;

    private FishingPerk selectedPerk;

    private int selectedPerkX;
    private int selectedPerkY;

    private ButtonWidget infoButton;
    private ButtonWidget hButton;
    private ButtonWidget oButton;
    private ButtonWidget sButton;
    private ButtonWidget unlockButton;
    private ButtonWidget sellButton;


    private int buttonsX;
    private int buttonsY;
    private int lastButtonX;

    private int perksX;
    private int perksY;


    ArrayList<PerkButtonWidget> perkButtonWidgets = new ArrayList<>();

    public FishingCardScreen(FishingCardScreenHandler fishingCardScreenHandler, PlayerInventory playerInventory, Text text) {
        super(fishingCardScreenHandler, playerInventory, text);
        this.client = MinecraftClient.getInstance();
        updateData();
        addButtons();
        BACKGROUND = BACKGROUND_INV;
    }

    public void updateData(){
        this.name = this.client.player.getName().getString();
        this.level = String.valueOf(FishingClubClient.getClientCard().getLevel());
        this.exp = FishingClubClient.getClientCard().getExp() + "/" + FishingClubClient.getClientCard().nextLevelXP();
        this.credit = String.valueOf(FishingClubClient.getClientCard().getCredit());
        this.hasSkillPoints = FishingClubClient.getClientCard().hasSkillPoints();
        this.skillPoints = String.valueOf(FishingClubClient.getClientCard().getSkillPoints());
    }

    public static void openScreen(PlayerEntity player){
        if(player.world != null && !player.world.isClient) {
            player.openHandledScreen(new FishingCardScreenFactory());
        }
    }

    @Override
    protected void init() {
        this.x = (width - BACKGROUND_TEXTURE_WIDTH - BANNER_WIDTH) / 2;
        this.y = (height - BACKGROUND_TEXTURE_HEIGHT) / 2;
        this.backgroundWidth = BACKGROUND_TEXTURE_WIDTH;
        this.backgroundHeight = BACKGROUND_TEXTURE_HEIGHT;

        this.buttonsX = x + BANNER_WIDTH + (BACKGROUND_TEXTURE_WIDTH - BANNER_WIDTH - BUTTONS_WIDTH - BORDER_THICKNESS) / 2;
        this.buttonsY = y + BORDER_THICKNESS + 1;
        this.lastButtonX = buttonsX;

        this.perksX = buttonsX;
        this.perksY = buttonsY + BUTTON_HEIGHT + 6;

        initButtons();
    }

    private void initButtons(){
        initInfoButton();
        initSkillButtons();
        initUnlockButton();
        initSellButton();
        resetButtonState();
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

    private void resetButtonState(){
        infoButton.active = false;
        unlockButton.visible = false;
        selectedPerk = null;
        this.handler.rootPerk = null;
        clearPerkButtons();
    }

    private void initInfoButton(){
        infoButton = new ButtonWidget(
                lastButtonX,
                buttonsY,
                INFO_BUTTON_WIDTH,
                BUTTON_HEIGHT,
                Text.of(sInfo),
                infoButtonAction()
        );
        lastButtonX += INFO_BUTTON_WIDTH + BUTTON_GAP;
    }

    private ButtonWidget.PressAction infoButtonAction(){
        return button -> {
            resetButtonState();
            lockClicked(button);
            BACKGROUND = BACKGROUND_INV;
            this.handler.addPlayerInventorySlots();
            sellButton.visible = true;
        };
    }

    private void initSkillButtons(){
        hButton = getButtonForSkill(FishingPerks.ROOT_HOBBYIST);
        oButton = getButtonForSkill(FishingPerks.ROOT_OPPORTUNIST);
        sButton = getButtonForSkill(FishingPerks.ROOT_SOCIALIST);
    }



    private ButtonWidget getButtonForSkill(FishingPerk fishingPerk) {
        ButtonWidget skillButton = new ButtonWidget(
                lastButtonX,
                buttonsY,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                Text.of(fishingPerk.getLabel()),
                skillButtonAction(fishingPerk)
        );
        lastButtonX += BUTTON_WIDTH + BUTTON_GAP;
        return skillButton;
    }

    private ButtonWidget.PressAction skillButtonAction(FishingPerk fishingPerk) {
        return button -> {
            BACKGROUND = BACKGROUND_SKILL;
            this.handler.removePlayerInventorySlots();
            this.handler.rootPerk = fishingPerk;
            sellButton.visible = false;
            lockClicked(button);
            addPerks();
        };
    }

    private void initUnlockButton(){
        unlockButton = new ButtonWidget(
                x + BACKGROUND_TEXTURE_WIDTH - 62  ,
                y + BACKGROUND_TEXTURE_HEIGHT - 114,
                50,
                BUTTON_HEIGHT,
                Text.of(sUnlock),
                unlockButtonAction()
        );
    }

    private ButtonWidget.PressAction unlockButtonAction(){
        return button -> {
            if (!FishingClubClient.getClientCard().availablePerk(selectedPerk) || FishingClubClient.getClientCard().hasPerk(selectedPerk)) return;
            FishingClubClient.getClientCard().addPerk(selectedPerk.getName());
            
            ClientPacketSender.unlockPerk(selectedPerk.getName());
            unlockButton.visible = false;
        };
    }

    private void initSellButton(){
        sellButton = new ButtonWidget(
                x + 343  ,
                y + 224,
                25,
                BUTTON_HEIGHT,
                Text.of(sSell),
                sellButtonAction()
        ){
            @Override
            public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
                if (!handler.sellSlot.hasStack()) return;
                super.renderButton(matrices, mouseX, mouseY, delta);
            }
        };

        if (!FishingClubClient.getClientCard().hasPerk(FishingPerks.INSTANT_FISH_CREDIT)) {
            sellButton.visible = false;
            sellButton.active = false;
        }
    }

    private ButtonWidget.PressAction sellButtonAction(){
        return button -> this.handler.sellSlot(this);
    }


    private void lockClicked(ButtonWidget button){
        this.children().forEach(child -> {
            if (child instanceof ButtonWidget) {
                ((ButtonWidget)child).active = true;
            }
        });
        button.active = false;
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
                BACKGROUND_TEXTURE_HEIGHT
        );
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
        if (FishingClubClient.getClientCard().hasSkillPoints()) {
            renderInfoLine(matrices, infoLineX, infoLineY + 59, sSkillPoint, String.valueOf(FishingClubClient.getClientCard().getSkillPoints()), dividerWidth);
        }

    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        renderFisherInfo(matrices);
        renderSelectedPerk(matrices);
        renderTooltip(matrices, mouseX, mouseY);
        renderSlotDisabled(matrices);
    }


    private void renderSlotDisabled(MatrixStack matrices){
        if (this.handler.rootPerk != null) return;
        for(Slot slot : this.handler.slots) {
            if (!(slot instanceof FishingCardScreenHandler.FisherSlot)) continue;
            if (slot.isEnabled()) continue;;
            fill(matrices, x + slot.x, y + slot.y, x + slot.x + 16, y + slot.y + 16, 0x55000001);
        }
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

    private void renderSelectedPerk(MatrixStack matrices){
        if (selectedPerk == null) return;
        int selectedPerkNameY = y + BACKGROUND_TEXTURE_HEIGHT - 107   ;
        textRenderer.drawWithShadow(matrices, labelFormatter + selectedPerk.getLabel(), buttonsX, selectedPerkNameY, TEXT_COLOR);

        ArrayList<Text> lines = selectedPerk.getDetailedDescription();
        int offset = 0;
        for(Text line : lines) {
            textRenderer.drawWithShadow(matrices, line, buttonsX, selectedPerkNameY + 20  + offset, TEXT_COLOR);
            offset += textRenderer.fontHeight + 2;
        }

        RenderSystem.setShaderTexture(0, SELECTED_SKILL_BORDER);
        drawTexture(matrices, this.selectedPerkX - 1, this.selectedPerkY - 1, 0, 0, 22, 22, 22, 22);
    }

    private void clearPerkButtons(){
        unlockButton.visible = false;
        selectedPerk = null;
        for(PerkButtonWidget perkButtonWidget : perkButtonWidgets) {
            remove(perkButtonWidget);
        }
    }

    private void addPerks(){
        clearPerkButtons();
        if (this.handler.rootPerk == null || !this.handler.rootPerk.hasChildren()) {
            return;
        }

        ArrayList<FishingPerk> children = this.handler.rootPerk.getChildren();
        int childY = 0;
        for(FishingPerk child : children) {
            addPerk(child, perksX, perksY + childY);
            childY += 24;
        }
    }

    private void addPerk(FishingPerk fishingPerk, int perkX, int perkY){
        addDrawableChild(new PerkButtonWidget(perkX, perkY, ICON_SIZE, ICON_SIZE, Text.empty(), onPerkClick(fishingPerk), fishingPerk));
        if (!fishingPerk.hasChildren()) return;
        addPerk(fishingPerk.getChildren().get(0), perkX + 30, perkY);
    }

    private ButtonWidget.PressAction onPerkClick(FishingPerk fishingPerk){
        return button -> {
            selectedPerk = fishingPerk;
            selectedPerkX = button.x;
            selectedPerkY = button.y;
            unlockButton.visible = FishingClubClient.getClientCard().availablePerk(fishingPerk) && !FishingClubClient.getClientCard().hasPerk(fishingPerk) && FishingClubClient.getClientCard().hasSkillPoints();
            updateData();
        };
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    class PerkButtonWidget extends ButtonWidget {
        FishingPerk fishingPerk;
        public PerkButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, FishingPerk fishingPerk) {
            super(x, y, width, height, message, onPress);
            this.fishingPerk = fishingPerk;
            perkButtonWidgets.add(this);
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.enableBlend();
            renderBackground(matrices);
            renderLink(matrices);
            renderIcon(matrices);
            RenderSystem.disableBlend();
        }

        private void renderBackground(MatrixStack matrices){
            boolean hasPerk = getScreenHandler().fishingCard.hasPerk(fishingPerk);
            boolean isAvailable = getScreenHandler().fishingCard.availablePerk(fishingPerk);
            if (hasPerk) {
                RenderSystem.setShaderColor(0.5f,1,0.5f,1);
            }
            if (!isAvailable) {
                RenderSystem.setShaderColor(0.6f,0.6f,0.6f,0.6f);
            }
            RenderSystem.setShaderTexture(0, SKILL_ICON);
            drawTexture(matrices, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
        }

        private void renderLink(MatrixStack matrices){
            if (fishingPerk.parentIsRoot()) {
                fill(matrices, x, y + 9, x - 10, y + 11, TEXT_COLOR);
            }
        }

        private void renderIcon(MatrixStack matrices){
            RenderSystem.setShaderColor(1,1,1,1);
            Identifier icon = fishingPerk.getIcon();
            if (icon != null) {
                RenderSystem.setShaderTexture(0, fishingPerk.getIcon());
                drawTexture(matrices, x, y, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
            }
        }
    }
}
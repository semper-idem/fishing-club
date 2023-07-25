package net.semperidem.fishingclub.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfos;
import net.semperidem.fishingclub.fisher.FishingPerk;
import net.semperidem.fishingclub.fisher.FishingPerks;
import net.semperidem.fishingclub.network.ClientPacketSender;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;

public class FisherInfoScreen extends HandledScreen<FisherInfoScreenHandler> implements ScreenHandlerProvider<FisherInfoScreenHandler> {
    private static final Identifier BACKGROUND_INV = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info_inv.png");
    private static final Identifier BACKGROUND_SKILL = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info.png");
    private static Identifier BACKGROUND = BACKGROUND_INV;
    private static final Identifier SKILL_ICON = new Identifier(FishingClub.MOD_ID, "textures/gui/skill_icon_placeholder.png");
    int backgroundTextureWidth = 384;
    int backgroundTextureHeight = 264;

    int x, y;

    private static final int TEXT_COLOR = Color.WHITE.getRGB();


    private static final String fisherInfoString = "Fisher Info:";

    FisherInfo clientInfo;
    String name;
    String level;
    String exp;
    String credit;
    boolean hasSkillPoints;
    String skillPoints;

    FishingPerk rootPerk;

    FishingPerk selectedPerk;

    ButtonWidget infoButton;
    ButtonWidget hButton;
    ButtonWidget oButton;
    ButtonWidget sButton;
    ButtonWidget unlockButton;


    int buttonWidth;
    int infoButtonWidth;
    int buttonHeight;
    int buttonsX;
    int buttonsY;
    int lastButtonX;

    int bannerWidth = 96;
    int borderThickness = 6;
    int buttonGap = 2;
    int buttonsWidth;


    ArrayList<PerkButtonWidget> perkButtonWidgets = new ArrayList<>();

    public FisherInfoScreen(FisherInfoScreenHandler fisherInfoScreenHandler, PlayerInventory playerInventory, Text text) {
        super(fisherInfoScreenHandler, playerInventory, text);
        this.client = MinecraftClient.getInstance();
        this.clientInfo = FisherInfos.getClientInfo();
        updateData();
        addPageButtons();
    }

    private void updateData(){
        this.name = this.client.player.getName().getString();
        this.level = String.valueOf(clientInfo.getLevel());
        this.exp = clientInfo.getExp() + "/" + clientInfo.nextLevelXP();
        this.credit = String.valueOf(clientInfo.getCredit());
        this.hasSkillPoints = clientInfo.hasSkillPoints();
        this.skillPoints = String.valueOf(clientInfo.getSkillPoints());

    }

    public static void openScreen(PlayerEntity player){
        if(player.world != null && !player.world.isClient) {
            player.openHandledScreen(new NamedScreenHandlerFactory() {

                @Override
                public Text getDisplayName() {
                    return Text.translatable("Fisher Info");
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new FisherInfoScreenHandler(syncId, inv);
                }
            });
        }
    }

    @Override
    protected void init() {

        x = (width - backgroundTextureWidth - bannerWidth) / 2;
        y = (height - backgroundTextureHeight) / 2;


        infoButtonWidth = 40;
        buttonWidth = 72;
        buttonHeight = 20;
        buttonsWidth = infoButtonWidth + buttonWidth * 3 + buttonGap * 3;

        buttonsX = x + bannerWidth + (backgroundTextureWidth - bannerWidth - buttonsWidth) / 2;
        buttonsY = y + borderThickness + 2;
        lastButtonX = buttonsX;

        initInfoButton();
        initSkillButtons();
        initUnlockButton();
        resetButtonState();
        addPageButtons();
    }


    private void resetButtonState(){
        infoButton.active = false;
        unlockButton.visible = false;
        selectedPerk = null;
        rootPerk = null;
        clearPerkButtons();
    }

    private void initInfoButton(){
        String info = "Info";
        infoButton = new ButtonWidget(lastButtonX,buttonsY,infoButtonWidth, buttonHeight, Text.of(info), button -> {
            resetButtonState();
            lockClicked(button);
            BACKGROUND = BACKGROUND_INV;
            this.handler.addPlayerInventorySlots();
        });
        lastButtonX += infoButtonWidth + buttonGap;
    }

    private void initSkillButtons(){
        hButton = getButtonForSkill(FishingPerks.ROOT_HOBBYIST);
        oButton = getButtonForSkill(FishingPerks.ROOT_OPPORTUNIST);
        sButton = getButtonForSkill(FishingPerks.ROOT_SOCIALIST);
    }

    private void initUnlockButton(){
        unlockButton = new ButtonWidget(x + backgroundTextureWidth - 62  ,y + backgroundTextureHeight - 114,50, 20, Text.of("Unlock"), button -> {
            if (clientInfo.availablePerk(selectedPerk) && !clientInfo.hasPerk(selectedPerk)) {
                ClientPacketSender.unlockPerk(selectedPerk.getName());
                unlockButton.visible = false;
                this.clientInfo = FisherInfos.getClientInfo();
            }
        });
    }

    private ButtonWidget getButtonForSkill(FishingPerk fishingPerk) {
        ButtonWidget skillButton = new ButtonWidget(lastButtonX,buttonsY,buttonWidth, buttonHeight, Text.of(fishingPerk.getLabel()), button -> {
            BACKGROUND = BACKGROUND_SKILL;
            this.handler.removePlayerInventorySlots();
            rootPerk = fishingPerk;
            lockClicked(button);
            addPerks();
        });
        lastButtonX += buttonWidth + buttonGap;
        return skillButton;
    }

    private void addPageButtons(){
        addDrawableChild(infoButton);
        addDrawableChild(hButton);
        addDrawableChild(oButton);
        addDrawableChild(sButton);
        addDrawableChild(unlockButton);
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
        drawTexture(matrices, x, y, 0, 0, backgroundTextureWidth, backgroundTextureHeight, backgroundTextureWidth, backgroundTextureHeight);
    }

    private void renderInfoLine(MatrixStack matrices, int x, int y, String left, String right, int lineWidth){
        textRenderer.drawWithShadow(matrices,left, x, y, TEXT_COLOR);
        int rightLength = textRenderer.getWidth(right);
        int rightX = x + lineWidth - rightLength;
        textRenderer.drawWithShadow(matrices,right, rightX, y, TEXT_COLOR);

    }

    public void renderFisherInfo(MatrixStack matrices){
        String divider = "_____________";
        int dividerWidth = textRenderer.getWidth(divider);
        int elementX = x + (bannerWidth - dividerWidth) / 2;
        int elementY = y + 8;
        textRenderer.drawWithShadow(matrices,fisherInfoString, elementX, elementY, TEXT_COLOR);
        renderInfoLine(matrices, elementX, elementY + 11, "", name, dividerWidth);
        textRenderer.drawWithShadow(matrices,divider, elementX  , elementY + 14 , TEXT_COLOR);
        renderInfoLine(matrices, elementX, elementY + 26, "Lvl.", level, dividerWidth);
        renderInfoLine(matrices, elementX, elementY + 37, "Exp:", exp, dividerWidth);
        renderInfoLine(matrices, elementX, elementY + 48, "$", credit, dividerWidth);
        if (hasSkillPoints) {
            renderInfoLine(matrices, elementX, elementY + 59, "Skill points:", skillPoints, dividerWidth);
        }

    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        renderBackground(matrices);
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        renderFisherInfo(matrices);
        renderSelectedPerk(matrices);
        renderTooltip(matrices, mouseX, mouseY);
    }

    private void renderTooltip(MatrixStack matrices, int mouseX, int mouseY){
        hoveredElement(mouseX, mouseY).ifPresent(hovered -> {
            if (hovered instanceof PerkButtonWidget) {
                FishingPerk fishingPerk = ((PerkButtonWidget) hovered).fishingPerk;
                ArrayList<Text> lines = new ArrayList<>();
                lines.add(Text.of("§6§l" + fishingPerk.getLabel()));//Optimize Later TODO
                lines.addAll(fishingPerk.getDescription());
                renderTooltip(matrices, lines, mouseX, mouseY);
            }
        });
    }

    private void renderSelectedPerk(MatrixStack matrices){
        if (selectedPerk == null) return;
        int selectedPerkY = y + backgroundTextureHeight - 107   ;
        textRenderer.drawWithShadow(matrices,selectedPerk.getLabel(), buttonsX, selectedPerkY, TEXT_COLOR);

        ArrayList<Text> lines = selectedPerk.getDetailedDescription();
        int offset = 0;
        for(Text line : lines) {
                textRenderer.drawWithShadow(matrices, line, buttonsX, selectedPerkY + 20  + offset, TEXT_COLOR);
            offset += textRenderer.fontHeight + 2;
        }
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
        if (this.rootPerk == null || !this.rootPerk.hasChildren()) {
            return;
        }

        int perksX = buttonsX;
        int perksY = buttonsY + buttonHeight + 6;

        ArrayList<FishingPerk> children = this.rootPerk.getChildren();
        int childY = 0;
        for(FishingPerk child : children) {
            addPerk(child, perksX, perksY + childY);
            childY += 24;
        }
    }

    private void addPerk(FishingPerk fishingPerk, int perkX, int perkY){
        addDrawableChild(new PerkButtonWidget(perkX, perkY, 20, 20, Text.empty(), button -> {
            selectedPerk = fishingPerk;
            unlockButton.visible = clientInfo.availablePerk(fishingPerk) && !clientInfo.hasPerk(fishingPerk) && clientInfo.hasSkillPoints();
            updateData();
        }, fishingPerk));
        ArrayList<FishingPerk> children = fishingPerk.getChildren();
        for(FishingPerk child : children) {
            addPerk(child, perkX + 30, perkY);
        }
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
            boolean hasPerk = clientInfo.hasPerk(fishingPerk);
            boolean isAvailable = clientInfo.availablePerk(fishingPerk);
            if (hasPerk) {
                RenderSystem.setShaderColor(0.5f,1,0.5f,1);
            }
            if (!isAvailable) {
                RenderSystem.setShaderColor(0.6f,0.6f,0.6f,0.6f);
            }
            RenderSystem.setShaderTexture(0, SKILL_ICON);
            drawTexture(matrices, x, y, 0, 0, 20, 20, 20, 20);
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
                drawTexture(matrices, x, y, 0, 0, 20, 20, 20, 20);
            }
        }
    }
}
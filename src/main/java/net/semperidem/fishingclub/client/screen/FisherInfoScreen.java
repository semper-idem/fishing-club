package net.semperidem.fishingclub.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfos;
import net.semperidem.fishingclub.fisher.FishingPerk;
import net.semperidem.fishingclub.fisher.FishingPerks;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.awt.*;
import java.util.ArrayList;

public class FisherInfoScreen extends Screen {
    //512 x 288
    private static final Identifier BACKGROUND = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info.png");
    private static final Identifier SKILL_ICON = new Identifier(FishingClub.MOD_ID, "textures/gui/skill_icon_placeholder.png");
    int backgroundTextureWidth = 512;
    int backgroundTextureHeight = 288;

    int x, y;
    float scale = .7f; //TODO RENAME TO BACKGROUND SCALE
    int backgroundWidth = backgroundTextureWidth;
    int backgroundHeight = backgroundTextureHeight;

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

    public FisherInfoScreen(Text text) {
        super(text);
        this.client = MinecraftClient.getInstance();
        this.clientInfo = FisherInfos.getClientInfo();
        this.name = this.client.player.getName().getString();
        this.level = String.valueOf(clientInfo.getLevel());
        this.exp = clientInfo.getExp() + "/" + clientInfo.nextLevelXP();
        this.credit = String.valueOf(clientInfo.getFisherCredit());
        hasSkillPoints = clientInfo.getSkillPoints() > 0;
        skillPoints = String.valueOf(clientInfo.getSkillPoints());

    }

    @Override
    protected void init() {
        backgroundWidth = (int) (backgroundTextureWidth * scale);
        backgroundHeight = (int) (backgroundTextureHeight * scale);

        x = (width - backgroundWidth) / 2;
        y = (height - backgroundHeight) / 2;
        //int x, int y, int width, int height, Text message, PressAction onPress
        addPageButtons();

    }

    private void addPageButtons(){
        int buttonWidth = 70;
        int infoButtonWidth = 40;
        int buttonHeight = 20;
        int buttonsX = x + 95;
        int buttonsY = y + 6;

        String info = "Info";
        ButtonWidget infoButton = new ButtonWidget(buttonsX,buttonsY,infoButtonWidth, buttonHeight, Text.of(info), button -> {
            rootPerk = null;
            lockClicked(button);
            addPerks();
        });
        infoButton.active = false;
        addDrawableChild(infoButton);

        addDrawableChild(new ButtonWidget(buttonsX + infoButtonWidth,buttonsY,buttonWidth, buttonHeight, Text.of(FishingPerks.ROOT_HOBBYIST.getLabel()), button -> {
            rootPerk = FishingPerks.ROOT_HOBBYIST;
            lockClicked(button);
            addPerks();
        } ));

        addDrawableChild(new ButtonWidget(buttonsX + buttonWidth  + infoButtonWidth,buttonsY,buttonWidth, buttonHeight, Text.of(FishingPerks.ROOT_OPPORTUNIST.getLabel()), button -> {
            rootPerk = FishingPerks.ROOT_OPPORTUNIST;
            lockClicked(button);
            addPerks();
        }));

        addDrawableChild(new ButtonWidget(buttonsX + buttonWidth * 2  + infoButtonWidth,buttonsY,buttonWidth, buttonHeight, Text.of(FishingPerks.ROOT_SOCIALIST.getLabel()), button -> {
            rootPerk = FishingPerks.ROOT_SOCIALIST;
            lockClicked(button);
            addPerks();
        }));
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
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
    }

    @Override
    public void tick() {
        this.clientInfo = FisherInfos.getClientInfo();
    }

    private void renderInfoLine(MatrixStack matrices, int x, int y, String left, String right){
        textRenderer.drawWithShadow(matrices,left, x, y, TEXT_COLOR);
        int rightLength = textRenderer.getWidth(right);
        int lineWidth = 74;
        int rightX = x + lineWidth - rightLength;
        textRenderer.drawWithShadow(matrices,right, rightX, y, TEXT_COLOR);

    }

    public void renderFisherInfo(MatrixStack matrices){
        int elementX = x + 8;
        int elementY = y + 8;
        textRenderer.drawWithShadow(matrices,fisherInfoString, elementX, elementY, TEXT_COLOR);
        renderInfoLine(matrices, elementX, elementY + 11, "", name);
        textRenderer.drawWithShadow(matrices,"-------------", elementX - 2, elementY + 18, TEXT_COLOR);
        renderInfoLine(matrices, elementX, elementY + 26, "Lvl.", level);
        renderInfoLine(matrices, elementX, elementY + 37, "Exp:", exp);
        renderInfoLine(matrices, elementX, elementY + 48, "$", credit);
        if (hasSkillPoints) {
            renderInfoLine(matrices, elementX, elementY + 59, "Skill points:", skillPoints);
        }

    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        renderFisherInfo(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        hoveredElement(mouseX, mouseY).ifPresent(hovered -> {
            if (hovered instanceof PerkButtonWidget) {
                renderTooltip(matrices, Text.of(((PerkButtonWidget) hovered).fishingPerk.getLabel()), mouseX, mouseY);
            }
        });
    }

    private void addPerks(){
        while (children().size() > 4) {
            remove(children().get(children().size() - 1));
        }
        if (this.rootPerk == null || !this.rootPerk.hasChildren()) {
            return;
        }

        int perksX = x + 100;
        int perksY = y + 30;

        ArrayList<FishingPerk> children = this.rootPerk.getChildren();
        int childY = 0;
        for(FishingPerk child : children) {
            addPerk(child, perksX, perksY + childY);
            childY += 24;
        }
    }

    private void addPerk(FishingPerk fishingPerk, int perkX, int perkY){
        addDrawableChild(new PerkButtonWidget(perkX, perkY, 20, 20, Text.empty(), button -> {
            if (clientInfo.availablePerk(fishingPerk) && !clientInfo.hasPerk(fishingPerk)) {
                ClientPacketSender.unlockPerk(fishingPerk.getName());
            }
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
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            RenderSystem.setShaderTexture(0, SKILL_ICON);
            boolean hasPerk = clientInfo.hasPerk(fishingPerk);
            boolean isAvailable = clientInfo.availablePerk(fishingPerk);
            RenderSystem.enableBlend();
            if (hasPerk) {
                RenderSystem.setShaderColor(0.5f,1,0.5f,1);
            } else if (!isAvailable) {
                RenderSystem.setShaderColor(1,0.9f,0.9f,0.5f);
            }
            drawTexture(matrices, x, y, 0, 0, 20, 20, 20, 20);
            if (fishingPerk.parentIsRoot()) {
                fill(matrices, x, y + 9, x - 10, y + 11, TEXT_COLOR);
            }
            RenderSystem.setShaderColor(1,1,1,1);
            RenderSystem.disableBlend();
        }
    }
}
package net.semperidem.fishingclub.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.fisher.FisherInfos;

import java.awt.*;

public class FisherInfoScreen extends Screen {
    //512 x 288
    private static final Identifier BACKGROUND = new Identifier(FishingClub.MOD_ID, "textures/gui/fisher_info.png");
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
    }

    @Override
    public void renderBackground(MatrixStack matrices) {
        RenderSystem.setShaderTexture(0, BACKGROUND);
        drawTexture(matrices, x, y, 0, 0, backgroundWidth, backgroundHeight, backgroundWidth, backgroundHeight);
    }

    @Override
    public void tick() {

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
    }




    @Override
    public boolean shouldPause() {
        return false;
    }

}
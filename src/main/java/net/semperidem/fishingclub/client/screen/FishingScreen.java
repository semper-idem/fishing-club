package net.semperidem.fishingclub.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.FishGameLogic;

public class FishingScreen extends Screen {
    private static final Identifier BOBBER = new Identifier(FishingClub.MOD_ID, "textures/gui/bobber_8x32.png");
    private static final Identifier FISH = new Identifier(FishingClub.MOD_ID, "textures/gui/fish_icon_8x8.png");
    private static final Identifier BACKGROUND = new Identifier(FishingClub.MOD_ID, "textures/gui/minigame_background_160x160.png");
    private static final Identifier WATER_BAR = new Identifier(FishingClub.MOD_ID, "textures/gui/water_bar_detailed_8x128.png");
    private static final Identifier PROGRESS_BAR = new Identifier(FishingClub.MOD_ID, "textures/gui/progress_bar_8x128.png");
    private static final Identifier BAR_BORDER = new Identifier(FishingClub.MOD_ID, "textures/gui/water_border_14x140.png");

    int windowX = 0;
    int windowY = 0;

    int bgWidth = 160;
    int bgHeight = 160;

    int barWidth = 8;
    int barHeight = 128;

    int barBorderWidth = 14;
    int barBorderHeight = 140;

    int bobberHeight = 32;


    FishGameLogic fishGameLogic;

    public FishingScreen(Text text) {
        super(text);
        this.fishGameLogic = new FishGameLogic(MinecraftClient.getInstance().player);
    }


    @Override
    public void tick() {
        this.fishGameLogic.tick();
        if (this.fishGameLogic.isFinished()) {
            this.close();
            if (this.fishGameLogic.isWon()) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Caught Lvl." + this.fishGameLogic.getLevel() + " " + this.fishGameLogic.getName() + "! Nice"));
                MinecraftClient.getInstance().player.sendMessage(Text.of("Gained " + this.fishGameLogic.getExperience() + "exp"));
            } else {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Fish escaped"));
            }
        }
    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        windowX = (this.width - 160) / 2;
        windowY = (this.height- 160) / 2;

        matrices.push();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        renderWindow(matrices);
        renderBobber(matrices);
        renderFish(matrices);
        renderProgressBar(matrices);
        renderLineHealth(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        matrices.pop();
    }

    private void renderWindow(MatrixStack matrices){
        RenderSystem.setShaderTexture(0, BACKGROUND);
        drawTexture(matrices, windowX, windowY, 0, 0, bgWidth, bgHeight, bgWidth, bgHeight);
    }

    private void renderBobber(MatrixStack matrices){
        matrices.push();
        float bobberPos = fishGameLogic.getBobberPos();
        float bobberLength = fishGameLogic.getBobberLength();
        float bobberScale = barHeight * bobberLength / bobberHeight;
        RenderSystem.setShaderTexture(0, BOBBER);
        matrices.scale(1f,bobberScale,1f);
        int y = (int) ( (windowY + 16 + barHeight - barHeight * bobberPos) * (1f / bobberScale));
        drawTexture(matrices, windowX + 26, y, 0, 0, barWidth, bobberHeight, barWidth, bobberHeight);

        matrices.pop();

    }

    private void renderFish(MatrixStack matrices){
        RenderSystem.setShaderTexture(0, FISH);
        float fishPos = fishGameLogic.getFishPos();
        drawTexture(matrices, windowX + 26, (int) (windowY + 16 + barHeight - barHeight * fishPos), 0, 0, barWidth, barWidth, barWidth, barWidth);

    }


    public void renderProgressBar(MatrixStack matrices ) {
        RenderSystem.setShaderTexture(0, PROGRESS_BAR);
        int progressBarLength = (int) (barHeight * fishGameLogic.getProgress());
        drawTexture(matrices, windowX + 126, windowY+ 16 + barHeight - progressBarLength, 0, 0, barWidth, progressBarLength, barWidth, barHeight);

    }
    public void renderLineHealth(MatrixStack matrices) {
        String healthLabel = "Line Health";
        int healthLabelLength = textRenderer.getWidth(healthLabel);
        textRenderer.drawWithShadow(matrices, Text.of(healthLabel), windowX + 80 - healthLabelLength / 2f,windowY + 75,0xFFFFFF);

        String healthInfo = String.format("%.1f" ,fishGameLogic.getHealth());
        int healthInfoLength = textRenderer.getWidth(healthInfo);
        textRenderer.drawWithShadow(matrices, Text.of(healthInfo), windowX + 80 - healthInfoLength / 2f,windowY + 90,0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}

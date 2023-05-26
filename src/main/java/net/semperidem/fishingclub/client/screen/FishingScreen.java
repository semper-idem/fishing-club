package net.semperidem.fishingclub.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
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
        renderFish(matrices);
//        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Text.of(String.format("%.2f", fishGameLogic.getHealth())), 0,0,0xFFFFFF);
//        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Text.of("Level:" + fishGameLogic.getLevel()), 0,20,0xFFFFFF);
////        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Text.of("bobberPos:" + String.format("%.2f",bobberPos)), 0,40,0xFFFFFF);
////        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Text.of("fishPos:" + String.format("%.2f",fishPos)), 0,60,0xFFFFFF);
//        renderFishWidget(matrices, screenX + 50, screenY + 150);
//        renderProgressWidget(matrices, screenX + 100, screenY + 150);
        super.render(matrices, mouseX, mouseY, delta);
        matrices.pop();
    }

    private void renderWindow(MatrixStack matrices){
        RenderSystem.setShaderTexture(0, BACKGROUND);
        drawTexture(matrices, windowX, windowY, 0, 0, bgWidth, bgHeight, bgWidth, bgHeight);
    }

    private void renderFish(MatrixStack matrices){
        RenderSystem.setShaderTexture(0, FISH);
        float fishPos = fishGameLogic.getFishPos();
        drawTexture(matrices, windowX + 26, (int) (windowY + 16 + barHeight - barHeight * fishPos), 0, 0, barWidth, barWidth, barWidth, barWidth);

    }

    private void renderFishWidget(MatrixStack matrices, int x, int y) {
        //Bar
        DrawableHelper.fill(matrices, x,y,x + 10,y - 100,0xFF282e68);
        //Bobber
        float bobberWidth = fishGameLogic.getBobberLength();
        float bobberPos = fishGameLogic.getBobberPos();
        float fishPos = fishGameLogic.getFishPos();
        DrawableHelper.fill(matrices, x + 1, (int) (y - bobberWidth*100 - (bobberPos * 100)),x + 9, (int) (y + bobberWidth*100 - (bobberPos * 100)),0xFFf49333);

        //Fish
        DrawableHelper.fill(matrices, x + 1, (int) (y - (fishPos * 100)),x + 9, (int) (y - 1 - (fishPos * 100)),0xFF468681);

    }

    public void renderProgressWidget(MatrixStack matrices, int x, int y) {
        DrawableHelper.fill(matrices, x,y,x + 10,y - 100,0xFF483e10);
        DrawableHelper.fill(matrices, x + 1,y,x + 9, (int) (y - (100 * fishGameLogic.getProgress())),0xFFeccf49);

    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}

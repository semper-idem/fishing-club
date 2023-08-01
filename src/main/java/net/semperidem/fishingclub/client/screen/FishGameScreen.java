package net.semperidem.fishingclub.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.game.FishGameLogic;
import net.semperidem.fishingclub.client.game.fish.Fish;

public class FishGameScreen extends Screen {
    private static final Identifier BOBBER = new Identifier(FishingClub.MOD_ID, "textures/gui/bobber_8x32.png");
    private static final Identifier FISH = new Identifier(FishingClub.MOD_ID, "textures/gui/fish_icon_8x8.png");
    private static final Identifier BACKGROUND = new Identifier(FishingClub.MOD_ID, "textures/gui/minigame_background_160x160.png");
    private static final Identifier BACKGROUND_EMPTY = new Identifier(FishingClub.MOD_ID, "textures/gui/minigame_background_empty.png");
    private static final Identifier WATER_BAR = new Identifier(FishingClub.MOD_ID, "textures/gui/water_bar_detailed_8x128.png");
    private static final Identifier PROGRESS_BAR = new Identifier(FishingClub.MOD_ID, "textures/gui/progress_bar_8x128.png");
    private static final Identifier BAR_BORDER = new Identifier(FishingClub.MOD_ID, "textures/gui/water_border_14x140.png");

    private static final Identifier TREASURE_BACKGROUND = new Identifier(FishingClub.MOD_ID, "textures/gui/treasure_background.png");
    private static final Identifier TREASURE_ARROW = new Identifier(FishingClub.MOD_ID, "textures/gui/treasure_arrow.png");
    private static final Identifier TREASURE_SPOT_L = new Identifier(FishingClub.MOD_ID, "textures/gui/treasure_spot_l.png");
    private static final Identifier TREASURE_SPOT_M = new Identifier(FishingClub.MOD_ID, "textures/gui/treasure_spot_m.png");
    private static final Identifier TREASURE_SPOT_R = new Identifier(FishingClub.MOD_ID, "textures/gui/treasure_spot_r.png");
    private static final Identifier TREASURE_MARK = new Identifier(FishingClub.MOD_ID, "textures/gui/treasure_mark.png");

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

    int ticks = 0;


    public FishGameScreen(Text text, ItemStack caughtUsing, Fish fish, boolean boatFishing) {
        super(text);
        this.fishGameLogic = new FishGameLogic(MinecraftClient.getInstance().player, caughtUsing,fish, boatFishing);
    }


    @Override
    public void tick() {
        ticks++;
        this.fishGameLogic.tick();
        if (this.fishGameLogic.isFinished()) {
            this.close();
            if (this.fishGameLogic.isFishWon()) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Caught Lvl." + this.fishGameLogic.getLevel() + " " + this.fishGameLogic.getName() + "! Nice"));
                MinecraftClient.getInstance().player.sendMessage(Text.of("Exp gained: " + this.fishGameLogic.getExperience()));
            } else {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Fish escaped"));
                if (this.fishGameLogic.isTreasureWon) {
                    MinecraftClient.getInstance().player.sendMessage(Text.of("But we got the treasure :^)"));
                }
            }
        }
    }


    private void renderTreasure(MatrixStack matrices, float delta){
        renderTreasureBackground(matrices);
        renderTreasureBar(matrices);
        renderTreasureSpot(matrices);
        renderTreasureArrow(matrices, delta);
    }

    private void renderTreasureBackground(MatrixStack matrices){
        RenderSystem.setShaderTexture(0, BACKGROUND_EMPTY);
        drawTexture(matrices, windowX, windowY, 0, 0, bgWidth, bgHeight, bgWidth, bgHeight);
    }

    private void renderTreasureBar(MatrixStack matrices){

        RenderSystem.setShaderTexture(0, TREASURE_BACKGROUND);
        drawTexture(matrices, windowX, windowY + bgHeight - 28, 0, 0, 160, 28, 160, 28);
    }

    private void renderTreasureArrow(MatrixStack matrices, float delta){
        float engineArrowPos = (1 - Math.abs(fishGameLogic.arrowPos - 1));
        float nextArrowPos = (1 - Math.abs(fishGameLogic.getNextArrowPos() - 1));
        float arrowPos = engineArrowPos + (nextArrowPos - engineArrowPos) * delta;
        RenderSystem.setShaderTexture(0, TREASURE_ARROW);
        drawTexture(matrices, (int) (windowX + arrowPos * 152 -5 + 4), windowY + bgHeight - 24, 0, 0, 10, 20, 10, 20);
    }

    private void renderTreasureSpot(MatrixStack matrices){
        int width = (int) (160 * fishGameLogic.treasureSpotSize);
        int totalWidth = width + 12;
        RenderSystem.setShaderTexture(0, TREASURE_SPOT_L);

        //LEFT
        drawTexture(matrices, windowX + ((bgWidth - totalWidth)/ 2), windowY + bgHeight - 24, 0, 0, 6, 20, 6, 20);
        //MID
        RenderSystem.setShaderTexture(0, TREASURE_SPOT_M);
        drawTexture(matrices, windowX + ((bgWidth - totalWidth)/ 2) + 6, windowY + bgHeight - 24, 0, 0, width, 20, width, 20);
        //RIGHT
        RenderSystem.setShaderTexture(0, TREASURE_SPOT_R);
        drawTexture(matrices, windowX + ((bgWidth - totalWidth)/ 2) + width + 6, windowY + bgHeight - 24, 0, 0, 6, 20, 6, 20);
    }

    private void renderTreasureMark(MatrixStack matrices){
        if (fishGameLogic.treasureAvailableTicks == 0) return;
        RenderSystem.setShaderTexture(0, TREASURE_MARK);
        int shakeOffset = (ticks / 3 ) % 2;
        drawTexture(matrices, windowX + (bgWidth - 16) / 2, windowY + bgHeight / 2 - 36 + shakeOffset, 0, 0, 16, 16, 16, 16);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {

        windowX = (this.width - 160) / 2;
        windowY = (this.height- 160) / 2;

        matrices.push();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        if (fishGameLogic.treasureOnHook) {
            renderTreasure(matrices, delta);
        } else {
            renderWindow(matrices);
            renderBobber(matrices, delta);
            renderFish(matrices, delta);
            renderProgressBar(matrices);
            renderTreasureMark(matrices);
        }

        renderLineHealth(matrices);
        super.render(matrices, mouseX, mouseY, delta);


        matrices.pop();
    }

    private void renderWindow(MatrixStack matrices){
        RenderSystem.setShaderTexture(0, BACKGROUND);
        drawTexture(matrices, windowX, windowY, 0, 0, bgWidth, bgHeight, bgWidth, bgHeight);
    }

    private void renderBobber(MatrixStack matrices, float delta){
        matrices.push();
        float engineBobberPos = fishGameLogic.getBobberPos();
        float bobberPos = engineBobberPos + (fishGameLogic.nextBobberPos() - engineBobberPos) * delta;
        float bobberLength = fishGameLogic.getBobberLength();
        float bobberScale = barHeight * bobberLength / bobberHeight;
        RenderSystem.setShaderTexture(0, BOBBER);
        matrices.scale(1f,bobberScale,1f);
        int y = (int) ( (windowY + 16 + barHeight - barHeight * bobberPos) * (1f / bobberScale));
        drawTexture(matrices, windowX + 26, y, 0, 0, barWidth, bobberHeight, barWidth, bobberHeight);

        matrices.pop();

    }

    private void renderFish(MatrixStack matrices, float delta){
        RenderSystem.setShaderTexture(0, FISH);
        float engineFishPos = fishGameLogic.getFishPos();
        float fishPos = engineFishPos + (fishGameLogic.nextFishPosition() - engineFishPos) * delta;
        drawTexture(matrices, windowX + 26, (int) (windowY + 16 + barHeight - barHeight * fishPos), 0, 0, barWidth, barWidth, barWidth, barWidth);

    }


    public void renderProgressBar(MatrixStack matrices ) {
        RenderSystem.setShaderTexture(0, PROGRESS_BAR);
        int progressBarLength = (int) (barHeight * fishGameLogic.getProgress());
        drawTexture(matrices, windowX + 126, windowY+ 16 + barHeight - progressBarLength, 0, 0, barWidth, progressBarLength, barWidth, barHeight);

    }
    public void renderLineHealth(MatrixStack matrices) {
        String healthLabel = "Line Health";
        String healthInfo = String.format("%.1f" ,fishGameLogic.getLineHealth());
        int color = 0xFFFFFF;
        if (fishGameLogic.treasureOnHook) {
            healthLabel = "Treasure!";
            healthInfo = String.valueOf(fishGameLogic.treasureHookedTicks / 20);
        }

        if (fishGameLogic.treasureAvailableTicks > 0 && !fishGameLogic.treasureOnHook) {
            healthLabel = "Treasure!";
            healthInfo = "[Enter]";
            color = ((ticks / 3 ) % 2) == 1 ?  0xFFFFFF :  0xffbb33;
        }

        int healthLabelLength = textRenderer.getWidth(healthLabel);
        textRenderer.drawWithShadow(matrices, Text.of(healthLabel), windowX + 80 - healthLabelLength / 2f,windowY + 75,color);

        int healthInfoLength = textRenderer.getWidth(healthInfo);
        textRenderer.drawWithShadow(matrices, Text.of(healthInfo), windowX + 80 - healthInfoLength / 2f,windowY + 90,0xFFFFFF);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}

package net.semperidem.fishingclub.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.game.FishGameLogic;
import net.semperidem.fishingclub.game.fish.Fish;

public class FishGameScreen extends Screen {
    private static final String TEXTURE_DIR_ROOT = "textures/gui/fish_game/";

    private static final int DEFAULT_COLOR = 0xFFFFFF;
    private static final int TREASURE_COLOR = 0xFFBB33;

    private static final String HEALTH_LABEL = "Line Health:";
    private static final String TREASURE_LABEL = "Treasure!";
    private static final String TREASURE_REEL_LABEL  = "[Enter]";

    private static final int backgroundWidth = 160, backgroundHeight = 160;
    private static final int barWidth = 128, barHeight = 8;
    private static final int bobberWidth = 128, bobberHeight = 8;
    private static final int fishIconWidth = 8, fishIconHeight = 8;

    private static final int treasureBarWidth = backgroundWidth, treasureBarHeight = 28;
    private static final int treasureArrowWidth = 10, treasureArrowHeight = treasureBarHeight;
    private static final int treasureSpotWidth = 24, treasureSpotHeight = treasureBarHeight;
    private static final int treasureMarkWidth = 16, treasureMarkHeight = 16;


    private static final Texture BACKGROUND = new Texture("background.png", backgroundWidth, backgroundHeight);
    private static final Texture BACKGROUND_EMPTY = new Texture("background_empty.png", backgroundWidth, backgroundHeight);
    private static final Texture BOBBER = new Texture("bobber.png", bobberWidth, bobberHeight);
    private static final Texture FISH = new Texture("fish_icon.png", fishIconWidth, fishIconHeight);
    private static final Texture PROGRESS_BAR = new Texture("progress_bar.png", barWidth, barHeight);

    private static final Texture TREASURE_BAR = new Texture("treasure_bar.png", treasureBarWidth, treasureBarHeight);
    private static final Texture TREASURE_ARROW = new Texture("treasure_arrow.png", treasureArrowWidth, treasureArrowHeight);
    private static final Texture TREASURE_SPOT = new Texture("treasure_spot.png", treasureSpotWidth, treasureSpotHeight);
    private static final Texture TREASURE_MARK = new Texture("treasure_mark.png", treasureMarkWidth, treasureMarkHeight);

    private int x, y;
    private int barX,barY;
    private int bobberX, bobberY;
    private int fishX,fishY;
    private int progressBarX,progressBarY;

    private int treasureBarX, treasureBarY;
    private int treasureArrowX, treasureArrowY;
    private int treasureSpotX, treasureSpotY;
    private int treasureMarkX, treasureMarkY;

    FishGameLogic fishGameLogic;
    boolean lightTick = false;

    public FishGameScreen(Text text, ItemStack caughtUsing, Fish fish, boolean boatFishing, BlockPos bobberPos) {
        super(text);
        this.fishGameLogic = new FishGameLogic(MinecraftClient.getInstance().player, caughtUsing,fish, boatFishing, bobberPos);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        float saveZone = 0.02f;

        if (mouseX > width / 2f - width * saveZone && mouseX < width / 2f + width *saveZone) {
            fishGameLogic.setReelForce(0);
        } else if (mouseX < width / 2f - width * saveZone){
            fishGameLogic.setReelForce((((float) mouseX - (width / 2f - width * saveZone)) / width) / 10f);
        } else if (mouseX > width / 2f + width * saveZone) {
            fishGameLogic.setReelForce((((float) mouseX - (width / 2f + width * saveZone)) / width) / 10f);
        }
        super.mouseMoved(mouseX, mouseY);
    }



    @Override
    protected void init() {
        super.init();
        x = (this.width - backgroundWidth) / 2;
        y = (this.height- backgroundHeight) / 2;

        barX = x + 16;
        barY = y + backgroundHeight - barHeight - 16;

        bobberX = barX;
        bobberY = barY;

        fishX = barX;
        fishY = barY;

        progressBarX = barX;
        progressBarY = y + 16;

        treasureBarX = x;
        treasureBarY = y + backgroundHeight - treasureBarHeight;

        treasureArrowX = treasureBarX;
        treasureArrowY = treasureBarY;

        treasureSpotX = x;
        treasureSpotY = treasureBarY;

        treasureMarkX = x + (backgroundWidth - treasureMarkWidth) / 2;
        treasureMarkY = y + backgroundHeight / 2 - 36;
    }

    @Override
    public void tick() {
        lightTick = !lightTick;
        this.fishGameLogic.tick();
        if (this.fishGameLogic.isFinished()) {
            this.close();
            if (this.fishGameLogic.isFishCaptured()) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Caught Lvl." + this.fishGameLogic.getLevel() + " " + this.fishGameLogic.getName() + "! Nice"));
                MinecraftClient.getInstance().player.sendMessage(Text.of("Exp gained: " + this.fishGameLogic.getExperience()));
            } else {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Fish escaped"));
                    if (this.fishGameLogic.isTreasureCaptured()) {
                    MinecraftClient.getInstance().player.sendMessage(Text.of("But we got the treasure :^)"));
                }
            }
        }
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        matrices.push();
        if (isTreasureHooked()) {
            renderTreasure(matrices, delta);
        } else {
            renderFish(matrices, delta);
        }
        renderInfo(matrices);
        matrices.pop();
    }

    private void renderFish(MatrixStack matrices, float delta){
        renderFishBackground(matrices);
        renderBobber(matrices, delta);
        renderFishIcon(matrices, delta);
        renderProgressBar(matrices);
        renderTreasureMark(matrices);
    }

    private void renderTreasure(MatrixStack matrices, float delta){
        renderTreasureBackground(matrices);
        renderTreasureBar(matrices);
        renderTreasureSpot(matrices);
        renderTreasureArrow(matrices, delta);
    }

    private void renderTreasureBar(MatrixStack matrices){
        TREASURE_BAR.render(matrices, treasureBarX, treasureBarY);
    }

    private void renderTreasureArrow(MatrixStack matrices, float delta){
        treasureArrowX = (int) (x + getArrowPos(delta) * (treasureBarWidth - treasureArrowWidth));
        TREASURE_ARROW.render(matrices, treasureArrowX, treasureArrowY);
    }

    private void renderTreasureSpot(MatrixStack matrices){
        matrices.push();
        float spotWidth = (treasureBarWidth * fishGameLogic.getTreasureSpotSize());
        float spotScale = spotWidth / treasureSpotWidth;
        matrices.scale(spotScale, 1,1);
        treasureSpotX = (int) ((x + (treasureBarWidth - spotWidth) / 2) * (1 / spotScale));
        TREASURE_SPOT.render(matrices, treasureSpotX, treasureSpotY);
        matrices.pop();
    }

    private void renderTreasureMark(MatrixStack matrices){
        if (!isTreasureOnHook()) return;
        int shakeOffset = lightTick ? 1 : 0;
        TREASURE_MARK.render(matrices, treasureMarkX, treasureMarkY + shakeOffset);
    }

    private void renderTreasureBackground(MatrixStack matrices){
        BACKGROUND_EMPTY.render(matrices, x,y);
    }

    private void renderFishBackground(MatrixStack matrices){
        BACKGROUND.render(matrices, x, y);
    }

    private void renderBobber(MatrixStack matrices, float delta){
        matrices.push();
        float bobberScale = barWidth * fishGameLogic.getBobberLength() / barWidth;
        matrices.scale(bobberScale,1f,1f);
        bobberX = (int) ( (barX + barWidth * getBobberPos(delta)) * (1f / bobberScale));
        BOBBER.render(matrices, bobberX, bobberY);
        matrices.pop();
    }

    private void renderFishIcon(MatrixStack matrices, float delta){
        fishX = (int) (barX + barWidth * getFishPos(delta));
        fishY = barY - (int)((Math.sqrt((10 - Math.abs(fishGameLogic.jumpTicks - 10))) * 4)) ;
        FISH.render(matrices, fishX, fishY);
    }

    private void renderProgressBar(MatrixStack matrices ) {
        int progressBarWidth = (int) (barWidth * fishGameLogic.getProgress());
        PROGRESS_BAR.render(matrices, progressBarX, progressBarY, progressBarWidth,barHeight);
    }

    private void renderInfo(MatrixStack matrices) {
        String infoLabel = (isTreasureHooked() || isTreasureOnHook())? TREASURE_LABEL : HEALTH_LABEL;
        String infoValue = isTreasureHooked() ? getTimeLeft() : isTreasureOnHook() ? TREASURE_REEL_LABEL : getLineHealth();
        int labelColor = isTreasureOnHook() && ! isTreasureHooked() ? (
                lightTick ?
                        DEFAULT_COLOR :
                        TREASURE_COLOR)
                : DEFAULT_COLOR;

        textRenderer.drawWithShadow(matrices, Text.of(infoLabel), x + (backgroundWidth - textRenderer.getWidth(infoLabel)) / 2f, y + 75,labelColor);
        textRenderer.drawWithShadow(matrices, Text.of(infoValue), x + (backgroundWidth - textRenderer.getWidth(infoValue)) / 2f, y + 90,DEFAULT_COLOR);
    }

    private String getTimeLeft(){
        return String.valueOf(fishGameLogic.getTimeLeft());
    }

    private String getLineHealth(){
        return String.format("%.1f" ,fishGameLogic.getLineHealth());
    }

    private boolean isTreasureOnHook(){
        return fishGameLogic.isTreasureOnHook();
    }

    private boolean isTreasureHooked(){
        return fishGameLogic.isReelingTreasure();
    }


    private float getFishPos(float delta) {
        return getDeltaPosition(fishGameLogic.getFishPos(),fishGameLogic.nextFishPosition(), delta);
    }

    private float getBobberPos(float delta){
        return getDeltaPosition(fishGameLogic.getBobberPos(), fishGameLogic.nextBobberPos(), delta);
    }

    private float getArrowPos(float delta){
        return getDeltaPosition(fishGameLogic.getArrowPos(), fishGameLogic.getNextArrowPos(), delta);
    }

    private static float getDeltaPosition(float initialPosition, float nextPosition, float delta){
     return initialPosition + (nextPosition - initialPosition) * delta;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    static class Texture {
        Identifier texture;
        int textureWidth;
        int textureHeight;

        Texture(String fileName, int textureWidth, int textureHeight){
            this.texture = new Identifier(FishingClub.MOD_ID, TEXTURE_DIR_ROOT + fileName);
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }
        private void render(MatrixStack matrices, int x, int y){
            render(matrices,x, y, textureWidth, textureHeight);
        }

        private void render(MatrixStack matrices, int x, int y, int width, int height){
            RenderSystem.setShaderTexture(0, this.texture);
            drawTexture(matrices, x, y, 0, 0, width, height, this.textureWidth, this.textureHeight);
        }
    }
}

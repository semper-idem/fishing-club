package net.semperidem.fishing_club.client.screen.game;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.game.FishingGameController;
import net.semperidem.fishing_club.network.payload.FishingGameInputPayload;
import net.semperidem.fishing_club.network.payload.FishingGameTickPayload;
import net.semperidem.fishing_club.registry.FCKeybindings;
import net.semperidem.fishing_club.screen.fishing_game.FishingGameScreenHandler;
import org.lwjgl.glfw.GLFW;

public class FishingGameScreen extends HandledScreen<FishingGameScreenHandler> implements ScreenHandlerProvider<FishingGameScreenHandler> {
    private static final String TEXTURE_DIR_ROOT = "textures/gui/fishing_game/";

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

    private static final float safeZone = 0.01f;
    private float halfScreen;
    private int halfBobber = 0;

    private int halfFish = fishIconWidth / 2;
    boolean lightTick = false;
    public final FishingGameController fishGameLogic;

    private final float startingPitch;
    private final float startingYaw;
    private float targetYaw;
    private float targetBodyYaw;
    private float targetPitch;
    private boolean swayTracked = false;
    PlayerEntity clientPlayer;

    public FishingGameScreen(FishingGameScreenHandler fishGameScreenHandler, PlayerInventory playerInventory, Text text) {
        super(fishGameScreenHandler, playerInventory, text);
        this.fishGameLogic = fishGameScreenHandler.fishGameLogic;
        clientPlayer = playerInventory.player;
        startingYaw = clientPlayer.getYaw();
        startingPitch = clientPlayer.getPitch();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        double distanceFromCenterPercent = (halfScreen - mouseX) / halfScreen;
        float reelForce = (Math.abs(distanceFromCenterPercent) > safeZone) ?
                (float) (distanceFromCenterPercent * -0.05f) :
                0;
        this.handler.fishGameLogic.reelForce = reelForce;
        updateCameraTarget();
        //todo this make reeling/pulling not have impact if im not moving mouse
        super.mouseMoved(mouseX, mouseY);
    }

    private boolean keyPressed(int keyCode){
        return InputUtil.isKeyPressed(MinecraftClient.getInstance().getWindow().getHandle(),keyCode);//todo use vanila keybindings
    }

    public void consumeTick(FishingGameTickPayload fishingGameTickPayload) {
        fishGameLogic.consumeTick(fishingGameTickPayload);
        ClientPlayNetworking.send(new FishingGameInputPayload(this.handler.fishGameLogic.reelForce, isReeling(), isPulling()));
    }
    boolean isReeling(){
        return keyPressed(GLFW.GLFW_KEY_SPACE);
    }

    boolean isPulling(){
        return keyPressed(GLFW.GLFW_KEY_ENTER);
    }


    @Override
    protected void init() {
        super.init();
        x = (this.width - backgroundWidth) / 2;
        y = (this.height- backgroundHeight) / 2;
        halfBobber = (int) (fishGameLogic.getBobberSize() * bobberWidth / 2);
        halfScreen = width / 2f;

        barX = x + 16;
        barY = y + backgroundHeight - barHeight - 16;

        bobberX = barX + barWidth / 2 - halfBobber;
        bobberY = barY;

        fishX = barX + barWidth / 2 - fishIconWidth / 2;
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
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {}

    @Override
    public void handledScreenTick() {
        lightTick = !lightTick;
    }

    private void updateCameraTarget() {
        if (MinecraftClient.getInstance().options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
            swayTracked = false;
            return;
        }
        swayTracked = true;
        targetYaw = startingYaw + this.handler.fishGameLogic.reelForce * 800;
        targetBodyYaw = startingYaw + this.handler.fishGameLogic.reelForce * 6000;
        targetPitch = startingPitch - (24.28f - (float) (Math.sqrt((0.05f - Math.abs(this.handler.fishGameLogic.reelForce )) * 1000) * 4));
    }

    private float getNextFramePitch(){
        float currentPitch = clientPlayer.getPitch();
        return currentPitch  + ((targetPitch - currentPitch) / 20);
    }
    private float getNextFrameYaw(){
        float currentYaw = clientPlayer.getYaw();
        return currentYaw  + ((targetYaw - currentYaw) / 10);
    }
    private float getNextFrameBodyYaw(){
        float currentBodyYaw = clientPlayer.getBodyYaw();
       return currentBodyYaw  + ((targetBodyYaw - currentBodyYaw) / 5);

    }

    private void updateCamera(){
        if (!swayTracked) {
            return;
        }
        clientPlayer.setPitch(getNextFramePitch());
        clientPlayer.setYaw(getNextFrameYaw());
        clientPlayer.setHeadYaw(getNextFrameYaw());
        clientPlayer.setBodyYaw(getNextFrameBodyYaw());
    }

    @Override
    public void render(DrawContext drawContext, int mouseX, int mouseY, float delta) {
        drawContext.getMatrices().push();
        updateCamera();
        if (isTreasureHooked()) {
            renderTreasure(drawContext, delta);
        } else {
            renderFish(drawContext, delta);
        }
        renderInfo(drawContext);
        drawContext.getMatrices().pop();
    }

    private void renderFish(DrawContext drawContext, float delta){
        renderFishBackground(drawContext);
        renderBobber(drawContext, delta);
        renderFishIcon(drawContext, delta);
        renderProgressBar(drawContext);
        renderTreasureMark(drawContext);
    }

    private void renderTreasure(DrawContext drawContext, float delta){
        renderTreasureBackground(drawContext);
        renderTreasureBar(drawContext);
        renderTreasureSpot(drawContext);
        renderTreasureArrow(drawContext, delta);
    }

    private void renderTreasureBar(DrawContext drawContext){
        TREASURE_BAR.render(drawContext, treasureBarX, treasureBarY);
    }

    private void renderTreasureArrow(DrawContext drawContext, float delta){
        treasureArrowX = (int) (x + getArrowPos(delta) * (treasureBarWidth - treasureArrowWidth));
        TREASURE_ARROW.render(drawContext, treasureArrowX, treasureArrowY);
    }

    private void renderTreasureSpot(DrawContext drawContext){
        drawContext.getMatrices().push();
        float spotWidth = (treasureBarWidth * fishGameLogic.getTreasureSpotSize());
        float spotScale = spotWidth / treasureSpotWidth;
        drawContext.getMatrices().scale(spotScale, 1,1);
        treasureSpotX = (int) ((x + (treasureBarWidth - spotWidth) / 2) * (1 / spotScale));
        TREASURE_SPOT.render(drawContext, treasureSpotX, treasureSpotY);
        drawContext.getMatrices().pop();
    }

    private void renderTreasureMark(DrawContext drawContext){
        if (!canPullTreasure()) return;
        int shakeOffset = lightTick ? 1 : 0;
        TREASURE_MARK.render(drawContext, treasureMarkX, treasureMarkY + shakeOffset);
    }

    private void renderTreasureBackground(DrawContext drawContext){
        BACKGROUND_EMPTY.render(drawContext, x,y);
    }

    private void renderFishBackground(DrawContext drawContext){
        BACKGROUND.render(drawContext, x, y);
    }

    private void renderBobber(DrawContext drawContext, float delta){
        drawContext.getMatrices().push();
        float bobberScale = barWidth * fishGameLogic.getBobberSize() / barWidth;
        drawContext.getMatrices().scale(bobberScale,1f,1f);
        if (fishGameLogic.bobberHasFish()) {
            RenderSystem.setShaderColor(1f,1,0.5f,1);
        }
        bobberX = (int) ( (barX + barWidth * getBobberStartPos(delta) - halfBobber) * (1f / bobberScale));
        BOBBER.render(drawContext, bobberX, bobberY);
        RenderSystem.setShaderColor(1f,1,1,1);
        drawContext.getMatrices().pop();
    }

    private void renderFishIcon(DrawContext drawContext, float delta){
        fishX = (int) (barX + barWidth * getFishPos(delta)) - halfFish;
        fishY = barY - (int)((fishGameLogic.getFishPosY() * 4)) ;
        FISH.render(drawContext, fishX, fishY);
    }

    private void renderProgressBar(DrawContext drawContext) {
        int progressBarWidth = (int) (barWidth * fishGameLogic.getProgress());
        PROGRESS_BAR.render(drawContext, progressBarX, progressBarY, progressBarWidth,barHeight);
    }

    private void renderInfo(DrawContext drawContext) {
        String infoLabel = (isTreasureHooked() || canPullTreasure())? TREASURE_LABEL : HEALTH_LABEL;
        String infoValue = isTreasureHooked() ? getTimeLeft() : canPullTreasure() ? TREASURE_REEL_LABEL : getLineHealth();
        int labelColor = canPullTreasure() && ! isTreasureHooked() ? (
                lightTick ?
                        DEFAULT_COLOR :
                        TREASURE_COLOR)
                : DEFAULT_COLOR;

        drawContext.drawTextWithShadow(textRenderer, infoLabel, (int) (x + (backgroundWidth - textRenderer.getWidth(infoLabel)) / 2f), y + 75,labelColor);
        drawContext.drawTextWithShadow(textRenderer, infoValue, (int) (x + (backgroundWidth - textRenderer.getWidth(infoValue)) / 2f), y + 90,DEFAULT_COLOR);
    }

    private String getTimeLeft(){
        return String.valueOf(fishGameLogic.getTimeLeft());
    }

    private String getLineHealth(){
        return String.format("%.1f" ,fishGameLogic.getLineHealth());
    }

    private boolean canPullTreasure(){
        return fishGameLogic.canPullTreasure();
    }

    private boolean isTreasureHooked(){
        return fishGameLogic.isTreasureHuntActive();
    }


    private float getFishPos(float delta) {
        return getDeltaPosition(fishGameLogic.getFishPosX(),fishGameLogic.getNextFishPosX(), delta);
    }

    private float getBobberStartPos(float delta){
        return getDeltaPosition(fishGameLogic.getBobberPos(), fishGameLogic.getNextBobberPos(), delta);
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
            this.texture = FishingClub.getIdentifier(TEXTURE_DIR_ROOT + fileName);
            this.textureWidth = textureWidth;
            this.textureHeight = textureHeight;
        }
        private void render(DrawContext drawContext, int x, int y){
            render(drawContext,x, y, textureWidth, textureHeight);
        }

        private void render(DrawContext drawContext, int x, int y, int width, int height){
            drawContext.drawTexture(this.texture , x, y, 0, 0, width, height, this.textureWidth, this.textureHeight);
        }
    }
}

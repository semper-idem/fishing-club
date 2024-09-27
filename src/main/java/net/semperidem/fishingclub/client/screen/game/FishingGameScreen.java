package net.semperidem.fishingclub.client.screen.game;

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
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.game.FishingGameController;
import net.semperidem.fishingclub.network.payload.FishingGameInputKeyboardPayload;
import net.semperidem.fishingclub.network.payload.FishingGameInputMousePayload;
import net.semperidem.fishingclub.network.payload.FishingGameTickS2CPayload;
import net.semperidem.fishingclub.screen.fishing_game.FishingGameScreenHandler;

public class FishingGameScreen extends HandledScreen<FishingGameScreenHandler> implements ScreenHandlerProvider<FishingGameScreenHandler> {
    public final FishingGameController controller;
    private static final Identifier ATLAS = FishingClub.identifier("textures/gui/fishing_game/atlas.png");

    private final float startingPitch;
    private final float startingYaw;
    private float targetYaw;
    private float targetBodyYaw;
    private float targetPitch;
    private final PlayerEntity clientPlayer;

    private long tick = 0;

    private static final float MIN_MOVE = 0.01f;

    private int baseFishX;
    private int baseFishY;

    private int baseProgressX;
    private int baseProgressY;

    private int baseTreasureX;
    private int baseTreasureY;

    public FishingGameScreen(FishingGameScreenHandler handler, PlayerInventory playerInventory, Text text) {
        super(handler, playerInventory, text);
        this.controller = this.handler.controller;
        this.clientPlayer = playerInventory.player;
        this.startingYaw = this.clientPlayer.getYaw();
        this.startingPitch = this.clientPlayer.getPitch();
    }


    @Override
    protected void handledScreenTick() {
        ClientPlayNetworking.send(
                new FishingGameInputKeyboardPayload(
                        InputUtil.isKeyPressed(
                                MinecraftClient.getInstance().getWindow().getHandle(),
                                InputUtil.GLFW_KEY_SPACE
                        )
                )
        );
        tick++;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (this.client == null) {
            return;
        }

        int centerX = (int) (this.client.getWindow().getWidth() * 0.5f);
        double distanceFromCenterPercent = (centerX - mouseX) / centerX;
        if (Math.abs(distanceFromCenterPercent) < MIN_MOVE) {
            return;
        }

        this.controller.reelForce = (float) distanceFromCenterPercent * -0.05f;
        ClientPlayNetworking.send(new FishingGameInputMousePayload(this.handler.controller.reelForce));
        this.updateCameraTarget();
    }

    public void update(FishingGameTickS2CPayload fishingGameTickPayload) {
        this.controller.updateClient(fishingGameTickPayload);
    }

    @Override
    protected void init() {
        super.init();
        if (this.client == null) {
            return;
        }

        this.x = (int) (this.client.getWindow().getScaledWidth() * 0.5f) - 90;//half of back
        this.y = this.client.getWindow().getScaledHeight() - 100;//back

        this.baseFishX = this.x;
        this.baseFishY = this.y + 48;

        this.baseProgressX = this.x + 3;
        this.baseProgressY = this.y + 3;

        this.baseTreasureX = this.baseFishX;
        this.baseTreasureY = this.baseFishY;
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {}

    private void updateCameraTarget() {
        if (MinecraftClient.getInstance().options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
            return;
        }
        this.targetYaw = startingYaw + this.handler.controller.reelForce * 800;
        this.targetBodyYaw = startingYaw + this.handler.controller.reelForce * 6000;
        this.targetPitch = startingPitch - (24.28f - (float) (Math.sqrt((0.05f - Math.abs(this.handler.controller.reelForce )) * 1000) * 4));
        this.clientPlayer.setPitch(getNextFramePitch());
        this.clientPlayer.setYaw(getNextFrameYaw());
        this.clientPlayer.setHeadYaw(getNextFrameYaw());
        this.clientPlayer.setBodyYaw(getNextFrameBodyYaw());
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

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(ATLAS, this.x, this.y, 0, 0, 180, 60, 180, 120);
    }

    public void renderFront(DrawContext context) {
        context.drawTexture(ATLAS, this.x, this.baseFishY, 0, 60, 180, 12, 180, 120);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        //Fish
        context.enableScissor(this.baseFishX, this.baseFishY - 20, this.baseFishX + 174, this.baseFishY + 9);

        //Treasure
        int currentTreasureX = this.baseTreasureX + 100;
        context.drawTexture(ATLAS, currentTreasureX + 3, this.baseTreasureY - 8, 35, 100, 6, 6, 180, 120);
        context.drawTexture(ATLAS, currentTreasureX, this.baseTreasureY, 35, 106, 11, 13, 180, 120);
       // context.drawTexture(ATLAS, this.baseFishX, this.baseFishY, 72, 80, 14, 12, 180, 120);
        context.disableScissor();
        //Progress
        context.drawTexture(ATLAS, this.baseProgressX, this.baseProgressY, 0, 72, (int)(174 * 1f), 8, 180, 120);


        context.drawTexture(ATLAS, currentTreasureX - 3,  this.baseTreasureY - 40, 0, 80, 18, 40, 180, 120);
        context.drawTexture(ATLAS, currentTreasureX + 4, (int) (this.baseTreasureY - 40 + 16 + 24 * 0.6f), 18, 80, 4, (int) (24 * 0.4f), 180, 120);

        int buttonU = Math.sin(tick) > 0 ? 0 : 10;
        context.drawTexture(ATLAS, currentTreasureX - 7, this.baseTreasureY - 1, 22, 80 + buttonU, 26, 10, 180, 120);
        boolean isWon = true;
        int treasureResultU = isWon ? 0 : 8;
        context.drawTexture(ATLAS, currentTreasureX, this.baseTreasureY - 36, 22, 100 + treasureResultU, 13, 8, 180, 120);
//        context.drawTexture(ATLAS, this.x, this.y + 60 - 12, 0, 60, 180, 12, 180, 120);
//        context.drawTexture(ATLAS, this.x, this.y + 60 - 12, 0, 60, 180, 12, 180, 120);
        this.renderFront(context);

        //Bobber
        boolean isPulling = false;
        boolean isSuccessful = false;
        int bobberU = isSuccessful ? 12 : 0;
        int bobberV = isPulling ? 9 : 0;
        context.enableScissor(this.baseFishX + 3, this.baseFishY, this.baseFishX + 174, this.baseFishY + 9);
        //context.drawTexture(ATLAS, this.baseFishX, this.baseFishY, 48 + bobberU, 80 + bobberV, 12, 9, 180, 120);
        context.disableScissor();
    }
}

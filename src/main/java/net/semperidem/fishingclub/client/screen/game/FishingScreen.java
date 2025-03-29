package net.semperidem.fishingclub.client.screen.game;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.game.BobberComponent;
import net.semperidem.fishingclub.game.FishingController;
import net.semperidem.fishingclub.network.payload.FishingGameInputKeyboardPayload;
import net.semperidem.fishingclub.network.payload.FishingGameInputMousePayload;
import net.semperidem.fishingclub.network.payload.FishingGameTickS2CPayload;
import net.semperidem.fishingclub.screen.fishing.FishingScreenHandler;

public class FishingScreen extends HandledScreen<FishingScreenHandler> implements ScreenHandlerProvider<FishingScreenHandler> {
    public final FishingController controller;
    private static final Identifier ATLAS = FishingClub.identifier("textures/gui/fishing_game.png");

    private final float startingPitch;
    private final float startingYaw;
    private float targetYaw;
    private float targetBodyYaw;
    private float targetPitch;
    private final PlayerEntity clientPlayer;

    private long tick = 0;
    private int reelTick = 0;

    private static final float MIN_MOVE = 0.03f;

    private int baseFishX;
    private int baseFishY;

    private int baseBobberX;
    private int baseBobberY;

    private int baseProgressX;
    private int baseProgressY;

    private int baseTreasureX;
    private int currentTreasureX;
    private int baseTreasureY;

    private int originX, originY;

    private int treasureStartTick = -1;


    public FishingScreen(FishingScreenHandler handler, PlayerInventory playerInventory, Text text) {
        super(handler, playerInventory, text);
        this.controller = this.handler.controller;
        this.clientPlayer = playerInventory.player;
        this.startingYaw = this.clientPlayer.getYaw();
        this.startingPitch = this.clientPlayer.getPitch();
    }


    @Override
    protected void handledScreenTick() {
        if (this.handler.controller.getProgress() >= 1) {
            this.x = (int) (this.originX + (Math.random() * 2) - 1);
            this.y = (int) (this.originY + (Math.random() * 2) - 1);
        }
        ClientPlayNetworking.send(
                new FishingGameInputKeyboardPayload(
                        InputUtil.isKeyPressed(
                                MinecraftClient.getInstance().getWindow().getHandle(),
                                InputUtil.GLFW_KEY_SPACE
                        )
                )
        );

        this.reelTick = MathHelper.clamp(this.reelTick + (controller.isReeling() ? 1 : -1), 0, 8);

        tick++;
    }


    public int getReelTick() {
        return this.reelTick;
    }
    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (this.client == null) {
            return;
        }

        int centerX = (int) (this.client.getWindow().getScaledWidth() * 0.5f);
        float bobberSizeScale = this.controller.getBobberSize() / BobberComponent.BASE_LENGTH;
        float inverseBobberSizeScale = BobberComponent.BASE_LENGTH  / this.controller.getBobberSize();
        float deadZone = MIN_MOVE * inverseBobberSizeScale;
        double distanceFromCenterPercent = (centerX - mouseX) / centerX;
        if (Math.abs(distanceFromCenterPercent) < deadZone) {
            return;
        }
        this.controller.reelForce = (float) (distanceFromCenterPercent - Math.copySign(deadZone, distanceFromCenterPercent)) * -0.02f * bobberSizeScale;
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

        this.x = (int) (this.client.getWindow().getScaledWidth() * 0.5f) - 91;//half of back
        this.y = this.client.getWindow().getScaledHeight() - 51;//back
        this.originX = this.x;
        this.originY = this.y;

    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
    }

    private void updateCameraTarget() {
        this.targetYaw = startingYaw + this.handler.controller.reelForce * 800;
        this.targetBodyYaw = startingYaw + this.handler.controller.reelForce * 6000;
        this.targetPitch = startingPitch - (24.28f - (float) (Math.sqrt((0.05f - Math.abs(this.handler.controller.reelForce)) * 1000) * 4));
        this.clientPlayer.setPitch(getNextFramePitch());
        this.clientPlayer.setYaw(getNextFrameYaw());
        this.clientPlayer.setHeadYaw(getNextFrameYaw());
        this.clientPlayer.setBodyYaw(getNextFrameBodyYaw());
    }

    private float getNextFramePitch() {
        float currentPitch = clientPlayer.getPitch();
        return currentPitch + ((targetPitch - currentPitch) / 20);
    }

    private float getNextFrameYaw() {
        float currentYaw = clientPlayer.getYaw();
        return currentYaw + ((targetYaw - currentYaw) / 10);
    }

    private float getNextFrameBodyYaw() {
        float currentBodyYaw = clientPlayer.getBodyYaw();
        return currentBodyYaw + ((targetBodyYaw - currentBodyYaw) / 5);

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(ATLAS, this.x, this.y, 0, 0, 182, 50, 182, 120);
    }

    public void renderFront(DrawContext context) {
        context.drawTexture(ATLAS, this.x, this.y + 38, 0, 50, 182, 12, 182, 120);
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.renderBackground(context, mouseX, mouseY, delta);
        context.enableScissor(this.x + 2, this.y + 38 - 20, this.x + 182, this.y + 38 + 9);

        double bobberSize = this.controller.getBobberSize() * 178;
        //left
        int bobberOffset = (int) (bobberSize * 0.5D);
        int bobberCenter = (int) (this.x + 2 + 178 * controller.getBobberPos() + 1);
        context.fill(bobberCenter - bobberOffset, this.y + 38, bobberCenter + bobberOffset, this.y + 38 + 14, 0x77d9caca);
        //Fish
        context.drawTexture(
                ATLAS,
                (int) (this.x - 6 + 176 * controller.getFishPosX()),
                (int) (this.y + 38 - 20 * controller.getFishPosY()),
                70,
                70,
                16,
                14,
                182,
                120
        );
        context.disableScissor();
        //Progress
        float progress = this.controller.getProgress();
        context.drawTexture(
                ATLAS,
                this.x + 3,
                this.y + 3,
                0,
                62,
                (int) (176 * progress),
                8,
                182,
                120
        );


        boolean treasureAvailable = this.controller.canPullTreasure();
        boolean treasureActive = this.controller.isTreasureHuntActive();

        if(!treasureActive) {
            String progressString = (int) (progress * 100) + "%";
            int progressStringWidth = (int) (this.client.textRenderer.getWidth(progressString) * 0.5f);
            context.drawText(this.client.textRenderer, progressString, this.x + 3 + 87 - progressStringWidth, this.y + 3, progress > 0.5f ? 0xDDDDDD : 0x888888, true);

            int level = this.controller.getLevel();
            int levelWidth = this.client.textRenderer.getWidth(String.valueOf(level));
            String levelString = "Level:";
            context.drawText(this.client.textRenderer, String.valueOf(level), this.x + 60 - levelWidth, this.y + 15, 0xffeace, true);
            context.drawText(this.client.textRenderer, levelString, this.x + 6, this.y + 15, 0xffeace, true);

            int health = (int) this.controller.getLineHealth();
            String healthString = "Health:";
            int healthWidth = this.client.textRenderer.getWidth(String.valueOf(health));
            context.drawText(this.client.textRenderer, String.valueOf(health), this.x + 60 - healthWidth, this.y + 26, 0xffeace, true);
            context.drawText(this.client.textRenderer, healthString, this.x + 6, this.y + 26, 0xffeace, true);

            String expectedFish = "[?]";
            context.drawText(this.client.textRenderer, expectedFish, this.x + 94, this.y + 20, 0xffeace, true);
            this.currentTreasureX = this.x - 3 + (int) (this.controller.getTreasureStartPosition() * 174);
        }
        if (treasureAvailable && !treasureActive) {
            if (this.treasureStartTick < 0) {
                this.treasureStartTick = (int) this.tick + 10;
            }
            context.enableScissor(this.x + 3, this.y + 41 - 20, this.x + 177, this.y + 41 + 9);
            //Treasure
            int timeSinceStart = (int) ((this.tick - this.treasureStartTick) * 0.5f);
            int popupY = timeSinceStart > 6 ? (int) (6 + (timeSinceStart * 0.2) % 2) : timeSinceStart;
            if (timeSinceStart < 50) {
                context.drawTexture(
                        ATLAS,
                        currentTreasureX + 3,
                        this.y + 41 - popupY,
                        33,
                        90,
                        6,
                        6,
                        182,
                        120
                );
            }
            context.drawTexture(
                    ATLAS,
                    currentTreasureX,
                    this.y + 41,
                    33,
                    96,
                    11,
                    13,
                    182,
                    120
            );
            context.disableScissor();
            float pickupProgress = this.controller.getTreasureStartProgress();
            if (pickupProgress > 0) {
                context.fill(currentTreasureX - 1, this.y + 41, currentTreasureX + 12, this.y + 41 - 3, 0xFF270038);
                context.fill(currentTreasureX, this.y + 41 - 1, (int) (currentTreasureX + 11 * pickupProgress), this.y + 41 - 2, 0xFFA400EB);

            }
        }
        //Bobber
        this.renderFront(context);
        boolean isPulling = controller.isReeling();
        boolean isSuccessful = controller.bobberHasFish();
        int bobberU = isSuccessful ? 12 : 0;
        int bobberV = isPulling ? 9 : 0;
       context.drawTexture(
                ATLAS,
                bobberCenter - bobberOffset,
                this.y + 38,
                46 + bobberU,
                70 + bobberV,
                3,
                9,
                182,
                120
        );
        //right
        context.drawTexture(
                ATLAS,
                bobberCenter + bobberOffset - 3,
                this.y + 38,
                55 + bobberU,
                70 + bobberV,
                3,
                9,
                182,
                120
        );


        if (!treasureActive) {
            return;
        }
        context.fill(this.x + 1, this.y + 1, this.x + 179, this.y + 49, 0xAA000000);


        context.enableScissor(this.x - 6 + 3, this.y + 38, this.x - 6 + 174, this.y + 38 + 9);
        //Treasure (again)
        context.drawTexture(
                ATLAS,
                currentTreasureX,
                this.y + 41,
                33,
                96,
                11,
                13,
                182,
                120
        );
        context.disableScissor();

        //Treasure widget bg
        context.drawTexture(
                ATLAS,
                currentTreasureX - 3,
                this.y + 41 - 50,
                0,
                70,
                17,
                40,
                182,
                120
        );
        //Treasure progress
        double treasureProgress = this.controller.getTreasureProgress();//next line replace cast to float with cast to int for stable progress
        context.drawTexture(
                ATLAS,
                currentTreasureX + 4,
                (int) (this.y + 41 - 35 + 26 * (1 - treasureProgress)),
                17,
                (float) (70 + 26 * (1 - treasureProgress)),
                3,
                (int) (26 * treasureProgress),
                182,
                120
        );

        int buttonU = Math.sin(tick) > 0 ? 0 : 10;
        context.drawTexture(
                ATLAS,
                currentTreasureX - 7,
                this.y + 41 - 11,
                20,
                70 + buttonU,
                26,
                10,
                182,
                120
        );
        boolean isDone = this.controller.isTreasureDone();
        if (!isDone) {
            int timeLeftWidth = (int) (this.client.textRenderer.getWidth(this.controller.getTreasureTimeLeft()) * 0.5f);
            context.drawText(this.client.textRenderer, this.controller.getTreasureTimeLeft(), currentTreasureX - timeLeftWidth + 6, this.y + 41 - 45, 0xFFFFFF, true);
            return;
        }
        int treasureResultU = this.controller.getTreasureProgress() >= 1 ? 0 : 8;

        context.drawTexture(
                ATLAS,
                currentTreasureX - 1,
                (int) (this.y + 41 - 46 + (this.tick * 0.2f % 2)),
                20,
                90 + treasureResultU,
                13,
                8,
                182,
                120
        );
    }
}

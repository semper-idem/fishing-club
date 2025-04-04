package net.semperidem.fishingclub.client.screen.game;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.specimen.SpecimenDescription;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.managers.CardProgression;
import net.semperidem.fishingclub.network.payload.FishingInputKeyboardPayload;
import net.semperidem.fishingclub.screen.fishing_post.FishingPostScreenHandler;

import java.util.ArrayList;
import java.util.List;

import static net.semperidem.fishingclub.util.TextUtil.*;

public class FishingPostScreen extends HandledScreen<FishingPostScreenHandler> implements ScreenHandlerProvider<FishingPostScreenHandler> {
    private static final Identifier BOOK_TEXTURE = FishingClub.identifier("textures/gui/page.png");
    private final Perspective previousPerspective;
    private static final Identifier EXPERIENCE_BAR_BACKGROUND_TEXTURE = Identifier.ofVanilla("hud/experience_bar_background");
    private static final Identifier EXPERIENCE_BAR_PROGRESS_TEXTURE = Identifier.ofVanilla("hud/experience_bar_progress");
    public static Text PRESENTING_CAMERA = Text.of("fishing_club$presenting_camera");
    int textStartX;
    int textEndX;
    int textY;
    int animationTick = 20;
    static int EXP_ANIMATION_LENGTH = 40;
    int expAnimationTick = EXP_ANIMATION_LENGTH;
    int expX;
    int expY;
    private static final int LINE_COLOR = 0xFF000000;
    private static final int OUTLINE_COLOR = 0xFFFFFFFF;
    private List<SpecimenDescription.Line> specimenDescription = new ArrayList<>();
    private final Card card;
    int initialLevel;
    int initialExp;
    int fishExp;

    public FishingPostScreen(FishingPostScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.previousPerspective = MinecraftClient.getInstance().options.getPerspective();
        this.client = MinecraftClient.getInstance();
        MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_FRONT);
        double angle = Math.toRadians(this.client.player.getYaw());
        double angle90 = Math.toRadians(this.client.player.getYaw() + 90);
        double xOffset = Math.sin(angle) - (Math.sin(angle90) * 2);
        double zOffset = - Math.cos(angle) + (Math.cos(angle90) * 2);
        ArmorStandEntity cameraEntity = new ArmorStandEntity(
                this.client.player.getEntityWorld(),
                this.client.player.getX() + xOffset,
                this.client.player.getY(),
                this.client.player.getZ() + zOffset
        );
        cameraEntity.setCustomName(PRESENTING_CAMERA);
        //cameraEntity.setYaw(this.client.player.getYaw() - 180);
        //cameraEntity.setPitch(this.client.player.getPitch() - 180);
        cameraEntity.setHeadYaw(this.client.player.getHeadYaw());
        //this.client.player.setHeadYaw(headYaw);

        this.client.player.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, cameraEntity.getPos());
        this.client.player.setYaw(this.client.player.getYaw() - 90);
        this.client.player.setBodyYaw(this.client.player.getYaw());
        this.client.player.setPitch(-10);
        cameraEntity.setInvisible(true);
        cameraEntity.setNoGravity(true);
//        Vec3d lookVec =
//        this.client.player.getEyePos().add(this.client.player.getCameraPosVec(0));
//        cameraEntity.
//        float yaw = this.client.player.getYaw();
//        double distance = 4.0; // Distance behind player
//        double height = 1.5;   // Height above player
//
//        // Convert angle to radians
//        double radians = Math.toRadians(yaw);
//
//        // Calculate offset
//        double offsetX = -Math.sin(radians) * distance;
//        double offsetZ = -Math.cos(radians) * distance;
//
//        // Set camera entity position
//        cameraEntity.setPos(
//                this.client.player.getX() + offsetX,
//                this.client.player.getY() + height,
//                this.client.player.getZ() + offsetZ
//        );


        // Add to world client-side only
        this.client.world.addEntity(cameraEntity);
        //cameraEntity.lookAt(EntityAnchorArgumentType.EntityAnchor.EYES, this.client.player.getEyePos());
        this.client.setCameraEntity(cameraEntity);
        this.card = this.handler.fishingCard();
        int currentExp = this.card.getExp();
        this.fishExp = this.handler.exp();
        this.initialLevel = currentExp < fishExp ? this.card.getLevel() - 1 : this.card.getLevel();
        this.initialExp = currentExp < fishExp ? CardProgression.levelExp(this.initialLevel) + (this.card.getExp() - fishExp) : this.card.getExp() - fishExp;
    }

    @Override
    public void removed() {
        super.removed();
        this.client.setCameraEntity(this.client.player);
    }

    @Override
    protected void init() {
        super.init();
        Window window = this.client.getWindow();
        this.x = (int) MathHelper.clamp(window.getScaledWidth() * 0.5 - 300, 0, window.getScaledWidth() - 180);
        this.y = (int) MathHelper.clamp(window.getScaledHeight() * 0.5 - 100, 0, window.getScaledHeight() - 100);
        this.textStartX = this.x + 20;
        this.textEndX = this.textStartX + 170;
        this.textY = this.y + 20;
        this.expX = this.x + 12;
        this.expY = this.y + 166;
        this.specimenDescription = SpecimenDescription.of(this.textRenderer, this.handler.fish()).get();
    }

    private int getExpForTick() {
        int currentExp = (int) (this.initialExp + this.fishExp * ((EXP_ANIMATION_LENGTH - this.expAnimationTick) * 1f / EXP_ANIMATION_LENGTH));
        int currentLevelUpExp = CardProgression.levelExp(this.initialLevel);
        if (currentExp > currentLevelUpExp) {
            return currentExp - currentLevelUpExp;
        }
        return currentExp;
    }

    private int getLevelForTick() {
        int currentExp = this.initialExp + this.fishExp * ((EXP_ANIMATION_LENGTH - this.expAnimationTick) / EXP_ANIMATION_LENGTH);
        int currentLevelUpExp = CardProgression.levelExp(this.initialLevel);
        if (currentExp > currentLevelUpExp) {
            return this.card.getLevel();
        }
        return this.initialLevel;

    }

    @Override
    protected void handledScreenTick() {
        double angle = Math.toRadians(this.client.player.getYaw());
        double angle90 = Math.toRadians(this.client.player.getYaw() + 90);
        int stage = this.handler.stage.get();
        double zoom = (stage * -1f) / FishingPostScreenHandler.LAST_STAGE;
        double xOffset = ((- Math.sin(angle) * zoom) - (Math.sin(angle90) * 2));
        double zOffset = ((Math.cos(angle) * zoom) + (Math.cos(angle90) * 2));
        this.client.cameraEntity.setPos(
                this.client.player.getX() + xOffset,
                this.client.player.getY(),
                this.client.player.getZ() + zOffset
        );

        if (this.handler.stage.get() < FishingPostScreenHandler.LAST_STAGE) {
            return;
        }

        this.tickTextAnimation();
        this.tickExpAnimation();
    }

    private void tickExpAnimation() {
        if (this.animationTick > 0) {
            return;
        }
        if (this.expAnimationTick == 0) {
            return;
        }
        this.expAnimationTick--;
    }

    private void tickTextAnimation() {
        if (this.animationTick == 0) {
            return;
        }
        this.animationTick--;

    }


    private void renderLine(DrawContext context, SpecimenDescription.Line line,  int index) {
        int currentTextY = this.textY + index * 13;
        int textRightX = this.textEndX - this.client.textRenderer.getWidth(line.getRight());
        drawTextOutline(textRenderer, context, line.getLeft(), this.textStartX, currentTextY, OUTLINE_COLOR, LINE_COLOR);
        drawTextOutline(textRenderer, context, line.getRight(), textRightX, currentTextY, OUTLINE_COLOR, LINE_COLOR);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(RenderLayer::getGuiTextured, BOOK_TEXTURE, this.x, this.y, 0, 0, 126, 190, 256, 256);
        context.drawTexture(RenderLayer::getGuiTextured, BOOK_TEXTURE, this.x + 100, this.y, 40, 0, 166, 190, 256, 256);
        if (this.client == null) {
            return;
        }
        for(int i = 0; i < (this.handler.stage.get() == FishingPostScreenHandler.LAST_STAGE ? this.specimenDescription.size() : this.handler.stage.get()); i++) {
            this.renderLine(context, this.specimenDescription.get(i), i);
        }
        this.renderExp(context);
    }

    private void renderExp(DrawContext context) {
        if ( this.animationTick > 0) {
            return;
        }
        this.renderExpBar(context);
        this.renderExpInfo(context);
    }

    private void renderExpBar(DrawContext context) {
        int barLength = (int) (((float) getExpForTick() / CardProgression.levelExp(this.getLevelForTick())) * 183.0F);
        context.drawGuiTexture(RenderLayer::getGuiTextured, EXPERIENCE_BAR_BACKGROUND_TEXTURE, 182, 5, 0, 0, this.expX, this.expY, 182, 5);
        if (barLength > 0) {
            RenderSystem.setShaderColor(1, 0.5f, 0, 1);
            context.drawGuiTexture(RenderLayer::getGuiTextured, EXPERIENCE_BAR_PROGRESS_TEXTURE, 182, 5, 0, 0, this.expX, this.expY, (int) (this.card.getExpProgress() * 183F), 5);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            context.drawGuiTexture(RenderLayer::getGuiTextured, EXPERIENCE_BAR_PROGRESS_TEXTURE, 182, 5, 0, 0, this.expX, this.expY, barLength, 5);
        }
    }

    private void renderExpInfo(DrawContext context) {
        int level = this.getLevelForTick();
        drawTextOutline(this.textRenderer, context, toOrderedText(this.textRenderer, Text.of("Level: " + level)), this.expX + 8, this.expY - 23, 8453920, 0);
        drawTextOutline(this.textRenderer, context,toOrderedText(this.textRenderer, Text.of(this.getExpForTick() + " / " + CardProgression.levelExp(level))), this.expX + 8, this.expY - 10, 8453920, 0);
        OrderedText expText = toOrderedText(this.textRenderer, Text.of("Exp: " + fishExp));
        drawTextOutline(this.textRenderer, context,expText, this.expX + 178 - this.textRenderer.getWidth(expText), this.expY - 10, 8453920, 0);
    }
    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {}

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_SPACE) {
            if (this.handler.isLastStage()) {
                this.client.options.setPerspective(this.previousPerspective);
            }
            ClientPlayNetworking.send(new FishingInputKeyboardPayload(true));
            return true;
        }
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            ClientPlayNetworking.send(new FishingInputKeyboardPayload(false));
            this.client.options.setPerspective(this.previousPerspective);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}

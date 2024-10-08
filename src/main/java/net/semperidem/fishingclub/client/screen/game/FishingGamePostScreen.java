package net.semperidem.fishingclub.client.screen.game;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.Window;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.specimen.SpecimenDescription;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.managers.ProgressionManager;
import net.semperidem.fishingclub.network.payload.FishingGameInputKeyboardPayload;
import net.semperidem.fishingclub.screen.fishing_game_post.FishingGamePostScreenHandler;

import java.util.ArrayList;
import java.util.List;

import static net.semperidem.fishingclub.util.TextUtil.*;

public class FishingGamePostScreen extends HandledScreen<FishingGamePostScreenHandler> implements ScreenHandlerProvider<FishingGamePostScreenHandler> {
    private static final Identifier BOOK_TEXTURE = FishingClub.identifier("textures/gui/page.png");
    private final Perspective previousPerspective;
    private static final Identifier EXPERIENCE_BAR_BACKGROUND_TEXTURE = Identifier.ofVanilla("hud/experience_bar_background");
    private static final Identifier EXPERIENCE_BAR_PROGRESS_TEXTURE = Identifier.ofVanilla("hud/experience_bar_progress");

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
    private final FishingCard fishingCard;
    int initialLevel;
    int initialExp;
    int fishExp;

    public FishingGamePostScreen(FishingGamePostScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.previousPerspective = MinecraftClient.getInstance().options.getPerspective();
        MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_FRONT);
        this.fishingCard = this.handler.fishingCard();
        int currentExp = this.fishingCard.getExp();
        this.fishExp = this.handler.exp();
        this.initialLevel = currentExp < fishExp ? this.fishingCard.getLevel() - 1 : this.fishingCard.getLevel();
        this.initialExp = currentExp < fishExp ? ProgressionManager.levelExp(this.initialLevel) + (this.fishingCard.getExp() - fishExp) : this.fishingCard.getExp() - fishExp;
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
        int currentLevelUpExp = ProgressionManager.levelExp(this.initialLevel);
        if (currentExp > currentLevelUpExp) {
            return currentExp - currentLevelUpExp;
        }
        return currentExp;
    }

    private int getLevelForTick() {
        int currentExp = this.initialExp + this.fishExp * ((EXP_ANIMATION_LENGTH - this.expAnimationTick) / EXP_ANIMATION_LENGTH);
        int currentLevelUpExp = ProgressionManager.levelExp(this.initialLevel);
        if (currentExp > currentLevelUpExp) {
            return this.fishingCard.getLevel();
        }
        return this.initialLevel;

    }

    @Override
    protected void handledScreenTick() {
        if (this.handler.stage.get() < FishingGamePostScreenHandler.LAST_STAGE) {
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
        context.drawTexture(BOOK_TEXTURE, this.x, this.y, 0, 0, 126, 190, 256, 256);
        context.drawTexture(BOOK_TEXTURE, this.x + 100, this.y, 40, 0, 166, 190, 256, 256);
        if (this.client == null) {
            return;
        }
        for(int i = 0; i < (this.handler.stage.get() == FishingGamePostScreenHandler.LAST_STAGE ? this.specimenDescription.size() : this.handler.stage.get()); i++) {
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
        int barLength = (int) (((float) getExpForTick() / ProgressionManager.levelExp(this.getLevelForTick())) * 183.0F);
        RenderSystem.enableBlend();
        context.drawGuiTexture(EXPERIENCE_BAR_BACKGROUND_TEXTURE, 182, 5, 0, 0, this.expX, this.expY, 182, 5);
        if (barLength > 0) {
            RenderSystem.setShaderColor(1, 0.5f, 0, 1);
            context.drawGuiTexture(EXPERIENCE_BAR_PROGRESS_TEXTURE, 182, 5, 0, 0, this.expX, this.expY, (int) (this.fishingCard.getExpProgress() * 183F), 5);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            context.drawGuiTexture(EXPERIENCE_BAR_PROGRESS_TEXTURE, 182, 5, 0, 0, this.expX, this.expY, barLength, 5);
        }
        RenderSystem.disableBlend();
    }

    private void renderExpInfo(DrawContext context) {
        int level = this.getLevelForTick();
        drawTextOutline(this.textRenderer, context, toOrderedText(this.textRenderer, Text.of("Level: " + level)), this.expX + 8, this.expY - 23, 8453920, 0);
        drawTextOutline(this.textRenderer, context,toOrderedText(this.textRenderer, Text.of(this.getExpForTick() + " / " + ProgressionManager.levelExp(level))), this.expX + 8, this.expY - 10, 8453920, 0);
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
            ClientPlayNetworking.send(new FishingGameInputKeyboardPayload(true));
            return true;
        }
        if (keyCode == InputUtil.GLFW_KEY_ESCAPE) {
            ClientPlayNetworking.send(new FishingGameInputKeyboardPayload(false));
            this.client.options.setPerspective(this.previousPerspective);
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}

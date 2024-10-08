package net.semperidem.fishingclub.client.screen.game;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.fish.specimen.SpecimenDescription;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.fisher.managers.ProgressionManager;
import net.semperidem.fishingclub.fisher.managers.StatusEffectHelper;
import net.semperidem.fishingclub.network.payload.FishingGameInputKeyboardPayload;
import net.semperidem.fishingclub.screen.fishing_game_post.FishingGamePostScreenHandler;

import java.util.ArrayList;
import java.util.List;

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
    private List<SpecimenDescription.Line> specimenDescription;
    private FishingCard fishingCard;
    int initialLevel;
    int initialExp;
    int fishExp;
    public FishingGamePostScreen(FishingGamePostScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.previousPerspective = MinecraftClient.getInstance().options.getPerspective();
        MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_FRONT);
        /**
         * Name
         * Weight
         * Length
         * Quality
         * Exp to player(with animation)
         * Date and Who caught with Exp
         * */

        this.specimenDescription = SpecimenDescription.of(this.handler.fish()).get();
        this.fishingCard = this.handler.fishingCard();
        int currentExp = this.fishingCard.getExp();
        this.initialLevel = currentExp < fishExp ? this.fishingCard.getLevel() - 1 : this.fishingCard.getLevel();
        this.initialExp = currentExp < fishExp ? ProgressionManager.levelExp(this.initialLevel) + (this.fishingCard.getExp() - fishExp) : this.fishingCard.getExp() - fishExp;
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

    @Override
    protected void init() {
        super.init();
        this.x = (int) (this.client.getWindow().getScaledWidth() * 0.5 - 300);
        this.y = (int) (this.client.getWindow().getScaledHeight() * 0.5 - 100);
        this.textStartX = this.x + 20;
        this.textEndX = this.textStartX + 160;
        this.textY = this.y + 20;
        this.expX = this.x + 12;
        this.expY = this.y + 166;
    }

    private void renderLine(DrawContext context, Text left, Text right, int index, boolean isNew) {
        renderThickLine(context, left, index, this.textStartX);
        int textRightX = this.textEndX - this.client.textRenderer.getWidth(right);
        //if (isNew && this.animationTick > 0 && !left.getString().contains("Quality")) {
          //  renderThickLine(context, right, index, textRightX);
//        } else
//        {
        renderThickLine(context, right, index, textRightX);
            //context.drawText(this.client.textRenderer, right, textRightX, this.textY + index * 20, LINE_COLOR, false);
//        }
    }

    private void renderThickLine(DrawContext context, Text text, int index, int textX) {
        context.drawText(this.client.textRenderer, text.copyContentOnly(), textX + 1, this.textY + 1 + index * 20, LINE_COLOR, false);
        context.drawText(this.client.textRenderer, text.copyContentOnly(), textX - 1, this.textY - 1 + index * 20, LINE_COLOR, false);
        context.drawText(this.client.textRenderer, text.copyContentOnly(), textX - 1, this.textY + 1 + index * 20, LINE_COLOR, false);
        context.drawText(this.client.textRenderer, text.copyContentOnly(), textX + 1, this.textY - 1 + index * 20, LINE_COLOR, false);
        context.drawText(this.client.textRenderer, text.copyContentOnly(), textX + 1, this.textY + index * 20, LINE_COLOR, false);
        context.drawText(this.client.textRenderer, text.copyContentOnly(), textX - 1, this.textY + index * 20, LINE_COLOR, false);
        context.drawText(this.client.textRenderer, text.copyContentOnly(), textX, this.textY + 1 + index * 20, LINE_COLOR, false);
        context.drawText(this.client.textRenderer, text.copyContentOnly(), textX, this.textY - 1 + index * 20, LINE_COLOR, false);
        context.drawText(this.client.textRenderer, text, textX, this.textY + index * 20, OUTLINE_COLOR, false);
    }

    private void renderLevel(DrawContext context, String level, int levelX, int levelY){
        int levelWidth = (int) (this.client.textRenderer.getWidth(level) * 0.5f);
        context.drawText(this.client.textRenderer, level, levelX - levelWidth - 1, levelY, 0, false);
        context.drawText(this.client.textRenderer, level, levelX - levelWidth + 1, levelY, 0, false);
        context.drawText(this.client.textRenderer, level, levelX - levelWidth, levelY - 1, 0, false);
        context.drawText(this.client.textRenderer, level, levelX - levelWidth, levelY - 1, 0, false);
        context.drawText(this.client.textRenderer, level, levelX - levelWidth, levelY, 8453920, false);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(BOOK_TEXTURE, this.x, this.y, 20, 1, 146, 180, 256, 256);
        context.drawTexture(BOOK_TEXTURE, this.x + 100, this.y, 60, 1, 186, 180, 256, 256);
        context.drawText(this.client.textRenderer, String.valueOf(this.handler.stage.get()), 20, 20, 0xFF000000, false);
        int lastIndex = this.handler.stage.get() == FishingGamePostScreenHandler.LAST_STAGE ? this.specimenDescription.size() : this.handler.stage.get();
        boolean isTextInprogress = this.animationTick > 0;
        for(int i = 0; i < lastIndex; i++) {
            this.renderLine(context, this.specimenDescription.get(i).getLeft(), this.specimenDescription.get(i).getRight(), i, i + 1 >= this.handler.stage.get());
        }

        if (isTextInprogress) {
            return;
        }
        int k = (int)((float) getExpForTick() / ProgressionManager.levelExp(this.getLevelForTick()) * 183.0F);
        int newExp = (int) (this.fishingCard.getExpProgress() * 183F);
        RenderSystem.enableBlend();
        context.drawGuiTexture(EXPERIENCE_BAR_BACKGROUND_TEXTURE, 182, 5, 0, 0, this.expX, this.expY, 182, 5);
        if (k > 0) {
            RenderSystem.setShaderColor(1, 0.5f, 0, 1);
            context.drawGuiTexture(EXPERIENCE_BAR_PROGRESS_TEXTURE, 182, 5, 0, 0, this.expX, this.expY, newExp, 5);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            context.drawGuiTexture(EXPERIENCE_BAR_PROGRESS_TEXTURE, 182, 5, 0, 0, this.expX, this.expY, k, 5);
        }
        String level = String.valueOf(this.getLevelForTick());
        this.renderLevel(context, level, this.expX + 91, this.expY - 5);
        this.renderLevel(context, "+" + this.handler.fish().experience(1), this.expX + 175, this.expY - 8);
        RenderSystem.disableBlend();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }

    @Override
    public void close() {
        super.close();
    }

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


    @Override
    public boolean shouldCloseOnEsc() {
       return true;
    }
}

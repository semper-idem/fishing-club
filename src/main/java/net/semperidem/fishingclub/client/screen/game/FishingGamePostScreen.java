package net.semperidem.fishingclub.client.screen.game;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.network.payload.FishingGameInputKeyboardPayload;
import net.semperidem.fishingclub.screen.fishing_game_post.FishingGamePostScreenHandler;

public class FishingGamePostScreen extends HandledScreen<FishingGamePostScreenHandler> implements ScreenHandlerProvider<FishingGamePostScreenHandler> {
    private static final Identifier BOOK_TEXTURE = Identifier.of("textures/gui/book.png");
    private final Perspective previousPerspective;

    public FishingGamePostScreen(FishingGamePostScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.previousPerspective = MinecraftClient.getInstance().options.getPerspective();
        MinecraftClient.getInstance().options.setPerspective(Perspective.THIRD_PERSON_FRONT);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTexture(BOOK_TEXTURE, 0, 0, 20, 1, 146, 180, 256, 256);
        context.drawText(this.client.textRenderer, String.valueOf(this.handler.stage.get()), 20, 20, 0xFF000000, false);
        /**
         * Name
         * Weight
         * Length
         * Quality
         * Exp to player(with animation)
         * Date and Who caught with Exp
         * */
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

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
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

}

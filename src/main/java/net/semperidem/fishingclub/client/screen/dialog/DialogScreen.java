package net.semperidem.fishingclub.client.screen.dialog;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.screen.dialog.DialogNode;
import net.semperidem.fishingclub.screen.dialog.DialogScreenHandler;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;

public class DialogScreen extends HandledScreen<DialogScreenHandler> implements ScreenHandlerProvider<DialogScreenHandler> {

    int x;
    int y;
    private final DialogBox dialogBox;

    public DialogScreen(DialogScreenHandler dialogScreenHandler, PlayerInventory playerInventory, Text title) {
        super(dialogScreenHandler, playerInventory, title);
        dialogBox = new DialogBox(
                this.x + TILE_SIZE * 10,
                this.y + TILE_SIZE,
                TEXTURE.textureWidth  - TILE_SIZE * 11,
                TEXTURE.textureHeight  - TILE_SIZE * 2
        );
        DialogNode startingNode = DialogUtil.getStartQuestion(getScreenHandler().getOpeningKeys());
        dialogBox.addMessage(startingNode);
     }


    private void addPlayerFaceComponent() {
        addDrawable(new PlayerFaceComponent(
                MinecraftClient.getInstance().player.getSkinTextures().texture(),
                x + TILE_SIZE,
                y + TEXTURE.renderHeight - TILE_SIZE * 9
        ));
    }

    @Override
    protected void init() {
        super.init();
        this.x = (int) ((width - TEXTURE.renderWidth) * 0.5f);
        this.y = height - TEXTURE.renderHeight;
        this.titleX = x + TEXTURE.renderWidth - 5 * TILE_SIZE;
        this.titleY = y + 13 * TILE_SIZE;
        addPlayerFaceComponent();
        dialogBox.resize(
                this.x + TILE_SIZE * 10,
                this.y + TILE_SIZE,
                TEXTURE.textureWidth  - TILE_SIZE * 11,
                TEXTURE.textureHeight  - TILE_SIZE * 2
        );
        addDrawable(dialogBox);
    }

    @Override
    protected void handledScreenTick() {
        dialogBox.tick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return dialogBox.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_SPACE) {
            dialogBox.finishLine();
            return true;
        } else if (keyCode > InputUtil.GLFW_KEY_0 && keyCode <= InputUtil.GLFW_KEY_0 + dialogBox.response.questions.size()) {
            dialogBox.addMessage(dialogBox.response.questions.get(keyCode - InputUtil.GLFW_KEY_0 - 1));
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {

    }

    public static final Texture TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/derek_dialog.png"),
            420,
            120
    );
    public static final int TILE_SIZE = 4;

}

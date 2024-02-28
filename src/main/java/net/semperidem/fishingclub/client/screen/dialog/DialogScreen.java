package net.semperidem.fishingclub.client.screen.dialog;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;

import java.awt.*;
import java.util.Set;

public class DialogScreen extends HandledScreen<DialogScreenHandler> implements ScreenHandlerProvider<DialogScreenHandler> {
    private static final Texture TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/derek_dialog.png"),
            420,
            120
    );
    private static final int tileSize = 4;

    private int x, y;
    private int titleX, titleY;
    private DialogBox dialogBox;
    private final Set<String> dialogOpeningKey;



    public DialogScreen(DialogScreenHandler dialogScreenHandler, PlayerInventory playerInventory, Text title) {
        super(dialogScreenHandler, playerInventory, title);
        this.dialogOpeningKey = handler.getOpeningKeys();
     }


    private void addPlayerFaceComponent() {
        addDrawable(new PlayerFaceComponent(
                MinecraftClient.getInstance().player.getSkinTexture(),
                x + tileSize,
                y + TEXTURE.renderHeight - tileSize * 9
        ));
    }

    @Override
    protected void init() {
        super.init();
        this.x = (int) ((width - TEXTURE.renderWidth) * 0.5f);
        this.y = height - TEXTURE.renderHeight;
        this.titleX = x + TEXTURE.renderWidth - 5 * tileSize;
        this.titleY = y + 13 * tileSize;
        addPlayerFaceComponent();
        addDrawable(dialogBox = new DialogBox(
                x + tileSize * 10,
                y + tileSize,
                TEXTURE.textureWidth  - tileSize * 11,
                TEXTURE.textureHeight  - tileSize * 2,
                textRenderer
        ));
        DialogNode startingNode = DialogHelper.getStartQuestion(dialogOpeningKey);
        dialogBox.addMessage(startingNode);
    }

    @Override
    protected void handledScreenTick() {
        dialogBox.tick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return dialogBox.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == InputUtil.GLFW_KEY_SPACE) {
            dialogBox.finishLine();
            return true;
        } else if (keyCode > InputUtil.GLFW_KEY_0 && keyCode <= InputUtil.GLFW_KEY_0 + dialogBox.response.questions.size()) {
            dialogBox.addMessage(dialogBox.response.questions.get(keyCode - InputUtil.GLFW_KEY_0 - 1));
            return true;
        } else {
            return super.keyPressed(keyCode, scanCode, modifiers);
        }
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        TEXTURE.render(matrixStack, x,y);
        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, float delta, int mouseX, int mouseY) {
    }

    @Override
    public void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
        drawCenteredText(matrixStack, textRenderer, title, titleX, titleY, Color.WHITE.getRGB());
    }
}

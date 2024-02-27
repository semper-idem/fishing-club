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
import net.semperidem.fishingclub.entity.FishermanEntity;

import java.awt.*;
import java.util.Set;

public class DialogScreen extends HandledScreen<DialogScreenHandler> implements ScreenHandlerProvider<DialogScreenHandler> {
    private static final Texture TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/derek_dialog.png"),
            420,
            180
    );
    private static final int tileSize = 4;


    private static final String GOLDEN = "GOLDEN";
    private static final String GRADE = "GRADE";
    private static final String SPELL = "SPELL";
    private static final String MEMBER = "MEMBER";
    private static final String NOT_MEMBER = "NOT_MEMBER";
    private static final String UNIQUE = "UNIQUE";
    private static final String NOT_UNIQUE = "NOT_UNIQUE";
    private static final String SUMMONER = "SUMMONER";
    private static final String NOT_SUMMONER = "NOT_SUMMONER";
    private static final String REPEATED = "REPEATED";
    private static final String NOT_REPEATED = "NOT_REPEATED";
    private static final String WELCOME = "WELCOME";
    private static final String NOT_WELCOME = "NOT_WELCOME";

    private int x, y;
    private int titleX, titleY;
    private DialogBox dialogBox;
    private FishermanEntity fishermanEntity;

    public DialogScreen(DialogScreenHandler dialogScreenHandler, PlayerInventory playerInventory, Text title) {
        super(dialogScreenHandler, playerInventory, title);
        this.fishermanEntity = fishermanEntity;
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
                x + tileSize * 11,
                y + tileSize * 2,
                TEXTURE.textureWidth  - tileSize * 13,
                TEXTURE.textureHeight  - tileSize * 12,
                textRenderer
        ));
        DialogNode startingNode = DialogHelper.getStartQuestion(Set.of(GOLDEN, NOT_WELCOME, NOT_REPEATED, NOT_MEMBER, UNIQUE, SUMMONER));
        dialogBox.addMessage(startingNode);
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

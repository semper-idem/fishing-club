package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.semperidem.fishingclub.screen.dialog.DialogKey;
import net.semperidem.fishingclub.screen.dialog.DialogNode;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TEXTURE;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TILE_SIZE;


public class DialogScreen implements MemberSubScreen {
    MemberScreen parent;
    private final DialogBox dialogBox;

    public DialogScreen(MemberScreen parent, Set<DialogKey> dialogOpeningKey) {
        this.parent = parent;
        dialogBox = new DialogBox(
                parent.x + TILE_SIZE * 10,
                parent.y + TILE_SIZE,
                TEXTURE.textureWidth  - TILE_SIZE * 11,
                TEXTURE.textureHeight  - TILE_SIZE * 2
        );
        DialogNode startingNode = DialogUtil.getStartQuestion(dialogOpeningKey);
        dialogBox.addMessage(startingNode);
    }

    public ArrayList<Drawable> getComponents() {
        return new ArrayList<>(List.of(dialogBox));
    }

    public void init() {
        dialogBox.resize(
                parent.x + TILE_SIZE * 10,
                parent.y + TILE_SIZE,
                TEXTURE.textureWidth  - TILE_SIZE * 11,
                TEXTURE.textureHeight  - TILE_SIZE * 2
        );
    }

    public void handledScreenTick() {
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
        }
        return false;
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {

    }

}

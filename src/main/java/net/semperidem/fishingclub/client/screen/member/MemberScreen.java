package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.screen.dialog.DialogKey;
import net.semperidem.fishingclub.screen.dialog.DialogNode;
import net.semperidem.fishingclub.screen.dialog.MemberScreenHandler;
import net.semperidem.fishingclub.screen.dialog.DialogUtil;

import java.awt.*;
import java.util.Set;

public class MemberScreen extends HandledScreen<MemberScreenHandler> implements ScreenHandlerProvider<MemberScreenHandler> {
    static final Texture TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/derek_dialog.png"),
            420,
            120
    );
    static final int TILE_SIZE = 4;

    int x;
    int y;
    private int titleX, titleY;
    DialogScreen dialogScreen;
    private MemberSubScreen currentView;


    public MemberScreen(MemberScreenHandler dialogScreenHandler, PlayerInventory playerInventory, Text title) {
        super(dialogScreenHandler, playerInventory, title);
        this.dialogScreen = new DialogScreen(this, getScreenHandler().getOpeningKeys());
        this.currentView  = this.dialogScreen;
     }


    private void addPlayerFaceComponent() {
        addDrawable(new PlayerFaceComponent(
                MinecraftClient.getInstance().player.getSkinTexture(),
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
        for(Drawable components : currentView.getComponents()) {
            addDrawable(components);
        }
        currentView.init();
    }

    @Override
    protected void handledScreenTick() {
        currentView.handledScreenTick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return currentView.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return currentView.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        currentView.render(matrixStack, mouseX, mouseY, delta);
        super.render(matrixStack, mouseX, mouseY, delta);
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, float delta, int mouseX, int mouseY) {
        TEXTURE.render(matrixStack, x , y);
    }

    @Override
    public void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
    }
}

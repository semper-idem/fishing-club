package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.client.screen.dialog.PlayerFaceComponent;
import net.semperidem.fishingclub.screen.dialog.DialogScreenHandler;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;

public class MemberScreen extends HandledScreen<MemberScreenHandler> implements ScreenHandlerProvider<MemberScreenHandler> {

    int x;
    int y;
    private MemberSubScreen currentView;
    private TabButtonWidget shopButton;
    private TabButtonWidget fireworksButton;
    private TabButtonWidget boxesButton;
    private TabButtonWidget miscButton;



    public MemberScreen(MemberScreenHandler memberScreenHandler, PlayerInventory playerInventory, Text title) {
        super(memberScreenHandler, playerInventory, title);
        shopButton = new TabButtonWidget(0,0, 1, 1, "Trade", this, new MemberShopScreen());
        fireworksButton = new TabButtonWidget(0,0, 1, 1, "Fireworks", this, new MemberFireworkScreen());
        boxesButton = new TabButtonWidget(0,0, 1, 1, "Black Market", this, new MemberIllegalScreen());
        miscButton = new TabButtonWidget(0,0, 1, 1, "Misc", this, new MemberMiscScreen());
    }

    public MemberSubScreen getCurrentView() {
        return currentView;
    }

    public void setCurrentView(MemberSubScreen memberSubScreen) {
        this.currentView = memberSubScreen;
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
        addDrawable(shopButton);
        addDrawable(miscButton);
        addDrawable(boxesButton);
        addDrawable(fireworksButton);
        shopButton.resize(0,0,1,1);
        miscButton.resize(0,0,1,1);
        boxesButton.resize(0,0,1,1);
        fireworksButton.resize(0,0,1,1);
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

    static final Texture TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/derek_dialog.png"),
            420,
            120
    );
    static final int TILE_SIZE = 4;

}

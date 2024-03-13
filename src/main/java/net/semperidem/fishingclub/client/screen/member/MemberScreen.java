package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.client.screen.dialog.PlayerFaceComponent;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;

public class MemberScreen extends HandledScreen<MemberScreenHandler> implements ScreenHandlerProvider<MemberScreenHandler> {

    int x;
    int y;
    private IMemberSubScreen currentView;
    private TabButtonWidget shopButton;
    private TabButtonWidget fireworksButton;
    private TabButtonWidget boxesButton;
    private TabButtonWidget miscButton;
    private TabButtonWidget flipButton;
    private Text shopTitle = Text.of("Trade");
    private Text miscTitle = Text.of("Misc");
    private Text fireworksTitle = Text.of("Fireworks");
    private Text flipTitle = Text.of("Coin Toss");
    private Text boxesTitle = Text.of("Black Market");




    public MemberScreen(MemberScreenHandler memberScreenHandler, PlayerInventory playerInventory, Text title) {
        super(memberScreenHandler, playerInventory, title);
        int buttonX = x + TEXTURE.textureWidth - buttonWidth - TILE_SIZE;
        int nextButtonY = y + TILE_SIZE * 2 + 2;
        textRenderer = MinecraftClient.getInstance().textRenderer;
        shopButton = new TabButtonWidget(buttonX,nextButtonY, buttonWidth, buttonHeight, shopTitle, this, new MemberShopScreen());
        nextButtonY += shopButton.getHeight();
        fireworksButton = new TabButtonWidget(buttonX,nextButtonY, buttonWidth, buttonHeight, fireworksTitle, this, new MemberFireworkScreen());
        nextButtonY += fireworksButton.getHeight();
        boxesButton = new TabButtonWidget(buttonX,nextButtonY, buttonWidth, buttonHeight, boxesTitle, this, new MemberIllegalScreen());
        nextButtonY += boxesButton.getHeight();
        miscButton = new TabButtonWidget(buttonX,nextButtonY, buttonWidth, buttonHeight, miscTitle, this, new MemberMiscScreen());
        nextButtonY += miscButton.getHeight();
        flipButton = new TabButtonWidget(buttonX,nextButtonY, buttonWidth, buttonHeight, flipTitle, this, new MemberFlipScreen(this));
        currentView = new MemberFlipScreen(this);
    }

    public IMemberSubScreen getCurrentView() {
        return currentView;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public void setCurrentView(IMemberSubScreen memberSubScreen) {
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
        addPlayerFaceComponent();
        addDrawable(shopButton);
        addDrawable(miscButton);
        addDrawable(boxesButton);
        addDrawable(fireworksButton);
        addDrawable(flipButton);
        int buttonX = x + TEXTURE.textureWidth - buttonWidth - TILE_SIZE;
        int nextButtonY = y + TILE_SIZE * 2 + 2;
        shopButton.resize(buttonX,nextButtonY,buttonWidth,buttonHeight);
        nextButtonY += shopButton.getHeight();
        miscButton.resize(buttonX,nextButtonY,buttonWidth,buttonHeight);
        nextButtonY += miscButton.getHeight();
        boxesButton.resize(buttonX,nextButtonY,buttonWidth,buttonHeight);
        nextButtonY += boxesButton.getHeight();
        fireworksButton.resize(buttonX,nextButtonY,buttonWidth,buttonHeight);
        nextButtonY += fireworksButton.getHeight();
        flipButton.resize(buttonX,nextButtonY,buttonWidth,buttonHeight);
        currentView.init();
        for(Drawable components : currentView.getComponents()) {
            addDrawable(components);
        }
    }

    @Override
    protected void handledScreenTick() {
        currentView.handledScreenTick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return currentView.mouseScrolled(mouseX, mouseY, amount) || super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return currentView.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return currentView.charTyped(chr, modifiers) || super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return currentView.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        super.render(matrixStack, mouseX, mouseY, delta);
        currentView.render(matrixStack, mouseX, mouseY, delta);
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
    private static final int buttonWidth = TILE_SIZE * 18;
    private static final int buttonHeight = TILE_SIZE * 5;

}

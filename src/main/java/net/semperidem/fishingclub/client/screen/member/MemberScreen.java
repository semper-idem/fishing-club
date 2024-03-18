package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.client.screen.dialog.PlayerFaceComponent;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;

import java.awt.*;

import static net.semperidem.fishingclub.util.TextUtil.drawOutlinedTextRightAlignedTo;

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
    private Text miscTitle = Text.of("Special Services");
    private Text fireworksTitle = Text.of("Fireworks");
    private Text flipTitle = Text.of("Coin Toss");
    private Text boxesTitle = Text.of("Black Market");

    public static final int CREDIT_COLOR = 0x618853;
    public static final int CREDIT_OUTLINE_COLOR = new Color(CREDIT_COLOR).darker().darker().getRGB();
    public static final int BEIGE_TEXT_COLOR = 0xffeace;
    public static final int WHITE_TEXT_COLOR = 0xdfe0df;

    Text creditText;
    int creditX, creditY;



    public MemberScreen(MemberScreenHandler memberScreenHandler, PlayerInventory playerInventory, Text title) {
        super(memberScreenHandler, playerInventory, title);
        int buttonX = x + TEXTURE.textureWidth - BUTTON_WIDTH - TILE_SIZE;
        int nextButtonY = y + TILE_SIZE * 2 + 2;
        textRenderer = MinecraftClient.getInstance().textRenderer;
        shopButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, shopTitle, this, new MemberShopScreen(this));
        nextButtonY += shopButton.getHeight();
        fireworksButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, fireworksTitle, this, new MemberFireworkScreen(this));
        nextButtonY += fireworksButton.getHeight();
        boxesButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, boxesTitle, this, new MemberIllegalScreen(this));
        nextButtonY += boxesButton.getHeight();
        miscButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, miscTitle, this, new MemberMiscScreen(this));
        nextButtonY += miscButton.getHeight();
        flipButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, flipTitle, this, new MemberFlipScreen(this));
        this.creditText = Text.literal("Credit: " + getScreenHandler().getCard().getCredit() + "$");

        currentView = new MemberFlipScreen(this);
    }

    public IMemberSubScreen getCurrentView() {
        return currentView;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public void setCurrentView(IMemberSubScreen memberSubScreen) {
        memberSubScreen.init();
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
        addDrawableChild(shopButton);
        addDrawableChild(miscButton);
        addDrawableChild(boxesButton);
        addDrawableChild(fireworksButton);
        addDrawableChild(flipButton);
        int buttonX = x + TEXTURE.textureWidth - BUTTON_WIDTH - TILE_SIZE;
        int nextButtonY = y + TILE_SIZE * 2 + 2;
        shopButton.resize(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        nextButtonY += shopButton.getHeight();
        miscButton.resize(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        nextButtonY += miscButton.getHeight();
        boxesButton.resize(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        nextButtonY += boxesButton.getHeight();
        fireworksButton.resize(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        nextButtonY += fireworksButton.getHeight();
        flipButton.resize(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT);
        creditX = x + TEXTURE.renderWidth - BUTTON_WIDTH - 3 * TILE_SIZE;
        creditY = y +  TILE_SIZE * 2;
        currentView.init();
    }
    @Override
    protected void handledScreenTick() {
        this.creditText = Text.literal("Credit: " + getScreenHandler().getCard().getCredit() + "$");

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
        drawOutlinedTextRightAlignedTo(textRenderer, matrixStack, creditText, creditX, creditY, CREDIT_COLOR, CREDIT_OUTLINE_COLOR);
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
    public static final int BUTTON_WIDTH = TILE_SIZE * 22;
    private static final int BUTTON_HEIGHT = TILE_SIZE * 5;

}

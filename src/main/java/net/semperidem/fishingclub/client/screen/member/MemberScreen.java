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

import static net.semperidem.fishingclub.util.TextUtil.drawOutlinedTextRightAlignedTo;
import static net.semperidem.fishingclub.util.TextUtil.drawTextRightAlignedTo;

public class MemberScreen extends HandledScreen<MemberScreenHandler> implements ScreenHandlerProvider<MemberScreenHandler> {

    int x;
    int y;
    private MemberSubScreen currentView;
    private TabButtonWidget buyButton;
    private TabButtonWidget sellButton;
    private TabButtonWidget fireworksButton;
    private TabButtonWidget boxesButton;
    private TabButtonWidget miscButton;
    private TabButtonWidget flipButton;
    private MemberSubScreen buyView;
    private MemberSubScreen sellView;
    private MemberSubScreen fireworksView;
    private MemberSubScreen boxesView;
    private MemberSubScreen miscView;
    private MemberSubScreen flipView;
    private Text buyTitle = Text.of("Trade - Buy");
    private Text sellTitle = Text.of("Trade - Sell");
    private Text miscTitle = Text.of("Services");
    private Text fireworksTitle = Text.of("Fireworks");
    private Text flipTitle = Text.of("Coin Toss");
    private Text boxesTitle = Text.of("Contraband");

    public static final int CREDIT_COLOR = 0xffcf51;
    public static final int CREDIT_OUTLINE_COLOR = 0x4b2f00;
    public static final int BEIGE_TEXT_COLOR = 0xffeace;
    public static final int WHITE_TEXT_COLOR = 0xdfe0df;

    private final Text creditText = Text.literal("Credit: ");
    private Text creditValue = Text.literal("0$");
    int creditX, creditY;
    int titleX, titleY;



    public MemberScreen(MemberScreenHandler memberScreenHandler, PlayerInventory playerInventory, Text title) {
        super(memberScreenHandler, playerInventory, title);
        this.buyView = new MemberBuyScreen(this, buyTitle);
        this.sellView = new MemberSellScreen(this, sellTitle);
        this.fireworksView = new MemberFireworkScreen(this, fireworksTitle);
        this.boxesView = new MemberIllegalScreen(this, boxesTitle);
        this.miscView = new MemberMiscScreen(this, miscTitle);
        this.flipView = new MemberFlipScreen(this, flipTitle);
        this.currentView = buyView;
    }

    public IMemberSubScreen getCurrentView() {
        return currentView;
    }

    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public void setCurrentView(MemberSubScreen memberSubScreen) {
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
        int buttonX = x + TEXTURE.textureWidth - BUTTON_WIDTH - TILE_SIZE;
        int nextButtonY = y + TILE_SIZE + 2;
        textRenderer = MinecraftClient.getInstance().textRenderer;
        buyButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, buyView);
        nextButtonY += buyButton.getHeight();
        sellButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, sellView);
        nextButtonY += sellButton.getHeight();
        fireworksButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, fireworksView);
        nextButtonY += fireworksButton.getHeight();
        boxesButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, boxesView);
        nextButtonY += boxesButton.getHeight();
        miscButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, miscView);
        nextButtonY += miscButton.getHeight();
        flipButton = new TabButtonWidget(buttonX,nextButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, flipView);
        addDrawableChild(buyButton);
        addDrawableChild(sellButton);
        addDrawableChild(fireworksButton);
        addDrawableChild(boxesButton);
        addDrawableChild(miscButton);
        addDrawableChild(flipButton);
        creditX = x + TEXTURE.renderWidth - BUTTON_WIDTH - 3 * TILE_SIZE;
        creditY = y +  TILE_SIZE * 2 - 1;

        titleX = x + TILE_SIZE * 12;
        titleY = y + TILE_SIZE * 2 - 1;
        currentView.init();
    }
    @Override
    protected void handledScreenTick() {
        this.creditValue = Text.literal(getScreenHandler().getCard().getCredit() + "$");
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
        drawOutlinedTextRightAlignedTo(textRenderer, matrixStack, creditValue, creditX, creditY, 0xffcf51 , 0x4b2f00);//separate text and value
        drawTextRightAlignedTo(textRenderer, matrixStack, creditText, creditX - textRenderer.getWidth(creditValue), creditY, BEIGE_TEXT_COLOR);//separate text and value
    }

    @Override
    protected void drawBackground(MatrixStack matrixStack, float delta, int mouseX, int mouseY) {
        TEXTURE.render(matrixStack, x , y);
        if (currentView == null) {
            return;
        }
        textRenderer.drawWithShadow(matrixStack,  currentView.getTitle(), titleX , titleY, BEIGE_TEXT_COLOR);

    }

    @Override
    public void drawForeground(MatrixStack matrixStack, int mouseX, int mouseY) {
    }

    static final Texture TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/member_screen.png"),
            420,
            120
    );


    static final int TILE_SIZE = 4;
    public static final int BUTTON_WIDTH = TILE_SIZE * 20;
    private static final int BUTTON_HEIGHT = TabButtonWidget.BUTTON_TEXTURE.renderHeight / 3;

}

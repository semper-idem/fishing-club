package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.client.screen.dialog.PlayerFaceComponent;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;

import java.util.function.Supplier;

import static net.semperidem.fishingclub.util.TextUtil.drawOutlinedTextRightAlignedTo;
import static net.semperidem.fishingclub.util.TextUtil.drawTextRightAlignedTo;

public class MemberScreen extends HandledScreen<MemberScreenHandler> implements ScreenHandlerProvider<MemberScreenHandler> {
    int x;
    int y;
    private MemberSubScreen currentView;
    private final MemberSubScreen buyView;
    private final MemberSubScreen sellView;
    private final MemberSubScreen miscView;
    private final MemberSubScreen boxesView;
    private final MemberSubScreen flipView;
    private final MemberSubScreen fireworksView;
    public static final int CREDIT_COLOR = 0xffcf51;
    public static final int CREDIT_OUTLINE_COLOR = 0x4b2f00;
    public static final int PRIMARY_SCROLLBAR_COLOR = 0xffbb8f1b;
    public static final int SECONDARY_SCROLLBAR_COLOR = 0xff4b2f00;
    public static final int BEIGE_TEXT_COLOR = 0xffeace;
    public static final int WHITE_TEXT_COLOR = 0xdfe0df;
    private static final int BOX_COLOR = 0xff272946;
    private static final int OUTLINE_BOX_COLOR = 0xff061319;

    private final Text creditText = Text.literal("Credit: ");
    private Text creditValue = Text.literal("0$");
    int creditX, creditY;
    int titleX, titleY;
    int subScreenButtonX;
    int lastSubScreenButtonY;

    public MemberScreen(MemberScreenHandler memberScreenHandler, PlayerInventory playerInventory, Text title) {
        super(memberScreenHandler, playerInventory, title);
        this.client = MinecraftClient.getInstance();
        this.textRenderer = client.textRenderer;
        this.buyView = new MemberBuyScreen(this, Text.literal("Trade - Buy"));
        this.sellView = new MemberSellScreen(this, Text.literal("Trade - Sell"));
        this.miscView = new MemberMiscScreen(this, Text.literal("Services"));
        this.boxesView = new MemberIllegalScreen(this, Text.literal("Contraband"));
        this.flipView = new MemberFlipScreen(this, Text.literal("Coin Toss"));
        this.fireworksView = new MemberFireworkScreen(this, Text.literal("Fireworks"));
        this.currentView = buyView;
    }

    public IMemberSubScreen getCurrentView() {
        return currentView;
    }

    public void setCurrentView(MemberSubScreen memberSubScreen) {
        memberSubScreen.init();
        this.currentView = memberSubScreen;
    }

    private void addPlayerFaceComponent() {
        addDrawable(new PlayerFaceComponent(
                client.player.getSkinTextures().texture(),
                x + TILE_SIZE,
                y + TEXTURE.renderHeight - TILE_SIZE * 9
        ));
    }


    private void addTabButton(MemberSubScreen subScreen) {
        TabButtonWidget tabButtonWidget = new TabButtonWidget(
                subScreenButtonX,
                lastSubScreenButtonY,
                BUTTON_WIDTH,
                BUTTON_HEIGHT,
                subScreen
        );
        if (getScreenHandler().getCard().getLevel() < subScreen.unlockLevel()) {
            tabButtonWidget.active = false;
            tabButtonWidget.setMessage(Text.literal("Unlocks at " + (subScreen.unlockLevel() < 10 ? " " : "") + subScreen.unlockLevel()));
        }
        addDrawableChild(
                tabButtonWidget
        );

        lastSubScreenButtonY += BUTTON_HEIGHT;
    }
    @Override
    protected void init() {
        super.init();
        this.x = (int) ((width - TEXTURE.renderWidth) * 0.5f);
        this.y = height - TEXTURE.renderHeight;

        titleX = x + TILE_SIZE * 12;
        titleY = y + TILE_SIZE * 2 - 1;

        creditX = x + TEXTURE.renderWidth - BUTTON_WIDTH - 3 * TILE_SIZE;
        creditY = y +  TILE_SIZE * 2 - 1;

        addPlayerFaceComponent();

        subScreenButtonX = x + TEXTURE.textureWidth - BUTTON_WIDTH - TILE_SIZE;
        lastSubScreenButtonY = y + TILE_SIZE + 2;

        addTabButton(buyView);
        addTabButton(sellView);
        addTabButton(miscView);
        addTabButton(boxesView);
        addTabButton(flipView);
        addTabButton(fireworksView);
        currentView.init();
    }
    @Override
    protected void handledScreenTick() {
        this.creditValue = Text.literal(getScreenHandler().getCard().getCredit() + "$");
        currentView.handledScreenTick();
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontal, double vertical) {
        return currentView.mouseScrolled(mouseX, mouseY, horizontal, vertical) || super.mouseScrolled(mouseX, mouseY, horizontal, vertical);
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
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        currentView.render(context, mouseX, mouseY, delta);
        drawOutlinedTextRightAlignedTo(textRenderer, context, creditValue, creditX, creditY, CREDIT_COLOR , CREDIT_OUTLINE_COLOR);//separate text and value
        drawTextRightAlignedTo(textRenderer, context, creditText, creditX - textRenderer.getWidth(creditValue), creditY, BEIGE_TEXT_COLOR);//separate text and value
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        TEXTURE.render(context, x , y);
        if (currentView == null) {
            return;
        }
        context.drawTextWithShadow(textRenderer,  currentView.getTitle(), titleX , titleY, BEIGE_TEXT_COLOR);

    }

    @Override
    public void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }


    public void drawContainerBox(DrawContext context, int x, int y, int x0, int y0, boolean outline) {
        if (outline) {
            context.fill(x - 1, y - 1, x0 + 1, y0 + 1, OUTLINE_BOX_COLOR);
        }
        context.fill(x, y, x0, y0, BOX_COLOR);
    }

    public MinecraftClient getClient() {
        if (client == null) {
            client = MinecraftClient.getInstance();
        }
        return client;
    }
    public TextRenderer getTextRenderer() {
        return textRenderer;
    }

    public ItemRenderer getItemRenderer() {
        return MinecraftClient.getInstance().getItemRenderer();
    }


    public static final Texture TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/member_screen.png"),
            420,
            120
    );


    static final int TILE_SIZE = 4;
    public static final int BUTTON_WIDTH = TILE_SIZE * 20;
    private static final int BUTTON_HEIGHT = TabButtonWidget.BUTTON_TEXTURE.renderHeight / 3;

}

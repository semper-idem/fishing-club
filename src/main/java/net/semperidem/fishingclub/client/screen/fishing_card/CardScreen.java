package net.semperidem.fishingclub.client.screen.fishing_card;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.PlayerFaceIcon;
import net.semperidem.fishingclub.fisher.Card;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.screen.card.CardScreenHandler;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public class CardScreen extends HandledScreen<CardScreenHandler> implements ScreenHandlerProvider<CardScreenHandler> {

    private static final Identifier SCALES_TEXTURE = FishingClub.identifier("textures/gui/golden_scales.png");
    private static final Identifier TEXTURES = FishingClub.identifier("textures/gui/card.png");
    private static final int SCREEN_WIDTH = 176;
    private static final int SCREEN_HEIGHT = 166;
    private static final int TAB_TEXTURE_HEIGHT = 256;

    private Tab currentTab;
    private final ArrayList<Tab> tabs = new ArrayList<>();
    private final StatsTab statsTab = new StatsTab();
    private final SecretTab secretsTab = new SecretTab();
    private final AtlasTab atlasTab = new AtlasTab();
    private final LeaderboardTab leaderboardTab = new LeaderboardTab();

    private final Identifier playerTexture;



    public CardScreen(CardScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.currentTab = statsTab;
        this.playerTexture = ((ClientPlayerEntity)inventory.player).getSkinTextures().texture();
    }
    @Override
    protected void init() {
        super.init();
        this.currentTab.activate();
        this.tabs.forEach(tab -> addDrawableChild(new TabButton(tab)));
    }


    //No Titles
    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {}

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        draw(context,
                x,
                y,
                0,
                this.currentTab.type.ordinal() * TAB_TEXTURE_HEIGHT,
                SCREEN_WIDTH,
                SCREEN_HEIGHT
        );
        this.currentTab.render(context, mouseX, mouseY);
    }

    class SecretTab extends Tab {

        SecretTab() {
            super(CardScreenHandler.Tab.SECRET);
        }

        @Override
        void initTab() {

        }

        @Override
        void render(DrawContext context, int mouseX, int mouseY) {

        }
    }
    class AtlasTab extends Tab {

        AtlasTab() {
            super(CardScreenHandler.Tab.ATLAS);
        }

        @Override
        void initTab() {

        }

        @Override
        void render(DrawContext context, int mouseX, int mouseY) {

        }
    }


    class LeaderboardTab extends Tab {

        LeaderboardTab() {
            super(CardScreenHandler.Tab.LEADERBOARD);
        }

        @Override
        void initTab() {

        }

        @Override
        void render(DrawContext context, int mouseX, int mouseY) {

        }
    }

    class StatsTab extends Tab{
        Text level;
        Text issuedText;
        Text issuedDate;
        float xpProgress;
        Text playerName;
        Text title;
        boolean isKing;

        StatsTab() {
            super(CardScreenHandler.Tab.STATS);
        }

        @Override
        void initTab() {
            Card card = getScreenHandler().card;
            level = Text.of(String.valueOf(card.getLevel()));
            xpProgress = card.getExpProgress();
            playerName = card.owner().getName();
            issuedText = Text.of("Issued:");//todo lang
            issuedDate = Text.of(card.getIssuedDate());
            title = Text.of("Master");//todo get dynamic and lang
            isKing = card.isKing();
            addDrawableChild(
                    new SellButtonWidget(
                            x + 118,
                            y + 22,
                            9,
                            11
                    )
            );
            addDrawable(new PlayerFaceIcon(playerTexture, x + 11,y + 11, 0.5f));
        }


        int textColor = 0x555555;
        int scalesColor = 0xdfb220;


        private void drawText(DrawContext context, Text text, int x, int y, int color) {
            drawText(context, text, x, y, color, 1);
        }
        private void drawText(DrawContext context, Text text, int x, int y, int color, float scale) {
            context.getMatrices().push();
            context.getMatrices().scale(scale, scale, scale);
            context.drawText(textRenderer, text, (int) (x / scale), (int) (y / scale), color, false);
            context.getMatrices().pop();
        }
        @Override
        void render(DrawContext context, int mouseX, int mouseY) {

            draw(context, x + 32, y + 9, 178, 0, (int) (135 * xpProgress), 5);
            drawText(context, playerName, x + 33, y + 18, textColor, 0.75f);

            if (isKing) {
                draw(context, (int) (x + 34 + textRenderer.getWidth(playerName) * 0.75f), y + 17, 178, 68, 10, 6);
            }
            drawText(context, title, x + 33, y + 25, 0x8c1991, 0.5f);
            drawText(context, issuedText, x + 11, y + 64, textColor, 0.5f);
            drawText(context, issuedDate, x + 11, y + 69, textColor, 0.75f);

            drawText(context, level, x + 96, y + 9, 0);
            drawText(context, level, x + 98, y + 9, 0);
            drawText(context, level, x + 97, y + 10, 0);
            drawText(context, level, x + 97, y + 8, 0);
            drawText(context, level, x + 97, y + 9, 8453921);


            int credit = getScreenHandler().card.getCredit();
            Text creditText = Text.of(String.valueOf(credit));
            int creditOffset = textRenderer.getWidth(creditText);
            int stage = MathHelper.clamp(String.valueOf(credit).length() * 2 - 1, 0, 8);
            context.drawTexture(RenderLayer::getGuiTextured, SCALES_TEXTURE, x + 140 - creditOffset - 20,y + 60,0, stage * 16, 16,16, 16, 256);
            drawText(context, Text.of(String.valueOf(getScreenHandler().card.getCredit())), x + 140 - creditOffset, y + 66, scalesColor, 1f);
            //Name
            //Title
            //Xp
            //Credit

        }

        class SellButtonWidget extends ButtonWidget{
            int level;
            protected SellButtonWidget(int x, int y, int width, int height) {
                super(x, y, width, height, ScreenTexts.EMPTY, button -> {
                    handler.sellFish();
                }, DEFAULT_NARRATION_SUPPLIER);
                this.level = getScreenHandler().card.tradeSecretLevel(TradeSecrets.INSTANT_FISH_CREDIT);
            }

            @Override
            protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                if (this.level == 0) {
                    return;
                }
                draw(context,
                        this.getX(),
                        this.getY(),
                        180 + (getScreenHandler().cannotSell() ? 0 : isHovered() ? 1 : 2) * 10,
                        7 + ((getScreenHandler().cannotSell() || isHovered()) ? 0 : (this.level - 1) * 12),
                        width,
                        height
                        );
            }
        }
    }

    abstract class Tab {
        CardScreenHandler.Tab type;

        Tab(CardScreenHandler.Tab type) {
            this.type = type;
            tabs.add(this);
        }

        void activate() {
            currentTab = this;
            getScreenHandler().setCurrentTab(type);
            initTab();
        }

        abstract void initTab();

        abstract void render(DrawContext context, int mouseX, int mouseY);
    }

    private void draw(DrawContext context, int x, int y, int u, int v, int width, int height) {
        context.drawTexture(RenderLayer::getGuiTextured, TEXTURES, x,y,u,v,width,height, 512, 1024);
    }

    class TabButton extends ButtonWidget {
        private static final int BASE_U = 448;
        private static final int BASE_V = 0;
        private static final int WIDTH = 20;
        private static final int HEIGHT = 18;
        private final Tab tab;

        protected TabButton(Tab tab) {
            super(
                    x + SCREEN_WIDTH - 3,
                    y + tab.type.ordinal() * HEIGHT,
                    WIDTH,
                    HEIGHT,
                    ScreenTexts.EMPTY,
                    button -> {
                        currentTab = tab;
                        clearAndInit();
                    },
                    DEFAULT_NARRATION_SUPPLIER
            );
            this.tab = tab;
        }


        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            draw(context,
                    this.getX(),
                    this.getY(),
                    BASE_U + 22 * (currentTab == tab ? 0 : isHovered() ? 1 : 2),//Width + 2 from texture spacing
                    BASE_V + tab.type.ordinal() * HEIGHT,
                    WIDTH,
                    HEIGHT
            );
            if (!(tab instanceof StatsTab)) {
                return;
            }
            PlayerFaceIcon.render(context, playerTexture, this.getX() + ((currentTab == tab) ? 6 : 7), this.getY() + 6, 0.25f);
        }
    }

}



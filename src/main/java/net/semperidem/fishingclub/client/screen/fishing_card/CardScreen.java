package net.semperidem.fishingclub.client.screen.fishing_card;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.screen.card.CardScreenHandler;

import java.util.ArrayList;

@Environment(EnvType.CLIENT)
public class CardScreen extends HandledScreen<CardScreenHandler> implements ScreenHandlerProvider<CardScreenHandler> {
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



    public CardScreen(CardScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.currentTab = statsTab;
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

        StatsTab() {
            super(CardScreenHandler.Tab.STATS);
        }

        @Override
        void initTab() {
            addDrawableChild(
                    new SellButtonWidget(
                            x + 116,
                            y + 60,
                            9,
                            11
                    )
            );
        }

        @Override
        void render(DrawContext context, int mouseX, int mouseY) {

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
        context.drawTexture(TEXTURES, x,y,u,v,width,height, 512, 1024);
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
        }
    }

}



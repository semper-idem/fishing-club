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
    private final StatsTab statsTab = new StatsTab(0);
    private final StatsTab secretsTab = new StatsTab(1);
    private final StatsTab atlasTab = new StatsTab(2);
    private final StatsTab leaderboardTab = new StatsTab(3);



    public CardScreen(CardScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.currentTab = this.statsTab;
    }
    @Override
    protected void init() {
        super.init();
        this.tabs.forEach(Tab::init);
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
                this.currentTab.order * TAB_TEXTURE_HEIGHT,
                SCREEN_WIDTH,
                SCREEN_HEIGHT
        );
        this.currentTab.drawBackground(context);
    }

    class StatsTab extends Tab{

        StatsTab(int order) {
            super(order);
        }

        @Override
        void drawBackground(DrawContext context) {

        }
    }

    abstract class Tab {
        private final int order;

        Tab(int order) {
            this.order = order;
            tabs.add(this);
        }

        void init() {
            addDrawableChild(new TabButton(this));
            if (currentTab != this) {
                return;
            }
            System.out.println("Other widgets etc.");
        }

        abstract void drawBackground(DrawContext context);
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
            super(x + SCREEN_WIDTH - 3, y + tab.order * HEIGHT, WIDTH, HEIGHT, ScreenTexts.EMPTY, button -> {
                currentTab = tab;
            }, DEFAULT_NARRATION_SUPPLIER);
            this.tab = tab;
        }


        @Override
        protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            draw(context,
                    this.getX(),
                    this.getY(),
                    BASE_U + 22 * (currentTab == tab ? 0 : isHovered() ? 1 : 2),//Width + 2 from texture spacing
                    BASE_V + tab.order * HEIGHT,
                    WIDTH,
                    HEIGHT
            );
        }
    }

}



package net.semperidem.fishingclub.client.screen.fishing_card;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.TextRenderer;
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
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.screen.card.CardScreenHandler;
import net.semperidem.fishingclub.util.TextHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class CardScreen extends HandledScreen<CardScreenHandler>
        implements ScreenHandlerProvider<CardScreenHandler>,
        TextHelper {

    private static final Identifier SCALES_TEXTURE = FishingClub.identifier("textures/gui/golden_scales.png");
    private static final Identifier TEXTURES = FishingClub.identifier("textures/gui/card.png");
    private static final Identifier TRADE_SECRET_BACKGROUND = FishingClub.identifier("textures/gui/barrel_side.png");
    private static final int SCREEN_WIDTH = 176;
    private static final int SCREEN_HEIGHT = 166;
    private static final int TAB_TEXTURE_HEIGHT = 256;

    private static final int INFO_COLOR = 0x555555;
    private static final int GS_COLOR = 0xdfb220;

    private Tab currentTab;
    private final ArrayList<Tab> tabs = new ArrayList<>();

    private final Identifier playerTexture;



    public CardScreen(CardScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.currentTab = new StatsTab();
        new SecretTab();
        new AtlasTab();
        new LeaderboardTab();
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
        this.currentTab.renderBackground(context, mouseX, mouseY);
        draw(context,
                x,
                y,
                0,
                this.currentTab.type.ordinal() * TAB_TEXTURE_HEIGHT,
                SCREEN_WIDTH,
                SCREEN_HEIGHT
        );
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
        super.render(context, mouseX, mouseY, deltaTicks);
        this.currentTab.render(context, mouseX, mouseY);
     }

    @Override
    public TextRenderer getTextRenderer() {
        return this.textRenderer;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return currentTab.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        return currentTab.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    class SecretTab extends Tab {
        int anchorX;
        int anchorY;

        static final int windowWidth = 154;
        static final int windowHeight = 144;

        static final int tileSize = 20;

        static final int border = 20;

        int widthRange;
        int heightRange;

        int verticalSpace = 2;
        int horizontalSpace = 8;
        Collection<TradeSecret.Instance> unlockedTradeSecrets = new ArrayList<>();
        Collection<TradeSecret> allTradeSecrets = TradeSecrets.all();
        int xTileCount;
        int yTileCount;



        SecretTab() {
            super(CardScreenHandler.Tab.SECRET);
            HashMap<Integer, Integer> heightMap = new HashMap<>();

            int tradeSecretMaxWidth = 0;
            for(TradeSecret tradeSecret : allTradeSecrets) {
                int tradeSecretWidth = tradeSecret.getRequiredSecrets().size();
                int presentHeight = heightMap.getOrDefault(tradeSecretWidth, 0);
                heightMap.put(tradeSecretWidth, presentHeight + 1);
                if (tradeSecretWidth > tradeSecretMaxWidth) {
                    tradeSecretMaxWidth = tradeSecretWidth;
                }
            }
            int tradeSecretMaxHeight = heightMap.values().stream().sorted((a,b) -> b - a).findAny().orElse(0);

            widthRange = tradeSecretMaxWidth * (tileSize + horizontalSpace);
            heightRange = tradeSecretMaxHeight * (tileSize + verticalSpace);
            xTileCount = 20 + widthRange / 16;
            yTileCount = 20 * heightRange / 16;
        }

        @Override
        boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            anchorX = (int) MathHelper.clamp(anchorX + deltaX,  0, widthRange);
            anchorY = (int) MathHelper.clamp(anchorY + deltaY, 0, heightRange);
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }

        @Override
        void initTab() {
        }

        @Override
        void renderBackground(DrawContext context, int mouseX, int mouseY) {
            int offXTiles = (int) (anchorX * 0.0625f) + 1;
            int offYTiles = (int) (anchorY * 0.0625f) + 1;
            context.enableScissor(x + 4, y + 4, x + SCREEN_WIDTH - 4, y + SCREEN_HEIGHT - 4);
            for (int i = -offXTiles; i < 13 - offXTiles; i++) {
                for (int j = -offYTiles; j < 13 - offYTiles; j++) {
                    context.drawTexture(RenderLayer::getGuiTextured, TRADE_SECRET_BACKGROUND,
                            x + anchorX + i * 16,
                            y + anchorY + j * 16,
                            0, 0, 16, 16, 16, 16);
                }
            }
            context.disableScissor();
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
        int iLevel;
        Text issuedText;
        Text issuedDate;
        float xpProgress;
        Text playerName;
        Text title;
        boolean isKing;
        int slotsUnlocked;

        StatsTab() {
            super(CardScreenHandler.Tab.STATS);
        }

        @Override
        void initTab() {
            Card card = getScreenHandler().card;
            iLevel = card.getLevel();
            level = Text.of(String.valueOf(iLevel));
            xpProgress = card.getExpProgress();
            playerName = card.owner().getName();
            issuedText = Text.of("Issued:");//todo lang
            issuedDate = Text.of(card.getIssuedDate());
            title = card.getTitle();
            isKing = card.isKing();
            slotsUnlocked = card.tradeSecretLevel(TradeSecrets.PLACE_IN_MY_HEART);
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


        @Override
        void render(DrawContext context, int mouseX, int mouseY) {

            //XP
            draw(context, x + 32, y + 9, 178, 0, (int) (135 * xpProgress), 5);

            //Name and Title
            drawText(context, playerName, x + 33, y + 18, INFO_COLOR, 0.75f);
            drawText(context, title, x + 33, y + 25, 0x8c1991, 0.5f, true);

            if (isKing) {
                //TODO Add info about title and hold time?
                draw(context, (int) (x + 34 + textRenderer.getWidth(playerName) * 0.75f), y + 17, 178, 68, 10, 6);
            }

            //Issued and date
            drawText(context, issuedText, x + 11, y + 64, INFO_COLOR, 0.5f);
            drawText(context, issuedDate, x + 11, y + 69, INFO_COLOR, 0.75f);


            //Level
            int levelX = (int) (x + (iLevel > 99 ? 19 : 20) - textRenderer.getWidth(level) * 0.5f);
            int levelY = y + 27;
            drawText(context, level, levelX - 1, levelY, 0);
            drawText(context, level, levelX + 1, levelY, 0);
            drawText(context, level, levelX, levelY - 1, 0);
            drawText(context, level, levelX, levelY + 1, 0);
            drawText(context, level, levelX, levelY, 8453921);


            //Golden Scales
            int gs = getScreenHandler().card.getGS();
            Text gsText = Text.of(String.valueOf(gs));
            int gsOffset = textRenderer.getWidth(gsText);
            int gsIconIndex = MathHelper.clamp(String.valueOf(gs).length() * 2 - 1, 0, 8);
            context.drawTexture(RenderLayer::getGuiTextured, SCALES_TEXTURE, x + 140 - gsOffset - 20,y + 60,0, gsIconIndex * 16, 16,16, 16, 256);
            drawText(context, Text.of(String.valueOf(getScreenHandler().card.getGS())), x + 140 - gsOffset, y + 66, GS_COLOR, 1f, true);


            //Locked slot cover
            for(int i = 0; i < 3 - slotsUnlocked; i++) {
                int coverY = y + 18  + i * 19 ;
                context.fill(x + 147, coverY, x + 165, coverY + 18, 0xAA000000);
            }
        }

        class SellButtonWidget extends ButtonWidget{
            int level;
            protected SellButtonWidget(int x, int y, int width, int height) {
                super(x, y, width, height, ScreenTexts.EMPTY, button -> {
                    //TODO Add sound on click
                    handler.sellFish();
                }, DEFAULT_NARRATION_SUPPLIER);
                this.level = getScreenHandler().card.tradeSecretLevel(TradeSecrets.INSTANT_FISH_CREDIT);
            }

            @Override
            protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
                if (this.level == 0) {
                    context.fill(x + 128, y + 18, x + 146, y + 36, 0xAA000000);
                    return;
                }

                //Show value if fish in slow
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

        boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            return false;
        }

        public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
            return false;
        }

        abstract void initTab();

        abstract void render(DrawContext context, int mouseX, int mouseY);

        void renderBackground(DrawContext context, int mouseX, int mouseY) {}
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



package net.semperidem.fishingclub.client.screen.fishing_card;

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

import java.util.*;

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

    int draggedX;
    int draggedY;


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
        context.enableScissor(x, y, x + SCREEN_WIDTH, y + SCREEN_HEIGHT);
        this.currentTab.render(context, mouseX, mouseY);
        context.disableScissor();
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

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX - draggedX, mouseY - draggedY, button);
    }

    public int getScreenX() {
        return this.x;
    }

    public int getScreenY() {
        return this.y;
    }

    class SecretTab extends Tab {
       HashMap<String, TradeSecret.Instance> unlockedTradeSecrets = new HashMap<>();
        TradeSecretNode root = new TradeSecretNode(null, TradeSecret.ROOT);

        static final int TILE_SIZE = 20;
        static final int PADDING = 20;
        static final int CHILD_GAP = 4;
        static final int PARENT_GAP = 16;

        class TradeSecretNode extends ButtonWidget {
            int containerWidth = TILE_SIZE;
            int containerHeight = TILE_SIZE;
            TradeSecret secret;

            TradeSecretNode parent;
            List<TradeSecretNode> children = new ArrayList<>();

            TradeSecretNode(TradeSecretNode parent, TradeSecret secret) {
                super(0,0, TILE_SIZE, TILE_SIZE, Text.empty(), selected -> {
                    System.out.println(secret.name());
                }, DEFAULT_NARRATION_SUPPLIER);
                this.secret = secret;
                this.parent = parent;
                addChildren(secret);
            }
            void addChildren(TradeSecret secret) {
                for(TradeSecret child : secret.getChildren()) {
                    children.add(new TradeSecretNode(this, child));
                }
            }

            void init() {
                resize();
                reposition();
                for(TradeSecretNode child : children) {
                    child.addDrawable();
                    child.init();
                }
            }

            void addDrawable() {
               addDrawableChild(this);
            }

            void resize() {
                for(TradeSecretNode child : children) {
                    child.resize();
                    containerHeight += child.containerHeight + CHILD_GAP;
                }
                if (!children.isEmpty()) {
                    containerHeight -= (TILE_SIZE + CHILD_GAP);
                }
                if (parent != null) {
                    parent.containerWidth = Math.max(containerWidth + TILE_SIZE + PARENT_GAP, parent.containerWidth);
                }
            }

            void reposition() {
                TradeSecretNode previousChild = null;
                for(TradeSecretNode child : children) {
                    child.setX(previousChild == null ? getX() + TILE_SIZE + PARENT_GAP : previousChild.getX());
                    child.setY(previousChild == null ? getY() : previousChild.getY() + previousChild.containerHeight + CHILD_GAP);
                    child.reposition();
                    previousChild = child;
                }
            }

            @Override
            protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {
                if (secret == null) {
                    return;
                }
                context.enableScissor(x + 4, y + 4, x + SCREEN_WIDTH - 4, y + SCREEN_HEIGHT - 4);

                draw(context, draggedX + getX() , draggedY + getY(), 178, 256, width, height);
                context.drawTexture(
                        RenderLayer::getGuiTextured,
                        secret.getTexture(),
                        draggedX + getX() + 2,
                        draggedY + getY() + 2,
                        0, 0,
                        16, 16,
                        16, 16
                );
                context.disableScissor();
            }
        }

        SecretTab() {
            super(CardScreenHandler.Tab.SECRET);
            getScreenHandler().card.tradeSecrets().forEach(tradeSecretInstance -> unlockedTradeSecrets.put(tradeSecretInstance.name(), tradeSecretInstance));
        }

        @Override
        boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
            draggedX = (int) MathHelper.clamp(draggedX + deltaX,  dragHorizontalMin, dragHorizontalMax);
            draggedY = (int) MathHelper.clamp(draggedY + deltaY, dragVerticalMin, dragVerticalMax);
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        }


        int dragVerticalMin = 0;
        int dragVerticalMax = 0;
        int dragHorizontalMin = 0;
        int dragHorizontalMax = 0;

        @Override
        void initTab() {
            root.setX(x + PADDING - TILE_SIZE - PARENT_GAP);
            root.setY(y + PADDING);
            root.init();
            dragHorizontalMin = (int) (root.containerWidth * -0.15f);
            dragHorizontalMax = 0;//(int) (root.containerWidth * 0.25f);
            dragVerticalMin = (int) (-root.containerHeight + SCREEN_HEIGHT * 0.75f);//(int) (root.containerHeight * -0.5f);
            dragVerticalMax = 0;//root.containerHeight;//(int) (root.containerHeight * 0.5f);
        }

        @Override
        public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
        @Override
        void render(DrawContext context, int mouseX, int mouseY) {
            draw(context,
                    x,
                    y,
                    0,
                    this.type.ordinal() * TAB_TEXTURE_HEIGHT,
                    SCREEN_WIDTH - 4,
                    SCREEN_HEIGHT
            );
        }


        void renderNode(DrawContext context, TradeSecretNode node, int parentX, int parentY) {
//            int nodeX = parentX + horizontalSpace + tileSize;
//            int nodeY = parentY + node.yFromParent;
//            int lineColor = 0xFFBBBBBB;
//            int lineShadowColor = 0xFF888888;
//            draw(context, nodeX -2 , nodeY - 2, 178, 256, 20, 20);
//            context.drawTexture(RenderLayer::getGuiTextured, node.secret.getTexture(),
//                    nodeX,
//                    nodeY,
//                    0,0,
//                    16,16,
//                    16,16);
//            if (!node.children.isEmpty()) {
//                int halfLineHeight = (int) ((node.children.size() -1 )* (tileSize) * 0.5f);
//                context.drawVerticalLine(nodeX + 25, nodeY - halfLineHeight + 8, nodeY + halfLineHeight + 8, lineShadowColor);
//                context.drawHorizontalLine(nodeX + 18, nodeX + 23, nodeY + 9, lineShadowColor);
//            }
//            for(TradeSecretNode child : node.children) {
//                context.drawHorizontalLine(nodeX + 24, nodeX + horizontalSpace + tileSize, nodeY + child.yFromParent + 9, lineShadowColor);
//                renderNode(context, child, nodeX, nodeY);
//            }
//            for(TradeSecretNode child : node.children) {
//                context.drawHorizontalLine(nodeX + 24, nodeX + horizontalSpace + tileSize - 3, nodeY + child.yFromParent + 8, lineColor);
//            }
//            if (!node.children.isEmpty()) {
//                int halfLineHeight = (int) ((node.children.size() -1 )* (tileSize) * 0.5f);
//                context.drawHorizontalLine(nodeX + 18, nodeX + 24, nodeY + 8, lineColor);
//                context.drawVerticalLine(nodeX + 24, nodeY - halfLineHeight + 8, nodeY + halfLineHeight + 8, lineColor);
//            }
        }

        @Override
        void renderBackground(DrawContext context, int mouseX, int mouseY) {
            int offXTiles = (int) (draggedX * 0.0625f) + 1;
            int offYTiles = (int) (draggedY * 0.0625f) + 1;
            context.enableScissor(x + 4, y + 4, x + SCREEN_WIDTH - 4, y + SCREEN_HEIGHT - 4);

            for (int i = -offXTiles; i < 13 - offXTiles; i++) {
                for (int j = -offYTiles; j < 13 - offYTiles; j++) {
                    context.drawTexture(RenderLayer::getGuiTextured, TRADE_SECRET_BACKGROUND,
                            x + draggedX + i * 16,
                            y + draggedY + j * 16,
                            0, 0, 16, 16, 16, 16);
                }
            }
//            for(int i = 0; i < root.size(); i++) {
//                renderNode(context, root.get(i), x - 5 - draggedX, y + 15 - draggedY + i * tileSize + verticalSpace);
//            }
            context.disableScissor();
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
        int tabX;
        int tabY;

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



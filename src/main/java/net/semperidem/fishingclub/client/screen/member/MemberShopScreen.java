package net.semperidem.fishingclub.client.screen.member;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.fisher.shop.MemberStock;
import net.semperidem.fishingclub.fisher.shop.StockEntry;

import java.util.ArrayList;
import java.util.Iterator;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TILE_SIZE;



public class MemberShopScreen extends MemberSubScreen {

    static final Texture OFFER_TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/offer_background.png"),
            16,
            16
    );


    private int baseX, baseY;
    private int categoryButtonX, categoryButtonY;
    private int categoryButtonCount = 0;
    private int offerGridX, offerGridY;
    CategoryButton fisherCategoryButton;
    CategoryButton minerCategoryButton;
    CategoryButton lumberjackCategoryButton;
    CategoryButton alchemistCategoryButton;
    CategoryButton librarianCategoryButton;
    CategoryButton adventurerCategoryButton;
    Category currentCategory;

    OfferGrid offerGrid;

    public MemberShopScreen(MemberScreen parent, Text title) {
        super(parent, title);
    }


    private void setCategory(Category category) {
        this.currentCategory = category;
        offerGrid.loadOffers(this.currentCategory);
    }

    public void init() {
        super.init();

        baseX = parent.x + TILE_SIZE * 10;
        baseY = parent.y + TILE_SIZE * 5;
        categoryButtonX = baseX;
        categoryButtonY = baseY;
        fisherCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.FISHING_ROD, 0));
        minerCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.IRON_PICKAXE, 10));
        lumberjackCategoryButton = new CategoryButton(new Category(MemberStock.LUMBERJACK_STOCK_KEY, Items.IRON_AXE, 0));
        alchemistCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.BREWING_STAND, 20));
        librarianCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.ENCHANTED_BOOK, 30));
        adventurerCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.FILLED_MAP, 30));
        offerGridX = baseX + CategoryButton.SIZE + TILE_SIZE;
        offerGridY = baseY;

        offerGrid = new OfferGrid(offerGridX, offerGridY, TILE_SIZE * 25, parent.height - baseY - TILE_SIZE, Text.empty());
        setCategory(fisherCategoryButton.category);
    }

     class OfferGrid extends ScrollableWidget {
        private static final int SCROLL_WIDTH = 4;
        private int entriesPerRow;
        ArrayList<Offer> entries = new ArrayList<>();

        public OfferGrid(int x, int y, int width, int height, Text text) {
            super(x, y, width, height, text);
            entriesPerRow = width / Offer.SIZE;
            components.add(this);
        }

        public void loadOffers(Category category) {
            entries.clear();
            for(StockEntry stockEntry : MemberStock.STOCK.get(category.name)) {
                entries.add(new Offer(stockEntry));
            }
        }


        private static class Offer {
            static int SIZE = 16;
            StockEntry stockEntry;
            Offer(StockEntry stockEntry) {
                this.stockEntry = stockEntry;
            }

            void render(MatrixStack matrixStack, int x, int y) {
                OFFER_TEXTURE.render(matrixStack, x, y);
                MinecraftClient.getInstance().getItemRenderer().renderInGui(stockEntry.item.getDefaultStack(), x, y);
            }
        }

        @Override
        protected int getContentsHeight() {
            return getRows() * Offer.SIZE;
        }

        private int getRows() {
            if (entriesPerRow == 0) {
                return 0;
            }
            return (int) (Math.ceil(1f * entries.size() / entriesPerRow));
        }

        @Override
        protected boolean overflows() {
            return false;
        }

        @Override
        protected double getDeltaYPerScroll() {
            return Offer.SIZE;
        }

         private int getContentsHeightWithPadding() {
             return this.getContentsHeight() + 4;
         }

         private int getScrollbarThumbHeight() {
             return MathHelper.clamp((int)((int)((float)(this.height * this.height) / (float)this.getContentsHeightWithPadding())), (int)32, (int)this.height);
         }

         @Override
         protected void renderOverlay(MatrixStack matrices) {
             int i = this.getScrollbarThumbHeight();
             int j = this.x + this.width - 4;
             int k = this.x + this.width;
             int l = Math.max(this.y, this.getMaxScrollY() == 0 ?  y : (int)this.getScrollY() * (this.height - i) / this.getMaxScrollY() + this.y);
             int m = l + i;
             RenderSystem.setShader(GameRenderer::getPositionColorShader);
             Tessellator tessellator = Tessellator.getInstance();
             BufferBuilder bufferBuilder = tessellator.getBuffer();
             bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
             bufferBuilder.vertex((double)j, (double)m, 0.0).color(128, 128, 128, 255).next();
             bufferBuilder.vertex((double)k, (double)m, 0.0).color(128, 128, 128, 255).next();
             bufferBuilder.vertex((double)k, (double)l, 0.0).color(128, 128, 128, 255).next();
             bufferBuilder.vertex((double)j, (double)l, 0.0).color(128, 128, 128, 255).next();
             bufferBuilder.vertex((double)j, (double)(m - 1), 0.0).color(192, 192, 192, 255).next();
             bufferBuilder.vertex((double)(k - 1), (double)(m - 1), 0.0).color(192, 192, 192, 255).next();
             bufferBuilder.vertex((double)(k - 1), (double)l, 0.0).color(192, 192, 192, 255).next();
             bufferBuilder.vertex((double)j, (double)l, 0.0).color(192, 192, 192, 255).next();
             tessellator.draw();
         }

         @Override
         public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
             if (this.visible) {
                 this.drawBox(matrices);
                 enableScissor(this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1);
                 matrices.push();
                 matrices.translate(0.0, -this.getScrollY(), 0.0);
                 this.renderContents(matrices, mouseX, mouseY, delta);
                 matrices.pop();
                 disableScissor();
                 this.renderOverlay(matrices);
             }
         }

         private void drawBox(MatrixStack matrices) {
             int i = 0xff5F5F60;
             fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, i);
             fill(matrices, this.x + 1, this.y + 1, this.x + this.width - 1, this.y + this.height - 1, 0xaa00223c);
         }

         @Override
         protected void renderBackground(MatrixStack matrices, MinecraftClient client, int mouseX, int mouseY) {
             super.renderBackground(matrices, client, mouseX, mouseY);
         }

         @Override
        protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            int offerY = y;
            int rows = getRows();
            Iterator<Offer> iterator = entries.iterator();
            for(int i = 0; i < rows; i++) {
                int offerX = x;
                for(int j = 0; j < entriesPerRow; j++) {
                    if (!iterator.hasNext()) {
                        return;
                    }
                    iterator.next().render(matrices, offerX, offerY);
                    offerX += Offer.SIZE;
                }
                offerY += Offer.SIZE;
            }

        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {

        }
    }


    class Category {
        String name;
        Item itemIcon;
        int unlockLevel;
        Category(String name, Item itemIcon, int unlockLevel) {
            this.name = name;
            this.itemIcon = itemIcon;
            this.unlockLevel = unlockLevel;
        }

        public ButtonWidget.PressAction onPress() {
            return button -> setCategory(this);

        }

    }
    class CategoryButton extends MemberButton {
        private static final int SIZE = 16;

        static final Texture CATEGORY_BUTTON = new Texture(
                FishingClub.getIdentifier("textures/gui/member_category_button.png"),
                SIZE,
                SIZE * 4
        );

        Category category;
        public CategoryButton(Category category) {
            super(categoryButtonX, categoryButtonY + SIZE * categoryButtonCount, SIZE, SIZE, Text.empty(), category.onPress());
            this.category = category;
            this.active = getParent().getScreenHandler().getCard().getLevel() >= category.unlockLevel;
            categoryButtonCount++;
            components.add(this);
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            int textureIndex = 0;
            if (currentCategory == category) {
                textureIndex = 1;
            } else if (hovered) {
                textureIndex = 3;
            } else if (active){
                textureIndex = 2;
            }
            CATEGORY_BUTTON.render(matrices, x, y, 0, SIZE * textureIndex, width, height);
            MinecraftClient.getInstance().getItemRenderer().renderInGui(category.itemIcon.getDefaultStack(), x, y);
            MinecraftClient.getInstance().getItemRenderer().renderInGui(active ? Items.GLASS_PANE.getDefaultStack() : Items.GRAY_STAINED_GLASS_PANE.getDefaultStack(), x, y);
        }
    }
/*
*
*   Category Icons
    Scrollable with offers
     - Offer Item
       - On left click add/ shift click x8
       - on right click remove / shift x8
       - on hover normal item description + price info

    Scrollable with cart items
     - Cart Item
       - Item
       - Count
       - -/+ buttons
       - total per item
       - on hover normal item description + price info
    Summary widget
     - Buy button
     - Total price
*
* */

}

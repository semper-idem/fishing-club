package net.semperidem.fishingclub.client.screen.member;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.fisher.shop.MemberStock;
import net.semperidem.fishingclub.fisher.shop.StockEntry;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static net.minecraft.client.gui.DrawableHelper.fill;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TILE_SIZE;



public class MemberBuyScreen extends MemberSubScreen {

    public static final Texture OFFER_TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/offer_background.png"),
            16,
            16
    );
    static final Texture CART_ITEM_TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/cart_item.png"),
            48,
            16
    );

    private int baseX, baseY;
    private int categoryButtonX, categoryButtonY;
    private int categoryButtonCount;
    private int offerGridX, offerGridY;
    CategoryButton fisherCategoryButton;
    CategoryButton minerCategoryButton;
    CategoryButton lumberjackCategoryButton;
    CategoryButton alchemistCategoryButton;
    CategoryButton librarianCategoryButton;
    CategoryButton adventurerCategoryButton;
    Category currentCategory;

    ButtonWidget buyButton;
    ButtonWidget clearButton;

    OfferGrid offerGrid;
    CartWidget cartWidget;

    MinecraftClient client;

    OfferGrid.Offer focusedOffer;

    public MemberBuyScreen(MemberScreen parent, Text title) {
        super(parent, title);
        client = MinecraftClient.getInstance();
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
        categoryButtonCount = 0;
        fisherCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.FISHING_ROD, 0));
        minerCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.IRON_PICKAXE, 10));
        lumberjackCategoryButton = new CategoryButton(new Category(MemberStock.LUMBERJACK_STOCK_KEY, Items.IRON_AXE, 0));
        alchemistCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.BREWING_STAND, 20));
        librarianCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.ENCHANTED_BOOK, 30));
        adventurerCategoryButton = new CategoryButton(new Category(MemberStock.FISHER_STOCK_KEY, Items.FILLED_MAP, 30));
        offerGridX = baseX + CategoryButton.SIZE + TILE_SIZE;
        offerGridY = baseY;

        offerGrid = new OfferGrid(offerGridX, offerGridY, TILE_SIZE * 25, parent.height - baseY - TILE_SIZE);
        cartWidget = new CartWidget(offerGridX + offerGrid.getWidth() + TILE_SIZE * 3, offerGridY + 3 * TILE_SIZE, CART_ITEM_TEXTURE.renderWidth + TILE_SIZE, parent.height - baseY - TILE_SIZE * 5);
        setCategory(fisherCategoryButton.category);


        int buyButtonX = parent.x + MemberScreen.TEXTURE.renderWidth - 42 * TILE_SIZE;
        int buttonY = parent.y + MemberScreen.TEXTURE.renderHeight - 20 - TILE_SIZE * 2;
        //int x, int y, int width, int height, Text message, PressAction onPress
        buyButton = new MemberButton(buyButtonX, buttonY, TILE_SIZE  * 18 , 20, Text.literal("Buy"),button -> {
            ArrayList<ItemStack> cart = new ArrayList<>();

            for(OfferGrid.Offer offer : cartWidget.cartItems.keySet()) {
                ItemStack offerStack = offer.stockEntry.item.getDefaultStack();
                offerStack.setCount(cartWidget.cartItems.get(offer));
                cart.add(offerStack);
            }
            ClientPacketSender.checkout(cart, cartWidget.getCartTotalAmount());
            cartWidget.cartItems.clear();
        });
        components.add(buyButton);

        clearButton = new MemberButton(buyButtonX - 20, buttonY, 20, 20, Text.literal("x"),button -> {
            cartWidget.cartItems.clear();
        });
        components.add(clearButton);
    }


    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        int boxX = clearButton.x;
        int boxX1 = buyButton.x + buyButton.getWidth();
        int boxY = clearButton.y - client.textRenderer.fontHeight - TILE_SIZE - 1;
        int boxY1 = clearButton.y + clearButton.getHeight();

        int color1 = 0xff272946;
        int color2 = 0xff061319;
        fill(matrixStack, boxX, boxY, boxX1, boxY1, color2);
        fill(matrixStack, boxX + 1, boxY + 1, boxX1 - 1, boxY1 - 1, color1);
        // fill(matrixStack, boxX + 1, boxY1 - client.textRenderer.fontHeight - 4, boxX1 - 1, boxY1 - client.textRenderer.fontHeight - 5, color2);
        client.textRenderer.drawWithShadow(matrixStack, "Total:", boxX + 2, boxY + 3, MemberScreen.BEIGE_TEXT_COLOR);
        String cartPrice = cartWidget.getCartTotal();
        client.textRenderer.drawWithShadow(matrixStack, cartPrice, boxX1 - client.textRenderer.getWidth(cartPrice) -  2, boxY + 3, MemberScreen.BEIGE_TEXT_COLOR);
        super.render(matrixStack, mouseX, mouseY, delta);
        if (!cartWidget.isHovered() && !offerGrid.isHovered()) {
            this.focusedOffer = null;
        }
        if (this.focusedOffer != null) {
            StockEntry entry = this.focusedOffer.stockEntry;
            List<Text> tooltip = parent.getTooltipFromItem(entry.item.getDefaultStack());
            if (cartWidget.cartItems.containsKey(this.focusedOffer)) {
                int total = (int) entry.getPriceFor(cartWidget.cartItems.get(this.focusedOffer));
                tooltip.add(Text.literal("Total: §6" + total + "$"));
            }
            tooltip.add(Text.literal("Price: §6" + (int)entry.price + "$"));
            if (entry.discount != 0) {
                tooltip.add(Text.literal("Discount: -§6" + (int)this.focusedOffer.stockEntry.discount + "$§r for every §3" + this.focusedOffer.stockEntry.discountPer));
                tooltip.add(Text.literal("Min Price: §6" + (int)this.focusedOffer.stockEntry.minPrice + "$"));
            }
            parent.renderTooltip(matrixStack, tooltip, mouseX, mouseY);

        }

    }


    private void addItem(OfferGrid.Offer offer) {
        cartWidget.addItem(offer);
    }

    private void removeItem(OfferGrid.Offer offer) {
        cartWidget.removeItem(offer);
    }

    private class CartWidget extends ScrollableWidget {

        HashMap<OfferGrid.Offer, Integer> cartItems = new HashMap<>();

        private int getStacksInCart() {
            int stacksInCart = 0;
            for(OfferGrid.Offer offer : cartItems.keySet()) {
                stacksInCart += (int) Math.ceil(cartItems.get(offer) / 64f);
            }
            return stacksInCart;
        }

        public String getCartTotal() {
            return getCartTotalAmount() + "$";
        }
        public int getCartTotalAmount() {
            int total = 0;
            for(OfferGrid.Offer offer : cartItems.keySet()) {
                total += offer.stockEntry.getPriceFor(cartItems.get(offer));
            }
            return total;
        }

        public CartWidget(int x, int y, int width, int height) {
            super(x, y, width, height, Text.empty());
            components.add(this);
        }
        private void addItem(OfferGrid.Offer offer) {
            int count = 1;
            count *= InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_SHIFT) ? 4 : 1;
            count *= InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_ALT) ? 4 : 1;
            cartItems.put(offer, Math.min(999, cartItems.getOrDefault(offer, 0) + (count)));
        }
        private void removeItem(OfferGrid.Offer offer) {
            int count = 1;
            count *= InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_SHIFT) ? 4 : 1;
            count *= InputUtil.isKeyPressed(client.getWindow().getHandle(), InputUtil.GLFW_KEY_LEFT_ALT) ? 4 : 1;
            cartItems.put(offer, cartItems.get(offer) - count);
            HashMap<OfferGrid.Offer, Integer> updatedCartItem = new HashMap<>();
            for(OfferGrid.Offer off : cartItems.keySet()) {
                int newCount = cartItems.get(off);
                if (newCount > 0 ) {
                    updatedCartItem.put(off, newCount);
                }
            }
            cartItems = updatedCartItem;
        }
        @Override
        protected int getContentsHeight() {
            return cartItems.size() * OfferGrid.Offer.SIZE;
        }
        @Override
        protected boolean overflows() {
            return false;
        }

        @Override
        public boolean isHovered() {
            return hovered;
        }

        @Override
        protected double getDeltaYPerScroll() {
            return OfferGrid.Offer.SIZE;
        }
        @Override
        protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            int index = 0;
            for (OfferGrid.Offer offer : cartItems.keySet()) {
                CART_ITEM_TEXTURE.render(matrices, x, y + index * OfferGrid.Offer.SIZE);
                ItemStack stackToRender = offer.stockEntry.item.getDefaultStack();
                stackToRender.setCount(cartItems.get(offer));
                client.getItemRenderer().renderInGui(
                        stackToRender,
                        x + ((CART_ITEM_TEXTURE.renderWidth - OfferGrid.Offer.SIZE) / 2),
                        (int) (y + index * OfferGrid.Offer.SIZE - getScrollY()));
                client.getItemRenderer().renderGuiItemOverlay(client.textRenderer,
                        stackToRender,
                        x + ((CART_ITEM_TEXTURE.renderWidth - OfferGrid.Offer.SIZE) / 2),
                        (int) (y + index * OfferGrid.Offer.SIZE - getScrollY()));
                index++;
                //next add to components
            }
        }
        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {}
        private int getContentsHeightWithPadding() {
            return this.getContentsHeight();
        }
        private int getScrollbarThumbHeight() {
            return MathHelper.clamp((int)((int)((float)(this.height * this.height) / (float)this.getContentsHeightWithPadding())), (int)32, (int)this.height);
        }

        @Override
        protected void renderOverlay(MatrixStack matrices) {
            int i = this.getScrollbarThumbHeight();
            int x0 = this.x + this.width - TILE_SIZE;
            int x1 = this.x + this.width;
            int y0 = Math.max(this.y, this.getMaxScrollY() == 0 ? this.y : (int)this.getScrollY() * (this.height - i) / this.getMaxScrollY() + this.y);
            int y1 = y0 + i;
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            int color1 = 0xffbb8f1b;
            int color2 = 0xff4b2f00;
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            bufferBuilder.vertex(x0, y1, 0).color(color2).next();
            bufferBuilder.vertex(x1, y1, 0).color(color2).next();
            bufferBuilder.vertex(x1, y0, 0).color(color2).next();
            bufferBuilder.vertex(x0, y0, 0).color(color2).next();
            bufferBuilder.vertex(x0, (y1 - 1), 0).color(color1).next();
            bufferBuilder.vertex((x1 - 1), (y1 - 1), 0).color(color1).next();
            bufferBuilder.vertex((x1 - 1), y0, 0).color(color1).next();
            bufferBuilder.vertex(x0, y0, 0).color(color1).next();
            tessellator.draw();
        }
        @Override
        public boolean isFocused() {
            return super.isFocused() || isHovered();
        }
        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (this.visible) {
                this.drawBox(matrices);
                client.textRenderer.drawWithShadow(matrices,"Cart: (" + getStacksInCart() + ")", this.x, this.y - TILE_SIZE * 2 - 2 , MemberScreen.BEIGE_TEXT_COLOR);
                enableScissor(this.x, this.y, this.x + this.width, this.y + this.height);
                matrices.push();
                matrices.translate(0, -this.getScrollY(), 0);
                this.renderContents(matrices, mouseX, mouseY, delta);
                matrices.pop();
                disableScissor();
                this.renderOverlay(matrices);
            }
        }
        private void drawBox(MatrixStack matrices) {
            int color1 = 0xff272946;
            int color2 = 0xff061319;
            fill(matrices, this.x - 1, this.y - 1, this.x + this.width + 1, this.y + this.height + 1, color2);
            fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, color1);
        }
        @Override
        protected int getMaxScrollY() {
            return Math.max(0, getContentsHeight() - this.height);
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (hovered) {
                focusedOffer = offerAtMouse(mouseX, mouseY);
            }
            super.render(matrices, mouseX, mouseY, delta);
        }

        private OfferGrid.Offer offerAtMouse(double mouseX, double mouseY) {
            if (!hovered) {
                return null;
            }
            int predictedIndex = (int) ((mouseY + getScrollY() - y) / OfferGrid.Offer.SIZE);
            if (cartItems.size() > predictedIndex && predictedIndex >= 0) {
                int clickSize = 16;
                boolean isIcon = mouseX >= x + clickSize  && mouseX <= x + CART_ITEM_TEXTURE.renderWidth + clickSize;
                if (isIcon) {
                    return new ArrayList<>(cartItems.keySet()).get(predictedIndex);
                }
            }
            return null;
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean isClicked = active;
            isClicked &= visible;
            isClicked &= mouseX >= x && mouseX <= x + width;
            isClicked &= mouseY >= y && mouseY <= y + height;
            if (!isClicked) {
                return false;
            }
            int predictedIndex = (int) ((mouseY + getScrollY() - y) / OfferGrid.Offer.SIZE);
            if (cartItems.size() > predictedIndex) {
                int clickSize = 16;
                boolean isLeftSide = mouseX >= x && mouseX <= x + clickSize;
                boolean isRightSide = mouseX >= x + CART_ITEM_TEXTURE.renderWidth - clickSize && mouseX <= x + CART_ITEM_TEXTURE.renderWidth;
                ArrayList<OfferGrid.Offer> offers = new ArrayList<>(cartItems.keySet());
                OfferGrid.Offer predictedOffer = offers.get(predictedIndex);
                if (isLeftSide) {
                    removeItem(predictedOffer);
                }
                if (isRightSide) {
                    addItem(predictedOffer);
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }


    class OfferGrid extends ScrollableWidget {
        private static final int SCROLL_WIDTH = 5;
        private final int entriesPerRow;
        ArrayList<Offer> entries = new ArrayList<>();

        public OfferGrid(int x, int y, int width, int height) {
            super(x, y, width, height, Text.empty());
            entriesPerRow = width / Offer.SIZE;
            components.add(this);
        }

        public void loadOffers(Category category) {
            entries.clear();
            int currentLevel = parent.getScreenHandler().getCard().getLevel();
            for(StockEntry stockEntry : MemberStock.STOCK.get(category.name)) {
                //  if (currentLevel > stockEntry.requiredLevel) {//TODO uncomment when finished with member screen
                entries.add(new Offer(stockEntry));
                //  }

            }for(StockEntry stockEntry : MemberStock.STOCK.get(MemberStock.LUMBERJACK_STOCK_KEY)) {
                //  if (currentLevel > stockEntry.requiredLevel) {//TODO uncomment when finished with member screen
                entries.add(new Offer(stockEntry));
                //  }

            }
        }


        @Override
        public boolean isHovered() {
            return hovered;
        }

        private class Offer {
            static int SIZE = 16;
            StockEntry stockEntry;
            Offer(StockEntry stockEntry) {
                this.stockEntry = stockEntry;
            }

            //(ItemStack stack, ModelTransformation.Mode renderMode, boolean leftHanded, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, BakedModel model) {
            void render(MatrixStack matrixStack, int x, int y) {
                OFFER_TEXTURE.render(matrixStack, x, y);
                client.getItemRenderer().renderGuiItemIcon(stockEntry.item.getDefaultStack(), x, (int) (y - getScrollY()));
            }

            public boolean mouseClicked(double mouseX, double mouseY, int button){
                switch (button) {
                    case 0 -> addItem(this);
                    case 1 -> removeItem(this);
                }
                return true;
            }
        }


        @Override
        protected int getMaxScrollY() {
            return Math.max(0, getContentsHeight() - this.height);
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
            return this.getContentsHeight();
        }

        private int getScrollbarThumbHeight() {
            return MathHelper.clamp((int)((int)((float)(this.height * this.height) / (float)this.getContentsHeightWithPadding())), (int)32, (int)this.height);
        }

        @Override
        protected void renderOverlay(MatrixStack matrices) {
            int i = this.getScrollbarThumbHeight();
            int x0 = this.x + this.width - 4;
            int x1 = this.x + this.width;
            int y0 = Math.max(this.y, this.getMaxScrollY() == 0 ? this.y : (int)this.getScrollY() * (this.height - i) / this.getMaxScrollY() + this.y);
            int y1 = y0 + i;
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder bufferBuilder = tessellator.getBuffer();
            bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
            int color1 = 0xffbb8f1b;
            int color2 = 0xff4b2f00;
            bufferBuilder.vertex(x0, y1, 0).color(color2).next();
            bufferBuilder.vertex(x1, y1, 0).color(color2).next();
            bufferBuilder.vertex(x1, y0, 0).color(color2).next();
            bufferBuilder.vertex(x0, y0, 0).color(color2).next();
            bufferBuilder.vertex(x0, (y1 - 1), 0).color(color1).next();
            bufferBuilder.vertex((x1 - 1), (y1 - 1), 0).color(color1).next();
            bufferBuilder.vertex((x1 - 1), y0, 0).color(color1).next();
            bufferBuilder.vertex(x0, y0, 0).color(color1).next();
            tessellator.draw();
        }

        @Override
        public boolean isFocused() {
            return super.isFocused() || isHovered();
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (this.visible) {
                this.drawBox(matrices);
                enableScissor(this.x, this.y, this.x + this.width, this.y + this.height);
                matrices.push();
                matrices.translate(0, -this.getScrollY(), 0);
                this.renderContents(matrices, mouseX, mouseY, delta);
                matrices.pop();
                disableScissor();
                this.renderOverlay(matrices);
            }
        }

        private void drawBox(MatrixStack matrices) {
            fill(matrices, this.x, this.y, this.x + this.width, this.y + this.height, 0xff272946);
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean isClicked = active;
            isClicked &= visible;
            isClicked &= mouseX >= x && mouseX <= x + width;
            isClicked &= mouseY >= y && mouseY <= y + height;
            if (!isClicked) {
                return false;
            }
            int predictedX = (int) ((mouseX - x) / Offer.SIZE);
            int predictedY = (int) ((mouseY + getScrollY() - y) / Offer.SIZE);
            int predictedIndex = predictedX + predictedY * entriesPerRow;
            if (entries.size() > predictedIndex) {
                if (entries.get(predictedIndex).mouseClicked(mouseX, mouseY, button)) {
                    return true;
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        private OfferGrid.Offer offerAtMouse(double mouseX, double mouseY) {
            int predictedX = (int) ((mouseX - x) / Offer.SIZE);
            int predictedY = (int) ((mouseY + getScrollY() - y) / Offer.SIZE);
            int predictedIndex = predictedX + predictedY * entriesPerRow;
            if (predictedIndex >= 0 && predictedIndex < entries.size()) {
                return entries.get(predictedIndex);
            }
            return null;
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (isHovered()) {
                focusedOffer = offerAtMouse(mouseX, mouseY);
            }
            super.render(matrices, mouseX, mouseY, delta);
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

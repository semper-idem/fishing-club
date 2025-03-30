package net.semperidem.fishingclub.client.screen.member;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.fisher.shop.OrderItem;
import net.semperidem.fishingclub.fisher.shop.StockEntry;
import net.semperidem.fishingclub.network.payload.CheckoutPayload;
import net.semperidem.fishingclub.registry.Keybindings;

import java.util.*;

import static java.lang.String.format;
import static net.minecraft.text.Text.literal;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.*;
import static net.semperidem.fishingclub.fisher.shop.MemberStock.*;


public class MemberBuyScreen extends MemberSubScreen {

    public static final Texture OFFER_TEXTURE = new Texture(
            FishingClub.identifier("textures/gui/offer_background.png"),
            16,
            16
    );
    static final Texture CART_ITEM_TEXTURE = new Texture(
            FishingClub.identifier("textures/gui/cart_item.png"),
            48,
            64,
            48,
            64,
            1,
            4
    );

    private static final Text BUY_BUTTON_TEXT = literal("Buy");;
    private static final Text CLEAR_BUTTON_TEXT = literal("x");
    private static final Text TOTAL_TEXT = literal("Total:");

    //TODO Change to String literally when updating to 1.21(where min java is 21)
    private static final String TOOLTIP_TOTAL_STRING = "Total: §6%s$";
    private static final String TOOLTIP_PRICE_STRING = "Price: §6%.0f$";
    private static final String TOOLTIP_DISCOUNT_STRING = "Discount: -§6%.0f$§r for every §3%o";
    private static final String TOOLTIP_MIN_PRICE_STRING = "Min Price: §6%.0f$";

    private static final int CART_WIDGET_WIDTH = CART_ITEM_TEXTURE.renderWidth + TILE_SIZE;
    private static final int CART_WIDGET_HEIGHT = TILE_SIZE * 20;
    private static final int OFFER_GRID_WIDTH = TILE_SIZE * 25;
    private static final int OFFER_GRID_HEIGHT = TILE_SIZE * 24;
    private static final int BUY_BUTTON_WIDTH = TILE_SIZE  * 18;
    private static final int CLEAR_BUTTON_WIDTH = TILE_SIZE  * 5;
    private static final int BUTTON_HEIGHT = TILE_SIZE * 5;
    private static final int TEXT_LINE_HEIGHT = 12;


    private int baseX, baseY;
    private int categoryButtonX, categoryButtonY;
    private int categoryButtonCount;
    private int offerGridX, offerGridY;
    private int cartWidgetX, cartWidgetY;
    private int buyButtonX;
    private int clearButtonX;
    private int buttonY;

    int buttonBoxX0, buttonBoxX1, buttonBoxY0, buttonBoxY1;
    int totalTextX, totalTextY;

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


    OfferGrid.Offer focusedOffer;

    public MemberBuyScreen(MemberScreen parent, Text title) {
        super(parent, title);
    }


    private void setCategory(Category category) {
        this.currentCategory = category;
        offerGrid.loadOffers(this.currentCategory);
    }

    public void setupElementPosition() {
        baseX = parent.x + TILE_SIZE * 10;
        baseY = parent.y + TILE_SIZE * 5;
        categoryButtonX = baseX;
        categoryButtonY = baseY;

        offerGridX = baseX + CategoryButton.SIZE + TILE_SIZE;
        offerGridY = baseY;
        cartWidgetX = offerGridX + OFFER_GRID_WIDTH + TILE_SIZE * 3;
        cartWidgetY = offerGridY + TILE_SIZE * 3;

        buyButtonX = parent.x + TEXTURE.renderWidth - 42 * TILE_SIZE;
        buttonY = parent.y + TEXTURE.renderHeight - BUTTON_HEIGHT - TILE_SIZE * 2;
        clearButtonX = buyButtonX - CLEAR_BUTTON_WIDTH;

        buttonBoxX0 = clearButtonX;
        buttonBoxX1 = buyButtonX + BUY_BUTTON_WIDTH;
        buttonBoxY0 = buttonY - TEXT_LINE_HEIGHT - 2;
        buttonBoxY1 = buttonY + BUTTON_HEIGHT;

        totalTextX =  buttonBoxX0 + 3;
        totalTextY = buttonBoxY0 + 4;
    }


    private void initCategoryButtons() {
        categoryButtonCount = 0;
        fisherCategoryButton = new CategoryButton(new Category(FISHER_STOCK_KEY, Items.FISHING_ROD, 0));
        minerCategoryButton = new CategoryButton(new Category(FISHER_STOCK_KEY, Items.IRON_PICKAXE, 10));
        lumberjackCategoryButton = new CategoryButton(new Category(LUMBERJACK_STOCK_KEY, Items.IRON_AXE, 0));
        alchemistCategoryButton = new CategoryButton(new Category(FISHER_STOCK_KEY, Items.BREWING_STAND, 20));
        librarianCategoryButton = new CategoryButton(new Category(FISHER_STOCK_KEY, Items.ENCHANTED_BOOK, 30));
        adventurerCategoryButton = new CategoryButton(new Category(FISHER_STOCK_KEY, Items.FILLED_MAP, 30));
    }

    private void initOfferGrid() {
        offerGrid = new OfferGrid(offerGridX, offerGridY, OFFER_GRID_WIDTH, OFFER_GRID_HEIGHT);
        components.add(offerGrid);
    }

    private void initCartWidget() {
        cartWidget = new CartWidget(cartWidgetX, cartWidgetY, CART_WIDGET_WIDTH, CART_WIDGET_HEIGHT);
        components.add(cartWidget);
    }

    private void initButtons() {
        buyButton = new MemberButton(buyButtonX, buttonY, BUY_BUTTON_WIDTH , BUTTON_HEIGHT, BUY_BUTTON_TEXT, onBuy());
        components.add(buyButton);

        clearButton = new MemberButton(buyButtonX - CLEAR_BUTTON_WIDTH, buttonY, CLEAR_BUTTON_WIDTH, BUTTON_HEIGHT, CLEAR_BUTTON_TEXT, onClear());
        components.add(clearButton);
    }

    public void init() {
        super.init();
        setupElementPosition();

        initCategoryButtons();
        initOfferGrid();
        initCartWidget();
        initButtons();

        setCategory(fisherCategoryButton.category);
    }

    private ButtonWidget.PressAction onBuy() {
        return button -> {
            ArrayList<OrderItem> cart = new ArrayList<>();
            for(OfferGrid.Offer offer : cartWidget.cartItems.keySet()) {
                ItemStack offerStack = offer.stockEntry.item.getDefaultStack();
                int count = cartWidget.cartItems.get(offer);
                offerStack.setCount(count);
                cart.add(new OrderItem(Optional.of(offerStack), count, Optional.empty()));
            }
            ClientPlayNetworking.send(new CheckoutPayload(cart));
            clearCart();
        };
    }

    private ButtonWidget.PressAction onClear() {
        return button -> clearCart();
    }

    private void clearCart() {
        cartWidget.cartItems.clear();
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        parent.drawContainerBox(context, buttonBoxX0, buttonBoxY0, buttonBoxX1, buttonBoxY1, true);
        context.drawTextWithShadow(textRenderer, TOTAL_TEXT, totalTextX, totalTextY, BEIGE_TEXT_COLOR);
        String cartPrice = cartWidget.getCartTotal();
        context.drawTextWithShadow(textRenderer, cartPrice, buttonBoxX1 - textRenderer.getWidth(cartPrice) -  3, totalTextY, BEIGE_TEXT_COLOR);
        super.render(context, mouseX, mouseY, delta);
        if (!cartWidget.isSelected() && !offerGrid.isSelected()) {
            this.focusedOffer = null;
        }

        renderTooltip(context, mouseX, mouseY);

    }

    private void renderTooltip(DrawContext context, int mouseX, int mouseY) {
        if (this.focusedOffer != null) {
            StockEntry entry = this.focusedOffer.stockEntry;
            List<Text> tooltip = entry.item.getDefaultStack().getTooltip(Item.TooltipContext.DEFAULT, MinecraftClient.getInstance().player, TooltipType.BASIC);;
            if (cartWidget.cartItems.containsKey(this.focusedOffer)) {
                int total = (int) entry.getPriceFor(cartWidget.cartItems.get(this.focusedOffer));
                tooltip.add(literal(format(TOOLTIP_TOTAL_STRING, total)));
            }
            tooltip.add(literal(format(TOOLTIP_PRICE_STRING, entry.price)));
            if (entry.discount != 0) {
                tooltip.add(literal(format(TOOLTIP_DISCOUNT_STRING, entry.discount, entry.discountPer)));
                tooltip.add(literal(format(TOOLTIP_MIN_PRICE_STRING, entry.minPrice)));
            }
            context.drawTooltip(textRenderer, tooltip, mouseX, mouseY);
        }
    }


    private void addItem(OfferGrid.Offer offer) {
        cartWidget.addItem(offer);
    }

    private void removeItem(OfferGrid.Offer offer) {
        cartWidget.removeItem(offer);
    }

    class CartWidget extends MemberScrollableWidget {

        HashMap<OfferGrid.Offer, Integer> cartItems = new HashMap<>();

        public CartWidget(int x, int y, int width, int height) {
            super(x, y, width, height);
        }
        private void addItem(OfferGrid.Offer offer) {
            cartItems.put(offer, Math.min(999, cartItems.getOrDefault(offer, 0) + (getActionCount())));
        }
        private void removeItem(OfferGrid.Offer offer) {
            cartItems.put(offer, cartItems.get(offer) - (getActionCount()));
            HashMap<OfferGrid.Offer, Integer> updatedCartItem = new HashMap<>();
            for(OfferGrid.Offer off : cartItems.keySet()) {
                int newCount = cartItems.get(off);
                if (newCount > 0 ) {
                    updatedCartItem.put(off, newCount);
                }
            }
            cartItems = updatedCartItem;
        }
        private int getActionCount() {
            int count = Keybindings.MULTIPLY_CART_ACTION_1.isPressed() ? 4 : 1;
            count *= Keybindings.MULTIPLY_CART_ACTION_2.isPressed() ? 4 : 1;
            return count;
        }
        protected int getContentsHeight() {
            return cartItems.size() * OfferGrid.Offer.SIZE;
        }

        @Override
        public boolean isSelected() {
            return hovered;
        }

        @Override
        protected double getDeltaYPerScroll() {
            return OfferGrid.Offer.SIZE;
        }

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

        protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
            int index = 0;
            for (OfferGrid.Offer offer : cartItems.keySet()) {
                CART_ITEM_TEXTURE.render(context, getX(), getY() + index * OfferGrid.Offer.SIZE);
                ItemStack stackToRender = offer.stockEntry.item.getDefaultStack();
                stackToRender.setCount(cartItems.get(offer));
//                context.drawItemInSlot(
//                        textRenderer,
//                        stackToRender,
//                        getX() + ((CART_ITEM_TEXTURE.renderWidth - OfferGrid.Offer.SIZE) / 2),
//                        (int) (getY() + index * OfferGrid.Offer.SIZE - getScrollY()));
//                context.drawItemInSlot(
//                        textRenderer,
//                        stackToRender,
//                        getX() + ((CART_ITEM_TEXTURE.renderWidth - OfferGrid.Offer.SIZE) / 2),
//                        (int) (getY() + index * OfferGrid.Offer.SIZE - getScrollY()));
                index++;
                //next add to components
            }
        }
        @Override
        public boolean isFocused() {
            return super.isFocused() || isSelected();
        }
        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            if (this.visible) {
                if (hovered) {
                    focusedOffer = offerAtMouse(mouseX, mouseY);
                }
                parent.drawContainerBox(context, getX(), getY(), getX() + this.width, getX() + this.height, true);
                context.drawTextWithShadow(textRenderer,"Cart: (" + getStacksInCart() + ")", getX(), getY() - TILE_SIZE * 2 - 2 , BEIGE_TEXT_COLOR);
                context.enableScissor(getX(), getY(), getX() + this.width, getX() + this.height);
                context.getMatrices().push();
                context.getMatrices().translate(0, -this.getScrollY(), 0);
                this.renderContents(context, mouseX, mouseY, delta);
                context.getMatrices().pop();
                context.disableScissor();
//                this.renderOverlay(context);
            }
        }

        private OfferGrid.Offer offerAtMouse(double mouseX, double mouseY) {
            if (!hovered) {
                return null;
            }
            int predictedIndex = (int) ((mouseY + getScrollY() - getY()) / OfferGrid.Offer.SIZE);
            if (cartItems.size() > predictedIndex && predictedIndex >= 0) {
                int clickSize = 16;
                boolean isIcon = mouseX >= getX() + clickSize  && mouseX <= getX() + CART_ITEM_TEXTURE.renderWidth + clickSize;
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
            isClicked &= mouseX >= getX() && mouseX <= getX() + width;
            isClicked &= mouseY >= getY() && mouseY <= getY() + height;
            if (!isClicked) {
                return false;
            }
            int predictedIndex = (int) ((mouseY + getScrollY() - getY()) / OfferGrid.Offer.SIZE);
            if (cartItems.size() > predictedIndex) {
                int clickSize = 16;
                boolean isLeftSide = mouseX >= getX() && mouseX <= getX() + clickSize;
                boolean isRightSide = mouseX >= getX() + CART_ITEM_TEXTURE.renderWidth - clickSize && mouseX <= getX() + CART_ITEM_TEXTURE.renderWidth;
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


    class OfferGrid extends MemberScrollableWidget {
        private final int entriesPerRow;
        ArrayList<Offer> entries = new ArrayList<>();

        public OfferGrid(int x, int y, int width, int height) {
            super(x, y, width, height);
            entriesPerRow = width / Offer.SIZE;
        }

        public void loadOffers(Category category) {
            entries.clear();
            int currentLevel = parent.getScreenHandler().getCard().getLevel();
            for(StockEntry stockEntry : STOCK.get(category.name)) {
                //  if (currentLevel > stockEntry.requiredLevel) {//TODO uncomment when finished with member screen
                entries.add(new Offer(stockEntry));
                //  }

            }for(StockEntry stockEntry : STOCK.get(LUMBERJACK_STOCK_KEY)) {
                //  if (currentLevel > stockEntry.requiredLevel) {//TODO uncomment when finished with member screen
                entries.add(new Offer(stockEntry));
                //  }

            }
        }


        @Override
        public boolean isSelected() {
            return hovered;
        }



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
        protected double getDeltaYPerScroll() {
            return Offer.SIZE;
        }



        @Override
        public boolean isFocused() {
            return super.isFocused() || isSelected();
        }

        @Override
        public void renderWidget(DrawContext drawContext, int mouseX, int mouseY, float delta) {
            if (this.visible) {
                if (isSelected()) {
                    focusedOffer = offerAtMouse(mouseX, mouseY);
                }
                parent.drawContainerBox(drawContext, getX(), getY(), getX() + this.width, getY() + this.height, false);
                drawContext.enableScissor(getX(), getY(), getX() + this.width, getY() + this.height);
                drawContext.getMatrices().push();
                drawContext.getMatrices().translate(0, -this.getScrollY(), 0);
                this.renderContents(drawContext, mouseX, mouseY, delta);
                drawContext.getMatrices().pop();
                drawContext.disableScissor();
//                this.renderOverlay(drawContext);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            boolean isClicked = active;
            isClicked &= visible;
            isClicked &= mouseX >= getX() && mouseX <= getX() + width;
            isClicked &= mouseY >= getY() && mouseY <= getY() + height;
            if (!isClicked) {
                return false;
            }
            int predictedX = (int) ((mouseX - getX()) / Offer.SIZE);
            int predictedY = (int) ((mouseY + getScrollY() - getY()) / Offer.SIZE);
            int predictedIndex = predictedX + predictedY * entriesPerRow;
            if (entries.size() > predictedIndex) {
                if (entries.get(predictedIndex).mouseClicked(button)) {
                    return true;
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        private OfferGrid.Offer offerAtMouse(double mouseX, double mouseY) {
            int predictedX = (int) ((mouseX - getX()) / Offer.SIZE);
            int predictedY = (int) ((mouseY + getScrollY() - getY()) / Offer.SIZE);
            int predictedIndex = predictedX + predictedY * entriesPerRow;
            if (predictedIndex >= 0 && predictedIndex < entries.size()) {
                return entries.get(predictedIndex);
            }
            return null;
        }


        protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
            int offerY = getY();
            int rows = getRows();
            Iterator<Offer> iterator = entries.iterator();
            for(int i = 0; i < rows; i++) {
                int offerX = getX();
                for(int j = 0; j < entriesPerRow; j++) {
                    if (!iterator.hasNext()) {
                        return;
                    }
                    iterator.next().render(context, offerX, offerY);
                    offerX += Offer.SIZE;
                }
                offerY += Offer.SIZE;
            }

        }

        private class Offer {
            static int SIZE = 16;
            StockEntry stockEntry;
            Offer(StockEntry stockEntry) {
                this.stockEntry = stockEntry;
            }

            void render(DrawContext context, int x, int y) {
                OFFER_TEXTURE.render(context, x, y);
                context.drawItem(stockEntry.item.getDefaultStack(), x, (int) (y - getScrollY()));
            }

            public boolean mouseClicked(int button){
                switch (button) {
                    case 0 -> addItem(this);
                    case 1 -> removeItem(this);
                }
                return true;
            }
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
                FishingClub.identifier("textures/gui/member_category_button.png"),
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
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            int textureIndex = 0;
            if (currentCategory == category) {
                textureIndex = 1;
            } else if (hovered) {
                textureIndex = 3;
            } else if (active){
                textureIndex = 2;
            }
            CATEGORY_BUTTON.render(context, getX(), getY(), 0, SIZE * textureIndex, width, height);
            context.drawItem(category.itemIcon.getDefaultStack(), getX(), getY());
            context.drawItem(active ? Items.GLASS_PANE.getDefaultStack() : Items.GRAY_STAINED_GLASS_PANE.getDefaultStack(), getX(), getY());
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

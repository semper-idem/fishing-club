package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.FireworkRocketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.network.ClientPacketSender;
import net.semperidem.fishingclub.registry.KeybindingRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static net.semperidem.fishingclub.client.screen.member.MemberButton.SMALL_BUTTON_TEXTURE;
import static net.semperidem.fishingclub.client.screen.member.MemberBuyScreen.OFFER_TEXTURE;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.*;

public class MemberFireworkScreen extends MemberSubScreen {
    private static final int BUTTON_WIDTH = TILE_SIZE * 32;
    private static final int SMALL_BUTTON_WIDTH = TILE_SIZE * 16;
    private static final int BUTTON_HEIGHT = TILE_SIZE * 4;


    static final Texture FIREWORK_CREATOR_TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/firework_creator.png"),
            64,
            64,
            64,
            64,
            1,
            4
    );


    static final Texture CART_ITEM_TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/cart_item.png"),
            48,
            64,
            48,
            64,
            1,
            4
    );

    private int colorPickerX, colorPickerY;
    private int colorPickerWidth, colorPickerHeight;
    private int fadePickerX, fadePickerY;
    private int flickerButtonX, flickerButtonY;
    private int trailButtonX, trailButtonY;
    private int shapeButtonX, shapeButtonY;
    private int rangeButtonX, rangeButtonY;
    private int priceTextX, priceTextY;
    private int priceValueMaxWidth;
    private int previewFireworkX, previewFireworkY;
    private int previewFireworkXStart,previewFireworkXEnd;
    private int buttonBoxX0, buttonBoxY0,buttonBoxX1,buttonBoxY1;


    private ColorPickerWidget colorPickerWidget;
    private ColorPickerWidget fadePickerWidget;

    private static final int TEXT_LINE_HEIGHT = 12;
    private static final Text COLOR_PICKER_TEXT = Text.literal("Primary:");
    private static final Text FADE_PICKER_TEXT = Text.literal("Fade:");
    private static final String PRICE_STRING = "Price: ";
    private static final Text PRICE_TEXT = Text.literal(PRICE_STRING);
    private static final String shapeText = "Shape: ";
    private static final String rangeText = "Distance: ";
    private static final String flickerText = "Flicker: ";
    private static final String trailText = "Trail: ";

    private static ToggleButton shapeButton;
    private static ToggleButton rangeButton;
    private static ToggleButton flickerButton;
    private static ToggleButton trailButton;

    private static final int MIN_RANGE = 1, MAX_RANGE = 3;
    boolean flicker = false;
    boolean trail = false;
    int range = 1;
    Shape shape = Shape.NONE;
    Iterator<Shape> shapeIterator = Arrays.stream(Shape.values()).iterator();

    private static final int[] BASE_PRICE = {20, 50, 100};
    private static final int COST_PER_COLOR = 25;
    private static final int COST_PER_FADE = 10;
    private static final float FLICKER_COST_MULTIPLIER = 1.5f;
    private static final float TRAIL_COST_MULTIPLIER = 1.25f;

    private static final int CART_WIDGET_WIDTH = CART_ITEM_TEXTURE.renderWidth + TILE_SIZE;
    private static final int CART_WIDGET_HEIGHT = TILE_SIZE * 12;

    ArrayList<Explosion> explosions = new ArrayList<>();

    ItemStack fireworkStack = Items.FIREWORK_ROCKET.getDefaultStack();
    private NbtCompound fireworkNbt = new NbtCompound();

    MemberButton buyButton;
    CartWidget cartWidget;
    private int buyButtonX, buyButtonY;
    private int cartWidgetX, cartWidgetY;
    int totalTextX, totalTextY;
    String totalString = "Total:";
    Text TOTAL_TEXT = Text.literal(totalString);
    int totalMaxWidth;


    enum Shape {
        NONE("None", 0),  SMALL_BALL("Small ball", 25), LARGE_BALL("Large Ball", 100), STAR("Star", 50), BURST("Burst", 50), CREEPER("Creeper", 100);
        final String name;
        final float basePrice;
        Shape(String name, int basePrice) {
            this.name = name;
            this.basePrice = basePrice;
        }
    }
    /*
     * Fireworks setup
     * shape effect  none/firecharge/gold nugget/head/feather
     * color picker (multi-select) // up to 8 could be more but lets not
     * range 1-3
     * twinkle trail checkboxes
     * fade color picker (multiselct)
     *
     *
     *
     * + / - count
     * buy button
     * current creation price text
     * total price
     * */
    public MemberFireworkScreen(MemberScreen parent, Text title) {
        super(parent, title);
    }

    @Override
    public int unlockLevel() {
        return 30;
    }

    @Override
    public void init() {
        super.init();

        colorPickerX = parent.x + TILE_SIZE * 12;
        colorPickerY = parent.y + TILE_SIZE * 6;
        colorPickerWidth = ColorPickerWidget.COLOR_SIZE * 4 + 10;
        colorPickerHeight = ColorPickerWidget.COLOR_SIZE * 4 + 10 + TEXT_LINE_HEIGHT;

        fadePickerX = colorPickerX + colorPickerWidth + TILE_SIZE * 3;
        fadePickerY = colorPickerY;

        colorPickerWidget = new ColorPickerWidget(colorPickerX, colorPickerY, colorPickerWidth, colorPickerHeight, COLOR_PICKER_TEXT);
        components.add(colorPickerWidget);
        fadePickerWidget = new ColorPickerWidget(fadePickerX, fadePickerY, colorPickerWidth, colorPickerHeight, FADE_PICKER_TEXT);
        components.add(fadePickerWidget);

        shapeButtonX = colorPickerX - 2;
        shapeButtonY = colorPickerY + colorPickerHeight + TILE_SIZE;
        rangeButtonX = shapeButtonX + BUTTON_WIDTH + TILE_SIZE + 2;
        rangeButtonY = shapeButtonY;
        flickerButtonX = rangeButtonX;
        flickerButtonY = rangeButtonY - BUTTON_HEIGHT - TILE_SIZE;
        trailButtonX = rangeButtonX;
        trailButtonY = flickerButtonY - BUTTON_HEIGHT - TILE_SIZE;


        priceTextX = rangeButtonX;
        priceTextY = colorPickerY;
        priceValueMaxWidth = textRenderer.getWidth(PRICE_STRING + "XXXX$") + 3;

        previewFireworkX = priceTextX;
        previewFireworkY = priceTextY + TEXT_LINE_HEIGHT + 2;
        previewFireworkXStart = previewFireworkX + 16;
        previewFireworkXEnd = previewFireworkXStart + 16;

        cartWidgetX = previewFireworkX + FIREWORK_CREATOR_TEXTURE.renderWidth + TILE_SIZE * 4 + 2;
        cartWidgetY = previewFireworkY - TILE_SIZE;
        cartWidget = new CartWidget(cartWidgetX, cartWidgetY, CART_WIDGET_WIDTH, CART_WIDGET_HEIGHT);

        totalTextX = previewFireworkX + FIREWORK_CREATOR_TEXTURE.renderWidth + TILE_SIZE * 2 + 2;
        totalTextY = cartWidgetY + CART_WIDGET_HEIGHT + TILE_SIZE + 2;
        totalMaxWidth = textRenderer.getWidth(totalString + "999999$");

        buyButtonX = previewFireworkX + FIREWORK_CREATOR_TEXTURE.renderWidth + TILE_SIZE * 2;
        buyButtonY = totalTextY + TEXT_LINE_HEIGHT - 2;
        buyButton = new MemberButton(buyButtonX, buyButtonY, SMALL_BUTTON_WIDTH + TILE_SIZE * 2, BUTTON_HEIGHT, Text.literal("Checkout"), button -> {
            ClientPacketSender.checkout(cartWidget.getItems(), cartWidget.getCartTotalAmount());
            resetValues();
            cartWidget.entries.clear();
        });
        buyButton.setTexture(SMALL_BUTTON_TEXTURE);
        components.add(buyButton);

        buttonBoxX0 = buyButtonX;
        buttonBoxY0 = totalTextY - 2;
        buttonBoxX1 = buyButtonX + SMALL_BUTTON_WIDTH + TILE_SIZE * 2;
        buttonBoxY1 = buyButtonY + BUTTON_HEIGHT;
        components.add(cartWidget);

        rangeButton = new ToggleButton(rangeButtonX,rangeButtonY,SMALL_BUTTON_WIDTH,BUTTON_HEIGHT,Text.literal(rangeText + range), button -> {
            if (range >= MAX_RANGE) {
                range = MIN_RANGE;
            } else {
                range++;
            }
            button.setMessage(Text.literal(rangeText + range));
            updateNbt();
        });
        components.add(rangeButton);

        flickerButton = new ToggleButton(flickerButtonX, flickerButtonY, SMALL_BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal(flickerText + (flicker ? "Yes" : "No")), button -> {
            flicker = !flicker;
            button.setMessage(Text.literal(flickerText + (flicker ? "Yes" : "No")));
        });
        components.add(flickerButton);

        trailButton = new ToggleButton(trailButtonX, trailButtonY, SMALL_BUTTON_WIDTH, BUTTON_HEIGHT, Text.literal(trailText + (trail ? "Yes" : "No")), button -> {
            trail = !trail;
            button.setMessage(Text.literal(trailText + (trail ? "Yes" : "No")));
        });
        components.add(trailButton);


        shapeButton = new ToggleButton(shapeButtonX,shapeButtonY,BUTTON_WIDTH,BUTTON_HEIGHT,Text.literal(shapeText + shape.name), button -> {
            if (!shapeIterator.hasNext()) {
                shapeIterator = Arrays.stream(Shape.values()).iterator();
            }
            shape = shapeIterator.next();
            button.setMessage(Text.literal(shapeText + shape.name));

            if (shape == Shape.NONE) {
                trailButton.active = false;
                flickerButton.active = false;
                colorPickerWidget.active = false;
                fadePickerWidget.active = false;
            } else {
                trailButton.active = true;
                flickerButton.active = true;
                colorPickerWidget.active = true;
                fadePickerWidget.active = true;
            }
        });

        trailButton.active = false;
        flickerButton.active = false;
        colorPickerWidget.active = false;
        fadePickerWidget.active = false;

        shapeButton.setTexture(MemberButton.SMALL_WIDE_BUTTON_TEXTURE);
        components.add(shapeButton);

        updateNbt();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean isHoveringRemove = mouseX >= previewFireworkX && mouseX <= previewFireworkXStart;
        boolean isHoveringAdd = mouseX >= previewFireworkXEnd && mouseX <= previewFireworkXEnd + 16;
        boolean isHoveringConfirm = mouseX >= previewFireworkX + FIREWORK_CREATOR_TEXTURE.renderWidth - 16 && mouseX <= previewFireworkX + FIREWORK_CREATOR_TEXTURE.renderWidth;
        boolean isHoveringY = mouseY >= previewFireworkY && mouseY <= previewFireworkY + OFFER_TEXTURE.renderHeight;
        if (isHoveringRemove && isHoveringY) {
            if (!explosions.isEmpty()) {
                explosions.remove(explosions.size() - 1);
            }
            if (!explosions.isEmpty()) {
                Explosion last = explosions.get(explosions.size() - 1);
                this.shape = last.shape;
                this.colorPickerWidget.colorsPicked = last.colors;
                this.fadePickerWidget.colorsPicked = last.fadeColors;
                this.flicker = last.flicker;
                this.trail = last.trail;
            } else {
                resetValues();
            }
            updateNbt();
            return true;
        } else if (isHoveringAdd && isHoveringY) {
            if (shape != Shape.NONE && !colorPickerWidget.colorsPicked.isEmpty()) {
                explosions.add(new Explosion(shape, colorPickerWidget.colorsPicked, fadePickerWidget.colorsPicked, flicker, trail));
                resetValues();
                updateNbt();
            }
            return true;
        } else if (isHoveringConfirm && isHoveringY) {
            addToCart();
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    private void addToCart() {
        updateNbt();
        cartWidget.addItem(fireworkStack, getPrice());
        fireworkStack = Items.FIREWORK_ROCKET.getDefaultStack();
        explosions = new ArrayList<>();
        resetValues();
        updateNbt();
    }

    private void resetValues() {
        this.shape = Shape.NONE;
        this.colorPickerWidget.colorsPicked = new ArrayList<>();
        this.fadePickerWidget.colorsPicked = new ArrayList<>();
        this.flicker = false;
        this.trail = false;
        shapeButton.setMessage(Text.literal(shapeText + shape.name));
        flickerButton.setMessage(Text.literal(flickerText + (flicker ? "Yes" : "No")));
        trailButton.setMessage(Text.literal(trailText + (trail ? "Yes" : "No")));

    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        drawTextWithShadow(matrixStack, textRenderer, PRICE_TEXT, priceTextX, priceTextY, BEIGE_TEXT_COLOR);
        String price = getPrice() + "$";
        int priceWidth = textRenderer.getWidth(price);
        drawTextWithShadow(matrixStack, textRenderer, Text.literal(price), priceTextX + priceValueMaxWidth - priceWidth, priceTextY, BEIGE_TEXT_COLOR);
        drawTextWithShadow(matrixStack, textRenderer, PRICE_TEXT, priceTextX, priceTextY, BEIGE_TEXT_COLOR);

        int mode = 2;
        if (shape == Shape.NONE && !explosions.isEmpty()) {
            mode = 1;
        }
        if (shape != Shape.NONE && !explosions.isEmpty()) {
            mode = 0;
        }
        if (shape != Shape.NONE && explosions.isEmpty() && !colorPickerWidget.colorsPicked.isEmpty()) {
            mode = 3;
        }
        FIREWORK_CREATOR_TEXTURE.render(matrixStack, previewFireworkX, previewFireworkY, 0, mode);
        itemRenderer.renderInGui(fireworkStack, previewFireworkX + 16, previewFireworkY);
        parent.drawContainerBox(matrixStack, buttonBoxX0, buttonBoxY0, buttonBoxX1, buttonBoxY1, true);
        super.render(matrixStack, mouseX, mouseY, delta);

        textRenderer.drawWithShadow(matrixStack, TOTAL_TEXT, totalTextX, totalTextY, BEIGE_TEXT_COLOR);
        String totalValueString = cartWidget.getCartTotal();
        int totalValueX = totalTextX + totalMaxWidth - textRenderer.getWidth(totalValueString);
        textRenderer.drawWithShadow(matrixStack, TOTAL_TEXT, totalTextX, totalTextY, BEIGE_TEXT_COLOR);
        textRenderer.drawWithShadow(matrixStack, totalValueString, totalValueX, totalTextY, BEIGE_TEXT_COLOR);

        boolean isHoveringX = mouseX >= previewFireworkXStart && mouseX <= previewFireworkXStart + 16;
        boolean isHoveringY = mouseY >= previewFireworkY && mouseY <= previewFireworkY + OFFER_TEXTURE.renderHeight;
        boolean hoveringFirework = isHoveringX && isHoveringY;
        if (hoveringFirework) {
            List<Text> tooltip = parent.getTooltipFromItem(fireworkStack);
            parent.renderTooltip(matrixStack, tooltip, mouseX, mouseY);
        }
        if (cartWidget.hoveredStack != null && cartWidget.isHovered()) {
            parent.renderTooltip(matrixStack, parent.getTooltipFromItem(cartWidget.hoveredStack.stack), mouseX, mouseY);
        }
    }

    private void updateNbt() {
        NbtCompound nbt = fireworkStack.getOrCreateNbt();
        NbtList explosionsNbt = new NbtList();
        for(Explosion explosion : explosions) {
            explosionsNbt.add(explosion.getNbt());
        }
        NbtCompound fireworkNbt = new NbtCompound();
        fireworkNbt.put(FireworkRocketItem.EXPLOSIONS_KEY, explosionsNbt);
        fireworkNbt.putByte(FireworkRocketItem.FLIGHT_KEY, (byte)range);
        nbt.put(FireworkRocketItem.FIREWORKS_KEY, fireworkNbt);
        fireworkStack.setNbt(nbt);
    }

    private int getPrice() {
        float result =  BASE_PRICE[range - 1];
        float currentStar = shape.basePrice;
        currentStar += colorPickerWidget.colorsPicked.size() * COST_PER_COLOR;
        currentStar += fadePickerWidget.colorsPicked.size() * COST_PER_FADE;
        currentStar *= flicker ? FLICKER_COST_MULTIPLIER : 1;
        currentStar *= trail ? TRAIL_COST_MULTIPLIER : 1;
        result += currentStar;
        for(Explosion explosion : explosions) {
            float explosionResult = explosion.shape.basePrice;
            explosionResult += explosion.colors.size() * COST_PER_COLOR;
            explosionResult += explosion.fadeColors.size() * COST_PER_FADE;
            explosionResult *= explosion.flicker ? FLICKER_COST_MULTIPLIER : 1;
            explosionResult *= explosion.trail ? TRAIL_COST_MULTIPLIER : 1;
            result += explosionResult;
        }
        return (int) result;
    }

    class Explosion {
        Shape shape;
        ArrayList<DyeColor> colors;
        ArrayList<DyeColor> fadeColors;
        boolean flicker;
        boolean trail;

        Explosion(Shape shape, ArrayList<DyeColor> colors, ArrayList<DyeColor> fadeColors, boolean flicker, boolean trail) {
            this.shape = shape;
            this.colors = colors;
            this.fadeColors = fadeColors;
            this.flicker = flicker;
            this.trail = trail;

        }

        NbtCompound getNbt() {
            NbtCompound explosionNbt = new NbtCompound();
            explosionNbt.putByte("Type", (byte) FireworkRocketItem.Type.valueOf(this.shape.name()).getId());
            explosionNbt.putIntArray(FireworkRocketItem.COLORS_KEY, ColorPickerWidget.getColors(this.colors));
            explosionNbt.putIntArray(FireworkRocketItem.FADE_COLORS_KEY,  ColorPickerWidget.getColors(this.fadeColors));
            explosionNbt.putBoolean(FireworkRocketItem.FLICKER_KEY, this.flicker);
            explosionNbt.putBoolean(FireworkRocketItem.TRAIL_KEY, this.trail);
            return explosionNbt;
        }
    }

    class ToggleButton extends MemberButton{
        public ToggleButton(int x, int y, int width, int height, Text message, PressAction onPress) {
            super(x, y, width, height, message, onPress);
            setTexture(SMALL_BUTTON_TEXTURE);
        }
    }

    class CartWidget extends MemberScrollableWidget {
        static class Entry {
            ItemStack stack;
            int price;
            Entry (ItemStack stack, int price) {
                this.stack = stack;
                this.price = price;
            }
        }
        private Entry hoveredStack;
        ArrayList<Entry> entries = new ArrayList<>();
        private static final int ENTRY_HEIGHT = 16;
        public CartWidget(int x, int y, int width, int height) {
            super(x, y, width, height);
        }

        public ArrayList<ItemStack> getItems() {
            ArrayList<ItemStack> items = new ArrayList<>();
            for(Entry entry : entries) {
                items.add(entry.stack);
            }

            return items;
        }

        private void addItem(ItemStack itemStack, int price) {
            if (!entries.isEmpty()) {
                for(CartWidget.Entry entry : cartWidget.entries) {
                    if (itemStack.getOrCreateNbt().toString().equals(entry.stack.getOrCreateNbt().toString())) {
                        entry.stack.setCount(entry.stack.getCount() + getActionCount());
                        return;
                    }
                }
            }
            entries.add(new CartWidget.Entry(itemStack, price));
        }

        private void increaseCount(int index, int amount) {
            ItemStack itemStackAtIndex = entries.get(index).stack;
            itemStackAtIndex.setCount(itemStackAtIndex.getCount() + amount);
            if (itemStackAtIndex.getCount() <= 0) {
                entries.remove(index);
            }
        }

        private void decreaseCount(int index, int amount) {
            increaseCount(index, -amount);
        }

        private int getActionCount() {
            int count = KeybindingRegistry.MULTIPLY_CART_ACTION_1.isPressed() ? 4 : 1;
            count *= KeybindingRegistry.MULTIPLY_CART_ACTION_2.isPressed() ? 4 : 1;
            return count;
        }

        @Override
        protected int getContentsHeight() {
            return entries.size() * ENTRY_HEIGHT;
        }

        @Override
        public boolean isHovered() {
            return hovered;
        }

        @Override
        protected double getDeltaYPerScroll() {
            return ENTRY_HEIGHT;
        }

        private int getStacksInCart() {
            int stacksInCart = 0;
            for(Entry entry : entries) {
                stacksInCart += (int) Math.ceil(entry.stack.getCount() / 64f);
            }
            return stacksInCart;
        }

        public String getCartTotal() {
            return getCartTotalAmount() + "$";
        }

        public int getCartTotalAmount() {
            int total = 0;
            for(Entry entry : entries) {
                total += (entry.price * entry.stack.getCount());
            }
            return total;
        }

        @Override
        protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            int index = 0;
            for (Entry entry : entries) {
                CART_ITEM_TEXTURE.render(matrices, x, y + index * ENTRY_HEIGHT);
                ItemStack stackToRender = entry.stack;
                itemRenderer.renderInGui(
                        stackToRender,
                        x + ((CART_ITEM_TEXTURE.renderWidth - 16) / 2),
                        (int) (y + index * 16 - getScrollY()));
                itemRenderer.renderGuiItemOverlay(textRenderer,
                        stackToRender,
                        x + ((CART_ITEM_TEXTURE.renderWidth - 16) / 2),
                        (int) (y + index * 16  - getScrollY()));
                index++;
                //next add to components
            }
        }
        @Override
        public boolean isFocused() {
            return super.isFocused() || isHovered();
        }
        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (this.visible) {
                parent.drawContainerBox(matrices, this.x, this.y, this.x + this.width, this.y + this.height, true);
                textRenderer.drawWithShadow(matrices,"Cart: (" + getStacksInCart() + ")", this.x, this.y - TILE_SIZE * 2 - 2 , BEIGE_TEXT_COLOR);
                enableScissor(this.x, this.y, this.x + this.width, this.y + this.height);
                matrices.push();
                matrices.translate(0, -this.getScrollY(), 0);
                this.renderContents(matrices, mouseX, mouseY, delta);
                matrices.pop();
                disableScissor();
                this.renderOverlay(matrices);
            }
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (hovered) {
                hoveredStack = offerAtMouse(mouseX, mouseY);
            }
            super.render(matrices, mouseX, mouseY, delta);
        }

        private Entry offerAtMouse(double mouseX, double mouseY) {
            if (!hovered) {
                return null;
            }
            int predictedIndex = (int) ((mouseY + getScrollY() - y) / 16);
            if (entries.size() > predictedIndex && predictedIndex >= 0) {
                int clickSize = 16;
                boolean isIcon = mouseX >= x + clickSize  && mouseX <= x + clickSize * 2;
                if (isIcon) {
                    return entries.get(predictedIndex);
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
            int predictedIndex = (int) ((mouseY + getScrollY() - y) / 16);
            if (entries.size() > predictedIndex) {
                int clickSize = 16;
                boolean isLeftSide = mouseX >= x && mouseX <= x + clickSize;
                boolean isRightSide = mouseX >= x + CART_ITEM_TEXTURE.renderWidth - clickSize && mouseX <= x + CART_ITEM_TEXTURE.renderWidth;
                if (isLeftSide) {
                    decreaseCount(predictedIndex, getActionCount());
                }
                if (isRightSide) {
                    increaseCount(predictedIndex, getActionCount());
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

    }

    class ColorPickerWidget extends ClickableWidget {

        public static final int COLOR_SIZE = TILE_SIZE * 3;
        ArrayList<DyeColor> colorsPicked = new ArrayList<>();


        public ColorPickerWidget(int x, int y, int width, int height, Text message) {
            super(x, y, width, height, message);
        }

        public static int[] getColors(ArrayList<DyeColor> colors) {
            int [] result = new int[colors.size()];
            int i = 0;
            for(DyeColor color : colors) {
                result[i] = color.getFireworkColor();
                i++;
            }
            return result;
        }

        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.active && this.visible) {
                if (this.isValidClickButton(button)) {
                    boolean bl = this.clicked(mouseX, mouseY);
                    if (bl) {
                        int predictedX = (int) ((mouseX - x) / (COLOR_SIZE + 2));
                        int predictedY = (int) ((mouseY - y - TEXT_LINE_HEIGHT) / (COLOR_SIZE + 2));
                        int predictedIndex = predictedX + predictedY * 4;
                        if (predictedIndex >= 0 && predictedIndex < DyeColor.values().length) {
                            DyeColor pickedColor = DyeColor.byId(predictedIndex);
                            if (colorsPicked.contains(pickedColor)) {
                                colorsPicked.remove(pickedColor);
                                return true;
                            }
                            if (colorsPicked.size() >= 8) {
                                colorsPicked.remove(0);
                            }
                            colorsPicked.add(pickedColor);
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        @Override
        public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            Iterator<DyeColor> colorIterator = Arrays.stream(DyeColor.values()).iterator();
            drawTextWithShadow(matrices, textRenderer, getMessage(),x,y, BEIGE_TEXT_COLOR);
            int colorY = y + TEXT_LINE_HEIGHT;
            parent.drawContainerBox(matrices, x - 1, colorY - 1, x + 4 * (COLOR_SIZE + 2) - 1, colorY + 4 * (COLOR_SIZE + 2) - 1, true);
            if (!active) {
                return;
            }
            for(int i = 0; i < 4; i++) {
                int colorX = x;
                for(int j = 0; j < 4; j++) {
                    DyeColor c = colorIterator.next();
                    int color = c.getFireworkColor() | 0xFF000000;
                    boolean isSelected = colorsPicked.contains(c);
                    if (isSelected) {
                        fill(matrices, colorX, colorY, colorX + COLOR_SIZE, colorY + COLOR_SIZE, color);
                    } else {
                        fill(matrices, colorX + 2, colorY + 2, colorX + COLOR_SIZE - 2, colorY + COLOR_SIZE - 2, color);

                    }
                    colorX += COLOR_SIZE + 2;
                }
                colorY += COLOR_SIZE + 2;
            }
            //super.render(matrices, mouseX, mouseY, delta);
        }


        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {

        }
    }
}

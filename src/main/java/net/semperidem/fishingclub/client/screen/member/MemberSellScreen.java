package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.fish.FishUtil;
import net.semperidem.fishingclub.network.ClientPacketSender;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.function.Function;

import static net.semperidem.fishingclub.client.screen.member.MemberBuyScreen.OFFER_TEXTURE;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.BEIGE_TEXT_COLOR;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TILE_SIZE;

public class MemberSellScreen extends MemberSubScreen{
    private static final int SLOT_SIZE = 16;

    private static final Text SELL_BUTTON_TEXT = Text.literal("Sell");
    private static final Text SELL_ALL_BUTTON_TEXT = Text.literal("All");

    private static final Text TOTAL_TEXT = Text.literal("Total:");
    private static final Text SELECTED_TEXT = Text.literal("Selected:");
    private static final Text NO_FISH_TEXT = Text.literal("No fish to sell :(");


    public static final Texture SELECTION_TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/fish_selection.png"),
            16,
            16
    );


    private static final int BUTTON_WIDTH = TILE_SIZE * 14;
    private static final int BUTTON_HEIGHT = TILE_SIZE * 5;
    private static final int GRID_WIDGET_WIDTH = SLOT_SIZE * 9;
    private static final int GRID_WIDGET_HEIGHT = SLOT_SIZE * 6;
    private static final int TEXT_LINE_HEIGHT = 12;

    int baseX, baseY;
    int sellButtonX, sellButtonY;
    int sellAllButtonX, sellAllButtonY;
    int gridWidgetX, gridWidgetY;
    int noFishTextX, noFishTextY;

    int buttonBoxX0,buttonBoxX1, buttonBoxY0 ,buttonBoxY1;
    int totalTextX, totalTextY;
    int selectedTextY;



    ButtonWidget sellButton;
    ButtonWidget sellAllButton;
    EntriesGridWidget fishGridWidget;

    public MemberSellScreen(MemberScreen parent, Text title) {
        super(parent, title);
    }

    @Override
    public void init() {
        super.init();
        setupElementPosition();

        initGridWidget();
        initSellButton();
        initSellAllButton();
    }

    private void setupElementPosition() {
        baseX = parent.x + TILE_SIZE * 10;
        baseY = parent.y + TILE_SIZE * 5;

        gridWidgetX = baseX;
        gridWidgetY = baseY;

        noFishTextX = (int) (gridWidgetX + GRID_WIDGET_WIDTH * 0.5f);
        noFishTextY = (int) (gridWidgetY + GRID_WIDGET_HEIGHT * 0.5f - TILE_SIZE);

        sellButtonX = baseX + GRID_WIDGET_WIDTH + TILE_SIZE * 7;
        sellButtonY = baseY + GRID_WIDGET_HEIGHT - TILE_SIZE * 6;

        sellAllButtonX = sellButtonX + BUTTON_WIDTH;
        sellAllButtonY = sellButtonY;

        buttonBoxX0 = sellButtonX;
        buttonBoxX1 = sellAllButtonX + BUTTON_WIDTH;
        buttonBoxY0 = sellButtonY - 2 - 2 * TEXT_LINE_HEIGHT;
        buttonBoxY1 = sellAllButtonY + BUTTON_HEIGHT;

        totalTextX = buttonBoxX0 + 3;
        totalTextY = buttonBoxY0 + TILE_SIZE;
        selectedTextY = totalTextY + TEXT_LINE_HEIGHT;

    }

    private void initGridWidget() {
        fishGridWidget = new EntriesGridWidget(gridWidgetX, gridWidgetY);
        components.add(fishGridWidget);

    }

    private void initSellButton() {
        sellButton = new MemberButton(sellButtonX, sellButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, SELL_BUTTON_TEXT, button -> sellFish(fish -> fish.isSelected));
        components.add(sellButton);
    }
    private void initSellAllButton() {
        sellAllButton = new MemberButton(sellAllButtonX, sellAllButtonY, BUTTON_WIDTH, BUTTON_HEIGHT, SELL_ALL_BUTTON_TEXT, button -> sellFish(o -> true));
        components.add(sellAllButton);
    }

    private void sellFish(Function<EntriesGridWidget.GridEntry, Boolean> condition) {
        ArrayList<ItemStack> fishToSell = new ArrayList<>();
        ArrayList<EntriesGridWidget.GridEntry> fishToRemove = new ArrayList<>();
        for(EntriesGridWidget.GridEntry fish : fishGridWidget.entries) {
            if (condition.apply(fish)) {
                continue;
            }
            fishToSell.add(fish.itemStack);
            fishToRemove.add(fish);
        }
        fishGridWidget.entries.removeAll(fishToRemove);
        fishGridWidget.recalculateTotals();
        ClientPacketSender.sendFishToSell(fishToSell);
    }



    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        parent.drawContainerBox(matrixStack, buttonBoxX0, buttonBoxY0, buttonBoxX1, buttonBoxY1, true);
        textRenderer.drawWithShadow(matrixStack, TOTAL_TEXT, totalTextX, totalTextY, BEIGE_TEXT_COLOR);
        textRenderer.drawWithShadow(matrixStack, SELECTED_TEXT, totalTextX, selectedTextY, BEIGE_TEXT_COLOR);

        String totalPrice = fishGridWidget.lastTotal + "$";
        String selectedPrice = fishGridWidget.lastTotalSelected + "$";
        int totalTextValueX = buttonBoxX1 - textRenderer.getWidth(totalPrice) -  3;
        int selectedTextValueX = buttonBoxX1 - textRenderer.getWidth(totalPrice) -  3;
        textRenderer.drawWithShadow(matrixStack, totalPrice, totalTextValueX, totalTextY, BEIGE_TEXT_COLOR);
        textRenderer.drawWithShadow(matrixStack, selectedPrice, selectedTextValueX, selectedTextY, BEIGE_TEXT_COLOR);

        super.render(matrixStack, mouseX, mouseY, delta);
    }

    private class EntriesGridWidget extends MemberScrollableWidget {
        private static final int SLOTS_IN_ROW = 9;
        final ArrayList<GridEntry> entries = new ArrayList<>();
        int lastTotalSelected = 0;
        int lastTotal = 0;

        public EntriesGridWidget(int x, int y) {
            super(x, y, SLOT_SIZE * 9, SLOT_SIZE * 6);
            parent.getScreenHandler().getFishes().forEach(f -> {
                if (FishUtil.isFish(f)) {
                    entries.add(new GridEntry(f));
                }
            });
            lastTotal = getTotalAvailable();
        }


        public void recalculateTotals() {
            this.lastTotalSelected = getTotalSelected();
            this.lastTotal = getTotalAvailable();
        }
        public int getTotalAvailable() {
            int total = 0;
            for(GridEntry entry : entries) {
                    total += FishUtil.getFishValue(entry.itemStack);
            }
            return total;
        }

        public int getTotalSelected() {
            int total = 0;
            for(GridEntry entry : entries) {
                if (entry.isSelected) {
                    total += FishUtil.getFishValue(entry.itemStack);
                }
            }
            return total;
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
            int predictedX = (int) ((mouseX - x) / SLOT_SIZE);
            int predictedY = (int) ((mouseY + getScrollY() - y) / SLOT_SIZE);
            int predictedIndex = predictedX + predictedY * SLOTS_IN_ROW;
            if (entries.size() > predictedIndex) {
                GridEntry fish = entries.get(predictedIndex);
                fish.isSelected = !fish.isSelected;
                lastTotalSelected = getTotalSelected();
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        protected int getContentsHeight() {
            return Math.max(1, getRows()) * TILE_SIZE;
        }

        private int getRows() {
            return (int) Math.ceil((double) entries.size() / SLOTS_IN_ROW);
        }

        @Override
        protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (entries.isEmpty()) {
                drawCenteredTextWithShadow(matrices, textRenderer, NO_FISH_TEXT, noFishTextX ,noFishTextY, BEIGE_TEXT_COLOR);
                return;
            }

            Iterator<GridEntry> fishIterator = entries.iterator();
            for(int i = 0; i < getRows(); i++) {
                for(int j = 0; j <SLOTS_IN_ROW; j++) {
                    if (!fishIterator.hasNext()) {
                        return;
                    }
                    fishIterator.next().render(matrices, x + j * SLOT_SIZE, y + (int) (i * SLOT_SIZE - getScrollY()));
                }
            }
        }

        @Override
        public boolean isSelected() {
            return hovered;
        }

        @Override
        protected double getDeltaYPerScroll() {
            return SLOT_SIZE;
        }

        @Override
        public boolean isFocused() {
            return super.isFocused() || isSelected();
        }

        @Override
        public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (this.visible) {
                parent.drawContainerBox(matrices, this.x, this.y, this.x + this.width, this.y + this.height, false);
                enableScissor(this.x, this.y, this.x + this.width, this.y + this.height);
                matrices.push();
                matrices.translate(0, -this.getScrollY(), 0);
                this.renderContents(matrices, mouseX, mouseY, delta);
                matrices.pop();
                disableScissor();
                this.renderOverlay(matrices);
            }
        }


        private class GridEntry {
            ItemStack itemStack;
            boolean isSelected;

            GridEntry(ItemStack itemStack){
                this.itemStack = itemStack;
                this.isSelected = false;
            }

            public void render(MatrixStack matrixStack, int x, int y){
                OFFER_TEXTURE.render(matrixStack, x, y);
                itemRenderer.renderGuiItemIcon(itemStack, x, y);
                if (isSelected) {
                    SELECTION_TEXTURE.render(matrixStack, x, y);
                }
            }
        }
    }
}

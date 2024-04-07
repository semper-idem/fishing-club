package net.semperidem.fishingclub.client.screen.member;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
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

    int baseX, baseY;

    private static final int BUTTON_WIDTH = TILE_SIZE * 15;
    private static final int BUTTON_HEIGHT = TILE_SIZE * 5;
    private static final int GRID_WIDGET_WIDTH = SLOT_SIZE * 9;
    private static final int GRID_WIDGET_HEIGHT = SLOT_SIZE * 6;
    private static final int TEXT_LINE_HEIGHT = 12;
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

        gridWidgetX = baseX + 5 * TILE_SIZE;
        gridWidgetY = baseY;

        noFishTextX = (int) (gridWidgetX + GRID_WIDGET_WIDTH * 0.5f);
        noFishTextY = (int) (gridWidgetY + GRID_WIDGET_HEIGHT * 0.5f - TILE_SIZE);

        sellButtonX = baseX + GRID_WIDGET_WIDTH + 6 * TILE_SIZE;
        sellButtonY = baseY + GRID_WIDGET_HEIGHT - TILE_SIZE * 6;

        sellAllButtonX = sellButtonX + TILE_SIZE * 15;
        sellAllButtonY = sellButtonY;

        buttonBoxX0 = sellButton.x;
        buttonBoxX1 = sellAllButton.x + BUTTON_WIDTH;
        buttonBoxY0 = sellButton.y - TILE_SIZE - 2 * TEXT_LINE_HEIGHT;
        buttonBoxY1 = sellAllButton.y + BUTTON_HEIGHT;

        totalTextX = buttonBoxX0 + 2;
        totalTextY = buttonBoxY0 + 3;
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
        parent.drawContainerBox(matrixStack, buttonBoxX0 + 1, buttonBoxY0 + 1, buttonBoxX1 - 1, buttonBoxY1 - 1);
        parent.getTextRenderer().drawWithShadow(matrixStack, TOTAL_TEXT, totalTextX, totalTextY, BEIGE_TEXT_COLOR);
        parent.getTextRenderer().drawWithShadow(matrixStack, SELECTED_TEXT, totalTextX, selectedTextY, BEIGE_TEXT_COLOR);

        String totalPrice = fishGridWidget.lastTotal + "$";
        String selectedPrice = fishGridWidget.lastTotalSelected + "$";
        int totalTextValueX = buttonBoxX1 - parent.getTextRenderer().getWidth(totalPrice) -  2;
        int selectedTextValueX = buttonBoxX1 - parent.getTextRenderer().getWidth(totalPrice) -  2;
        parent.getTextRenderer().drawWithShadow(matrixStack, totalPrice, totalTextValueX, totalTextY, BEIGE_TEXT_COLOR);
        parent.getTextRenderer().drawWithShadow(matrixStack, selectedPrice, selectedTextValueX, selectedTextY, BEIGE_TEXT_COLOR);

        super.render(matrixStack, mouseX, mouseY, delta);
    }

    private class EntriesGridWidget extends ScrollableWidget {
        private static final int SLOTS_IN_ROW = 9;
        final ArrayList<GridEntry> entries = new ArrayList<>();
        int lastTotalSelected = 0;
        int lastTotal = 0;


        public EntriesGridWidget(int x, int y) {
            super(x, y, SLOT_SIZE * 9, SLOT_SIZE * 6, Text.empty());
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

        @Override
        protected boolean overflows() {
            return false;
        }

        private int getRows() {
            return (int) Math.ceil((double) entries.size() / SLOTS_IN_ROW);
        }

        @Override
        protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (entries.isEmpty()) {
                drawCenteredText(matrices, parent.getTextRenderer(), NO_FISH_TEXT, noFishTextX ,noFishTextY, BEIGE_TEXT_COLOR);
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
        public boolean isHovered() {
            return hovered;
        }

        @Override
        protected double getDeltaYPerScroll() {
            return SLOT_SIZE;
        }

        @Override
        public void appendNarrations(NarrationMessageBuilder builder) {}
        private int getContentsHeightWithPadding() {
            return this.getContentsHeight();
        }
        private int getScrollbarThumbHeight() {
            return MathHelper.clamp((int)((float)(this.height * this.height) / (float)this.getContentsHeightWithPadding()), 32, this.height);
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
                parent.drawContainerBox(matrices, this.x, this.y, this.x + this.width, this.y + this.height + 1);
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
        protected int getMaxScrollY() {
            return Math.max(0, getContentsHeight() - this.height);
        }


        private static class GridEntry {
            ItemStack itemStack;
            boolean isSelected;

            GridEntry(ItemStack itemStack){
                this.itemStack = itemStack;
                this.isSelected = false;
            }

            public void render(MatrixStack matrixStack, int x, int y){
                OFFER_TEXTURE.render(matrixStack, x, y);
                MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(itemStack, x, y);
                if (isSelected) {
                    SELECTION_TEXTURE.render(matrixStack, x, y);
                }
            }
        }
    }
}

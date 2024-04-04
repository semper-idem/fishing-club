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

import static net.minecraft.client.gui.DrawableHelper.fill;
import static net.semperidem.fishingclub.client.screen.member.MemberBuyScreen.OFFER_TEXTURE;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.BEIGE_TEXT_COLOR;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TILE_SIZE;

public class MemberSellScreen extends MemberSubScreen{
    private static final int SLOT_SIZE = 16;
    int baseX, baseY;


    public static final Texture SELECTION_TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/fish_selection.png"),
            16,
            16
    );

    ButtonWidget sellButton;
    ButtonWidget sellAllButton;
    FishGridWidget fishGridWidget;
    public MemberSellScreen(MemberScreen parent, Text title) {
        super(parent, title);
    }

    @Override
    public void init() {
        super.init();

        baseX = parent.x + TILE_SIZE * 10;
        baseY = parent.y + TILE_SIZE * 5;

        fishGridWidget = new FishGridWidget(baseX + 5 * TILE_SIZE, baseY, SLOT_SIZE * 9, SLOT_SIZE * 6, parent.getScreenHandler().getFishes());
        int sellButtonX = baseX + fishGridWidget.getWidth() + 6 * TILE_SIZE;
        int sellButtonY = baseY + fishGridWidget.getHeight() - TILE_SIZE * 6;
        int sellAllButtonX = sellButtonX + TILE_SIZE * 15;
        int sellAllButtonY = sellButtonY;
        components.add(sellButton = new MemberButton(sellButtonX, sellButtonY, TILE_SIZE * 15, TILE_SIZE * 5, Text.literal("Sell"), button -> {
            ArrayList<ItemStack> fishToSell = new ArrayList<>();
            ArrayList<FishGridWidget.Fish> fishToRemove = new ArrayList<>();
            for(FishGridWidget.Fish fish : fishGridWidget.fishes) {
                if (fish.isSelected) {
                    fishToSell.add(fish.fishStack);
                    fishToRemove.add(fish);
                }
            }
            fishGridWidget.fishes.removeAll(fishToRemove);
            fishGridWidget.recalculateTotals();
            ClientPacketSender.sendFishToSell(fishToSell);
        }));
        components.add(sellAllButton = new MemberButton(sellAllButtonX, sellAllButtonY, TILE_SIZE * 15, TILE_SIZE * 5, Text.literal("All"), button -> {
            ArrayList<ItemStack> fishToSell = new ArrayList<>();
            ArrayList<FishGridWidget.Fish> fishToRemove = new ArrayList<>();
            for(FishGridWidget.Fish fish : fishGridWidget.fishes) {
                fishToSell.add(fish.fishStack);
                fishToRemove.add(fish);
            }
            fishGridWidget.fishes.removeAll(fishToRemove);
            fishGridWidget.recalculateTotals();
            ClientPacketSender.sendFishToSell(fishToSell);
        }));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta) {
        int boxX = sellButton.x;
        int boxX1 = sellAllButton.x + sellAllButton.getWidth();
        int boxY = sellButton.y - MinecraftClient.getInstance().textRenderer.fontHeight - TILE_SIZE - 13;
        int boxY1 = sellAllButton.y + sellAllButton.getHeight();

        int color1 = 0xff272946;
        int color2 = 0xff061319;
        fill(matrixStack, boxX, boxY, boxX1, boxY1, color2);
        fill(matrixStack, boxX + 1, boxY + 1, boxX1 - 1, boxY1 - 1, color1);
        // fill(matrixStack, boxX + 1, boxY1 - client.textRenderer.fontHeight - 4, boxX1 - 1, boxY1 - client.textRenderer.fontHeight - 5, color2);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, "Total:", boxX + 2, boxY + 3, MemberScreen.BEIGE_TEXT_COLOR);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, "Selected:", boxX + 2, boxY + 3 + 12, MemberScreen.BEIGE_TEXT_COLOR);
        String totalPrice = fishGridWidget.lastTotal + "$";
        String selectedPrice = fishGridWidget.lastTotalSelected + "$";
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, totalPrice, boxX1 - MinecraftClient.getInstance().textRenderer.getWidth(totalPrice) -  2, boxY + 3, MemberScreen.BEIGE_TEXT_COLOR);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrixStack, selectedPrice, boxX1 - MinecraftClient.getInstance().textRenderer.getWidth(selectedPrice) -  2, boxY + 3 + 12, MemberScreen.BEIGE_TEXT_COLOR);
        super.render(matrixStack, mouseX, mouseY, delta);
    }

    private class FishGridWidget extends ScrollableWidget {
        private static final int SLOTS_IN_ROW = 9;
        ArrayList<Fish> fishes = new ArrayList<>();
        int lastTotalSelected = 0;
        int lastTotal = 0;


        public void recalculateTotals() {
            this.lastTotalSelected = getTotalSelected();
            this.lastTotal = getTotalAvailable();
        }
        public int getTotalAvailable() {
            int total = 0;
            for(Fish fish : fishes) {
                    total += FishUtil.getFishValue(fish.fishStack);
            }
            return total;
        }

        public int getTotalSelected() {
            int total = 0;
            for(Fish fish : fishes) {
                if (fish.isSelected) {
                    total += FishUtil.getFishValue(fish.fishStack);
                }
            }
            return total;
        }

        private class Fish {
            ItemStack fishStack;
            boolean isSelected;

            Fish(ItemStack fish){
                this.fishStack = fish;
                this.isSelected = false;
            }

            public void render(MatrixStack matrixStack, int x, int y){
                OFFER_TEXTURE.render(matrixStack, x, y);
                MinecraftClient.getInstance().getItemRenderer().renderGuiItemIcon(fishStack, x, y);
                if (isSelected) {
                    SELECTION_TEXTURE.render(matrixStack, x, y);
                }
            }
        }

        public FishGridWidget(int x, int y, int width, int length, ArrayList<ItemStack> fishesInInventory) {
            super(x, y, width, length, Text.empty());
            fishesInInventory.forEach(f -> {
                if (FishUtil.isFish(f)) {
                    fishes.add(new Fish(f));
                }
            });
            components.add(this);
            lastTotal = getTotalAvailable();
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
            if (fishes.size() > predictedIndex) {
                Fish fish = fishes.get(predictedIndex);
                fish.isSelected = !fish.isSelected;
                lastTotalSelected = getTotalSelected();
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        protected int getContentsHeight() {
            return (int) (Math.max(1, getRows()) * TILE_SIZE);
        }

        @Override
        protected boolean overflows() {
            return false;
        }

        private int getRows() {
            return (int) Math.ceil((double) fishes.size() / SLOTS_IN_ROW);
        }

        @Override
        protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {
            if (fishes.size() == 0) {
                drawCenteredText(matrices, parent.getTextRenderer(), "No fish to sell :(", this.x + width / 2 ,this.y + height / 2 - 4,BEIGE_TEXT_COLOR);
                return;
            }

            Iterator<Fish> fishIterator = fishes.iterator();
            for(int i = 0; i < getRows(); i++) {
                for(int j = 0; j <SLOTS_IN_ROW; j++) {


                    if (!fishIterator.hasNext()) {
                        return;
                    }
                    Fish fish = fishIterator.next();
                    fish.render(matrices, x + j * SLOT_SIZE, y + (int) (i * SLOT_SIZE - getScrollY()));
                }
            }
            for(Fish fish : fishes) {
                fish.render(matrices, x, y);
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

    }
}

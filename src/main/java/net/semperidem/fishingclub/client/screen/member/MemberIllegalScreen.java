package net.semperidem.fishingclub.client.screen.member;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.fisher.shop.OrderItem;
import net.semperidem.fishingclub.item.IllegalGoodsItem;
import net.semperidem.fishingclub.network.payload.CheckoutPayload;
import net.semperidem.fishingclub.registry.FCEnchantments;
import net.semperidem.fishingclub.util.MathUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Set;

import static net.minecraft.text.Text.literal;
import static net.semperidem.fishingclub.client.screen.member.MemberBuyScreen.OFFER_TEXTURE;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.*;

public class MemberIllegalScreen extends MemberSubScreen {
    static final Texture PREVIEW_BACKGROUND = new Texture(
            FishingClub.getIdentifier("textures/gui/box_preview_entry.png"),
            188,
            24
    );
    static final Texture SELECTED_PREVIEW_BACKGROUND = new Texture(
            FishingClub.getIdentifier("textures/gui/box_preview_entry_selected.png"),
            188,
            24
    );
    private final double moonPhaseDiscount;

    public MemberIllegalScreen(MemberScreen parent, Text title) {
        super(parent, title);
        moonPhaseDiscount = parent.getScreenHandler().getMoonPhaseDiscount() * 0.0625;
    }
    BoxPreviewWidget.BoxPreviewEntry selectedEntry;
    BoxPreviewWidget previewWidget;
    MemberButton buyButton;

    public static final int[] BOX_PRICES = {1000, 2000, 5000, 10000, 50000};


    private static final int BUTTON_WIDTH = TILE_SIZE * 14;
    private static final int BUTTON_HEIGHT = TILE_SIZE * 5;
    private static final int TEXT_LINE_HEIGHT = 12;

    private int buyButtonX,  buyButtonY;
    int buttonBoxX0, buttonBoxX1, buttonBoxY0, buttonBoxY1;
    int priceTextX, priceTextY;
    int helpTextX, helpTextY;
    int previewWidgetX, previewWidgetY, previewWidgetWidth,previewWidgetHeight;

    String helpText = """
            Higher tier doesn't
            guarantee better
            rewards, just
            improves your odds
            """;
    private static final Text PRICE_TEXT = literal("Price:");


    @Override
    public int unlockLevel() {
        return 15;
    }
    @Override
    public void init() {
        super.init();

        previewWidgetX = parent.x + TILE_SIZE  * 10;
        previewWidgetY = parent.y + TILE_SIZE * 5;
        previewWidgetWidth = TILE_SIZE * 47;
        previewWidgetHeight = TILE_SIZE * 24;

        previewWidget = new BoxPreviewWidget(parent.x + TILE_SIZE  * 10, parent.y + TILE_SIZE * 5, TILE_SIZE * 47, TILE_SIZE * 24);
        components.add(previewWidget);


        buyButtonX = parent.x + TEXTURE.renderWidth - 42 * TILE_SIZE;
        buyButtonY = parent.y + TEXTURE.renderHeight - BUTTON_HEIGHT - TILE_SIZE * 2;

        buyButton = new MemberButton(buyButtonX,buyButtonY,BUTTON_WIDTH,BUTTON_HEIGHT, literal("Buy"), onBuy());
        components.add(buyButton);


        buttonBoxX0 = buyButtonX - 15;
        buttonBoxX1 = buyButtonX + BUTTON_WIDTH + 15;
        buttonBoxY0 = buyButtonY - TEXT_LINE_HEIGHT - 2;
        buttonBoxY1 = buyButtonY + BUTTON_HEIGHT;

        priceTextX = buttonBoxX0 + 3;
        priceTextY = buttonBoxY0 + 2;

        helpTextX = previewWidgetX + previewWidgetWidth + TILE_SIZE;
        helpTextY = previewWidgetY + TILE_SIZE;
    }

    private ButtonWidget.PressAction onBuy() {
        return button -> {
            OrderItem oi = new OrderItem(Optional.of(IllegalGoodsItem.getStackWithTier(selectedEntry.tier)),1, Optional.of(BOX_PRICES[selectedEntry.tier - 1]));
            ClientPlayNetworking.send(new CheckoutPayload(new ArrayList<>(Set.of(oi))));
        };
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        parent.drawContainerBox(context, buttonBoxX0, buttonBoxY0, buttonBoxX1, buttonBoxY1, true);
        int helpTextLineY = helpTextY;
        for(String string : helpText.split("\n")) {
            context.drawTextWithShadow(textRenderer, string, helpTextX, helpTextLineY, BEIGE_TEXT_COLOR);
            helpTextLineY += TEXT_LINE_HEIGHT;
        }
        context.drawTextWithShadow(textRenderer, PRICE_TEXT, priceTextX, priceTextY, BEIGE_TEXT_COLOR);
        if (selectedEntry != null) {
            String boxPrice = String.format( "%.0f$", BOX_PRICES[selectedEntry.tier - 1] * (1 - moonPhaseDiscount));
            context.drawTextWithShadow(textRenderer, boxPrice, buttonBoxX1 - textRenderer.getWidth(boxPrice) -  3, priceTextY, BEIGE_TEXT_COLOR);
        }
        super.render(context, mouseX, mouseY, delta);

    }

    public class BoxPreviewWidget extends MemberScrollableWidget {
        private static final int ENTRY_HEIGHT = 24;
        ArrayList<BoxPreviewEntry> entries = new ArrayList<>();

        public BoxPreviewWidget(int x, int y, int width, int height) {
            super(x, y, width, height);
            HashMap<Integer, ArrayList<Item>> possibleLootMap = IllegalGoodsItem.getPossibleLoot();
            for(Integer tier : possibleLootMap.keySet()) {
                entries.add(new BoxPreviewEntry(tier, possibleLootMap.get(tier)));
            }
        }
        @Override
        public boolean isFocused() {
            return super.isFocused() || isSelected();
        }
        @Override
        protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
            int entryY = getY();
            for(BoxPreviewEntry entry : entries) {
                entry.render(context, getX(), (int) (entryY - getScrollY()));
                entryY += ENTRY_HEIGHT;
            }
        }

        @Override
        protected double getDeltaYPerScroll() {
            return ENTRY_HEIGHT;
        }

        @Override
        protected int getContentsHeight() {
            return ENTRY_HEIGHT * entries.size();
        }

        @Override
        public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
            if (this.visible) {
                parent.drawContainerBox(context, getX(), getY(), getX() + this.width, getY() + this.height, true);
                context.enableScissor(getX(), getY(), getX() + this.width, getY() + this.height);
                context.getMatrices().push();
                //RENDER PRICE
                this.renderContents(context, mouseX, mouseY, delta);
                context.getMatrices().pop();
                context.disableScissor();
                this.renderOverlay(context);
            }
        }


        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (mouseX > getX() + 8 && mouseX < getX() + 24) {
                int predictedIndex = (int) ((mouseY + getScrollY() - getY()) / ENTRY_HEIGHT);
                if (predictedIndex >= 0 && predictedIndex < entries.size()) {
                    if (selectedEntry != null ) {
                        selectedEntry.selected = false;
                    }
                    selectedEntry = entries.get(predictedIndex);
                    selectedEntry.selected = true;
                }
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        class BoxPreviewEntry {
            int tier;
            boolean selected;
            ArrayList<ItemStack> possibleLoot = new ArrayList<>();

            BoxPreviewEntry(int tier, ArrayList<Item> possibleLoot) {
                this.tier = tier;
                for(Item item : possibleLoot) {
                    ItemStack enchantedStack = item.getDefaultStack();

                    MinecraftClient.getInstance().world
                            .getRegistryManager()
                            .get(RegistryKeys.ENCHANTMENT)
                            .getEntry(FCEnchantments.CURSE_OF_MORTALITY.getValue())
                            .ifPresent(curseOfMortality -> enchantedStack.addEnchantment(curseOfMortality, 1));
                    this.possibleLoot.add(enchantedStack);
                }
            }

            private void render(DrawContext context, int x, int y) {
                if (selected) {
                    SELECTED_PREVIEW_BACKGROUND.render(context, x, y);
                } else {
                    PREVIEW_BACKGROUND.render(context, x, y);
                }
                int itemX = x + TILE_SIZE * 8;
                int itemY = y + TILE_SIZE;
                context.drawItem(IllegalGoodsItem.getStackWithTier(tier), x + TILE_SIZE * 2, itemY);
                context.getMatrices().push();
                context.getMatrices().translate(0.0, 0.0, 200.0F);
                context.drawTextWithShadow(textRenderer, literal(MathUtil.integerToRoman(tier)).asOrderedText(), x + 16,y + 8,BEIGE_TEXT_COLOR);
                context.getMatrices().pop();
                for(ItemStack lootStack : possibleLoot) {
                    OFFER_TEXTURE.render(context, itemX ,itemY);


                    context.drawItem(lootStack, itemX, itemY);
                    context.drawItemInSlot(textRenderer, lootStack, itemX, itemY);

                    itemX += 16;
                }
            }
        }
    }
}

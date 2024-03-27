package net.semperidem.fishingclub.client.screen.member;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.TILE_SIZE;


public class MemberShopScreen extends MemberSubScreen {
    private int baseX, baseY;
    private int categoryButtonX, categoryButtonY;
    private int categoryButtonCount = 0;
    CategoryButton fisherCategory;
    CategoryButton minerCategory;
    CategoryButton lumberjackCategory;
    CategoryButton alchemistCategory;
    CategoryButton librarianCategory;
    CategoryButton adventurerCategory;
    Category currentCategory;
    public MemberShopScreen(MemberScreen parent, Text title) {
        super(parent, title);
    }

    public void init() {
        super.init();

        baseX = parent.x + TILE_SIZE * 10;
        baseY = parent.y + TILE_SIZE * 5;
        categoryButtonX = baseX;
        categoryButtonY = baseY;
        fisherCategory = new CategoryButton(new Category(Items.FISHING_ROD, 0));
        minerCategory = new CategoryButton(new Category(Items.IRON_PICKAXE, 10));
        lumberjackCategory = new CategoryButton(new Category(Items.IRON_AXE, 10));
        alchemistCategory = new CategoryButton(new Category(Items.BREWING_STAND, 20));
        librarianCategory = new CategoryButton(new Category(Items.ENCHANTED_BOOK, 30));
        adventurerCategory = new CategoryButton(new Category(Items.FILLED_MAP, 30));
    }

    class Category {
        Item itemIcon;
        int unlockLevel;
        Category(Item itemIcon, int unlockLevel) {
            this.itemIcon = itemIcon;
            this.unlockLevel = unlockLevel;
        }

        public ButtonWidget.PressAction onPress() {
            return button -> currentCategory = this;
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

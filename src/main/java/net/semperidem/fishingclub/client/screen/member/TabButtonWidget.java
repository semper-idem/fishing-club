package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;

public class TabButtonWidget extends ButtonWidget  {

    static final Texture BUTTON_TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/member_tab_button.png"),
            80,
            54
    );

    private final MemberSubScreen tabView;
    public TabButtonWidget(int x, int y, int width, int height, MemberSubScreen tabView) {
        super(x, y, width, height, tabView.getTitle(), button -> tabView.getParent().setCurrentView(tabView));
        this.tabView = tabView;
    }

    public void resize(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    private boolean isSelected() {
        return this.tabView.getParent().getCurrentView() == tabView;
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        BUTTON_TEXTURE.render(matrices, x, y, 0, 18 * (isSelected() ? 0 : hovered ? 2 : 1), 80, 18);
        int j = this.active ? MemberScreen.BEIGE_TEXT_COLOR : 10526880;
        drawCenteredTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);

        //super.renderButton(matrices, mouseX, mouseY, delta);
    }
}

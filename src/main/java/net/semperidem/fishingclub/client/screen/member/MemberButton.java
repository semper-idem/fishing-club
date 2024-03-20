package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;

public class MemberButton extends ButtonWidget {


    static final Texture BUTTON_TEXTURE = new Texture(
            FishingClub.getIdentifier("textures/gui/member_button.png"),
            80,
            60
    );


    public MemberButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress);
    }


    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        BUTTON_TEXTURE.render(matrices, x, y, 0, 20 * (active ? hovered ? 2 : 1 : 0), width, height);
        int j = this.active ? MemberScreen.BEIGE_TEXT_COLOR : 10526880;
        drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);

        //super.renderButton(matrices, mouseX, mouseY, delta);
    }
}

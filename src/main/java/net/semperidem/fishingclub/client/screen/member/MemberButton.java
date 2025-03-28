package net.semperidem.fishingclub.client.screen.member;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;

import java.util.function.Supplier;

public class MemberButton extends ButtonWidget {

    private Texture texture = BUTTON_TEXTURE;
    int buttonHeight = texture.renderHeight / 3;



    static final Texture BUTTON_TEXTURE = new Texture(
            FishingClub.identifier("textures/gui/member_button.png"),
            80,
            60
    );
    public static final Texture BUTTON_EXIT_TEXTURE = new Texture(
            FishingClub.identifier("textures/gui/member_button_exit.png"),
            16,
            48
    );


    public static final Texture SMALL_BUTTON_TEXTURE = new Texture(
            FishingClub.identifier("textures/gui/member_button_small.png"),
            64,
            48
    );



    public static final Texture SMALL_WIDE_BUTTON_TEXTURE = new Texture(
            FishingClub.identifier("textures/gui/member_button_small_wide.png"),
            128,
            48
    );


    public MemberButton(int x, int y, int width, int height, Text message, PressAction onPress) {
        super(x, y, width, height, message, onPress, Supplier::get);
    }

    public void setTexture(Texture texture){
        this.texture = texture;
        this.buttonHeight = texture.renderHeight / 3;
    }


    @Override
    public void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        texture.render(context, getX(), getY(), 0, this.buttonHeight * (active ? hovered ? 2 : 1 : 0), width, height);
        int j = this.active ? MemberScreen.BEIGE_TEXT_COLOR : 10526880;
        context.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, this.getMessage(), this.getX() + this.width / 2, this.getY() + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);

        //super.renderButton(matrices, mouseX, mouseY, delta);
    }
}

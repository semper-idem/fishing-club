package net.semperidem.fishingclub.client.screen.member;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class MemberScrollableWidget extends ScrollableWidget {
    private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("widget/scroller");
    public MemberScrollableWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty());
    }

//    @Override
//    protected int getContentsHeight() {
//        return 0;
//    }

    @Override
    protected boolean overflows() {
        return false;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 0;
    }

//    @Override
////    protected void renderContents(DrawContext context, int mouseX, int mouseY, float delta) {
////
////    }

    public int getContentsHeightWithPadding() {
        return 0;//this.getContentsHeight();
    }

    public int getScrollbarThumbHeight() {
        return MathHelper.clamp((int)((float)(this.height * this.height) / (float)this.getContentsHeightWithPadding()), 32, this.height);
    }

    @Override
    public int getMaxScrollY() {
        return 0;//Math.max(0, getContentsHeight() - this.height);
    }

//    @Override
//    protected void renderOverlay(DrawContext context) {
//        int i = this.getScrollbarThumbHeight();
//        int j = this.getX() + this.width;
//        if (this.getMaxScrollY() <= 0) {
//            return;
//        }
//        int k = Math.max(this.getY(), (int)this.getScrollY() * (this.height - i) / this.getMaxScrollY() + this.getY());
//        RenderSystem.enableBlend();
//        context.drawGuiTexture(SCROLLER_TEXTURE, j, k, 8, i);
//        RenderSystem.disableBlend();
//    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float deltaTicks) {

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }
}

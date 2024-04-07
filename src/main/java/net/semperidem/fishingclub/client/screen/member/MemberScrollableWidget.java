package net.semperidem.fishingclub.client.screen.member;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;

import static net.semperidem.fishingclub.client.screen.member.MemberScreen.PRIMARY_SCROLLBAR_COLOR;
import static net.semperidem.fishingclub.client.screen.member.MemberScreen.SECONDARY_SCROLLBAR_COLOR;

public class MemberScrollableWidget extends ScrollableWidget {
    public MemberScrollableWidget(int x, int y, int width, int height) {
        super(x, y, width, height, Text.empty());
    }

    @Override
    protected int getContentsHeight() {
        return 0;
    }

    @Override
    protected boolean overflows() {
        return false;
    }

    @Override
    protected double getDeltaYPerScroll() {
        return 0;
    }

    @Override
    protected void renderContents(MatrixStack matrices, int mouseX, int mouseY, float delta) {

    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {

    }

    private int getContentsHeightWithPadding() {
        return this.getContentsHeight();
    }

    private int getScrollbarThumbHeight() {
        return MathHelper.clamp((int)((float)(this.height * this.height) / (float)this.getContentsHeightWithPadding()), 32, this.height);
    }

    @Override
    protected int getMaxScrollY() {
        return Math.max(0, getContentsHeight() - this.height);
    }

    @Override
    protected void renderOverlay(MatrixStack matrices) {
        int i = this.getScrollbarThumbHeight();
        int x0 = this.x + this.width - 4;
        int x1 = this.x + this.width;
        int y0 = Math.max(this.y, this.getMaxScrollY() == 0 ? this.y : (int)this.getScrollY() * (this.height - i) / this.getMaxScrollY() + this.y);
        int y1 = y0 + i;
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(x0, y1, 0).color(SECONDARY_SCROLLBAR_COLOR).next();
        bufferBuilder.vertex(x1, y1, 0).color(SECONDARY_SCROLLBAR_COLOR).next();
        bufferBuilder.vertex(x1, y0, 0).color(SECONDARY_SCROLLBAR_COLOR).next();
        bufferBuilder.vertex(x0, y0, 0).color(SECONDARY_SCROLLBAR_COLOR).next();
        bufferBuilder.vertex(x0, (y1 - 1), 0).color(PRIMARY_SCROLLBAR_COLOR).next();
        bufferBuilder.vertex((x1 - 1), (y1 - 1), 0).color(PRIMARY_SCROLLBAR_COLOR).next();
        bufferBuilder.vertex((x1 - 1), y0, 0).color(PRIMARY_SCROLLBAR_COLOR).next();
        bufferBuilder.vertex(x0, y0, 0).color(PRIMARY_SCROLLBAR_COLOR).next();
        tessellator.draw();
    }

}

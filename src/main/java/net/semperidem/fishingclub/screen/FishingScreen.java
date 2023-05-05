package net.semperidem.fishingclub.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.fish.FishGameLogic;

public class FishingScreen extends Screen {
    int screenInnerHeight = 300;
    int screenInnerWidth = 200;

    FishGameLogic fishGameLogic;

    public FishingScreen(Text text) {
        super(text);
        this.fishGameLogic = new FishGameLogic();
    }


    @Override
    public void tick() {
        this.fishGameLogic.tick();
        if (this.fishGameLogic.isFinished()) {
            this.close();
            if (this.fishGameLogic.isWon()) {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Caught Lvl." + this.fishGameLogic.getLevel() + " fish! Nice"));
            } else {
                MinecraftClient.getInstance().player.sendMessage(Text.of("Fish escaped"));
            }
        }
    }
    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        int screenX = (this.width - screenInnerWidth) / 2;
        int screenY = (this.height - screenInnerHeight) / 2;

        DrawableHelper.fill(matrices, screenX,screenY,screenX + screenInnerWidth,screenY + screenInnerHeight,0xFFdecbe4);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Text.of(String.format("%.2f", fishGameLogic.getHealth())), 0,0,0xFFFFFF);
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Text.of("Level:" + fishGameLogic.getLevel()), 0,20,0xFFFFFF);
//        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Text.of("bobberPos:" + String.format("%.2f",bobberPos)), 0,40,0xFFFFFF);
//        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Text.of("fishPos:" + String.format("%.2f",fishPos)), 0,60,0xFFFFFF);
        renderFishWidget(matrices, screenX + 50, screenY + 150);
        renderProgressWidget(matrices, screenX + 100, screenY + 150);
        super.render(matrices, mouseX, mouseY, delta);
    }

    public void renderFishWidget(MatrixStack matrices, int x, int y) {
        //Bar
        DrawableHelper.fill(matrices, x,y,x + 10,y - 100,0xFF282e68);
        //Bobber
        float bobberWidth = fishGameLogic.getBobberWidth();
        float bobberPos = fishGameLogic.getBobberPos();
        float fishPos = fishGameLogic.getFishPos();
        DrawableHelper.fill(matrices, x + 1, (int) (y - bobberWidth*100 - (bobberPos * 100)),x + 9, (int) (y + bobberWidth*100 - (bobberPos * 100)),0xFFf49333);

        //Fish
        DrawableHelper.fill(matrices, x + 1, (int) (y - (fishPos * 100)),x + 9, (int) (y - 1 - (fishPos * 100)),0xFF468681);

    }

    public void renderProgressWidget(MatrixStack matrices, int x, int y) {
        DrawableHelper.fill(matrices, x,y,x + 10,y - 100,0xFF483e10);
        DrawableHelper.fill(matrices, x + 1,y,x + 9, (int) (y - (100 * fishGameLogic.getProgress())),0xFFeccf49);

    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}

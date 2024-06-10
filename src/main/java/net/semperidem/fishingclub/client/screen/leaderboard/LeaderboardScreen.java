package net.semperidem.fishingclub.client.screen.leaderboard;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.leaderboard.Leaderboard;
import net.semperidem.fishingclub.leaderboard.LeaderboardTracker;
import net.semperidem.fishingclub.screen.leaderboard.LeaderboardScreenHandler;

import java.awt.*;

public class LeaderboardScreen  extends HandledScreen<LeaderboardScreenHandler> implements ScreenHandlerProvider<LeaderboardScreenHandler> {
    private static final Texture BACKGROUND = new Texture(FishingClub.getIdentifier("textures/gui/leaderboard.png"), 400, 280);
    private static final int TITLE_COLOR = Color.WHITE.getRGB();
    private int mainBoardX, mainBoardY;

    public LeaderboardScreen(LeaderboardScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        x = (int) ((width - BACKGROUND.renderWidth) * 0.5f);
        y = (int) ((height - BACKGROUND.renderHeight) * 0.5f);
        titleX = (int) ((BACKGROUND.renderHeight + textRenderer.getWidth(title)) * 0.5f);
        mainBoardX = x;
        mainBoardY = y;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
//        Leaderboard lfc = handler.getLeaderboards().values().iterator().next();
//        int entryOffset = 0;
//        for(Leaderboard.Entry entry : lfc.standings) {
//            textRenderer.drawWithShadow(matrices, entry.holderName + ": " + entry.value, mainBoardX, mainBoardY + entryOffset, TITLE_COLOR);
//            entryOffset += 20;
//        }
    }

    @Override
    protected void drawForeground(MatrixStack matrices, int mouseX, int mouseY) {
        textRenderer.drawWithShadow(matrices, title, titleX, titleY,TITLE_COLOR);

    }

    @Override
    protected void drawBackground(MatrixStack matrices, float delta, int mouseX, int mouseY) {
        BACKGROUND.render(matrices, x, y);
    }
}

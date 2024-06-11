package net.semperidem.fishingclub.client.screen.leaderboard;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.client.screen.member.MemberButton;
import net.semperidem.fishingclub.leaderboard.Leaderboard;
import net.semperidem.fishingclub.leaderboard.LeaderboardTracker;
import net.semperidem.fishingclub.screen.leaderboard.LeaderboardScreenHandler;

import java.awt.*;
import java.math.BigDecimal;
import java.util.Iterator;

public class LeaderboardScreen  extends HandledScreen<LeaderboardScreenHandler> implements ScreenHandlerProvider<LeaderboardScreenHandler> {
    private static final Texture BACKGROUND = new Texture(FishingClub.getIdentifier("textures/gui/leaderboard.png"), 400, 280);
    private static final int TITLE_COLOR = Color.WHITE.getRGB();
    private int mainBoardX, mainBoardY;
    private int leftRecordX, middleRecordX, rightRecordX;
    Leaderboard<?> currentLeaderboard;
    MemberButton nextButton;
    MemberButton previousButton;
    private int leaderboardTitleX, leaderboardTitleY;
    private int nextButtonX, nextButtonY;
    private int previousButtonX, previousButtonY;
    private int boardBottomY;
    private static final int RECORD_PADDING = 60;
    private int firstColor = 0xffc30b;
    private int secondColor = 0xbbb9bd;
    private int thirdColor = 0x7d3f11;
    private int otherColor = 0x808080;
    private int dividerColor = 0x33777777;
    private int dividerColor2 = 0x33444444;
    private static final int MAX_RECORDS_SHOWS = 10;

    public LeaderboardScreen(LeaderboardScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        x = (int) ((width - BACKGROUND.renderWidth) * 0.5f);
        y = (int) ((height - BACKGROUND.renderHeight) * 0.5f);
        titleX = (int) ((BACKGROUND.renderWidth - textRenderer.getWidth(title)) * 0.5f);
        leaderboardTitleY = y + 24;
        mainBoardX = x + RECORD_PADDING;
        mainBoardY = leaderboardTitleY + 24;
        nextButtonX = x + BACKGROUND.renderWidth - 32;
        nextButtonY = y + 8;
        previousButtonX = x + 8;
        previousButtonY = nextButtonY;
        leftRecordX = mainBoardX;
        middleRecordX = leftRecordX + 100;
        rightRecordX = x + BACKGROUND.renderWidth - RECORD_PADDING;
        boardBottomY = y + BACKGROUND.renderHeight - 32;

        currentLeaderboard = handler.getCurrentLeaderboard();
        nextButton = new MemberButton(nextButtonX,nextButtonY,24,20,Text.literal(">>"), button -> currentLeaderboard = handler.getNextLeaderboard());
        previousButton = new MemberButton(previousButtonX,previousButtonY,24,20,Text.literal("<<"), button -> currentLeaderboard = handler.getPreviousLeaderboard());
        addDrawableChild(nextButton);
        addDrawableChild(previousButton);
    }

    public int getRecordColor(int recordNumber) {
        return switch (recordNumber) {
            case 0 -> firstColor;
            case 1 -> secondColor;
            case 2 -> thirdColor;
            default -> otherColor;
        };
    }
    public String getRecordFormatting(int recordNumber) {
        return switch (recordNumber) {
            case 0 -> "§l";
            case 1,2 -> "";
            default -> "";
        };
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        drawHorizontalLine(matrices, 0, 100, 150, 0xffeace);
        super.render(matrices, mouseX, mouseY, delta);
        if (currentLeaderboard == null) {
            return;
        }
        leaderboardTitleX = (int) (x + (BACKGROUND.renderWidth - textRenderer.getWidth(currentLeaderboard.label)) * 0.5f);
        textRenderer.drawWithShadow(matrices, currentLeaderboard.label, leaderboardTitleX, leaderboardTitleY, TITLE_COLOR);
        int entryOffset = 0;
        int index = 0;
        for (Iterator<Leaderboard.Entry> it = currentLeaderboard.getIterator(); it.hasNext(); ) {
            if (index == MAX_RECORDS_SHOWS) {
                return;
            }
            Leaderboard.Entry entry = it.next();
            String leftString = getRecordFormatting(index) + (index + 1) + ") " + entry.playerName;
            int color = getRecordColor(index);

            String middleString = getRecordFormatting(index) + (!entry.context.isEmpty() ? "["+ entry.context +"]" : "");
            String rightString = getRecordFormatting(index) + String.format(currentLeaderboard.name.startsWith("_") ? "%.0f" : "%.2f", entry.value) + currentLeaderboard.unit;
            textRenderer.drawWithShadow(matrices, leftString, leftRecordX, mainBoardY + entryOffset, color);
            textRenderer.drawWithShadow(matrices, middleString, middleRecordX, mainBoardY + entryOffset, color);
            textRenderer.drawWithShadow(matrices, rightString, rightRecordX - textRenderer.getWidth(rightString), mainBoardY + entryOffset, color);
            fill(matrices, leftRecordX - 5, mainBoardY + entryOffset + 11, rightRecordX + 5, mainBoardY + entryOffset + 13, dividerColor2);
            fill(matrices, leftRecordX - 5, mainBoardY + entryOffset + 11, rightRecordX + 5, mainBoardY + entryOffset + 12, dividerColor);
            entryOffset += 20;
            index++;
        }

        Leaderboard.Entry entry = currentLeaderboard.getCurrentRecord(MinecraftClient.getInstance().player.getUuid());
        int place = currentLeaderboard.getIndexOf(MinecraftClient.getInstance().player.getUuid());
        String leftString = getRecordFormatting(place) + (place + 1) + ") " + "Your record:";
        int color = getRecordColor(place);
        String middleString = "";
        String rightString = "-";
        if (entry != null) {
            middleString = getRecordFormatting(place) + (!entry.context.isEmpty() ? "["+ entry.context +"]" : "");
            rightString = getRecordFormatting(place) + String.format(currentLeaderboard.name.startsWith("_") ? "%.0f" : "%.2f", entry.value) + currentLeaderboard.unit;
        }
        textRenderer.drawWithShadow(matrices, leftString, leftRecordX, boardBottomY, color);
        textRenderer.drawWithShadow(matrices, middleString, middleRecordX, boardBottomY, color);
        textRenderer.drawWithShadow(matrices, rightString, rightRecordX - textRenderer.getWidth(rightString), boardBottomY, color);

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

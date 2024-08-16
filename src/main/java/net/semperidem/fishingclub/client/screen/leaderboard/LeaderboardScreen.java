package net.semperidem.fishingclub.client.screen.leaderboard;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.client.screen.member.MemberButton;
import net.semperidem.fishingclub.leaderboard.Leaderboard;
import net.semperidem.fishingclub.network.payload.FishingCardPayload;

import java.awt.*;
import java.util.Iterator;

public class LeaderboardScreen  extends Screen {
    private static final Texture BACKGROUND = new Texture(FishingClub.getIdentifier("textures/gui/leaderboard.png"), 400, 280);
    private static final int TITLE_COLOR = Color.WHITE.getRGB();
    private int mainBoardX, mainBoardY;
    private int leftRecordX, middleRecordX, rightRecordX;
    Leaderboard<?> currentLeaderboard;
    MemberButton nextButton;
    MemberButton previousButton;
    MemberButton exitButton;
    private int exitButtonX, exitButtonY;
    private int leaderboardTitleX, leaderboardTitleY;
    private int nextButtonX, nextButtonY;
    private int previousButtonX, previousButtonY;
    private int boardBottomY;
    private int x,y;
    private static final int RECORD_PADDING = 60;
    private int firstColor = 0xffaa00;
    private int secondColor = 0xbbb9bd;
    private int thirdColor = 0x7d3f11;
    private int otherColor = 0x808080;
    private int dividerColor = 0x33777777;
    private int dividerColor2 = 0x33444444;
    private static final int MAX_RECORDS_SHOWS = 10;
    private int[] firstColorGradient = new int[80];
    static final int CYCLE = 1600;
    int timer = CYCLE;

    public LeaderboardScreen(Text title) {
        super(title);
        populateGradient();
    }

    private void populateGradient() {
        Color gradientColor = new Color(firstColor);
        for(int i = 39; i >= 0; i--) {
            firstColorGradient[i] = gradientColor.getRGB();

            int r = gradientColor.getRed();
            int g = gradientColor.getGreen();
            int b = gradientColor.getBlue();
            gradientColor = new Color(
                    r,
                    (int) Math.min(222, g * 1.0125f),
                    b
            );
        }
    }
    @Override
    protected void init() {
        x = (int) ((width - BACKGROUND.renderWidth) * 0.5f);
        y = (int) ((height - BACKGROUND.renderHeight) * 0.5f);
        //titleX = (int) ((BACKGROUND.renderWidth - textRenderer.getWidth(title)) * 0.5f);
        leaderboardTitleY = y + 24;
        mainBoardX = x + RECORD_PADDING;
        mainBoardY = leaderboardTitleY + 24;
        nextButtonX = x + BACKGROUND.renderWidth - 32;
        nextButtonY = y + 8;
        previousButtonX = x + 8;
        previousButtonY = nextButtonY;
        leftRecordX = mainBoardX;
        rightRecordX = x + BACKGROUND.renderWidth - RECORD_PADDING;
        middleRecordX = rightRecordX - 60;
        boardBottomY = y + BACKGROUND.renderHeight - 32;
        exitButtonX = x + 14;
        exitButtonY =  y + BACKGROUND.renderHeight - 28;

        //currentLeaderboard = handler.getCurrentLeaderboard();
        exitButton = new MemberButton(exitButtonX,exitButtonY,16,16, Text.empty(), button -> {
            ClientPlayNetworking.send(new FishingCardPayload());
        });
        exitButton.setTexture(MemberButton.BUTTON_EXIT_TEXTURE);
        //nextButton = new MemberButton(nextButtonX,nextButtonY,24,20,Text.literal(">>"), button -> currentLeaderboard = handler.getNextLeaderboard());
        //previousButton = new MemberButton(previousButtonX,previousButtonY,24,20,Text.literal("<<"), button -> currentLeaderboard = handler.getPreviousLeaderboard());
       // addDrawableChild(nextButton);
        addDrawableChild(exitButton);
        //addDrawableChild(previousButton);
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
            case 0 -> "";
            default -> "";
        };
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (timer < -1600) {
            timer = CYCLE;
        }
        timer--;
        int gradientNumber = (int) Math.sqrt(Math.abs(timer));
        int st = firstColor;
        if (gradientNumber < 40) {
            st = firstColorGradient[gradientNumber];
        }

        context.drawTextWithShadow(textRenderer, String.valueOf(gradientNumber), 5, 5, TITLE_COLOR);
        context.drawHorizontalLine(0, 100, 150, 0xffeace);
        super.render(context, mouseX, mouseY, delta);
        if (currentLeaderboard == null) {
            return;
        }
        leaderboardTitleX = (int) (x + (BACKGROUND.renderWidth - textRenderer.getWidth(currentLeaderboard.label)) * 0.5f);
        context.drawTextWithShadow(textRenderer, currentLeaderboard.label, leaderboardTitleX, leaderboardTitleY, TITLE_COLOR);
        int entryOffset = 0;
        int index = 0;
        for (Iterator<Leaderboard.Entry> it = currentLeaderboard.getIterator(); it.hasNext(); ) {
            if (index == MAX_RECORDS_SHOWS) {
                return;
            }
            Leaderboard.Entry entry = it.next();
            boolean isKing = true;//FishingClubClient.FISHING_KING_UUID.compareTo(entry.key) == 0;
            String leftString =getRecordFormatting(index) + (index + 1) + ") "  + entry.playerName+ (isKing ? " [♚]" : "");
            int color = index == 0 ? st : getRecordColor(index);

            String middleString = getRecordFormatting(index) + (!entry.context.isEmpty() ? "["+ entry.context +"]" : "");
            String rightString = getRecordFormatting(index) + String.format(currentLeaderboard.name.startsWith("_") ? "%.0f" : "%.2f", entry.value) + currentLeaderboard.unit;
            context.drawTextWithShadow(textRenderer, leftString, leftRecordX, mainBoardY + entryOffset, color);
            context.drawTextWithShadow(textRenderer, middleString, middleRecordX - textRenderer.getWidth(middleString), mainBoardY + entryOffset, color);
            context.drawTextWithShadow(textRenderer, rightString, rightRecordX - textRenderer.getWidth(rightString), mainBoardY + entryOffset, color);
            context.fill(leftRecordX - 5, mainBoardY + entryOffset + 11, rightRecordX + 5, mainBoardY + entryOffset + 13, dividerColor2);
            context.fill( leftRecordX - 5, mainBoardY + entryOffset + 11, rightRecordX + 5, mainBoardY + entryOffset + 12, dividerColor);
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
        context.drawTextWithShadow(textRenderer, leftString, leftRecordX, boardBottomY, color);
        context.drawTextWithShadow(textRenderer, middleString, middleRecordX - textRenderer.getWidth(middleString), boardBottomY, color);
        context.drawTextWithShadow(textRenderer, rightString, rightRecordX - textRenderer.getWidth(rightString), boardBottomY, color);

    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        BACKGROUND.render(context, x, y);
    }
}

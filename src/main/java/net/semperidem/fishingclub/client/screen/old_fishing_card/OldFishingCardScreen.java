package net.semperidem.fishingclub.client.screen.old_fishing_card;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.CacheAwareButtonWidget;
import net.semperidem.fishingclub.client.screen.Cacheable;
import net.semperidem.fishingclub.client.screen.Texture;
import net.semperidem.fishingclub.client.screen.leaderboard.LeaderboardScreen;
import net.semperidem.fishingclub.client.screen.member.MemberButton;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecret;
import net.semperidem.fishingclub.fisher.tradesecret.TradeSecrets;
import net.semperidem.fishingclub.network.payload.LearnTradeSecretPayload;
import net.semperidem.fishingclub.screen.card.OldFishingCardScreenHandler;

import java.util.ArrayList;

public class OldFishingCardScreen extends HandledScreen<OldFishingCardScreenHandler> implements ScreenHandlerProvider<OldFishingCardScreenHandler>, Cacheable {

    private static final int BACKGROUND_TEXTURE_WIDTH = 400;
    private static final int BACKGROUND_TEXTURE_HEIGHT = 280;

    private static final Texture BACKGROUND_GENERAL = new Texture(FishingClub.identifier("textures/gui/fishing_card_inv.png"), BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    private static final Texture BACKGROUND_PERK = new Texture(FishingClub.identifier("textures/gui/fishing_card.png"), BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    private static final int TEXT_COLOR = 0xffeace;

    private static final int TILE_SIZE = 4;
    private static final int TEXT_LINE_HEIGHT = 12;

    private static final int BANNER_WIDTH = TILE_SIZE * 26;
    private static final int TAB_WIDTH = (int) ((BACKGROUND_TEXTURE_WIDTH - BANNER_WIDTH - TILE_SIZE) * 0.25f);
    private static final int TAB_HEIGHT = 16;

    private static final String sUnlock = "Unlock";
    private static final String sPerkPoint = "Perk points:";
    private static final String sLevel = "Lvl.";
    private static final String sCredit = "$";
    private static final String sExp = "Exp.";
    private static final String labelFormatter = "§6§l";

    private String name;
    private String level;
    private String exp;
    private String credit;
    private String perkPoints;

    private Texture background = BACKGROUND_GENERAL;

    private TradeSecret selectedPerk;
    private PerkButtonWidget selectedPerkButton;

    private MemberButton unlockButton;
    public InstantSellButtonWidget instantSellSlotButton;
    private MemberButton leaderboardButton;
    private final ArrayList<PerkButtonWidget> perkButtons = new ArrayList<>();

    private int buttonsX;
    private int buttonsY;
    private int detailsLeftX, detailsY, detailsRightX;
    private int nextButtonX;
    private int leaderBoardButtonX,leaderBoardButtonY;

    private int perksX;
    private int perksY;

    public OldFishingCardScreen(OldFishingCardScreenHandler oldFishingCardScreenHandler, PlayerInventory playerInventory, Text title) {
        super(oldFishingCardScreenHandler, playerInventory, Text.literal("Fishing Card"));
        updateCache();
    }

    public void updateCache(){
        name = MinecraftClient.getInstance().player.getName().getString();
        level = String.valueOf(handler.card.getLevel());
        exp = handler.card.getExp() + "/" + handler.card.nextLevelXP();
        credit = String.valueOf(handler.card.getCredit());
        perkPoints = String.valueOf(handler.card.getPerkPoints());
    }

    @Override
    protected void init() {
        x = (width - BACKGROUND_TEXTURE_WIDTH) / 2;
        y = (height - BACKGROUND_TEXTURE_HEIGHT) / 2;
        backgroundWidth = BACKGROUND_TEXTURE_WIDTH;
        backgroundHeight = BACKGROUND_TEXTURE_HEIGHT;

        buttonsX = x + BANNER_WIDTH;
        buttonsY = y + 2;

        perksX = x + BANNER_WIDTH + TILE_SIZE * 2;
        perksY = buttonsY + TAB_HEIGHT + TILE_SIZE * 2;
        titleX = x + TILE_SIZE  + 2;
        titleY = y + TILE_SIZE + 2;
        detailsLeftX = titleX;
        detailsRightX = detailsLeftX + BANNER_WIDTH - TILE_SIZE * 3;
        detailsY = y + TILE_SIZE * 5 + 2 ;


        leaderBoardButtonX = x + TILE_SIZE;
        leaderBoardButtonY = y + BACKGROUND_TEXTURE_HEIGHT - TILE_SIZE * 6;
        leaderboardButton = new MemberButton(leaderBoardButtonX, leaderBoardButtonY, BANNER_WIDTH - TILE_SIZE * 2, TILE_SIZE * 5, Text.literal("Leaderboard"), button -> {
            MinecraftClient.getInstance().setScreen(new LeaderboardScreen(Text.empty()));
        });
        addDrawableChild(leaderboardButton);

        initButtons();
    }


    private void initButtons(){
        setupPerkButtons();
        initUnlockButton();
        initSellButton();
        addButtons();
    }

    private void addButtons(){
        addDrawableChild(unlockButton);
        addDrawableChild(instantSellSlotButton);
    }


    private void initTabButton(){
        nextButtonX = buttonsX;
        setupPerkButtons();
    }


    private void initSellButton() {
        instantSellSlotButton = new InstantSellButtonWidget(
                x + 343,
                y + 224,
                25,
                TAB_HEIGHT,
                handler,
                this
        );
    }

    private void initUnlockButton(){
        unlockButton = new CacheAwareButtonWidget(
                x + BACKGROUND_TEXTURE_WIDTH - 62  ,
                y + BACKGROUND_TEXTURE_HEIGHT - 114,
                50,
                TAB_HEIGHT,
                Text.of(sUnlock),
                unlockButtonAction(),
                this
        );
        unlockButton.visible = false;
    }




    private ButtonWidget.PressAction unlockButtonAction(){
        return button -> {
            handler.card.learnTradeSecret(selectedPerk.name());
            //this is prob not needed since we are using cardinal components TODO verify
            ClientPlayNetworking.send(new LearnTradeSecretPayload(selectedPerk.name()));
            unlockButton.visible = false;
        };
    }



    protected void setPerk(PerkButtonWidget newSelectedPerkButton, TradeSecret perk) {
        selectedPerk = perk;
        if (selectedPerkButton != null) {
            selectedPerkButton.isSelected = false;
        }
        newSelectedPerkButton.isSelected = true;
        selectedPerkButton = newSelectedPerkButton;
        unlockButton.visible = handler.card.canUnlockPerk(perk);
    }


    private void renderDetailLine(DrawContext context, int x, int y, String l, String r){
        context.drawTextWithShadow(textRenderer,l, x, y, TEXT_COLOR);
        context.drawTextWithShadow(textRenderer,r, detailsRightX - textRenderer.getWidth(r), y, TEXT_COLOR);
    }

    public void renderDetails(DrawContext context){
        int lastDetailY = detailsY;
        renderDetailLine(context, detailsLeftX, lastDetailY, "", name);
        lastDetailY += TEXT_LINE_HEIGHT + TILE_SIZE;
        renderDetailLine(context, detailsLeftX, lastDetailY, sLevel, level);
        lastDetailY += TEXT_LINE_HEIGHT + TILE_SIZE;
        renderDetailLine(context, detailsLeftX, lastDetailY, sExp, exp);
        lastDetailY += TEXT_LINE_HEIGHT + TILE_SIZE;
        renderDetailLine(context, detailsLeftX, lastDetailY, sCredit, credit);
        lastDetailY += TEXT_LINE_HEIGHT + TILE_SIZE;
        renderDetailLine(context, detailsLeftX, lastDetailY, sPerkPoint, perkPoints);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        renderDetails(context);
        renderSelectedPerkDescription(context);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
    }


    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        background.render(context, x,y);
        context.drawTextWithShadow(textRenderer, title, titleX, titleY, TEXT_COLOR);
    }

    @Override
    protected void drawForeground(DrawContext context, int mouseX, int mouseY) {
    }

//    private void renderTooltip(DrawContext context, int mouseX, int mouseY){
//        if (handler.getCursorStack().isEmpty() && focusedSlot != null && focusedSlot.hasStack()) {
//            super.renderWithTooltip(context, focusedSlot.getStack(), mouseX, mouseY);
//        }
//        hoveredElement(mouseX, mouseY).ifPresent(hovered -> {
//            if (!(hovered instanceof PerkButtonWidget)) return;
//            FishingPerk fishingPerk = ((PerkButtonWidget) hovered).fishingPerk;
//            ArrayList<Text> lines = new ArrayList<>();
//            lines.add(Text.of(labelFormatter + fishingPerk.getLabel()));//Optimize Later TODO
//            lines.addAll(fishingPerk.getDescription());
//            renderTooltip(context, lines, mouseX, mouseY);
//        });
//    }

    private void renderSelectedPerkDescription(DrawContext context){
        if (selectedPerk == null) {
            return;
        }

        int selectedPerkNameY = y + BACKGROUND_TEXTURE_HEIGHT - 107;

                context.drawTextWithShadow(textRenderer,
                labelFormatter + selectedPerk.getLabel(),
                buttonsX,
                selectedPerkNameY,
                TEXT_COLOR
        );

        Text lines = selectedPerk.longDescription(1);
        context.drawTextWithShadow(textRenderer, lines, buttonsX, selectedPerkNameY + 20, TEXT_COLOR);
    }

    private void setupPerkButtons(){
        final int[] idx = {0};
        TradeSecrets.all().forEach(tradeSecret -> {
            setupPerkButton(tradeSecret, idx[0] * 24);
            idx[0]++;
        });
//        for(Path path : TradeSecrets.SKILL_TREE.keySet()) {
//            int treeHeight = 0;
//            for(TradeSecret perk : TradeSecrets.SKILL_TREE.get(path)) {
//                setupPerkButton(perk, treeHeight);
//                treeHeight = treeHeight + 24;
//            }
//        }
    }

    private void setupPerkButton(TradeSecret tradeSecret, int perkY) {
        TradeSecret perkToAdd = tradeSecret;
        int perkX = 0;
        while(perkToAdd != null) {
            PerkButtonWidget perkButtonWidget = new PerkButtonWidget(
                    perksX + perkX,
                    perksY + perkY,
                    handler.card,
                    perkToAdd,
                    this
            );
            perkButtons.add(perkButtonWidget);
            addDrawableChild(perkButtonWidget);
            if (perkToAdd.getChildren().isEmpty()) {
                break;
            }
            perkToAdd = perkToAdd.getChildren().getFirst();
            perkX += 26;
        }
    }



    @Override
    public boolean shouldPause() {
        return false;
    }

}
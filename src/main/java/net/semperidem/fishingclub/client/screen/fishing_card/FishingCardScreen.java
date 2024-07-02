package net.semperidem.fishingclub.client.screen.fishing_card;

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
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;
import net.semperidem.fishingclub.fisher.perks.Path;
import net.semperidem.fishingclub.network.payload.AddPerkPayload;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;

import java.util.ArrayList;

public class FishingCardScreen extends HandledScreen<FishingCardScreenHandler> implements ScreenHandlerProvider<FishingCardScreenHandler>, Cacheable {

    private static final int BACKGROUND_TEXTURE_WIDTH = 400;
    private static final int BACKGROUND_TEXTURE_HEIGHT = 280;

    private static final Texture BACKGROUND_GENERAL = new Texture(FishingClub.getIdentifier("textures/gui/fishing_card_inv.png"), BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
    private static final Texture BACKGROUND_PERK = new Texture(FishingClub.getIdentifier("textures/gui/fishing_card.png"), BACKGROUND_TEXTURE_WIDTH, BACKGROUND_TEXTURE_HEIGHT);
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

    private FishingPerk selectedPerk;
    private PerkButtonWidget selectedPerkButton;

    private MemberButton generalButton;
    private MemberButton hobbyistButton;
    private MemberButton opportunistButton;
    private MemberButton socialistButton;
    private ButtonWidget activeTabButton;
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

    public FishingCardScreen(FishingCardScreenHandler fishingCardScreenHandler, PlayerInventory playerInventory, Text title) {
        super(fishingCardScreenHandler, playerInventory, Text.literal("Fishing Card"));
        updateCache();
    }

    public void updateCache(){
        name = MinecraftClient.getInstance().player.getName().getString();
        level = String.valueOf(handler.fishingCard.getLevel());
        exp = handler.fishingCard.getExp() + "/" + handler.fishingCard.nextLevelXP();
        credit = String.valueOf(handler.fishingCard.getCredit());
        perkPoints = String.valueOf(handler.fishingCard.getPerkPoints());
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
        generalButton.onPress();
    }


    private void initButtons(){
        initTabButton();
        initUnlockButton();
        initSellButton();
        addButtons();
    }

    private void addButtons(){
        addDrawableChild(generalButton);
        addDrawableChild(hobbyistButton);
        addDrawableChild(opportunistButton);
        addDrawableChild(socialistButton);
        addDrawableChild(unlockButton);
        addDrawableChild(instantSellSlotButton);
    }


    private void initTabButton(){
        nextButtonX = buttonsX;
        generalButton = getButtonForTab(Path.GENERAL);
        hobbyistButton = getButtonForTab(Path.HOBBYIST);
        opportunistButton = getButtonForTab(Path.OPPORTUNIST);
        socialistButton = getButtonForTab(Path.SOCIALIST);
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

    private MemberButton getButtonForTab(Path tab) {
        MemberButton tabButton = new MemberButton(
                nextButtonX,
                buttonsY,
                TAB_WIDTH,
                TAB_HEIGHT,
                Text.of(tab.name),
                tabButtonAction(tab)
        );
        tabButton.setTexture(MemberButton.SMALL_WIDE_BUTTON_TEXTURE);
        nextButtonX += TAB_WIDTH;
        return tabButton;
    }

    private ButtonWidget.PressAction tabButtonAction(Path tab) {
        return button -> setTab(button, tab);
    }



    private ButtonWidget.PressAction unlockButtonAction(){
        return button -> {
            handler.fishingCard.addPerk(selectedPerk.getName());
            ClientPlayNetworking.send(new AddPerkPayload(selectedPerk.getName()));
            unlockButton.visible = false;
        };
    }

    private void setTab(ButtonWidget button, Path tab){
        background = tab == Path.GENERAL ? BACKGROUND_GENERAL : BACKGROUND_PERK;
        if (activeTabButton != null) {
            activeTabButton.active = true;
        }
        unlockButton.visible = false;
        selectedPerk = null;
        activeTabButton = button;
        button.active = false;
        handler.setActiveTab(tab);
        togglePerkButtons(tab);
    }

    private void togglePerkButtons(Path tab) {
        for(PerkButtonWidget perkButton : perkButtons) {
            perkButton.visible = perkButton.fishingPerk.getPath() == tab;
        }
    }

    protected void setPerk(PerkButtonWidget newSelectedPerkButton, FishingPerk perk) {
        selectedPerk = perk;
        if (selectedPerkButton != null) {
            selectedPerkButton.isSelected = false;
        }
        newSelectedPerkButton.isSelected = true;
        selectedPerkButton = newSelectedPerkButton;
        unlockButton.visible = handler.fishingCard.canUnlockPerk(perk);
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
       // renderTooltip(context, mouseX, mouseY);
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
        if (selectedPerk == null || activeTabButton == generalButton) {
            return;
        }

        int selectedPerkNameY = y + BACKGROUND_TEXTURE_HEIGHT - 107;

                context.drawTextWithShadow(textRenderer,
                labelFormatter + selectedPerk.getLabel(),
                buttonsX,
                selectedPerkNameY,
                TEXT_COLOR
        );

        ArrayList<Text> lines = selectedPerk.getDetailedDescription();
        int offset = 0;
        for(Text line : lines) {
            context.drawTextWithShadow(textRenderer, line, buttonsX, selectedPerkNameY + 20  + offset, TEXT_COLOR);
            offset += textRenderer.fontHeight + 2;
        }
    }

    private void setupPerkButtons(){
        for(Path path : FishingPerks.SKILL_TREE.keySet()) {
            int treeHeight = 0;
            for(FishingPerk perk : FishingPerks.SKILL_TREE.get(path)) {
                setupPerkButton(perk, treeHeight);
                treeHeight = treeHeight + 24;
            }
        }
    }

    private void setupPerkButton(FishingPerk fishingPerk, int perkY) {
        FishingPerk perkToAdd = fishingPerk;
        int perkX = 0;
        while(perkToAdd != null) {
            PerkButtonWidget perkButtonWidget = new PerkButtonWidget(
                    perksX + perkX,
                    perksY + perkY,
                    handler.fishingCard,
                    perkToAdd,
                    this
            );
            perkButtons.add(perkButtonWidget);
            addDrawableChild(perkButtonWidget);
            perkToAdd = perkToAdd.getChild();
            perkX += 26;
        }
    }



    @Override
    public boolean shouldPause() {
        return false;
    }

}
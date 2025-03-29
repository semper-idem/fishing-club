package net.semperidem.fishingclub.client.screen.fishing_card;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.ingame.ScreenHandlerProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.screen.card.CardScreenHandler;

public class CardScreen extends HandledScreen<CardScreenHandler> implements ScreenHandlerProvider<CardScreenHandler> {
    private static final Identifier TEXTURES = FishingClub.identifier("textures/gui/card.png");

    private Tab currentTab;
    private final StatTab statTab = new StatTab();



    public CardScreen(CardScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
        this.currentTab = statTab;
    }

    @Override
    protected void init() {
        super.init();
        this.currentTab.init();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        this.currentTab.drawBackground(context);
    }

    class StatTab extends Tab{

        @Override
        void drawBackground(DrawContext context) {

            context.drawTexture(TEXTURES,
                    x,
                    y,
                    0,
                    0,
                    176,
                    166,
                    512,
                    1024
                    );
        }
    }

    abstract class Tab {
        String name;

        void init() {
        }

        abstract void drawBackground(DrawContext context);
    }
}



package net.semperidem.fishingclub.registry;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.FishingCardScreen;
import net.semperidem.fishingclub.client.screen.fishing_net.FishingNetScreen;
import net.semperidem.fishingclub.client.screen.fishing_net.FishingNetScreenHandler;
import net.semperidem.fishingclub.client.screen.game.FishGameScreen;
import net.semperidem.fishingclub.client.screen.game.FishGameScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreen;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreen;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreenHandler;
import net.semperidem.fishingclub.screen.FishingCardScreenHandler;

import static net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerExtended;
import static net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerSimple;

public class FScreenHandlerRegistry {
    public static ScreenHandlerType<FisherWorkbenchScreenHandler> FISHER_WORKBENCH_SCREEN_HANDLER;
    public static ScreenHandlerType<FishingCardScreenHandler> FISHING_CARD_SCREEN;
    public static ScreenHandlerType<FishingNetScreenHandler> FISHING_NET_SCREEN_HANDLER;
    public static ScreenHandlerType<ShopScreenHandler> SHOP_SCREEN;
    public static ScreenHandlerType<FishGameScreenHandler> FISH_GAME_SCREEN;

    @SuppressWarnings("deprecation") //TODO FIX DEPRECATION
    public static void register(){
        FISHER_WORKBENCH_SCREEN_HANDLER = registerSimple(FishingClub.getIdentifier("fisher_workbench_gui"), FisherWorkbenchScreenHandler::new);
        FISHING_CARD_SCREEN = registerExtended(FishingClub.getIdentifier("fisher_info_screen"), FishingCardScreenHandler::new);
        FISHING_NET_SCREEN_HANDLER = registerExtended(FishingClub.getIdentifier("fishing_net_screen_handler"), FishingNetScreenHandler::new);
        SHOP_SCREEN = registerExtended(FishingClub.getIdentifier("sell_screen"), ShopScreenHandler::new);
        FISH_GAME_SCREEN = registerExtended(FishingClub.getIdentifier("fish_game_screen"), FishGameScreenHandler::new);
    }

    public static void registerClient(){
        HandledScreens.register(SHOP_SCREEN, ShopScreen::new);
        HandledScreens.register(FISHER_WORKBENCH_SCREEN_HANDLER, FisherWorkbenchScreen::new);
        HandledScreens.register(FISHING_CARD_SCREEN, FishingCardScreen::new);
        HandledScreens.register(FISHING_NET_SCREEN_HANDLER, FishingNetScreen::new);
        HandledScreens.register(FISH_GAME_SCREEN, FishGameScreen::new);
    }
}

package net.semperidem.fishingclub.registry;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.fisher_info.FisherInfoScreen;
import net.semperidem.fishingclub.client.screen.fisher_info.FisherInfoScreenHandler;
import net.semperidem.fishingclub.client.screen.fishing_net.FishingNetScreen;
import net.semperidem.fishingclub.client.screen.fishing_net.FishingNetScreenHandler;
import net.semperidem.fishingclub.client.screen.shop.ShopScreen;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreen;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreenHandler;

import static net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerExtended;
import static net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerSimple;

public class FScreenHandlerRegistry {
    public static ScreenHandlerType<FisherWorkbenchScreenHandler> FISHER_WORKBENCH_SCREEN_HANDLER;
    public static ScreenHandlerType<FisherInfoScreenHandler> FISHER_INFO_SCREEN;
    public static ScreenHandlerType<FishingNetScreenHandler> FISHING_NET_SCREEN_HANDLER;
    public static ScreenHandlerType<ShopScreenHandler> SHOP_SCREEN;

    @SuppressWarnings("deprecation") //TODO FIX ME
    public static void register(){
        FISHER_WORKBENCH_SCREEN_HANDLER = registerSimple(FishingClub.getIdentifier("fisher_workbench_gui"), FisherWorkbenchScreenHandler::new);
        FISHER_INFO_SCREEN = registerExtended(FishingClub.getIdentifier("fisher_info_screen"), FisherInfoScreenHandler::new);
        FISHING_NET_SCREEN_HANDLER = registerExtended(FishingClub.getIdentifier("fishing_net_screen_handler"), FishingNetScreenHandler::new);
        SHOP_SCREEN = registerSimple(FishingClub.getIdentifier("sell_screen"), ShopScreenHandler::new);
    }

    public static void registerClient(){
        HandledScreens.register(SHOP_SCREEN, ShopScreen::new);
        HandledScreens.register(FISHER_WORKBENCH_SCREEN_HANDLER, FisherWorkbenchScreen::new);
        HandledScreens.register(FISHER_INFO_SCREEN, FisherInfoScreen::new);
        HandledScreens.register(FISHING_NET_SCREEN_HANDLER, FishingNetScreen::new);
    }
}

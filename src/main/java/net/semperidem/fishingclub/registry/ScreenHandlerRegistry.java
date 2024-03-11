package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.dialog.DialogScreen;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreen;
import net.semperidem.fishingclub.client.screen.fishing_net.FishingNetScreen;
import net.semperidem.fishingclub.client.screen.fishing_net.FishingNetScreenHandler;
import net.semperidem.fishingclub.client.screen.game.FishingGameScreen;
import net.semperidem.fishingclub.client.screen.member.MemberScreen;
import net.semperidem.fishingclub.client.screen.shop.ShopScreen;
import net.semperidem.fishingclub.client.screen.shop.ShopScreenHandler;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreen;
import net.semperidem.fishingclub.client.screen.workbench.FisherWorkbenchScreenHandler;
import net.semperidem.fishingclub.screen.dialog.DialogScreenHandler;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;
import net.semperidem.fishingclub.screen.fishing_game.FishingGameScreenHandler;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;

import static net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerExtended;
import static net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry.registerSimple;

public class ScreenHandlerRegistry {
    public static ScreenHandlerType<FisherWorkbenchScreenHandler> FISHER_WORKBENCH_SCREEN_HANDLER;
    public static ExtendedScreenHandlerType<FishingCardScreenHandler> FISHING_CARD_SCREEN = new ExtendedScreenHandlerType<>(FishingCardScreenHandler::new);
    public static ScreenHandlerType<FishingNetScreenHandler> FISHING_NET_SCREEN_HANDLER;
    public static ScreenHandlerType<ShopScreenHandler> SHOP_SCREEN;
    public static ScreenHandlerType<FishingGameScreenHandler> FISH_GAME_SCREEN;
    public static ExtendedScreenHandlerType<DialogScreenHandler> DIALOG_SCREEN = new ExtendedScreenHandlerType<>(DialogScreenHandler::new);
    public static ExtendedScreenHandlerType<MemberScreenHandler> MEMBER_SCREEN = new ExtendedScreenHandlerType<>(MemberScreenHandler::new);

    @SuppressWarnings("deprecation") //TODO FIX DEPRECATION
    public static void register(){
        FISHER_WORKBENCH_SCREEN_HANDLER = registerSimple(FishingClub.getIdentifier("fisher_workbench_gui"), FisherWorkbenchScreenHandler::new);

        Registry.register(Registry.SCREEN_HANDLER, FishingClub.getIdentifier("dialog_screen_handler"), DIALOG_SCREEN);
        Registry.register(Registry.SCREEN_HANDLER, FishingClub.getIdentifier("fisher_info_screen_handler"), FISHING_CARD_SCREEN);
        Registry.register(Registry.SCREEN_HANDLER, FishingClub.getIdentifier("member_screen_handler"), MEMBER_SCREEN);
        FISHING_NET_SCREEN_HANDLER = registerExtended(FishingClub.getIdentifier("fishing_net_screen_handler"), FishingNetScreenHandler::new);
        SHOP_SCREEN = registerExtended(FishingClub.getIdentifier("sell_screen"), ShopScreenHandler::new);
        FISH_GAME_SCREEN = registerExtended(FishingClub.getIdentifier("fish_game_screen"), FishingGameScreenHandler::new);
    }

    public static void registerClient(){
        HandledScreens.register(SHOP_SCREEN, ShopScreen::new);
        HandledScreens.register(FISHER_WORKBENCH_SCREEN_HANDLER, FisherWorkbenchScreen::new);
        HandledScreens.register(FISHING_CARD_SCREEN, FishingCardScreen::new);
        HandledScreens.register(DIALOG_SCREEN, DialogScreen::new);
        HandledScreens.register(MEMBER_SCREEN, MemberScreen::new);
        HandledScreens.register(FISHING_NET_SCREEN_HANDLER, FishingNetScreen::new);
        HandledScreens.register(FISH_GAME_SCREEN, FishingGameScreen::new);
    }
}

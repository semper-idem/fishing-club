package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.fishing_card.FishingCardScreen;
import net.semperidem.fishingclub.client.screen.game.FishingGameScreen;
import net.semperidem.fishingclub.client.screen.member.MemberScreen;
import net.semperidem.fishingclub.network.payload.DialogPayload;
import net.semperidem.fishingclub.network.payload.FishingCardPayload;
import net.semperidem.fishingclub.network.payload.FishingGamePayload;
import net.semperidem.fishingclub.client.screen.dialog.DialogScreen;
import net.semperidem.fishingclub.network.payload.MemberPayload;
import net.semperidem.fishingclub.screen.dialog.DialogScreenHandler;
import net.semperidem.fishingclub.screen.fishing_card.FishingCardScreenHandler;
import net.semperidem.fishingclub.screen.fishing_game.FishingGameScreenHandler;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;

public class ScreenHandlerRegistry {
    public static ExtendedScreenHandlerType<FishingCardScreenHandler, FishingCardPayload> FISHING_CARD_SCREEN;
    public static ExtendedScreenHandlerType<FishingGameScreenHandler, FishingGamePayload> FISHING_GAME_SCREEN;
    public static ExtendedScreenHandlerType<DialogScreenHandler, DialogPayload> DIALOG_SCREEN;
    public static ExtendedScreenHandlerType<MemberScreenHandler, MemberPayload> MEMBER_SCREEN;
//    public static ExtendedScreenHandlerType<LeaderboardScreenHandler> LEADERBOARD_SCREEN = new
//            ExtendedScreenHandlerType<>(LeaderboardScreenHandler::new);
//    public static ScreenHandlerType<FishingNetScreenHandler> FISHING_NET_SCREEN_HANDLER; =new
//            ExtendedScreenHandlerType<>(FishingNetScreenHandler::new);

    public static void register() {
        FISHING_CARD_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.getIdentifier("fishing_card_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                FishingCardScreenHandler::new, FishingCardPayload.CODEC));

        FISHING_GAME_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.getIdentifier("fishing_game_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                FishingGameScreenHandler::new, FishingGamePayload.CODEC));
        DIALOG_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.getIdentifier("dialog_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                DialogScreenHandler::new, DialogPayload.CODEC));
        MEMBER_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.getIdentifier("member_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                MemberScreenHandler::new, MemberPayload.CODEC));

//        Registry.register(Registries.SCREEN_HANDLER,
//                FishingClub.getIdentifier("member_screen_handler"), MEMBER_SCREEN);
//        Registry.register(Registries.SCREEN_HANDLER,
//                FishingClub.getIdentifier("fishing_net_screen_handler"), FISHING_NET_SCREEN_HANDLER);
    }

    public static void registerClient() {
        HandledScreens.register(FISHING_GAME_SCREEN, FishingGameScreen::new);
        HandledScreens.register(FISHING_CARD_SCREEN, FishingCardScreen::new);
        HandledScreens.register(DIALOG_SCREEN, DialogScreen::new);
        HandledScreens.register(MEMBER_SCREEN, MemberScreen::new);
//        HandledScreens.register(LEADERBOARD_SCREEN, LeaderboardScreen::new);
//        HandledScreens.register(MEMBER_SCREEN, MemberScreen::new);
//        HandledScreens.register(FISHING_NET_SCREEN_HANDLER, FishingNetScreen::new);
    }
}

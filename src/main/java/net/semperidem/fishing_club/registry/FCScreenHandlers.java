package net.semperidem.fishing_club.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.semperidem.fishing_club.FishingClub;
import net.semperidem.fishing_club.client.screen.configuration.ConfigurationScreen;
import net.semperidem.fishing_club.client.screen.fishing_card.FishingCardScreen;
import net.semperidem.fishing_club.client.screen.game.FishingGameScreen;
import net.semperidem.fishing_club.client.screen.member.MemberScreen;
import net.semperidem.fishing_club.network.payload.*;
import net.semperidem.fishing_club.client.screen.dialog.DialogScreen;
import net.semperidem.fishing_club.screen.configuration.ConfigurationScreenHandler;
import net.semperidem.fishing_club.screen.dialog.DialogScreenHandler;
import net.semperidem.fishing_club.screen.fishing_card.FishingCardScreenHandler;
import net.semperidem.fishing_club.screen.fishing_game.FishingGameScreenHandler;
import net.semperidem.fishing_club.screen.member.MemberScreenHandler;

public class FCScreenHandlers {
    public static ExtendedScreenHandlerType<FishingCardScreenHandler, FishingCardPayload> FISHING_CARD_SCREEN;
    public static ExtendedScreenHandlerType<FishingGameScreenHandler, FishingGamePayload> FISHING_GAME_SCREEN;
    public static ExtendedScreenHandlerType<DialogScreenHandler, DialogPayload> DIALOG_SCREEN;
    public static ExtendedScreenHandlerType<MemberScreenHandler, MemberPayload> MEMBER_SCREEN;
    public static ExtendedScreenHandlerType<ConfigurationScreenHandler, ConfigurationPayload> CONFIGURATION_SCREEN;

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
        CONFIGURATION_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.getIdentifier("configuration_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                ConfigurationScreenHandler::new, ConfigurationPayload.CODEC));
    }

    public static void registerClient() {
        HandledScreens.register(FISHING_GAME_SCREEN, FishingGameScreen::new);
        HandledScreens.register(FISHING_CARD_SCREEN, FishingCardScreen::new);
        HandledScreens.register(DIALOG_SCREEN, DialogScreen::new);
        HandledScreens.register(MEMBER_SCREEN, MemberScreen::new);
        HandledScreens.register(CONFIGURATION_SCREEN, ConfigurationScreen::new);
    }
}

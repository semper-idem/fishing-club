package net.semperidem.fishingclub.registry;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.semperidem.fishingclub.FishingClub;
import net.semperidem.fishingclub.client.screen.configuration.ConfigurationScreen;
import net.semperidem.fishingclub.client.screen.game.FishingPostScreen;
import net.semperidem.fishingclub.client.screen.game.FishingScreen;
import net.semperidem.fishingclub.client.screen.member.MemberScreen;
import net.semperidem.fishingclub.network.payload.*;
import net.semperidem.fishingclub.screen.configuration.ConfigurationScreenHandler;
import net.semperidem.fishingclub.screen.fishing.FishingGameScreenHandler;
import net.semperidem.fishingclub.screen.fishing_post.FishingPostScreenHandler;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;

public class ScreenHandlers {
    public static ExtendedScreenHandlerType<CardScreenHandler, CardPayload> FISHING_CARD_SCREEN;
    public static ExtendedScreenHandlerType<FishingGameScreenHandler, FishingGameStartS2CPayload> FISHING_GAME_SCREEN;
    public static ExtendedScreenHandlerType<FishingPostScreenHandler, FishingGamePostS2CPayload> FISHING_GAME_POST_SCREEN;
    public static ExtendedScreenHandlerType<MemberScreenHandler, MemberPayload> MEMBER_SCREEN;
    public static ExtendedScreenHandlerType<ConfigurationScreenHandler, ConfigurationPayload> CONFIGURATION_SCREEN;

    public static void register() {
        FISHING_CARD_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.identifier("fishing_card_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                CardScreenHandler::new, CardPayload.CODEC));

        FISHING_GAME_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.identifier("fishing_game_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                FishingGameScreenHandler::new, FishingGameStartS2CPayload.CODEC));

        FISHING_GAME_POST_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.identifier("fishing_game_post_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                FishingPostScreenHandler::new, FishingGamePostS2CPayload.CODEC));

        MEMBER_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.identifier("member_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                MemberScreenHandler::new, MemberPayload.CODEC));
        CONFIGURATION_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.identifier("configuration_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                ConfigurationScreenHandler::new, ConfigurationPayload.CODEC));
    }

    public static void registerClient() {
        HandledScreens.register(FISHING_GAME_SCREEN, FishingScreen::new);
        HandledScreens.register(FISHING_GAME_POST_SCREEN, FishingPostScreen::new);
        HandledScreens.register(FISHING_CARD_SCREEN, FishingCardScreen::new);
        HandledScreens.register(MEMBER_SCREEN, MemberScreen::new);
        HandledScreens.register(CONFIGURATION_SCREEN, ConfigurationScreen::new);
    }
}

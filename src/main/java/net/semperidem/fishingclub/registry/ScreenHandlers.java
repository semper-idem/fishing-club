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
import net.semperidem.fishingclub.screen.fishing.FishingScreenHandler;
import net.semperidem.fishingclub.screen.fishing_post.FishingPostScreenHandler;
import net.semperidem.fishingclub.screen.member.MemberScreenHandler;

public class ScreenHandlers {
    public static ExtendedScreenHandlerType<CardScreenHandler, CardPayload> CARD;
    public static ExtendedScreenHandlerType<FishingScreenHandler, FishingStartS2CPayload> FISHING_SCREEN;
    public static ExtendedScreenHandlerType<FishingPostScreenHandler, FishingPostPayload> FISHING_GAME_POST_SCREEN;
    public static ExtendedScreenHandlerType<MemberScreenHandler, MemberPayload> MEMBER_SCREEN;
    public static ExtendedScreenHandlerType<ConfigurationScreenHandler, ConfigurationPayload> CONFIGURATION_SCREEN;

    public static void register() {
        CARD =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.identifier("fishing_card_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                CardScreenHandler::new, CardPayload.CODEC));

        FISHING_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.identifier("fishing_game_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                FishingScreenHandler::new, FishingStartS2CPayload.CODEC));

        FISHING_GAME_POST_SCREEN =
                Registry.register(
                        Registries.SCREEN_HANDLER,
                        FishingClub.identifier("fishing_game_post_screen_handler"),
                        new ExtendedScreenHandlerType<>(
                                FishingPostScreenHandler::new, FishingPostPayload.CODEC));

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
        HandledScreens.register(FISHING_SCREEN, FishingScreen::new);
        HandledScreens.register(FISHING_GAME_POST_SCREEN, FishingPostScreen::new);
        HandledScreens.register(CARD, FishingCardScreen::new);
        HandledScreens.register(MEMBER_SCREEN, MemberScreen::new);
        HandledScreens.register(CONFIGURATION_SCREEN, ConfigurationScreen::new);
    }
}

package net.semperidem.fishing_club.client;

import net.fabricmc.api.ClientModInitializer;
import net.semperidem.fishing_club.registry.FCRegistry;

public class FishingClubClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FCRegistry.registerClient();
    }
}

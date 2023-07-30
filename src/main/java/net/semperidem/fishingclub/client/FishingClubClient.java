package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.semperidem.fishingclub.registry.FRegistry;

public class FishingClubClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        FRegistry.registerClient();
    }
}

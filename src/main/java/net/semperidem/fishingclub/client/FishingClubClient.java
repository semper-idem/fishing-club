package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.semperidem.fishingclub.fisher.FisherInfo;
import net.semperidem.fishingclub.registry.FRegistry;

public class FishingClubClient implements ClientModInitializer {
    public static final FisherInfo CLIENT_INFO = new FisherInfo();
    @Override
    public void onInitializeClient() {
        FRegistry.registerClient();
    }
}

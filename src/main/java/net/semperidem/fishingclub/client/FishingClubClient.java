package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.FRegistry;

public class FishingClubClient implements ClientModInitializer {
    public static final FishingCard CLIENT_INFO = FishingCard.getClientCard();

    @Override
    public void onInitializeClient() {
        FRegistry.registerClient();
    }
}

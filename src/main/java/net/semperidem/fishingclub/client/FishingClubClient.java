package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.semperidem.fishingclub.fisher.FishingCard;
import net.semperidem.fishingclub.registry.FRegistry;

public class FishingClubClient implements ClientModInitializer {
    private static FishingCard clientInfoInstance;

    public static FishingCard getClientCard() {
        if (clientInfoInstance == null) {
            clientInfoInstance = FishingCard.getClientCard();
        }
        return clientInfoInstance;
    }

    @Override
    public void onInitializeClient() {
        FRegistry.registerClient();
    }
}

package net.semperidem.fishingclub;

import net.semperidem.fishingclub.fish.Fish;
import net.semperidem.fishingclub.fisher.FishingCard;

import java.util.HashMap;
import java.util.UUID;

public class FishingDatabase {
    private static final HashMap<UUID, Fish> FISHER_TO_LAST_CATCH_MAP = new HashMap<>();
    private static final HashMap<UUID, FishingCard> FISHER_TO_CARD_MAP = new HashMap<>();

    public static void putCatch(UUID fisherUUID, Fish fish) {
        FISHER_TO_LAST_CATCH_MAP.put(fisherUUID, fish);
    }

    public static Fish getLastCatch(UUID fisherUUID){
        return FISHER_TO_LAST_CATCH_MAP.get(fisherUUID);
    }

    public static void putCard(UUID fisherUUID, FishingCard fishingCard) {
        FISHER_TO_CARD_MAP.put(fisherUUID, fishingCard);
    }

    public static FishingCard getCard(UUID fisherUUID){
        if (!FISHER_TO_CARD_MAP.containsKey(fisherUUID)) {
            return FishingCard.EMPTY;
        }
        return FISHER_TO_CARD_MAP.get(fisherUUID);
    }
}

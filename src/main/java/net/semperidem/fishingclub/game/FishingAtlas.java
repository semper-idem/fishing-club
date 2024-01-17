package net.semperidem.fishingclub.game;

import net.semperidem.fishingclub.fish.Fish;

import java.util.HashMap;
import java.util.UUID;

public class FishingAtlas {
    private static final HashMap<UUID, Fish> FISHER_TO_LAST_CATCH_MAP = new HashMap<>();

    public static void putCatch(UUID fisherUUID, Fish fish) {
        FISHER_TO_LAST_CATCH_MAP.put(fisherUUID, fish);
    }

    public static Fish getLastCatch(UUID fisherUUID){
        return FISHER_TO_LAST_CATCH_MAP.get(fisherUUID);
    }
}

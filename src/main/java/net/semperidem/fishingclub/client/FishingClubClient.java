package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.item.fishing_rod.components.FishingRodConfiguration;
import net.semperidem.fishingclub.registry.FishingClubRegistry;

import java.util.HashMap;
import java.util.UUID;

public class FishingClubClient implements ClientModInitializer {
    public static UUID FISHING_KING_UUID;
    private static HashMap<FishingPerk, SpellInstance> AVAILABLE_SPELLS = new HashMap<>();
    private static HashMap<String, FishingPerk> PERKS = new HashMap<>();

    public static HashMap<FishingPerk, SpellInstance> getAvailableSpells() {
        return AVAILABLE_SPELLS;
    }

    public static void updateAvailableSpells(HashMap<FishingPerk, SpellInstance> availableSpells) {
        AVAILABLE_SPELLS = availableSpells;
    }
    public static HashMap<String, FishingPerk> getPerks() {
        return PERKS;
    }

    public static void updatePerks(HashMap<String, FishingPerk> perksMap) {
        PERKS = perksMap;
    }

    @Override
    public void onInitializeClient() {
        FishingClubRegistry.registerClient();
    }
}

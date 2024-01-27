package net.semperidem.fishingclub.client;

import net.fabricmc.api.ClientModInitializer;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.spells.SpellInstance;
import net.semperidem.fishingclub.registry.FRegistry;

import java.util.HashMap;

public class FishingClubClient implements ClientModInitializer {
    private static HashMap<FishingPerk, SpellInstance> AVAILABLE_SPELLS;
    private static HashMap<String, FishingPerk> PERKS;

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
        FRegistry.registerClient();
    }
}

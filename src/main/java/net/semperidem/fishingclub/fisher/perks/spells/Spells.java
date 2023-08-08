package net.semperidem.fishingclub.fisher.perks.spells;

import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;

import java.util.HashMap;

public class Spells {
    static Spell SUMMON_RAIN;
    static HashMap<FishingPerk, Spell> perkToSpell = new HashMap<>();

    static {
        SUMMON_RAIN = new Spell("summon_rain", FishingPerks.RAIN_SUMMON, 72000, playerEntity -> playerEntity.world.setRainGradient(1));
    }

    public static Spell getSpellFromPerk(FishingPerk fishingPerk){
        if (!perkHasSpell(fishingPerk)) return null;
        return perkToSpell.get(fishingPerk);
    }

    public static boolean perkHasSpell(FishingPerk fishingPerk){
        return perkToSpell.containsKey(fishingPerk);
    }
}

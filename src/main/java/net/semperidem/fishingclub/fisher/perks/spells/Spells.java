package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;

import java.util.HashMap;

public class Spells {
    static Spell SUMMON_RAIN;
    static Spell FISHING_SCHOOL;
    static HashMap<FishingPerk, Spell> perkToSpell = new HashMap<>();

    static {
        SUMMON_RAIN = new Spell(FishingPerks.RAIN_SUMMON.getName(), FishingPerks.RAIN_SUMMON, 72000, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity)) return;
            ((ServerWorld)playerEntity.world).setWeather(0, 6000, true, Math.random() < 0.1f);
        });
        FISHING_SCHOOL = new Spell(FishingPerks.FISHING_SCHOOL.getName(), FishingPerks.FISHING_SCHOOL, 72000, playerEntity -> {
            if (!(playerEntity instanceof ServerPlayerEntity)) return;
            playerEntity.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 100));
        });
    }

    public static Spell getSpellFromPerk(FishingPerk fishingPerk){
        if (!perkHasSpell(fishingPerk)) return null;
        return perkToSpell.get(fishingPerk);
    }

    public static boolean perkHasSpell(FishingPerk fishingPerk){
        return perkToSpell.containsKey(fishingPerk);
    }
}

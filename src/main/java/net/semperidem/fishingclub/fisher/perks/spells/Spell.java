package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;

public class Spell {
    final String name;
    final int cooldown;
    final FishingPerk requiredPerk;
    Effect effect;


    public Spell(String name, FishingPerk fishingPerk, int cooldown, Effect effect){
        this.name = name;
        this.requiredPerk = fishingPerk;
        this.cooldown = cooldown;
        this.effect = effect;
        Spells.perkToSpell.put(fishingPerk, this);
    }

    interface Effect{
        void cast(PlayerEntity playerEntity);
    }
}

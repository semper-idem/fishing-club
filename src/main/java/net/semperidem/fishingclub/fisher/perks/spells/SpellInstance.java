package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;

public class SpellInstance {
    final Spell spell;
    long nextAvailableCastTime;

    private SpellInstance(FishingPerk fishingPerk,long nextCast){
        this.spell = Spells.getSpellFromPerk(fishingPerk);
        this.nextAvailableCastTime = nextCast;
    }

    public static SpellInstance getSpellInstance(FishingPerk fishingPerk, long nextCast){
        if (!Spells.perkHasSpell(fishingPerk)) return null;
        return new SpellInstance(fishingPerk, nextCast);
    }

    public void use(PlayerEntity playerEntity){
        long worldTime = playerEntity.world.getTime();
        if (worldTime < nextAvailableCastTime) return;
        this.nextAvailableCastTime = worldTime + spell.cooldown;
        spell.effect.cast(playerEntity);
    }

    public String getKey(){
        return spell.requiredPerk.getName();
    }

    public long getNextCast(){
        return this.nextAvailableCastTime;
    }
}

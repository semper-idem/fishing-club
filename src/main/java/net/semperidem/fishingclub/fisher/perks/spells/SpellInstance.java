package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.player.PlayerEntity;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;

public class SpellInstance {
    final Spell spell;
    int cooldown;
    long nextPossibleCastTime;


    private SpellInstance(FishingPerk fishingPerk,int cooldown, long nextPossibleCastTime){
        this.spell = Spells.getSpellFromPerk(fishingPerk);
        this.cooldown = cooldown;
        this.nextPossibleCastTime = nextPossibleCastTime;
    }

    public static SpellInstance getSpellInstance(FishingPerk fishingPerk,int cooldown, long nextPossibleCastTime){
        if (!Spells.perkHasSpell(fishingPerk)) return null;
        return new SpellInstance(fishingPerk, cooldown, nextPossibleCastTime);
    }

    public void tick(){
        if (this.cooldown == 0) return;
        this.cooldown--;
    }

    public void use(PlayerEntity playerEntity){
        if (cooldown > 0) return;
        this.cooldown = spell.cooldown;
        this.nextPossibleCastTime = playerEntity.world.getTime() + cooldown;
        spell.effect.cast(playerEntity);
    }

    public String getKey(){
        return spell.requiredPerk.getName();
    }

    public long getNextCast(){
        return nextPossibleCastTime;
    }
    public int getCooldown(){
        return cooldown;
    }
}

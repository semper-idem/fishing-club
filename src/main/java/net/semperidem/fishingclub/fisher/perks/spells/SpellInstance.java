package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
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

    public boolean needsTarget(){
        return spell.needsTarget;
    }

    public void use(ServerPlayerEntity playerEntity, Entity target){
        if (cooldown > 0) return;
        this.cooldown = spell.cooldown;
        this.nextPossibleCastTime = playerEntity.world.getTime() + cooldown;
        if (spell.needsTarget) {
            spell.effect.targetedCast(playerEntity, target);
        } else {
            spell.effect.cast(playerEntity);
        }
    }

    public String getKey(){
        return spell.requiredPerk.getName();
    }

    public String getLabel(){
        return spell.requiredPerk.getLabel();
    }

    public long getNextCast(){
        return nextPossibleCastTime;
    }
    public int getCooldown(){
        return cooldown;
    }
}

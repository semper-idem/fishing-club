package net.semperidem.fishing_club.fisher.perks.spells;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishing_club.fisher.perks.FishingPerk;
import net.semperidem.fishing_club.fisher.perks.FishingPerks;

public class SpellInstance {
    final Spell spell;
    long nextPossibleCastTime;

    private static final String KEY_TAG = "k";
    private static final String NEXT_CAST_TAG = "next";


    private SpellInstance(FishingPerk fishingPerk, long nextPossibleCastTime){
        this.spell = Spells.getSpellFromPerk(fishingPerk);
        this.nextPossibleCastTime = nextPossibleCastTime;
    }

    public static SpellInstance getSpellInstance(FishingPerk fishingPerk, long nextPossibleCastTime){
        if (!Spells.perkHasSpell(fishingPerk)) return null;
        return new SpellInstance(fishingPerk, nextPossibleCastTime);
    }

    public void resetCooldown(){
        this.nextPossibleCastTime = 0;
    }

    public boolean needsTarget(){
        return spell.needsTarget;
    }

    public void use(ServerPlayerEntity playerEntity, Entity target){
        long currentTime = playerEntity.getServerWorld().getTime();
        if (currentTime <= nextPossibleCastTime) return;
        this.nextPossibleCastTime = playerEntity.getServerWorld().getTime() + spell.cooldown;
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

    public FishingPerk getPerk() {
        return spell.requiredPerk;
    }

    public static SpellInstance fromNbt(NbtCompound nbt) {
        String perkName = nbt.getString(KEY_TAG);
        return new SpellInstance(FishingPerks.getPerkFromName(perkName), nbt.getLong(NEXT_CAST_TAG));
    }

    public static NbtCompound toNbt(SpellInstance spellInstance) {
        NbtCompound spellTag = new NbtCompound();
        spellTag.putString(KEY_TAG, spellInstance.getKey());
        spellTag.putLong(NEXT_CAST_TAG, spellInstance.nextPossibleCastTime);
        return spellTag;
    }
}

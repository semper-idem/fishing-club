package net.semperidem.fishingclub.fisher.perks.spells;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.semperidem.fishingclub.fisher.perks.FishingPerk;
import net.semperidem.fishingclub.fisher.perks.FishingPerks;

public class SpellInstance {
    final Spell spell;
    int cooldown;
    long nextPossibleCastTime;

    private static final String KEY_TAG = "k";
    private static final String COOLDOWN_TAG = "cd";
    private static final String NEXT_CAST_TAG = "next";


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

    public void resetCooldown(){
        this.cooldown = 0;
        this.nextPossibleCastTime = 0;
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

    public FishingPerk getPerk() {
        return spell.requiredPerk;
    }

    public static SpellInstance fromNbt(NbtCompound nbt) {
        String perkName = nbt.getString(KEY_TAG);
        return new SpellInstance(
                FishingPerks.getPerkFromName(perkName).orElse(FishingPerks.FREE_SHOP_SUMMON),
                nbt.getInt(COOLDOWN_TAG),
                nbt.getLong(NEXT_CAST_TAG)
        );
    }

    public static NbtCompound toNbt(SpellInstance spellInstance) {
        NbtCompound spellTag = new NbtCompound();
        spellTag.putString(KEY_TAG, spellInstance.getKey());
        spellTag.putInt(COOLDOWN_TAG, spellInstance.cooldown);
        spellTag.putLong(NEXT_CAST_TAG, spellInstance.nextPossibleCastTime);
        return spellTag;
    }
}
